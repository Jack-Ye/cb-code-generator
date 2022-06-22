<Context>
  <Resource name="jdbc/${javaProjectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="org.postgresql.Driver"
            url="jdbc:postgresql://localhost:5432/${javaProjectName}"
            username="${javaProjectName}"
            password="123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
