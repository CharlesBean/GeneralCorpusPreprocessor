package webmd

import model.database.Database

/**
 * Created by charles on 4/7/15.
 */
class WebMDGenerator {

    WebMDCorpus mCorpus
    Database mDatabase
    WebMDPreprocessor mPreprocessor

    DocumentTopicTable mDocTopicTable
    TermTable mTermTable
    TopicTable mTopicTable
    TopicTermTable mTopicTermTable
    TopicRelationshipTable mRelationshipTable
    DiabetesExchangeTable mDiabetesExchangeTable

    File mCSVTopicDocument                      // CSV file of [topic x document] with weight
    File mCSVTermTopic                          // CSV file of [topic x term] weight weight
    File mCSVTopicTopic                         // CSV file of [topic x topic] with weight

    File mPropertiesFile                        // TODO - implement properties file loading (somewhere...)

    boolean mCreateTables = false               // Should we create topic model tables
    boolean mClearTables = true                 // Should we clear tables before construction

    /**
     * Constructor
     *
     * @param corpus
     * @param database
     */
    WebMDGenerator(WebMDCorpus corpus, Database database, WebMDPreprocessor preprocessor) {
        mCorpus = corpus
        mDatabase = database
        mPreprocessor = preprocessor

        mDocTopicTable = new DocumentTopicTable(mDatabase, 'document_label')
        mTermTable = new TermTable(mDatabase, 'term')
        mTopicTable = new TopicTable(mDatabase, 'topic')
        mTopicTermTable = new TopicTermTable(mDatabase, 'topic_term')
        mRelationshipTable = new TopicRelationshipTable(mDatabase, 'topic_relationship')
        mDiabetesExchangeTable = new DiabetesExchangeTable(mDatabase, 'diabetes_exchange')
    }

    /**
     * Sets our member files. Each is a csv file representing a matrix - with the first term as the column,
     * and the second as the row.
     *
     * @param topicDocumentCSV CSV file of [topic x document] with weight
     * @param termTopicCSV CSV file of [term x topic] weight weight
     * @param topicTopicCSV CSV file of [topic x topic] with weight
     * @return
     */
    void SetFiles(File topicDocumentCSV, File termTopicCSV, File topicTopicCSV) {
        mCSVTopicDocument = topicDocumentCSV
        mCSVTermTopic = termTopicCSV
        mCSVTopicTopic = topicTopicCSV
    }

    /**
     * Creates and fills the topic model tables
     *
     * @return true on success
     */
    def GenerateTopicTables() {
        // Create and clearing the tables if necessary
        if (mCreateTables) CreateTopicModelTables()
        if (mClearTables) ClearTables()

        ReadTopicDocumentMatrix()
        ReadTermTopicMatrix()
        ReadTopicTopicMatrix()
    }

    /**
     * Takes the content column of our table, cleans each entry, and then
     * inserts into cleaned_content column
     *
     * @param removeEmptyContent flag to remove empty content
     * @return true if successful
     */
    boolean CleanContent(boolean removeEmptyContent=false) {
        // If we create a cleaned_content column, or if it already exists
        if (mDiabetesExchangeTable.CreateCleanedContentColumn()) {
            return mDiabetesExchangeTable.CleanContent(mPreprocessor, true)
        }

        return false;
    }

    /**
     * Creates our topic model tables. Paste queries below.
     *
     * @return true if successful
     */
    boolean CreateTopicModelTables() {
        try{
            mDatabase.CreateTable("document_label", "(post_id varchar(25) NOT NULL, topic_id int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (post_id, topic_id));")
            mDatabase.CreateTable("topic", "(id int(10) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id));")
            mDatabase.CreateTable("term", "(id int(10) NOT NULL AUTO_INCREMENT, term varchar(50) NOT NULL, PRIMARY KEY (id));")
            mDatabase.CreateTable("topic_relationship", "(topicid_A int(10) NOT NULL, topicid_B int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid_A, topicid_B));")
            mDatabase.CreateTable("topic_term", "(topicid int(10) NOT NULL, termid int(10) NOT NULL, weight double NOT NULL, PRIMARY KEY (topicid, termid));")

            return true
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clears all topic model tables
     */
    void ClearTables() {
        mTopicTable.Clear()
        mTopicTermTable.Clear()
        mDocTopicTable.Clear()
        mTermTable.Clear()
        mRelationshipTable.Clear()
    }

    /**
     * Reads a csv file matrix:
     *      column : topic
     *      row : document
     *      value : weight
     */
    void ReadTopicDocumentMatrix() {
        /*
            Reading topicDocumentMatrix.csv (inserting into 'document_label' and 'topic' tables)
         */

        // First, lets read the topic x document matrix rows into a list (strings)
        List topicDocumentLines = mCSVTopicDocument.readLines()

        // Next, get the list of topics, by creating a list of topics, and popping the front index via [1..size()]
        List<String> topicsList = topicDocumentLines[0].split(',')[1..<topicDocumentLines[0].split(',').size()]

        // Now lets add the topics to our 'topic' table (null entries - auto increment PK)
        1.upto(topicsList.size(), {
            mTopicTable.AddRow()
            println "Added - Topic $it"
        })

        /* Now, we look at the rest of the lines in the csv file as a list. Their index is their topic. The first entry,
            of index 0, is the document's 'uniqueID'. We will create a list of lines, iterate over each line, and add
            rows to our relational table - 'document_label'
         */
        List<String> tdWeightLines = topicDocumentLines[1..<topicDocumentLines.size()]
        tdWeightLines.each { rowString ->
            String uniqueID = rowString.split(',').first().replace('\"', '').toString()

            /* Lets get the rest of the row (weights), and create our associations. The index of the row is the topic,
                and we already have the rowname (uniqueID).
             */
            List<String> row = rowString.split(',')[1..<rowString.split(',').size()]
            row.eachWithIndex { weight, index ->
                index = index.toInteger() + 1
                mDocTopicTable.AddRow(uniqueID.toString(), index.toInteger(), weight.toDouble())
                println "Added - DocumentLabel ($uniqueID, $index, $weight)"
            }
        }
    }

    /**
     * Reads a csv file matrix:
     *      column : term
     *      row : topic
     *      value : log_10(weight)
     */
    void ReadTermTopicMatrix () {
        /*
            Reading termTopicMatrix.csv (inserting into 'topic_term' and 'term' tables
         */

        // Read lines
        List termTopicLines = mCSVTermTopic.readLines()

        // Next, lets get the list of words/terms, that are our column names
        List<String> termList = termTopicLines[0].split(',')[1..<termTopicLines[0].split(',').size()]

        // Adding the terms to the 'term' table
        termList.each { term ->
            // Replacing any quotes (they are strings aready...)
            term = term.replace('\"', '')

            if (term.size() < 50){
                mTermTable.AddRow(term)
                println "Added - Term $term"
            }
        }

        /*
            For each row, add to 'topic_term' table, using the first index as the 'topic_id' and the rest as weights. We
            will add connections from topics to terms.
         */
        List<String> termTopicWeightLines = termTopicLines[1..<termTopicLines.size()]
        termTopicWeightLines.each { rowString ->
            String topicID = rowString.split(',').first().replace('\"', '').toInteger()

            /* Lets get the rest of the row (weights), and create our associations. The index of the row is the index we
                can use to get the term (from above list), and will also be the termID if we clear our tables. Finally,
                the value is the weight logarithmized, so we will have to inverse that when we analyze (10^weight).
             */
            List<String> row = rowString.split(',')[1..<rowString.split(',').size()]
            row.eachWithIndex { logWeight, index ->
                String term = termList[index].replace('\"','')
                int termID = mTermTable.GetID(term)

                // If we got a result
                if (termID != -1) {
                    mTopicTermTable.AddRow(topicID.toInteger(), termID.toInteger(), logWeight.toDouble())
                    println "Added - TopicTerm ($topicID, $term, $logWeight)"
                }
            }
        }
    }

    /**
     * Reads a csv matrix:
     *      column : topic
     *      row : topic
     *      value : boolean (is connected)
     */
    void ReadTopicTopicMatrix() {
        /*
            Reading topicTopicMatrix.csv (inserting into the 'topic_relationship' table)
         */

        List topicTopicLines = mCSVTopicTopic.readLines()

        // Omitting first row, and putting rest of lines into a list
        List<String> topicTopicWeightLines = topicTopicLines[1..<topicTopicLines.size()]
        topicTopicWeightLines.each { rowString ->
            String aTopicID = rowString.split(',').first().replace('\"', '').toInteger()

            /* For each connection (or adjacency) in the row, if it is "TRUE", then we can create connection between
                the index/column (topicA) and the row (topicB). Inserting into 'topic_relationship' table.
             */
            List<String> row = rowString.split(',')[1..<rowString.split(',').size()]
            row.eachWithIndex { connected, index ->
                index = index.toInteger() + 1

                if (connected == "TRUE") {
                    mRelationshipTable.AddRow(aTopicID.toInteger(), index.toInteger() + 1, 1.0)
                    println "Added - TopicRelationship ($aTopicID, $index, $connected)"
                }
            }
        }
    }
}
