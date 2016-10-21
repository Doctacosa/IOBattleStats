package com.interordi.iobattlestats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class DataAccess {

	 IOBattleStats plugin;
	 
	 
	 DataAccess(IOBattleStats plugin) {
		 this.plugin = plugin;
	 }
	
	
	@SuppressWarnings("unused")
	public void recordDamage(String source, String target, float damage, String cause, boolean playerSource, boolean playerTarget) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/creeperslab?user=root&password=");
			
			Statement stmt = conn.createStatement();
			
			if (false) {
				ResultSet rs = stmt.executeQuery("SELECT * FROM Stats_io_damage");
				
				System.out.println("Query result: " + rs);
				while (rs.next()) {
					System.out.println(rs.getString("source") + " -> " + rs.getString("target") + ": " + rs.getFloat("damage"));
					/*
					int x = rs.getInt("a");
					String s = rs.getString("b");
					float f = rs.getFloat("c");
					*/
				}
				rs.close();
			}
			
			
			int isPlayerSource = (playerSource) ? 1 : 0;
			int isPlayerTarget = (playerTarget) ? 1 : 0;
			
			int val = stmt.executeUpdate(
				"INSERT INTO Stats_io_damage (source, target, damage, cause, player_source, player_target)" + 
				"VALUES ('" + source + "', '" + target + "', '" + damage + "', '" + cause + "', '" + isPlayerSource + "', '" + isPlayerTarget + "')"
			);
			System.out.println("Nb updates: " + val);
			
			stmt.close();
			conn.close();
			
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		this.plugin.getServer().broadcastMessage(target + ", " + damage + " damage by " + source + " through " + cause);
	}


	//Load an incoming player's stats
	public void loadStats(Player player) {
		// TODO Auto-generated method stub
		
	}


	//Save an outgoing player's stats
	public void saveStats(Player player) {
		// TODO Auto-generated method stub
		
	}

}
