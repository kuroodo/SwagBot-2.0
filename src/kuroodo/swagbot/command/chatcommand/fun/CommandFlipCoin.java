package kuroodo.swagbot.command.chatcommand.fun;

import java.util.Random;

import kuroodo.swagbot.command.chatcommand.ChatCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandFlipCoin extends ChatCommand {
	private Random rand;

	public CommandFlipCoin() {
		rand = new Random();
	}

	@Override
	protected void setCommandPermissiosn() {
		requiredPermissions.add(Permission.MESSAGE_WRITE);
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!selfHasPermissions()) {
			return;
		}

		int sides = 2;
		rand.setSeed(System.nanoTime());
		int x = rand.nextInt(sides);

		if (x == 0) {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on tails\n```");
		} else {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on heads\n```");
		}
	}

	@Override
	public String commandDescription() {
		return "Flip a coin between heads or tails";
	}

	@Override
	public String commandFormat() {
		return "!flipcoin";
	}
}
