package ru.epam.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//    private static Logger LOG = Logger.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            LOG.warn(String.format("User [%s] attempted to access the protected URL [%s]!", authentication.getName(), request.getRequestURI()));
//        }

        response.sendRedirect(request.getContextPath() + "/access_denied");
    }
}
