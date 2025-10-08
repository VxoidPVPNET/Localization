package net.vxoidpvp.localization;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.vxoidpvp.localization.api.Locale;
import net.vxoidpvp.localization.api.LocaleProvider;
import net.vxoidpvp.localization.api.LocaleProviderImpl;
import net.vxoidpvp.localization.api.Localization;
import net.vxoidpvp.localization.commands.LangCommand;
import net.vxoidpvp.localization.listener.GuiListener;
import net.vxoidpvp.localization.storage.PlayerLocaleStorage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class LocalizationPaperPlugin extends JavaPlugin {

    private static LocalizationPaperPlugin instance;
    private Logger logger = getLogger();
    private LocaleProvider localeProvider;
    private PlayerLocaleStorage playerLocaleStorage;

    @Override
    public void onLoad() {
        logger.info("Initializing Localization Paper Plugin...");
        instance = this;
        localeProvider = new LocaleProviderImpl();
        Localization.initialize(localeProvider);

        // set default locale
        Locale defaultLocale = new Locale("en", "English");
        Locale.register(defaultLocale);
        Locale.setDefault(defaultLocale);

        // Register german locale
        Locale germanLocale = new Locale("de", "German");
        Locale.register(germanLocale);
    }

    @Override
    public void onEnable() {
        logger.info("Enable Localization...");
        playerLocaleStorage = new PlayerLocaleStorage(this);
        playerLocaleStorage.loadAll();

        // Load plugin locales
        Localization.setPluginContext("Localization");
        File langDirectory = new File(getDataFolder(), "lang");
        if (!langDirectory.exists()) {
            langDirectory.mkdirs();
            saveResource("lang/en.locale.json", false);
            saveResource("lang/de.locale.json", false);
        }
        localeProvider.loadLocales("Localization", langDirectory);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            new LangCommand(this).register(commands);
        });

        getServer().getPluginManager().registerEvents(new GuiListener(), this);

        logger.info("Localization enabled successfully!");
    }

    @Override
    public void onDisable() {
        logger.info("Disable Localization...");
        if (playerLocaleStorage != null) {
            playerLocaleStorage.saveAll();
        }
    }

    public static LocalizationPaperPlugin getInstance() {
        return instance;
    }

    public LocaleProvider getLocaleProvider() {
        return localeProvider;
    }

    public PlayerLocaleStorage getPlayerLocaleStorage() {
        return playerLocaleStorage;
    }
}