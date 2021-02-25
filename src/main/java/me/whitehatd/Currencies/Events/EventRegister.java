package me.whitehatd.Currencies.Events;

import me.whitehatd.Currencies.Events.Event.ConnectEvent;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class EventRegister {

    public ArrayList<Listener> events = new ArrayList<>();

    public EventRegister(){
        events.add(new ConnectEvent());
    }
}
