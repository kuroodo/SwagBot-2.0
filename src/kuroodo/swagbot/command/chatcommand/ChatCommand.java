package kuroodo.swagbot.command.chatcommand;

import java.util.function.Consumer;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChatCommand extends Command {
	protected MessageReceivedEvent event;
	protected String[] commandParams;

	protected String commandPrefix = "";
	protected boolean isPrivateMessageCommand = false;
	protected boolean isPermission0 = false, isPermission1 = false, isPermission2 = false;

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		this.event = event;
		this.commandParams = commandParams;
		commandPrefix = GuildManager.getGuild(event.getGuild().getIdLong()).commandPrefix;
	}

	@Override
	public String commandDescription() {
		return "ERROR: Command not found or no description";
	}

	protected void sendMessage(String message) {
		if (event.isFromGuild()) {
			event.getChannel().sendMessage(message).queue();

		} else if (event.isFromType(ChannelType.PRIVATE)) {

			event.getAuthor().openPrivateChannel().queue(new Consumer<PrivateChannel>() {
				@Override
				public void accept(PrivateChannel t) {
					t.sendMessage(message).queue();
				}
			});

		}
	}

	protected boolean memberHasPermissions(GuildSettings guild, Member member) {
		// If member role is of higher permission levels
		if (isPermission0) {
			Role permission0Role = guild.guild.getRoleById(guild.rolePermission0);

			return member.getRoles().contains(permission0Role) || member.isOwner()
					|| member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
		} else if (isPermission1) {
			Role permission1Role = guild.guild.getRoleById(guild.rolePermission1);
			return member.getRoles().contains(permission1Role);
		} else if (isPermission2) {
			Role permission2Role = guild.guild.getRoleById(guild.rolePermission2);
			return member.getRoles().contains(permission2Role);
		}

		return true;
	}

	protected void sendNoPermissionsMessage() {
		sendMessage("You lack permissions to use this command");
	}

}
