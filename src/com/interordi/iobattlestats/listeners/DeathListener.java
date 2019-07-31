package com.interordi.iobattlestats.listeners;

//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.interordi.iobattlestats.IOBattleStats;
import com.interordi.iobattlestats.utilities.Heads;
import com.interordi.iobattlestats.utilities.Utilities;


public class DeathListener implements Listener {
	
	IOBattleStats plugin;
	
	
	public DeathListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
		String killerName = "";
		String killedName = "";
		boolean playerSource = false;
		boolean playerTarget = false;
		
		//entity = PLAYER / CHICKEN
		//getName() -> target = Chicken / DrCossack / Zombo.com
		//getCustomName() = null / Zombo.com
		
		/*
		EntityType entity = event.getEntityType();
		this.plugin.getServer().broadcastMessage("Entity death to " + entity + " - " + event.getEntity().getName());
		*/
		
		if (event.getEntity().getLastDamageCause() == null)
			return;
		
		if (event.getEntity() instanceof Player) {
			//Ignore PvP kills, handled by the method below
			
			/*
			Player p = (Player)event.getEntity();
			
			@SuppressWarnings("unused")
			EntityDamageEvent entityDamageCause = p.getLastDamageCause();
			*/
			
		} else {
			
			Player killer = event.getEntity().getKiller();
			LivingEntity killed = event.getEntity();
			
			if (killer != null) {
				//Mob killed by player, record
				
				String cause = event.getEntity().getLastDamageCause().getCause().toString();
				String itemName = "";
				
				//Check the off hand, then the primary hand
				ItemStack held = killer.getInventory().getItemInOffHand();
				if (held != null && held.getItemMeta() != null && held.getItemMeta().hasDisplayName()) {
					itemName = held.getItemMeta().getDisplayName();
					cause = held.getType().toString();
				}
				
				if (itemName == null || itemName.equals("")) {
					held = killer.getInventory().getItemInMainHand();
					if (held != null && held.getItemMeta() != null && held.getItemMeta().hasDisplayName()) {
						itemName = held.getItemMeta().getDisplayName();
						cause = held.getType().toString();
					}
				}
				
				killerName = killer.getUniqueId().toString();
				killedName = killed.getType().toString();
				playerSource = true;
				
				if (killer.hasPermission("iobattlestats.track")) {
					this.plugin.data.recordDeath(killerName, killedName, killed.getWorld().getName(), cause, itemName, playerSource, playerTarget);
				}
				
			} else {
				//Random mob death, ignore
			}
		}
		
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		Player killer = event.getEntity().getKiller();	//NOTE: Only set for PvP deaths
		Player killed = event.getEntity().getPlayer();
		String itemName = "";
		String killerName = "";
		String killedName = killed.getUniqueId().toString();
		String cause = "";
		boolean playerSource = false;
		boolean playerTarget = true;
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();
		
		
		//PvP-related death
		if (killer != null) {
			ItemStack head = Heads.getCustomSkull(killed);
			
			//Drop the head on the ground at the slain players position
			event.getEntity().getWorld().dropItemNaturally(killed.getLocation(), head);
			
			//To add to drops, only works if keepInventory is disabled
			//event.getDrops().add(head);
			
			//Check the off hand, then the primary hand
			ItemStack held = killer.getInventory().getItemInOffHand();
			if (held != null) {
				cause = held.getType().toString();
				if (held.getItemMeta() != null && held.getItemMeta().hasDisplayName())
					itemName = held.getItemMeta().getDisplayName();
			}
			
			if (itemName == null || itemName.equals("")) {
				held = killer.getInventory().getItemInMainHand();
				if (held != null) {
					cause = held.getType().toString();
					if (held.getItemMeta() != null && held.getItemMeta().hasDisplayName())
						itemName = held.getItemMeta().getDisplayName();
				}
			}
			
			//Update ongoing kill streaks
			plugin.tracker.registerKill(killer);
			plugin.tracker.resetKills(killed);
			
			killerName = killer.getUniqueId().toString();
			playerSource = true;
			playerTarget = true;
		}
		//Death from other causes
		else {
			
			playerSource = false;
			
			//Killed by mob
			if (lastDamage != null && lastDamage instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent)lastDamage;
				
				//If the damage came from a projectile, find said arrow's owner
				if (nEvent.getDamager() instanceof Projectile) {
					
					final Projectile projectile = (Projectile)nEvent.getDamager();
					Entity temp = (Entity)projectile.getShooter();
					killerName = temp.getType().toString();
					cause = Utilities.getDamagerType(nEvent.getDamager());
					
					//Use if telling apart players and mobs is needed
					//if (arrow.getShooter() instanceof Player) {
					//}
				}
				//Just a regular mob kill
				else {
					killerName = nEvent.getDamager().getType().toString();
				}
			}
			//Other reason, whatever
			else {
				
			}
		}
		
		if (cause == null || cause.equals("")) {
			if (lastDamage != null)
				cause = lastDamage.getCause().toString();
			else
				cause = "Natural causes";
		}
		//String customName = event.getEntity().getCustomName();
		
		if ((playerSource && killer.hasPermission("iobattlestats.track")) ||
			(playerTarget && killed.hasPermission("iobattlestats.track"))) {
			this.plugin.data.recordDeath(killerName, killedName, killed.getWorld().getName(), cause, itemName, playerSource, playerTarget);
		}
	}
	
}
