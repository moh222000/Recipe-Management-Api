package example.recipeapi.security;

import example.recipeapi.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        jwtToken = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwtToken);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtUtil.validateToken(jwtToken)) {
            // Extract roles from token and convert to GrantedAuthority
            var roles = jwtUtil.extractRoles(jwtToken);
            var authorities = roles.stream()
                    .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
                    .toList();

            var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    chain.doFilter(request, response);
}

}
