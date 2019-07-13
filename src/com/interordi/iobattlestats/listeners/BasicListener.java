package com.interordi.iobattlestats.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.interordi.iobattlestats.IOBattleStats;


//Basic listener to catch simple events and immediately save them
public class BasicListener implements Listener {

	IOBattleStats plugin;
	
	
	public BasicListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		this.plugin.data.recordBasicStat("beds_entered", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		this.plugin.data.recordBasicStat("buckets_emptied", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
		this.plugin.data.recordBasicStat("buckets_filled", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		this.plugin.data.recordBasicStat("commands", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
		this.plugin.data.recordBasicStat("eggs_thrown", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event) {
		//TODO: Keep only fish in results, or store the item type caught
		if (event.getCaught() != null)
			this.plugin.data.recordBasicStat("fish_caught", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		//TODO: Keep only the results of eating solids?
		this.plugin.data.recordBasicStat("consumed", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
		this.plugin.data.recordBasicStat("shears", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		//TODO: Record teleport by type
		this.plugin.data.recordBasicStat("teleports", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		this.plugin.data.recordBasicStat("kicks", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
		if (event.getAmount() > 0)
			this.plugin.data.recordBasicStat("xp_gained", event.getPlayer().getUniqueId(), event.getAmount(), event.getPlayer().getWorld().getName());
	}
	
	
	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player)event.getEntity().getShooter();
			this.plugin.data.recordBasicStat("arrows", player.getUniqueId(), 1, player.getWorld().getName());
		}
	}
	
	
	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		this.plugin.data.recordBasicStat("change_world", event.getPlayer().getUniqueId(), 1, event.getPlayer().getWorld().getName());
	}
}
