package com.rollenholt.router;

/**
 * Created by wenchao.ren
 * 2014/7/15.
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<String>();

    public static void setDataSource(String dataSource){
        dataSourceHolder.set(dataSource);
    }

    public static String getDataSource(){
        return dataSourceHolder.get();
    }

    public static void clearDataSource(){
        dataSourceHolder.remove();
    }
}
