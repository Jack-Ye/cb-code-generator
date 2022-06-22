<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC
    "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${modelClassSimpleName?uncap_first}DAO">
  <resultMap id="${modelClassSimpleName?uncap_first}" type="${basePackageName}.${javaProjectName}.model.${modelClassSimpleName}">
  <#list propertyNameList as propertyName>
    <result property="${propertyName}" column="${columnInfoList[propertyName_index].columnName}" />
  </#list>
  </resultMap>

  <sql id="sql_insert_column_list">
    <trim prefix="" prefixOverrides=",">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${columnInfoList[propertyName_index].columnName}</if>
    <#else>
      <if test="entity.${propertyName} != null">,${columnInfoList[propertyName_index].columnName}</if>
      <#if propertyName == "createdAt" || propertyName == "updatedAt">
      <if test="entity.${propertyName} == null">,${columnInfoList[propertyName_index].columnName}</if>
      </#if>
    </#if>
    </#list>
    </trim>
  </sql>

  <sql id="sql_insert_property_list">
    <trim prefix="" prefixOverrides=",">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${r"#"}{entity.${propertyName}}</if>
    <#else>
      <if test="entity.${propertyName} != null">,${r"#"}{entity.${propertyName}}</if>
      <#if propertyName == "createdAt" || propertyName == "updatedAt">
      <if test="entity.${propertyName} == null">,GETDATE()</if>
      </#if>
    </#if>
    </#list>
    </trim>
  </sql>

  <sql id="sql_column_list">
  <#list columnInfoList as columnInfo>
    <#if (columnInfo_index > 0)>,</#if>${columnInfo.columnName}
  </#list>
  </sql>

  <sql id="sql_update_set">
    <trim prefix="SET" prefixOverrides=",">
      <if test="entity != null">
      <#list propertyNameList as propertyName>
      <#if propertyTypeList[propertyName_index] == "java.lang.String">
        <choose>
          <when test="entity.forceUpdateProperties.contains('${propertyName}')">
            ,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}, jdbcType=${columnTypeList[propertyName_index]}}
          </when>
          <otherwise>
            <if test="entity.${propertyName} != null and entity.${propertyName} != ''">,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}}</if>
          </otherwise>
        </choose>
      <#else>
        <choose>
          <when test="entity.forceUpdateProperties.contains('${propertyName}')">
            ,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}, jdbcType=${columnTypeList[propertyName_index]}}
          </when>
          <otherwise>
            <if test="entity.${propertyName} != null">,${columnInfoList[propertyName_index].columnName} = ${r"#"}{entity.${propertyName}}</if>
            <#if propertyName == "updatedAt">
            <if test="entity.${propertyName} == null">,${columnInfoList[propertyName_index].columnName} = GETDATE()</if>
            </#if>
          </otherwise>
        </choose>
      </#if>
      </#list>
        <if test="!entity.incrementUpdateMap.isEmpty()">
          ,
          <foreach collection="entity.incrementUpdateMap" index="key" item="value" separator=",">
            ${r"$"}{key} = ${r"$"}{key} + ${r"#"}{value}
          </foreach>
        </if>
      </if>
    </trim>
  </sql>

  <sql id="sql_condition">
    <if test="condition != null">
    <#list propertyNameList as propertyName>
    <#if propertyTypeList[propertyName_index] == "java.lang.String">
      <if test="condition.${propertyName} != null and condition.${propertyName} != ''">AND ${columnInfoList[propertyName_index].columnName} = ${r"#"}{condition.${propertyName}}</if>
    <#else>
      <if test="condition.${propertyName} != null">AND ${columnInfoList[propertyName_index].columnName} = ${r"#"}{condition.${propertyName}}</if>
    </#if>
    </#list>
      <if test="!condition.expressionChainList.empty">
        AND
        <foreach collection="condition.expressionChainList" item="expressionChain" separator="OR">
          <if test="!expressionChain.expressionList.empty">
            <foreach collection="expressionChain.expressionList" item="expression" separator="AND">
              <choose>
                <when test="expression.type == 0">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator}
                </when>
                <when test="expression.type == 1">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator} ${r"#"}{expression.value}
                </when>
                <when test="expression.type == 2">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator} ${r"#"}{expression.value} AND ${r"#"}{expression.value1}
                </when>
                <when test="expression.type == 3">
                  ${r"$"}{expression.column} ${r"$"}{expression.operator}
                  <foreach collection="expression.values" item="value" open="(" separator="," close=")">
                    ${r"#"}{value}
                  </foreach>
                </when>
              </choose>
            </foreach>
          </if>
        </foreach>
      </if>
    </if>
  </sql>

  <sql id="sql_pagination_start">
  <![CDATA[
    SELECT
    <#list columnInfoList as columnInfo>
      <#if (columnInfo_index > 0)>,</#if>t.${columnInfo.columnName}
    </#list>
    FROM (SELECT t2.n, t1.id FROM ${tableName} t1, (
      SELECT TOP ${r"$"}{endRow} ROW_NUMBER() OVER (
  ]]>
  </sql>

  <sql id="sql_pagination_end">
  <![CDATA[
    ) t2 WHERE t1.id = t2.id AND t2.n >= ${r"$"}{startRow}) a, ${tableName} t WHERE a.id = t.id
  ]]>
  </sql>

  <insert id="insert" parameterType="java.util.Map">
    <#if pkType == "java.lang.Integer" || pkType == "java.lang.Long">
    <selectKey keyProperty="entity.id" resultType="${pkType}" order="AFTER">
      SELECT SCOPE_IDENTITY()
    </selectKey>
    </#if>
    INSERT INTO ${tableName} (
      <include refid="sql_insert_column_list" />
    ) VALUES (
      <include refid="sql_insert_property_list" />
    )
  </insert>

  <select id="get" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <select id="getById" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    SELECT
      <include refid="sql_column_list" />
    FROM ${tableName}
    WHERE id = ${r"#"}{id}
  </select>

  <select id="find" parameterType="java.util.Map" resultMap="${modelClassSimpleName?uncap_first}">
    <if test="startRow == null or endRow == null">
      SELECT
        <include refid="sql_column_list" />
      FROM ${tableName}
      <trim prefix="WHERE" prefixOverrides="AND">
        <include refid="sql_condition" />
      </trim>
      <if test="orderBy != null">
        ORDER BY ${r"$"}{orderBy}
      </if>
    </if>
    <if test="startRow != null and endRow != null">
      <include refid="sql_pagination_start" />
      <if test="orderBy != null">
        ORDER BY ${r"$"}{orderBy}
      </if>
      <if test="orderBy == null">
        ORDER BY id
      </if>
          ) n, id FROM ${tableName}
      <trim prefix="WHERE" prefixOverrides="AND">
        <include refid="sql_condition" />
      </trim>
      <include refid="sql_pagination_end" />
    </if>
  </select>

  <select id="count" parameterType="java.util.Map" resultType="java.lang.Integer">
    SELECT count(${r"$"}{count_column}) AS count_ FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <select id="aggregate" parameterType="java.util.Map" resultType="java.util.HashMap">
    SELECT ${r"$"}{aggregate_sql} FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </select>

  <update id="update" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_update_set" />
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </update>

  <update id="updateById" parameterType="java.util.Map">
    UPDATE ${tableName}
    <include refid="sql_update_set" />
    WHERE id = ${r"#"}{id}
  </update>

  <delete id="remove" parameterType="java.util.Map">
    DELETE FROM ${tableName}
    <trim prefix="WHERE" prefixOverrides="AND">
      <include refid="sql_condition" />
    </trim>
  </delete>

  <delete id="removeById" parameterType="java.util.Map">
    DELETE FROM ${tableName}
    WHERE id = ${r"#"}{id}
  </delete>
</mapper>
