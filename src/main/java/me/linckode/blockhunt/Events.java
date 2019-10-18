package me.linckode.blockhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

 class Events implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){

        if (BlockHunt.isEnabled) {

            Player player = event.getPlayer();

            File playerFile = new File(BlockHunt.plugin.getDataFolder() + File.separator + "Players" + File.separator + player.getName() + ".yml");

            if (playerFile.exists()) {
                if (BlockHunt.useOnJoinMessages) {
                    String count = String.valueOf(Config.getInt(playerFile, "numberOfCollected"));
                    if (!count.equalsIgnoreCase(String.valueOf(BlockHunt.blockCount)))
                        player.sendMessage(BlockHunt.prefix + BlockHunt.onJoinMessage.replace("^", player.getName()).replace("@", count).replace("*", String.valueOf(BlockHunt.blockCount)));
                }
            } else {

                for (int index = 1; index <= BlockHunt.blockCount; index++) {
                    Config.set(playerFile, String.valueOf(index), false);
                    System.out.println(index);
                }

                Config.set(playerFile, "numberOfCollected", 0);


                if (BlockHunt.useOnJoinMessages) {
                    player.sendMessage(BlockHunt.prefix + BlockHunt.onFirstJoinMessage.replace("^", player.getName()).replace("*", String.valueOf(BlockHunt.blockCount)));

                }
            }

        }
    }

    @EventHandler
    void onInteraction(PlayerInteractEvent event) {

        if (BlockHunt.isEnabled) {

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (BlockHunt.canIndividuallySet) {

                    Player player = event.getPlayer();

                    Block block = event.getClickedBlock();

                    File blockFile = new File(BlockHunt.plugin.getDataFolder() + File.separator + "Blocks" + File.separator + BlockHunt.selectedBlock + ".yml");
                    Config.set(blockFile, "x", block.getX());
                    Config.set(blockFile, "y", block.getY());
                    Config.set(blockFile, "z", block.getZ());
                    Config.set(blockFile, "world", block.getWorld().getName());

                    player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Block nr. " + ChatColor.GOLD + BlockHunt.selectedBlock + ChatColor.GREEN + " has been set.");

                    BlockHunt.selectedBlock = 0;

                    BlockHunt.canIndividuallySet = false;


                    event.setCancelled(true);
                    BlockHunt.canIndexBlocks = true;
                    BlockHunt.loadConfigData();
                }

                if (BlockHunt.canMultiSelect) {

                    Player player = event.getPlayer();

                    Block block = event.getClickedBlock();

                    if (BlockHunt.selectedBlock <= 25) {

                        File blockFile = new File(BlockHunt.plugin.getDataFolder() + File.separator + "Blocks" + File.separator + BlockHunt.selectedBlock + ".yml");
                        Config.set(blockFile, "x", block.getX());
                        Config.set(blockFile, "y", block.getY());
                        Config.set(blockFile, "z", block.getZ());
                        Config.set(blockFile, "world", block.getWorld().getName());

                        if (BlockHunt.selectedBlock == BlockHunt.blockCount) {
                            player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Block nr. " + ChatColor.GOLD + BlockHunt.selectedBlock + ChatColor.YELLOW + "/" + ChatColor.GOLD + BlockHunt.blockCount + ChatColor.GREEN + " has been set.");
                            player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "All blocks have been set.");
                            BlockHunt.selectedBlock = 0;
                            BlockHunt.canMultiSelect = false;
                            BlockHunt.canIndexBlocks = true;
                        } else
                            player.sendMessage(BlockHunt.prefix + ChatColor.GREEN + "Block nr. " + ChatColor.LIGHT_PURPLE + BlockHunt.selectedBlock + ChatColor.YELLOW + "/" + ChatColor.GOLD + BlockHunt.blockCount + ChatColor.GREEN + " has been set.");

                        BlockHunt.selectedBlock += 1;
                    }
                    event.setCancelled(true);
                    BlockHunt.loadConfigData();
                }

            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {

                Player player = event.getPlayer();

                if (BlockHunt.blocks.contains(event.getClickedBlock())) {

                    File playerFile = new File(BlockHunt.plugin.getDataFolder() + File.separator + "Players" + File.separator + player.getName() + ".yml");

                    if (Config.getInt(playerFile, "numberOfCollected") < BlockHunt.blockCount) {

                        int index = BlockHunt.blocks.indexOf(event.getClickedBlock()) + 1;

                        if (!Config.getBool(playerFile, String.valueOf(index))) {

                            Config.set(playerFile, String.valueOf(index), true);
                            Config.set(playerFile, "numberOfCollected", Config.getInt(playerFile, "numberOfCollected") + 1);
                            player.sendMessage(BlockHunt.prefix + BlockHunt.blockFoundMessage.replace("!", String.valueOf(BlockHunt.blockCount)).replace("?", String.valueOf(Config.getInt(playerFile, "numberOfCollected"))));

                        } else {
                            player.sendMessage(BlockHunt.prefix + BlockHunt.blockAlreadyFoundMessage);
                        }
                        Config.set(playerFile, "allFound", false);
                    } else {
                        if (!Config.getBool(playerFile, "allFound")) {
                            player.sendMessage(BlockHunt.prefix + BlockHunt.allBlocksFoundMessage);
                            Config.set(playerFile, "allFound", true);
                            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                            String command = BlockHunt.prizeCommand.replace("^", player.getName());
                            Bukkit.dispatchCommand(console, command);
                        } else {
                            player.sendMessage(BlockHunt.prefix + BlockHunt.allBlocksAlreadyFoundMessage);
                        }


                    }
                }

            }

        }
    }
}
