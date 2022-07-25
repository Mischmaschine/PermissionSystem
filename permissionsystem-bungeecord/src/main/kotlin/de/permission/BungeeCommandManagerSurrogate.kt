package de.permission

import co.aikar.commands.BungeeCommandManager
import co.aikar.commands.MessageKeys
import co.aikar.locales.MessageKeyProvider
import net.md_5.bungee.api.plugin.Plugin
import java.util.*

class BungeeCommandManagerSurrogate(plugin: Plugin) : BungeeCommandManager(plugin) {

    init {
        this.locales.addMessages(Locale.ENGLISH, buildMap<MessageKeyProvider, String> {
            put(MessageKeys.PERMISSION_DENIED, "You don't have permission to do that.")
            put(MessageKeys.PERMISSION_DENIED_PARAMETER, "You don't have permission to do that with {0}.")
            put(MessageKeys.ERROR_GENERIC_LOGGED, "An error has occurred: {0}")
            put(MessageKeys.UNKNOWN_COMMAND, "Command not found. Type '/help' for help.")
            put(MessageKeys.INVALID_SYNTAX, "§cInvalid syntax. Type '/help' for help.")
            put(MessageKeys.ERROR_PREFIX, "Error: ")
            put(MessageKeys.ERROR_PERFORMING_COMMAND, "An error has occurred while performing the command: {0}")
            put(MessageKeys.INFO_MESSAGE, "Info: {0}")
            put(MessageKeys.PLEASE_SPECIFY_ONE_OF, "§cError: Please specify one of (§a{valid}§c).")
            put(MessageKeys.MUST_BE_A_NUMBER, "Error: §c{num} must be a number.")
            put(MessageKeys.MUST_BE_MIN_LENGTH, "Error: §c{num} must be at least {min} characters long.")
            put(MessageKeys.MUST_BE_MAX_LENGTH, "Error: §c{num} must be at most {max} characters long.")
            put(MessageKeys.PLEASE_SPECIFY_AT_MOST, "Error: §c{num} must be at most {max}.")
            put(MessageKeys.PLEASE_SPECIFY_AT_LEAST, "Error: §c{num} must be at least {min}.")
            put(
                MessageKeys.NOT_ALLOWED_ON_CONSOLE,
                "Error: This command is not allowed on the console. Please use it in-game."
            )
            put(
                MessageKeys.COULD_NOT_FIND_PLAYER,
                "Error: Could not find player {0}. Please check the name and try again."
            )
            put(MessageKeys.NO_COMMAND_MATCHED_SEARCH, "No commands matched your search. Type '/help' for help.")
            put(MessageKeys.HELP_NO_RESULTS, "No results found. Type '/help' for help.")
            put(MessageKeys.HELP_FORMAT, "§6{commandprefix}{command} {parameters} §8┃ §7{description}")
            put(
                MessageKeys.HELP_HEADER,
                "§8§m------------┃§7 Help for §6{commandprefix}{command} §8§m-┃§m-------------"
            )
        })
    }

}