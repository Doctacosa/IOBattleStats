package com.interordi.iobattlestats;

import org.bukkit.Bukkit;
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
		String dbHost = this.getConfig().getString("database.host");
		int dbPort = this.getConfig().getInt("database.port");
		String dbUsername = this.getConfig().getString("database.username");
		String dbPassword = this.getConfig().getString("database.password");
		String dbBase = this.getConfig().getString("database.base");

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
			System.err.println("---------------------------------");
			System.err.println("Failed to initialize the database");
			System.err.println("Make sure to configure config.yml");
			System.err.println("---------------------------------");
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
			
			//Select the target of the command
			Player target = null;
			String targetName = "";
			if (args.length >= 1) {
				target = Bukkit.getServer().getPlayer(args[0]);
				if (target != null)
					targetName = target.getDisplayName();
				else
					targetName = args[0];
			} else {
				if (user != null)	user.sendMessage("You need to specify a player name.");
			}
			
			//Check if the user has permission to use this command
			if (!isConsole && !user.hasPermission("iobattlestats.givehead")) {
				user.sendMessage("You would like that, wouldn't you.");
				return true;
			}
			
			user.sendMessage("Here's some head.");
			
			Heads.giveToPlayer(Bukkit.getOfflinePlayer(targetName), user);
			
			return true;
		}
		
		return false;
	}
}
