package kuroodo.swagbot.command.chatcommand.moderation;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandKick extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.KICK_MEMBERS);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}

		Member member = null;

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			member = event.getMessage().getMentionedMembers().get(0);
		} else if (commandParams.length > 1) { // Else check if entered a user ID
			// Check if entered valid long ID
			try {
				member = event.getGuild().getMemberById(commandParams[1]);
			} catch (NumberFormatException e) {
			}
		}

		if (member == null) {
			sendMessage("Please mention a valid user or ensure correct command format");
			return;
		}

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performKick(member);
		} else {
			sendMessage("This person is too important to be removed");
		}
	}

	private void performKick(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String reason = getReason();

		member.kick(reason).queue();

		logKick(settings, reason, member);
		sendKickMessage(member);
	}

	private void sendKickMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was kicked"));
	}

	private void logKick(GuildSettings settings, String reason, Member member) {
		String logMessage = event.getAuthor().getAsMention() + " has KICKED user " + member.getAsMention()
				+ " with reason: " + reason;

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
		return "Kick a member from the server";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "kick @user <reason>(optional)";
	}
}