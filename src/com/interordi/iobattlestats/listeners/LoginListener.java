package com.interordi.iobattlestats.listeners;

import org.bukkit.event.player.*;

import com.interordi.iobattlestats.IOBattleStats;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class LoginListener implements Listener {
	
	private IOBattleStats plugin;
	
	public LoginListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler	//Annotation - EventPriority.NORMAL by default
	public void onPlayerLogin(PlayerJoinEvent event) {
		this.plugin.addPlayer(event.getPlayer());
		this.plugin.data.recordPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		
		this.plugin.data.recordBasicStat("joins", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		this.plugin.removePlayer(event.getPlayer());
	}
}
