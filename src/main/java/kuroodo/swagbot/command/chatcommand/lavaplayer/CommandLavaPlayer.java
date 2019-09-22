package kuroodo.swagbot.command.chatcommand.lavaplayer;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.lavaplayer.AudioKeys;

public class CommandLavaPlayer extends ChatCommand {

	// Does nothing, see kuroodo.swagbot.lavaplayer.AudioPlayer

	@Override
	protected void setCommandPermissiosn() {

	}

	@Override
	public String commandDescription() {
		return "";
	}

	@Override
	public String commandFormat() {
		return "lavaplayer commands:\n\n" 
				+ commandPrefix + AudioKeys.KEY_PLAY + "<URL> , play an audio track or playlist (such as youtube playlist)\n\n" 
				+ commandPrefix + AudioKeys.KEY_PLAYRANDOM + "<URL> , play and shuffle a track playlist\n\n" 
				+ commandPrefix + AudioKeys.KEY_PAUSE + " , pause the current audio track\n\n" 
				+ commandPrefix + AudioKeys.KEY_RESUME + " , resume playing the current audio track\n\n" 
				+ commandPrefix + AudioKeys.KEY_STOP + " , Stop playing all tracks; clears the entire queue\n\n" 
				+ commandPrefix + AudioKeys.KEY_SKIP + " , Skip the current track and play the next track in the queue\n\n"
				+ commandPrefix + AudioKeys.KEY_TRACKINFO + " , Get information about the current track\n\n";
	}
}