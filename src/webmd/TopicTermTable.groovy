package webmd

import model.database.Database
import model.database.Table

/**
 * Created by charles on 4/1/15.
 */

/**
 * Represents a Many-to-Many Relational Table between Topic and Term tables
 */
class TopicTermTable extends Table {
    /**
     * Default constructor
     * @param database @param mTableName
     */
    TopicTermTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Creates a topic-to-term relationship. Adds a row to the table.
     *
     * @param topicID the id for the topic
     * @param termID the id for the term
     * @param weight the strength of the edge
     * @return true if query successful
     */
    boolean AddRow(int topicID, int termID, double weight=1) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "INSERT INTO $tableName(topicid, termid, weight) VALUES(?, ?, ?)"

        return mDatabase.GetDBO().execute(queryString.toString(), [topicID, termID, weight])
    }

    /**
     * Removes a topic relationship from the table
     *
     * Arguments are optional - but nothing will be removed if topicA or topicB ID's are not
     * specified. If there are multiple rows with an ID (for one argument..), then they will
     * all be removed.
     *
     * @param topicID the row(s) with topicid_A to remove
     * @param termID the row(s) with topicid_B to remove
     * @return true if our query was successful
     */
    boolean RemoveRow(int topicID=-1, int termID=-1) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = null
        def parameters = []

        // Creating our query and parameters based on arguments supplied
        if (termID == -1 && topicID != -1) {
            // If they specified a topicID to remove by
            queryString = "DELETE FROM $tableName WHERE topicid=?"
            parameters.push(topicID)
        }
        else if (topicID == -1 && termID != -1) {
            // If termID specified
            queryString = "DELETE FROM $tableName WHERE termid=?"
            parameters.push(termID)
        }
        else if (termID != -1 && termID != -1) {
            // If both topicID and termID specified
            queryString = "DELETE FROM $tableName WHERE topicid=? AND termid=?"
            parameters.push(topicID)
            parameters.push(termID)
        }

        // If the user specified an ID to remove from the table (topicID, or termID), return if query success
        if (queryString)
            return mDatabase.GetDBO().execute(queryString.toString(), parameters)

        return false;
    }
}
