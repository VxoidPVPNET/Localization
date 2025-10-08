package net.vxoidpvp.localization.api;

import net.vxoidpvp.localization.api.exception.MessageNotFoundException;

import java.util.List;
import java.util.UUID;

public class Localization {

    private static LocaleProvider localeProvider;
    private static String currentPluginName;
    public static void initialize(LocaleProvider provider) {
        localeProvider = provider;
    }
    public static void setPluginContext(String pluginName) {
        currentPluginName = pluginName;
    }
    public static LocaleProvider getProvider() {
        return localeProvider;
    }

    public static String getMessage(String key, Locale locale, Object... placeholders) {
        if (currentPluginName == null) {
            throw new IllegalStateException("Plugin context not set. Call setPluginContext() first.");
        }

        LocaleData localeData = localeProvider.getLocaleData(currentPluginName, locale);
        if (localeData == null) {
            localeData = localeProvider.getLocaleData(currentPluginName, Locale.getDefault());
        }

        if (localeData == null || !localeData.hasMessage(key)) {
            throw new MessageNotFoundException(key, locale.getCode());
        }

        String message = localeData.getMessage(key);
        return MessageFormatter.format(message, placeholders);
    }

    public static String getPlayerMessage(String key, UUID playerId, Object... placeholders) {
        Locale locale = Locale.getPlayerLocale(playerId);
        return getMessage(key, locale, placeholders);
    }

    public static List<String> getMessageList(String key, Locale locale, Object... placeholders) {
        if (currentPluginName == null) {
            throw new IllegalStateException("Plugin context not set. Call setPluginContext() first.");
        }

        LocaleData localeData = localeProvider.getLocaleData(currentPluginName, locale);
        if (localeData == null) {
            // Try default locale
            localeData = localeProvider.getLocaleData(currentPluginName, Locale.getDefault());
        }

        if (localeData == null || !localeData.hasMessage(key)) {
            throw new MessageNotFoundException(key, locale.getCode());
        }

        List<String> messages = localeData.getMessageList(key);
        if (messages == null) {
            throw new MessageNotFoundException(key + " is not a list", locale.getCode());
        }

        return MessageFormatter.formatList(messages, placeholders);
    }

    public static List<String> getPlayerMessageList(String key, UUID playerId, Object... placeholders) {
        Locale locale = Locale.getPlayerLocale(playerId);
        return getMessageList(key, locale, placeholders);
    }
}
