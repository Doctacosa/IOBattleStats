package com.interordi.iobattlestats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public class Heads {

	
	//Generate a player head
	public static ItemStack getCustomSkull(OfflinePlayer target) {
		final ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(target);	//p.getSkinName()
		meta.setDisplayName(ChatColor.RESET + target.getName());
		item.setItemMeta(meta);
		return item;
	}
	
	
	//Give a head to a specific player
	public static void giveToPlayer(OfflinePlayer target, Player user) {
		ItemStack head = getCustomSkull(target);
		PlayerInventory inv = user.getInventory();
		inv.addItem(head);
	}
}
