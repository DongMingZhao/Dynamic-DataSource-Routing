package com.rollenholt.aspect;

import com.rollenholt.annotation.Slave;
import com.rollenholt.router.DataSourceContextHolder;
import com.rollenholt.router.DataSourceRouter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by wenchao.ren
 * 2014/7/16.
 */
@Component
@Aspect
public class SlaveDataSourceAspect extends BaseAspect {

    private static final Logger logger = LoggerFactory.getLogger(SlaveDataSourceAspect.class);

    @Resource
    private DataSourceRouter dataSourceRouter;

    @Around("@annotation(com.rollenholt.annotation.Slave)")
    public Object methodSupport(ProceedingJoinPoint joinPoint) throws Throwable {
        Slave methodAnnotation = getMethodAnnotation(joinPoint, Slave.class);
        String dataSourceId = methodAnnotation.value();
        boolean failover = methodAnnotation.failover();
        return routerDataSource(joinPoint, dataSourceId, failover);
    }

    @Around("@within(com.rollenholt.annotation.Slave)")
    public Object classSupport(ProceedingJoinPoint joinPoint) throws Throwable {
        Slave annotation = joinPoint.getTarget().getClass().getAnnotation(Slave.class);
        String dataSourceId = annotation.value();
        boolean failover = annotation.failover();
        return routerDataSource(joinPoint, dataSourceId, failover);
    }

    private Object routerDataSource(ProceedingJoinPoint joinPoint, String dataSourceId, boolean failover) throws Throwable {
        String originDataSource = DataSourceContextHolder.getDataSource();
        DataSourceContextHolder.setDataSource(dataSourceId);
        try {
             return joinPoint.proceed();
        } catch(Exception e){
            logger.error(e.getMessage(), e);
            if(failover){
                String masterDataSourceId = dataSourceRouter.getMasterDataSourceId();
                DataSourceContextHolder.setDataSource(masterDataSourceId);
                return joinPoint.proceed();
            }else{
                throw e;
            }
        }finally {
            DataSourceContextHolder.setDataSource(originDataSource);
        }
    }
}
