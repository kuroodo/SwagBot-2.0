package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSlap extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!selfHasPermissions()) {
			return;
		}

		Member member = findParamsMember();
		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}
		sendMessage(event.getAuthor().getAsMention() + " slaps " + member.getAsMention());
	}

	@Override
	public String commandDescription() {
		return "Slap a user";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "slap @user";
	}
}
