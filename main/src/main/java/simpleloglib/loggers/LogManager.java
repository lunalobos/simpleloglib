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

package simpleloglib.loggers;

import simpleloglib.LoggingEventHandler;
import simpleloglib.Config;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import simpleloglib.Level;
import simpleloglib.Logger;
import simpleloglib.Message;
import simpleloglib.events.EventFactory;
import simpleloglib.messages.MsgFactory;

public class LogManager {

    private static final Map<String, Logger> loggers = new HashMap<>();

    public static Logger getLogger(Class<?> classObj) {
        if (loggers.containsKey(classObj.getName())) {
            return loggers.get(classObj.getName());
        } else {
            var logger = new LoggerImp(classObj);
            loggers.put(classObj.getName(), logger);
            return logger;
        }
    }
}

class LoggerImp implements Logger {

    private static final Throwable NULL_THROWABLE = new Throwable() {
        @Override
        public String toString() {
            return "";
        }
    };

    private String className;
    private ExecutorService threadPool;

    public LoggerImp(Class<?> classObj) {
        this.className = classObj.getName();
        threadPool = Config.SINGLETON.getThreadPool();
    }

    private void newEvent(Message msg, Throwable throwable, Level Level) {
        var threadName = Thread.currentThread().getName();
        var date = OffsetDateTime.now();
        threadPool.submit(() -> triggerEvent(msg, throwable, Level, threadName, date));
    }

    private void triggerEvent(Message msg, Throwable throwable, Level Level, String threadName, OffsetDateTime date) {
        LoggingEventHandler.handle(EventFactory.getDefaultInstance(throwable, threadName, Level, msg, date, this));
    }

    @Override
    public void trace(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.TRACE);
    }

    @Override
    public void trace(Message msg) {
        newEvent(msg, NULL_THROWABLE, Level.TRACE);
    }

    @Override
    public void trace(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.TRACE);
    }

    @Override
    public void debug(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.DEBUG);
    }

    @Override
    public void debug(Message msg) {
        newEvent(msg, NULL_THROWABLE, Level.DEBUG);
    }

    @Override
    public void debug(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.DEBUG);
    }

    @Override
    public void info(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.INFO);
    }

    @Override
    public void info(Message msg) {
        newEvent(msg, null, Level.INFO);
    }

    @Override
    public void info(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.INFO);
    }

    @Override
    public void warn(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.WARN);
    }

    @Override
    public void warn(Message msg) {
        newEvent(msg, NULL_THROWABLE, Level.WARN);
    }

    @Override
    public void warn(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.WARN);
    }

    @Override
    public void error(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.ERROR);
    }

    @Override
    public void error(Message msg) {
        newEvent(msg, NULL_THROWABLE, Level.ERROR);
    }

    @Override
    public void error(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.ERROR);
    }

    @Override
    public void fatal(String msg) {
        newEvent(MsgFactory.getDefaultInstance(msg), NULL_THROWABLE, Level.FATAL);
    }

    @Override
    public void fatal(Message msg) {
        newEvent(msg, NULL_THROWABLE, Level.FATAL);
    }

    @Override
    public void fatal(Throwable t) {
        newEvent(MsgFactory.getDefaultInstance(""), t, Level.FATAL);
    }

    @Override
    public String name() {
        return className;
    }

    @Override
    public String toString() {
        return className;
    }

}
