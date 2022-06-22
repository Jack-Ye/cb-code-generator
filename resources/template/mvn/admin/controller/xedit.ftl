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
</head>
<body class="market post-content">
<${r"#"}include "/common/header.ftl">
<${r"#"}assign menuGroup = "">
<${r"#"}assign menuItem = "">
<${r"#"}include "/common/sidebar.ftl">
<div class="main-content">
  <div class="page-content">
    <div class="row">
      <div class="col-xs-12">
        <${r"#"}if id??>
          <h3 class="header smaller lighter blue">文章修改</h3>
        <${r"#"}else>
          <h3 class="header smaller lighter blue">文章新增</h3>
        </${r"#"}if>
        <div id="warning"></div>
        <${r"@"}co.danger />
        <div class="table-responsive dataTables_wrapper">
          <div class="content">
            <form id="form" action="/article/save" method="post" enctype="multipart/form-data">
              <input type="hidden" id="id" name="id"<${r"#"}if id??> value="${r"$"}{id}"</${r"#"}if>>
              <#list propertyNameList as propertyName>
              <div class="row">
                <div class="form-group">
                  <label class="col-xs-3 control-label no-padding-right" for="${propertyNameMap["${propertyName}"]}">${propertyName}</label>
                  <div class="col-xs-3">
                    <input type="text" id="${propertyNameMap["${propertyName}"]}" name="${propertyName}"<${r"#"}if id??> value="${r"$"}{(${modelClassSimpleName?uncap_first}.${propertyName})!}"</${r"#"}if> class="form-control">
                  </div>
                </div>
              </div>
              <div class="space-8"></div>
              </#list>
              <div class="row">
                <div class="form-group col-xs-12">
                  <span id="error-container" class="error-info" style="color: red"></span><br>
                  <button type="submit" class="btn btn-info">保存</button>&nbsp;&nbsp;&nbsp;
                  <a href="/article/index" class="btn">取消</a>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<${r"#"}include "/common/corner-float.ftl">
<${r"@"}js.basic />
<${r"@"}js.dataTables />
<${r"@"}js.datePicker />
<${r"@"}js.customize />
<${r"@"}js.dropzone />
<script>
${r"$"}(function() {
});
</script>
</body>
</html>
</${r"#"}escape>
