package com.interordi.iobattlestats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.interordi.iobattlestats.listeners.BasicListener;
import com.interordi.iobattlestats.listeners.BlockListener;
import com.interordi.iobattlestats.listeners.ChatListener;
import com.interordi.iobattlestats.listeners.DamageListener;
import com.interordi.iobattlestats.listeners.DeathListener;
import com.interordi.iobattlestats.listeners.LoginListener;
import com.interordi.iobattlestats.listeners.PlayersMove;
import com.interordi.iobattlestats.utilities.Heads;


public class IOBattleStats extends JavaPlugin {

	public DataAccess data;
	public PlayersTracking tracker;
	
	
	public void onEnable() {
		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();
		
		boolean enable = true;

		//Configuration file use (config.yml): http://wiki.bukkit.org/Configuration_API_Reference
		String dbHost = this.getConfig().getString("database.host", null);
		int dbPort = this.getConfig().getInt("database.port", 3306);
		String dbUsername = this.getConfig().getString("database.username", null);
		String dbPassword = this.getConfig().getString("database.password", null);
		String dbBase = this.getConfig().getString("database.base", null);

		//Old config format
		if (dbHost == null)
			dbHost = this.getConfig().getString("mysql.server");
		if (dbUsername == null)
			dbUsername = this.getConfig().getString("mysql.username");
		if (dbPassword == null)
			dbPassword = this.getConfig().getString("mysql.password");
		if (dbBase == null)
			dbBase = this.getConfig().getString("mysql.base");

		if (this.getConfig().contains("enable"))
			enable = this.getConfig().getBoolean("enable");

		data = new DataAccess(this, dbHost, dbPort, dbUsername, dbPassword, dbBase);
		if (!data.init()) {
			Bukkit.getLogger().severe("---------------------------------");
			Bukkit.getLogger().severe("Failed to initialize the database");
			Bukkit.getLogger().severe("Make sure to configure config.yml");
			Bukkit.getLogger().severe("---------------------------------");
			enable = false;
			return;
		}

		new LoginListener(this, enable);

		if (enable) {
			new BasicListener(this);
			new BlockListener(this);
			new ChatListener(this);
			new DamageListener(this);
			new DeathListener(this);

			PlayersMove playersMove = new PlayersMove(this);
			
			//Check for player movements every so often
			tracker = new PlayersTracking(this);
			getServer().getScheduler().scheduleSyncRepeatingTask(this, playersMove, 10*20L, 10*20L);
		}
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, data, 60*20L, 60*20L);	//Run every minute
		
		getLogger().info("IOBattleStats enabled");
	}
	
	
	public void onDisable() {
		data.run();	//Save the current data before stopping
		getLogger().info("IOBattleStats disabled");
	}
	
	
	public void addPlayer(Player player) {
		if (this.tracker == null)
			return;
		this.tracker.add(player);
		this.data.loadStats(player);
	}
	
	
	public void removePlayer(Player player) {
		if (this.tracker == null)
			return;
		this.data.saveStats(player);
		this.tracker.remove(player);
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("givehead")) {
			
			//Only players can run this command
			boolean isConsole = !(sender instanceof Player);

			Player user = null;
			if (!isConsole)
				user = (Player)sender;
			
			//Check if the user has permission to use this command
			if (!isConsole && !user.hasPermission("iobattlestats.givehead")) {
				user.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
				return true;
			}
			
			//Select the target of the command
			Player headOf = null;
			Player giveTo = user;
			String targetName = "";
			int amount = 1;
			if (args.length >= 3) {
				giveTo = Bukkit.getServer().getPlayer(args[0]);
				targetName = args[1];

				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + args[2] + " is not a valid number.");
					return true;
				}

			} else if (args.length >= 2) {
				giveTo = Bukkit.getServer().getPlayer(args[0]);
				targetName = args[1];

			} else if (args.length >= 1) {
				targetName = args[0];
			} else {
				sender.sendMessage(ChatColor.RED + "You need to specify a player name.");
				return true;
			}

			if (giveTo == null) {
				sender.sendMessage(ChatColor.RED + "The target player is offline.");
				return true;
			}

			headOf = Bukkit.getServer().getPlayer(targetName);
			if (headOf != null)
				targetName = headOf.getDisplayName();
			
			Heads.giveToPlayer(Bukkit.getOfflinePlayer(targetName), giveTo, amount);
			
			return true;
		}
		
		return false;
	}
}
