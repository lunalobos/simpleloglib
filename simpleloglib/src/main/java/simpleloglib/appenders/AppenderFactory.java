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

import java.util.Collection;

import lombok.NonNull;
import simpleloglib.Appender;
import simpleloglib.Event;
import simpleloglib.Filter;
import simpleloglib.Layout;

/**
 * Appender factory for implementations provided by this library.
 * 
 * @author lunalobos
 */
public class AppenderFactory {


    /**
     * Retrieves an {@link Appender} that writes to the console
     * @param name
     * @return an instance of {@link ConsoleAppender}
     */
    public static Appender getDefaultInstance(String name) {
        return new ConsoleAppender(name);
    }

    /**
     * Retrieves an {@link Appender} that persists log events in a relational database
     * @param name
     * @param config
     * @return an implementation of {@link Appender}
     */
    public static Appender getJDBCAppender(String name, JDBCAppenderConfig config) {
        return new JDBCAppender(name, config);
    }

    /**
     * Retrieves an {@link Appender} that writes to a file
     * @param name
     * @param path
     * @return an implementation of {@link Appender}
     */
    public static Appender getFileAppender(String name, String path) {
        return new FileAppender(name, path);
    }

    /**
     * Experimental
     * <p>Retrieves an {@link Appender} that writes to a web server using HTTP protocol
     * with authentication. Warning: this appender is not tested
     * @param name
     * @param url
     * @param authorization
     * @return an implementation of {@link Appender}
     */
    public static Appender getHttpAppender(String name, String url, String authorization) {
        return new HttpAppender(name, url, authorization);
    }

}

class ConsoleAppender implements Appender {
    private String name;
    private Filter filter;

    public ConsoleAppender(String name) {
        this.name = name;
        filter = null;
    }
    @Override
    public void append(@NonNull Event event, @NonNull Layout layout) {
        if(filter.accept(event))
            System.out.println(layout.format(event));
    }

    @Override
    public void append(@NonNull Collection<Event> events, @NonNull Layout layout) {
        for(Event event : events) {
            append(event, layout);
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "ConsoleAppender(name = %s, filter = %s)".formatted(name, filter);
    }
}
