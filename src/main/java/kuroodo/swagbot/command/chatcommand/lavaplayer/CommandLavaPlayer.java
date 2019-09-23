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
		return "lavaplayer commands:\n" 
				+ commandPrefix + AudioKeys.KEY_PLAY + "<URL>\nplay an audio track or playlist (such as youtube playlist)\n\n" 
				+ commandPrefix + AudioKeys.KEY_PLAYRANDOM + "<URL>\nplay and shuffle a track playlist\n\n" 
				+ commandPrefix + AudioKeys.KEY_PAUSE + "\npause the current audio track\n\n" 
				+ commandPrefix + AudioKeys.KEY_RESUME + "\nresume playing the current audio track\n\n" 
				+ commandPrefix + AudioKeys.KEY_STOP + "\nStop playing all tracks; clears the entire queue\n\n" 
				+ commandPrefix + AudioKeys.KEY_SKIP + "\nSkip the current track and play the next track in the queue\n\n"
				+ commandPrefix + AudioKeys.KEY_TRACKINFO + "\nGet information about the current track\n\n";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + AudioKeys.KEY_PLAY + " https://www.youtube.com/watch?v=fQQxhyhdg-w\n" + commandPrefix
				+ AudioKeys.KEY_PLAYRANDOM + "https://www.youtube.com/playlist?list=PL95cvZS6DlOFNXlvKBrUHNtFMIYEQO9f9";
	}
}