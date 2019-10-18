package me.linckode.blockhunt.Commands;

import me.linckode.blockhunt.BlockHunt;
import me.linckode.blockhunt.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Main implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            if (player.hasPermission(BlockHunt.permission)){
            if (args.length == 0){
                player.sendMessage("");
                player.sendMessage(Config.messageParser("&6" + BlockHunt.plugin.getDescription().getName() + " &bv.&3" + BlockHunt.plugin.getDescription().getVersion()));
                player.sendMessage(Config.messageParser("&a by &5") + BlockHunt.plugin.getDescription().getAuthors());
                player.sendMessage("");
                return true;
            }

            if (args.length == 1){
                if (args[0].equalsIgnoreCase("reload") && BlockHunt.isEnabled){
                    BlockHunt.loadConfigData();
                    player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Successfully reloaded.");
                    return true;
                }

            }


            if (args[0].equalsIgnoreCase("setPrefix") && BlockHunt.isEnabled){
                StringBuilder prefix = new StringBuilder();
                for (int index = 1; index < args.length; index++)
                {
                    prefix.append(args[index]).append(" ");
                }
                if (BlockHunt.configFile.exists()){
                    Config.set(BlockHunt.configFile, "prefix", prefix.toString());
                    player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Prefix set to: " + Config.messageParser(prefix.toString()));
                    BlockHunt.loadConfigData();
                }
                else {
                    player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Config file not found or corrupted.");
                    player.sendMessage(BlockHunt.prefix + ChatColor.YELLOW + "Reload using '/blockhunt reload' and try again.");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("setBlockCount") && BlockHunt.isEnabled){

                if (args.length == 2) {
                    if (BlockHunt.configFile.exists()) {
                        Config.set(BlockHunt.configFile, "blockCount", Integer.parseInt(args[1]));
                        player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Block count set to: " + ChatColor.GOLD + args[1]);
                        BlockHunt.loadConfigData();
                    } else {
                        player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Config file not found or corrupted.");
                        player.sendMessage(BlockHunt.prefix + ChatColor.YELLOW + "Reload using '/blockhunt reload' and try again.");
                    }

                }
                else
                    player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Insufficient arguments.");
            }

            if (args[0].equalsIgnoreCase("set") && BlockHunt.isEnabled){

                if (args.length == 2) {
                    if (BlockHunt.blockCount != 0) {
                        if (Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= BlockHunt.blockCount) {

                            BlockHunt.selectedBlock = Integer.parseInt(args[1]);
                            BlockHunt.canIndividuallySet = true;

                            player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Left click on a block to set it as block nr. " + ChatColor.GOLD + BlockHunt.selectedBlock + ChatColor.GREEN + ".");

                            return true;

                        } else {
                            player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Number must be between 1 and " + BlockHunt.blockCount + ".");
                        }
                    }
                    else {
                        player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Block count not set.");
                        player.sendMessage(BlockHunt.prefix + ChatColor.YELLOW + "Set the maximum block count with '/blockhunt setblockcount <number>' .");
                    }
                }
                else
                    player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Insufficient arguments.");
            }

            if (args[0].equalsIgnoreCase("setall") && BlockHunt.isEnabled){

                if (args.length == 1) {
                    if (BlockHunt.blockCount != 0) {

                        BlockHunt.selectedBlock = 1;
                        BlockHunt.canMultiSelect = true;
                        player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Left click on a block to set it as block nr. " + ChatColor.GOLD + BlockHunt.selectedBlock + ChatColor.GREEN + ".");
                        return true;
                    }
                    else {
                        player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Block count not set.");
                        player.sendMessage(BlockHunt.prefix + ChatColor.YELLOW + "Set the maximum block count with '/blockhunt setblockcount <number>' .");
                    }
                }
                else
                    player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Too many arguments.");
            }

            if (args[0].equalsIgnoreCase("enable")){

                if (args.length == 2){

                    switch (args[1]){

                        default:
                            player.sendMessage(ChatColor.YELLOW + "Only " + ChatColor.GREEN + "true" + ChatColor.YELLOW + "/" + ChatColor.RED + "false" + ChatColor.YELLOW + " accepted.");
                            break;
                        case "true":
                            if (Config.getBool(BlockHunt.configFile,"enabled")){
                                player.sendMessage("");
                                player.sendMessage(ChatColor.GREEN + "Already enabled.");
                                player.sendMessage("");
                            }
                            else {

                                Config.set(BlockHunt.configFile, "enabled", true);
                                player.sendMessage("");
                                player.sendMessage(ChatColor.GOLD + "Block hunt " + ChatColor.GREEN + "enabled");
                                player.sendMessage("");
                                BlockHunt.loadConfigData();

                            }
                            break;

                        case "false" :
                            if (!Config.getBool(BlockHunt.configFile, "enabled")){
                                player.sendMessage("");
                                player.sendMessage(ChatColor.RED + "Already disabled.");
                                player.sendMessage("");
                            }
                            else {
                                Config.set(BlockHunt.configFile, "enabled", false);
                                player.sendMessage("");
                                player.sendMessage(ChatColor.GOLD + "Block hunt " + ChatColor.RED + "disabled");
                                player.sendMessage("");
                                Config.set(BlockHunt.configFile,"enabled",false);
                                BlockHunt.loadConfigData();
                            }
                            break;
                    }

                }

            }

            if (args[0].equalsIgnoreCase("resetPlayer")){
                if (args.length == 2){
                    if (Bukkit.getPlayer(args[1])!= null){

                        Player target = Bukkit.getPlayer(args[1]);
                        File targetFile = new File(BlockHunt.plugin.getDataFolder() + File.separator + "Players" + File.separator + target.getName() + ".yml");
                        targetFile.delete();

                        player.sendMessage(BlockHunt.prefix + ChatColor.YELLOW + args[1] + "'s data has been reset.");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Player not existing or not online.");
                    }
                }
                else
                    player.sendMessage(BlockHunt.prefix + ChatColor.RED + "Insufficient arguments.");
            }

            if (args[0].equalsIgnoreCase("help")){
                if (args.length == 1){
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GOLD + "Block hunt" + ChatColor.AQUA + " - " + ChatColor.WHITE + BlockHunt.plugin.getDescription().getDescription());
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "help" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Shows this message.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "reload" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Reloads the plugin.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "setprefix" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Allows you to set the prefix of the plugin.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "setblockcount" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Allows you to set how many blocks can be set/found.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "setall" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Allows you to set all the blocks by left clicking them.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "set" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Allows you to set each block individually.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "enable " + ChatColor.GREEN + "true" + ChatColor.YELLOW + "/" + ChatColor.RED + "false" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Allows you to enable or disable the plugin.");
                    player.sendMessage(ChatColor.YELLOW + "/blockhunt " + ChatColor.GOLD + "resetplayer" + ChatColor.AQUA + " - " + ChatColor.WHITE + "Resets the amount of blocks the player has found to 0.");
                    player.sendMessage("");

                }
            }

            return true;
            }
            else {
                player.sendMessage(BlockHunt.permissionMessage);
            }
        }

        else
            sender.sendMessage(ChatColor.RED + "Player only command.");

        return true;
    }
}
