package com.interordi.iobattlestats;


//Object to hold a damage pair, used to quickly store and retrive information
//Especially useful for data saved in maps
public class DamageElement {
	public String source;
	public String target;
	boolean playerSource;
	boolean playerTarget;
	float damage;
	
	
	public DamageElement(String source, String target, boolean playerSource, boolean playerTarget, float damage) {
		this.source = source;
		this.target = target;
		this.playerSource = playerSource;
		this.playerTarget = playerTarget;
		this.damage = damage;
	}
	
	
	public void addDamage(float damage) {
		this.damage += damage;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DamageElement))
			return false;
		DamageElement o = (DamageElement)obj;
		return o.source.equals(this.source) && o.target.equals(this.target) && o.playerSource == this.playerSource;
	}
	
	
	@Override
	public int hashCode() {
		return (this.source + '|' + this.target).hashCode();
	}
	

}
