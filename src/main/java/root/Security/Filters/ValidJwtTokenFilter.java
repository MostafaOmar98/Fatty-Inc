package root.Security.Filters;


import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import root.Security.JwtUtil;
import root.Security.UserCredentials;
import root.User.UserType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ValidJwtTokenFilter extends OncePerRequestFilter {


    public ValidJwtTokenFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        JwtUtil jwtUtil = new JwtUtil();
        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            jwtToken = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwtToken);
        }
        if (jwtToken == null || !jwtUtil.validateToken(jwtToken, username))
            throw new ServletException("Bad Token");
        filterChain.doFilter(request, response);
    }

    @Bean
    public FilterRegistrationBean<ValidJwtTokenFilter> ValidJwtTokenFilterBean() {
        FilterRegistrationBean<ValidJwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ValidJwtTokenFilter());
        registrationBean.addUrlPatterns("/admin/listAll");
        return registrationBean;
    }

}
