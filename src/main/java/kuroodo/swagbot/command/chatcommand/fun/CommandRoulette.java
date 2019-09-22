package kuroodo.swagbot.command.chatcommand.fun;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.RoleManager;

public class CommandRoulette extends ChatCommand {
	private final int TOTALSHOTS = 4;
	private final long DEATHCOOLDOWN = 15;
	private final long TRANSITION_TIME = 3;
	private final String DEATHROLENAME = "RIP";

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
		// Delete the command message if permissions
		if (BotUtility.hasPermission(Permission.MESSAGE_MANAGE, BotUtility.getSelfMember(event.getGuild()))) {
			event.getMessage().delete().queue();
		}

		// Create death role if doesn't already exist
		createDeathRole();
		playRoulette();
	}

	private void playRoulette() {
		sendMessage(event.getMember().getAsMention() + " raises a revolver to their head");

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runResults();
			}
		}, TRANSITION_TIME * 1000);
	}

	private void runResults() {
		Random rand = new Random(System.nanoTime());
		int randomNumber = rand.nextInt(TOTALSHOTS);

		// Lose
		if (randomNumber == 1) {
			sendMessage(event.getAuthor().getAsMention()
					+ " pulls the trigger and BAM! Their brain splatters all over the place!");
			addDeathRoleToUser();
			startCoolDownTimer();
		} else { // Win
			sendMessage(event.getAuthor().getAsMention() + " pulls the trigger and survives to tell the tale!");
		}
	}

	private void startCoolDownTimer() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				removeDeathRoleFromUser();
			}
		}, DEATHCOOLDOWN * 1000);
	}

	private void createDeathRole() {
		Guild guild = event.getGuild();
		// If the role doesn't already exist
		if (!BotUtility.doesRoleExist(guild, DEATHROLENAME)) {
			// Create role
			guild.createRole().queue(new Consumer<Role>() {
				@Override
				public void accept(Role t) {
					Role role = t;
					RoleManager manager = role.getManager();

					// Set the name
					manager.setName(DEATHROLENAME).queue(new Consumer<Object>() {

						@Override
						public void accept(Object t) {
							// Revoke all permissions
							manager.revokePermissions(Permission.values()).queue();
						}
					});
				}
			});
		}
	}

	private void addDeathRoleToUser() {
		Guild guild = event.getGuild();
		if (BotUtility.doesRoleExist(guild, DEATHROLENAME)) {
			Role role = guild.getRolesByName(DEATHROLENAME, true).get(0);
			BotUtility.addRoleToMember(guild, role, event.getMember());
		}

	}

	private void removeDeathRoleFromUser() {
		Guild guild = event.getGuild();
		if (BotUtility.doesRoleExist(guild, DEATHROLENAME)) {
			Role role = guild.getRolesByName(DEATHROLENAME, true).get(0);
			BotUtility.removeRoleFromMember(guild, role, event.getMember());
		}
	}

	@Override
	public String commandDescription() {
		return "Test out your luck in a game of roulette";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_ROULETTE;
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_ROULETTE;
	}
}