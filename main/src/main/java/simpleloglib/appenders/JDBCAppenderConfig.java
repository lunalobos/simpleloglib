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

import java.util.Collection;

import lombok.Data;

@Data
public class JDBCAppenderConfig {
    private String tableName;
    private Collection<Column> columns;

    public String slq(){
        var sb = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        for(var column : columns) {
            sb.append(column.name()).append(", ");
        }
        sb.append(") VALUES (");
        columns.forEach(column -> sb.append("?, "));

        return sb.append(");").toString();
    }

}
