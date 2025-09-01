package com.interordi.iobattlestats.utilities;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Heads {

	
	//Generate a player head
	public static ItemStack getCustomSkull(OfflinePlayer target, int amount) {
		final ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		if (meta != null) {
			meta.setOwningPlayer(target);	//p.getSkinName()
			meta.setDisplayName(ChatColor.RESET + target.getName());
			item.setItemMeta(meta);
		}
		return item;
	}

	public static ItemStack getCustomSkull(OfflinePlayer target) {
		return getCustomSkull(target, 1);
	}
	

	//Fetch a player's UUID without relying on Spigot's deprecated methods
	public static CompletableFuture<String> fetchUUIDAsync(String username) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				if (connection.getResponseCode() == 200) {
					InputStreamReader reader = new InputStreamReader(connection.getInputStream());
					JsonObject json = new Gson().fromJson(reader, JsonObject.class);
					reader.close();

					//Raw UUID without dashes
					return json.get("id").getAsString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	
	//Give a head to a specific player
	public static void giveToPlayer(Plugin plugin, String target, Player user, int amount) {

		Player headOf = Bukkit.getServer().getPlayer(target);
		if (headOf != null) {
			UUID uuid = headOf.getUniqueId();

			OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(uuid);

			ItemStack head = getCustomSkull(targetOffline, amount);
			PlayerInventory inv = user.getInventory();
			inv.addItem(head);
		} else {
			//Fetch UUID of offline and/or unknown player
			fetchUUIDAsync(target).thenAccept(rawUuid -> {
				if (rawUuid != null) {
					Bukkit.getScheduler().runTask(plugin, () -> {
						UUID uuid = UUID.fromString(rawUuid.replaceFirst( 
							"(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5" 
						));

						OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(uuid);

						ItemStack head = getCustomSkull(targetOffline, amount);
						PlayerInventory inv = user.getInventory();
						inv.addItem(head);

					});
				} else {
					Bukkit.getScheduler().runTask(plugin, () -> {
						user.sendMessage(ChatColor.RED + "Failed to get the player's UUID.");
					});
				}
			});
		}
	}
}
