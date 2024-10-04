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
 * Layouts are used to format events before they are pushed to the output. In
 * some cases this format is useless becouse the target is a database, so the
 * event will be save in a table with many columns, one for each event field
 * the user wants to log persist. Layout are intended to build a string with
 * the event information, not just the message.
 * 
 * @author lunalobos
 */
public interface Layout {
    /**
     * Format the event.
     * @param event
     * @return the formatted string
     */
    String format(Event event);
}
