package br.com.devpaulo.legendchat.channels;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import br.com.devpaulo.legendchat.api.Legendchat;
import br.com.devpaulo.legendchat.channels.types.BungeecordChannel;
import br.com.devpaulo.legendchat.channels.types.Channel;
import br.com.devpaulo.legendchat.channels.types.PermanentChannel;

public class ChannelManager {
	private final HashMap<String,Channel> channels = new HashMap<>();
	public ChannelManager() {
	}
	
	public void createChannel(Channel c) {
		if(existsChannel(c.getName()))
			return;
		channels.put(c.getName().toLowerCase(), c);
	}
	
	public void createPermanentChannel(Channel c) {
		if(existsChannel(c.getName()))
			return;
		channels.put(c.getName().toLowerCase(), c);
		File channel = new File(Legendchat.getPlugin().getDataFolder(),"channels"+File.separator+c.getName().toLowerCase()+".yml");
		if(!channel.exists()) {
			try {channel.createNewFile();} catch(Exception e) {}
			YamlConfiguration channel2 = YamlConfiguration.loadConfiguration(channel);
			channel2.set("name", c.getName());
			channel2.set("nickname", c.getNickname());
			channel2.set("format", c.getFormat());
			channel2.set("color", c.getStringColor());
			channel2.set("shortcutAllowed", c.isShortcutAllowed());
			channel2.set("needFocus", c.isFocusNeeded());
			channel2.set("distance", c.getMaxDistance());
			channel2.set("crossworlds", c.isCrossworlds());
			channel2.set("delayPerMessage", c.getDelayPerMessage());
			channel2.set("costPerMessage", c.getCostPerMessage());
			channel2.set("showCostMessage", c.showCostMessage());
			try {channel2.save(channel);} catch (Exception e) {}
		}
	}
	
	public void deleteChannel(Channel c) {
		if(!existsChannel(c.getName()))
			return;
		for(Player p : c.getPlayersFocusedInChannel())
			Legendchat.getPlayerManager().setPlayerFocusedChannel(p, Legendchat.getDefaultChannel(), false);
		channels.remove(c.getName().toLowerCase());
		new File(Legendchat.getPlugin().getDataFolder(),"channels"+File.separator+c.getName().toLowerCase()+".yml").delete();
	}
	
	public Channel getChannelByName(String name) {
		name = name.toLowerCase();
		if(existsChannel(name))
			return channels.get(name);
		return null;
	}
	
	public Channel getChannelByNickname(String nickname) {
		for(Channel c : getChannels())
			if(c.getNickname().equalsIgnoreCase(nickname))
				return c;
		return null;
	}
	
	public Channel getChannelByNameOrNickname(String name_or_nickname) {
		Channel c = getChannelByName(name_or_nickname);
		if(c==null)
			c = getChannelByNickname(name_or_nickname);
		return c;
	}
	
	public boolean existsChannel(String name) {
		return channels.containsKey(name.toLowerCase());
	}
	
	public boolean existsChannelAdvanced(String name_or_nickname) {
		boolean e = channels.containsKey(name_or_nickname.toLowerCase());
		if(!e)
			e = (getChannelByNickname(name_or_nickname) != null);
		return e;
	}
	
	public List<Channel> getChannels() {
		List<Channel> c = new ArrayList<>();
		c.addAll(channels.values());
		return c;
	}
	
	public void loadChannels() {
		String bungee = Legendchat.getPlugin().getConfig().getString("bungeecord.channel");
		channels.clear();
		for (File channel : new File(Legendchat.getPlugin().getDataFolder(),"channels").listFiles()) {
			if(channel.getName().toLowerCase().endsWith(".yml")) {
		        if(!channel.getName().toLowerCase().equals(channel.getName()))
		        	channel.renameTo(new File(Legendchat.getPlugin().getDataFolder(),"channels"+File.separator+channel.getName().toLowerCase()));
		        loadChannel(channel,bungee);
			}
	    }
		for(Player p : Bukkit.getOnlinePlayers())
			Legendchat.getPlayerManager().setPlayerFocusedChannel(p, Legendchat.getDefaultChannel(), false);
	}
	
	private void loadChannel(File channel, String bungee) {
		YamlConfiguration channel2 = YamlConfiguration.loadConfiguration(channel);
		if(channel2.getString("name").toLowerCase().equals(bungee.toLowerCase()))
			createPermanentChannel(new BungeecordChannel(channel2.getString("name"),channel2.getString("nickname"),channel2.getString("format"),channel2.getString("color"),channel2.getBoolean("shortcutAllowed"),channel2.getBoolean("needFocus"),channel2.getDouble("distance"),channel2.getBoolean("crossworlds"),channel2.getInt("delayPerMessage"),channel2.getDouble("costPerMessage"),channel2.getBoolean("showCostMessage")));
		else
			createPermanentChannel(new PermanentChannel(channel2.getString("name"),channel2.getString("nickname"),channel2.getString("format"),channel2.getString("color"),channel2.getBoolean("shortcutAllowed"),channel2.getBoolean("needFocus"),channel2.getDouble("distance"),channel2.getBoolean("crossworlds"),channel2.getInt("delayPerMessage"),channel2.getDouble("costPerMessage"),channel2.getBoolean("showCostMessage")));
	}
	
}
