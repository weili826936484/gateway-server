package com.weili.api_gateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * 限流filter 使用google的guava
 */
@Component
public class OrderRateLimitFilter extends ZuulFilter {

    private static final RateLimiter limiter = RateLimiter.create(5);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!limiter.tryAcquire(5)){
            RequestContext requestContext = getCurrentContext();
            HttpServletRequest request = requestContext.getRequest();
            System.out.println("登录失败---超过qps");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            try {
                requestContext.getResponse().getWriter().write("too many requests");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
