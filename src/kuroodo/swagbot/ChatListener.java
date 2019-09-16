package kuroodo.swagbot;

import net.dv8tion.jda.api.entities.ChannelType;
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
		}else if(event.isFromType(ChannelType.PRIVATE)) {
			event.getChannel().sendMessage("PING PANG PONG").queue();
		}

	}

}
