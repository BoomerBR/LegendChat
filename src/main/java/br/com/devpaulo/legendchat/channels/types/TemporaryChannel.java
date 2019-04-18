package br.com.devpaulo.legendchat.channels.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import br.com.devpaulo.legendchat.api.Legendchat;
import br.com.devpaulo.legendchat.channels.utils.ChannelUtils;

public final class TemporaryChannel implements Channel {
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
	private Player leader = null;
	private final List<Player> mods = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();
	private boolean mod_can_kick = false;
	private boolean mod_can_invite = false;
	private final List<Player> invites = new ArrayList<>();
	public TemporaryChannel(String name, String nick, String format, String color, boolean shortcut, boolean focus, double distance, boolean crossworlds, int delay, double cost,boolean show_cost_msg,Player leader,boolean mod_can_kick,boolean mod_can_invite) {
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
		this.mod_can_kick=mod_can_kick;
		this.mod_can_invite=mod_can_invite;
		leader_set(leader);
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
	
	public void setColorByString(String color) {
		this.color=ChannelUtils.translateStringColor(color);
		color2=color.toLowerCase();
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
	public List<Player> getPlayersFocusedInChannel() {
		return Legendchat.getPlayerManager().getPlayersFocusedInChannel(this);
	}
	
        @Override
	public List<Player> getPlayersWhoCanSeeChannel() {
		List<Player> pl = new ArrayList<>();
		pl.addAll(players);
		return pl;
	}
	
	public void invite_add(Player p) {
		if(!invites.contains(p))
			invites.add(p);
	}
	
	public void invite_remove(Player p) {
		if(invites.contains(p))
			invites.remove(p);
	}
	
	public List<Player> invite_list() {
		List<Player> invs = new ArrayList<>();
		invs.addAll(invites);
		return invs;
	}
	
	public void user_add(Player p) {
		if(invites.contains(p))
			invites.remove(p);
		if(!players.contains(p))
			players.add(p);
	}
	
	public void user_remove(Player p) {
		if(players.contains(p))
			players.remove(p);
		if(mods.contains(p))
			mods.remove(p);
		if(leader==p)
			leader_change();
	}
	
	public List<Player> user_list() {
		return getPlayersWhoCanSeeChannel();
	}
	
	public List<Player> moderator_list() {
		List<Player> mods2 = new ArrayList<>();
		mods2.addAll(mods);
		return mods2;
	}
	
	public void moderator_add(Player p) {
		if(!players.contains(p))
			user_add(p);
		if(!mods.contains(p))
			mods.add(p);
	}
	
	public void moderator_remove(Player p) {
		if(mods.contains(p))
			mods.remove(p);
	}
	
	public boolean moderator_canKick() {
		return mod_can_kick;
	}
	
	public boolean moderator_canInvite() {
		return mod_can_invite;
	}
	
	public void moderator_setCanKick(boolean b) {
		mod_can_kick=b;
	}
	
	public void moderator_setCanInvite(boolean b) {
		mod_can_invite=b;
	}
	
	public Player leader_get() {
		return leader;
	}
	
	public void leader_set(Player p) {
		if(p!=null) {
			if(!players.contains(p))
				players.add(p);
			if(mods.contains(p))
				mods.remove(p);
		}
		leader=p;
	}
	
	public void leader_change() {
		Player m = getModToLeader();
		if(m==null) {
			String msg = Legendchat.getMessageManager().getMessage("tc_msg1").replace("@channel", getName());
			for(Player p : players)
				p.sendMessage(msg);
			Legendchat.getChannelManager().deleteChannel(this);
		}
		else {
			String msg = Legendchat.getMessageManager().getMessage("tc_ch6").replace("@channel", getName()).replace("@player", m.getName());
			for(Player p : players)
				p.sendMessage(msg);
			leader_set(m);
		}
	}
	
	private Player getModToLeader() {
		if(mods.isEmpty())
			return null;
            for (Player p : mods) {
                int max_admin = Legendchat.getConfigManager().getTemporaryChannelConfig().getMaxAdminPerPlayer();
                if(max_admin==0)
                    return p;
                else if(Legendchat.getTemporaryChannelManager().getPlayerTempChannelsAdmin(p).size()<max_admin)
                    return p;
            }
		return null;
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
}
