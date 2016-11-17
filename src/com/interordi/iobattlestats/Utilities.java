package com.interordi.iobattlestats;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
//import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;

public class Utilities {

	public static String getDamagerType(Entity damager) {
		String cause = "";
		
		if (damager instanceof Arrow)
			cause = ((Arrow)damager).getType().toString();
		else if (damager instanceof DragonFireball)
			cause = ((DragonFireball)damager).getType().toString();
		else if (damager instanceof Egg)
			cause = ((Egg)damager).getType().toString();
		else if (damager instanceof EnderPearl)
			cause = ((EnderPearl)damager).getType().toString();
		else if (damager instanceof Fireball)
			cause = ((Fireball)damager).getType().toString();
		/*
		else if (damager instanceof Fish)
			cause = ((Fish)damager).getType().toString();
		*/
		else if (damager instanceof FishHook)
			cause = ((FishHook)damager).getType().toString();
		else if (damager instanceof LargeFireball)
			cause = ((LargeFireball)damager).getType().toString();
		else if (damager instanceof LingeringPotion)
			cause = ((LingeringPotion)damager).getType().toString();
		/*
		else if (damager instanceof LlamaSpit)
			cause = ((LlamaSpit)damager).getType().toString();
		*/
		else if (damager instanceof ShulkerBullet)
			cause = ((ShulkerBullet)damager).getType().toString();
		else if (damager instanceof SmallFireball)
			cause = ((SmallFireball)damager).getType().toString();
		else if (damager instanceof Snowball)
			cause = ((Snowball)damager).getType().toString();
		else if (damager instanceof SpectralArrow)
			cause = ((SpectralArrow)damager).getType().toString();
		else if (damager instanceof SplashPotion)
			cause = ((SplashPotion)damager).getType().toString();
		else if (damager instanceof ThrownExpBottle)
			cause = ((ThrownExpBottle)damager).getType().toString();
		else if (damager instanceof ThrownPotion)
			cause = ((ThrownPotion)damager).getType().toString();
		else if (damager instanceof TippedArrow)
			cause = ((TippedArrow)damager).getType().toString();
		else if (damager instanceof WitherSkull)
			cause = ((WitherSkull)damager).getType().toString();
		
		return cause;

	}
}
