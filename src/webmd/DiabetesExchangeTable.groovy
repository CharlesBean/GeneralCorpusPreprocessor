package webmd

import model.database.Database
import model.database.Table

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
            throw e;
        }
    }

    boolean InsertCleanedContent(String content) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName

        mDatabase.GetDBO().execute()
    }
}
