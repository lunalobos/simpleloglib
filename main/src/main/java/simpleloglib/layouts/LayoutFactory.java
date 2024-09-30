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

package simpleloglib.layouts;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

import simpleloglib.Event;
import simpleloglib.Layout;
import simpleloglib.Level;
import simpleloglib.Logger;
import simpleloglib.Message;

public class LayoutFactory {
    private static final Pattern THROWABLE = Pattern.compile("%throwable");
    private static final Pattern MESSSAGE = Pattern.compile("%msg");
    private static final Pattern DATE = Pattern.compile("%date");
    private static final Pattern LEVEL = Pattern.compile("%level");
    private static final Pattern THREAD = Pattern.compile("%thread");
    private static final Pattern LOGGER = Pattern.compile("%logger");

    public static Layout getDefaultInstance(String layoutConfigString) {
        var format = new String(layoutConfigString.toCharArray());
        PriorityQueue<Getter<?>> getters = new PriorityQueue<>();
        var throwableMatcher = THROWABLE.matcher(format);
        if (throwableMatcher.find()) {
            int index = throwableMatcher.start();
            format = throwableMatcher.replaceFirst("%s");
            getters.add(new ThrowableGetter(index));
        }
        var messageMatcher = MESSSAGE.matcher(format);
        if (messageMatcher.find()) {
            int index = messageMatcher.start();
            format = messageMatcher.replaceFirst("%s");
            getters.add(new MessageGetter(index));
        }
        var dateMatcher = DATE.matcher(format);
        if (dateMatcher.find()) {
            int index = dateMatcher.start();
            format = dateMatcher.replaceFirst("%s");
            getters.add(new DateGetter(index));
        }
        var levelMatcher = LEVEL.matcher(format);
        if (levelMatcher.find()) {
            int index = levelMatcher.start();
            format = levelMatcher.replaceFirst("%s");
            getters.add(new LevelGetter(index));
        }
        var threadMatcher = THREAD.matcher(format);
        if (threadMatcher.find()) {
            int index = threadMatcher.start();
            format = threadMatcher.replaceFirst("%s");
            getters.add(new ThreadGetter(index));
        }
        var loggerMatcher = LOGGER.matcher(format);
        if (loggerMatcher.find()) {
            int index = loggerMatcher.start();
            format = loggerMatcher.replaceFirst("%s");
            getters.add(new LoggerGetter(index));
        }
        return new LayoutImpl(format, getters);
    }
}

interface Getter<T> extends Comparable<Getter<?>> {
    int priority();
    T getProperty(Event event);
}

class ThrowableGetter implements Getter<Throwable> {

    private int priority;

    public ThrowableGetter(int priority) {
        this.priority = priority;
    }
    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Throwable getProperty(Event event) {
        return event.throwable();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class MessageGetter implements Getter<Message> {

    private int priority;

    public MessageGetter(int priority) {
        this.priority = priority;
    }
    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Message getProperty(Event event) {
        return event.message();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class DateGetter implements Getter<OffsetDateTime> {

    private int priority;

    public DateGetter(int priority) {
        this.priority = priority;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public OffsetDateTime getProperty(Event event) {
        return event.timestamp();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class LevelGetter implements Getter<Level> {

    private int priority;

    public LevelGetter(int priority) {
        this.priority = priority;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Level getProperty(Event event) {
        return event.level();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class ThreadGetter implements Getter<String> {

    private int priority;

    public ThreadGetter(int priority) {
        this.priority = priority;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public String getProperty(Event event) {
        return event.threadName();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class LoggerGetter implements Getter<Logger> {

    private int priority;

    public LoggerGetter(int priority) {
        this.priority = priority;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Logger getProperty(Event event) {
        return event.logger();
    }

    @Override
    public int compareTo(Getter<?> arg0) {
        return Integer.compare(priority(), arg0.priority());
    }
}

class LayoutImpl implements Layout {

    private String format;
    private PriorityQueue<Getter<?>> getters = new PriorityQueue<>();
    
    public LayoutImpl(String format, PriorityQueue<Getter<?>> getters) {
        this.format = format;
        this.getters = getters;
    }

    private PriorityQueue<Getter<?>> copy(){
        return new PriorityQueue<>(getters);
    }

    @Override
    public String format(Event event) {
        List<Object> objects = new LinkedList<>();
        PriorityQueue<Getter<?>> getters = copy();
        while (!getters.isEmpty()) {
            Getter<?> getter = getters.poll();
            objects.add(getter.getProperty(event));
        }
        return format.formatted(objects.toArray());
    }
}