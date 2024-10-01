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

package simpleloglib.messages;

import simpleloglib.Message;

public class MsgFactory {
    public static Message getDefaultInstance(String format, Object... args) {
        return new SimpleMessage(format, args);
    }
}

class SimpleMessage implements Message {
    private String format;
    private Object[] args;

    public SimpleMessage(String format, Object[] args) {
        this.format = format;
        this.args = args;
    }
    @Override
    public String getFormattedMessage() {
        return format.formatted(args);
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }
}
