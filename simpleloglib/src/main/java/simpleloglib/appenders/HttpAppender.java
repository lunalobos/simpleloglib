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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;
import simpleloglib.Appender;
import simpleloglib.Event;
import simpleloglib.Filter;
import simpleloglib.Layout;

class HttpAppender implements Appender {
    private String name;
    private String url;
    private String authorization;
    private Filter filter;

    @Override
    public String name() {
        return name;
    }

    public HttpAppender(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public HttpAppender(String name, String url, String authorization) {
        this.name = name;
        this.url = url;
        this.authorization = authorization;
    }

    @Override
    public void append(Event event, Layout layout) {
        if (filter.accept(event)) {
            sendEventBean(EventBean.of(event, layout));
        }
    }

    @Override
    public void append(Collection<Event> events, Layout layout) {
        events.stream().filter(this.filter::accept).map(event -> EventBean.of(event, layout))
                .forEach(this::sendEventBean);
    }

    private void sendEventBean(EventBean eventBean) {
        try {
            var url = new URI(this.url).toURL();
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            if(authorization != null) {
                connection.setRequestProperty("Authorization", authorization);
            }
            var objectMappper = new ObjectMapper();
            var json = objectMappper.writeValueAsString(eventBean);
            var os = connection.getOutputStream();
            var input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            connection.disconnect();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

}

@Data
class EventBean {

    public static EventBean of(Event event, Layout layout) {
        return EventBean.builder()
                .throwable(event.throwable().toString())
                .threadName(event.threadName())
                .level(event.level().name())
                .formattedMessage(event.message().getFormattedMessage())
                .timestamp(event.timestamp())
                .loggerName(event.logger().name())
                .formattedEvent(layout.format(event))
                .build();
    }

    private String throwable;
    private String threadName;
    private String level;
    private String formattedMessage;
    private OffsetDateTime timestamp;
    private String loggerName;
    private String formattedEvent;

    public EventBean() {
    };

    @Builder
    public EventBean(String throwable, String threadName, String level, String formattedMessage,
            OffsetDateTime timestamp, String loggerName, String formattedEvent) {
        this.throwable = throwable;
        this.threadName = threadName;
        this.level = level;
        this.formattedMessage = formattedMessage;
        this.timestamp = timestamp;
        this.loggerName = loggerName;
        this.formattedEvent = formattedEvent;
    }

}