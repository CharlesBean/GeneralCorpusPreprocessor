package webmd

import model.database.Database
import model.database.Table
import model.preprocessing.Preprocessor

import java.sql.SQLException

/**
 * Created by charles on 4/1/15.
 */

/**
 * Represents a Diabetes Exchange table, with content from WebMD.
 */
class DiabetesExchangeTable extends Table {
    /**
     * Default constructor
     * @param database @param mTableName
     */
    DiabetesExchangeTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Adds a row to the diabetes_exchange table
     *
     * @param uniqueID
     * @param qID
     * @param localID
     * @param title
     * @param poster
     * @param date
     * @param content
     * @param replyTo
     * @return
     */
    boolean AddRow(String uniqueID, String qID, int localID, String title, String poster, Date date, String content, String replyTo=null) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "INSERT INTO $tableName(uniqueID, qid, localID, title, poster, date, replyTo, content) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"

        return mDatabase.GetDBO().execute(queryString.toString(), [uniqueID, qID, localID, title, poster, date, replyTo, content])
    }

    /**
     * Removes a row based on its uniqueID
     *
     * @param postID
     */
    boolean RemoveRow(String uniqueID) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "DELETE FROM $tableName WHERE uniqueID=?"

        return mDatabase.GetDBO().execute(queryString.toString(), [uniqueID])
    }

    /**
     * Adds a cleaned content column to the table
     *
     * @return true if the query succeeded
     */
    boolean CreateCleanedContentColumn() {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "ALTER TABLE $tableName ADD cleaned_content text;"

        // Only intitiates
        try {
            return mDatabase.GetDBO().execute(queryString, [])
        }
        catch (SQLException e) {
            return true
        }
    }

    /**
     * Inserts text into a column named 'cleaned_content'
     *
     * @param uniqueID the unique id of the row (diabetes exchange)
     * @param content the content to insert
     * @return true if query successful
     */
    boolean UpdateCleanedContent(String uniqueID, String content) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "UPDATE $tableName SET cleaned_content=? WHERE uniqueID=?;"

        return mDatabase.GetDBO().executeUpdate(queryString.toString(), [content, uniqueID])
    }

    /**
     * Cleans the content of posts in our database, and save thems to a new column in the same table.
     *
     * @param preprocessor removes stopwords, etc.
     * @param removeEmptyContent flag - whether we should remove null and empty content
     * @return true if success
     */
    boolean CleanContent(WebMDPreprocessor preprocessor, boolean removeEmptyContent=false) {
        int added = 0;
        int deleted = 0;
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName;
        def queryString = "SELECT * FROM $tableName"        // ughh

        // TODO - properties files
        File slangTerms = new File("../../../text/slangTerms.txt")
        File stopwords = new File("../../../text/stopwords/english/ranks-nl/stopwords_english_1.txt")

        // For each row in the table
        mDatabase.GetDBO().eachRow("SELECT * FROM WebMD1.diabetes_exchange", {
            def content = it['content']
            def uniqueID = it['uniqueID'].toString()

            // Preprocess the content
            content = content.toString().toLowerCase()
            content = preprocessor.RemoveTags(content)
            content = preprocessor.RemoveSlang(content, slangTerms)
            content = preprocessor.RemoveStopwords(content, stopwords)
            content = preprocessor.RemoveNonalphabeticals(content)
            content = preprocessor.Stem(content)

            if (content == null || content.size() == 0) {
                // If the content is empty after preprocessing, and we want to remove
                if (removeEmptyContent) {
                    RemoveRow(uniqueID)
                }

                println "Deleted - $uniqueID"
                deleted++
            }
            else
            {
                // Update the cleanedContent column
                if (UpdateCleanedContent(uniqueID, content))
                    added++
            }
        })

        println "\n -- Added: $added \n -- Deleted: $deleted"

        return true;
    }
}
