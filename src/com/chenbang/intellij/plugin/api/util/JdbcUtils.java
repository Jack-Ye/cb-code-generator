package com.chenbang.intellij.plugin.api.util;

import com.chenbang.intellij.plugin.api.jdbc.meta.table.ColumnInfo;
import com.chenbang.intellij.plugin.api.jdbc.meta.table.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public final class JdbcUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtils.class);

    private static final String[] DEFAULT_TABLE_TYPES = { "TABLE" };

    public static List<TableInfo> getTableInfoList(Connection connection, String catalog, String schema, List<String> tableNameList) {
        List<TableInfo> tableInfoList = new LinkedList<TableInfo>();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            LOGGER.info("catalogs={}=", metaData.getCatalogs());
            for (String tableName : tableNameList) {
                ResultSet rs = metaData.getTables(catalog, schema, tableName, DEFAULT_TABLE_TYPES);
                while (rs.next()) {
                    ResultSetMetaData rsMetaData = rs.getMetaData();
//                    System.out.println("columnCount: " + rsMetaData.getColumnCount());
                    for (int i = 1; i <= rsMetaData.getColumnCount(); ++i) {
//                        System.out.println("catalogName" + i + ": " + rsMetaData.getCatalogName(i));
//                        System.out.println("schemaName" + i + ": " + rsMetaData.getSchemaName(i));
//                        System.out.println("tableName" + i + ": " + rsMetaData.getTableName(i));
//                        System.out.println("columnClassName" + i + ": " + rsMetaData.getColumnClassName(i));
//                        System.out.println("columnDisplaySize" + i + ": " + rsMetaData.getColumnDisplaySize(i));
//                        System.out.println("columnLabel" + i + ": " + rsMetaData.getColumnLabel(i));
//                        System.out.println("columnName" + i + ": " + rsMetaData.getColumnName(i));
//                        System.out.println("columnType" + i + ": " + rsMetaData.getColumnType(i));
//                        System.out.println("columnTypeName" + i + ": " + rsMetaData.getColumnTypeName(i));
//                        System.out.println("precision" + i + ": " + rsMetaData.getPrecision(i));
//                        System.out.println("scale" + i + ": " + rsMetaData.getScale(i));
                    }
//                    System.out.println("TABLE_CAT: " + rs.getString("TABLE_CAT"));
//                    System.out.println("TABLE_SCHEM: " + rs.getString("TABLE_SCHEM"));
//                    System.out.println("REMARKS: " + rs.getString("REMARKS"));
//                    System.out.println("TYPE_CAT: " + rs.getString("TYPE_CAT"));
//                    System.out.println("TYPE_SCHEM: " + rs.getString("TYPE_SCHEM"));
//                    System.out.println("TYPE_NAME: " + rs.getString("TYPE_NAME"));
//                    System.out.println("SELF_REFERENCING_COL_NAME: " + rs.getString("SELF_REFERENCING_COL_NAME"));
//                    System.out.println("REF_GENERATION: " + rs.getString("REF_GENERATION"));
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setTableName(rs.getString("TABLE_NAME"));
                    tableInfo.setTableType(rs.getString("TABLE_TYPE"));
                    tableInfo.setComment(rs.getString("REMARKS"));

                    // TODO: Primary Key
                    ResultSet pkRs = metaData.getPrimaryKeys(catalog, schema, tableInfo.getTableName());
                    rsMetaData = pkRs.getMetaData();
                    System.out.println("columnCount: " + rsMetaData.getColumnCount());
                    for (int i = 1; i <= rsMetaData.getColumnCount(); ++i) {
                        System.out.println(rsMetaData.getColumnName(i) + "-" + rsMetaData.getColumnClassName(i));
                    }
                    while (pkRs.next()) {
                        for (int i = 1; i <= rsMetaData.getColumnCount(); ++i) {
                            System.out.println(rsMetaData.getColumnName(i) + "-" + rsMetaData.getColumnClassName(i) + "-" + pkRs.getString(i));
                        }
                    }
                    // TODO: Index
                    // ResultSet indexRs = metaData.getIndexInfo(catalog, schema, tableInfo.getTableName(), true, false);

                    ResultSet colRs = metaData.getColumns(catalog, schema, tableInfo.getTableName(), "%");
                    rsMetaData = colRs.getMetaData();
                    System.out.println("columnCount: " + rsMetaData.getColumnCount());
                    for (int i = 1; i <= rsMetaData.getColumnCount(); ++i) {
//                        System.out.println("catalogName" + i + ": " + rsMetaData.getCatalogName(i));
//                        System.out.println("schemaName" + i + ": " + rsMetaData.getSchemaName(i));
//                        System.out.println("tableName" + i + ": " + rsMetaData.getTableName(i));
//                        System.out.println("columnClassName" + i + ": " + rsMetaData.getColumnClassName(i));
//                        System.out.println("columnDisplaySize" + i + ": " + rsMetaData.getColumnDisplaySize(i));
//                        System.out.println("columnLabel" + i + ": " + rsMetaData.getColumnLabel(i));
//                        System.out.println("columnName" + i + ": " + rsMetaData.getColumnName(i));
//                        System.out.println("columnType" + i + ": " + rsMetaData.getColumnType(i));
//                        System.out.println("columnTypeName" + i + ": " + rsMetaData.getColumnTypeName(i));
//                        System.out.println("precision" + i + ": " + rsMetaData.getPrecision(i));
//                        System.out.println("scale" + i + ": " + rsMetaData.getScale(i));
                    }
                    while (colRs.next()) {
                        for (int i = 9; i <= 9; ++i) {
                            System.out.println(rsMetaData.getColumnName(i) + "-" + rsMetaData.getColumnClassName(i) + "-" + colRs.getInt(i));
                        }
                        System.out.println();

                        ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setTableName(tableInfo.getTableName());
                        columnInfo.setColumnName(colRs.getString("COLUMN_NAME"));
                        columnInfo.setColumnType(colRs.getInt("DATA_TYPE"));
                        columnInfo.setColumnTypeName(colRs.getString("TYPE_NAME"));
                        columnInfo.setLength(colRs.getInt("COLUMN_SIZE"));
                        columnInfo.setPrecision(columnInfo.getLength());
                        columnInfo.setScale(colRs.getInt("DECIMAL_DIGITS"));
                        columnInfo.setNullable("YES".equals(colRs.getString("IS_NULLABLE")));
                        columnInfo.setAutoIncrement("YES".equals(colRs.getString("IS_AUTOINCREMENT")));
                        columnInfo.setGeneratedColumn("YES".equals(colRs.getString("IS_GENERATEDCOLUMN")));
                        columnInfo.setComment(colRs.getString("REMARKS"));
                        columnInfo.setPrimaryKey(false); // TODO: Primary Key

                        tableInfo.addColumnInfo(columnInfo);
                    }
                    close(colRs);

                    tableInfoList.add(tableInfo);
                }
                close(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tableInfoList;
    }

    public static List<TableInfo> getTableInfoList(Connection connection, String catalog, String schema, String tableNamePattern) {
        List<TableInfo> tableInfoList = new LinkedList<TableInfo>();

        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(catalog, schema, tableNamePattern, DEFAULT_TABLE_TYPES);
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(rs.getString("TABLE_NAME"));
                tableInfo.setTableType(rs.getString("TABLE_TYPE"));
                tableInfo.setComment(rs.getString("REMARKS"));

                // TODO: Primary Key
                // ResultSet pkRs = metaData.getPrimaryKeys(catalog, schema, tableInfo.getTableName());

                // TODO: Index
                // ResultSet indexRs = metaData.getIndexInfo(catalog, schema, tableInfo.getTableName(), true, false);

                ResultSet colRs = metaData.getColumns(catalog, schema, tableInfo.getTableName(), "%");
                while (colRs.next()) {
                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfo.setTableName(tableInfo.getTableName());
                    columnInfo.setColumnName(colRs.getString("COLUMN_NAME"));
                    columnInfo.setColumnType(colRs.getInt("DATA_TYPE"));
                    columnInfo.setColumnTypeName(colRs.getString("TYPE_NAME"));
                    columnInfo.setLength(colRs.getInt("COLUMN_SIZE"));
                    columnInfo.setPrecision(columnInfo.getLength());
                    columnInfo.setScale(colRs.getInt("DECIMAL_DIGITS"));
                    columnInfo.setNullable("YES".equals(colRs.getString("IS_NULLABLE")));
                    columnInfo.setAutoIncrement("YES".equals(colRs.getString("IS_AUTOINCREMENT")));
                    // columnInfo.setGeneratedColumn("YES".equals(colRs.getString("IS_GENERATEDCOLUMN")));
                    columnInfo.setComment(colRs.getString("REMARKS"));
                    columnInfo.setPrimaryKey(false); // TODO: Primary Key

                    tableInfo.addColumnInfo(columnInfo);
                }
                close(colRs);

                tableInfoList.add(tableInfo);
            }
            close(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tableInfoList;
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            // ignore
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            // ignore
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            // ignore
        }
    }

    private JdbcUtils() {
    }
}
