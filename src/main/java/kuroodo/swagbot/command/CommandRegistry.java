package kuroodo.swagbot.command;

import kuroodo.swagbot.command.chatcommand.CommandBlank;
import kuroodo.swagbot.command.chatcommand.config.CommandSetup;
import kuroodo.swagbot.command.chatcommand.config.CommandSetupHelp;
import kuroodo.swagbot.command.chatcommand.fun.CommandAvatar;
import kuroodo.swagbot.command.chatcommand.fun.CommandFlipCoin;
import kuroodo.swagbot.command.chatcommand.fun.CommandLaser;
import kuroodo.swagbot.command.chatcommand.fun.CommandMagicBall;
import kuroodo.swagbot.command.chatcommand.fun.CommandPoke;
import kuroodo.swagbot.command.chatcommand.fun.CommandRoulette;
import kuroodo.swagbot.command.chatcommand.fun.CommandSlap;
import kuroodo.swagbot.command.chatcommand.fun.CommandSpartanKick;
import kuroodo.swagbot.command.chatcommand.fun.CommandUserInfo;
import kuroodo.swagbot.command.chatcommand.help.CommandHelp;
import kuroodo.swagbot.command.chatcommand.lavaplayer.CommandLavaPlayer;
import kuroodo.swagbot.command.chatcommand.moderation.CommandBan;
import kuroodo.swagbot.command.chatcommand.moderation.CommandClearChat;
import kuroodo.swagbot.command.chatcommand.moderation.CommandKick;
import kuroodo.swagbot.command.chatcommand.moderation.CommandMute;
import kuroodo.swagbot.command.chatcommand.moderation.CommandUnmute;

public class CommandRegistry {
	public static Command getCommand(String command) {

		switch (command) {
		// HELP commands
		case CommandKeys.COMMAND_HELP:
			return new CommandHelp();

		// MODERATION commands
		case CommandKeys.COMMAND_BAN:
			return new CommandBan();
		case CommandKeys.COMMAND_KICK:
			return new CommandKick();
		case CommandKeys.COMMAND_MUTE:
			return new CommandMute();
		case CommandKeys.COMMAND_UNMUTE:
			return new CommandUnmute();
		case CommandKeys.COMMAND_CLEARCHAT:
			return new CommandClearChat();

		// FUN commands
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
		case CommandKeys.COMMAND_SPARTANKICK:
			return new CommandSpartanKick();
		case CommandKeys.COMMAND_MAGICBALL:
			return new CommandMagicBall();

		// CONFIG commands
		case CommandKeys.COMMAND_SETUP:
			return new CommandSetup();
		case CommandKeys.COMMAND_SETUPHELP:
			return new CommandSetupHelp();

		// LAVA PLAYER Commands
		case CommandKeys.COMMAND_PLAY:
		case CommandKeys.COMMAND_PLAYRANDOM:
		case CommandKeys.COMMAND_PAUSE:
		case CommandKeys.COMMAND_RESUME:
		case CommandKeys.COMMAND_STOP:
		case CommandKeys.COMMAND_SKIP:
		case CommandKeys.COMMAND_TRACKINFO:
			return new CommandLavaPlayer();
		default:
			// Return empty command
			return new CommandBlank();
		}
	}

	public static boolean isCommandRegistered(String command) {
		return !(getCommand(command) instanceof CommandBlank);
	}
}
