package de.permission.extensions

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

fun CommandSender.sendMessage(vararg message: List<String>) {
    message.forEach { list ->
        list.forEach {
            this.sendMessage(TextComponent("§8§l┃ §3PermissionSystem §8┃ §7${it}"))
        }
    }
}