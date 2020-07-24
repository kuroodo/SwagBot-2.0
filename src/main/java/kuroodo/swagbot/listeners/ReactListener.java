package kuroodo.swagbot.listeners;

import java.util.HashMap;

import kuroodo.swagbot.reactions.ReactActivity;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactListener extends ListenerAdapter {
	// MessageID, Activity
	private static final HashMap<Long, ReactActivity> REACT_ACTIVITIES = new HashMap<Long, ReactActivity>();

	public static void addActivity(ReactActivity activity) {
		REACT_ACTIVITIES.put(activity.messageID, activity);
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		super.onGuildMessageReactionAdd(event);
		// Todo: tell the relevant ReactActivity that a user reacted
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		super.onGuildMessageReactionRemove(event);
		System.out.println("REACT REMOVE");
	}
}
