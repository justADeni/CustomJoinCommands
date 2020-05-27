package me.prostedeni.goodcraft.customcommandjoin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomCommandJoin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getConfig();
        saveConfig();
    }

    static int getNumbers(String s) {

        String[] n = s.split(""); //array of strings
        StringBuffer f = new StringBuffer(); // buffer to store numbers

        for (int i = 0; i < n.length; i++) {
            if((n[i].matches("[0-9]+"))) {// validating numbers
                f.append(n[i]); //appending
            }else {
                //parsing to int and returning value
                return Integer.parseInt(f.toString());
            }
        }
        return 0;
    }
    //found pretty cool solution on https://stackoverflow.com/questions/53974884/get-the-first-numbers-from-a-string-on-java


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                String playerJoined = e.getPlayer().getName();
                if((getConfig().getString(playerJoined)) != null){
                    String executable = getConfig().getString(playerJoined);
                    if(getNumbers(executable) == 1) {
                        String decypheredCommand = executable.replaceFirst("1","");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), decypheredCommand);
                    } else if (getNumbers(executable) == 0){
                        String decypheredCommand = executable.replaceFirst("0","");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), decypheredCommand);

                        getConfig().set(playerJoined, null);
                        saveConfig();
                    }
                }
            }
        }, 15L);
        //waiting 15 ticks before starting to operate with player
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("onjoin")){
            if (sender instanceof Player) {
                if (sender.hasPermission("onjoin.use")) {
                    if (args.length == 0) {
                        sender.sendMessage(ChatColor.DARK_RED + ("Use /onjoin <player> <command> or /onjoin reload"));
                    } else if (args.length == 1){
                        if (args[0].equalsIgnoreCase("reload")){
                            reloadConfig();
                            getConfig();
                            saveConfig();
                            sender.sendMessage(ChatColor.DARK_GREEN + "Config reloaded sucesfully");
                        }
                    } else if (args.length == 2){
                        if (args[0].equalsIgnoreCase("remove")) {

                            if((getConfig().getString(args[1])) != null) {

                                getConfig().set(args[1], null);
                                saveConfig();
                                sender.sendMessage(ChatColor.DARK_GREEN + "Player removed succesfully");
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "Player not found to remove");
                            }
                        }
                    } else {
                        String purePlayer = args[0];
                            if((getConfig().getString(purePlayer)) == null) {

                                String build = "";
                                for(String s : args)
                                    build=build+" "+s;
                                build=build.replaceFirst(" "+args[0]+" "+args[1]+" ", "");

                                if (Boolean.parseBoolean(args[1])) {
                                    getConfig().set(purePlayer, 1+build);
                                } else {
                                    getConfig().set(purePlayer, 0+build);
                                }
                                saveConfig();
                                reloadConfig();
                                sender.sendMessage(ChatColor.DARK_GREEN + "Command added succesfully");
                            } else {
                                sender.sendMessage(ChatColor.DARK_RED + "That player already has command assigned");
                            }
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have required permissions");
                }
            } else {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.DARK_RED + ("Use /onjoin <player> <command> or /onjoin reload"));
                } else if (args.length == 1){
                    if (args[0].equalsIgnoreCase("reload")){
                        reloadConfig();
                        getConfig();
                        saveConfig();
                        sender.sendMessage(ChatColor.DARK_GREEN + "Config reloaded sucesfully");
                    }
                } else if (args.length == 2){
                    if (args[0].equalsIgnoreCase("remove")) {

                        if((getConfig().getString(args[1])) != null) {

                            getConfig().set(args[1], null);
                            saveConfig();
                            sender.sendMessage(ChatColor.DARK_GREEN + "Player removed succesfully");
                        } else {
                            sender.sendMessage(ChatColor.DARK_RED + "Player not found to remove");
                        }
                    }
                } else {
                    String purePlayer = args[0];
                    if((getConfig().getString(purePlayer)) == null) {

                        String build = "";
                        for(String s : args)
                            build=build+" "+s;
                        build=build.replaceFirst(" "+args[0]+" "+args[1]+" ", "");

                        if (Boolean.parseBoolean(args[1])) {
                            getConfig().set(purePlayer, 1+build);
                        } else {
                            getConfig().set(purePlayer, 0+build);
                        }
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(ChatColor.DARK_GREEN + "Command added succesfully");
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "That player already has command assigned");
                    }
                }
            }
        }
        return false;
    }
}
