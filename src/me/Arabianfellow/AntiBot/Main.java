package me.Arabianfellow.AntiBot;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main 
extends JavaPlugin 
implements Listener {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		if(!new File(getDataFolder(), "config.yml").exists()){
			saveConfig();
		}
	}
	ArrayList<String> moved = new ArrayList<String>();
	List<String> blacklist = getConfig().getStringList("blacklisted");
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!player.hasPermission("antichatbot.bypass")) {
		moved.add(player.getName());
	} else {
		player.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "AntiChatBot" + ChatColor.BLACK + "] " + ChatColor.RED + "You are in Bypass Mode! if you feel this is an error please contact an admin");
	}
}
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
	    	if(moved.contains(player.getName())){
	        player.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "AntiChatBot" + ChatColor.BLACK + "] " + ChatColor.RED + "You must move before you can talk in chat!");
	        event.setCancelled(true);
	    	} else {
	    		return;
	    	}
	}
	@EventHandler
	public void onBlacklistChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
	    	if(blacklist.contains(player.getName())){
	        player.sendMessage(ChatColor.BLACK + "[" + ChatColor.RED + "AntiChatBot" + ChatColor.BLACK + "] " + ChatColor.RED + "You have been blacklisted from talking in chat for being a bot/other reasons. If you feel this is an error contact an Administrator!");
	        event.setCancelled(true);
	    	} else {
	    		event.setCancelled(false);
	    	}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		moved.remove(player.getName());
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		if(cmd.getName().equalsIgnoreCase("chatbl") || (cmd.getName().equalsIgnoreCase("chatblacklist"))){
		if(args.length == 0){
			player.sendMessage(ChatColor.RED + "Invalid Arguments: Please use /chatbl <player name>");
		 } else if(args.length == 1){
			 if(player.hasPermission("AntiChatBot.blacklistadd")) {
					   if(target != null){
					if (!getConfig().getStringList("blacklisted").contains(target.getName())) {
			   blacklist.add(target.getName());
			   saveConfig();
			   getConfig().set("blacklisted", blacklist);
			   player.sendMessage(ChatColor.GREEN + "Added " + target.getName() + " to the blacklist");
			   target.sendMessage(ChatColor.DARK_RED + "You have been blacklisted from chatting by " + player.getName());
		   } else {
			   player.sendMessage(ChatColor.RED + target.getName() + " is already blacklisted");
		   } 
		   } else {
			   player.sendMessage(ChatColor.RED + "Error: Player " + ChatColor.GOLD + args[0] + ChatColor.RED +  " isnt online! Are you sure you types the players name right?");
		   }
				 } else {
					 player.sendMessage(ChatColor.RED + "No Permissions!");
				 }
		 }
		   } else if(cmd.getName().equalsIgnoreCase("chatblremove")){
			   if(args.length == 0) {
			   player.sendMessage(ChatColor.RED + "Invalid Arguments: Please use /chatblremove <player name>");
			   } else if(args.length == 1){
				   if(player.hasPermission("AntiChatBot.blacklistremove")) {
				   if(target != null){
					   if (getConfig().getStringList("blacklisted").contains(target.getName())) {
					   blacklist.remove(target.getName());
					   saveConfig();
					   getConfig().set("blacklisted", blacklist);
					   player.sendMessage(ChatColor.GREEN + "Removed " + target.getName() + " from the blacklist");
					   target.sendMessage(ChatColor.GREEN + "You have been removed from the chat blacklist by " + player.getName());
				   } else {
					   player.sendMessage(ChatColor.RED + target.getName() + " is not blacklisted");
				   }
				   } else {
					   player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[0] + ChatColor.RED +  " isnt online! Are you sure you types the players name right?");
				   }
				   } else {
					   player.sendMessage(ChatColor.RED + "No Permissions!");
				   }
		   }
		}
		return false;
	}
}
