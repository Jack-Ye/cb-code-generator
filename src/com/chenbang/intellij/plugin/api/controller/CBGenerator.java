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

    public static class Form {
        String projectName = "sunrise";
        String basePackageName = "com.chenbang";
        String subprojectNames = "com.chenbang.intellij.plugin.api,admin";
        String tableNames = "clazz";
        String tablePrefix = "";
        String baseDirName = "/Users/kcajay/Code/cb/temp/";
        List<TableInfo> tableInfoList;
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

        //TODO ？
        String coreBaseDirName = form.baseDirName + "/wj-services";

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
                TemplateUtils.generateFile(sqlMapPathname, "template/mybatis/mapper-" + GenerateModel.getDbTypeName(1).toLowerCase().replace(" ", "-") + ".ftl", configuration, context);
                TemplateUtils.generateFile(modelPathname, "template/model/model.ftl", configuration, context);
                TemplateUtils.generateFile(daoPathname, "template/dao/dao.ftl", configuration, context);
                TemplateUtils.generateFile(daoImplPathname, "template/mybatis/dao/dao-impl.ftl", configuration, context);
                TemplateUtils.generateFile(boPathname, "template/bo/bo.ftl", configuration, context);
                TemplateUtils.generateFile(boImplPathname, "template/bo/bo-impl.ftl", configuration, context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
