package com.interordi.iobattlestats.structures;

import java.util.UUID;

public class StatUpdate {
	
	public String table;
	public UUID uuid;
	public int value;
	public String world;
	
	
	public StatUpdate(String table, UUID uuid, int value, String world) {
		this.table = table;
		this.uuid = uuid;
		this.value = value;
		this.world = world;
	}


	public StatUpdate() {
		
	}

}
