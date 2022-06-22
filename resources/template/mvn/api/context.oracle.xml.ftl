<Context>
  <Resource name="jdbc/${javaProjectName}"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="oracle.jdbc.driver.OracleDriver"
            url="jdbc:oracle:thin:@localhost:1521:${javaProjectName}"
            username="${javaProjectName}"
            password="123456"
            maxActive="100"
            maxIdle="30"
            maxWait="10000" />
</Context>
