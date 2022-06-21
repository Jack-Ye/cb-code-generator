package com.chenbang.intellij.plugin.api.cli;

import com.chenbang.intellij.plugin.api.jdbc.meta.table.ColumnInfo;
import com.chenbang.intellij.plugin.api.jdbc.meta.table.TableInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateModel {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String getJavaBaseDirName(String baseDirName, String basePackageName, String projectName) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseDirName);
        sb.append("/src/main/java/");
        sb.append(basePackageName.replace('.', '/'));
        sb.append("/");
        sb.append(projectName);
        return sb.toString();
    }

    public static String getModelClassSimpleName(String tableName, String tablePrefix) {
        return getClassSimpleName(tableName, tablePrefix);
    }

    private static String getClassSimpleName(String tableName, String tablePrefix) {
        if (tablePrefix == null) {
            tablePrefix = "";
        }

        String name = tableName.toLowerCase();
        String prefix = tablePrefix.toLowerCase();

        if (!name.startsWith(prefix)) {
            throw new RuntimeException("表名前缀不正确");
        }

        name = name.substring(prefix.length());
        String[] ss = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return sb.toString();
    }

    public static Map<Object, Object> getContext(String basePackageName, String projectName, String tablePrefix, TableInfo tableInfo) {
        String tableName = tableInfo.getTableName().toLowerCase();

        Map<Object, Object> context = new HashMap<Object, Object>();
        context.put("basePackageName", basePackageName);
        context.put("javaProjectName", projectName);
        context.put("projectName", projectName);
//        context.put("iBasePackageName", "com.chenbang.analysis");
//        context.put("iBasePackageName", "com.yunfang.hxz");
        context.put("iBasePackageName", "com.calanger");
//        context.put("iBasePackageName", "com.chenbang.questionnaire");
        context.put("iLibParentVersion", "6");
        context.put("iCommonVersion", "3.0.0-SNAPSHOT");
        context.put("tableName", tableName);
        context.put("modelClassSimpleName", getModelClassSimpleName(tableName, tablePrefix));

        List<String> propertyNameList = new ArrayList<String>();
        List<String> propertyTypeList = new ArrayList<String>();
        List<String> columnTypeList = new ArrayList<String>();
        boolean autoIncrement = false;
        String pkType = "";
        List<ColumnInfo> columnInfoList = tableInfo.getColumnInfoList();
        for (ColumnInfo columnInfo : columnInfoList) {
            String columnName = columnInfo.getColumnName();
            String columnTypeName = columnInfo.getColumnTypeName();

            propertyNameList.add(getPropertyName(columnName));

            if (!autoIncrement && columnInfo.isAutoIncrement()) {
                autoIncrement = true;
            }

            if ("char".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("CHAR");
            } else if ("varchar".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("tinytext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("text".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("mediumtext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("longtext".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("VARCHAR");
            } else if ("decimal".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("DECIMAL");
            } else if ("date".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("DATE");
            } else if ("datetime".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("TIMESTAMP");
            } else if ("bit".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("tinyint".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("smallint".equalsIgnoreCase(columnTypeName)
                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("mediumint".equalsIgnoreCase(columnTypeName)
                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("int".equalsIgnoreCase(columnTypeName)
                    || "int identity".equalsIgnoreCase(columnTypeName)
                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("INTEGER");
            } else if ("bigint".equalsIgnoreCase(columnTypeName)
                    || "bigint identity".equalsIgnoreCase(columnTypeName)
                    || "bigint unsigned".equalsIgnoreCase(columnTypeName)
                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
                columnTypeList.add("BIGINT");
            } else {
                columnTypeList.add("UnknownType" + "[" + columnTypeName + "]");
            }

            if ("bit".equalsIgnoreCase(columnTypeName)
                    || "tinyint".equalsIgnoreCase(columnTypeName)
                    || "tinyint unsigned".equalsIgnoreCase(columnTypeName)
                    || "smallint".equalsIgnoreCase(columnTypeName)
                    || "smallint unsigned".equalsIgnoreCase(columnTypeName)
                    || "mediumint".equalsIgnoreCase(columnTypeName)
                    || "mediumint unsigned".equalsIgnoreCase(columnTypeName)
                    || "int".equalsIgnoreCase(columnTypeName)
                    || "int identity".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.Integer");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Integer";
                }
            } else if ("bigint".equalsIgnoreCase(columnTypeName)
                    || "bigint identity".equalsIgnoreCase(columnTypeName)
                    || "bigint unsigned".equalsIgnoreCase(columnTypeName)
                    || "int unsigned".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.Long");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.Long";
                }
            } else if ("char".equalsIgnoreCase(columnTypeName)
                    || "varchar".equalsIgnoreCase(columnTypeName)
                    || "tinytext".equalsIgnoreCase(columnTypeName)
                    || "text".equalsIgnoreCase(columnTypeName)
                    || "mediumtext".equalsIgnoreCase(columnTypeName)
                    || "longtext".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.lang.String");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.lang.String";
                }
            } else if ("date".equalsIgnoreCase(columnTypeName)
                    || "datetime".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.util.Date");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.util.Date";
                }
            } else if ("decimal".equalsIgnoreCase(columnTypeName)) {
                propertyTypeList.add("java.math.BigDecimal");

                if ("id".equalsIgnoreCase(columnName)) {
                    pkType = "java.math.BigDecimal";
                }
            } else {
                propertyTypeList.add("UnknownType[" + columnTypeName + "]");
            }
        }
        context.put("propertyNameList", propertyNameList);
        context.put("propertyTypeList", propertyTypeList);
        context.put("columnTypeList", columnTypeList);
        context.put("columnInfoList", columnInfoList);
        context.put("autoIncrement", autoIncrement);
        context.put("pkType", pkType);

        return context;
    }

    private static String getPropertyName(String columnName) {
        String name = columnName.toLowerCase();

        String[] ss = name.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append(StringUtils.capitalize(s));
        }
        return StringUtils.uncapitalize(sb.toString());
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
