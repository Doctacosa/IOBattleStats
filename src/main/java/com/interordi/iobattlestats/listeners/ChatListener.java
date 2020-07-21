package com.interordi.iobattlestats.listeners;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.interordi.iobattlestats.IOBattleStats;
import com.interordi.iobattlestats.utilities.Utilities;


@SuppressWarnings("unused")
public class ChatListener implements Listener {

	IOBattleStats plugin;
	
	
	public ChatListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		
		UUID uuid = event.getPlayer().getUniqueId();
		int nbWords = event.getMessage().split("\\s").length;
		
		this.plugin.data.recordBasicStat("chat_words", uuid, nbWords, event.getPlayer().getWorld().getName());
	}
}
