package kuroodo.swagbot.command;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.command.chatcommand.CommandFlipCoin;

public class CommandRegistry {
	public static Command getCommand(String command) {
		
		switch(command){
		case CommandKeys.COMMAND_FLIPCOIN:
			return new CommandFlipCoin();
		default:
			// Return empty command
			return new ChatCommand();
		}
	}
}
