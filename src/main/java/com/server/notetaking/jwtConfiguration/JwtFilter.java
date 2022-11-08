package com.server.notetaking.jwtConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    @Value("${jwt.jwtSignKey:NoteTaking123#}")
    String secretKeyJwt;



    @Autowired
    ApplicationFilterConfig filter;

    public JwtFilter() {

    }



    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        } else {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
                return;
            }
            final String token = authHeader.substring(7);
            try {
                final Claims claims = Jwts.parser().setSigningKey("NoteTaking123#").parseClaimsJws(token).getBody();
                request.setAttribute("claims", claims);
            } catch (final Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
                return;
            }
            chain.doFilter(req, res);
        }
    }

}
