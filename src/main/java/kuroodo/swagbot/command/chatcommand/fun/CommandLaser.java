package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandLaser extends ChatCommand {

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
		sendMessage(event.getAuthor().getAsMention() + " points a laser at " + member.getAsMention() + "'s eye");
	}

	@Override
	public String commandDescription() {
		return "Point a laser at someones eye";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_LASER + " @user";
	}
	
	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_LASER + " @Person#1234";
	}
}
