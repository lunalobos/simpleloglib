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

public class AppenderFactory {

    public static Appender getDefaultInstance(String name) {
        return new ConsoleAppender(name);
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
}
