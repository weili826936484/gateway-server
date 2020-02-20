package com.weili.api_gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.xml.internal.ws.client.ResponseContext;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.RequestContent;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.netflix.zuul.context.RequestContext.*;

@Component
public class LoginFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if("/gateway/api/order/saveOrder/v2".equals(request.getRequestURI())){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        Cookie[] cookie = request.getCookies();
        //System.out.println(cookie.toString());
        String token = request.getHeader("token");
        System.out.println(token);
        token = request.getParameter("token");
        if (StringUtils.isBlank(token)){
            System.out.println("登录失败");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

        }
        return null;
    }
}
