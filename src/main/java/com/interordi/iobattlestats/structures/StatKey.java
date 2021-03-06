package com.interordi.iobattlestats.structures;

import java.util.UUID;

public class StatKey {
	
	public UUID uuid;
	public String world;
	public String value;
	public String name;
	public String extra;
	
	
	public StatKey(UUID uuid, String world, String value, String name) {
		this.uuid = uuid;
		this.world = world;
		this.value = value;
		this.name = name;
	}

	public StatKey(UUID uuid, String world, String value, String name, String extra) {
		this.uuid = uuid;
		this.world = world;
		this.value = value;
		this.name = name;
		this.extra = extra;
	}

}
