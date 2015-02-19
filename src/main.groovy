import groovy.sql.Sql

/**
 * Created by charles on 2/19/15.
 */

// Doing some testing...

// TODO - add driver??

def databaseWebMD = [url: "jdbc:mysql://localhost:3306/WebMD1", user: "root", password: ""]
def sql = Sql.newInstance(databaseWebMD.url, databaseWebMD.user, databaseWebMD.password)

def rows = sql.rows("SELECT * FROM WebMD1.diabetes_exchange LIMIT 10;")
println rows.join('\n')
