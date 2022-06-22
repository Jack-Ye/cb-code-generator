<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>${iBasePackageName}</groupId>
    <artifactId>lib-parent</artifactId>
    <version>${iLibParentVersion}</version>
  </parent>
  <groupId>${basePackageName}.${javaProjectName}</groupId>
  <artifactId>${javaProjectName}-parent</artifactId>
  <packaging>pom</packaging>
  <version>1.0.0-SNAPSHOT</version>
  <name>${r"$"}{project.artifactId} v${r"$"}{project.version}</name>

  <modules>
    <module>${javaProjectName}-core</module>
    <module>${javaProjectName}-api</module>
    <module>${javaProjectName}-mobile</module>
    <module>${javaProjectName}-web</module>
    <module>${javaProjectName}-admin</module>
  </modules>

  <properties>
    <common.util.version>${iCommonVersion}</common.util.version>
    <common.dao.version>${iCommonVersion}</common.dao.version>
    <common.bo.version>${iCommonVersion}</common.bo.version>
    <common.pagination.version>${iCommonVersion}</common.pagination.version>
    <common.web.version>${iCommonVersion}</common.web.version>
    <common.test.version>${iCommonVersion}</common.test.version>
  </properties>

  <build>
    <resources>
      <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
      </resource>
    </resources>
    <filters>
      <filter>${r"$"}{user.home}/.filter/${javaProjectName}.properties</filter>
    </filters>
  </build>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>${r"$"}{project.groupId}</groupId>
        <artifactId>${javaProjectName}-core</artifactId>
        <version>${r"$"}{project.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-util</artifactId>
        <version>${r"$"}{common.util.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-dao</artifactId>
        <version>${r"$"}{common.dao.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-bo</artifactId>
        <version>${r"$"}{common.bo.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-pagination</artifactId>
        <version>${r"$"}{common.pagination.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-web</artifactId>
        <version>${r"$"}{common.web.version}</version>
      </dependency>
      <dependency>
        <groupId>${iBasePackageName}.common</groupId>
        <artifactId>common-test</artifactId>
        <version>${r"$"}{common.test.version}</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
</project>
