package net.vxoidpvp.localization.api;

import net.vxoidpvp.localization.api.exception.LocaleFileException;

import java.io.File;
import java.util.List;

public interface LocaleProvider {

    void loadLocales(String pluginName, File langDirectory);

    void reloadAll();

    void reloadPlugin(String pluginName);

    LocaleData getLocaleData(String pluginName, Locale locale);

    List<String> getRegisteredPlugins();

    boolean isPluginRegistered(String pluginName);
}