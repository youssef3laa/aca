package com.asset.appwork.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by karim on 10/19/20.
 * @classdesc Tenant management class used for handling tenant on all over the application
 * it identify tenant id from attribute X-TenantID used in request header and give it a valid a header data source.
 */
public class TenantManagement {
    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static String getTenantName() {
        return tenant.get();
    }

    /**
     * @classdesc Spring boot provide AbstractRoutingDataSource for determining a data source at runtime
     */
    public static class TenantAwareRoutingSource extends AbstractRoutingDataSource {

        @Override
        protected Object determineCurrentLookupKey() {
            String tenantName =  TenantManagement.getTenantName();
            if (tenantName == null) tenantName = "TenantOne";
            return tenantName;
        }
    }

    /**
     * @classdesc TenantNameInterceptor class used  to extract the tenant identifier from an incoming request.
     * HandlerInterceptorAdapter is a class provided by Spring MVC to  intercept an incoming request and extract data from it.
     */
    public static class TenantNameInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            // curl -H "X-TenantID: TenantOne" -H "Content-Type: application/json" http://localhost:8080/customers
            String tenantName = request.getHeader("X-TenantID");
            TenantManagement.setTenantName(tenantName);

            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            TenantManagement.setTenantName(null);
        }
    }


    public class CustomPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

        @Override
        public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment context) {
            return convertToSnakeCase(identifier);
        }

        @Override
        public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment context) {
            return convertToSnakeCase(identifier);
        }

        private Identifier convertToSnakeCase(final Identifier identifier) {
            final String regex = "([a-z])([A-Z])";
            final String replacement = "$1_$2";
            final String newName = identifier.getText()
                    .replaceAll(regex, replacement)
                    .toLowerCase();
            return Identifier.toIdentifier(newName);
        }

    }

}
