package net.tracystacktrace.bootifulblockoutline;

import com.fox2code.foxloader.loader.Mod;

public class BootifulBlockOutline extends Mod {

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
}
