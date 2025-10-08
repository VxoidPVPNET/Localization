package net.vxoidpvp.localization.gui;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.vxoidpvp.localization.api.Locale;
import net.vxoidpvp.localization.api.Localization;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class LangGui {

    private final Player player;
    private final Inventory inventory;

    public LangGui(Player player) {
        this.player = player;

        String title = Localization.getPlayerMessage(
                "localization.gui.lang.title",
                player.getUniqueId()
        );

        this.inventory = Bukkit.createInventory(
                null,
                27,
                LegacyComponentSerializer.legacySection().deserialize(title)
        );

        setupItems();
    }

    private void setupItems() {
        Map<String, Locale> locales = Locale.getAll();

        int slot = 10;
        for (Map.Entry<String, Locale> entry : locales.entrySet()) {
            Locale locale = entry.getValue();
            ItemStack item = createLocaleItem(locale);
            inventory.setItem(slot, item);
            slot += 2;
        }
    }

    private ItemStack createLocaleItem(Locale locale) {
        Material material = Material.PAPER;
        if (locale.getCode().equals("en")) {
            material = Material.BOOK;
        } else if (locale.getCode().equals("de")) {
            material = Material.WRITABLE_BOOK;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String itemKey = "localization.gui.lang.item." + locale.getCode().toLowerCase();
        String displayName = Localization.getPlayerMessage(itemKey, player.getUniqueId());
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(displayName));

        String loreKey = itemKey + ".lore";
        try {
            List<String> loreList = Localization.getPlayerMessageList(loreKey, player.getUniqueId());
            List<TextComponent> componentLore = loreList.stream()
                    .map(line -> LegacyComponentSerializer.legacySection().deserialize(line))
                    .toList();
            meta.lore(componentLore);
        } catch (Exception e) {

        }

        item.setItemMeta(meta);
        return item;
    }

    public void open() {
        player.openInventory(inventory);
    }

    public static boolean isLangGui(Inventory inventory) {
        // Check if inventory is a lang gui by checking the holder
        return inventory.getHolder() == null;
    }

    public static Locale getLocaleFromSlot(int slot) {
        Map<String, Locale> locales = Locale.getAll();
        int index = 0;
        int currentSlot = 10;

        for (Locale locale : locales.values()) {
            if (currentSlot == slot) {
                return locale;
            }
            currentSlot += 2;
        }

        return null;
    }
}
