package net.tracystacktrace.bootifulblockoutline;

import com.fox2code.foxloader.config.ConfigEntry;
import com.fox2code.foxloader.config.ConfigIO;
import com.fox2code.foxloader.loader.Mod;
import com.fox2code.foxloader.loader.ModContainer;

import java.awt.*;

public class BootifulBlockOutline extends Mod {

    public static final ModConfig CONFIG = new ModConfig();

    private static ModContainer INTERNAL_CONTAINER;

    @Override
    public void onPreInit() {
        INTERNAL_CONTAINER = this.getModContainer();
        this.setConfigObject(CONFIG);
    }

    public static void forceSaveConfig() {
        ConfigIO.writeConfiguration(INTERNAL_CONTAINER, CONFIG);
    }

    public static short safeStringToShort(final String s) {
        if (s == null || s.isEmpty()) return 0;
        return (short) Math.max(0, Math.min(255, Integer.parseInt(s)));
    }

    public static boolean isValidInputDigit(char eventChar, int eventKey) {
        return Character.isDigit(eventChar) ||
                eventKey == 14 || eventKey == 203 || eventKey == 205; //left, right and backspace
    }

    public static boolean isValidInputHEX(char c, int eventKey) {
        return (c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F') ||
                eventKey == 14 || eventKey == 203 || eventKey == 205; //left, right and backspace
    }

    public static boolean withinUnsignedByte(short value, char input) {
        if(Character.isDigit(input))
            return Short.parseShort(String.valueOf(value) + input) < 256;
        return true;
    }

    public static String fixColorHex(String damaged) {
        if (damaged.length() == 8) {
            return damaged;
        }

        if (damaged.length() > 8) {
            return damaged.substring(0, 8);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(damaged);
        for (int i = 0; i < 8 - damaged.length(); i++) {
            builder.append('0');
        }
        return builder.toString();
    }


    public static float normalizeFloat(float value, float lower, float upper) {
        return Math.round(((value - lower) / upper) * 10f) / 10f;
    }

    public static float denormalizeFloat(float value, float lower, float upper) {
        return Math.round((lower + value * upper) * 10f) / 10f;
    }

    public static int getSilverARGB() {
        return (Color.HSBtoRGB((float) (System.currentTimeMillis() % 5000L) / 5000.0F, 1.0F, 1.0F) & 16777215) | 0xFF000000;
    }

    public static class ModConfig {

        /* block hitbox */

        @ConfigEntry(
                configComment = "0 - disabled (no render), 1 - ARGB static color, 2 - RGB rainbow color",
                lowerBounds = 0, upperBounds = 2
        )
        public byte selectionBoxMode = 0; //0 - off, 1 - ARGB, 2 - rainbow

        @ConfigEntry(configComment = "Follows 0xAARRGGBB format but int")
        public int selectionBoxColor = 0xFF000000; //AARRGGBB

        @ConfigEntry(lowerBounds = 1.0, upperBounds = 4.0)
        public float selectionBoxWidth = 2.0f;

        /* entity hitbox */

        @ConfigEntry
        public boolean enableEntityHitbox = true;

        @ConfigEntry(configComment = "Follows 0xAARRGGBB format but int")
        public int colorEntityHitbox = 0xFF000000; //AARRGGBB

        @ConfigEntry(configComment = "Follows 0xAARRGGBB format but int")
        public int colorOutlineUnbreakable = 0xFF000000; //AARRGGBB
    }
}
