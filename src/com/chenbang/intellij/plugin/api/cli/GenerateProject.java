package com.chenbang.intellij.plugin.api.cli;

import com.chenbang.intellij.plugin.api.util.DirUtils;
import com.chenbang.intellij.plugin.api.util.TemplateUtils;
import freemarker.template.Configuration;

import java.util.HashMap;
import java.util.Map;

public class GenerateProject {
    public static final String DEFAULT_CHARSET = "UTF-8";


    public static void generateParentProject(String baseDirName, String basePackageName, String projectName, Configuration configuration, Map<Object, Object> context) throws Exception {
        TemplateUtils.generateFile(baseDirName + "/pom.xml", "template/mvn/pom.xml.ftl", configuration, context);
    }

    public static void generateCoreProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "core");

        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/core/pom.xml.ftl", configuration, context);

        String resourcesDir = dirMap.get("/src/main/resources");

        DirUtils.mkdir(resourcesDir + "/spring");
        TemplateUtils.generateFile(resourcesDir + "/spring/applicationContext-dao.xml", "template/mvn/core/spring/applicationContext-dao.xml.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/spring/applicationContext-bo.xml", "template/mvn/core/spring/applicationContext-bo.xml.ftl", configuration, context);

        DirUtils.mkdir(resourcesDir + "/mybatis/base");
        DirUtils.mkdir(resourcesDir + "/mybatis/extension");
        TemplateUtils.generateFile(resourcesDir + "/mybatis/mybatis-config.xml", "template/mvn/core/mybatis/mybatis-config.xml.ftl", configuration, context);

        String testResourcesDir = dirMap.get("/src/test/resources");

        DirUtils.mkdir(testResourcesDir + "/spring");
        TemplateUtils.generateFile(testResourcesDir + "/logback.xml", "template/mvn/core/test/logback.xml.ftl", configuration, context);
        TemplateUtils.generateFile(testResourcesDir + "/spring/applicationContext-test.xml", "template/mvn/core/test/applicationContext-test." + dbTypeName + ".xml.ftl", configuration, context);

        String testJavaDir = dirMap.get("/src/test/java");
        DirUtils.mkdir(testJavaDir + "/dao/impl");
        TemplateUtils.generateFile(testJavaDir + "/dao/impl/DefaultSampleDAOTest.java", "template/mvn/core/test/DefaultSampleDAOTest.java.ftl", configuration, context);
    }

    public static void generateApiProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "api");

        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/com.chenbang.intellij.plugin.api/pom.xml.ftl", configuration, context);

        String resourcesDir = dirMap.get("/src/main/resources");

        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/com.chenbang.intellij.plugin.api/freemarker.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/com.chenbang.intellij.plugin.api/config.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/com.chenbang.intellij.plugin.api/logback.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/com.chenbang.intellij.plugin.api/web.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/com.chenbang.intellij.plugin.api/dispatcher-servlet.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/com.chenbang.intellij.plugin.api/context." + dbTypeName + ".xml.ftl", configuration, context);
    }

    public static void generateMobileProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "mobile");

        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/mobile/pom.xml.ftl", configuration, context);

        String resourcesDir = dirMap.get("/src/main/resources");

        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/mobile/freemarker.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/mobile/config.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/mobile/logback.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/mobile/web.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/mobile/dispatcher-servlet.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/mobile/context." + dbTypeName + ".xml.ftl", configuration, context);
    }

    public static void generateWebProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "web");

        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/web/pom.xml.ftl", configuration, context);

        String resourcesDir = dirMap.get("/src/main/resources");

        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/web/freemarker.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/web/config.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/web/logback.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/view");
        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/web/web.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/web/dispatcher-servlet.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/403.jsp", "template/mvn/jsp/403.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/404.jsp", "template/mvn/jsp/404.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/500.jsp", "template/mvn/jsp/500.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/error.jsp", "template/mvn/jsp/error.jsp.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/web/context." + dbTypeName + ".xml.ftl", configuration, context);
    }

    public static void generateAdminProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "admin");

        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/admin/pom.xml.ftl", configuration, context);

        String resourcesDir = dirMap.get("/src/main/resources");

        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/admin/freemarker.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/admin/config.properties.ftl", configuration, context);
        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/admin/logback.xml.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/view");
        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/admin/web.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/admin/dispatcher-servlet.xml.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/403.jsp", "template/mvn/jsp/403.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/404.jsp", "template/mvn/jsp/404.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/500.jsp", "template/mvn/jsp/500.jsp.ftl", configuration, context);
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/error.jsp", "template/mvn/jsp/error.jsp.ftl", configuration, context);

        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/admin/context." + dbTypeName + ".xml.ftl", configuration, context);
    }

    private static Map<String, String> mkdir(String baseDirName, String basePackageName, String projectName, String subprojectSuffix) {
        Map<String, String> dirMap = new HashMap<String, String>();

        String subprojectBaseDirName = baseDirName + "/" + projectName + "-" + subprojectSuffix;
        DirUtils.mkdir(subprojectBaseDirName);
        dirMap.put("basedir", subprojectBaseDirName);

        String subpackageName = "core".equals(subprojectSuffix) ? "" : ("/" + subprojectSuffix);

        String dirName = subprojectBaseDirName + "/src/main/java/" + basePackageName.replace('.', '/') + "/" + projectName + subpackageName;
        DirUtils.mkdir(dirName);
        dirMap.put("/src/main/java", dirName);

        dirName = subprojectBaseDirName + "/src/main/resources";
        DirUtils.mkdir(dirName);
        dirMap.put("/src/main/resources", dirName);

        dirName = subprojectBaseDirName + "/src/test/java/" + basePackageName.replace('.', '/') + "/" + projectName + subpackageName;
        DirUtils.mkdir(dirName);
        dirMap.put("/src/test/java", dirName);

        dirName = subprojectBaseDirName + "/src/test/resources";
        DirUtils.mkdir(dirName);
        dirMap.put("/src/test/resources", dirName);

        return dirMap;
    }

    public static String getDbTypeName(int dbType) {
        String dbTypeName = null;
        switch (dbType) {
            case 1:
                dbTypeName = "MySQL";
                break;
            case 2:
                dbTypeName = "PostgreSQL";
                break;
            case 3:
                dbTypeName = "Oracle";
                break;
            case 4:
                dbTypeName = "SQL Server";
                break;
            default:
                break;
        }
        return dbTypeName;
    }
}
