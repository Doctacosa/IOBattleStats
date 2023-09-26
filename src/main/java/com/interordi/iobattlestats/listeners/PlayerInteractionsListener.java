package com.interordi.iobattlestats.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.interordi.iobattlestats.IOBattleStats;


public class PlayerInteractionsListener implements Listener {
	
	IOBattleStats plugin;
	
	
	public PlayerInteractionsListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	//https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/PlayerInteractEntityEvent.html
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		//Return as early as possible...

		ItemStack sourceItem = event.getItem();
		Material sourceMaterial = event.getMaterial();

		//Using flint and steel
		if (sourceMaterial == Material.FLINT_AND_STEEL) {
			String itemName = "";
			if (sourceItem != null && sourceItem.getItemMeta() != null && sourceItem.getItemMeta().hasDisplayName())
				itemName = sourceItem.getItemMeta().getDisplayName();

			this.plugin.data.recordItemNamedStat(
				"lighters",
				player.getUniqueId(),
				sourceMaterial.toString(),
				itemName,
				1,
				player.getWorld().getName()
			);
		
		} else if (sourceMaterial == Material.ENDER_PEARL) {
			String itemName = "";
			if (sourceItem != null && sourceItem.getItemMeta() != null && sourceItem.getItemMeta().hasDisplayName())
				itemName = sourceItem.getItemMeta().getDisplayName();

			this.plugin.data.recordItemNamedStat(
				"enderpearls",
				player.getUniqueId(),
				sourceMaterial.toString(),
				itemName,
				1,
				player.getWorld().getName()
			);
		}
		
	}


	//https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/PlayerInteractEntityEvent.html
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();

		//Entity that was right-clicked
		Entity target = event.getRightClicked();

		//Cow milking
		if (target.getType() == EntityType.COW ||
			target.getType() == EntityType.MUSHROOM_COW) {
			if (mainHand.getType() == Material.BUCKET ||
				offHand.getType() == Material.BUCKET) {

				this.plugin.data.recordItemStat(
					"milk",
					player.getUniqueId(),
					target.getType().toString(),
					1,
					player.getWorld().getName()
				);
			}

		}
	}
}
