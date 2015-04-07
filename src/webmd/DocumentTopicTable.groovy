package webmd

import model.database.Database
import model.database.Table

/**
 * Created by charles on 4/1/15.
 */

/**
 * Represents a Many-to-Many Relational Table between Document and Topic tables
 */
class DocumentTopicTable extends Table {
    /**
     * Default constructor
     * @param database @param mTableName
     */
    DocumentTopicTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Adds a row to the DocumentTopic Relational Table
     *      - Primary keys post_id, topic_id
     *      - Weight (UNUSED) default 1
     *
     * @param postID
     * @param topicID
     * @param weight
     */
    boolean AddRow(String postID, int topicID, double weight=1) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "INSERT INTO $tableName(post_id, topic_id, weight) VALUES(?, ?, ?)"

        return mDatabase.GetDBO().execute(queryString.toString(), [postID, topicID, weight])
    }

    /**
     * Removes a row based on its post_id
     *
     * @param postID
     */
    boolean RemoveRow(String postID) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "DELETE FROM $tableName WHERE post_id=?"

        return mDatabase.GetDBO().execute(queryString.toString(), [postID])
    }
}
