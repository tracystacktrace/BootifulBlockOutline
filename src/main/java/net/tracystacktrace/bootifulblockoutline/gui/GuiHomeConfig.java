package net.tracystacktrace.bootifulblockoutline.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import net.tracystacktrace.bootifulblockoutline.gui.element.GuiButtonMultipleChoice;

public class GuiHomeConfig extends GuiScreen {

    protected final String title;

    protected GuiButtonMultipleChoice modeBlockOutline;
    protected GuiButtonMultipleChoice modeEntityOutline;

    public GuiHomeConfig(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.title = StringTranslate.getInstance().translateKey("bootifulblockoutline.config.title");
    }

    @Override
    public void initGui() {
        this.controlList.clear();

        final int offsetX = this.width / 2 - 125;
        final int offsetY = this.height / 2 - 45;

        final StringTranslate translate = StringTranslate.getInstance();

        //block outline management
        this.controlList.add(this.modeBlockOutline = new GuiButtonMultipleChoice(
                0, offsetX, offsetY + 11, 160, 20,
                translate.translateKey("bootifulblockoutline.config.block"),
                new String[] {
                        translate.translateKey("bootifulblockoutline.disabled"),
                        translate.translateKey("bootifulblockoutline.static"),
                        translate.translateKey("bootifulblockoutline.rainbow")
                }
        ));
        this.controlList.add(new GuiButton(2, offsetX + 210, offsetY + 11, 40, 20, translate.translateKey("bootifulblockoutline.button.width")));
        this.controlList.add(new GuiButton(1, offsetX + 165, offsetY + 11, 40, 20, translate.translateKey("bootifulblockoutline.button.argb")));

        //entity outline management
        this.controlList.add(this.modeEntityOutline = new GuiButtonMultipleChoice(
                3, offsetX, offsetY + 11 + 25, 160, 20,
                translate.translateKey("bootifulblockoutline.config.block"),
                new String[] {
                        translate.translateKey("bootifulblockoutline.disabled"),
                        translate.translateKey("bootifulblockoutline.static"),
                        translate.translateKey("bootifulblockoutline.rainbow"),
                }
        ));
        this.controlList.add(new GuiButton(5, offsetX + 210, offsetY + 11 + 25, 40, 20, translate.translateKey("bootifulblockoutline.button.width")));
        this.controlList.add(new GuiButton(4, offsetX + 165, offsetY + 11 + 25, 40, 20, translate.translateKey("bootifulblockoutline.button.argb")));

        this.controlList.add(new GuiButton(6, this.width / 2 - 45, offsetY + 11 + 55, 90, 20, translate.translateKey("gui.done")));

        this.modeBlockOutline.setElementIndex(BootifulBlockOutline.CONFIG.blockOutlineMode);
        this.modeEntityOutline.setElementIndex(BootifulBlockOutline.CONFIG.entityOutlineMode);
        this.onModeChange();
    }

    @Override
    public void keyTyped(char eventChar, int eventKey) {
        //enter quit
        if (eventChar == '\r') {
            this.saveToConfig();
            this.mc.displayGuiScreen(parentScreen);
            return;
        }
        super.keyTyped(eventChar, eventKey);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if(guiButton.enabled) {
            switch (guiButton.id) {
                case 0:
                case 3: {
                    ((GuiButtonMultipleChoice)guiButton).moveNextElement();
                    this.onModeChange();
                    return;
                }

                //block outline argb
                case 1: {
                    this.saveToConfig();
                    this.mc.displayGuiScreen(new GuiChangeARGB(
                            this,
                            StringTranslate.getInstance().translateKey("bootifulblockoutline.title.argb.block"),
                            BootifulBlockOutline.CONFIG.blockOutlineColor,
                            i -> {
                                BootifulBlockOutline.CONFIG.blockOutlineColor = i;
                                BootifulBlockOutline.forceSaveConfig();
                            }
                    ));
                    return;
                }

                //block outline width
                case 2: {
                    this.saveToConfig();
                    this.mc.displayGuiScreen(new GuiChangeWithSlider(
                            this,
                            StringTranslate.getInstance().translateKey("bootifulblockoutline.title.width.block"),
                            "bootifulblockoutline.slider.width.block",
                            BootifulBlockOutline.CONFIG.blockOutlineWidth,
                            f -> {
                                BootifulBlockOutline.CONFIG.blockOutlineWidth = f;
                                BootifulBlockOutline.forceSaveConfig();
                            }
                    ));
                    return;
                }

                //entity outline argb
                case 4: {
                    this.saveToConfig();
                    this.mc.displayGuiScreen(new GuiChangeARGB(
                            this,
                            StringTranslate.getInstance().translateKey("bootifulblockoutline.title.argb.entity"),
                            BootifulBlockOutline.CONFIG.entityOutlineColor,
                            i -> {
                                BootifulBlockOutline.CONFIG.entityOutlineColor = i;
                                BootifulBlockOutline.forceSaveConfig();
                            }
                    ));
                    return;
                }

                //entity outline width
                case 5: {
                    this.saveToConfig();
                    this.mc.displayGuiScreen(new GuiChangeWithSlider(
                            this,
                            StringTranslate.getInstance().translateKey("bootifulblockoutline.title.width.entity"),
                            "bootifulblockoutline.slider.width.entity",
                            BootifulBlockOutline.CONFIG.entityOutlineWidth,
                            f -> {
                                BootifulBlockOutline.CONFIG.entityOutlineWidth = f;
                                BootifulBlockOutline.forceSaveConfig();
                            }
                    ));
                    return;
                }


                case 6: {
                    this.saveToConfig();
                    this.mc.displayGuiScreen(this.parentScreen);
                    return;
                }
            }
        }
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        final int offsetY = this.height / 2 - 52;

        this.drawCenteredString(fontRenderer, this.title, this.width / 2, offsetY, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }

    /* other magic */

    private void onModeChange() {
        //block outline
        ((GuiButton)this.controlList.get(2)).enabled = this.modeBlockOutline.getCurrentIndex() == 1;
        ((GuiButton)this.controlList.get(1)).enabled = this.modeBlockOutline.getCurrentIndex() != 0;

        //entity outline
        ((GuiButton)this.controlList.get(5)).enabled = this.modeEntityOutline.getCurrentIndex() == 1;
        ((GuiButton)this.controlList.get(4)).enabled = this.modeEntityOutline.getCurrentIndex() != 0;
    }

    private void saveToConfig() {
        BootifulBlockOutline.CONFIG.blockOutlineMode = (byte) this.modeBlockOutline.getCurrentIndex();
        BootifulBlockOutline.CONFIG.entityOutlineMode = (byte) this.modeEntityOutline.getCurrentIndex();

        BootifulBlockOutline.forceSaveConfig();
    }

}
