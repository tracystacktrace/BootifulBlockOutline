package net.tracystacktrace.bootifulblockoutline.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import net.tracystacktrace.bootifulblockoutline.gui.element.GuiSliderCompact;
import net.tracystacktrace.bootifulblockoutline.gui.element.IUpdateSliders;
import org.lwjgl.input.Keyboard;

import java.util.function.IntConsumer;

public class GuiChangeARGB extends GuiScreen implements IUpdateSliders {

    //Red
    private GuiTextField redIntTextField;
    private GuiSliderCompact redIntSlider;
    private short red;

    //Green
    private GuiTextField greenIntTextField;
    private GuiSliderCompact greenIntSlider;
    private short green;

    //Blue
    private GuiTextField blueIntTextField;
    private GuiSliderCompact blueIntSlider;
    private short blue;

    //Alpha
    private GuiTextField alphaIntTextField;
    private GuiSliderCompact alphaIntSlider;
    private short alpha;

    //Common
    private GuiTextField hexTextField;

    //Internal
    private final GuiScreen parentScreen;
    private final IntConsumer handleResult;
    protected final String screenTitle;

    public GuiChangeARGB(GuiScreen parentScreen, String title, int argb, IntConsumer onAcceptChange) {
        this.parentScreen = parentScreen;
        this.screenTitle = StringTranslate.getInstance().translateKey(title);
        this.handleResult = onAcceptChange;
        this.alpha = (short) ((argb >> 24) & 0xFF);
        this.red = (short) ((argb >> 16) & 0xFF);
        this.green = (short) ((argb >> 8) & 0xFF);
        this.blue = (short) ((argb) & 0xFF);
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);

        final int offsetX = this.width / 2 - 120;
        final int offsetY = this.height / 2 - 55;

        final StringTranslate translate = StringTranslate.getInstance();

        //buttons init
        this.controlList.add(new GuiButton(1, this.width / 2 - 45, offsetY + 120, 90, 20, translate.translateKey("bootifulblockoutline.save")));

        //sliders init
        this.redIntSlider = new GuiSliderCompact(2, offsetX, offsetY, translate.translateKeyFormat("bootifulblockoutline.red", this.red), this.red / 255.0F, this);
        this.greenIntSlider = new GuiSliderCompact(3, offsetX, offsetY + 30, translate.translateKeyFormat("bootifulblockoutline.green", this.green), this.green / 255.0F, this);
        this.blueIntSlider = new GuiSliderCompact(4, offsetX, offsetY + 60, translate.translateKeyFormat("bootifulblockoutline.blue", this.blue), this.blue / 255.0F, this);
        this.alphaIntSlider = new GuiSliderCompact(5, offsetX, offsetY + 90, translate.translateKeyFormat("bootifulblockoutline.alpha", this.alpha), this.alpha / 255.0F, this);

        this.controlList.add(this.redIntSlider);
        this.controlList.add(this.greenIntSlider);
        this.controlList.add(this.blueIntSlider);
        this.controlList.add(this.alphaIntSlider);

        //text fields init
        this.redIntTextField = new GuiTextField(offsetX + 110, offsetY, 50, 20, String.valueOf(this.red));
        this.greenIntTextField = new GuiTextField(offsetX + 110, offsetY + 30, 50, 20, String.valueOf(this.green));
        this.blueIntTextField = new GuiTextField(offsetX + 110, offsetY + 60, 50, 20, String.valueOf(this.blue));
        this.alphaIntTextField = new GuiTextField(offsetX + 110, offsetY + 90, 50, 20, String.valueOf(this.alpha));

        this.redIntTextField.setMaxStringLength(3);
        this.redIntTextField.isEnabled = true;

        this.greenIntTextField.setMaxStringLength(3);
        this.greenIntTextField.isEnabled = true;

        this.blueIntTextField.setMaxStringLength(3);
        this.blueIntTextField.isEnabled = true;

        this.alphaIntTextField.setMaxStringLength(3);
        this.alphaIntTextField.isEnabled = true;

        //hex field
        this.hexTextField = new GuiTextField(offsetX + 170, offsetY, 70, 20, "");
        this.hexTextField.setMaxStringLength(8);
        this.hexTextField.isEnabled = true;
        this.updateHexTextFieldDisplayString();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.redIntTextField.updateCursorCounter();
        this.greenIntTextField.updateCursorCounter();
        this.blueIntTextField.updateCursorCounter();
        this.alphaIntTextField.updateCursorCounter();
        this.hexTextField.updateCursorCounter();
    }

    @Override
    public void keyTyped(char eventChar, int eventKey) {
        if (this.redIntTextField.isFocused && BootifulBlockOutline.isValidInputDigit(eventChar, eventKey) && BootifulBlockOutline.withinUnsignedByte(this.red, eventChar)) {
            this.redIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.onUpdateFromTextFields();
        }

        if (this.greenIntTextField.isFocused && BootifulBlockOutline.isValidInputDigit(eventChar, eventKey) && BootifulBlockOutline.withinUnsignedByte(this.green, eventChar)) {
            this.greenIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.onUpdateFromTextFields();
        }

        if (this.blueIntTextField.isFocused && BootifulBlockOutline.isValidInputDigit(eventChar, eventKey) && BootifulBlockOutline.withinUnsignedByte(this.blue, eventChar)) {
            this.blueIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.onUpdateFromTextFields();
        }

        if (this.alphaIntTextField.isFocused && BootifulBlockOutline.isValidInputDigit(eventChar, eventKey) && BootifulBlockOutline.withinUnsignedByte(this.alpha, eventChar)) {
            this.alphaIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.onUpdateFromTextFields();
        }

        if (this.hexTextField.isFocused && BootifulBlockOutline.isValidInputHEX(eventChar, eventKey)) {
            this.hexTextField.textboxKeyTyped(eventChar, eventKey);
            this.onUpdateFromHexField();
        }

        //esc quit
        if (eventKey == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }

        //enter quit
        if (eventChar == '\r') {
            this.saveResult();
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void mouseClicked(float x, float y, int click) {
        super.mouseClicked(x, y, click);
        this.redIntTextField.mouseClicked(x, y, click);
        this.greenIntTextField.mouseClicked(x, y, click);
        this.blueIntTextField.mouseClicked(x, y, click);
        this.alphaIntTextField.mouseClicked(x, y, click);
        this.hexTextField.mouseClicked(x, y, click);
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        this.redIntTextField.drawTextBox();
        this.greenIntTextField.drawTextBox();
        this.blueIntTextField.drawTextBox();
        this.alphaIntTextField.drawTextBox();
        this.hexTextField.drawTextBox();

        final int offsetX = this.width / 2 - 120;
        final int offsetY = this.height / 2 - 40;

        this.drawCenteredString(this.fontRenderer, this.screenTitle, (float) this.width / 2, offsetY - 34, 0x00FFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);

        //draw color
        drawRect(
                offsetX + 170, offsetY + 15,
                offsetX + 170 + 70, offsetY + 15 + 80,
                ((this.alpha & 0xFF) << 24) | ((this.red & 0xFF) << 16) | ((this.green & 0xFF) << 8) | (this.blue & 0xFF)
        );
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled) {
            //done button
            if (guiButton.id == 1) {
                this.saveResult();
                this.mc.displayGuiScreen(parentScreen);
            }
        }
    }

    public void saveResult() {
        this.handleResult.accept(((this.alpha & 0xFF) << 24) | ((this.red & 0xFF) << 16) | ((this.green & 0xFF) << 8) | (this.blue & 0xFF));
    }

    /* ===== ===== RGB TEXT/SLIDER METHODS ===== ===== */

    /**
     * Applies update edits from text fields
     * lol
     */
    private void onUpdateFromTextFields() {
        //apply values
        this.red = BootifulBlockOutline.safeStringToShort(this.redIntTextField.getText());
        this.green = BootifulBlockOutline.safeStringToShort(this.greenIntTextField.getText());
        this.blue = BootifulBlockOutline.safeStringToShort(this.blueIntTextField.getText());
        this.alpha = BootifulBlockOutline.safeStringToShort(this.alphaIntTextField.getText());

        //force update of sliders values
        this.updateSlidersDisplayString();
        this.updateHexTextFieldDisplayString();
    }

    /**
     * Applies update edits from sliders
     */
    public void onUpdateFromSliders() {
        //apply values
        this.red = (short) (this.redIntSlider.sliderValue * 255.0F + 0.5F);
        this.green = (short) (this.greenIntSlider.sliderValue * 255.0F + 0.5F);
        this.blue = (short) (this.blueIntSlider.sliderValue * 255.0F + 0.5F);
        this.alpha = (short) (this.alphaIntSlider.sliderValue * 255.0F + 0.5F);

        final StringTranslate translate = StringTranslate.getInstance();
        this.redIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.red", this.red);
        this.greenIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.green", this.green);
        this.blueIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.blue", this.blue);
        this.alphaIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.alpha", this.alpha);

        //force update of text fields values
        this.updateTextFieldsDisplayString();
        this.updateHexTextFieldDisplayString();
    }

    /**
     * Applies update edits from the hex text field
     */
    private void onUpdateFromHexField() {
        final String safeHex = BootifulBlockOutline.fixColorHex(this.hexTextField.getText());

        this.alpha = (short) Integer.parseInt(safeHex.substring(0, 2), 16);
        this.red = (short) Integer.parseInt(safeHex.substring(2, 4), 16);
        this.green = (short) Integer.parseInt(safeHex.substring(4, 6), 16);
        this.blue = (short) Integer.parseInt(safeHex.substring(6, 8), 16);

        this.updateSlidersDisplayString();
        this.updateTextFieldsDisplayString();
    }

    /**
     * Update sliders data to current RGB values
     * <br>
     * Primarily edits {@link GuiSlider#sliderValue} and {@link GuiSlider#displayString}
     */
    private void updateSlidersDisplayString() {
        this.redIntSlider.sliderValue = this.red / 255.0F;
        this.greenIntSlider.sliderValue = this.green / 255.0F;
        this.blueIntSlider.sliderValue = this.blue / 255.0F;
        this.alphaIntSlider.sliderValue = this.alpha / 255.0F;

        final StringTranslate translate = StringTranslate.getInstance();
        this.redIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.red", this.red);
        this.greenIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.green", this.green);
        this.blueIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.blue", this.blue);
        this.alphaIntSlider.displayString = translate.translateKeyFormat("bootifulblockoutline.alpha", this.alpha);
    }

    /**
     * Update text fields data to current RGB values
     * <br>
     * Primarily edits {@link GuiTextField#text} value
     */
    private void updateTextFieldsDisplayString() {
        this.redIntTextField.setText(String.valueOf(this.red));
        this.greenIntTextField.setText(String.valueOf(this.green));
        this.blueIntTextField.setText(String.valueOf(this.blue));
        this.alphaIntTextField.setText(String.valueOf(this.alpha));

        this.redIntTextField.moveCursorBy(4);
        this.greenIntTextField.moveCursorBy(4);
        this.blueIntTextField.moveCursorBy(4);
        this.alphaIntTextField.moveCursorBy(4);
    }

    /**
     * Update hex text field data to current RGB value
     */
    private void updateHexTextFieldDisplayString() {
        final String formatted = String.format("%02X%02X%02X%02X", this.alpha, this.red, this.green, this.blue);
        this.hexTextField.setText(formatted);
        this.hexTextField.moveCursorBy(6);
    }
}
