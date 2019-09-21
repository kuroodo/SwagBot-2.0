package kuroodo.swagbot.command.chatcommand.moderation;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUnmute extends ChatCommand {
	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MANAGE_ROLES);
		isPermission0 = true;
		isPermission1 = true;
		isPermission2 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}

		Member member = null;

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			member = event.getMessage().getMentionedMembers().get(0);
		} else if (commandParams.length > 1) { // Else check if entered a user ID

			// Check if entered valid long ID
			try {
				member = event.getGuild().getMemberById(commandParams[1]);
			} catch (NumberFormatException e) {
			}
		}

		if (member == null) {
			sendMessage("Please mention a valid user");
			return;
		}
		performUnmute(member);
	}

	private void performUnmute(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		Role muterole = settings.guild.getRoleById(settings.muteRole);

		if (muterole != null) {
			if (member.getRoles().contains(muterole)) {
				settings.guild.removeRoleFromMember(member, muterole).queue();
				logUnmute(settings, member);
			}
		}
	}

	private void logUnmute(GuildSettings settings, Member member) {
		String logMessage = BotUtility.quotifyText(
				event.getAuthor().getName() + " has UNMUTED user " + member.getAsMention());
		Logger.sendLogMessage(settings, logMessage);
	}
}
