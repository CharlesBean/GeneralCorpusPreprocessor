package webmd

import model.database.Database
import model.database.Table

/**
 * Created by charles on 4/6/15.
 */
class TopicTable extends Table {
    /**
     * Default constructor
     * @param database @param tableName
     */
    TopicTable(Database database, String tableName) {
        super(database, tableName)
    }

    /**
     * Automatically adds a new topic (number) by adding a null row.
     *
     * The 'Topic' table is just a table of numbers (id).
     *
     * @return
     */
    @Override
    boolean AddRow() {
        return super.AddRow()
    }

    /**
     * Removes a row based on its id
     *
     * @param topicID
     */
    boolean RemoveRow(int topicID) {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "DELETE FROM $tableName WHERE id=?"

        return mDatabase.GetDBO().execute(queryString.toString(), [topicID])
    }

}
