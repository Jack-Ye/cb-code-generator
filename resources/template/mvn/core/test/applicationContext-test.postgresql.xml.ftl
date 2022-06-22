<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:jee="http://www.springframework.org/schema/jee"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
           http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-2.5.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd">

  <bean name="driverManagerDataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName">
      <value>org.postgresql.Driver</value>
    </property>
    <property name="url">
      <value>jdbc:postgresql://localhost:5432/${javaProjectName}</value>
    </property>
    <property name="username">
      <value>${javaProjectName}</value>
    </property>
    <property name="password">
      <value>123456</value>
    </property>
  </bean>

  <bean class="${iBasePackageName}.common.test.MockJndiContext">
    <property name="jndiObjects">
      <map>
        <entry key="java:comp/env/jdbc/${javaProjectName}" value-ref="driverManagerDataSource" />
      </map>
    </property>
  </bean>
</beans>
