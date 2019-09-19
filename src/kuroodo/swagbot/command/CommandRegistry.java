package kuroodo.swagbot.command;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.command.chatcommand.CommandAvatar;
import kuroodo.swagbot.command.chatcommand.CommandFlipCoin;
import kuroodo.swagbot.command.chatcommand.CommandHelp;
import kuroodo.swagbot.command.chatcommand.CommandSetup;
import kuroodo.swagbot.command.chatcommand.CommandSetupHelp;
import kuroodo.swagbot.command.chatcommand.CommandUserInfo;

public class CommandRegistry {
	public static Command getCommand(String command) {

		switch (command) {
		case CommandKeys.COMMAND_FLIPCOIN:
			return new CommandFlipCoin();
		case CommandKeys.COMMAND_SETUP:
			return new CommandSetup();
		case CommandKeys.COMMAND_HELP:
			return new CommandHelp();
		case CommandKeys.COMMAND_SETUPHELP:
			return new CommandSetupHelp();
		case CommandKeys.COMMAND_AVATAR:
			return new CommandAvatar();
		case CommandKeys.COMMAND_USERINFO:
			return new CommandUserInfo();
		default:
			// Return empty command
			return new ChatCommand();
		}
	}
}
