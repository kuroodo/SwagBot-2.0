/*
 * Copyright 2019 Leandro Gaspar

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
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

public class CommandVoiceKick extends PunishmentCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.VOICE_MOVE_OTHERS);
		isPermission0 = true;
		isPermission1 = true;
		isPermission2 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!canExecute)
			return;

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performVoiceKick(member);
		} else {
			sendMessage("This person is too important to be removed");
		}

	}

	private void performVoiceKick(Member member) {
		try {
			if (!member.getVoiceState().inVoiceChannel()) {
				sendNoChannelMessage(member);
				return;
			}

			String channelName = member.getVoiceState().getChannel().getName();
			event.getGuild().kickVoiceMember(member).queue();
			sendSuccessMessage(member, channelName);

		} catch (HierarchyException e) {
			sendHierarchyErrorMessage();
		}
	}

	private void sendNoChannelMessage(Member member) {
		sendMessage(BotUtility.quotifyText(member.getUser().getAsTag() + " is not currently in a voice channel"));
	}

	private void sendSuccessMessage(Member member, String channelName) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " has been kicked from " + channelName));
	}

	@Override
	public String commandDescription() {
		return "Kick a member from the voice channel they are in";
	}

	@Override
	public String commandFormat() {
		return "`" + commandPrefix + CommandKeys.COMMAND_VOICEKICK + "` @user";
	}

	@Override
	public String commandUsageExample() {
		return "`" + commandPrefix + CommandKeys.COMMAND_VOICEKICK + "` @Person#1234";
	}
}