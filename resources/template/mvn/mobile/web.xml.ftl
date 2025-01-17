<?xml version="1.0" encoding="UTF-8"?>

<web-app version="2.5"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

  <display-name>${javaProjectName} mobile project</display-name>

  <filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
  </filter>

  <filter-mapping>
    <filter-name>encoding</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>

  <servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

  <session-config>
    <session-timeout>60</session-timeout>
  </session-config>

  <error-page>
    <error-code>403</error-code>
    <location>/WEB-INF/jsp/403.jsp</location>
  </error-page>

  <error-page>
    <error-code>404</error-code>
    <location>/WEB-INF/jsp/404.jsp</location>
  </error-page>

  <error-page>
    <error-code>500</error-code>
    <location>/WEB-INF/jsp/500.jsp</location>
  </error-page>

  <error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/WEB-INF/jsp/error.jsp</location>
  </error-page>
</web-app>
