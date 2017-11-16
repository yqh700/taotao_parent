package com.taotao.portal.web.interceptor;

import com.taotao.manager.domain.User;
import com.taotao.portal.util.CookieUtils;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 杨清华.
 * on 2017/11/16.
 */
public class OrderHandlerInterceptor implements HandlerInterceptor {

    @Value("${TT_TICKET}")
    private String TT_TICKET;

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //检查用户是否登录
        String ticket = CookieUtils.getCookieValue(httpServletRequest, TT_TICKET);

        if(StringUtils.isBlank(ticket)) {
            //用户未登录
            String url = httpServletRequest.getRequestURL().toString();
            httpServletResponse.sendRedirect("/page/login.html?redirectURL=" + url);
            return false;
        }

        User user = userService.queryUserByTicket(ticket);
        if(user == null) {
            //登录过期
            httpServletResponse.sendRedirect("/page/login.html");
            return false;
        }

        httpServletRequest.setAttribute("user", user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
