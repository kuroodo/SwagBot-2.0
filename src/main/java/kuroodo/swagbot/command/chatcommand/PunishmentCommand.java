package kuroodo.swagbot.command.chatcommand;

import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PunishmentCommand extends ChatCommand {
	protected Member member;

	@Override
	protected void setCommandPermissiosn() {
		return;
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
		member = findParamsMember();

		if (member == null) {
			return;
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

	}

	protected String getReason(int reasonStartIndex) {
		String reason = "";
		for (int i = reasonStartIndex; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}

	protected void sendHierarchyErrorMessage() {
		sendMessage(
				"Action cannot be performed. My role is positioned to low compared to the other role, or other member's role(s)");
	}
}
