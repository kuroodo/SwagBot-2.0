package kuroodo.swagbot.command;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.command.chatcommand.CommandFlipCoin;
import kuroodo.swagbot.command.chatcommand.CommandSetup;

public class CommandRegistry {
	public static Command getCommand(String command) {
		
		switch(command){
		case CommandKeys.COMMAND_FLIPCOIN:
			return new CommandFlipCoin();
		case CommandKeys.COMMAND_SETUP:
			return new CommandSetup();
		default:
			// Return empty command
			return new ChatCommand();
		}
	}
}