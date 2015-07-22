package com.rollenholt.aspect;

import com.rollenholt.router.DataSourceContextHolder;
import com.rollenholt.router.DataSourceRouter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by wenchao.ren
 * 2014/7/15.
 */
@Aspect
@Component
public class MasterDataSourceAspect extends BaseAspect{

    @Resource
    private DataSourceRouter dataSourceRouter;

    @Around("@annotation(com.rollenholt.annotation.Master)")
    public Object methodSupport(ProceedingJoinPoint joinPoint) throws Throwable {

        String masterDataSourceId = dataSourceRouter.getMasterDataSourceId();
        return routerDataSource(joinPoint, masterDataSourceId);
    }

    @Around("@within(com.rollenholt.annotation.Master)")
    public Object classSupport(ProceedingJoinPoint joinPoint) throws Throwable{
        String masterDataSourceId = dataSourceRouter.getMasterDataSourceId();
        return routerDataSource(joinPoint, masterDataSourceId);
    }

    private Object routerDataSource(ProceedingJoinPoint joinPoint, String dataSourceId) throws Throwable {
        String originDataSource = DataSourceContextHolder.getDataSource();
        try {
            DataSourceContextHolder.setDataSource(dataSourceId);
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.setDataSource(originDataSource);
        }
    }

}
