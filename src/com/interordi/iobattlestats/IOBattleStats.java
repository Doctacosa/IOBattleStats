package com.interordi.iobattlestats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class IOBattleStats extends JavaPlugin {

	DataAccess data;
	PlayersTracking tracker;
	
	
	public void onEnable() {
		new DamageListener(this);
		new DeathListener(this);
		data = new DataAccess(this);
		tracker = new PlayersTracking(this);
		/*
		MyCommandExecutor executor = new MyCommandExecutor(this);
		getCommand("basic").setExecutor(executor);
		getCommand("basic2").setExecutor(executor);
		*/
		
		getLogger().info("IOBattleStats enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IOBattleStats disabled");
	}
	
	
	public void addPlayer(Player player) {
		this.tracker.addPlayer(player);
		this.data.loadStats(player);
	}
	
	
	public void removePlayer(Player player) {
		this.data.saveStats(player);
		this.tracker.removePlayer(player);
	}
	
	
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
			
			Heads.giveToPlayer(targetName, user);
			
			return true;
		}
		
		return false;
	}
}
