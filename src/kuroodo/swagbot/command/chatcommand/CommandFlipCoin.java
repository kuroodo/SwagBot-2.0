package kuroodo.swagbot.command.chatcommand;

import java.util.Random;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandFlipCoin extends ChatCommand {
	private Random rand;

	public CommandFlipCoin() {
		rand = new Random();
	}

	@Override
	public void executeCommand(String[] commandParams, MessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		rand.setSeed(System.nanoTime());
		int x = rand.nextInt(2);

		if (x == 0) {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on tails\n```");
		}else {
			sendMessage(event.getAuthor().getAsMention() + "```css\nflips a coin and it lands on heads\n```");
		}
	}

	@Override
	public String commandDescription() {
		return "Flip a coin between heads or tails\nUsage: !flipcoin";
	}
}
