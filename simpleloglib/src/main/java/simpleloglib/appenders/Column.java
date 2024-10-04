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

package simpleloglib.appenders;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

import com.github.f4b6a3.uuid.UuidCreator;

import simpleloglib.Event;

/**
 * This class represents a column in the database.
 * 
 * @author lunalobos
 */
public class Column {
	private String name;
	private BiConsumer<PreparedStatement, Event> setter;
	private int index;

	public Column(String name, int index) {
		this.name = name;
		this.index = index;

		switch (name) {
		case "event_id" -> setter = new EventIdSetter(index);
		case "logger" -> setter = new LoggerSetter(index);
		case "level" -> setter = new LevelSetter(index);
		case "thread" -> setter = new ThreadSetter(index);
		case "message" -> setter = new MessageSetter(index);
		case "throwable" -> setter = new ThrowableSetter(index);
		case "event_date" -> setter = new EventDateSetter(index);
		default -> throw new RuntimeException("Column not found");
		}

	}

	/**
	 * Gets the name of the column.
	 * @return the name
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets the setter of the column.
	 * @return the setter
	 */
	public BiConsumer<PreparedStatement, Event> setter() {
		return setter;
	}

	/**
	 * Gets the index of the column.
	 * @return the index
	 */
	public int index() {
		return index;
	}

	@Override
	public String toString() {
		return "Column(name = %s, index = %d)".formatted(name, index);
	}
}

class EventIdSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public EventIdSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(index, UuidCreator.getTimeOrdered().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

class LoggerSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public LoggerSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(this.index, u.logger().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

class LevelSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public LevelSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(this.index, u.logger().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

class ThreadSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public ThreadSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(this.index, u.threadName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

class MessageSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public MessageSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(this.index, sanitizeMessage(u.message().getFormattedMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String sanitizeMessage(String message) {
		if (message == null) {
			return "";
		}

		String sanitized = message.replaceAll("[<>]", "") // XSS
				.replaceAll("[\\x00-\\x1F\\x7F]", "");

		return sanitized.length() > 255 ? sanitized.substring(0, 1000) : sanitized;
	}
}

class ThrowableSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public ThrowableSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setString(this.index, u.throwable().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

class EventDateSetter implements BiConsumer<PreparedStatement, Event> {
	private int index;

	public EventDateSetter(int index) {
		this.index = index;
	}

	@Override
	public void accept(PreparedStatement t, Event u) {
		try {
			t.setTimestamp(this.index, new java.sql.Timestamp(u.timestamp().toInstant().toEpochMilli()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
