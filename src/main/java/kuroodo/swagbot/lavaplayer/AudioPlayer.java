package kuroodo.swagbot.lavaplayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import kuroodo.swagbot.guild.GuildManager;
import kuroodo.swagbot.guild.GuildSettings;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AudioPlayer extends ListenerAdapter {

	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	public AudioPlayer() {
		this.musicManagers = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		GuildSettings settings = GuildManager.getGuild(event.getGuild());
		String prefix = settings.commandPrefix;

		String[] params = event.getMessage().getContentRaw().split(" ", 2);
		String command = params[0];

		// play
		if (command.equals(prefix + AudioKeys.KEY_PLAY) && params.length == 2) {
			setupPlay(settings, event.getMember(), event.getChannel(), params, false);

			// PLAYRANDOM
		} else if (command.equals(prefix + AudioKeys.KEY_PLAYRANDOM) && params.length == 2) {
			setupPlay(settings, event.getMember(), event.getChannel(), params, true);

			// pause
		} else if (command.equals(prefix + AudioKeys.KEY_PAUSE)) {
			GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
			musicManager.player.setPaused(true);

			// RESUME
		} else if (command.equals(prefix + AudioKeys.KEY_RESUME) || command.equals(prefix + AudioKeys.KEY_PLAY)) {
			GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
			musicManager.player.setPaused(false);

			// STOP
		} else if (command.equals(prefix + AudioKeys.KEY_STOP)) {
			GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
			musicManager.player.stopTrack();
			musicManager.scheduler.clearQueue();
			// SKIP
		} else if (command.equals(prefix + AudioKeys.KEY_SKIP)) {
			skipTrack(event.getChannel());

			// TRACK INFO
		} else if (command.equals(prefix + AudioKeys.KEY_TRACKINFO)) {
			String message = "";
			String trackTime = "";

			GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
			message = message + musicManager.player.getPlayingTrack().getInfo().title;

			long milliseconds = Long.valueOf(musicManager.player.getPlayingTrack().getInfo().length);

			trackTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
					TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

			message = message + " (" + trackTime + ")";

			BotUtility.sendGuildMessage(settings.guild, event.getChannel(), message);
		}

		super.onGuildMessageReceived(event);
	}

	private void setupPlay(GuildSettings settings, Member member, TextChannel textchannel, String[] params,
			boolean isRandomized) {
		VoiceChannel musicChannel = settings.guild.getVoiceChannelById(settings.musicchannel);

		if (musicChannel == null) {
			// Try getting the command issuers channel
			musicChannel = member.getVoiceState().getChannel();
		}

		if (musicChannel != null) {
			loadAndPlay(textchannel, musicChannel, params[1], isRandomized);
		}
	}

	private void loadAndPlay(final TextChannel channel, final VoiceChannel voicechannel, final String trackUrl,
			boolean isRandomized) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				BotUtility.sendGuildMessage(channel.getGuild(), channel, "Adding to queue " + track.getInfo().title);

				play(channel.getGuild(), voicechannel, musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				channel.sendMessage("Adding playlist " + playlist.getName()).queue();

				play(channel.getGuild(), voicechannel, musicManager, playlist, isRandomized);
			}

			@Override
			public void noMatches() {
				BotUtility.sendGuildMessage(channel.getGuild(), channel, "Nothing found by " + trackUrl);
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				BotUtility.sendGuildMessage(channel.getGuild(), channel, "Could not play: " + exception.getMessage());
			}
		});
	}

	private void play(Guild guild, VoiceChannel userVoiceChannel, GuildMusicManager musicManager, AudioTrack track) {
		guild.getAudioManager().openAudioConnection(userVoiceChannel);

		musicManager.scheduler.queue(track);
	}

	private void play(Guild guild, VoiceChannel userVoiceChannel, GuildMusicManager musicManager,
			AudioPlaylist playlist, boolean isRandomized) {
		guild.getAudioManager().openAudioConnection(userVoiceChannel);

		if (isRandomized) {
			long seed = System.nanoTime();
			Collections.shuffle(playlist.getTracks(), new Random(seed));
		}

		for (AudioTrack track : playlist.getTracks()) {
			musicManager.scheduler.queue(track);
		}

	}

	private void skipTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		if (musicManager.scheduler.isEmpty()) {
			BotUtility.sendGuildMessage(channel.getGuild(), channel, "No more tracks in queue");
			return;
		}
		musicManager.scheduler.nextTrack();

		BotUtility.sendGuildMessage(channel.getGuild(), channel, "Skipped to next track.");
	}
}