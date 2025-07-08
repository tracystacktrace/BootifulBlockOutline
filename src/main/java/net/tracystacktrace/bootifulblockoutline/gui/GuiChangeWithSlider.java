package net.tracystacktrace.bootifulblockoutline.gui;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import net.tracystacktrace.bootifulblockoutline.gui.element.GuiSliderCompact;
import net.tracystacktrace.bootifulblockoutline.gui.element.IUpdateSliders;

public class GuiChangeWithSlider extends GuiScreen implements IUpdateSliders {

    protected final String title;
    protected final String name;
    protected final float initialValue;
    protected final FloatConsumer saveHandler;

    //Value handler
    protected GuiSliderCompact slider;
    protected float value;

    public GuiChangeWithSlider(GuiScreen parentScreen, String title, String name, float value, FloatConsumer saveHandler) {
        this.parentScreen = parentScreen;
        this.title = StringTranslate.getInstance().translateKey(title);
        this.name = name;
        this.initialValue = value;
        this.saveHandler = saveHandler;

        this.value = value;
    }

    @Override
    public void initGui() {
        this.controlList.clear();

        final int offsetX = this.width / 2 - 100;
        final int offsetY = this.height / 2 - 25;

        this.slider = new GuiSliderCompact(0, offsetX, offsetY, 200, "", BootifulBlockOutline.normalizeFloat(this.value, 1f, 3f), this);
        this.controlList.add(this.slider);

        final StringTranslate translate = StringTranslate.getInstance();

        this.controlList.add(new GuiButton(1, offsetX, offsetY + 30, 95, 20, translate.translateKey("bootifulblockoutline.reset")));
        this.controlList.add(new GuiButton(2, offsetX + 105, offsetY + 30, 95, 20, translate.translateKey("bootifulblockoutline.save")));

        this.onUpdateFromSliders();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled) {
            if (guiButton.id == 1) {
                this.value = initialValue;
                this.slider.sliderValue = BootifulBlockOutline.normalizeFloat(this.initialValue, 1f, 3f);
                this.slider.displayString = StringTranslate.getInstance().translateKeyFormat(this.name, this.value);
                return;
            }

            if (guiButton.id == 2) {
                this.saveHandler.accept(this.value);
                this.mc.displayGuiScreen(this.parentScreen);
                return;
            }
        }
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        final int offsetY = this.height / 2 - 25;
        this.drawCenteredString(fontRenderer, this.title, this.width / 2, offsetY - 29, 0xFFFFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);
    }

    @Override
    public void onUpdateFromSliders() {
        this.value = BootifulBlockOutline.denormalizeFloat(this.slider.sliderValue, 1f, 3f);
        this.slider.displayString = StringTranslate.getInstance().translateKeyFormat(this.name, this.value);
    }
}
