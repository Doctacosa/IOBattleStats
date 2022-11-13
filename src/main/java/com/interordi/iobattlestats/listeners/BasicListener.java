package com.interordi.iobattlestats.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;

import com.interordi.iobattlestats.IOBattleStats;


//Basic listener to catch simple events and immediately save them
public class BasicListener implements Listener {

	IOBattleStats plugin;
	
	
	public BasicListener(IOBattleStats plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"beds_entered",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"buckets_emptied",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"buckets_filled",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"commands",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"eggs_thrown",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		//TODO: Keep only fish in results, or store the item type caught
		if (event.getCaught() != null)
			this.plugin.data.recordBasicStat(
				"fish_caught",
				event.getPlayer().getUniqueId(),
				1,
				event.getPlayer().getWorld().getName()
			);
	}
	
	
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		//TODO: Keep only the results of eating solids?
		this.plugin.data.recordBasicStat(
			"consumed",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"shears",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		//TODO: Record teleport by type
		this.plugin.data.recordBasicStat(
			"teleports",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"kicks",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onPlayerExpChangeEvent(PlayerExpChangeEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		if (event.getAmount() > 0)
			this.plugin.data.recordBasicStat(
				"xp_gained",
				event.getPlayer().getUniqueId(),
				event.getAmount(),
				event.getPlayer().getWorld().getName()
			);
	}
	
	
	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;
		this.plugin.data.recordBasicStat(
			"change_world",
			event.getPlayer().getUniqueId(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onEnchantItemEvent(EnchantItemEvent event) {
		if (!event.getEnchanter().hasPermission("iobattlestats.track"))
			return;

		String itemName = "";
		if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().hasDisplayName())
			itemName = event.getItem().getItemMeta().getDisplayName();

		this.plugin.data.recordItemNamedStat(
			"enchants",
			event.getEnchanter().getUniqueId(),
			event.getItem().getType().toString(),
			itemName,
			1,
			event.getEnchanter().getWorld().getName()
		);
	}


	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		if (!event.getPlayer().hasPermission("iobattlestats.track"))
			return;

		this.plugin.data.recordItemStat(
			"inventories",
			event.getPlayer().getUniqueId(),
			event.getInventory().getType().toString(),
			1,
			event.getPlayer().getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player)event.getEntity();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		this.plugin.data.recordItemStat(
			"arrows",
			player.getUniqueId(),
			event.getProjectile().toString(),
			1,
			player.getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onEntityTameEvent(EntityTameEvent event) {
		if (!(event.getOwner() instanceof Player))
			return;
		Player player = (Player)event.getOwner();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		this.plugin.data.recordItemStat(
			"tamings",
			player.getUniqueId(),
			event.getEntityType().toString(),
			1,
			player.getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player)event.getEntity();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		if (event.getAmount() <= 0)
			return;

		this.plugin.data.recordItemStat(
			"heals",
			player.getUniqueId(),
			event.getRegainReason().toString(),
			(int) event.getAmount(),
			player.getWorld().getName()
		);
	}
	
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player)event.getEntity();
		if (!player.hasPermission("iobattlestats.track"))
			return;

		int change = event.getFoodLevel() - player.getFoodLevel();
		if (change <= 0)
			return;

		this.plugin.data.recordItemStat(
			"hunger_refills",
			player.getUniqueId(),
			event.getItem().getType().toString(),
			change,
			player.getWorld().getName()
		);
	}


	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent event) {
		for (LivingEntity target : event.getAffectedEntities()) {
			if (!(target instanceof Player))
				continue;
			Player player = (Player)target;

			//Save a record for each effect
			for (PotionEffect effect : event.getPotion().getEffects()) {
				this.plugin.data.recordItemStat(
					"splashes",
					player.getUniqueId(),
					effect.getType().toString(),
					1,
					player.getWorld().getName()
				);
			}
		}
		
	}

}
