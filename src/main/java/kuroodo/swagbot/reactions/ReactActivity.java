package kuroodo.swagbot.reactions;

import kuroodo.swagbot.listeners.ReactListener;
import net.dv8tion.jda.api.entities.User;

public abstract class ReactActivity {
	public long guildID, messageID, channelID;
	protected String title = "";

	public ReactActivity(long guildID, long channelID, String title) {
		this.guildID = guildID;
		this.channelID = channelID;
		this.title = title;
	}

	public void onUserReact(User user, long emoteID) {

	}

	protected void addSelfToListener() {
		ReactListener.addActivity(this);
	}

}
