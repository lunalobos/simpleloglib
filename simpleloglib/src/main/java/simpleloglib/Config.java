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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import simpleloglib.appenders.AppenderFactory;
import simpleloglib.appenders.Column;
import simpleloglib.appenders.ConnectionFactory;
import simpleloglib.appenders.JDBCAppenderConfig;
import simpleloglib.filters.FilterFactory;
import simpleloglib.layouts.LayoutFactory;

/**
 * Main cofiguration class. In case there is no config file, this class will
 * load a default configuration.
 * 
 * @author lunalobos
 */
@Data
public class Config {
	private static final String DEFAULT_LAYOUT = "[%level] %date - %logger - %thread : %msg %throwable";
	private static final ConfigTemplate DEFAULT_TEMPLATE = defaultTemplate();
	/**
	 * Singleton instance. Do not modify this instance if you do not know what you
	 * are doing.
	 */
	public static final Config SINGLETON = new Config();

	private static ConfigTemplate defaultTemplate() {

		var template = new ConfigTemplate();
		template.setThreads(10);
		template.setBatchSize(100);
		template.setLayout(DEFAULT_LAYOUT);
		var consoleAppenderTemplate = new AppenderTemplate();
		consoleAppenderTemplate.setType("console");
		consoleAppenderTemplate.setName("console");
		var filterTemplate = new FilterTemplate();
		filterTemplate.setLevel("TRACE");
		consoleAppenderTemplate.setFilter(filterTemplate);
		template.setAppenders(List.of(consoleAppenderTemplate));
		return template;
	}

	private ExecutorService threadPool;
	private int batchSize;
	private Collection<Appender> appenders;
	private Layout layout;

	private Config() {
		ConfigTemplate template = DEFAULT_TEMPLATE;
		try {
			template = new ObjectMapper().readValue(Config.class.getClassLoader().getResourceAsStream("simplelog.json"),
					ConfigTemplate.class);
		} catch (IOException | IllegalArgumentException e) {
			System.out.println("WARNING: simplelog.json not found, loading default config.");
		}
		loadFromTemplate(template);
		
	}

	public void loadFromTemplate(ConfigTemplate template) {
		threadPool = Executors.newFixedThreadPool(template.getThreads());
		batchSize = template.getBatchSize();
		appenders = template.getAppenders().stream().map(templ -> {
			Appender appender;
			switch (templ.getType()) {
				case "jdbc":
					appender = AppenderFactory.getJDBCAppender(templ.getName(), createJDBCAppenderConfig(templ));
					break;
				case "console":
					appender = AppenderFactory.getDefaultInstance(templ.getName());
					break;
				default:
					appender = AppenderFactory.getDefaultInstance(templ.getName());
			}
			if (templ.getFilter() != null) {
				appender.setFilter(FilterFactory.getDefaultFilter(Level.valueOf(templ.getFilter().getLevel())));
			} else {
				appender.setFilter(FilterFactory.getDefaultFilter(Level.TRACE));
			}
			return appender;
		}).toList();
		layout = LayoutFactory.getDefaultInstance(template.getLayout());
	}

	private JDBCAppenderConfig createJDBCAppenderConfig(AppenderTemplate templ) {
		Collection<Column> columns = new LinkedList<>();
		List<ColumnTemplate> columnTemplates = templ.getColumns();
		int index = 1;
		for (ColumnTemplate columnTemplate : columnTemplates) {
			columns.add(new Column(columnTemplate.getName(), index));
			index++;
		}
		ConnectionFactory factory = null;
		if (templ.getConnectionClassName() != null) {
			try {
				Constructor<?> constructor = Class.forName(templ.getConnectionClassName()).getConstructors()[0];
				factory = (ConnectionFactory) constructor.newInstance(new Object[] {});
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException | ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return JDBCAppenderConfig.builder().columns(columns).tableName(templ.getTableName()).connectionFactory(factory)
				.connectURI(templ.getConnectURL()).build();
	}

	public void shutdown() throws InterruptedException {
		getThreadPool().awaitTermination(10000, TimeUnit.MILLISECONDS);
		getThreadPool().shutdown();
	}

}

@Data
class ConfigTemplate {
	private int threads;
	private int batchSize;
	private List<AppenderTemplate> appenders;
	private String layout;
}

@Data
class AppenderTemplate {
	private String name;
	private String type;
	private String connectURL;
	private String connectionClassName;
	private String tableName;
	private List<ColumnTemplate> columns;
	private FilterTemplate filter;
}

@Data
class FilterTemplate {
	private String level;
}

@Data
class ColumnTemplate {
	private String name;
}
