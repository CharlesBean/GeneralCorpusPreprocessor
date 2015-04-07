package webmd

import model.database.Database
import model.database.Table

/**
 * Created by charles on 4/1/15.
 */

/**
 * Represents a Many-to-Many Relational Table between Document and Topic tables
 */
class TermTable extends Table {
    /**
     * Default constructor
     * @param database @param mTableName
     */
    TermTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Adds a term to the table of terms.
     *
     * @param term the word
     */
    boolean AddRow(String term) {
        if (term.find(~".*\\s.")) {
            // If we find a space between two characters, can't add (not just a word)
            return false;
        } else {
            // Otherwise, string is just a word, so we can trim and add
            term = term.trim()

            def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
            def queryString = "INSERT INTO $tableName(term) VALUES(?)"

            return mDatabase.GetDBO().execute(queryString.toString(), [term])
        }
    }

    /**
     * Removes a row based on its post_id
     *
     * @param postID
     */
    boolean RemoveRow(int id) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "DELETE FROM $tableName WHERE id=?"

        return mDatabase.GetDBO().execute(queryString.toString(), [id])
    }

    // TODO - exists function
}
