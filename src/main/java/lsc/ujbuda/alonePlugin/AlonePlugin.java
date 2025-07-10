package lsc.ujbuda.alonePlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class AlonePlugin extends JavaPlugin implements Listener {
    public static Map<String, Player> playersAloneMap = new HashMap<>();
    static Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        getServer().getPluginManager().registerEvents(this, this);
        log.info("AlonePlugin successfully initialized.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmdObj, String label, String[] args) {
        try{
            if(cmdObj.getName().equalsIgnoreCase("alone")) {
                String user = sender.getName();
                if (user.equals("CONSOLE")) {
                    sender.sendMessage("[AlonePlugin] Console may not run this command.");
                    return true;
                }

                List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                Player senderPlayer = (Player) sender;

                if (playersAloneMap.containsKey(user)) {
                    playersAloneMap.remove(user);
                    sender.sendMessage("[AlonePlugin] You're no longer alone.");
                    for (Player player : onlinePlayers) {
                        senderPlayer.showPlayer(this, player);
                    }
                } else {
                    playersAloneMap.put(user, senderPlayer);
                    sender.sendMessage("[AlonePlugin] You're now alone.");
                    for (Player player : onlinePlayers) {
                        senderPlayer.hidePlayer(this, player);
                    }
                }

                return true;
            }
        } catch (Exception e) {
            sender.sendMessage("An unexpected error occured");
            log.info("Unexpected error");
            log.info(e.getMessage());
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        //Hide newly joined users
        for (Player player : playersAloneMap.values()) {
            player.hidePlayer(this, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String user = player.getName();
        playersAloneMap.remove(user);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        String user = player.getName();
        playersAloneMap.remove(user);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info("AlonePlugin successfully unloaded.");
    }
}
