package com.interordi.iobattlestats;

//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Arrow;
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


public class DeathListener implements Listener {
	
	IOBattleStats plugin;
	
	
	public DeathListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		
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
				//Mob death by player, record
				
				String cause = event.getEntity().getLastDamageCause().getCause().toString();
				String itemName = "";
				
				if (killer.getItemInHand() != null && killer.getItemInHand().getItemMeta().hasDisplayName())
					itemName = killer.getItemInHand().getItemMeta().getDisplayName();
				
				this.plugin.data.recordDeath(killer.getName(), killed.getName(), killed.getWorld().getName(), cause, itemName, true, true);
				
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
		
		
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();
		if (lastDamage == null)
			return;
		
		
		//PvP-related death
		if (killer != null) {
			ItemStack head = Heads.getCustomSkull(killed.getDisplayName());
			
			//Drop the head on the ground at the slain players position
			event.getEntity().getWorld().dropItemNaturally(killed.getLocation(), head);
			
			//To add to drops, only works if keepInventory is disabled
			//event.getDrops().add(head);
			
			if (killer.getItemInHand() != null && killer.getItemInHand().getItemMeta().hasDisplayName())
				itemName = killer.getItemInHand().getItemMeta().getDisplayName();
			
			killerName = killer.getDisplayName();
		}
		//Death from other causes
		else {
			
			//Killed by mob
			if (lastDamage instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent)lastDamage;
				
				//If the damage came from an arrow, find said arrow's owner
				if (nEvent.getDamager() instanceof Arrow) {
					
					final Arrow arrow = (Arrow)nEvent.getDamager();
					Entity temp = (Entity)arrow.getShooter();
					killerName = temp.getName();
					
					//Use if telling apart players and mobs is needed
					//if (arrow.getShooter() instanceof Player) {
					//}
				}
				//Just a regular mob kill
				else {
					killerName = nEvent.getDamager().getName();
				}
			}
			//Other reason, whatever
			else {
				
			}
		}
		
		String cause = lastDamage.getCause().toString();
		//String customName = event.getEntity().getCustomName();
		
		this.plugin.data.recordDeath(killerName, killed.getDisplayName(), killed.getWorld().getName(), cause, itemName, true, true);
	}
	
}
