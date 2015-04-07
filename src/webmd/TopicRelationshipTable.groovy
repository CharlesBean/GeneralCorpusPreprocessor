package webmd

import model.database.Database
import model.database.Table

/**
 * Created by charles on 4/1/15.
 */

/**
 * Represents a Many-to-Many Relational Table between Topics (the Topic table)
 */
class TopicRelationshipTable extends Table {
    /**
     * Default constructor
     * @param database @param mTableName
     */
    TopicRelationshipTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Creates a topic-to-topic relationship. Adds a row to the table.
     *
     * @param aTopicID topicid_A
     * @param bTopicID topicid_B
     * @param weight the strength of the connection (not given in CTM)
     * @return true if query successful
     */
    boolean AddRow(int aTopicID, int bTopicID, double weight=1) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "INSERT INTO $tableName(topicid_A, topicid_B, weight) VALUES(?, ?, ?)"

        return mDatabase.GetDBO().execute(queryString.toString(), [aTopicID, bTopicID, weight])
    }

    /**
     * Removes a topic relationship from the table
     *
     * Arguments are optional - but nothing will be removed if topicA or topicB ID's are not
     * specified. If there are multiple rows with an ID (for one argument..), then they will
     * all be removed.
     *
     * @param aTopicID the row(s) with topicid_A to remove
     * @param bTopicID the row(s) with topicid_B to remove
     * @return true if our query was successful
     */
    boolean RemoveRow(int aTopicID=-1, int bTopicID=-1) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = null
        def parameters = []

        // Creating our query and parameters based on arguments supplied
        if (bTopicID == -1 && aTopicID != -1) {
            // If they specified a topicID-A to remove by
            queryString = "DELETE FROM $tableName WHERE topicid_A=?"
            parameters.push(aTopicID)
        }
        else if (aTopicID == -1 && bTopicID != -1) {
            // If they specified a topicID-B to remove by
            queryString = "DELETE FROM $tableName WHERE topicid_B=?"
            parameters.push(bTopicID)
        }
        else if (bTopicID != -1 && bTopicID != -1) {
            // If both specified
            queryString = "DELETE FROM $tableName WHERE topicid_A=? AND topicid_B=?"
            parameters.push(aTopicID)
            parameters.push(bTopicID)
        }

        // If the user specified an ID to remove from the table (topicA, or topicB), return if query success
        if (queryString)
            return mDatabase.GetDBO().execute(queryString.toString(), parameters)

        return false;
    }
}
