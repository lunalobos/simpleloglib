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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import simpleloglib.Appender;
import simpleloglib.Event;
import simpleloglib.Filter;
import simpleloglib.Layout;

class FileAppender implements Appender {

    private String name;
    private Path path;
    private Filter filter;

    public FileAppender(String name, String path) {
        this.name = name;
        this.path = Paths.get(path);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void append(Event event, Layout layout) {
        if (!filter.accept(event)) {
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            writer.write(layout.format(event));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void append(Collection<Event> events, Layout layout) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
            for (Event event : events) {
                if (filter.accept(event)) {
                    writer.write(layout.format(event));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "FileAppender(name = %s, path = %s, filter = %s)".formatted(name, path, filter);
    }

}
