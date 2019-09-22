package kuroodo.swagbot;

public class ConsoleInputKeys {
	public static final String[] EXITKEYS = new String[] { "shutdown", "quit", "exit", "stop"};

	public static boolean isExitKey(String key) {
		for (String str : EXITKEYS) {
			if (str.equals(key)) {
				return true;
			}
		}
		return false;
	}
}
