package com.interordi.iobattlestats.listeners;

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
		String block = event.getBlock().getBlockData().getMaterial().toString();
		this.plugin.data.recordItemStat("blocks_broken", event.getPlayer().getUniqueId(), block, 1, event.getPlayer().getWorld().getName());
	}


	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		String block = event.getBlock().getBlockData().getMaterial().toString();
		this.plugin.data.recordItemStat("blocks_placed", event.getPlayer().getUniqueId(), block, 1, event.getPlayer().getWorld().getName());
	}


	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player) ||
			(!event.getInventory().getType().equals(InventoryType.CRAFTING) && !event.getInventory().getType().equals(InventoryType.WORKBENCH)) ||
			event.getSlotType() != InventoryType.SlotType.RESULT ||
			event.getCurrentItem() == null)
			return;
		
		//TODO: Detect shift-click...?
		event.isShiftClick();
		
		Player player = (Player)event.getWhoClicked();
		String item = event.getCurrentItem().getType().toString();
		String name = event.getCurrentItem().getItemMeta().getDisplayName();
		int amount = event.getCurrentItem().getAmount();

		this.plugin.data.recordItemNamedStat("crafted", player.getUniqueId(), item, name, amount, player.getWorld().getName());
	}


	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		//TODO: Avoid /give triggering this
		String item = event.getItemDrop().getItemStack().getType().toString();
		String name = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();

		this.plugin.data.recordItemNamedStat("item_dropped", event.getPlayer().getUniqueId(), item, name, 1, event.getPlayer().getWorld().getName());
	}


	@EventHandler
	public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		
		String item = event.getItem().getItemStack().getType().toString();
		String name = event.getItem().getItemStack().getItemMeta().getDisplayName();
		
		Player player = (Player)event.getEntity();
		this.plugin.data.recordItemNamedStat("item_picked_up", player.getUniqueId(), item, name, 1, player.getWorld().getName());
	}


	@EventHandler
	public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
		String item = event.getBrokenItem().getType().toString();
		this.plugin.data.recordItemStat("item_broken", event.getPlayer().getUniqueId(), item, 1, event.getPlayer().getWorld().getName());
	}
}