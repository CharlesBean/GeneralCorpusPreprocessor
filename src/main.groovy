import classes.WebMDPreprocessor
import groovy.sql.Sql

import java.sql.SQLException

/**
 * Created by charles on 2/19/15.
 */

def processDiabetesExchangeForum() {
    int added = 0;
    int deleted = 0;
    
    // If we want to remove posts that have essentially no content (after parsing)
    boolean removeEmptyContent = false;
    
    // Creating DBO and Our Preprocessor
    def databaseWebMD = [url: 'jdbc:mysql://localhost:3306/WebMD1', user: 'root', password: '', driver: 'com.mysql.jdbc.Driver']
    def sql = Sql.newInstance(databaseWebMD.url, databaseWebMD.user, databaseWebMD.password)
    def webMDpp = new WebMDPreprocessor(sql)
    
    
    try {
        // Add a new column for cleaned content (apply only once...)
        sql.execute("ALTER TABLE WebMD1.diabetes_exchange ADD cleanedContent text;")
    }
    catch (SQLException e) {
        // Pass
    }
    
    
    sql.eachRow("SELECT * FROM WebMD1.diabetes_exchange;") {
        def content = it['content']
        def uniqueID = it['uniqueID']
        
        content = content.toString().toLowerCase()
        content = webMDpp.removeTags(content)
        content = webMDpp.removeSlang(content, new File("../text/slangTerms.txt"))
        content = webMDpp.removeStopwords(content, new File("../text/stopwords/english/ranks-nl/stopwords_english_1.txt"))
        content = webMDpp.removeNonAlphabeticals(content)
        content = webMDpp.stem(content)
        
        // If this row is of no interest to us... (empty)
        if (content == null || content.size() == 0) {
            if (removeEmptyContent) {
                // Delete the row from the table
                sql.execute("DELETE FROM WebMD1.diabetes_exchange WHERE uniqueID=$uniqueID;")
            }

            println "$uniqueID \t\t Deleted \n"
            deleted++
        }
        else
        {
            // Update the cleanedContent column 
            sql.executeUpdate("UPDATE WebMD1.diabetes_exchange SET cleanedContent=$content WHERE uniqueID=$uniqueID;")

            println "$uniqueID \t\t Added \n"
            added++
        }
    }
    
    println "\n -- Added: $added \n -- Deleted: $deleted"
}

processDiabetesExchangeForum()