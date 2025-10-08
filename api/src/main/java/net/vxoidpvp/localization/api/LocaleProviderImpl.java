package net.vxoidpvp.localization.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.vxoidpvp.localization.api.exception.LocaleFileException;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleProviderImpl implements LocaleProvider {

    private final Map<String, Map<Locale, LocaleData>> pluginLocales = new HashMap<>();
    private final Map<String, File> pluginDirectories = new HashMap<>();

    @Override
    public void loadLocales(String pluginName, File langDirectory) {
        if (!langDirectory.exists() || !langDirectory.isDirectory()) {
            throw new LocaleFileException("Language directory does not exist: " + langDirectory.getAbsolutePath());
        }

        pluginDirectories.put(pluginName, langDirectory);
        Map<Locale, LocaleData> locales = new HashMap<>();

        File[] files = langDirectory.listFiles((dir, name) -> name.endsWith(".locale.json"));
        if (files == null || files.length == 0) {
            throw new LocaleFileException("No locale files found in: " + langDirectory.getAbsolutePath());
        }

        for (File file : files) {
            String fileName = file.getName();
            String localeCode = fileName.substring(0, fileName.indexOf(".locale.json"));

            Locale locale = Locale.get(localeCode);
            if (locale == null) {
                locale = new Locale(localeCode, localeCode.toUpperCase());
                Locale.register(locale);
            }

            try (FileReader reader = new FileReader(file)) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                LocaleData localeData = new LocaleData(locale, jsonObject);
                locales.put(locale, localeData);
            } catch (Exception e) {
                throw new LocaleFileException("Failed to load locale file: " + file.getName(), e);
            }
        }

        pluginLocales.put(pluginName, locales);
    }

    @Override
    public void reloadAll() {
        for (String pluginName : new ArrayList<>(pluginDirectories.keySet())) {
            reloadPlugin(pluginName);
        }
    }

    @Override
    public void reloadPlugin(String pluginName) {
        File directory = pluginDirectories.get(pluginName);
        if (directory == null) {
            throw new LocaleFileException("Plugin not registered: " + pluginName);
        }

        pluginLocales.remove(pluginName);
        loadLocales(pluginName, directory);
    }

    @Override
    public LocaleData getLocaleData(String pluginName, Locale locale) {
        Map<Locale, LocaleData> locales = pluginLocales.get(pluginName);
        if (locales == null) {
            return null;
        }
        return locales.get(locale);
    }

    @Override
    public List<String> getRegisteredPlugins() {
        return new ArrayList<>(pluginLocales.keySet());
    }

    @Override
    public boolean isPluginRegistered(String pluginName) {
        return pluginLocales.containsKey(pluginName);
    }
}
