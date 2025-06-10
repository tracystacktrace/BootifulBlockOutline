package net.tracystacktrace.bootifulblockoutline.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.common.util.i18n.StringTranslate;

public class GuiHomeConfig extends GuiScreen {

    protected final String title;

    public GuiHomeConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.title = StringTranslate.getInstance().translateKey("bootifulblockoutline.config.title");
    }

    @Override
    public void initGui() {
        this.controlList.clear();

        final int offsetX = this.width / 2 - 125;
        final int offsetY = this.height / 2 - 46;

        final StringTranslate translate = StringTranslate.getInstance();

        this.controlList.add(new GuiButton(0, offsetX, offsetY + 11, 160, 20, "A"));
        this.controlList.add(new GuiButton(1, offsetX + 165, offsetY + 11, 40, 20, translate.translateKey("bootifulblockoutline.button.argb")));
        this.controlList.add(new GuiButton(2, offsetX + 210, offsetY + 11, 40, 20, translate.translateKey("bootifulblockoutline.button.width")));

        this.controlList.add(new GuiButton(3, offsetX, offsetY + 11 + 25, 160, 20, "B"));
        this.controlList.add(new GuiButton(4, offsetX + 165, offsetY + 11 + 25, 40, 20, translate.translateKey("bootifulblockoutline.button.argb")));
        this.controlList.add(new GuiButton(5, offsetX + 210, offsetY + 11 + 25, 40, 20, translate.translateKey("bootifulblockoutline.button.width")));

        this.controlList.add(new GuiButton(6, this.width / 2 - 45, offsetY + 11 + 55, 90, 20, translate.translateKey("gui.done")));

        ((GuiButton)this.controlList.get(1)).canDisplayInfo = true;
        ((GuiButton)this.controlList.get(2)).canDisplayInfo = true;
        ((GuiButton)this.controlList.get(4)).canDisplayInfo = true;
        ((GuiButton)this.controlList.get(5)).canDisplayInfo = true;
    }


    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        final int offsetY = this.height / 2 - 48;

        this.drawCenteredString(fontRenderer, this.title, this.width / 2, offsetY, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }
}
