package com.interordi.iobattlestats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.UUID;

import org.bukkit.Bukkit;
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

	//The maximum length of item names to save
	private static final int maxNameLength = 100;
	
	
	DataAccess(IOBattleStats plugin, String dbHost, int dbPort, String dbUsername, String dbPassword, String dbBase) {
		this.plugin = plugin;
		
		database = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbBase + "?user=" + dbUsername + "&password=" + dbPassword + "&useSSL=false";
	}
	
	
	//Initialize the database
	public boolean init() {

		//Create or update the required database table
		//A failure indicates that the database wasn't configured properly
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "";
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "arrows` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "beds_entered` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "blocks_broken` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "blocks_placed` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "buckets_emptied` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "buckets_filled` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "change_world` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "chat_words` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "commands` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "consumed` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "crafted` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "damage` ( " +
				"  `source` varchar(36) NOT NULL, " +
				"  `target` varchar(36) NOT NULL, " +
				"  `world` varchar(50) NOT NULL, " +
				"  `cause` varchar(36) NOT NULL, " +
				"  `weapon_name` varchar(100) NOT NULL, " +
				"  `damage` float NOT NULL, " +
				"  `player_source` tinyint(1) NOT NULL, " +
				"  `player_target` tinyint(1) NOT NULL, " +
				"  PRIMARY KEY (`source`,`target`,`world`,`cause`,`weapon_name`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "deaths` ( " +
				"  `source` varchar(36) NOT NULL, " +
				"  `target` varchar(36) NOT NULL, " +
				"  `world` varchar(50) NOT NULL, " +
				"  `cause` varchar(36) NOT NULL, " +
				"  `weapon_name` varchar(100) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `player_source` tinyint(1) NOT NULL, " +
				"  `player_target` tinyint(1) NOT NULL, " +
				"  KEY `main` (`source`,`target`,`world`,`cause`,`weapon_name`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "eggs_thrown` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "fish_caught` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "item_broken` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "item_dropped` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "item_picked_up` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(36) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "joins` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "kicks` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "move` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "players` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `name` varchar(16) NOT NULL, " +
				"  `ip` varchar(16) NOT NULL, " +
				"  PRIMARY KEY (`uuid`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "pvp_streak` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `value` int(11) NOT NULL, " +
				"  PRIMARY KEY (`uuid`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "shears` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "teleports` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "trades` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "xp_gained` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "tamings` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "inventories` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "heals` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "hunger_refills` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "milk` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "splashes` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "enchants` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "lighters` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("" +
				"CREATE TABLE IF NOT EXISTS `" + this.tablePrefix + "enderpearls` ( " +
				"  `uuid` varchar(36) NOT NULL, " +
				"  `amount` int(11) NOT NULL, " +
				"  `value` varchar(20) NOT NULL, " +
				"  `name` varchar(100) NOT NULL, " +
				"  `world` varchar(30) NOT NULL, " +
				"  PRIMARY KEY (`uuid`,`value`,`name`,`world`) " +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1; "
			);
			pstmt.executeUpdate();
			
		} catch (SQLException ex) {
			Bukkit.getLogger().severe("Query: " + query);
			Bukkit.getLogger().severe("SQLException: " + ex.getMessage());
			Bukkit.getLogger().severe("SQLState: " + ex.getSQLState());
			Bukkit.getLogger().severe("VendorError: " + ex.getErrorCode());
			return false;
		}

		return true;
	}

	
	public void recordDamage(String source, String target, String world, float damage, String cause, String weaponName, boolean playerSource, boolean playerTarget) {
		
		int isPlayerSource = (playerSource) ? 1 : 0;
		int isPlayerTarget = (playerTarget) ? 1 : 0;
		
		BattleKey bk = new BattleKey(source, target, world, cause, weaponName, isPlayerSource, isPlayerTarget);
		
		//Bukkit.getLogger().info("Damage: " + bk.toString());
		
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
		
		//Bukkit.getLogger().info(" Death: " + bk.toString());
		
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
	public void recordPlayer(UUID uuid, String name, String ip) {
		StatUpdate playerData = new StatUpdate(StatUpdate.PLAYER, "players", uuid, name);
		playerData.setExtra(ip);
		basicStats.add(playerData);
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
			
			StatKey thisStat = new StatKey(entry.uuid, entry.world, entry.value, entry.name, entry.extra);
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
				
				Bukkit.getLogger().info("Query result: " + rs);
				while (rs.next()) {
					Bukkit.getLogger().info(rs.getString("source") + " -> " + rs.getString("target") + ": " + rs.getFloat("damage"));
					
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
				String weaponName = bk.weaponName;
				if (weaponName.length() > 100)
					weaponName = weaponName.substring(0, maxNameLength);
				
				pstmt.setString(1, bk.source);
				pstmt.setString(2, bk.target);
				pstmt.setString(3, bk.world);
				pstmt.setString(4, bk.cause);
				pstmt.setFloat(5, val);
				pstmt.setString(6, weaponName);
				pstmt.setInt(7, bk.isPlayerSource);
				pstmt.setInt(8, bk.isPlayerTarget);
				pstmt.setFloat(9, val);
				
				@SuppressWarnings("unused")
				int res = pstmt.executeUpdate();
			}
			
			
			//Deaths
			pstmt = conn.prepareStatement("" +
					"INSERT INTO " + this.tablePrefix + "deaths (source, target, world, cause, amount, weapon_name, player_source, player_target)" + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
					"ON DUPLICATE KEY UPDATE amount = amount + ?");
			
			for (Map.Entry< BattleKey, Integer > entry : deathsCopy.entrySet()) {
				BattleKey bk = entry.getKey();
				Integer val = entry.getValue();
				String weaponName = bk.weaponName;
				if (weaponName.length() > 100)
					weaponName = weaponName.substring(0, maxNameLength);
				
				pstmt.setString(1, bk.source);
				pstmt.setString(2, bk.target);
				pstmt.setString(3, bk.world);
				pstmt.setString(4, bk.cause);
				pstmt.setFloat(5, val);
				pstmt.setString(6, weaponName);
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
					query = "INSERT INTO " + this.tablePrefix + table + " (uuid, name, ip)" + 
							"VALUES (?, ?, ?) " +
							"ON DUPLICATE KEY UPDATE name = ?, ip = ?";
				
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
						String itemName = entry.getKey().name;
						if (itemName.length() > 100)
							itemName = itemName.substring(0, maxNameLength);
		
						pstmt.setString(2, entry.getKey().world);
						pstmt.setInt(3, entry.getValue());
						pstmt.setString(4, entry.getKey().value);
						pstmt.setString(5, itemName);
						pstmt.setInt(6, entry.getValue());
					} else if (format == StatUpdate.PLAYER) {
						pstmt.setString(2, entry.getKey().value);
						pstmt.setString(3, entry.getKey().extra);
						pstmt.setString(4, entry.getKey().value);
						pstmt.setString(5, entry.getKey().extra);
					}
					
					@SuppressWarnings("unused")
					int res = pstmt.executeUpdate();
				}
			}
			
			
			if (plugin.tracker != null) {
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
			}
			
			
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException ex) {
			// handle any errors
			Bukkit.getLogger().severe("Query error for " + plugin.getName() + ": " + query);
			Bukkit.getLogger().severe("Error " + ex.getErrorCode() + ": " + ex.getMessage());
		}
	}


	//Get a player's current PvP streak
	public int getPvPStreak(UUID player) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";
		int streak = 0;
		
		try {
			conn = DriverManager.getConnection(database);
			
			pstmt = conn.prepareStatement("" +
				"SELECT value " + 
				"FROM " + this.tablePrefix + "pvp_streak " +
				"WHERE uuid = ? "
			);
			
			pstmt.setString(1, player.toString());
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				streak = rs.getInt("value");
			}
			rs.close();
		} catch (SQLException ex) {
			// handle any errors
			Bukkit.getLogger().severe("Query error for " + plugin.getName() + ": " + query);
			Bukkit.getLogger().severe("Error " + ex.getErrorCode() + ": " + ex.getMessage());
		}

		return streak;
	}
}
