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
package simpleloglib;

import java.util.Collection;

/**
 * <p>
 * An {@code Appender} is a component that can append events to a output target.
 * Already in alpha version there are four implementations:
 * {@code ConsoleAppender}, {@code JDBCAppender}, {@code FileAppender} and {@code HttpAppender}.
 * <p>
 * In the future i will add more implementations to log to a file or a to a web
 * server.
 * 
 * @author lunalobos
 */
public interface Appender {
    /**
     * Get the name of the {@code Appender}.
     * @return
     */
    String name();

    /**
     * Append an event to the target.
     * @param event
     * @param layout
     */
    void append(Event event, Layout layout);

    /**
     * Append a collection of events to the target.
     * @param events
     * @param layout
     */
    void append(Collection<Event> events, Layout layout);

    /**
     * Set the filter for the target.
     * @param filter
     */
    void setFilter(Filter filter);
}
