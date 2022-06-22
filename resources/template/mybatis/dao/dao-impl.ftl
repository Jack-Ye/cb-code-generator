package ${basePackageName}.${javaProjectName}.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${iBasePackageName}.common.dao.AbstractDAO;
import ${basePackageName}.${javaProjectName}.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.${javaProjectName}.model.${modelClassSimpleName};

@Repository("${modelClassSimpleName?uncap_first}DAO")
public class MyBatis${modelClassSimpleName}DAO extends AbstractDAO<${modelClassSimpleName}, ${pkType}> implements ${modelClassSimpleName}DAO {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    protected SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    @Override
    protected String getNamespace() {
        return "${modelClassSimpleName?uncap_first}DAO";
    }
}
