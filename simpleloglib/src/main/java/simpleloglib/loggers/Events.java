/* Copyright 2024 Miguel Angel Luna Lobos

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

package simpleloglib.loggers;

import java.util.LinkedList;
import java.util.Queue;

import lombok.Synchronized;
import simpleloglib.Config;
import simpleloglib.Event;

/**
 * @author lunalobos
 */
class Events {

	public static final Events SINGLETON = new Events();

	private Queue<Event> events;
	private Object $lock = new Object();

	private Events() {
		events = new LinkedList<>();
	}

	@Synchronized("$lock")
	public void handleEvent(Event event) {
		if (events.size() < Config.SINGLETON.getBatchSize()) {
			events.add(event);
		} else {
			pushEvents();
			events.add(event);
		}
	}

	@Synchronized("$lock")
	public void pushEvents() {
		var appenders = Config.SINGLETON.getAppenders();
		for (var appender : appenders) {
			appender.append(events, Config.SINGLETON.getLayout());
		}
		events.clear();

	}
}
