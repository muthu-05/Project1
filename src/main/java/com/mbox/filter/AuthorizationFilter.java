package com.mbox.filter;

import com.mbox.exception.RequestException;
import com.mbox.service.UserService;
import com.mbox.util.JWTUtil;
import com.mbox.util.TokenUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Component
public class AuthorizationFilter implements Filter {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOGGER.info("########## Initiating CustomURLFilter filter ##########");
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    LOGGER.info("doFilter called");
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    try {
      String jwtToken = request.getHeader(TokenUtil.AUTHORIZATION_HEADER);
      LOGGER.info("doFilter called jwtToken{}", jwtToken);
      TokenUtil tokenUtil = getOject(servletRequest, TokenUtil.class);

      tokenUtil.getEmailAddress(jwtToken);
      filterChain.doFilter(request, response);
    } catch(Exception exception) {
      LOGGER.info("exception occured while validating the token, exception {}", exception.getMessage());
      throw new ServletException("invalid authorization header");
    }
  }

  private <T> T getOject(ServletRequest servletRequest, Class<T> classToBeUsed) {
    ServletContext servletContext = servletRequest.getServletContext();
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    return webApplicationContext.getBean(classToBeUsed);
  }

  @Override
  public void destroy() {

  }
}
