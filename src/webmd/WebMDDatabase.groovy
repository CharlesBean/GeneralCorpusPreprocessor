package webmd

import model.database.Database
import model.forum.Corpus

/**
 * Created by charles on 4/9/15.
 */
class WebMDDatabase extends Database {

    DocumentTopicTable mDocumentTopicTable
    TermTable mTermTable
    TopicTable mTopicTable
    TopicTermTable mTopicTermTable
    TopicRelationshipTable mTopicRelationshipTable
    DiabetesExchangeTable mDiabetesExchangeTable

    /**
     * Constructor (super call)
     *
     * @param corpus Corpus object (unused)
     * @param url host url
     * @param user username for db login
     * @param password db user password
     * @param databaseName ..
     * @param driver optional driver
     * @param tablePrefix optional table prefix (e.g., 'testing')
     */
    WebMDDatabase(Corpus corpus, String url, String user, String password, String databaseName, String driver=null, String tablePrefix="") {
        super(corpus, url, user, password, databaseName, driver, tablePrefix)

        mDocumentTopicTable = new DocumentTopicTable(this, 'document_label')
        mTermTable = new TermTable(this, 'term')
        mTopicTable = new TopicTable(this, 'topic')
        mTopicTermTable = new TopicTermTable(this, 'topic_term')
        mTopicRelationshipTable = new TopicRelationshipTable(this, 'topic_relationship')
        mDiabetesExchangeTable = new DiabetesExchangeTable(this, 'diabetes_exchange')
    }

    /**
     * Creates our topic model tables. Paste queries below.
     *
     * @return true if successful
     */
    boolean CreateTables() {
        try{
            mDocumentTopicTable.CreateTable("(post_id varchar(25) NOT NULL, topic_id int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (post_id, topic_id));")
            mTopicTable.CreateTable("(id int(10) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id));")
            mTermTable.CreateTable("(id int(10) NOT NULL AUTO_INCREMENT, term varchar(50) NOT NULL, PRIMARY KEY (id));")
            mTopicRelationshipTable.CreateTable("(topicid_A int(10) NOT NULL, topicid_B int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid_A, topicid_B));")
            mTopicTermTable.CreateTable("(topicid int(10) NOT NULL, termid int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid, termid));")

            return true
        } catch (Exception e) {
            return false
        }
    }

    /**
     * Clears all topic model tables
     */
    void ClearTables() {
        mTopicTable.Clear()
        mTopicTermTable.Clear()
        mDocumentTopicTable.Clear()
        mTermTable.Clear()
        mTopicRelationshipTable.Clear()
    }

    /*
        Getters & Setters
     */
    DocumentTopicTable GetDocumentTopicTable() { return mDocumentTopicTable }
    void SetDocumentTopicTable(DocumentTopicTable mDocTopicTable) { this.mDocumentTopicTable = mDocTopicTable }

    TermTable GetTermTable() { return mTermTable }
    void SetTermTable(TermTable mTermTable) { this.mTermTable = mTermTable }

    TopicTable GetTopicTable() { return mTopicTable }
    void SetTopicTable(TopicTable mTopicTable) { this.mTopicTable = mTopicTable }

    TopicTermTable GetTopicTermTable() { return mTopicTermTable }
    void SetTopicTermTable(TopicTermTable mTopicTermTable) { this.mTopicTermTable = mTopicTermTable }

    TopicRelationshipTable GetTopicRelationshipTable() { return mTopicRelationshipTable }
    void SetTopicRelationshipTable(TopicRelationshipTable mRelationshipTable) { this.mTopicRelationshipTable = mRelationshipTable }

    DiabetesExchangeTable GetDiabetesExchangeTable() { return mDiabetesExchangeTable }
    void SetDiabetesExchangeTable(DiabetesExchangeTable mDiabetesExchangeTable) { this.mDiabetesExchangeTable = mDiabetesExchangeTable }
}
