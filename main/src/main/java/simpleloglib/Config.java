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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Data;
import simpleloglib.appenders.AppenderFactory;
import simpleloglib.filters.FilterFactory;
import simpleloglib.layouts.LayoutFactory;

@Data
public class Config {

    public static final Config SINGLETON = new Config();

    private ExecutorService threadPool;
    private int batchSize;
    private Collection<Appender> appenders;
    private Layout layout;
    private Map<String, Filter> filters;
    private Watcher watcher;

    private Config(){
        threadPool = Executors.newFixedThreadPool(10);
        batchSize = 100;
        appenders = List.of(AppenderFactory.getDefaultInstance("console"));
        appenders.forEach(appender -> appender.setFilter(FilterFactory.getDefaultFilter(Level.TRACE)));
        layout = LayoutFactory.getDefaultInstance(
            "[%level] %date - %logger - %thread : %msg %throwable");
        filters = Map.of(
            "console", FilterFactory.getDefaultFilter(Level.TRACE)
        );
        watcher = new Watcher();
        new Thread(watcher).start();
    }

}
