package br.com.devpaulo.legendchat.channels.types;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import br.com.devpaulo.legendchat.api.Legendchat;
import br.com.devpaulo.legendchat.channels.utils.ChannelUtils;

public class PermanentChannel implements Channel {
	private String name = "";
	private String nick = "";
	private String format = "";
	private String color = "";
	private String color2 = "";
	private boolean shortcut = false;
	private boolean focus = false;
	private double distance = 0;
	private boolean crossworlds = false;
	private double cost = 0;
	private boolean show_cost_msg = false;
	private int delay = 0;
	public PermanentChannel(String name, String nick, String format, String color, boolean shortcut, boolean focus, double distance, boolean crossworlds, int delay, double cost,boolean show_cost_msg) {
		this.name=name;
		this.nick=nick;
		this.format=format;
		this.color=ChannelUtils.translateStringColor(color);
		color2=color.toLowerCase();
		this.shortcut=shortcut;
		this.focus=focus;
		this.distance=distance;
		this.crossworlds=crossworlds;
		this.cost=cost;
		this.show_cost_msg=show_cost_msg;
		this.delay=delay;
	}
	
        @Override
	public String getName() {
		return name;
	}
	
        @Override
	public String getNickname() {
		return nick;
	}
	
        @Override
	public String getFormat() {
		return format;
	}
	
        @Override
	public String getColor() {
		return color;
	}
	
        @Override
	public String getStringColor() {
		return color2;
	}
	
        @Override
	public boolean isShortcutAllowed() {
		return shortcut;
	}
	
        @Override
	public boolean isFocusNeeded() {
		return focus;
	}
	
        @Override
	public boolean isCrossworlds() {
		return crossworlds;
	}
	
        @Override
	public double getMaxDistance() {
		return distance;
	}
	
        @Override
	public double getMessageCost() {
		return cost;
	}
        @Override
	public double getCostPerMessage() {
		return cost;
	}
	
        @Override
	public boolean showCostMessage() {
		return show_cost_msg;
	}
	
        @Override
	public int getDelayPerMessage() {
		return delay;
	}
	
        @Override
	public void setNickname(String n) {
		nick=n;
	}
	
        @Override
	public void setFormat(String n) {
		format=n;
	}
	
        @Override
	public void setColor(ChatColor c) {
		color2=ChannelUtils.translateChatColorToStringColor(c);
		color=ChannelUtils.translateStringColor(color2);
	}
	
        @Override
	public void setShortcutAllowed(boolean n) {
		shortcut=n;
	}
	
        @Override
	public void setFocusNeeded(boolean n) {
		focus=n;
	}
	
        @Override
	public void setCrossworlds(boolean n) {
		crossworlds=n;
	}
	
        @Override
	public void setMaxDistance(double n) {
		distance=n;
	}
	
        @Override
	public void setMessageCost(double n) {
		cost=n;
	}
        @Override
	public void setCostPerMessage(double n) {
		cost=n;
	}
	
        @Override
	public void setShowCostMessage(boolean n) {
		show_cost_msg=n;
	}
	
        @Override
	public void setDelayPerMessage(int n) {
		delay=n;
	}
	
        @Override
	public List<Player> getPlayersFocusedInChannel() {
		return Legendchat.getPlayerManager().getPlayersFocusedInChannel(this);
	}
	
        @Override
	public List<Player> getPlayersWhoCanSeeChannel() {
		return Legendchat.getPlayerManager().getPlayersWhoCanSeeChannel(this);
	}
	
        @Override
	public void sendMessage(final String message) {
		ChannelUtils.otherMessage(this, message);
	}
	
        @Override
	public void sendMessage(final Player sender, final String message) {
		ChannelUtils.fakeMessage(this, sender, message);
	}
	
        @Override
	public void sendMessage(Player sender, String message, String bukkit_format, boolean cancelled) {
		ChannelUtils.realMessage(this, sender, message, bukkit_format, cancelled);
	}
	
}
