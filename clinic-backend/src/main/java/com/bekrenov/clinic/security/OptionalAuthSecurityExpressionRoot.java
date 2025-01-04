package com.bekrenov.clinic.security;

import com.bekrenov.clinic.util.CurrentAuthUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

@Log4j2
public class OptionalAuthSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private MethodSecurityExpressionOperations delegate;

    public OptionalAuthSecurityExpressionRoot(MethodSecurityExpressionOperations delegate) {
        super(delegate.getAuthentication());
        this.delegate = delegate;
    }

    public boolean requireRoleIfAuthenticated(String role) {
        log.info("Using 'requireRoleIfAuthenticated()' security expression to authorize method invocation (with authentication {})", getAuthentication());
        if(CurrentAuthUtil.isAuthenticated()) {
            return CurrentAuthUtil.hasAuthority(Role.valueOf(role));
        }
        return true;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.delegate.setFilterObject(filterObject);
    }

    @Override
    public Object getFilterObject() {
        return this.delegate.getFilterObject();
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.delegate.setReturnObject(returnObject);
    }

    @Override
    public Object getReturnObject() {
        return this.delegate.getReturnObject();
    }

    @Override
    public Object getThis() {
        return this.delegate.getThis();
    }
}