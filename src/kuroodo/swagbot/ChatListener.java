package kuroodo.swagbot;

import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Listens for chat specific events like commands
public class ChatListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		super.onMessageReceived(event);

		String msg = event.getMessage().getContentRaw();
		if (event.isFromGuild()) {
			if (msg.equals("!ping")) {
				event.getChannel().sendMessage("PONG!").queue();
			}
		} else if (event.isFromType(ChannelType.PRIVATE)) {
			event.getChannel().sendMessage("PING PANG PONG").queue();
		}

	}

	// Just a test. This functionality should stay here or go to server listener
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		super.onGuildMemberJoin(event);
		GuildSettings guild = GuildManager.getGuild(event.getGuild().getIdLong());
		if (guild.enableWelcome) {
			GuildManager.getTextChannel(guild.guildID, guild.welcomeChannel).sendMessage(guild.welcomeMessage).queue();
			if (guild.enableWelcomeRole) {
				event.getGuild().addRoleToMember(event.getMember(),
						GuildManager.getRole(guild.guildID, guild.welcomeRole)).queue();;
			}
		}
	}

}
