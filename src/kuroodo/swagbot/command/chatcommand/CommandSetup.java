package kuroodo.swagbot.command.chatcommand;

import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.json.GuildSettingsWriter;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSetup extends ChatCommand {
	public CommandSetup() {
		isPermission0 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		int expectedParamsLength = 3;
		if (!memberHasPermissions(GuildManager.getGuild(event.getGuild().getIdLong()), event.getMember())) {
			sendNoPermissionsMessage();
			return;
		} else if (commandParams.length != expectedParamsLength) {
			sendMessage(BotUtility.quotifyText("Parameters incorrect.\nCorrect format: " + commandPrefix + "setup <param> <value> OR enter "
					+ commandPrefix + "setuphelp for more information"));
			return;
		}
		GuildSettings settings = GuildManager.getGuild(event.getGuild());

		switch (commandParams[1].toLowerCase()) {
		case JSONKeys.SETTINGS_COMMAND_PREFIX:
			if (commandParams[2].equals("-1")) {
				settings.commandPrefix = "!";
			} else {
				settings.commandPrefix = commandParams[2];
			}
			break;
		default:

		}

		GuildSettingsWriter.writeSettings(settings);
		GuildManager.reloadGuildSettings(settings.guildID);
		sendMessage(BotUtility.codifyText("Command prefix has been set to " + settings.commandPrefix));
	}

	@Override
	public String commandDescription() {
		// TODO Auto-generated method stub
		return super.commandDescription();
	}
}
