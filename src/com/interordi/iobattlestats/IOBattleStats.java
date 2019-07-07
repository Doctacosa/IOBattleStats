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
import com.interordi.iobattlestats.utilities.Heads;
import com.interordi.iobattlestats.utilities.PlayersMove;


public class IOBattleStats extends JavaPlugin {

	public DataAccess data;
	public PlayersTracking tracker;
	
	
	public void onEnable() {
		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();
		
		//Configuration file use (config.yml): http://wiki.bukkit.org/Configuration_API_Reference
		String dbServer = this.getConfig().getString("mysql.server");
		String dbUsername = this.getConfig().getString("mysql.username");
		String dbPassword = this.getConfig().getString("mysql.password");
		String dbBase = this.getConfig().getString("mysql.base");
		
		new DamageListener(this);
		new DeathListener(this);
		new ChatListener(this);
		new BasicListener(this);
		new BlockListener(this);
		PlayersMove playersMove = new PlayersMove(this);
		
		data = new DataAccess(this, dbServer, dbUsername, dbPassword, dbBase);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, data, 60*20L, 60*20L);	//Run every minute
		
		tracker = new PlayersTracking(this);
		/*
		MyCommandExecutor executor = new MyCommandExecutor(this);
		getCommand("basic").setExecutor(executor);
		getCommand("basic2").setExecutor(executor);
		*/
		
		//Check for player movements every so often
		getServer().getScheduler().scheduleSyncRepeatingTask(this, playersMove, 10*20L, 10*20L);
		
		getLogger().info("IOBattleStats enabled");
	}
	
	
	public void onDisable() {
		data.run();	//Save the current data before stopping
		getLogger().info("IOBattleStats disabled");
	}
	
	
	public void addPlayer(Player player) {
		this.tracker.add(player);
		this.data.loadStats(player);
	}
	
	
	public void removePlayer(Player player) {
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
