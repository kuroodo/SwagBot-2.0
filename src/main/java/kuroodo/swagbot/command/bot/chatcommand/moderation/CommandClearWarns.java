/*
 * Copyright 2020 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package kuroodo.swagbot.command.bot.chatcommand.moderation;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.bot.chatcommand.PunishmentCommand;
import kuroodo.swagbot.json.GuildMetaReader;
import kuroodo.swagbot.json.GuildMetaWriter;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandClearWarns extends PunishmentCommand {

	@Override
	protected void setCommandPermissiosn() {
		isPermission0 = true;
		isPermission1 = true;
		isPermission2 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!canExecute)
			return;

		long guildID = event.getGuild().getIdLong();

		if (!GuildMetaReader.metaFileExists(guildID)) {
			sendNoWarningsMessage();
			return;
		}

		String val = GuildMetaReader.getValue(guildID, member.getIdLong() + "_warns");
		int warnings = 0;

		if (!val.isEmpty()) {
			try {
				warnings = Integer.parseInt(val);
			} catch (NumberFormatException e) {
				System.err.println("ERROR ACCESSING WARNINGS FOR USER " + member.getIdLong() + " in guild " + guildID);
				return;
			}
		}

		if (warnings == 0) {
			sendNoWarningsMessage();
			return;
		}

		GuildMetaWriter.writeVal(guildID, member.getIdLong() + "_warns", "0");

		sendSuccessMessage(member, warnings);

	}

	private void sendSuccessMessage(Member member, int warnings) {
		sendMessage(BotUtility.quotifyText("Removed " + warnings + " warnings from " + member.getUser().getAsTag()));
	}

	private void sendNoWarningsMessage() {
		sendMessage(BotUtility.quotifyText(member.getUser().getAsTag() + " has no warnings!"));
	}

	@Override
	public String commandDescription() {
		return "Remove all warnings from a user";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_CLEARWARNS + "` @user";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_CLEARWARNS + "` @Person#1234";
	}
}
