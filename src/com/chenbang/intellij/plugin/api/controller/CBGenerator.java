package com.chenbang.intellij.plugin.api.controller;

import com.chenbang.intellij.plugin.api.cli.GenerateModel;
import com.chenbang.intellij.plugin.api.cli.GenerateProject;
import com.chenbang.intellij.plugin.api.jdbc.meta.table.TableInfo;
import com.chenbang.intellij.plugin.api.util.DirUtils;
import com.chenbang.intellij.plugin.api.util.TemplateUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CBGenerator {

    public static void main(String[] args) {
        generateProject(new Form());
    }

    public static class Form {
        String projectName = "demo";
        String baseDirName = "./";
        String basePackageName = "com.chenbang";
        String subprojectNames = "api,admin,mobile,web";
        String tableNames = "";
        String tablePrefix = "";
        List<TableInfo> tableInfoList;

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public void setBaseDirName(String baseDirName) {
            this.baseDirName = baseDirName.endsWith("/") ? baseDirName : baseDirName + "/";
        }

        public void setBasePackageName(String basePackageName) {
            this.basePackageName = basePackageName;
        }

        public void setSubprojectNames(String subprojectNames) {
            this.subprojectNames = subprojectNames;
        }

        public void setTableNames(String tableNames) {
            this.tableNames = tableNames;
        }

        public void setTablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
        }

        public void setTableInfoList(List<TableInfo> tableInfoList) {
            this.tableInfoList = tableInfoList;
        }
    }

    public static void generateProject(Form form) {

        List<String> subprojectNameList = Arrays.asList(form.subprojectNames.split(","));
        String baseDirName = form.baseDirName + form.projectName;
        DirUtils.mkdir(baseDirName);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setDefaultEncoding(GenerateProject.DEFAULT_CHARSET);
        configuration.setTemplateLoader(new ClassTemplateLoader(GenerateProject.class, "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        String dbTypeName = GenerateProject.getDbTypeName(1).toLowerCase().replace(" ", "-");
        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put("basePackageName", form.basePackageName);
        context.put("javaProjectName", form.projectName);
        context.put("dbType", 1);
        context.put("dbTypeName", dbTypeName);
        context.put("iBasePackageName", form.basePackageName);
        context.put("iLibParentVersion", "4");
        context.put("iCommonVersion", "3.0.0-SNAPSHOT");

        try {
            GenerateProject.generateParentProject(baseDirName, form.basePackageName, form.projectName, configuration, context);
            GenerateProject.generateCoreProject(baseDirName, form.basePackageName, form.projectName, dbTypeName, configuration, context);
            if (subprojectNameList.contains("api")) {
                GenerateProject.generateApiProject(baseDirName, form.basePackageName, form.projectName, dbTypeName, configuration, context);
            }
            if (subprojectNameList.contains("mobile")) {
                GenerateProject.generateMobileProject(baseDirName, form.basePackageName, form.projectName, dbTypeName, configuration, context);
            }
            if (subprojectNameList.contains("web")) {
                GenerateProject.generateWebProject(baseDirName, form.basePackageName, form.projectName, dbTypeName, configuration, context);
            }
            if (subprojectNameList.contains("admin")) {
                GenerateProject.generateAdminProject(baseDirName, form.basePackageName, form.projectName, dbTypeName, configuration, context);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateDAO(Form form) {

        List<String> tableNameList = Arrays.asList(form.tableNames.split(","));

        String coreBaseDirName = form.baseDirName + form.projectName + "/" + form.projectName + "-core";

        DirUtils.mkdir(coreBaseDirName);

        String sqlMapDirName = coreBaseDirName + "/src/main/resources/mybatis/base";
        DirUtils.mkdir(sqlMapDirName);

        String javaBaseDirName = GenerateModel.getJavaBaseDirName(coreBaseDirName, form.basePackageName, form.projectName);
        DirUtils.mkdir(javaBaseDirName);

        String modelDirName = javaBaseDirName + "/model";
        DirUtils.mkdir(modelDirName);

        String daoDirName = javaBaseDirName + "/dao";
        DirUtils.mkdir(daoDirName);

        String daoImplDirName = javaBaseDirName + "/dao/impl";
        DirUtils.mkdir(daoImplDirName);

        String boDirName = javaBaseDirName + "/bo";
        DirUtils.mkdir(boDirName);

        String boImplDirName = javaBaseDirName + "/bo/impl";
        DirUtils.mkdir(boImplDirName);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setDefaultEncoding(GenerateModel.DEFAULT_CHARSET);
        configuration.setTemplateLoader(new ClassTemplateLoader(GenerateModel.class, "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        for (TableInfo tableInfo : form.tableInfoList) {
            String xTableName = tableInfo.getTableName().toLowerCase();
            String sqlMapPathname = sqlMapDirName + "/mapper-" + xTableName + ".xml";
            String modelPathname = modelDirName + "/" + GenerateModel.getModelClassSimpleName(xTableName, form.tablePrefix) + ".java";
            String daoPathname = daoDirName + "/" + GenerateModel.getModelClassSimpleName(xTableName, form.tablePrefix) + "DAO.java";
            String daoImplPathname = daoImplDirName + "/MyBatis" + GenerateModel.getModelClassSimpleName(xTableName, form.tablePrefix) + "DAO.java";
            String boPathname = boDirName + "/" + GenerateModel.getModelClassSimpleName(xTableName, form.tablePrefix) + "BO.java";
            String boImplPathname = boImplDirName + "/Default" + GenerateModel.getModelClassSimpleName(xTableName, form.tablePrefix) + "BO.java";

            Map<Object, Object> context = GenerateModel.getContext(form.basePackageName, form.projectName, form.tablePrefix, tableInfo);

            try {
                //model类不存在，生成dao文件
                if (!DirUtils.exists(modelPathname)) {
                    TemplateUtils.generateFile(daoPathname, "template/dao/dao.ftl", configuration, context);
                    TemplateUtils.generateFile(daoImplPathname, "template/mybatis/dao/dao-impl.ftl", configuration, context);
                    TemplateUtils.generateFile(boPathname, "template/bo/bo.ftl", configuration, context);
                    TemplateUtils.generateFile(boImplPathname, "template/bo/bo-impl.ftl", configuration, context);
                }
                TemplateUtils.generateFile(sqlMapPathname, "template/mybatis/mapper-" + GenerateModel.getDbTypeName(1).toLowerCase().replace(" ", "-") + ".ftl", configuration, context);
                TemplateUtils.generateFile(modelPathname, "template/model/model.ftl", configuration, context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
