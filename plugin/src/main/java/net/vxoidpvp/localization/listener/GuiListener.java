package net.vxoidpvp.localization.listener;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.vxoidpvp.localization.LocalizationPaperPlugin;
import net.vxoidpvp.localization.api.Locale;
import net.vxoidpvp.localization.api.Localization;
import net.vxoidpvp.localization.gui.LangGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        String title = LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        if (!title.contains("Language") && !title.contains("Sprache")) {
            return;
        }

        event.setCancelled(true);

        int slot = event.getRawSlot();
        Locale locale = LangGui.getLocaleFromSlot(slot);

        if (locale != null) {
            LocalizationPaperPlugin.getInstance().getPlayerLocaleStorage().savePlayerLocale(player.getUniqueId(), locale);

            String message = Localization.getPlayerMessage(
                    "localization.command.lang.change",
                    player.getUniqueId(),
                    "local", locale.getDisplayName()
            );
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
            player.closeInventory();
        }
    }
}
