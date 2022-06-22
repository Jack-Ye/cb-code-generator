<${r"#"}import "/macro/url.ftl" as url>
<${r"#"}import "/macro/css.ftl" as css>
<${r"#"}import "/macro/js.ftl" as js>
<${r"#"}import "/macro/layout.ftl" as layout>
<${r"#"}import "/macro/pagination.ftl" as pagination>
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
          <h3 class="box-title">${modelClassSimpleName}列表</h3>
        </div>
        <form name="listForm" action="<${r"@"}url.home />/${viewDirName}/" method="get" class="form-horizontal">
          <div class="box-body">
            <#list propertyNameList as propertyName>
            <div class="form-group">
              <label for="${propertyNameMap["${propertyName}"]}" class="col-sm-1 control-label">${propertyName}:</label>
              <div class="col-sm-2">
                <input id="${propertyNameMap["${propertyName}"]}" type="text" name="${propertyName}" value="${r"$"}{(q.${propertyName})!}" class="form-control">
              </div>
            </div>
            </#list>
          </div>
          <div class="box-footer">
            <div class="form-group">
              <label class="col-sm-1 control-label">&nbsp;</label>
              <div class="col-sm-2 mr-20">
                <button type="submit" class="btn btn-primary">查询</button>
                <button type="reset" class="btn btn-default">重置</button>
              </div>
            </div>
          </div>
        </form>
        <div class="box-body">
          <div class="box-action">
            <div class="btn-group">
              <a href="<${r"@"}url.home />/${viewDirName}/new?redirectUrl=${r"$"}{encodedReturnUrl}" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span> 新建</a>
            </div>
          </div>
          <table class="table table-bordered table-hover">
            <thead>
              <tr>
                <#list propertyNameList as propertyName>
                <th>${propertyName}
                </#list>
                <th>操作
              </tr>
            </thead>
            <tbody>
              <${r"#"}list p.thisPageElements as e>
                <tr>
                  <#list propertyNameList as propertyName>
                  <td>${r"$"}{e.${propertyName}!}
                  </#list>
                  <td>
                    <a href="<${r"@"}url.home />/${viewDirName}/${r"$"}{e.id}/edit?redirectUrl=${r"$"}{encodedReturnUrl}">编辑</a>
                    | <a href="<${r"@"}url.home />/${viewDirName}/${r"$"}{e.id}/remove?redirectUrl=${r"$"}{encodedReturnUrl}" class="btn-remove">删除</a>
                </tr>
              </${r"#"}list>
            </tbody>
          </table>
          <${r"@"}pagination.pagination />
        </div>
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
  ${r"$"}(".btn-remove").click(function(e) {
    if (!confirm("确定删除吗？")) {
      e.preventDefault();
    }
  });
});
</script>
</body>
</html>
</${r"#"}escape>
