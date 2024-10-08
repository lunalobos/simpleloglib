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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import simpleloglib.Appender;
import simpleloglib.Event;
import simpleloglib.Filter;
import simpleloglib.Layout;

/**
 * @author lunalobos
 */
class JDBCAppender implements Appender {

    private ConnectionFactory connectionFactory;
    private String name;
    private JDBCAppenderConfig config;
    private Filter filter;
    private String sql;

    public JDBCAppender(String name, JDBCAppenderConfig config) {
        this.name = name;
        this.connectionFactory = config.getConnectionFactory() == null
                ? new DefaultConnectionFactory(config.getConnectURI())
                : config.getConnectionFactory();
        this.config = config;
        sql = config.slq();
    }

    @Override
    public String name() {
        return name;
    }

    private void persistInDB(Event event, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            config.getColumns().stream().forEach(c -> {
                c.setter().accept(preparedStatement, event);
            });
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(Event event, Layout layout) {
        try (Connection connection = connectionFactory.getConnection()) {
            if (filter.accept(event))
                persistInDB(event, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(Collection<Event> events, Layout layout) {
        try (Connection connection = connectionFactory.getConnection()) {
            for (Event event : events) {
                if (filter.accept(event))
                    persistInDB(event, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "JDBCAppender(name = %s, connectionFactory = %s, config = %s, filter = %s, sql = %s)"
            .formatted(name, connectionFactory, config, filter, sql);
    }

}
