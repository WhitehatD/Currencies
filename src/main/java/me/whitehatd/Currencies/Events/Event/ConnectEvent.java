package me.whitehatd.Currencies.Events.Event;

import me.whitehatd.Currencies.Utilities.CurrencyUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        CurrencyUtil.registerPlayer(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
    }
}
