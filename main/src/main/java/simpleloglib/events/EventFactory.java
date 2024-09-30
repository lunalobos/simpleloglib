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

package simpleloglib.events;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;
import simpleloglib.Event;
import simpleloglib.Level;
import simpleloglib.Logger;
import simpleloglib.Message;

public class EventFactory {
    public static Event getDefaultInstance(Throwable throwable,
            String threadName, Level level, Message message,
            OffsetDateTime timestamp, Logger logger) {
        return SimpleEvent.builder()
                .throwable(throwable)
                .threadName(threadName)
                .level(level)
                .message(message)
                .timestamp(timestamp)
                .logger(logger)
                .build();
    }
}


@Builder
@Data
class SimpleEvent implements Event {
    private Throwable throwable;
    private String threadName;
    private Level level;
    private Message message;
    private OffsetDateTime timestamp;
    private Logger logger;

    @Override
    public Throwable throwable() {
        return throwable;
    }

    @Override
    public String threadName() {
        return threadName;
    }

    @Override
    public Level level() {
        return level;
    }

    @Override
    public Message message() {
        return message;
    }

    @Override
    public OffsetDateTime timestamp() {
        return timestamp;
    }

    @Override
    public Logger logger() {
        return logger;
    }


}
