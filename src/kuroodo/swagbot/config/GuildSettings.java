package kuroodo.swagbot.config;

import kuroodo.swagbot.SwagBot;
import net.dv8tion.jda.api.entities.Guild;

public class GuildSettings {
	public Guild guild;
	public long guildID = -1;
	public boolean enableWelcome = false;
	public long welcomeChannel = -1;
	public String welcomeMessage = "";
	public boolean enableWelcomeRole = false;
	public long welcomeRole = -1;
	public long logChannel = -1;
	public long muteRole = -1;
	public long muteChannel = -1;
	public long rolePermission0 = -1;
	public long rolePermission1 = -1;
	public long rolePermission2 = -1;

	public GuildSettings(Guild guild) {
		this.guild = guild;
		guildID = guild.getIdLong();
		// TODO: Call 3rd constructor or method that sets up all values from guild file
		// if exists
	}

	public GuildSettings(long guildID) {
		this(SwagBot.getJDA().getGuildById(guildID));
	}

	public GuildSettings(Guild guild, long guildID, boolean enableWelcome, long welcomeChannel,
			String welcomeMessage, boolean enableWelcomeRole, long welcomeRole, long logChannel, long muteRole, long muteChannel,
			long rolePermission0, long rolePermission1, long rolePermission2) {
		this.guild = guild;
		this.guildID = guildID;
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

// TODO: Create Reader class, and Read JSON files and values (just print to console)
// TODO: Create Writer class, and Write JSON files and values
// TODO: Store the values here
// TODO: Decide if this class or GuildManager class calls the writer for writing
// TODO: Have bot send a hello message and assign a role to a joining user
// TODO: Implement command system to allow value manipulation (writing)

}

// Channels and roles are their ID
// JSON Values listen as blank will be ignored/set as disabled