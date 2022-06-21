package com.chenbang.intellij.plugin.api.controller;//package com.calanger.generator.com.chenbang.intellij.plugin.api.controller;
//
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.nio.charset.Charset;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.ConnectionCallback;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.google.common.base.Splitter;
//import com.xushuzn.common.util.RandomUtils;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.constant.Constants;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.dto.ApiResult;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.jdbc.meta.table.ColumnInfo;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.jdbc.meta.table.TableInfo;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.util.DirUtils;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.util.JdbcUtils;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.util.TemplateUtils;
//import com.xushuzn.imber.com.chenbang.intellij.plugin.api.util.ZipUtils;
//import com.xushuzn.imber.bo.DbConnectionBO;
//import com.xushuzn.imber.model.DbConnection;
//
//import freemarker.cache.ClassTemplateLoader;
//import freemarker.template.Configuration;
//import freemarker.template.TemplateExceptionHandler;
//
//@Controller
//@RequestMapping("/generator")
//public class GeneratorController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorController.class);
//
//    private static final String DEFAULT_CHARSET = "UTF-8";
//
//    private static int BUFFER_SIZE = 32 * 1024;
//
//    @Autowired
//    private DbConnectionBO dbConnectionBO;
//
//    @RequestMapping(value = "/generate-project", method = RequestMethod.POST)
//    public void generateProject(
//            String basePackageName,
//            String projectName,
//            String subprojectName,
//            int dbType,
//            HttpServletResponse response) {
//        List<String> subprojectNameList = Splitter.on(",").splitToList(subprojectName);
//
//        String baseDirName = Constants.TEMP_DIR + "/" + RandomUtils.getRandomUUID(false) + "/" + projectName;
//        DirUtils.mkdir(baseDirName);
//
//        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
//        configuration.setDefaultEncoding(DEFAULT_CHARSET);
//        configuration.setTemplateLoader(new ClassTemplateLoader(GeneratorController.class, "/"));
//        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//
//        String dbTypeName = getDbTypeName(dbType).toLowerCase().replace(" ", "-");
//        Map<Object, Object> context = new HashMap<Object, Object>();
//        context.put("basePackageName", basePackageName);
//        context.put("javaProjectName", projectName);
//        context.put("dbType", dbType);
//        context.put("dbTypeName", dbTypeName);
//        context.put("iBasePackageName", "com.xushuzn");
//        context.put("iLibParentVersion", "1");
//        context.put("iCommonVersion", "1.0.0-SNAPSHOT");
//
//        try {
//            generateParentProject(baseDirName, basePackageName, projectName, configuration, context);
//            generateCoreProject(baseDirName, basePackageName, projectName, dbTypeName, configuration, context);
//            if (subprojectNameList.contains("com.chenbang.intellij.plugin.api")) {
//                generateApiProject(baseDirName, basePackageName, projectName, dbTypeName, configuration, context);
//            }
//            if (subprojectNameList.contains("mobile")) {
//                generateMobileProject(baseDirName, basePackageName, projectName, dbTypeName, configuration, context);
//            }
//            if (subprojectNameList.contains("web")) {
//                generateWebProject(baseDirName, basePackageName, projectName, dbTypeName, configuration, context);
//            }
//            if (subprojectNameList.contains("admin")) {
//                generateAdminProject(baseDirName, basePackageName, projectName, dbTypeName, configuration, context);
//            }
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
//            ZipUtils.zip(baseDirName, baos, true, Charset.forName("UTF-8"));
//
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment; filename=" + projectName + ".zip");
//            response.setContentLength(baos.size());
//            OutputStream out = null;
//            try {
//                out = response.getOutputStream();
//                out.write(baos.toByteArray());
//            } finally {
//                IOUtils.closeQuietly(out);
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    @RequestMapping(value = "/generate-model", method = RequestMethod.POST)
//    public void generateModel(
//            String basePackageName,
//            String projectName,
//            String connectionId,
//            String tableName,
//            String tablePrefix,
//            HttpServletResponse response) {
//        List<String> tableNameList = Splitter.on(",").splitToList(tableName);
//
//        String baseDirName = Constants.TEMP_DIR + "/" + RandomUtils.getRandomUUID(false) + "/" + projectName;
//        DirUtils.mkdir(baseDirName);
//
//        String coreBaseDirName = baseDirName + "/" + projectName + "-core";
//        DirUtils.mkdir(coreBaseDirName);
//
//        String sqlMapDirName = coreBaseDirName + "/src/main/resources/mybatis/base";
//        DirUtils.mkdir(sqlMapDirName);
//
//        String javaBaseDirName = getJavaBaseDirName(coreBaseDirName, basePackageName, projectName);
//        DirUtils.mkdir(javaBaseDirName);
//
//        String modelDirName = javaBaseDirName + "/model";
//        DirUtils.mkdir(modelDirName);
//
//        String daoDirName = javaBaseDirName + "/dao";
//        DirUtils.mkdir(daoDirName);
//
//        String daoImplDirName = javaBaseDirName + "/dao/impl";
//        DirUtils.mkdir(daoImplDirName);
//
//        String boDirName = javaBaseDirName + "/bo";
//        DirUtils.mkdir(boDirName);
//
//        String boImplDirName = javaBaseDirName + "/bo/impl";
//        DirUtils.mkdir(boImplDirName);
//
//        final DbConnection dbConnection = dbConnectionBO.get(connectionId);
//
//        String driverClassName;
//        String url;
//        if (dbConnection.getDbtype() == 1) { // MySQL
//            driverClassName = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + "/" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 2) { // PostgreSQL
//            driverClassName = "org.postgresql.Driver";
//            url = "jdbc:postgresql://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + "/" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 3) { // Oracle
//            driverClassName = "oracle.jdbc.driver.OracleDriver";
//            url = "jdbc:oracle:thin:@" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + ":" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 4) { // SQL Server
//            driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            url = "jdbc:sqlserver://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + ";DatabaseName=" + dbConnection.getDbname() + ";integratedSecurity=false";
//        } else {
//            throw new RuntimeException("dbType=" + dbConnection.getDbtype());
//        }
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(dbConnection.getDbuser());
//        dataSource.setPassword(dbConnection.getDbpasswd());
//
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        final List<String> finalTableNameList = tableNameList;
//        List<TableInfo> tableInfoList = jdbcTemplate.execute(new ConnectionCallback<List<TableInfo>>() {
//            @Override
//            public List<TableInfo> doInConnection(Connection connection) throws SQLException, DataAccessException {
//                String catalog = null;
//                String schema = null;
//                if (dbConnection.getDbtype() == 4) {
//                    catalog = dbConnection.getDbname();
//                } else {
//                    schema = dbConnection.getDbname();
//                }
//                return JdbcUtils.getTableInfoList(connection, catalog, schema, finalTableNameList);
//            }
//        });
//
//        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
//        configuration.setDefaultEncoding(DEFAULT_CHARSET);
//        configuration.setTemplateLoader(new ClassTemplateLoader(GeneratorController.class, "/"));
//        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
//
//        for (TableInfo tableInfo : tableInfoList) {
//            String xTableName = tableInfo.getTableName().toLowerCase();
//
//            String sqlMapPathname = sqlMapDirName + "/mapper-" + xTableName + ".xml";
//            String modelPathname = modelDirName + "/" + getModelClassSimpleName(xTableName, tablePrefix) + ".java";
//            String daoPathname = daoDirName + "/" + getModelClassSimpleName(xTableName, tablePrefix) + "DAO.java";
//            String daoImplPathname = daoImplDirName + "/MyBatis" + getModelClassSimpleName(xTableName, tablePrefix) + "DAO.java";
//            String boPathname = boDirName + "/" + getModelClassSimpleName(xTableName, tablePrefix) + "BO.java";
//            String boImplPathname = boImplDirName + "/Default" + getModelClassSimpleName(xTableName, tablePrefix) + "BO.java";
//
//            Map<Object, Object> context = getContext(basePackageName, projectName, tablePrefix, tableInfo);
//
//            try {
//                TemplateUtils.generateFile(sqlMapPathname, "template/mybatis/mapper-" + getDbTypeName(dbConnection.getDbtype()).toLowerCase().replace(" ", "-") + ".ftl", configuration, context);
//                TemplateUtils.generateFile(modelPathname, "template/model/model.ftl", configuration, context);
//                TemplateUtils.generateFile(daoPathname, "template/dao/dao.ftl", configuration, context);
//                TemplateUtils.generateFile(daoImplPathname, "template/mybatis/dao/dao-impl.ftl", configuration, context);
//                TemplateUtils.generateFile(boPathname, "template/bo/bo.ftl", configuration, context);
//                TemplateUtils.generateFile(boImplPathname, "template/bo/bo-impl.ftl", configuration, context);
//            } catch (Exception e) {
//                LOGGER.error(e.getMessage(), e);
//                throw new RuntimeException(e);
//            }
//        }
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
//        ZipUtils.zip(baseDirName, baos, true, Charset.forName("UTF-8"));
//
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=" + projectName + ".zip");
//        response.setContentLength(baos.size());
//        OutputStream out = null;
//        try {
//            out = response.getOutputStream();
//            out.write(baos.toByteArray());
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        } finally {
//            IOUtils.closeQuietly(out);
//        }
//    }
//
//    @RequestMapping(value = "/table-list", method = RequestMethod.GET)
//    @ResponseBody
//    public ApiResult<Map<String, Object>> listTable(String connectionId) {
//        final DbConnection dbConnection = dbConnectionBO.get(connectionId);
//
//        String driverClassName;
//        String url;
//        if (dbConnection.getDbtype() == 1) { // MySQL
//            driverClassName = "com.mysql.jdbc.Driver";
//            url = "jdbc:mysql://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + "/" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 2) { // PostgreSQL
//            driverClassName = "org.postgresql.Driver";
//            url = "jdbc:postgresql://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + "/" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 3) { // Oracle
//            driverClassName = "oracle.jdbc.driver.OracleDriver";
//            url = "jdbc:oracle:thin:@" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + ":" + dbConnection.getDbname();
//        } else if (dbConnection.getDbtype() == 4) { // SQL Server
//            driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            url = "jdbc:sqlserver://" + dbConnection.getDbip() + ":" + dbConnection.getDbport() + ";DatabaseName=" + dbConnection.getDbname() + ";integratedSecurity=false";
//        } else {
//            throw new RuntimeException("dbType=" + dbConnection.getDbtype());
//        }
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(dbConnection.getDbuser());
//        dataSource.setPassword(dbConnection.getDbpasswd());
//
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        List<TableInfo> tableInfoList = jdbcTemplate.execute(new ConnectionCallback<List<TableInfo>>() {
//            @Override
//            public List<TableInfo> doInConnection(Connection connection) throws SQLException, DataAccessException {
//                String catalog = null;
//                String schema = null;
//                if (dbConnection.getDbtype() == 4) {
//                    catalog = dbConnection.getDbname();
//                } else {
//                    schema = dbConnection.getDbname();
//                }
//                return JdbcUtils.getTableInfoList(connection, catalog, schema, "%");
//            }
//        });
//
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(tableInfoList.size());
//        for (TableInfo tableInfo : tableInfoList) {
//            Map<String, Object> e = new LinkedHashMap<String, Object>();
//            e.put("tableName", tableInfo.getTableName());
//            e.put("tableType", tableInfo.getTableType());
//            e.put("comment", tableInfo.getComment());
//
//            list.add(e);
//        }
//
//        Map<String, Object> data = new LinkedHashMap<String, Object>();
//        data.put("list", list);
//
//        return new ApiResult<Map<String, Object>>(0, "success", data);
//    }
//
//    private static void generateParentProject(String baseDirName, String basePackageName, String projectName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        TemplateUtils.generateFile(baseDirName + "/pom.xml", "template/mvn/pom.xml.ftl", configuration, context);
//    }
//
//    private static void generateCoreProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "core");
//
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/core/pom.xml.ftl", configuration, context);
//
//        String resourcesDir = dirMap.get("/src/main/resources");
//
//        DirUtils.mkdir(resourcesDir + "/spring");
//        TemplateUtils.generateFile(resourcesDir + "/spring/applicationContext-dao.xml", "template/mvn/core/spring/applicationContext-dao.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/spring/applicationContext-bo.xml", "template/mvn/core/spring/applicationContext-bo.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(resourcesDir + "/mybatis/base");
//        DirUtils.mkdir(resourcesDir + "/mybatis/extension");
//        TemplateUtils.generateFile(resourcesDir + "/mybatis/mybatis-config.xml", "template/mvn/core/mybatis/mybatis-config.xml.ftl", configuration, context);
//
//        String testResourcesDir = dirMap.get("/src/test/resources");
//
//        DirUtils.mkdir(testResourcesDir + "/spring");
//        TemplateUtils.generateFile(testResourcesDir + "/logback.xml", "template/mvn/core/test/logback.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(testResourcesDir + "/spring/applicationContext-test.xml", "template/mvn/core/test/applicationContext-test." + dbTypeName + ".xml.ftl", configuration, context);
//
//        String testJavaDir = dirMap.get("/src/test/java");
//        DirUtils.mkdir(testJavaDir + "/dao/impl");
//        TemplateUtils.generateFile(testJavaDir + "/dao/impl/DefaultSampleDAOTest.java", "template/mvn/core/test/DefaultSampleDAOTest.java.ftl", configuration, context);
//    }
//
//    private static void generateApiProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "com.chenbang.intellij.plugin.api");
//
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/com.chenbang.intellij.plugin.api/pom.xml.ftl", configuration, context);
//
//        String resourcesDir = dirMap.get("/src/main/resources");
//
//        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/com.chenbang.intellij.plugin.api/freemarker.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/com.chenbang.intellij.plugin.api/config.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/com.chenbang.intellij.plugin.api/logback.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/com.chenbang.intellij.plugin.api/web.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/com.chenbang.intellij.plugin.api/dispatcher-servlet.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/com.chenbang.intellij.plugin.api/context." + dbTypeName + ".xml.ftl", configuration, context);
//    }
//
//    private static void generateMobileProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "mobile");
//
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/mobile/pom.xml.ftl", configuration, context);
//
//        String resourcesDir = dirMap.get("/src/main/resources");
//
//        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/mobile/freemarker.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/mobile/config.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/mobile/logback.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/mobile/web.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/mobile/dispatcher-servlet.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/mobile/context." + dbTypeName + ".xml.ftl", configuration, context);
//    }
//
//    private static void generateWebProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "web");
//
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/web/pom.xml.ftl", configuration, context);
//
//        String resourcesDir = dirMap.get("/src/main/resources");
//
//        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/web/freemarker.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/web/config.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/web/logback.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/view");
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/web/web.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/web/dispatcher-servlet.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/403.jsp", "template/mvn/jsp/403.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/404.jsp", "template/mvn/jsp/404.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/500.jsp", "template/mvn/jsp/500.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/error.jsp", "template/mvn/jsp/error.jsp.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/web/context." + dbTypeName + ".xml.ftl", configuration, context);
//    }
//
//    private static void generateAdminProject(String baseDirName, String basePackageName, String projectName, String dbTypeName, Configuration configuration, Map<Object, Object> context) throws Exception {
//        Map<String, String> dirMap = mkdir(baseDirName, basePackageName, projectName, "admin");
//
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/pom.xml", "template/mvn/admin/pom.xml.ftl", configuration, context);
//
//        String resourcesDir = dirMap.get("/src/main/resources");
//
//        TemplateUtils.generateFile(resourcesDir + "/freemarker.properties", "template/mvn/admin/freemarker.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/config.properties", "template/mvn/admin/config.properties.ftl", configuration, context);
//        TemplateUtils.generateFile(resourcesDir + "/logback.xml", "template/mvn/admin/logback.xml.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/view");
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/web.xml", "template/mvn/admin/web.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml", "template/mvn/admin/dispatcher-servlet.xml.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/403.jsp", "template/mvn/jsp/403.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/404.jsp", "template/mvn/jsp/404.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/500.jsp", "template/mvn/jsp/500.jsp.ftl", configuration, context);
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/main/webapp/WEB-INF/jsp/error.jsp", "template/mvn/jsp/error.jsp.ftl", configuration, context);
//
//        DirUtils.mkdir(dirMap.get("basedir") + "/src/test/META-INF");
//        TemplateUtils.generateFile(dirMap.get("basedir") + "/src/test/META-INF/context.xml", "template/mvn/admin/context." + dbTypeName + ".xml.ftl", configuration, context);
//    }
//
//    private static Map<String, String> mkdir(String baseDirName, String basePackageName, String projectName, String subprojectSuffix) {
//        Map<String, String> dirMap = new HashMap<String, String>();
//
//        String subprojectBaseDirName = baseDirName + "/" + projectName + "-" + subprojectSuffix;
//        DirUtils.mkdir(subprojectBaseDirName);
//        dirMap.put("basedir", subprojectBaseDirName);
//
//        String subpackageName = "core".equals(subprojectSuffix) ? "" : ("/" + subprojectSuffix);
//
//        String dirName = subprojectBaseDirName + "/src/main/java/" + basePackageName.replace('.', '/') + "/" + projectName + subpackageName;
//        DirUtils.mkdir(dirName);
//        dirMap.put("/src/main/java", dirName);
//
//        dirName = subprojectBaseDirName + "/src/main/resources";
//        DirUtils.mkdir(dirName);
//        dirMap.put("/src/main/resources", dirName);
//
//        dirName = subprojectBaseDirName + "/src/test/java/" + basePackageName.replace('.', '/') + "/" + projectName + subpackageName;
//        DirUtils.mkdir(dirName);
//        dirMap.put("/src/test/java", dirName);
//
//        dirName = subprojectBaseDirName + "/src/test/resources";
//        DirUtils.mkdir(dirName);
//        dirMap.put("/src/test/resources", dirName);
//
//        return dirMap;
//    }
//
//    private static String getJavaBaseDirName(String baseDirName, String basePackageName, String projectName) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(baseDirName);
//        sb.append("/src/main/java/");
//        sb.append(basePackageName.replace('.', '/'));
//        sb.append("/");
//        sb.append(projectName);
//        return sb.toString();
//    }
//
//    private static String getModelClassSimpleName(String tableName, String tablePrefix) {
//        return getClassSimpleName(tableName, tablePrefix);
//    }
//
//    private static String getClassSimpleName(String tableName, String tablePrefix) {
//        if (tablePrefix == null) {
//            tablePrefix = "";
//        }
//
//        String name = tableName.toLowerCase();
//        String prefix = tablePrefix.toLowerCase();
//
//        if (!name.startsWith(prefix)) {
//            throw new RuntimeException("表名前缀不正确");
//        }
//
//        name = name.substring(prefix.length());
//        String[] ss = name.split("_");
//        StringBuilder sb = new StringBuilder();
//        for (String s : ss) {
//            sb.append(StringUtils.capitalize(s));
//        }
//        return sb.toString();
//    }
//
//    private static Map<Object, Object> getContext(String basePackageName, String projectName, String tablePrefix, TableInfo tableInfo) {
//        String tableName = tableInfo.getTableName().toLowerCase();
//
//        Map<Object, Object> context = new HashMap<Object, Object>();
//        context.put("basePackageName", basePackageName);
//        context.put("javaProjectName", projectName);
//        context.put("iBasePackageName", "com.xushuzn");
//        context.put("iLibParentVersion", "1");
//        context.put("iCommonVersion", "1.0.0-SNAPSHOT");
//        context.put("tableName", tableName);
//        context.put("modelClassSimpleName", getModelClassSimpleName(tableName, tablePrefix));
//
//        List<String> propertyNameList = new ArrayList<String>();
//        List<String> propertyTypeList = new ArrayList<String>();
//        List<String> columnTypeList = new ArrayList<String>();
//        String pkType = "";
//        List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();
//        for (ColumnInfo columnInfo : columnInfoList) {
//            String columnName = columnInfo.getColumnName();
//            String columnTypeName = columnInfo.getColumnTypeName();
//
//            propertyNameList.add(getPropertyName(columnInfo.getColumnName()));
//
//            if ("char".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("CHAR");
//            } else if ("varchar".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("VARCHAR");
//            } else if ("tinytext".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("VARCHAR");
//            }  else if ("text".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("VARCHAR");
//            }  else if ("mediumtext".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("VARCHAR");
//            }  else if ("longtext".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("VARCHAR");
//            }  else if ("decimal".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("DECIMAL");
//            } else if ("date".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("DATE");
//            } else if ("datetime".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("TIMESTAMP");
//            } else if ("bit".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("INTEGER");
//            } else if ("tinyint".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("INTEGER");
//            } else if ("smallint".equalsIgnoreCase(columnTypeName)
//                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("INTEGER");
//            } else if ("mediumint".equalsIgnoreCase(columnTypeName)
//                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("INTEGER");
//            } else if ("int".equalsIgnoreCase(columnTypeName)
//                    || "int identity".equalsIgnoreCase(columnTypeName)
//                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("INTEGER");
//            } else if ("bigint".equalsIgnoreCase(columnTypeName)
//                    || "bigint identity".equalsIgnoreCase(columnTypeName)
//                    || "bigint unsigned".equalsIgnoreCase(columnTypeName)
//                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
//                columnTypeList.add("BIGINT");
//            }  else {
//                columnTypeList.add("UnknownType" + "[" + columnTypeName + "]");
//            }
//
//            if ("bit".equalsIgnoreCase(columnTypeName)
//                    || "tinyint".equalsIgnoreCase(columnTypeName)
//                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)
//                    || "smallint".equalsIgnoreCase(columnTypeName)
//                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)
//                    || "mediumint".equalsIgnoreCase(columnTypeName)
//                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)
//                    || "int".equalsIgnoreCase(columnTypeName)
//                    || "int identity".equalsIgnoreCase(columnTypeName)) {
//                propertyTypeList.add("java.lang.Integer");
//
//                if ("id".equalsIgnoreCase(columnName)) {
//                    pkType = "java.lang.Integer";
//                }
//            } else if ("bigint".equalsIgnoreCase(columnTypeName)
//                    || "bigint identity".equalsIgnoreCase(columnTypeName)
//                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
//                propertyTypeList.add("java.lang.Long");
//
//                if ("id".equalsIgnoreCase(columnName)) {
//                    pkType = "java.lang.Long";
//                }
//            } else if ("char".equalsIgnoreCase(columnTypeName)
//                    || "varchar".equalsIgnoreCase(columnTypeName)
//                    || "tinytext".equalsIgnoreCase(columnTypeName)
//                    || "text".equalsIgnoreCase(columnTypeName)
//                    || "mediumtext".equalsIgnoreCase(columnTypeName)
//                    || "longtext".equalsIgnoreCase(columnTypeName)) {
//                propertyTypeList.add("java.lang.String");
//
//                if ("id".equalsIgnoreCase(columnName)) {
//                    pkType = "java.lang.String";
//                }
//            } else if ("date".equalsIgnoreCase(columnTypeName)
//                    || "datetime".equalsIgnoreCase(columnTypeName)) {
//                propertyTypeList.add("java.util.Date");
//
//                if ("id".equalsIgnoreCase(columnName)) {
//                    pkType = "java.util.Date";
//                }
//            } else if ("decimal".equalsIgnoreCase(columnTypeName)) {
//                propertyTypeList.add("java.math.BigDecimal");
//
//                if ("id".equalsIgnoreCase(columnName)) {
//                    pkType = "java.math.BigDecimal";
//                }
//            } else {
//                propertyTypeList.add("UnknownType[" + columnTypeName + "]");
//            }
//        }
//        context.put("propertyNameList", propertyNameList);
//        context.put("propertyTypeList", propertyTypeList);
//        context.put("columnTypeList", columnTypeList);
//        context.put("columnInfoList", columnInfoList);
//        context.put("pkType", pkType);
//
//        return context;
//    }
//
//    private static String getPropertyName(String columnName) {
//        String name = columnName.toLowerCase();
//
//        String[] ss = name.split("_");
//        StringBuilder sb = new StringBuilder();
//        for (String s : ss) {
//            sb.append(StringUtils.capitalize(s));
//        }
//        return StringUtils.uncapitalize(sb.toString());
//    }
//
//    private static String getDbTypeName(int dbType) {
//        String dbTypeName = null;
//        switch (dbType) {
//        case 1:
//            dbTypeName = "MySQL";
//            break;
//        case 2:
//            dbTypeName = "PostgreSQL";
//            break;
//        case 3:
//            dbTypeName = "Oracle";
//            break;
//        case 4:
//            dbTypeName = "SQL Server";
//            break;
//        default:
//            break;
//        }
//        return dbTypeName;
//    }
//}
