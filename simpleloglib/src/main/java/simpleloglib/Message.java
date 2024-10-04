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

/**
 * A message is an object that represents a log message. The idea is log
 * information but also do not lose performance. Instead of formatting
 * a string you provide the information and format implementing this interface.
 * 
 * <p>The library implementation for event handling will call getFormattedMessage
 * in a separete thread, so the main thread will not block, and logging will be
 * as fast as possible.
 * 
 * @author lunalobos
 */
public interface Message {
    String getFormattedMessage();
}
