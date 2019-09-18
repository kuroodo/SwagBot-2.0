package kuroodo.swagbot.guild;

import kuroodo.swagbot.SwagBot;
import net.dv8tion.jda.api.entities.Guild;

public class GuildSettings {
	public Guild guild;
	public long guildID = 0;
	public String commandPrefix = "!";
	public boolean enableWelcome = false;
	public long welcomeChannel = 0;
	public String welcomeMessage = "";
	public boolean enableWelcomeRole = false;
	public long welcomeRole = 0;
	public long logChannel = 0;
	public long muteRole = 0;
	public long muteChannel = 0;
	public long rolePermission0 = 0;
	public long rolePermission1 = 0;
	public long rolePermission2 = 0;

	public GuildSettings(Guild guild) {
		this.guild = guild;
		guildID = guild.getIdLong();
		// TODO: Call 3rd constructor or method that sets up all values from guild file
		// if exists
	}

	public GuildSettings(long guildID) {
		this(SwagBot.getJDA().getGuildById(guildID));
	}

	public GuildSettings(Guild guild, long guildID, String commandPrefix, boolean enableWelcome, long welcomeChannel,
			String welcomeMessage, boolean enableWelcomeRole, long welcomeRole, long logChannel, long muteRole, long muteChannel,
			long rolePermission0, long rolePermission1, long rolePermission2) {
		this.guild = guild;
		this.guildID = guildID;
		this.commandPrefix = commandPrefix;
		this.enableWelcome = enableWelcome;
		this.welcomeChannel = welcomeChannel;
		this.welcomeMessage = welcomeMessage;
		this.enableWelcomeRole = enableWelcomeRole;
		this.welcomeRole = welcomeRole;
		this.logChannel = logChannel;
		this.muteRole = muteRole;
		this.muteChannel = muteChannel;
		this.rolePermission0 = rolePermission0;
		this.rolePermission1 = rolePermission1;
		this.rolePermission2 = rolePermission2;
	}
}