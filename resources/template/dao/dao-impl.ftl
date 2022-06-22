package ${basePackageName}.${javaProjectName}.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;

import ${iBasePackageName}.common.dao.AbstractDAO;
import ${basePackageName}.${javaProjectName}.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.${javaProjectName}.model.${modelClassSimpleName};
import ${basePackageName}.${javaProjectName}.vo.${modelClassSimpleName}VO;

@Repository("${modelClassSimpleName?uncap_first}DAO")
public class Ibatis${modelClassSimpleName}DAO extends AbstractDAO<${modelClassSimpleName}, ${modelClassSimpleName}VO, ${pkType}> implements ${modelClassSimpleName}DAO {
    @Autowired
    private SqlMapClientTemplate sqlMapClientTemplate;

    @Override
    protected SqlMapClientTemplate getSqlMapClientTemplate() {
        return sqlMapClientTemplate;
    }

    @Override
    protected String getNamespace() {
        return "${modelClassSimpleName?uncap_first}DAO";
    }
}
