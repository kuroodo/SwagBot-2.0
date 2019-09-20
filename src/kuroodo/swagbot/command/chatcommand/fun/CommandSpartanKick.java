package kuroodo.swagbot.command.chatcommand.fun;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSpartanKick extends ChatCommand {
	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (event.getMessage().getMentionedMembers().size() == 0) {
			return;
		}

		Member member = event.getMessage().getMentionedMembers().get(0);
		if (!member.getVoiceState().inVoiceChannel()) {
			return;
		}
		
		Guild guild = event.getGuild();

		int totalChannels = guild.getVoiceChannels().size() - 1;
		int userCurrentIndex = guild.getVoiceChannels().indexOf(member.getVoiceState().getChannel()); // getUserChannelIndex(member);

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
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.VOICE_MOVE_OTHERS);
	}
}
