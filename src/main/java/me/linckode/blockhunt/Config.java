package me.linckode.blockhunt;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    public static void set(File file, String path, Object value){

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        try {
            configuration.set(path,value);
            configuration.save(file);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Object getObj(File file, String path){

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        return configuration.get(path);
    }

    public static int getInt(File file, String path){

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        return configuration.getInt(path);
    }

    public static String getString(File file, String path){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        return configuration.getString(path);
    }

    public static boolean getBool(File file, String path){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        return configuration.getBoolean(path);
    }

    public static String messageParser(String message){

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
