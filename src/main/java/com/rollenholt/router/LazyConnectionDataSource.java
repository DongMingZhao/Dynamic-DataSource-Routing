package com.rollenholt.router;

import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.SmartDataSource;

import java.sql.Connection;

/**
 * Created by wenchao.ren
 * 2014/7/15.
 */
public class LazyConnectionDataSource  extends LazyConnectionDataSourceProxy implements SmartDataSource {

    @Override
    public boolean shouldClose(Connection con) {
        return false;
    }

    @Override
    protected Boolean defaultAutoCommit() {
        return null;
    }
}
