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
 * Log levels. Levels are used to express the importance of an event.
 * 
 * @author lunalobos
 */
public enum Level {
    /**
     * Trace level for really detailed information.
     */
    TRACE,
    /**
     * Debug level for detailed information.
     */
    DEBUG,
    /**
     * Info level for general information.
     */
    INFO,
    /**
     * Warn level for warnings.
     */
    WARN,
    /**
     * Error level for not fatal errors.
     */
    ERROR,
    /**
     * Fatal level for fatal errors.
     */
    FATAL
}
