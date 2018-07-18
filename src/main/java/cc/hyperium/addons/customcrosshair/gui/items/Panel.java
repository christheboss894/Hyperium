package cc.hyperium.addons.customcrosshair.gui.items;

import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import cc.hyperium.addons.customcrosshair.utils.RGBA;
import net.minecraft.client.gui.GuiScreen;

public class Panel extends GuiItem {
    private int contentWidth;
    private int contentHeight;
    private boolean verticalScroll;
    private int verticalScrollPosition;
    private boolean verticalScrollMouseDown;
    private boolean horizontalScroll;
    private int horizontalScrollPosition;
    private boolean horizontalScrollMouseDown;
    private int scrollSize;
    private int boxSize;

    public Panel(final GuiScreen screen, final int id, final int x, final int y, final int width, final int height, final int cWidth, final int cHeight) {
        super(screen, id, "", x, y, width, height);
        this.contentWidth = cWidth;
        this.contentHeight = cHeight;
        if (this.contentHeight > this.getHeight()) {
            this.verticalScroll = true;
        }
        if (this.contentWidth > this.getWidth()) {
            this.horizontalScroll = true;
        }
        this.scrollSize = 10;
        this.boxSize = 30;
    }

    @Override
    public void drawItem(final int mouseX, final int mouseY) {
        GuiGraphics.drawBorderedRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), new RGBA(255, 255, 255, 32), GuiTheme.SECONDARY);
        if (this.verticalScrollMouseDown) {
            this.verticalScrollPosition = mouseY - this.getPosY();
            if (this.verticalScrollPosition < 0) {
                this.verticalScrollPosition = 0;
            }
            if (this.verticalScrollPosition > this.getHeight() - this.scrollSize - this.boxSize) {
                this.verticalScrollPosition = this.getHeight() - this.scrollSize - this.boxSize;
            }
        }
        if (this.verticalScroll) {
            GuiGraphics.drawThemeBorderedRectangle(this.getPosX() + this.getWidth() - this.scrollSize, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - (this.horizontalScroll ? this.scrollSize : 0));
            GuiGraphics.drawThemeBorderedRectangle(this.getPosX() + this.getWidth() - this.scrollSize, this.getPosY() + this.verticalScrollPosition, this.getPosX() + this.getWidth(), this.getPosY() + this.verticalScrollPosition + this.boxSize);
        }
        if (this.horizontalScroll) {
            GuiGraphics.drawThemeBorderedRectangle(this.getPosX(), this.getPosY() + this.getHeight() - this.scrollSize, this.getPosX() + this.getWidth() - (this.verticalScroll ? this.scrollSize : 0), this.getPosY() + this.getHeight());
            GuiGraphics.drawThemeBorderedRectangle(this.getPosX() + this.horizontalScrollPosition, this.getPosY() + this.getHeight() - this.scrollSize, this.getPosX() + this.horizontalScrollPosition + this.boxSize, this.getPosY() + this.getHeight());
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if (this.verticalScroll && mouseX >= this.getPosX() + this.getWidth() - this.scrollSize && mouseX < this.getPosX() + this.getWidth() && mouseY >= this.getPosY() + this.verticalScrollPosition && mouseY <= this.getPosY() + this.verticalScrollPosition + this.boxSize) {
            this.verticalScrollMouseDown = true;
        }
        if (this.horizontalScroll && mouseX >= this.getPosX() + this.horizontalScrollPosition && mouseX <= this.getPosX() + this.horizontalScrollPosition + this.boxSize && mouseY >= this.getPosY() + this.getHeight() - this.scrollSize && mouseY <= this.getPosY() + this.getHeight()) {
            this.horizontalScrollMouseDown = true;
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.verticalScrollMouseDown = false;
        this.horizontalScrollMouseDown = false;
    }
}

