package model.database
/**
 * Created by charles on 4/1/15.
 */
abstract class Table {

    // TODO - create another superclass - RelationalTable ??

    protected String mTableName;
    protected Database mDatabase;

    /**
     * Default constructor
     * @param mTableName
     */
    Table(Database database, String tableName) {
        this.mDatabase = database;
        this.mTableName = tableName
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
