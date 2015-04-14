package model.database

/**
 * Created by charles on 4/1/15.
 */
abstract class Table {

    protected String mTableName
    protected Database mDatabase

    /**
     * Default constructor
     * @param mTableName
     */
    Table(Database database, String tableName) {
        mDatabase = database
        mTableName = mDatabase.GetTablePrefix() + tableName
    }

    /**
     *
     * @param tableName
     * @param columns
     * @return
     */
    boolean CreateTable(String columns) {
        String queryString = "CREATE TABLE $mTableName " + columns
        mDatabase.GetDBO().execute(queryString)
    }

    /**
     * Clears the table of all data (without dropping)
     *
     * @return true if query fine
     */
    boolean Clear() {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "TRUNCATE TABLE $tableName"

        return mDatabase.GetDBO().execute(queryString.toString(), [])
    }

    /**
     * Base Method for adding a row to a table
     *
     * Adds a null row to a table by default. Auto incrementing any AI fields.
     *
     * WARNING: Careful if there are nullable fields in the table.
     *
     * @return true if we added a row (SQL fine)
     */
    boolean AddRow() {
        def tableName = mDatabase.GetDatabaseName() + '.' + mTableName
        def queryString = "INSERT INTO $tableName() VALUES(NULL)"

        return mDatabase.GetDBO().execute(queryString.toString(), [])
    }

    String GetTableName() { return mTableName }
    Database GetDatabase() { return mDatabase; }
}
