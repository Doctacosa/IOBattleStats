package com.interordi.iobattlestats;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class DataAccess implements Runnable {

	 @SuppressWarnings("unused")
	private IOBattleStats plugin;
	 private String dbServer;
	 private String dbUsername;
	 private String dbPassword;
	 private String dbBase;
	 
	 private Map< BattleKey, Float > damages = new HashMap< BattleKey, Float>();
	 private Map< BattleKey, Integer > deaths = new HashMap< BattleKey, Integer>();
	 
	 
	 DataAccess(IOBattleStats plugin, String dbServer, String dbUsername, String dbPassword, String dbBase) {
		 this.plugin = plugin;
		 this.dbServer = dbServer;
		 this.dbUsername = dbUsername;
		 this.dbPassword = dbPassword;
		 this.dbBase = dbBase;
	 }
	
	
	public void recordDamage(String source, String target, String world, float damage, String cause, String weaponName, boolean playerSource, boolean playerTarget) {
		
		int isPlayerSource = (playerSource) ? 1 : 0;
		int isPlayerTarget = (playerTarget) ? 1 : 0;
		
		BattleKey bk = new BattleKey(source, target, world, cause, weaponName, isPlayerSource, isPlayerTarget);
		
		//System.out.println("Damage: " + bk.toString());
		
		Float val = damages.get(bk);
		if (val == null)
			val = damage;
		else
			val += damage;
		
		damages.put(bk, val);
	}


	public void recordDeath(String source, String target, String world, String cause, String weaponName, boolean playerSource, boolean playerTarget) {
		
		int isPlayerSource = (playerSource) ? 1 : 0;
		int isPlayerTarget = (playerTarget) ? 1 : 0;
		
		BattleKey bk = new BattleKey(source, target, world, cause, weaponName, isPlayerSource, isPlayerTarget);
		
		//System.out.println(" Death: " + bk.toString());
		
		Integer val = deaths.get(bk);
		if (val == null)
			val = 1;
		else
			val += 1;
		
		deaths.put(bk, val);
	}


	//Load an incoming player's stats
	public void loadStats(Player player) {
		// TODO Auto-generated method stub
		
	}


	//Save an outgoing player's stats
	public void saveStats(Player player) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void run() {
		//Save data on a regular basis in its own thread to lighten load on the main server
		
		//Nothing to do, exit
		if (damages.size() == 0 && deaths.size() == 0) {
			return;
		}
		
		
		//Copy the maps then clear them
		Map< BattleKey, Float > damagesCopy = new HashMap< BattleKey, Float>();
		Map< BattleKey, Integer > deathsCopy = new HashMap< BattleKey, Integer>();
		damagesCopy.putAll(damages);
		deathsCopy.putAll(deaths);
		damages.clear();
		deaths.clear();
		
		
		//Damages
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + dbServer + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword);
			
			Statement stmt = conn.createStatement();
			
			/*
			if (false) {
				ResultSet rs = stmt.executeQuery("SELECT * FROM Stats_io_damage");
				
				System.out.println("Query result: " + rs);
				while (rs.next()) {
					System.out.println(rs.getString("source") + " -> " + rs.getString("target") + ": " + rs.getFloat("damage"));
					
					//int x = rs.getInt("a");
					//String s = rs.getString("b");
					//float f = rs.getFloat("c");
				}
				rs.close();
			}
			*/
			
			for (Map.Entry< BattleKey, Float > entry : damagesCopy.entrySet()) {
				BattleKey bk = entry.getKey();
				Float val = entry.getValue();
				
				query = "INSERT INTO Stats_io_damage (source, target, world, cause, damage, weapon_name, player_source, player_target)" + 
					"VALUES ('" + bk.source + "', '" + bk.target + "', '" + bk.world + "', '" + bk.cause + "', '" + val + "', '" + bk.weaponName + "', '" + bk.isPlayerSource + "', '" + bk.isPlayerTarget + "') " +
					"ON DUPLICATE KEY UPDATE damage = damage + " + val + "";
				
				@SuppressWarnings("unused")
				int res = stmt.executeUpdate(query);
				//System.out.println("Nb updates: " + res);
				
			}
			
			
			//Deaths
			for (Map.Entry< BattleKey, Integer > entry : deathsCopy.entrySet()) {
				BattleKey bk = entry.getKey();
				Integer val = entry.getValue();
				
				query = "INSERT INTO Stats_io_deaths (source, target, world, cause, amount, weapon_name, player_source, player_target)" + 
					"VALUES ('" + bk.source + "', '" + bk.target + "', '" + bk.world + "', '" + bk.cause + "', '" + val + "', '" + bk.weaponName + "', '" + bk.isPlayerSource + "', '" + bk.isPlayerTarget + "') " +
					"ON DUPLICATE KEY UPDATE amount = amount + " + val + "";
				
				@SuppressWarnings("unused")
				int res = stmt.executeUpdate(query);
			}
			
			stmt.close();
			conn.close();
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("Query: " + query);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
}
