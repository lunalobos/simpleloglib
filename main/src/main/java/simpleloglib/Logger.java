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

public interface Logger {
    void trace(String msg);
    void trace(Message msg);
    void trace(Throwable t);
    void debug(String msg);
    void debug(Message msg);
    void debug(Throwable t);
    void info(String msg);
    void info(Message msg);
    void info(Throwable t);
    void warn(String msg);
    void warn(Message msg);
    void warn(Throwable t);
    void error(String msg);
    void error(Message msg);
    void error(Throwable t);
    void fatal(String msg);
    void fatal(Message msg);
    void fatal(Throwable t);
    String name();
}
