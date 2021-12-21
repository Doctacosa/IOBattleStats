package com.interordi.iobattlestats.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public class Heads {

	
	//Generate a player head
	public static ItemStack getCustomSkull(OfflinePlayer target, int amount) {
		final ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwningPlayer(target);	//p.getSkinName()
		meta.setDisplayName(ChatColor.RESET + target.getName());
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCustomSkull(OfflinePlayer target) {
		return getCustomSkull(target, 1);
	}
	
	
	//Give a head to a specific player
	public static void giveToPlayer(OfflinePlayer target, Player user, int amount) {
		ItemStack head = getCustomSkull(target, amount);
		PlayerInventory inv = user.getInventory();
		inv.addItem(head);
	}
}
