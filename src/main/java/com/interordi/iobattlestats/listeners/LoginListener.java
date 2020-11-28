package com.interordi.iobattlestats.listeners;

import org.bukkit.event.player.*;

import java.util.UUID;

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
		this.plugin.data.recordPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().toString());
		
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		
		this.plugin.data.recordBasicStat("joins", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());

		//Load appropriate stats
		final UUID uuid = event.getPlayer().getUniqueId();

		this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				int streak = plugin.data.getPvPStreak(uuid);
				plugin.tracker.setKillStreak(uuid, streak);
			}
		});
	}
	
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		this.plugin.removePlayer(event.getPlayer());
	}
}
