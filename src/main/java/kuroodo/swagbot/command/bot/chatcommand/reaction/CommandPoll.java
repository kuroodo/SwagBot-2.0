package kuroodo.swagbot.command.bot.chatcommand.reaction;

import kuroodo.swagbot.command.bot.chatcommand.ChatCommand;
import kuroodo.swagbot.reactions.ReactPoll;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandPoll extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {

	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		for (String str : commandParams) {
			System.out.println(str);
		}

		ReactPoll p = new ReactPoll(event.getGuild().getIdLong(), event.getChannel().getIdLong(), "blank");
	}

}
