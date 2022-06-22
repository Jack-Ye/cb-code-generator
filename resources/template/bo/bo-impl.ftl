package ${basePackageName}.${javaProjectName}.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${iBasePackageName}.common.bo.AbstractBO;
import ${iBasePackageName}.common.dao.DAO;
import ${basePackageName}.${javaProjectName}.bo.${modelClassSimpleName}BO;
import ${basePackageName}.${javaProjectName}.dao.${modelClassSimpleName}DAO;
import ${basePackageName}.${javaProjectName}.model.${modelClassSimpleName};

@Service("${modelClassSimpleName?uncap_first}BO")
public class Default${modelClassSimpleName}BO extends AbstractBO<${modelClassSimpleName}, ${pkType}> implements ${modelClassSimpleName}BO {
    @Autowired
    private ${modelClassSimpleName}DAO ${modelClassSimpleName?uncap_first}DAO;

    @Override
    protected DAO<${modelClassSimpleName}, ${pkType}> getDAO() {
        return ${modelClassSimpleName?uncap_first}DAO;
    }
}
