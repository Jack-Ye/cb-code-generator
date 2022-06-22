<${r"#"}import "/macro/url.ftl" as url>
<${r"#"}import "/macro/css.ftl" as css>
<${r"#"}import "/macro/js.ftl" as js>
<${r"#"}import "/macro/callouts.ftl" as co>
<${r"#"}escape x as x?html>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${modelClassSimpleName}管理 - 网上加控制台</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
<${r"@"}css.basic />
<${r"@"}css.datePicker />
<${r"@"}css.ace />
<style>
${r"#"}form .btn {
  height: 34px;
  padding: 0 10px;
}
</style>
</head>
<body>
<${r"#"}include "/common/header.ftl">
<div class="main-container" id="main-container">
  <script>
    try {ace.settings.check('main-container', 'fixed')} catch(e) {}
  </script>
  <div class="main-container-inner">
    <a class="menu-toggler" id="menu-toggler" href="${r"#"}">
      <span class="menu-text"></span>
    </a>
    <${r"#"}assign menuGroup = "">
    <${r"#"}assign menuItem = "">
    <${r"#"}include "/common/sidebar.ftl">
    <div class="main-content">
      <div class="breadcrumbs" id="breadcrumbs">
        <script>
          try {ace.settings.check('breadcrumbs', 'fixed')} catch(e) {}
        </script>
        <ul class="breadcrumb">
          <li>${modelClassSimpleName}管理
          <li class="active">${modelClassSimpleName}列表
        </ul>
      </div>
      <div class="page-content">
        <div class="row">
          <div class="col-xs-12">
            <div class="row">
              <div class="col-xs-12">
                <div class="row">
                  <div class="col-xs-12">
                    <h3 class="header smaller lighter blue">${modelClassSimpleName}列表</h3>
                    <div class="table-header">
                    </div>
                    <div class="row">
                      <div id="warning" class="col-md-12"></div>
                      <form id="myform" action="" method="get">
                        <input type="hidden" name="pageNumber" value="1">
                        <#list propertyNameList as propertyName>
                        <div class="col-sm-3 form-group">
                          <div class="input-group">
                            <span class="input-group-addon">${propertyName}</span>
                            <input type="text" name="${propertyName}" value="${r"$"}{(q.${propertyName})!}" class="form-control date-picker">
                          </div>
                        </div>
                        </#list>
                        <div class="col-sm-3 form-group">
                          <button type="submit" class="btn btn-primary">查询</button>
                        </div>
                      </form>
                      <div class="col-sm-3 form-group">
                        <a href="/article/edit" class="btn btn-success">新增文章</a>
                      </div>
                    </div>
                    <div class="space-6"></div>
                    <div class="table-responsive">
                      <div class="alert alert-block alert-success">
                        <i class="icon-bar-chart green"></i> 总计<strong class="red">${r"$"}{count!}</strong>条数据
                      </div>
                      <table id="sample-table-1" class="table table-striped table-bordered table-hover">
                        <thead>
                          <tr>
                            <#list propertyNameList as propertyName>
                            <th>${propertyName}
                            </#list>
                            <th>操作
                        </thead>
                        <tbody>
                        <${r"#"}list p.thisPageElements as e>
                          <tr>
                            <#list propertyNameList as propertyName>
                            <td>${r"$"}{e.${propertyName}!}
                            </#list>
                            <td>
                              <div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">
                                <a class="red j-delete" href="javascript:del(${r"$"}{e.id})">
                                  <i class="icon-trash bigger-110">删除</i>
                                </a>
                                <a class="blue j-update" href="<${r"@"}url.home />/${viewDirName}/${r"$"}{e.id}/edit?redirectUrl=${r"$"}{encodedReturnUrl}">
                                  <i class="icon-pencil bigger-110">修改</i>
                                </a>
                              </div>
                          </tr>
                        </${r"#"}list>
                        </tbody>
                      </table>
                      <${r"#"}include "/common/pagination.ftl">
                      <${r"@"}pagination p />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<${r"@"}js.basic />
<${r"@"}js.bootbox />
<${r"@"}js.customize />
<script>
${r"$"}(function() {
});
</script>
</body>
</html>
</${r"#"}escape>
