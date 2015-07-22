# Dynamic-DataSource-Routing

## 简介

`Dynamic-DataSource-Routing` 是一个用来实现读写分离的小东东，这个项目目前刚刚开工，敬请期待。

## DONE

- 实现了`@Master`注解，可以使用`@Master`来指定使用主库，主库是`com.rollenholt.router.DataSourceRouter`的`defaultTargetDataSource`属性指定的
- 如果`@Master`和`@Slave`都没指定，则使用主库的数据源
- 支持从库的failover， 如果设置failover为true，从库操作出问题后自动切换到主库，并重试一次（因此需要人工保证从库的操作都是可重复执行的），如果failover为false，
则从库操作失败之后直接抛出异常

## TODO

- 支持“一主多从”的结构
- 支持“一主多从”结构的负载均衡
- 期望能够实现“多主多从”的结构

## example


    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:mvc="http://www.springframework.org/schema/mvc"
           xmlns:context="http://www.springframework.org/schema/context"
           xmlns:aop="http://www.springframework.org/schema/aop" 
           xmlns:tx="http://www.springframework.org/schema/tx"
           xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
    		http://www.springframework.org/schema/context
    		http://www.springframework.org/schema/context/spring-context-4.0.xsd
    		http://www.springframework.org/schema/mvc
    		http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
    		http://www.springframework.org/schema/aop
    		http://www.springframework.org/schema/aop/spring-aop.xsd 
    		http://www.springframework.org/schema/tx 
    		http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.rollenholt"/>

    <context:annotation-config/>

    <mvc:annotation-driven/>

    <aop:aspectj-autoproxy />

    <context:property-placeholder location="classpath:jdbc.properties" ignore-unresolvable="true"/>

    <bean id="dataSourceRouter" class="com.rollenholt.router.DataSourceRouter">
        <property name="targetDataSources">
            <map key-type="java.lang.String">
                <entry key="dataSource1" value-ref="dataSource1"/>
                <entry key="dataSource2" value-ref="dataSource2"/>
            </map>
        </property>
        <property name="masterDataSourceId" value="dataSource1"/>
    </bean>

    <bean id="lazyConnectionDataSource" class="com.rollenholt.router.LazyConnectionDataSource">
        <property name="targetDataSource" ref="dataSourceRouter"/>
        <property name="defaultAutoCommit" value="true"/>
        <property name="defaultTransactionIsolation" value="-1"/>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="lazyConnectionDataSource"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>

    <tx:annotation-driven transaction-manager="txManager" proxy-target-class="true"/>

    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="lazyConnectionDataSource"/>
    </bean>


    <bean id="dataSource1" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${dataSource1.jdbc.driver}"/>
        <property name="url" value="${dataSource1.jdbc.url}"/>
        <property name="username" value="${dataSource1.jdbc.username}"/>
        <property name="password" value="${dataSource1.jdbc.password}"/>
    </bean>

    <bean id="dataSource2" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${dataSource2.jdbc.driver}"/>
        <property name="url" value="${dataSource2.jdbc.url}"/>
        <property name="username" value="${dataSource2.jdbc.username}"/>
        <property name="password" value="${dataSource2.jdbc.password}"/>
    </bean>

    </beans>


然后在代码中使用下面的方式：


    @Slave(value = "dataSource2")
    public List<Integer> getIds() {
        return getSqlSession().selectList("User.getIds");
    }

    @Master
    public List<String> getNames() {
        return getSqlSession().selectList("User.getNames");
    }
    