<Context>
  <Resource name="jdbc/${javaProjectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/${javaProjectName}?useUnicode=true&amp;characterEncoding=UTF-8"
            username="${javaProjectName}"
            password="123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
