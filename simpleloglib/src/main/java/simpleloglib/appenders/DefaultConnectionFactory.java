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
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;

/**
 * Default implementation of {@link simpleloglib.appenders.ConnectionFactory}.
 * I take this from commons-dbcp2 repo examples and adapted to my needs.
 * 
 * @author lunalobos
 */
public class DefaultConnectionFactory implements simpleloglib.appenders.ConnectionFactory {

    private DataSource dataSource;

    public DefaultConnectionFactory(String connectURL){
        dataSource = setupDataSource(connectURL);
    }

    public DataSource setupDataSource(String connectURI) {
        
        ConnectionFactory connectionFactory =
            new DriverManagerConnectionFactory(connectURI, null);

        
        PoolableConnectionFactory poolableConnectionFactory =
            new PoolableConnectionFactory(connectionFactory, null);

        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);
        
        poolableConnectionFactory.setPool(connectionPool);

        PoolingDataSource<PoolableConnection> dataSource =
                new PoolingDataSource<>(connectionPool);

        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public String toString(){
        return "DefaultConnectionFactory(dataSource = %s)".formatted(dataSource);
    }
}
