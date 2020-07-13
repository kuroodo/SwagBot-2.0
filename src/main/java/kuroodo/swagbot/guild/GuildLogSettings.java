package kuroodo.swagbot.guild;

import kuroodo.swagbot.SwagBot;
import net.dv8tion.jda.api.entities.Guild;

public class GuildLogSettings {
	public Guild guild;
	public long guildID = 0;
	public boolean nicknameLogging = false;
	public boolean memberRoleLogging = true;
	public boolean roleEditLogging = true;

	public GuildLogSettings(Guild guild) {
		this.guild = guild;
		guildID = guild.getIdLong();
	}

	public GuildLogSettings(long guildID) {
		this(SwagBot.getJDA().getGuildById(guildID));
	}

	public GuildLogSettings(Guild guild, long guildID, boolean nicknameLogging, boolean memberRoleLogging,
			boolean roleEditLogging) {
		this.guild = guild;
		this.guildID = guildID;
		this.nicknameLogging = nicknameLogging;
		this.memberRoleLogging = memberRoleLogging;
		this.roleEditLogging = roleEditLogging;
	}
}
