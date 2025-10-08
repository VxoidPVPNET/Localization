package net.vxoidpvp.localization.api;

import java.util.ArrayList;
import java.util.List;

public class MessageFormatter {

    public static String format(String message, Object... placeholders) {
        if (message == null) {
            return "";
        }

        String formatted = message;

        // Replace placeholders
        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i < placeholders.length; i += 2) {
                if (i + 1 < placeholders.length) {
                    String key = placeholders[i].toString();
                    String value = placeholders[i + 1].toString();
                    formatted = formatted.replace("{" + key + "}", value);
                }
            }
        }

        // Replace color codes
        formatted = translateColorCodes(formatted);

        return formatted;
    }

    public static List<String> formatList(List<String> messages, Object... placeholders) {
        if (messages == null) {
            return new ArrayList<>();
        }

        List<String> formatted = new ArrayList<>();
        for (String message : messages) {
            formatted.add(format(message, placeholders));
        }
        return formatted;
    }

    public static String translateColorCodes(String text) {
        if (text == null) {
            return "";
        }
        return text.replace('&', '\u00A7');
    }
}
