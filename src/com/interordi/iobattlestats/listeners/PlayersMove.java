package com.interordi.iobattlestats.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.interordi.iobattlestats.IOBattleStats;


@SuppressWarnings("unused")
public class PlayersMove implements Runnable, Listener {
	
	
	IOBattleStats plugin = null;

	
	public PlayersMove(IOBattleStats plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	
	@Override
	public void run() {
		
		//Get the list of online players
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			Location newPos = player.getLocation();
			Location oldPos = plugin.tracker.getLastPosition(player);
			
			//Compute the distance between two events if possible
			if (oldPos != null && newPos != null && oldPos.getWorld() == newPos.getWorld()) {
				double movement = oldPos.distance(newPos);
				
				if (movement > 0.5 && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
					String type = "";
					
					if (player.isFlying())
						type = "flight";
					else if (player.isInsideVehicle()) {
						Entity vehicle = player.getVehicle();
						if (vehicle.getType() == EntityType.HORSE ||
							vehicle.getType() == EntityType.SKELETON_HORSE ||
							vehicle.getType() == EntityType.ZOMBIE_HORSE ||
							vehicle.getType() == EntityType.MULE ||
							vehicle.getType() == EntityType.DONKEY)
							type = "horse";
						else if (vehicle.getType() == EntityType.PIG)
							type = "pig";
						else if (vehicle.getType() == EntityType.LLAMA)
							type = "llama";
						else if (vehicle.getType() == EntityType.BOAT)
							type = "boat";
						else
							type = "minecart";
					}
					else
						type = "walk";
					
					this.plugin.data.recordItemStat("move", player.getUniqueId(), type, (int)Math.round(movement), player.getWorld().getName());
				}
			}
			
			plugin.tracker.setPosition(player, newPos);
			
		}
	}
	
	
	//On teleport, immediately set the new position
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		plugin.tracker.setPosition(e.getPlayer(), e.getTo());
	}
}
