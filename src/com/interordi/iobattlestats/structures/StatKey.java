package com.interordi.iobattlestats.structures;

import java.util.UUID;

public class StatKey {
	
	public UUID uuid;
	public String world;
	
	
	public StatKey(UUID uuid, String world) {
		this.uuid = uuid;
		this.world = world;
	}

}
