package net.vxoidpvp.localization.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Locale {

    private static final Map<String, Locale> LOCALES = new HashMap<>();
    private static final Map<UUID, Locale> PLAYER_LOCALES = new HashMap<>();
    private static Locale defaultLocale;

    private final String code;
    private final String displayName;

    public Locale(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static void register(Locale locale) {
        LOCALES.put(locale.getCode(), locale);
    }

    public static Locale get(String code) {
        return LOCALES.get(code);
    }

    public static Map<String, Locale> getAll() {
        return new HashMap<>(LOCALES);
    }

    public static void setDefault(Locale locale) {
        defaultLocale = locale;
    }

    public static Locale getDefault() {
        return defaultLocale;
    }

    public static void setPlayerLocale(UUID playerId, Locale locale) {
        PLAYER_LOCALES.put(playerId, locale);
    }

    public static Locale getPlayerLocale(UUID playerId) {
        return PLAYER_LOCALES.getOrDefault(playerId, defaultLocale);
    }

    public static void removePlayerLocale(UUID playerId) {
        PLAYER_LOCALES.remove(playerId);
    }

    @Override
    public String toString() {
        return code;
    }
}
