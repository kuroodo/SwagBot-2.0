package kuroodo.swagbot.command.chatcommand.fun;

import java.awt.Color;
import java.time.format.DateTimeFormatter;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUserInfo extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
		requiredPermissions.add(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!selfHasPermissions()) {
			return;
		}

		Member member = findParamsMember();
		// If no parameters get the message author
		if (commandParams.length == 1) {
			member = event.getMember();
		}

		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}
		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

		sendEmbed(makeEmbed(member));
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
		return "Get information about a user or yourself";
	}

	@Override
	public String commandFormat() {
		return "To get your information: " + commandPrefix + "userinfo , To get a users information:  " + commandPrefix
				+ "userinfo @user";
	}
}