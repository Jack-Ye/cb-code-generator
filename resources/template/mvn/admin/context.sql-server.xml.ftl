<Context>
  <Resource name="jdbc/${javaProjectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
            url="jdbc:sqlserver://localhost:1433;DatabaseName=${javaProjectName};integratedSecurity=false"
            username="${javaProjectName}"
            password="123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
