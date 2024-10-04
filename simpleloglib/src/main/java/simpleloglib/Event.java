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

import java.time.OffsetDateTime;

/**
 * An event is a log message with useful information about it, like timestamp,
 * logger name and level.
 * 
 * @author lunalobos
 */
public interface Event {
    /**
     * Get the throwable associated with this event.
     * @return the throwable
     */
    Throwable throwable();

    /**
     * Get the name of the thread associated with this event.
     * @return the name of the thread
     */
    String threadName();

    /**
     * Get the level associated with this event.
     * @return the level
     */
    Level level();

    /**
     * Get the message associated with this event.
     * @return the message
     */
    Message message();

    /**
     * Get the timestamp associated with this event.
     * @return the timestamp
     */
    OffsetDateTime timestamp();

    /**
     * Get the logger associated with this event.
     * @return the logger
     */
    Logger logger();
}
