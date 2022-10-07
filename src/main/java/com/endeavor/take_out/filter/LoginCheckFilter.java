package com.endeavor.take_out.filter;

import com.alibaba.fastjson.JSON;
import com.endeavor.take_out.common.BaseContext;
import com.endeavor.take_out.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Sunrise
 * @create 2022-09-29 18:16
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) request;
        HttpServletResponse rp = (HttpServletResponse) response;

        String requestURI = rq.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/user/sendMsg",  //移动端发送短信
                "/user/login",    //移动端登录
                "/front/**",
                "/common/**"
        };
        boolean check = check(urls, requestURI);
        if (check) {
            chain.doFilter(rq, rp);
            return;
        }
        // 4.2判断移动端是否已经登录
        if (rq.getSession().getAttribute("user") != null) {
            // log.info("用户已登录");
            // 设置当前登录用户id
            Long userId = (Long) rq.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            // 放行
            chain.doFilter(request, response);
            return;
        }
        Long empId = (Long) rq.getSession().getAttribute("employee");
        if (empId != null) {
            BaseContext.setCurrentId(empId);
            chain.doFilter(rq, rp);
            return;
        }
        rp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

}
