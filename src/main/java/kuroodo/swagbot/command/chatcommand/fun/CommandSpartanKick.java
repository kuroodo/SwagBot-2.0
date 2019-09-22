package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSpartanKick extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.VOICE_MOVE_OTHERS);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		Guild guild = event.getGuild();
		// If spartankick is disabled
		if (!GuildManager.getGuild(guild).spartankick) {
			return;
		}
		if (!selfHasPermissions()) {
			return;
		}

		if (event.getMessage().getMentionedMembers().size() == 0) {
			return;
		}

		Member member = findParamsMember();
		if (member == null) {
			return;
		}

		if (!member.getVoiceState().inVoiceChannel()) {
			return;
		}

		int totalChannels = guild.getVoiceChannels().size() - 1;
		int userCurrentIndex = guild.getVoiceChannels().indexOf(member.getVoiceState().getChannel());

		final int totalKicks = 3;
		for (int i = 0; i < totalKicks; i++) {
			userCurrentIndex++;
			// Go to first channel if on last channel
			if (userCurrentIndex > totalChannels) {
				userCurrentIndex = 0;
			}

			guild.moveVoiceMember(member, guild.getVoiceChannels().get(userCurrentIndex)).queue();
		}
	}

	@Override
	public String commandDescription() {
		return "Spartankick a user across voice channels, only if they are in a voicechannel";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_SPARTANKICK + " @user";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_SPARTANKICK + " @Person#1234";
	}
}
