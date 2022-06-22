<${r"#"}import "/macro/url.ftl" as url>
<${r"#"}import "/macro/css.ftl" as css>
<${r"#"}import "/macro/js.ftl" as js>
<${r"#"}import "/macro/layout.ftl" as layout>
<${r"#"}escape x as x?html>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${modelClassSimpleName}管理 - 网上加控制台</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
<${r"@"}css.bootstrap />
<${r"@"}css.adminLTE />
<${r"@"}css.allSkins />
<${r"@"}css.fontAwesome />
<${r"@"}css.ionicons />
<${r"@"}css.style />
</head>
<body class="hold-transition skin-blue-light sidebar-mini">
<div class="wrapper">
  <${r"#"}include "/include/header.ftl">

  <${r"@"}layout.nav menu="" menuItem="" />

  <div class="content-wrapper">
    <section class="content">
      <div class="box box-primary">
        <div class="box-header with-border">
          <h3 class="box-title"><${r"#"}if id??>编辑${modelClassSimpleName}<${r"#"}else>新建${modelClassSimpleName}</${r"#"}if></h3>
        </div>
        <form id="edit-form" name="editForm" action="<${r"#"}if id??><${r"@"}url.home />/${viewDirName}/${r"$"}{id}/update<${r"#"}else><${r"@"}url.home />/${viewDirName}/create</${r"#"}if>" method="post" class="form-horizontal">
          <input type="hidden" name="redirectUrl" value="${r"$"}{redirectUrl}">
          <div class="box-body">
            <#list propertyNameList as propertyName>
            <#if propertyName != "id" && propertyName != "createdAt" && propertyName != "updatedAt">
            <div class="form-group">
              <label for="${propertyNameMap["${propertyName}"]}" class="col-sm-2 control-label"><span class="required">*</span>${propertyName}:</label>
              <div class="col-sm-3">
                <input id="${propertyNameMap["${propertyName}"]}" type="text" name="${propertyName}" value="${r"$"}{(${modelClassSimpleName?uncap_first}.${propertyName})!}" class="form-control">
              </div>
            </div>
            </#if>
            </#list>
          </div>
          <div class="box-footer">
            <div class="form-group">
              <label class="col-sm-2 control-label">&nbsp;</label>
              <div class="col-sm-3">
                <button type="submit" id="btn-save" class="btn btn-primary mr-20">保存</button>
                <a href="${r"$"}{redirectUrl}" class="btn btn-default">取消</a>
              </div>
            </div>
          </div>
        </form>
      </div>
    </section>
  </div>

  <${r"#"}include "/include/footer.ftl">
</div>

<${r"@"}js.jquery />
<${r"@"}js.bootstrap />
<${r"@"}js.slimscroll />
<${r"@"}js.fastclick />
<${r"@"}js.app />

<script>
${r"$"}(function() {
});
</script>
</body>
</html>
</${r"#"}escape>
