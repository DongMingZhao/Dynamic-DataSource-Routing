package com.rollenholt.router;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by wenchao.ren
 * 2014/7/14.
 */
public class DataSourceRouter extends AbstractRoutingDataSource {

    @Resource
    private ApplicationContext context;

    private String masterDataSourceId;

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

    public void setMasterDataSourceId(String masterDataSourceId) {
        this.masterDataSourceId = masterDataSourceId;
        DataSource dataSource = (DataSource)context.getBean(this.masterDataSourceId);
        setDefaultTargetDataSource(dataSource);
    }

    public String getMasterDataSourceId() {
        return masterDataSourceId;
    }
}
