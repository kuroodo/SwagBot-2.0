package kuroodo.swagbot.command.chatcommand;

import java.util.function.Consumer;

import kuroodo.swagbot.command.Command;
import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ChatCommand extends Command {
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
		setCommandPermissiosn();

		if (!selfHasPermissions()) {
			sendMessage("I lack the required permissions to run this command\nPermissions required: "
					+ permissionsToString());
		}
	}

	@Override
	public String commandDescription() {
		return "ERROR: Command not found or no description";
	}

	@Override
	public String commandFormat() {
		return "ERROR: Command not found or no format";
	}

	protected void sendMessage(String message) {
		// Check if has perms to send message
		if (!BotUtility.hasPermission(Permission.MESSAGE_WRITE, event.getTextChannel(),
				BotUtility.getSelfMember(event.getGuild()))) {
			return;
		}

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

	protected void sendEmbed(EmbedBuilder eb) {
		// Check if has perms to send message
		if (!BotUtility.hasPermission(Permission.MESSAGE_EMBED_LINKS, event.getTextChannel(),
				BotUtility.getSelfMember(event.getGuild()))) {
			return;
		}
		event.getChannel().sendMessage(eb.build()).queue();
	}

	protected void sendNoPermissionsMessage() {
		sendMessage("You lack permissions to use this command");
	}

	public boolean selfHasPermissions() {
		return BotUtility.hasPermissions(requiredPermissions, event.getTextChannel(),
				BotUtility.getSelfMember(event.getGuild()));
	}

	protected boolean memberHasPermissions(Member member) {
		// TODO: TEST THIS
		// If not owner or admin
		if (!member.isOwner() || !member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR)) {
			GuildSettings settings = GuildManager.getGuild(member.getGuild());
			if (isPermission0 || isPermission1 || isPermission2) {

				if (isPermission0) {
					Role permission0Role = settings.guild.getRoleById(settings.permission0);
					if (permission0Role != null && member.getRoles().contains(permission0Role)) {
						return true;
					}
				}

				if (isPermission1) {
					Role permission1Role = settings.guild.getRoleById(settings.permission1);
					if (permission1Role != null && member.getRoles().contains(permission1Role)) {
						return true;
					}
				}

				if (isPermission2) {
					Role permission2Role = settings.guild.getRoleById(settings.permission2);
					if (permission2Role != null && member.getRoles().contains(permission2Role)) {
						return true;
					}
				}
				// Return false if member does not have any perm roles
				return false;
			}
		}
		return true;
	}

	protected Member findParamsMember() {
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

		return member;
	}

	protected String permissionsToString() {
		String str = "";
		for (Permission perm : requiredPermissions) {
			str += perm.getName() + "; ";
		}
		return str;
	}

}
