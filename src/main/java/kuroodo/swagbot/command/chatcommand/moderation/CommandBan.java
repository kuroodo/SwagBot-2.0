package kuroodo.swagbot.command.chatcommand.moderation;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandBan extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.BAN_MEMBERS);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}
		// If empty parameters
		if (commandParams.length <= 1) {
			sendMessage(commandFormat());
			return;
		}

		Member member = findParamsMember();

		if (member == null) {
			sendMessage("Please mention a valid user or ensure correct command format");
			return;
		}

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performBan(member);
		} else {
			sendMessage("This person is too important to be removed");
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}
	}

	private void performBan(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String reason = getReason();
		int days = 0;

		// If entered a ban duration
		if (commandParams.length >= 3) {
			try {
				days = Integer.parseInt(commandParams[2]);
			} catch (NumberFormatException e) {
				sendMessage("ERROR: Ban duration is incorrect. Please enter a NUMBER of days, or 0 for permanent ban");
				return;
			}
		}

		member.ban(days, reason).queue();

		logBan(settings, days, reason, member);
		sendBanMessage(member);
	}

	private void sendBanMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was banned"));

	}

	private void logBan(GuildSettings settings, int days, String reason, Member member) {
		String logMessage = event.getAuthor().getAsMention() + " has BANNED user " + member.getAsMention();

		if (days > 0) {
			logMessage += " for " + days + " day(s)";
		}
		logMessage += " with reason: " + reason;

		Logger.sendLogMessage(settings, BotUtility.quotifyText(logMessage));
	}

	private String getReason() {
		String reason = "";
		for (int i = 3; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}

	@Override
	public String commandDescription() {
		return "Ban a member from the server";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "ban @user <duration DAYS> <reason>(optional) , enter 0 for days for a permanent ban";
	}

}
