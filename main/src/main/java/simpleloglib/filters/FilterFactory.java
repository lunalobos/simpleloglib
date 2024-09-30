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

package simpleloglib.filters;

import simpleloglib.Event;
import simpleloglib.Filter;
import simpleloglib.Level;

public class FilterFactory {
    public static Filter getDefaultFilter(Level thresholdLevel) {
        return new ThresholdFilter(thresholdLevel);
    }
}

class ThresholdFilter implements Filter {
    private Level level;

    public ThresholdFilter(Level level) {
        this.level = level;
    }

    @Override
    public boolean accept(Event event) {
        return event.level().ordinal() >= level.ordinal();
    }

}
