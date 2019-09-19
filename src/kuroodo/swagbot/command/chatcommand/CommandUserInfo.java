package kuroodo.swagbot.command.chatcommand;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUserInfo extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (commandParams.length == 1) {
			sendEmbed(makeEmbed(event.getMember()));
		} else if (!event.getMessage().getMentionedMembers().isEmpty()) {
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
		String name = member.getUser().getAsTag();
		String nickname = member.getEffectiveName();
		String avatarURL = member.getUser().getAvatarUrl();
		String status = member.getOnlineStatus().toString();
		String joinDate = member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE);

		String boostTime = "Not Boosting";
		if (member.getTimeBoosted() != null) {
			boostTime = member.getTimeBoosted().toString();
		}

		eb.setColor(Color.RED);
		eb.setTitle(nickname + "'s info");
		eb.addField("Username", name, true);
		eb.addField("Status", status, true);
		eb.addField("Joined Server", joinDate, true);
		eb.addField("Nitro Boost", boostTime, true);
		eb.addField("Avatar: ", member.getAsMention(), false);
		eb.setImage(avatarURL);

		return eb;
	}

	@Override
	public String commandDescription() {
		return "Get information of a user\nUsage: " + commandPrefix + "userinfo or " + commandPrefix + "userinfo @user";
	}
}
