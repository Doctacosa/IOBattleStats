package com.interordi.iobattlestats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayersTracking {
	
	IOBattleStats plugin;
	private Map< UUID , Location > locations = new HashMap< UUID, Location >();
	private Map< UUID, Integer > pvpStreaks = new HashMap< UUID, Integer>();
	
	
	PlayersTracking(IOBattleStats plugin) {
		this.plugin = plugin;
	}
	
	
	public void add(Player player) {
		locations.put(player.getUniqueId(), null);
	}
	
	
	public void remove(Player player) {
		locations.remove(player.getUniqueId());
	}


	//Get the last known position of a player
	public Location getLastPosition(Player player) {
		return locations.get(player.getUniqueId());
	}
	
	
	//Set the current position of a player
	public void setPosition(Player player, Location pos) {
		locations.put(player.getUniqueId(), pos);
	}
	
	
	//Add 1 to the player's kill streak
	public void registerKill(Player player) {
		if (pvpStreaks.containsKey(player.getUniqueId())) {
			int kills = pvpStreaks.get(player.getUniqueId());
			pvpStreaks.put(player.getUniqueId(), kills + 1);
		} else {
			pvpStreaks.put(player.getUniqueId(), 1);
		}
	}
	
	
	//Player died - reset kill streak!
	public void resetKills(Player player) {
		pvpStreaks.put(player.getUniqueId(), 0);
	}
	
	
	//Get the list of kill streaks to save them
	public Map< UUID, Integer > getKillStreaks() {
		return pvpStreaks;
	}
	

}
