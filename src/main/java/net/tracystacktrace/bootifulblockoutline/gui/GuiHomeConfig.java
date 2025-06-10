package net.tracystacktrace.bootifulblockoutline.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiHomeConfig extends GuiScreen {

    public GuiHomeConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.controlList.clear();
    }
}
