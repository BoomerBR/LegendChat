package br.com.devpaulo.legendchat.delays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import br.com.devpaulo.legendchat.api.Legendchat;
import br.com.devpaulo.legendchat.channels.types.Channel;

public class Delay {
	private String name = "";
	private final HashMap<Channel,Integer> delays = new HashMap<>();
	private final HashMap<Channel,BukkitTask> timers = new HashMap<>();
	public Delay(String name) {
		this.name=name.toLowerCase();
	}
	
	public void addDelay(final Channel c) {
		removeDelay(c);
		delays.put(c, c.getDelayPerMessage());
		timers.put(c, Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("Legendchat"), new Runnable() {
                        @Override
			public void run() {
				if(hasDelay(c)) {
					int delay = getDelay(c);
					if(delay>0) {
						delays.remove(c);
						delays.put(c, delay-1);
					}
					else {
						removeDelay(c);
					}
				}
			}
		}, 20, 20));
	}
	
	public void removeDelay(Channel c) {
		if(hasDelay(c)) {
			timers.get(c).cancel();
			timers.remove(c);
			delays.remove(c);
			if(delays.isEmpty())
				Legendchat.getDelayManager().removePlayerDelay(name);
		}
	}
	
	public boolean hasDelay(Channel c) {
		return delays.containsKey(c);
	}
	
	public int getDelay(Channel c) {
		if(hasDelay(c))
			return delays.get(c);
		return 0;
	}
	
	public String getPlayerName() {
		return name;
	}
	
	public int getDelaysSize() {
		return delays.size();
	}
	
	public List<Channel> getDelayedChannels() {
		List<Channel> c = new ArrayList<>();
		c.addAll(delays.keySet());
		return c;
	}

}
