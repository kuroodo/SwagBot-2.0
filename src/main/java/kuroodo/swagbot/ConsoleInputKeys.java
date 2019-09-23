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
