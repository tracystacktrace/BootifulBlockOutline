package net.tracystacktrace.bootifulblockoutline.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.util.GameSettings;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import org.lwjgl.input.Keyboard;

public class GuiOutlineEditor extends GuiScreen {

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

    //Common
    private byte outlineMode = 0; //0 for default (RGB), 1 for silver mode
    private GuiTextField hexTextField;

    //Internal
    private final GuiScreen parentScreen;
    protected final String screenTitle = StringTranslate.getInstance().translateKey("gui.editBlockOutline");

    public GuiOutlineEditor(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.fetchPreviousValue();
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);

        final int offsetX = this.width / 2 - 120;
        final int offsetY = this.height / 2 - 40;

        //buttons init
        final StringTranslate translate = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, offsetY + 100, 90, 20, translate.translateKey("bootifulblockoutline.mode." + (this.outlineMode == 0 ? "default" : "rgb"))));
        this.controlList.add(new GuiButton(1, this.width / 2 + 10, offsetY + 100, 90, 20, translate.translateKey("gui.done")));

        //sliders init
        this.redIntSlider = new GuiSliderCompact(2, offsetX, offsetY, "Red: " + this.red, this.red / 255.0F, this);
        this.greenIntSlider = new GuiSliderCompact(3, offsetX, offsetY + 30, "Green: " + this.green, this.green / 255.0F, this);
        this.blueIntSlider = new GuiSliderCompact(4, offsetX, offsetY + 60, "Blue: " + this.blue, this.blue / 255.0F, this);

        this.controlList.add(this.redIntSlider);
        this.controlList.add(this.greenIntSlider);
        this.controlList.add(this.blueIntSlider);

        //text fields init
        this.redIntTextField = new GuiTextField(offsetX + 110, offsetY, 50, 20, String.valueOf(this.red));
        this.greenIntTextField = new GuiTextField(offsetX + 110, offsetY + 30, 50, 20, String.valueOf(this.green));
        this.blueIntTextField = new GuiTextField(offsetX + 110, offsetY + 60, 50, 20, String.valueOf(this.blue));

        this.redIntTextField.setMaxStringLength(3);
        this.redIntTextField.isEnabled = true;

        this.greenIntTextField.setMaxStringLength(3);
        this.greenIntTextField.isEnabled = true;

        this.blueIntTextField.setMaxStringLength(3);
        this.blueIntTextField.isEnabled = true;

        //hex field
        this.hexTextField = new GuiTextField(offsetX + 170, offsetY, 70, 20, "");
        this.hexTextField.setMaxStringLength(6);
        this.hexTextField.isEnabled = true;
        this.updateHexTextField();

        //toggle if silver mode
        this.toggleOutlineMode(this.outlineMode == 0);
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
        this.hexTextField.updateCursorCounter();
    }

    @Override
    public void keyTyped(char eventChar, int eventKey) {
        if (this.redIntTextField.isFocused && BootifulBlockOutline.allowedEditKey(eventChar, eventKey)) {
            this.redIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.applyEditsFromText();
        }

        if (this.greenIntTextField.isFocused && BootifulBlockOutline.allowedEditKey(eventChar, eventKey)) {
            this.greenIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.applyEditsFromText();
        }

        if (this.blueIntTextField.isFocused && BootifulBlockOutline.allowedEditKey(eventChar, eventKey)) {
            this.blueIntTextField.textboxKeyTyped(eventChar, eventKey);
            this.applyEditsFromText();
        }

        if (this.hexTextField.isFocused && BootifulBlockOutline.allowedHexKey(eventChar, eventKey)) {
            this.hexTextField.textboxKeyTyped(eventChar, eventKey);
            this.applyEditsFromHexField();
        }

        //esc quit
        if (eventKey == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }

        //enter quit
        if (eventChar == '\r') {
            this.saveToSettings();
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(parentScreen);
        }
    }

    @Override
    public void mouseClicked(float x, float y, int click) {
        super.mouseClicked(x, y, click);
        this.redIntTextField.mouseClicked(x, y, click);
        this.greenIntTextField.mouseClicked(x, y, click);
        this.blueIntTextField.mouseClicked(x, y, click);
        this.hexTextField.mouseClicked(x, y, click);
    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float deltaTicks) {
        this.drawDefaultBackground();

        this.redIntTextField.drawTextBox();
        this.greenIntTextField.drawTextBox();
        this.blueIntTextField.drawTextBox();
        this.hexTextField.drawTextBox();

        this.drawCenteredString(this.fontRenderer, this.screenTitle, (float) this.width / 2, 60, 0x00FFFFFF);

        super.drawScreen(mouseX, mouseY, deltaTicks);

        //draw color
        final int offsetX = this.width / 2 - 120;
        final int offsetY = this.height / 2 - 40;
        drawRect(
                offsetX + 170, offsetY + 30,
                offsetX + 170 + 70, offsetY + 30 + 50,
                (this.outlineMode == 0) ? ((0xFF << 24) | ((this.red & 0xFF) << 16) | ((this.green & 0xFF) << 8) | (this.blue & 0xFF)) : BootifulBlockOutline.getSilverARGB()
        );
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled) {
            //done button
            if (guiButton.id == 1) {
                this.saveToSettings();
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(parentScreen);
            }

            //toggle silver mode
            if (guiButton.id == 0) {
                this.outlineMode = (byte) ((this.outlineMode == 0) ? 1 : 0);
                this.toggleOutlineMode(this.outlineMode == 0);
                guiButton.displayString = StringTranslate.getInstance().translateKey("bootifulblockoutline.mode." + (this.outlineMode == 0 ? "default" : "rgb"));
            }
        }
    }

    public void toggleOutlineMode(boolean rgbMode) {
        //safely de-focus fields
        this.redIntTextField.isFocused = false;
        this.greenIntTextField.isFocused = false;
        this.blueIntTextField.isFocused = false;
        this.hexTextField.isFocused = false;

        //toggle stuff

        this.redIntTextField.isEnabled = rgbMode;
        this.greenIntTextField.isEnabled = rgbMode;
        this.blueIntTextField.isEnabled = rgbMode;
        this.hexTextField.isEnabled = rgbMode;

        this.redIntSlider.enabled = rgbMode;
        this.greenIntSlider.enabled = rgbMode;
        this.blueIntSlider.enabled = rgbMode;
    }

    private void fetchPreviousValue() {
        //silver (aka rainbow) mode
        if (GameSettings.outlineColor.equals("silver")) {
            this.outlineMode = 1;
            return;
        }

        //empty (black) string
        if (GameSettings.outlineColor.isEmpty() || GameSettings.outlineColor.equals("0x")) {
            this.outlineMode = 0;
            this.red = this.green = this.blue = 0;
            return;
        }

        //hex string to R, G and B
        if (GameSettings.outlineColor.length() == 6) {
            this.red = (short) Integer.parseInt(GameSettings.outlineColor.substring(0, 2), 16);
            this.green = (short) Integer.parseInt(GameSettings.outlineColor.substring(2, 4), 16);
            this.blue = (short) Integer.parseInt(GameSettings.outlineColor.substring(4, 6), 16);
        }
    }

    public void saveToSettings() {
        if (outlineMode == 1) {
            GameSettings.outlineColor = "silver";
            GameSettings.outlineColorChanged = true;
            return;
        }

        if (this.red == 0 && this.green == 0 && this.blue == 0) {
            GameSettings.outlineColor = "0x";
            GameSettings.outlineColorChanged = false;
        } else {
            GameSettings.outlineColor = String.format("%02X%02X%02X", this.red, this.green, this.blue);
            GameSettings.outlineColorChanged = true;
        }
    }

    /* ===== ===== RGB TEXT/SLIDER METHODS ===== ===== */

    /**
     * Applies update edits from text fields
     * lol
     */
    private void applyEditsFromText() {
        //apply values
        this.red = BootifulBlockOutline.safeStringToShort(this.redIntTextField.getText());
        this.green = BootifulBlockOutline.safeStringToShort(this.greenIntTextField.getText());
        this.blue = BootifulBlockOutline.safeStringToShort(this.blueIntTextField.getText());

        //force update of sliders values
        this.updateSliders();
        this.updateHexTextField();
    }

    /**
     * Applies update edits from sliders
     */
    void applyEditsFromSliders() {
        //apply values
        this.red = (short) (this.redIntSlider.sliderValue * 255.0F + 0.5F);
        this.green = (short) (this.greenIntSlider.sliderValue * 255.0F + 0.5F);
        this.blue = (short) (this.blueIntSlider.sliderValue * 255.0F + 0.5F);

        this.redIntSlider.displayString = "Red: " + this.red;
        this.greenIntSlider.displayString = "Green: " + this.green;
        this.blueIntSlider.displayString = "Blue: " + this.blue;

        //force update of text fields values
        this.updateTextFields();
        this.updateHexTextField();
    }

    /**
     * Applies update edits from the hex text field
     */
    private void applyEditsFromHexField() {
        final String safeHex = BootifulBlockOutline.autoCompleteHex(this.hexTextField.getText());

        this.red = (short) Integer.parseInt(safeHex.substring(0, 2), 16);
        this.green = (short) Integer.parseInt(safeHex.substring(2, 4), 16);
        this.blue = (short) Integer.parseInt(safeHex.substring(4, 6), 16);

        this.updateSliders();
        this.updateTextFields();
    }


    /**
     * Update sliders data to current RGB values
     * <br>
     * Primarily edits {@link GuiSlider#sliderValue} and {@link GuiSlider#displayString}
     */
    private void updateSliders() {
        this.redIntSlider.sliderValue = this.red / 255.0F;
        this.greenIntSlider.sliderValue = this.green / 255.0F;
        this.blueIntSlider.sliderValue = this.blue / 255.0F;

        this.redIntSlider.displayString = "Red: " + this.red;
        this.greenIntSlider.displayString = "Green: " + this.green;
        this.blueIntSlider.displayString = "Blue: " + this.blue;
    }

    /**
     * Update text fields data to current RGB values
     * <br>
     * Primarily edits {@link GuiTextField#text} value
     */
    private void updateTextFields() {
        this.redIntTextField.setText(String.valueOf(this.red));
        this.greenIntTextField.setText(String.valueOf(this.green));
        this.blueIntTextField.setText(String.valueOf(this.blue));

        this.redIntTextField.moveCursorBy(4);
        this.greenIntTextField.moveCursorBy(4);
        this.blueIntTextField.moveCursorBy(4);
    }

    /**
     * Update hex text field data to current RGB value
     */
    private void updateHexTextField() {
        final String formatted = String.format("%02X%02X%02X", this.red, this.green, this.blue);
        this.hexTextField.setText(formatted);
        this.hexTextField.moveCursorBy(6);
    }
}
