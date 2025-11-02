package org.example.fastcampus.configs;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Slf4j
public class DataSourceConfiguration {
    @Bean
    public DataSource masterSlaveDataSource() {
        log.info("MasterSlaveDataSourceConfiguration requested");
        return new LazyConnectionDataSourceProxy(createMasterSlaveRoutingDataSource());
    }

    private DataSource createMasterSlaveRoutingDataSource() {
        DataSource masterDataSource = createDataSource(masterHikariConfig());
        DataSource slaveDataSource = createDataSource(slaveHikariConfig());

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);

        MasterSlaveRoutingDataSource masterSlaveRoutingDataSource = new MasterSlaveRoutingDataSource();
        masterSlaveRoutingDataSource.setTargetDataSources(dataSourceMap);
        masterSlaveRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        masterSlaveRoutingDataSource.afterPropertiesSet();

        return masterSlaveRoutingDataSource;
    }

}
