package model.database

import model.forum.Corpus
import groovy.sql.Sql

/**
 * Created by charles on 4/1/15.
 */
class Database {
    private String mHostURL;
    private String mUsername;
    private String mPassword;
    private String mDriverClassName;
    private String mTablePrefix;
    private String mDatabaseName;

    private Sql mDBO;
    private Corpus mCorpus;
    private mTables = [];           // TODO - remove?

    // TODO - create Disconnect()

    /**
     * Constructor
     *      - Initializes a database object
     *      - Creates connection to forum
     *
     * @param corpus - a Corpus to which this DB owns
     * @param url - a String for the host of the DB
     * @param user - a String for the username (login)
     * @param password - a String for the user password (login)
     * @param driver - optional driver package/class name
     * @param tablePrefix - optional prefix for table access
     */
    Database(Corpus corpus, String url, String user, String password, String databaseName, String driver=null, String tablePrefix=null) {
        mCorpus = corpus
        mHostURL = url + databaseName;         // Make sure leading '/' exists... ?
        mUsername = user;
        mPassword = password;
        mDatabaseName = databaseName;
        mDriverClassName = driver;
        mTablePrefix = tablePrefix;
    }

    /**
     * Conects to a DB
     */
    void Connect() {
        try {
            if (mDriverClassName != null) {
                mDBO = Sql.newInstance(mHostURL, mUsername, mPassword, mDriverClassName)
            } else {
                mDBO = Sql.newInstance(mHostURL, mUsername, mPassword)
            }
        } catch (Exception e) {
            throw e;            // TODO - log
        }
    }

    /**
     * Adds a table to our DB's list of tables
     *
     * @param table
     * @return true if the push was successful
     */
    boolean AddTable(Table table) {
        return mTables.push(table);
    }

    Sql GetDBO() { return mDBO }

    String GetDatabaseName() { return mDatabaseName }

    String GetTablePrefix() { return mTablePrefix }
}
