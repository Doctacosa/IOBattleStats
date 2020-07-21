package com.interordi.iobattlestats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.interordi.iobattlestats.structures.BattleKey;
import com.interordi.iobattlestats.structures.StatKey;
import com.interordi.iobattlestats.structures.StatUpdate;

public class DataAccess implements Runnable {

	 @SuppressWarnings("unused")
	private IOBattleStats plugin;
	private String database = "";
	private String tablePrefix = "stats_io_";
	
	private Map< BattleKey, Float > damages = new HashMap< BattleKey, Float>();
	private Map< BattleKey, Integer > deaths = new HashMap< BattleKey, Integer>();
	
	private Vector< StatUpdate > basicStats = new Vector< StatUpdate >();
	
	
	DataAccess(IOBattleStats plugin, String dbServer, String dbUsername, String dbPassword, String dbBase) {
		this.plugin = plugin;
		
		database = "jdbc:mysql://" + dbServer + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword + "&useSSL=false";
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
	
	
	//Generic method to record a stat in the given table
	public void recordBasicStat(String table, UUID uuid, int amount, String world) {
		basicStats.add(new StatUpdate(StatUpdate.BASIC, table, uuid, amount, world));
	}


	//Generic method to record a named stat in the given table
	public void recordItemStat(String table, UUID uuid, String value, int amount, String world) {
		basicStats.add(new StatUpdate(StatUpdate.VALUE, table, uuid, amount, world, value));
	}


	//Generic method to record a named stat in the given table
	public void recordItemNamedStat(String table, UUID uuid, String value, String name, int amount, String world) {
		basicStats.add(new StatUpdate(StatUpdate.NAMED, table, uuid, amount, world, value, name));
	}
	
	
	//Record a player's name and UUID
	public void recordPlayer(UUID uuid, String name) {
		basicStats.add(new StatUpdate(StatUpdate.PLAYER, "players", uuid, name));
	}



	//Load an incoming player's stats
	public void loadStats(Player player) {
		// TODO Auto-generated method stub
		
	}


	//Save an outgoing player's stats
	public void saveStats(Player player) {
		// TODO Auto-generated method stub
		
	}


	//Save data on a regular basis in its own thread to lighten load on the main server
	@Override
	public void run() {
		
		//Copy the maps then clear them
		Map< BattleKey, Float > damagesCopy = new HashMap< BattleKey, Float>();
		Map< BattleKey, Integer > deathsCopy = new HashMap< BattleKey, Integer>();
		Vector< StatUpdate > basicStatsCopy = new Vector< StatUpdate >();
		damagesCopy.putAll(damages);
		deathsCopy.putAll(deaths);
		basicStatsCopy.addAll(basicStats);
		damages.clear();
		deaths.clear();
		basicStats.clear();
		
		Map< String, Integer > formats = new HashMap< String, Integer >();
		
		
		//Structure the basic stats to minimize the amount of SQL queries
		Map< String, Map< StatKey, Integer > > basicStructured = new HashMap< String, Map< StatKey, Integer > >();
		for (StatUpdate entry : basicStatsCopy) {
			Map< StatKey, Integer > tableStats;
			tableStats = basicStructured.get(entry.table);
			if (tableStats == null)
				tableStats = new HashMap< StatKey, Integer >();
			
			StatKey thisStat = new StatKey(entry.uuid, entry.world, entry.value, entry.name);
			int newAmount = entry.amount;
			if (tableStats.containsKey(thisStat))
				newAmount += tableStats.get(thisStat);
			tableStats.put(thisStat, newAmount);
			
			basicStructured.put(entry.table, tableStats);
			formats.put(entry.table, entry.format);
		}
		
		
		//Damages
		Connection conn = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);
			
			PreparedStatement pstmt = conn.prepareStatement("" +
					"INSERT INTO " + this.tablePrefix + "damage (source, target, world, cause, damage, weapon_name, player_source, player_target)" + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
					"ON DUPLICATE KEY UPDATE damage = damage + ?");
			
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
				
				pstmt.setString(1, bk.source);
				pstmt.setString(2, bk.target);
				pstmt.setString(3, bk.world);
				pstmt.setString(4, bk.cause);
				pstmt.setFloat(5, val);
				pstmt.setString(6, bk.weaponName);
				pstmt.setInt(7, bk.isPlayerSource);
				pstmt.setInt(8, bk.isPlayerTarget);
				pstmt.setFloat(9, val);
				
				@SuppressWarnings("unused")
				int res = pstmt.executeUpdate();
				//System.out.println("Nb updates: " + res);
			}
			
			
			//Deaths
			pstmt = conn.prepareStatement("" +
					"INSERT INTO " + this.tablePrefix + "deaths (source, target, world, cause, amount, weapon_name, player_source, player_target)" + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
					"ON DUPLICATE KEY UPDATE amount = amount + ?");
			
			for (Map.Entry< BattleKey, Integer > entry : deathsCopy.entrySet()) {
				BattleKey bk = entry.getKey();
				Integer val = entry.getValue();
				
				pstmt.setString(1, bk.source);
				pstmt.setString(2, bk.target);
				pstmt.setString(3, bk.world);
				pstmt.setString(4, bk.cause);
				pstmt.setFloat(5, val);
				pstmt.setString(6, bk.weaponName);
				pstmt.setInt(7, bk.isPlayerSource);
				pstmt.setInt(8, bk.isPlayerTarget);
				pstmt.setFloat(9, val);
				
				@SuppressWarnings("unused")
				int res = pstmt.executeUpdate();
			}
			
			
			//Basic stats, saved table by table
			for (Map.Entry< String, Map< StatKey, Integer > > tableEntry: basicStructured.entrySet()) {
				String table = tableEntry.getKey();
				int format = formats.get(table);
				
				if (format == StatUpdate.BASIC)
					query = "INSERT INTO " + this.tablePrefix + table + " (uuid, world, amount)" + 
							"VALUES (?, ?, ?) " +
							"ON DUPLICATE KEY UPDATE amount = amount + ?";
				else if (format == StatUpdate.VALUE)
					query = "INSERT INTO " + this.tablePrefix + table + " (uuid, world, amount, value)" + 
							"VALUES (?, ?, ?, ?) " +
							"ON DUPLICATE KEY UPDATE amount = amount + ?";
				else if (format == StatUpdate.NAMED)
					query = "INSERT INTO " + this.tablePrefix + table + " (uuid, world, amount, value, name)" + 
							"VALUES (?, ?, ?, ?, ?) " +
							"ON DUPLICATE KEY UPDATE amount = amount + ?";
				else if (format == StatUpdate.PLAYER)
					query = "INSERT INTO " + this.tablePrefix + table + " (uuid, name)" + 
							"VALUES (?, ?) " +
							"ON DUPLICATE KEY UPDATE name = ?";
				
				pstmt = conn.prepareStatement(query);
				
				for (Map.Entry< StatKey, Integer > entry : tableEntry.getValue().entrySet()) {
					pstmt.setString(1, entry.getKey().uuid.toString());
					if (format == StatUpdate.BASIC) {
						pstmt.setString(2, entry.getKey().world);
						pstmt.setInt(3, entry.getValue());
						pstmt.setInt(4, entry.getValue());
					} else if (format == StatUpdate.VALUE) {
						pstmt.setString(2, entry.getKey().world);
						pstmt.setInt(3, entry.getValue());
						pstmt.setString(4, entry.getKey().value);
						pstmt.setInt(5, entry.getValue());
					} else if (format == StatUpdate.NAMED) {
						pstmt.setString(2, entry.getKey().world);
						pstmt.setInt(3, entry.getValue());
						pstmt.setString(4, entry.getKey().value);
						pstmt.setString(5, entry.getKey().name);
						pstmt.setInt(6, entry.getValue());
					} else if (format == StatUpdate.PLAYER) {
						pstmt.setString(2, entry.getKey().value);
						pstmt.setString(3, entry.getKey().value);
					}
					
					@SuppressWarnings("unused")
					int res = pstmt.executeUpdate();
				}
			}
			
			
			//Kill streaks
			query = "INSERT INTO " + this.tablePrefix + "pvp_streak (uuid, value)" + 
					"VALUES (?, ?) " +
					"ON DUPLICATE KEY UPDATE value = ?";
			pstmt = conn.prepareStatement(query);
			
			for (Map.Entry< UUID, Integer > killStreak : plugin.tracker.getKillStreaks().entrySet()) {
				pstmt.setString(1, killStreak.getKey().toString());
				pstmt.setInt(2, killStreak.getValue());
				pstmt.setInt(3, killStreak.getValue());

				@SuppressWarnings("unused")
				int res = pstmt.executeUpdate();
			}
			
			
			
			pstmt.close();
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
