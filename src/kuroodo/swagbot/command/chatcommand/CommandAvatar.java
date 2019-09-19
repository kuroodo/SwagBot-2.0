package kuroodo.swagbot.command.chatcommand;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandAvatar extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		// no params = author avatar
		if (commandParams.length == 1) {
			sendEmbed(makeEmbed(event.getAuthor()));
		} else if (!event.getMessage().getMentionedUsers().isEmpty()) {
			sendEmbed(makeEmbed(event.getMessage().getMentionedUsers().get(0)));
		} else {
			Member member = event.getGuild().getMemberById(commandParams[1]);
			if (member != null) {
				sendEmbed(makeEmbed(member.getUser()));
			}
		}
	}

	private EmbedBuilder makeEmbed(User user) {
		String name = user.getName();
		String avatarURL = user.getAvatarUrl();

		EmbedBuilder eb = new EmbedBuilder();

		eb.setColor(Color.RED);
		eb.addField(name + "'s Avatar: ", user.getAsMention(), false);
		eb.setImage(avatarURL);
		return eb;
	}

	@Override
	public String commandDescription() {
		return "Get the avatar of a user\nUsage: !avatar or !avatar @user";
	}

}
