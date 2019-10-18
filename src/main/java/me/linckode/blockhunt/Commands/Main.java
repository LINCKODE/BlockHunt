package me.linckode.blockhunt.Commands;

import me.linckode.blockhunt.BlockHunt;
import me.linckode.blockhunt.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Main implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;

            if (player.hasPermission(BlockHunt.permission)){
            if (args.length == 0){
                player.sendMessage("");
                player.sendMessage(Config.messageParser("&6" + BlockHunt.plugin.getDescription().getName() + " &bv.&3" + BlockHunt.plugin.getDescription().getVersion()));
                player.sendMessage(Config.messageParser("&a by &5LINCKODE"));
                player.sendMessage("");
                return true;
            }

            if (args.length == 1){
                if (args[0].equalsIgnoreCase("reload")){
                    BlockHunt.loadConfigData();
                    player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Successfully reloaded.");
                    return true;
                }

            }
            if (args[0].equalsIgnoreCase("setPrefix")){
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

            if (args[0].equalsIgnoreCase("setBlockCount")){

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

            if (args[0].equalsIgnoreCase("set")){

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

            if (args[0].equalsIgnoreCase("setall")){

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

            return true;
            }
        }

        else
            sender.sendMessage(ChatColor.RED + "Player only command.");

        return true;
    }
}
