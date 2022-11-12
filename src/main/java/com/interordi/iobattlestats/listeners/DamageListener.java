package com.interordi.iobattlestats.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.interordi.iobattlestats.IOBattleStats;
import com.interordi.iobattlestats.utilities.Utilities;


@SuppressWarnings("unused")
public class DamageListener implements Listener {

	IOBattleStats plugin;
	
	
	public DamageListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	//TODO: Catch falling and other causes...?
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		
		//Attack from a mob or another player
		if (event instanceof EntityDamageByEntityEvent) {
			onEntityDamage((EntityDamageByEntityEvent)event);
			
		//Damage from a block
		} else if (event instanceof EntityDamageByBlockEvent) {
			onBlockDamage((EntityDamageByBlockEvent)event);
			
		//Other types of damage
		} else {
			onOtherDamage(event);
		}
	}
	
	
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		
		float damage = Math.round(event.getFinalDamage() * 10) / 10;
		
		//Ignore no damage
		if (damage == 0)
			return;
		
		boolean playerSource = (event.getDamager() != null && event.getDamager() instanceof Player);
		boolean playerTarget = (event.getEntity() != null && event.getEntity() instanceof Player);
		boolean projectileSource = (event.getDamager() != null && event.getDamager() instanceof Projectile);
		
		//No human interaction = don't care
		if (!playerSource && !playerTarget && !projectileSource) {
			return;
		}
		
		Entity attacker = event.getDamager();
		Entity target = event.getEntity();
		String damageSource = event.getCause().toString();
		String weaponName = "";
		String attackerName = attacker.getType().toString();
		String targetName = target.getType().toString();
		
		//Damage source, if known
		if (playerSource) {
			attackerName = attacker.getUniqueId().toString();
			
			//Get the weapon's name if one was used
			if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				
				//Check the off hand, then the main one
				ItemStack held = ((Player)attacker).getInventory().getItemInOffHand();
				if (held != null) {
					damageSource = held.getType().toString();
					if (held.getItemMeta() != null)
						weaponName = held.getItemMeta().getDisplayName();
				}
				
				if (weaponName == null || weaponName.equals("")) {
					held = ((Player)attacker).getInventory().getItemInMainHand();
					if (held != null) {
						damageSource = held.getType().toString();
						if (held.getItemMeta() != null)
							weaponName = held.getItemMeta().getDisplayName();
					}
				}
			}
		}
		
		if (playerTarget) {
			targetName = target.getUniqueId().toString();
		}
		
		//If the damage came from a projectile, find said arrow's owner
		if (event.getDamager() instanceof Projectile) {
			
			final Projectile projectile = (Projectile)event.getDamager();
			attacker = (Entity)projectile.getShooter();
			
			if (attacker instanceof Player)
				playerSource = true;
			
			if (playerSource) {
				attackerName = attacker.getUniqueId().toString(); 
			}
			damageSource = Utilities.getDamagerType(event.getDamager());
			
			//TODO: Detect when the player damages a mob using projectiles
			
			//Use if telling apart players and mobs is needed
			//if (projectile.getShooter() instanceof Player) {
			//}
		}
		
		//No human interaction after all = don't care
		if (!playerSource && !playerTarget) {
			return;
		}
		
		if (weaponName == null)
			weaponName = "";
		
		if ((playerSource && attacker.hasPermission("iobattlestats.track")) ||
			(playerTarget && target.hasPermission("iobattlestats.track"))) {
			//Bukkit.getLogger().info("PLAYER " + target.getName() + ", " + event.getFinalDamage() + " damage by ENTITY through " + event.getCause());
			this.plugin.data.recordDamage(
				attackerName,
				targetName,
				attacker.getWorld().getName(),
				damage,
				damageSource,
				weaponName,
				playerSource,
				playerTarget
			);
		}
	}
	
	
	public void onBlockDamage(EntityDamageByBlockEvent event) {
		
		float damage = Math.round(event.getFinalDamage() * 10) / 10;
		
		//Ignore no damage
		if (damage == 0)
			return;
		
		//Don't care about what happens to mobs...
		if (!(event.getEntity() instanceof Player))
			return;
			
		Player target = (Player)event.getEntity();
		String targetName = target.getUniqueId().toString();
		
		if (target.hasPermission("iobattlestats.track")) {
			//Bukkit.getLogger().info("PLAYER " + target.getName() + ", " + event.getFinalDamage() + " damage by BLOCK through " + event.getCause());
			this.plugin.data.recordDamage(
				"BLOCK",
				targetName,
				target.getWorld().getName(),
				damage,
				event.getCause().toString(),
				"",
				false,
				true
			);
		}
	}
	
	
	public void onOtherDamage(EntityDamageEvent event) {
		
		float damage = Math.round(event.getFinalDamage() * 10) / 10;
		
		//entity = SKELETON / PLAYER
		//event.getCause() = ENTITY_ATTACK / FIRE / FIRE_TICK
		
		//Don't care about what happens to mobs...
		if (!(event.getEntity() instanceof Player))
			return;
			
		//Handle player being damaged
		Player target = (Player)event.getEntity();
		String targetName = target.getUniqueId().toString();
		
		if (target.hasPermission("iobattlestats.track")) {
			//Bukkit.getLogger().info("PLAYER " + target + ", " + event.getFinalDamage() + " damage by OTHER through " + event.getCause());
			this.plugin.data.recordDamage(
				"OTHER",
				targetName,
				target.getWorld().getName(),
				damage,
				event.getCause().toString(),
				"",
				false,
				true
			);
		}
	}
}
