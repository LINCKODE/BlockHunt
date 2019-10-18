package me.linckode.blockhunt;

import me.linckode.blockhunt.Commands.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public final class BlockHunt extends JavaPlugin {

    //vars
    public static Plugin plugin;
    public static File configFile;
    public static String prefix;
    public static String permission;
    public static int blockCount;
    static boolean canIndexBlocks = false;
    static ArrayList<Block> blocks = new ArrayList<>();
    static String prizeCommand;

    //event vars
    public static boolean canIndividuallySet = false;
    public static boolean canMultiSelect = false;
    public static int selectedBlock;

    //Player messages:
    static String blockFoundMessage;
    static String allBlocksFoundMessage;
    static String blockAlreadyFoundMessage;
    static String allBlocksAlreadyFoundMessage;

    //On join player messages
    static boolean useOnJoinMessages;
    static String onFirstJoinMessage;
    static String onJoinMessage;



    @Override
    public void onEnable() {
        plugin = this;

        //Config stuff
        saveDefaultConfig();
        configFile = new File(getDataFolder() + File.separator + "config.yml");
        loadConfigData();

        //Commands
        this.getCommand("blockhunt").setExecutor(new Main());

        //Events
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {


    }

    public static void loadConfigData(){
        if (configFile.exists()){
            //vars
            prefix = Config.messageParser(Config.getString(configFile,"prefix"));
            permission = Config.getString(configFile, "permission");
            blockCount = Config.getInt(configFile, "blockCount");
            prizeCommand = Config.getString(configFile, "prizeCommand");

            //messages
            blockFoundMessage = Config.messageParser(Config.getString(configFile, "blockFoundMessage"));
            allBlocksFoundMessage = Config.messageParser(Config.getString(configFile, "allBlocksFoundMessage"));
            blockAlreadyFoundMessage = Config.messageParser(Config.getString(configFile, "blockAlreadyFoundMessage"));
            allBlocksAlreadyFoundMessage = Config.messageParser(Config.getString(configFile, "allBlocksAlreadyFoundMessage"));

            //on join messages
            useOnJoinMessages = Config.getBool(configFile,"useOnJoinMessages");
            onFirstJoinMessage = Config.messageParser(Config.getString(configFile, "onFirstJoinMessage"));
            onJoinMessage = Config.messageParser(Config.getString(configFile, "onJoinMessage"));

            //Block assignment and stuff
            blocks.clear();
            for (int index = 1; index <= blockCount; index++){

                File blockFile = new File(plugin.getDataFolder() + File.separator + "Blocks" + File.separator + index + ".yml");

                String worldName = Config.getString(blockFile, "world");
                int x = Config.getInt(blockFile, "x");
                int y = Config.getInt(blockFile, "y");
                int z = Config.getInt(blockFile, "z");

                try {
                    Block block = Bukkit.getWorld(worldName).getBlockAt(x, y, z);
                    blocks.add(block);
                }
                catch (IllegalArgumentException e){
                    //
                }

            }

        }
        else {
            plugin.saveDefaultConfig();
            loadConfigData();
        }
    }
}
