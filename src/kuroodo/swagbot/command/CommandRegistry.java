package kuroodo.swagbot.command;

import kuroodo.swagbot.command.chatcommand.CommandBlank;
import kuroodo.swagbot.command.chatcommand.config.CommandSetup;
import kuroodo.swagbot.command.chatcommand.config.CommandSetupHelp;
import kuroodo.swagbot.command.chatcommand.config.CommandUserInfo;
import kuroodo.swagbot.command.chatcommand.fun.CommandAvatar;
import kuroodo.swagbot.command.chatcommand.fun.CommandFlipCoin;
import kuroodo.swagbot.command.chatcommand.fun.CommandLaser;
import kuroodo.swagbot.command.chatcommand.fun.CommandPoke;
import kuroodo.swagbot.command.chatcommand.fun.CommandRoulette;
import kuroodo.swagbot.command.chatcommand.fun.CommandSlap;
import kuroodo.swagbot.command.chatcommand.help.CommandHelp;

public class CommandRegistry {
	public static Command getCommand(String command) {

		switch (command) {
		case CommandKeys.COMMAND_HELP:
			return new CommandHelp();
		case CommandKeys.COMMAND_FLIPCOIN:
			return new CommandFlipCoin();
		case CommandKeys.COMMAND_AVATAR:
			return new CommandAvatar();
		case CommandKeys.COMMAND_USERINFO:
			return new CommandUserInfo();
		case CommandKeys.COMMAND_POKE:
			return new CommandPoke();
		case CommandKeys.COMMAND_LASER:
			return new CommandLaser();
		case CommandKeys.COMMAND_SLAP:
			return new CommandSlap();
		case CommandKeys.COMMAND_ROULETTE:
			return new CommandRoulette();
		case CommandKeys.COMMAND_SETUP:
			return new CommandSetup();
		case CommandKeys.COMMAND_SETUPHELP:
			return new CommandSetupHelp();

		default:
			// Return empty command
			return new CommandBlank();
		}
	}
}
