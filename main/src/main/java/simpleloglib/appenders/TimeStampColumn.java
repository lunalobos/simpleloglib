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


public class TimeStampColumn implements Column {
    private int index;
    private String name;
    private BiConsumer<PreparedStatement,Event> setter;

    public TimeStampColumn(String name, int index) {
        this.name = name;
        this.index = index;
        this.setter = (preparedStatement, event) -> {
            try {
                preparedStatement.setTimestamp(index, new java.sql.Timestamp(event.timestamp().toInstant().toEpochMilli()));
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
    public BiConsumer<PreparedStatement,Event> setter() {
        return setter;
    }
    @Override
    public int index() {
        return index;
    }
}
