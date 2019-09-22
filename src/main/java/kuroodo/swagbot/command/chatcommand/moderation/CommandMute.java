package kuroodo.swagbot.command.chatcommand.moderation;

import java.util.Timer;
import java.util.TimerTask;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import kuroodo.swagbot.utils.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandMute extends ChatCommand {

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
		Member member = findParamsMember();

		if (member == null) {
			sendMessage("Please mention a valid user or ensure correct command format");
			return;
		}

		if (GuildManager.canMemberBeRemoved(event.getGuild(), member)) {
			performMute(member);
		} else {
			sendMessage("This person is too important to be muted");
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}
	}

	private void performMute(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		Role muterole = settings.guild.getRoleById(settings.muteRole);
		String reason = getReason();
		long duration = 0;

		if (muterole != null) {
			// If entered a mute duration
			if (commandParams.length >= 3) {
				try {
					duration = Long.parseLong(commandParams[2]);
				} catch (NumberFormatException e) {
					sendMessage("ERROR: Mute duration is incorrect. Please enter a NUMBER of days");
				}
			}

			// Give mute role
			settings.guild.addRoleToMember(member, muterole).queue();
			if (duration > 0)
				startUnmuteTimer(duration, member);

			logMute(settings, duration, reason, member);
			sendMuteMessage(member);
		}
	}

	private void unMute(Member member) {
		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		Role muterole = settings.guild.getRoleById(settings.muteRole);

		if (muterole != null) {
			if (member.getRoles().contains(muterole)) {
				settings.guild.removeRoleFromMember(member, muterole).queue();
				logUnmute(settings, member);
			}
		}
	}

	private void sendMuteMessage(Member member) {
		sendMessage(BotUtility.boldifyText(member.getUser().getAsTag() + " was muted"));
	}

	private void logMute(GuildSettings settings, long duration, String reason, Member member) {
		String logMessage = event.getAuthor().getAsMention() + " has MUTED user " + member.getAsMention();

		if (duration > 0) {
			logMessage += " for " + duration + " minute(s)";
		}

		logMessage += " with reason: " + reason;

		Logger.sendLogMessage(settings, BotUtility.quotifyText(logMessage));
	}

	private void startUnmuteTimer(long duration, Member member) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				unMute(member);
			}
		}, duration * 60000);
	}

	private void logUnmute(GuildSettings settings, Member member) {
		String logMessage = BotUtility
				.quotifyText("The MUTE duration for user " + member.getAsMention() + " has expired.");
		Logger.sendLogMessage(settings, logMessage);
	}

	private String getReason() {
		String reason = "";
		for (int i = 3; i < commandParams.length; i++) {
			reason += commandParams[i] + " ";
		}

		return reason;
	}

	@Override
	public String commandDescription() {
		return "Give a member the mute role";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "mute @user <duration MINUTES>(optional)";
	}
}
