package com.interordi.iobattlestats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayersTracking {
	
	IOBattleStats plugin;
	private Map< UUID , Location > locations = new HashMap< UUID, Location >();
	
	
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
	

}
