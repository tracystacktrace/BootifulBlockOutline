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
        if(s == null || s.isEmpty()) return 0;
        return (short) Math.max(0, Math.min(255, Integer.parseInt(s)));
    }

    public static boolean allowedEditKey(char eventChar, int eventKey) {
        return Character.isDigit(eventChar) ||
                eventKey == 14 || eventKey == 203 || eventKey == 205; //left, right and backspace
    }

    public static boolean allowedHexKey(char c, int eventKey) {
        return (c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F') ||
                eventKey == 14 || eventKey == 203 || eventKey == 205; //left, right and backspace
    }

    public static String autoCompleteHex(String damaged) {
        if(damaged.length() == 6) {
            return damaged;
        }

        if(damaged.length() > 6) {
            return damaged.substring(0, 6);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(damaged);
        for(int i = 0; i < 6 - damaged.length(); i++) {
            builder.append('0');
        }
        return builder.toString();
    }

    public static int getSilverARGB() {
        return (Color.HSBtoRGB((float)(System.currentTimeMillis() % 5000L) / 5000.0F, 1.0F, 1.0F) & 16777215) | 0xFF000000;
    }

    public static class ModConfig {
        @ConfigEntry(lowerBounds = 1.0, upperBounds = 10.0)
        public float selectionBoxWidth = 2.0f;
    }
}
