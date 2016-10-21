package com.interordi.iobattlestats;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

public class Heads {

	
	//Generate a player head
	public static ItemStack getCustomSkull(String target) {
		final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(target);	//p.getSkinName()
		meta.setDisplayName(ChatColor.RESET + target);
		item.setItemMeta(meta);
		return item;
	}
	
	
	//Give a head to a specific player
	public static void giveToPlayer(String target, Player user) {
		ItemStack head = getCustomSkull(target);
		PlayerInventory inv = user.getInventory();
		inv.addItem(head);
	}
}
