package de.permission.extensions

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

fun CommandSender.sendMessage(message: List<String>) {
    message.forEach {
        this.sendMessage(TextComponent("§8| ${ChatColor.of("#25e8a4")}ProxySystem §m| §7${it}"))
    }
}