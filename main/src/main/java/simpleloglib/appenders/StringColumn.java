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

import simpleloglib.Event;

public class StringColumn implements Column {
    private String name;
    private BiConsumer<PreparedStatement, Event> setter;
    private int index;

    public StringColumn(String name, int index) {
        this.name = name;
        this.setter = (preparedStatement, event) -> {
            try {
                switch (name) {
                    case "logger":
                        preparedStatement.setString(index, event.logger().toString());
                        break;
                    case "level":
                        preparedStatement.setString(index, event.level().toString());
                        break;
                    case "thread":
                        preparedStatement.setString(index, event.threadName());
                        break;
                    case "message":
                        preparedStatement.setString(index, event.message().toString());
                        break;
                    case "throwable":
                        preparedStatement.setString(index, event.throwable().toString());
                        break;
                    default:
                        throw new RuntimeException("Column not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BiConsumer<PreparedStatement, Event> setter() {
        return setter;
    }

    @Override
    public int index() {
        return index;
    }
}
