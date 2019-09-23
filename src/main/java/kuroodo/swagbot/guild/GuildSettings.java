/*
 * Copyright 2019 Leandro Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
	public long musicchannel = 0;
	public boolean spartankick = false;
	public long permission0 = 0;
	public long permission1 = 0;
	public long permission2 = 0;

	public GuildSettings(Guild guild) {
		this.guild = guild;
		guildID = guild.getIdLong();
	}

	public GuildSettings(long guildID) {
		this(SwagBot.getJDA().getGuildById(guildID));
	}

	public GuildSettings(Guild guild, long guildID, String commandPrefix, boolean enableWelcome, long welcomeChannel,
			String welcomeMessage, boolean enableWelcomeRole, long welcomeRole, long logChannel, long muteRole,
			long muteChannel, long musicChannel, boolean spartankick, long permission0, long permission1,
			long permission2) {
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
		this.musicchannel = musicChannel;
		this.spartankick = spartankick;
		this.permission0 = permission0;
		this.permission1 = permission1;
		this.permission2 = permission2;
	}
}