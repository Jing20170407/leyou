package com.example.filter;

import com.example.auth.entity.UserInfo;
import com.example.auth.utils.JwtUtils;
import com.example.auth.utils.RsaUtils;
import com.example.common.utils.CookieUtils;
import com.example.config.FilterPropertise;
import com.example.config.JwtPropertise;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

@EnableConfigurationProperties({FilterPropertise.class,JwtPropertise.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private FilterPropertise filterProp;

    @Autowired
    private JwtPropertise jwtProp;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 获取请求路径
        String requestURI = currentContext.getRequest().getRequestURI();
        // 匹配白名单
        for (String allowPath : filterProp.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取请求
        HttpServletRequest request = context.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, jwtProp.getCookieName());
        //检验token，获取userinfe，绑定到请求域
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(jwtProp.getPubKeyPath());
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);
            request.setAttribute("userInfo",userInfo);
        } catch (Exception e) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(403);
        }

        return null;
    }
}
