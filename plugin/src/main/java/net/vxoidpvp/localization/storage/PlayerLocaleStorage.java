package net.vxoidpvp.localization.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.vxoidpvp.localization.LocalizationPaperPlugin;
import net.vxoidpvp.localization.api.Locale;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLocaleStorage {

    private final LocalizationPaperPlugin plugin;
    private final File storageFile;
    private final Gson gson;

    public PlayerLocaleStorage(LocalizationPaperPlugin plugin) {
        this.plugin = plugin;
        this.storageFile = new File(plugin.getDataFolder(), "player-locales.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void loadAll() {
        if (!storageFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(storageFile)) {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> data = gson.fromJson(reader, type);

            if (data != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    UUID playerId = UUID.fromString(entry.getKey());
                    Locale locale = Locale.get(entry.getValue());
                    if (locale != null) {
                        Locale.setPlayerLocale(playerId, locale);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load player locales: " + e.getMessage());
        }
    }

    public void saveAll() {
        try {
            if (!storageFile.getParentFile().exists()) {
                storageFile.getParentFile().mkdirs();
            }
            Map<String, String> data = new HashMap<>();
            try (FileWriter writer = new FileWriter(storageFile)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player locales: " + e.getMessage());
        }
    }

    public void savePlayerLocale(UUID playerId, Locale locale) {
        Locale.setPlayerLocale(playerId, locale);
    }
}