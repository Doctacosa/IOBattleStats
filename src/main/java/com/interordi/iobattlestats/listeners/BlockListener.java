package com.interordi.iobattlestats.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.interordi.iobattlestats.IOBattleStats;


//Basic listener to catch simple events and immediately save them
public class BlockListener implements Listener {

	IOBattleStats plugin;
	
	
	public BlockListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		String block = event.getBlock().getBlockData().getMaterial().toString();
		this.plugin.data.recordItemStat(
			"blocks_broken",
			event.getPlayer().getUniqueId(),
			block,
			1,
			event.getPlayer().getWorld().getName()
		);
	}


	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		String block = event.getBlock().getBlockData().getMaterial().toString();
		this.plugin.data.recordItemStat(
			"blocks_placed",
			event.getPlayer().getUniqueId(),
			block,
			1,
			event.getPlayer().getWorld().getName()
		);
	}


	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player) ||
			event.getSlotType() != InventoryType.SlotType.RESULT ||
			event.getCurrentItem().getItemMeta() == null ||
			event.getCurrentItem() == null)
			return;

		if (!event.getWhoClicked().hasPermission("iobattlestats.track"))
			return;
		
		Player player = (Player)event.getWhoClicked();
		String item = event.getCurrentItem().getType().toString();
		String name = event.getCurrentItem().getItemMeta().getDisplayName();
		int amount = event.getCurrentItem().getAmount();

		if (event.isShiftClick()) {
			int itemsChecked = 0;
			int possibleCreations = 1;
			int amountPossible = 0;

			//Exclude the first entry, it's the results slot
			ItemStack[] itemGrid = event.getClickedInventory().getContents();
			for (int i = 1; i < itemGrid.length; i++) {
				ItemStack is = itemGrid[i];
				if (itemGrid == null || is == null || is.getType().equals(Material.AIR))
					continue;

				if (itemsChecked == 0) {
					possibleCreations = is.getAmount();
					itemsChecked++;
				} else {
					possibleCreations = Math.min(possibleCreations, is.getAmount());
				}
			}

			//Max amount that could be crafted based on supplies
			int amountMax = event.getCurrentItem().getAmount() * possibleCreations;

			//Max amount that could be crafted based on the available slots
			ItemStack is = event.getCurrentItem();

			//NOTE: 36 = standard inventory, excluding player armor and offhand
			for (int s = 0; s < 36; s++) {
				ItemStack test = player.getInventory().getItem(s);
				if (test == null || test.getType() == Material.AIR) {
					amountPossible += is.getMaxStackSize();
					continue;
				}
				if (test.isSimilar(is)) {
					amountPossible += is.getMaxStackSize() - test.getAmount();
				}
			}

			amount = Math.min(amountMax, amountPossible);
		}
		


		if (event.getInventory().getType().equals(InventoryType.CRAFTING) ||
			event.getInventory().getType().equals(InventoryType.WORKBENCH) ||
			event.getInventory().getType().equals(InventoryType.STONECUTTER))
			this.plugin.data.recordItemNamedStat(
				"crafted",
				player.getUniqueId(),
				item,
				name,
				amount,
				player.getWorld().getName()
			);
		else if (event.getInventory().getType().equals(InventoryType.MERCHANT))
			this.plugin.data.recordItemNamedStat(
				"trades",
				player.getUniqueId(),
				item,
				name,
				amount,
				player.getWorld().getName()
			);
	}


	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		//TODO: Avoid /give triggering this
		String item = event.getItemDrop().getItemStack().getType().toString();
		String name = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();

		this.plugin.data.recordItemNamedStat(
			"item_dropped",
			event.getPlayer().getUniqueId(),
			item,
			name,
			event.getItemDrop().getItemStack().getAmount(),
			event.getPlayer().getWorld().getName()
		);
	}


	@EventHandler
	public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		String item = event.getItem().getItemStack().getType().toString();
		String name = event.getItem().getItemStack().getItemMeta().getDisplayName();

		Player player = (Player)event.getEntity();
		if (player.hasPermission("iobattlestats.track"))
			this.plugin.data.recordItemNamedStat(
				"item_picked_up",
				player.getUniqueId(),
				item,
				name,
				event.getItem().getItemStack().getAmount(),
				player.getWorld().getName()
			);
	}


	@EventHandler
	public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		String item = event.getBrokenItem().getType().toString();
		this.plugin.data.recordItemStat(
			"item_broken",
			event.getPlayer().getUniqueId(),
			item,
			1,
			event.getPlayer().getWorld().getName()
		);
	}
}