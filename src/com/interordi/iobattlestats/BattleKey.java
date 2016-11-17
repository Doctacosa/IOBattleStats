package com.interordi.iobattlestats;

public class BattleKey {

	//Store a unique key for storage and comparison
	
	public String source;
	public String target;
	public String world;
	public String cause;
	public String weaponName;
	public int isPlayerSource;
	public int isPlayerTarget;
	
	
	public BattleKey(String source, String target, String world, String cause, String weaponName, int isPlayerSource, int isPlayerTarget) {
		this.source = source;
		this.target = target;
		this.world = world;
		this.cause = cause;
		this.weaponName = weaponName;
		this.isPlayerSource = isPlayerSource;
		this.isPlayerTarget = isPlayerTarget;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof BattleKey) {
			BattleKey bk = (BattleKey)obj;
			return (source.equals(bk.source) &&
					target.equals(bk.target) &&
					world.equals(bk.world) &&
					cause.equals(bk.cause) &&
					weaponName.equals(bk.weaponName));
		}	
		
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return (source + "|" + target + "|" + world + "|" + cause + "|" + weaponName).hashCode();
	}
	
	
	@Override
	public String toString() {
		return source + "|" + target + "|" + world + "|" + cause + "|" + weaponName;
	}

}
