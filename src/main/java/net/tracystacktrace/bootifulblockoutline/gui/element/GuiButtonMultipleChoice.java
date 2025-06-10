package net.tracystacktrace.bootifulblockoutline.gui.element;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonMultipleChoice extends GuiButton {

    protected final String[] options;
    protected final String descriptor;
    protected int index;

    public GuiButtonMultipleChoice(int _id, int x, int y, int w, int h, String descriptor, String[] options) {
        super(_id, x, y, w, h, "");
        this.options = options;

        this.descriptor = descriptor;
        this.index = 0;
        this.displayString = String.format(this.descriptor, this.options[index]);
    }

    public GuiButtonMultipleChoice(int _id, int x, int y, String descriptor, String[] options) {
        this(_id, x, y, 200, 20, descriptor, options);
    }

    public void setElementIndex(int i) {
        if (i >= 0 && i < options.length) {
            this.index = i;
            this.displayString = String.format(this.descriptor, this.options[i]);
        }
    }

    public void moveNextElement() {
        if (this.index + 1 == this.options.length) {
            this.index = 0;
        } else {
            this.index++;
        }

        this.displayString = String.format(this.descriptor, this.options[index]);
    }

    public int getCurrentIndex() {
        return this.index;
    }
}
