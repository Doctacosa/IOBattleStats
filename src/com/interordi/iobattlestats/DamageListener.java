package com.interordi.iobattlestats;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


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
		
		boolean playerSource = (event.getDamager() instanceof Player);
		boolean playerTarget = (event.getEntity() instanceof Player);
		
		//No human interaction = don't care
		if (!playerSource && !playerTarget) {
			return;
		}
		
		Entity attacker = event.getDamager();
		Entity target = event.getEntity();
		String damageSource = event.getCause().toString();
		
		//Damage source, if known
		if (playerSource)
			damageSource = ((Player)attacker).getItemInHand().getType().toString();
		
		if (damageSource.equals("Arrow")) {
			//TODO: Find arrow's owner
		}

		this.plugin.data.recordDamage(attacker.getName(), target.getName(), damage, damageSource, playerSource, playerTarget);
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
		
		//this.plugin.getServer().broadcastMessage("PLAYER " + target.getName() + ", " + event.getFinalDamage() + " damage by BLOCK through " + event.getCause());
		this.plugin.data.recordDamage("BLOCK", target.getName(), damage, event.getCause().toString(), false, true);
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
		
		//this.plugin.getServer().broadcastMessage("PLAYER " + target + ", " + event.getFinalDamage() + " damage by OTHER through " + event.getCause());
		this.plugin.data.recordDamage("OTHER", target.getName(), damage, event.getCause().toString(), false, true);
	}
}
