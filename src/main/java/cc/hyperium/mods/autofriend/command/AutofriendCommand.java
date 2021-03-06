package cc.hyperium.mods.autofriend.command;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.autofriend.AutoFriendUtils;
import cc.hyperium.mods.autofriend.AutofriendMod;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Pattern;

public class AutofriendCommand implements BaseCommand {

    private Pattern username;
    private AutofriendMod mod;
    private Minecraft mc = Minecraft.getMinecraft();

    public AutofriendCommand() {
        this.username = Pattern.compile("\\w{1,16}");
    }

    public void throwError(final String error) {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Error: " + error));
    }

    public void sendMessage(final String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public String getName() {
        return "autofriend";
    }

    @Override
    public String getUsage() {
        return "/autofriend blacklist add/remove <username>";
    }

    @Override
    public void onExecute(String[] args) {
        if (args[0].equals("blacklist")) {
            if (args.length == 1 || (!args[1].equals("add") && !args[1].equals("remove"))) {
                if (AutofriendMod.blacklist.size() > 0) {
                    int page = 1;
                    final int pages = (int) Math.ceil(AutofriendMod.blacklist.size() / 7.0);
                    if (args.length > 1) {
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException ignored) {
                            page = -1;
                        }
                    }
                    if (page < 1 || page > pages) {
                        this.throwError("Invalid page number");
                    } else {
                        this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                        this.sendMessage(EnumChatFormatting.BLUE + "Blacklist " + EnumChatFormatting.DARK_AQUA + "(Page " + page + " of " + pages + ")");
                        AutofriendMod.blacklist.stream().skip((page - 1) * 7).limit(7L).forEach(name -> this.sendMessage(AutoFriendUtils.of(EnumChatFormatting.BLUE + name + EnumChatFormatting.GRAY + " - ").append("[REMOVE]").setColor(EnumChatFormatting.RED).setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, AutoFriendUtils.of("Remove " + name).setColor(EnumChatFormatting.RED).build()).append(" - ").setColor(EnumChatFormatting.GRAY).append("[UNBLACKLIST]").setColor(EnumChatFormatting.GREEN).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/autofriend blacklist remove " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, AutoFriendUtils.of("Blacklist " + name).setColor(EnumChatFormatting.RED).build()).build().toString()));
                        this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                    }
                } else {
                    this.throwError("You haven't blacklisted anyone");
                }
            } else if (args[1].equals("add") || args[1].equals("remove")) {
                if (args.length > 2 && this.username.matcher(args[2]).matches()) {
                    if (args[1].equals("add")) {
                        AutofriendMod.blacklist.add(args[2]);
                    } else {
                        AutofriendMod.blacklist.remove(args[2]);
                    }
                    this.sendMessage(EnumChatFormatting.BLUE + (args[1].equals("add") ? ("Added " + EnumChatFormatting.RED + args[2] + EnumChatFormatting.BLUE + " to") : ("Removed " + EnumChatFormatting.GREEN + args[2] + EnumChatFormatting.BLUE + " from")) + " your blacklist.");
                    AutofriendMod.writeBlacklist();
                } else {
                    this.throwError("Invalid username. Usage: /autofriend blacklist add/remove <username>");
                }
            }
        } else {
            this.throwError("Unknown usage! Usage: /autofriend blacklist add/remove <username>");
        }
    }
}
