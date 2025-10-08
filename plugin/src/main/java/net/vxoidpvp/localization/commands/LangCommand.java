package net.vxoidpvp.localization.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.vxoidpvp.localization.LocalizationPaperPlugin;
import net.vxoidpvp.localization.api.Locale;
import net.vxoidpvp.localization.api.Localization;
import net.vxoidpvp.localization.gui.LangGui;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class LangCommand {

    private final LocalizationPaperPlugin plugin;

    public LangCommand(LocalizationPaperPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(Commands commands) {
        LiteralArgumentBuilder<CommandSourceStack> langCommand = Commands.literal("lang")
                .executes(context -> {
                    if (!(context.getSource().getSender() instanceof Player player)) {
                        context.getSource().getSender().sendMessage(Component.text("Only players can use this command"));
                        return 0;
                    }
                    new LangGui(player).open();
                    return 1;
                })
                .then(Commands.literal("set")
                        .then(Commands.argument("locale", StringArgumentType.word())
                                .suggests(this::suggestLocales)
                                .executes(context -> {
                                    if (!(context.getSource().getSender() instanceof Player player)) {
                                        context.getSource().getSender().sendMessage(Component.text("Only players can use this command"));
                                        return 0;
                                    }

                                    String localeCode = StringArgumentType.getString(context, "locale");
                                    Locale locale = Locale.get(localeCode);

                                    if (locale == null) {
                                        player.sendMessage(Component.text("Unknown locale: " + localeCode));
                                        return 0;
                                    }

                                    plugin.getPlayerLocaleStorage().savePlayerLocale(player.getUniqueId(), locale);

                                    String message = Localization.getPlayerMessage("localization.command.lang.change", player.getUniqueId(), "local", locale.getDisplayName()
                                    );
                                    player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("plugins")
                        .executes(context -> {
                            context.getSource().getSender().sendMessage(
                                    Component.text("Registered plugins: " +
                                            String.join(", ", plugin.getLocaleProvider().getRegisteredPlugins()))
                            );
                            return 1;
                        })
                )
                .then(Commands.literal("reload")
                        .executes(context -> executeReloadAll(context))
                        .then(Commands.argument("plugin", StringArgumentType.word())
                                .suggests(this::suggestPlugins)
                                .executes(context -> {
                                    String pluginName = StringArgumentType.getString(context, "plugin");
                                    try {
                                        plugin.getLocaleProvider().reloadPlugin(pluginName);

                                        if (context.getSource().getSender() instanceof Player player) {
                                            String message = Localization.getPlayerMessage(
                                                    "localization.command.lang.reload.successfully",
                                                    player.getUniqueId()
                                            );
                                            context.getSource().getSender().sendMessage(
                                                    LegacyComponentSerializer.legacySection().deserialize(message)
                                            );
                                        } else {
                                            context.getSource().getSender().sendMessage(
                                                    Component.text("Successfully reloaded locale files for " + pluginName)
                                            );
                                        }
                                        return 1;
                                    } catch (Exception e) {
                                        if (context.getSource().getSender() instanceof Player player) {
                                            String message = Localization.getPlayerMessage(
                                                    "localization.command.lang.reload.failed",
                                                    player.getUniqueId()
                                            );
                                            context.getSource().getSender().sendMessage(
                                                    LegacyComponentSerializer.legacySection().deserialize(message)
                                            );
                                        } else {
                                            context.getSource().getSender().sendMessage(
                                                    Component.text("Failed to reload: " + e.getMessage())
                                            );
                                        }
                                        return 0;
                                    }
                                })
                        )
                );

        commands.register(langCommand.build(), "Change your language");
    }

    private int executeReloadAll(CommandContext<CommandSourceStack> context) {
        try {
            plugin.getLocaleProvider().reloadAll();

            if (context.getSource().getSender() instanceof Player player) {
                String message = Localization.getPlayerMessage(
                        "localization.command.lang.reload.successfully",
                        player.getUniqueId()
                );
                context.getSource().getSender().sendMessage(
                        LegacyComponentSerializer.legacySection().deserialize(message)
                );
            } else {
                context.getSource().getSender().sendMessage(
                        Component.text("Successfully reloaded all locale files")
                );
            }
            return 1;
        } catch (Exception e) {
            if (context.getSource().getSender() instanceof Player player) {
                String message = Localization.getPlayerMessage(
                        "localization.command.lang.reload.failed",
                        player.getUniqueId()
                );
                context.getSource().getSender().sendMessage(
                        LegacyComponentSerializer.legacySection().deserialize(message)
                );
            } else {
                context.getSource().getSender().sendMessage(
                        Component.text("Failed to reload: " + e.getMessage())
                );
            }
            return 0;
        }
    }

    private CompletableFuture<Suggestions> suggestLocales(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Locale.getAll().keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestPlugins(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        plugin.getLocaleProvider().getRegisteredPlugins().forEach(builder::suggest);
        return builder.buildFuture();
    }
}