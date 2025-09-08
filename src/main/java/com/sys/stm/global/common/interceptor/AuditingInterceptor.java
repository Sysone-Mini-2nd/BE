package com.sys.stm.global.common.interceptor;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import java.util.Properties;


@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class AuditingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        
        // 디버깅 로그 추가
        log.info("=== AuditingInterceptor 실행 ===");
        log.info("SQL Command Type: {}", sqlCommandType);
        log.info("Parameter type: {}", parameter.getClass().getSimpleName());
        log.info("Is BaseEntity instance: {}", parameter instanceof BaseEntity);

        if (parameter instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) parameter;
            log.info("BaseEntity fields - createdAt: {}, updatedAt: {}, isDeleted: {}", 
                    entity.getCreatedAt(), entity.getUpdatedAt(), entity.getIsDeleted());

            switch (sqlCommandType) {
                case INSERT:
                    entity.onCreate();
                    log.info("Auditing: INSERT - Entity created at {}", entity.getCreatedAt());
                    break;
                case UPDATE:
                    entity.onUpdate();
                    log.info("Auditing: UPDATE - Entity updated at {}", entity.getUpdatedAt());
                    break;
                case DELETE:
                    entity.markAsDeleted();
                    log.info("Auditing: DELETE - Entity marked as deleted");
                    break;
            }
        } else {
            log.warn("Parameter is not BaseEntity instance: {}", parameter.getClass());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 필요시 설정
    }
}