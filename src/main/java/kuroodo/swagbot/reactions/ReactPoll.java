package kuroodo.swagbot.reactions;

import java.awt.Color;
import java.time.Instant;
import java.util.HashMap;
import java.util.function.Consumer;

import kuroodo.swagbot.SwagBot;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class ReactPoll extends ReactActivity {
	// Entry EmoteID, name
	// TODO: Refactor this since there is a difference between an Emote and a
	// unicode Emoji. Maybe just store the Emote reference instead since unicode
	// emoji do not have an ID
	private HashMap<Long, String> values = new HashMap<Long, String>();

	public ReactPoll(long guildID, long channelID, String title) {
		super(guildID, channelID, title);
		EmbedBuilder eb = createEmbded("");
		SwagBot.getJDA().getGuildById(guildID).getTextChannelById(channelID).sendMessage(eb.build())
				.queue(new Consumer<Message>() {
					@Override
					public void accept(Message t) {
						messageID = t.getIdLong();
						t.editMessage(createEmbded(messageID + "").build()).queue();
					}
				});
	}

	@Override
	public void onUserReact(User user, long emoteID) {
		super.onUserReact(user, emoteID);
		System.out.println("User did the thing");
		// Update the message with new contents
	}

	public EmbedBuilder createEmbded(String msgID) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(SwagBot.getJDA().getSelfUser().getAsTag(), SwagBot.getJDA().getSelfUser().getAvatarUrl(),
				SwagBot.getJDA().getSelfUser().getAvatarUrl());
		eb.setColor(new Color(BotUtility.EMBED_ALERT_COLOR));
		eb.setTitle("Poll");
		eb.setDescription("Insert descriptive stuff on what the poll will look like/basic template");
		if (!msgID.isEmpty()) {
			eb.setFooter("Message ID: " + msgID);
		}
		eb.setTimestamp(Instant.now());
		return eb;
	}

	public void addEntry(long emoteID, String entryName) {
		if (values.containsKey(emoteID)) {
			// Send error message
			return;
		}

		values.put(emoteID, entryName);
	}

	// !poll <name>
	// !poll <msgID> <param> <content1> <content2>

}
