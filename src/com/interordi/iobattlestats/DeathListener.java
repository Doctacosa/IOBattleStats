package com.interordi.iobattlestats;

//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		//TODO: Do something here
		
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
			Player p = (Player)event.getEntity(); 
			
			@SuppressWarnings("unused")
			EntityDamageEvent entityDamageCause = p.getLastDamageCause();
		} else {
			
		}
		
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		Player killer = event.getEntity().getKiller();
		Player killed = event.getEntity().getPlayer();
		
		//System.out.println("Killer: " + killer.getDisplayName());
		//System.out.println("Killed: " + event.getEntity().getPlayer().getDisplayName());
		
		if (killer != null) {
			ItemStack head = Heads.getCustomSkull(killed.getDisplayName());
			
			//Drop the head on the ground at the slain players position
			event.getEntity().getWorld().dropItemNaturally(killed.getLocation(), head);
			
			//To add to drops, only works if keepInventory is disabled
			//event.getDrops().add(head);
		}
		
		String cause = event.getEntity().getLastDamageCause().getCause().toString();
		//String customName = event.getEntity().getCustomName();
		String customName = killer.getItemInHand().getItemMeta().getDisplayName();
		
		this.plugin.data.recordDeath(killer.getDisplayName(), killed.getDisplayName(), cause, customName, true, true);
	}
	
}
