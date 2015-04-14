package model.database

import model.forum.Corpus
import groovy.sql.Sql

/**
 * Created by charles on 4/1/15.
 */
abstract class Database {
    protected String mHostURL
    protected String mUsername
    protected String mPassword
    protected String mDriverClassName
    protected String mTablePrefix
    protected String mDatabaseName

    protected Sql mDBO
    protected Corpus mCorpus

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
    Database(Corpus corpus, String url, String user, String password, String databaseName, String driver=null, String tablePrefix="") {
        mCorpus = corpus
        mHostURL = url + databaseName               // Make sure leading '/' exists
        mUsername = user
        mPassword = password
        mDatabaseName = databaseName
        mDriverClassName = driver
        mTablePrefix = tablePrefix
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
            throw e
        }
    }

    /**
     * Closes a DB connection
     */
    void Disconnect() {
        if (mDBO)
            mDBO.close()
    }

    Sql GetDBO() { return mDBO }
    String GetDatabaseName() { return mDatabaseName }
    String GetTablePrefix() { return mTablePrefix }
}
