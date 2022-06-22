package ${basePackageName}.${javaProjectName}.model;

import ${iBasePackageName}.common.dao.AbstractPO;

public class ${modelClassSimpleName} extends AbstractPO {
    private static final long serialVersionUID = 1L;

    <#list propertyNameList as propertyName>
    private ${propertyTypeList[propertyName_index]} ${propertyName};
    </#list>

    <#list propertyNameList as propertyName>
    <#if (propertyName_index > 0)>

    </#if>
    public ${propertyTypeList[propertyName_index]} get${propertyName?cap_first}() {
        return ${propertyName};
    }

    public void set${propertyName?cap_first}(${propertyTypeList[propertyName_index]} ${propertyName}) {
        this.${propertyName} = ${propertyName};
    }

    public void set${propertyName?cap_first}(${propertyTypeList[propertyName_index]} ${propertyName}, boolean forceUpdate) {
        set${propertyName?cap_first}(${propertyName});

        if (forceUpdate) {
          addForceUpdateProperty("${propertyName}");
        }
    }
    </#list>
}
