package kuroodo.swagbot.command.chatcommand;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandAvatar extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (commandParams.length == 1) {
			sendEmbed(makeEmbed(event.getMember()));
		} else if (!event.getMessage().getMentionedUsers().isEmpty()) {
			sendEmbed(makeEmbed(event.getMessage().getMentionedMembers().get(0)));
		} else {
			Member member = null;
			// Check if entered valid long ID
			try {
				member = event.getGuild().getMemberById(commandParams[1]);
			} catch (NumberFormatException e) {
			}

			if (member != null) {
				sendEmbed(makeEmbed(member));
			} else {
				sendMessage("Please mention a valid user");
			}
		}
	}

	private EmbedBuilder makeEmbed(Member member) {
		EmbedBuilder eb = new EmbedBuilder();

		String name = member.getUser().getName();
		String avatarURL = member.getUser().getAvatarUrl();

		eb.setColor(Color.RED);
		eb.addField(name + "'s Avatar: ", member.getAsMention(), false);
		eb.setImage(avatarURL);
		return eb;
	}

	@Override
	public String commandDescription() {
		return "Get the avatar of a user\nUsage: " + commandPrefix + "avatar or " + commandPrefix + "avatar @user";
	}

}
