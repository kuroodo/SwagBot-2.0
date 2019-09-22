package kuroodo.swagbot.command.chatcommand.moderation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class CommandClearChat extends ChatCommand {

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_MANAGE);
		isPermission0 = true;
		isPermission1 = true;
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		if (!selfHasPermissions() || !memberHasPermissions(event.getMember())) {
			return;
		}
		// If empty parameters
		if (commandParams.length <= 1) {
			sendMessage(commandFormat());
			return;
		}

		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

		final int MAX_MESSAGES = 25;

		RestAction<List<Message>> restAction;
		Member member = findParamsMember();
		boolean containsMember = !event.getMessage().getMentionedMembers().isEmpty() || member != null ? true : false;
		int numMessages = getInputtedNumber(containsMember);

		if (numMessages > MAX_MESSAGES || numMessages == 0) {
			sendMessage("ERROR: Can only delete 1-25 messages");
			return;
		} else if (numMessages == -1) {
			sendMessage(commandFormat());
			return;
		}

		if (containsMember) {
			if (member == null) {
				sendMessage("ERROR: Please mention a valid user");
				return;
			}
			// Gether messages from the channel
			final int messagesToSeek = 50;
			restAction = getMessages(messagesToSeek);
		} else {
			// Gether messages from the channel
			restAction = getMessages(numMessages);
		}

		if (restAction == null) {
			sendMessage("Error: Something went wrong.");
			return;
		}

		// Retrieve messages
		restAction.queue(new Consumer<List<Message>>() {
			@Override
			public void accept(List<Message> t) {
				if (t.isEmpty()) {
					sendMessage("No messages found.");
					return;
				}
				int deletedMessageCount = t.size();

				if (containsMember) {
					List<Message> memberMessages = getMemberMessages(t, numMessages, member);
					deletedMessageCount = memberMessages.size();
					deleteMessages(memberMessages);
				} else {
					deleteMessages(t);
				}
				sendMessage(BotUtility.boldifyText("Deleting " + deletedMessageCount + " messages"));
			}
		});

	}

	private void deleteMessages(List<Message> messages) {
		event.getChannel().purgeMessages(messages);
	}

	// Get messages from a specific member
	private List<Message> getMemberMessages(List<Message> messages, int numMessage, Member member) {
		List<Message> messagesToDelete = new ArrayList<Message>();
		for (Message msg : messages) {
			if (msg.getMember() == member) {
				messagesToDelete.add(msg);

				if (messagesToDelete.size() == numMessage) {
					return messagesToDelete;
				}
			}
		}
		return messagesToDelete;
	}

	private RestAction<List<Message>> getMessages(int numMessages) {
		RestAction<List<Message>> restAction;
		MessageHistory msgHistory = new MessageHistory(event.getChannel());
		restAction = msgHistory.retrievePast(numMessages);
		return restAction;
	}

	private int getInputtedNumber(boolean containsMention) {
		int num = -1;
		try {
			if (!containsMention) {
				num = Integer.parseInt(commandParams[1]);
			} else if (commandParams.length == 3) {
				num = Integer.parseInt(commandParams[2]);
			}
		} catch (NumberFormatException E) {
		}

		return num;
	}

	@Override
	public String commandDescription() {
		return "Delete a MAXIMUM of 25 messages, either general messages or from a specific user";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + "clearchat <user> <amount> OR " + commandPrefix + "clearchat <armound>";
	}
}