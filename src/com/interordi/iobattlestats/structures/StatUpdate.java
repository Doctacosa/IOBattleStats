package com.interordi.iobattlestats.structures;

import java.util.UUID;

public class StatUpdate {
	
	public int format;
	public String table;
	public UUID uuid;
	public int amount;
	public String world;
	public String value;
	public String name;
	
	public static final int BASIC = 1;
	public static final int VALUE = 2;
	public static final int NAMED = 3;
	
	
	public StatUpdate(int format, String table, UUID uuid, int amount, String world) {
		this(format, table, uuid, amount, world, null, null);
	}


	public StatUpdate(int format, String table, UUID uuid, int amount, String world, String value) {
		this(format, table, uuid, amount, world, value, null);
	}


	public StatUpdate(int format, String table, UUID uuid, int amount, String world, String value, String name) {
		this.format = format;
		this.table = table;
		this.uuid = uuid;
		this.amount = amount;
		this.world = world;
		this.value = value;
		this.name = name;
	}


	public StatUpdate() {
		
	}

}
