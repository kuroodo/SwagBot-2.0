package kuroodo.swagbot.command.chatcommand.fun;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import kuroodo.swagbot.command.CommandKeys;
import kuroodo.swagbot.command.chatcommand.ChatCommand;
import kuroodo.swagbot.json.JSONKeys;
import kuroodo.swagbot.utils.BotUtility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandMagicBall extends ChatCommand {

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

		try {
			Reader reader;
			reader = new BufferedReader(new FileReader(JSONKeys.MAGICBALL_FILE_NAME));
			JsonObject object = Json.parse(reader).asObject();

			// If only mentions the bot
			if (commandParams.length == 0) {
				sendPassiveResponse(object);
				// Else if can't find response
			} else if (object.get(commandParams[0].toLowerCase()) == null) {
				sendOtherResponse(object);
			} else { // Else if has parameter
				sendActiveResponse(object);
			}

			reader.close();
		} catch (ParseException | IOException e) {
			String errorMessage = "ERROR: Could not parse " + JSONKeys.MAGICBALL_FILE_NAME
					+ " ensure the exists or is in the correct json format";
			System.err.println(errorMessage);
			BotUtility.sendMessageToBotOwner(errorMessage);
		}

	}

	private void sendPassiveResponse(JsonObject object) {
		if (object.get(JSONKeys.MAGICBALL_PASSIVE_RESPONSE) == null)
			return;
		String response = "";
		JsonArray items = object.get(JSONKeys.MAGICBALL_PASSIVE_RESPONSE).asArray();
		int randomIndex = BotUtility.getRandomInt((items.size()));
		response = items.get(randomIndex).asString();

		if (response != null) {
			sendMessage(response);
		}
	}

	private void sendActiveResponse(JsonObject object) {
		String response = "";
		JsonArray items = object.get(commandParams[0].toLowerCase()).asArray();
		int randomIndex = BotUtility.getRandomInt((items.size()));
		response = items.get(randomIndex).asString();

		if (response != null) {
			sendMessage(response);
		}
	}

	private void sendOtherResponse(JsonObject object) {
		if (object.get(JSONKeys.MAGICBALL_OTHER_RESPONSE) == null)
			return;
		String response = "";
		JsonArray items = object.get(JSONKeys.MAGICBALL_OTHER_RESPONSE).asArray();
		int randomIndex = BotUtility.getRandomInt((items.size()));
		response = items.get(randomIndex).asString();

		if (response != null) {
			sendMessage(response);
		}
	}

	@Override
	public String commandDescription() {
		return "Ask the magic ball, or the bot, for advice or words of wisdom";
	}

	@Override
	public String commandFormat() {
		return commandPrefix + CommandKeys.COMMAND_MAGICBALL + " QUESTION\n"
				+ BotUtility.getSelfUser().getAsMention() + " QUESTION";
	}

	@Override
	public String commandUsageExample() {
		return commandPrefix + CommandKeys.COMMAND_MAGICBALL + " What is the meaning of life?\n"
				+ BotUtility.getSelfUser().getAsMention() + " Will my crush ask me out?";
	}
}