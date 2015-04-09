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

    File mTopicDocumentCSV              // CSV file of [topic x document] with weight
    File mTermTopicCSV                  // CSV file of [topic x term] weight weight
    File mTopicTopicCSV                 // CSV file of [topic x topic] with weight

    File mPropertiesFile                // TODO - implement properties file loading (somewhere...)

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
     * @param termTopicCSV CSV file of [topic x term] weight weight
     * @param topicTopicCSV CSV file of [topic x topic] with weight
     * @return
     */
    def SetFiles(File topicDocumentCSV, File termTopicCSV, File topicTopicCSV) {
        mTopicDocumentCSV = topicDocumentCSV
        mTermTopicCSV = termTopicCSV
        mTopicTopicCSV = topicTopicCSV
    }

    /**
     * Creates and fills the topic model tables
     *
     * @return true on success
     */
    def BuildTopicModelTables() {
        /*
            Processing
         */

        // First, lets read all lines into a list
        def lines = topicTermCSVFile.readLines()

        // Next, get the list of topics, by creating a list of topics, popping the front (corner), and then size()
        def topicsList = lines[0].split(',')[1..<lines[0].split(',').size()]

        /*
            Creating Topics Table
         */
        1.upto(topicsList.size(), {
            def added = this.mTopic
            println it
        })

        /* Now, look at the rest of the lines in the csv file as a list. Their index is their topic. We
            will create a list of lines, iterate over each line, and add new topics, terms, and topicTerms.
         */
        def termLines = lines[1..<lines.size()]
        termLines.each { termLine ->
            def termsList = termLine.split(',')[1..<termLine.split(',').size()]

            // For each term in the line (omitting first index)
            termsList.eachWithIndex { term, index ->

            }
        }
    }

    /**
     * Takes the content column of our table, cleans each entry, and then
     * inserts into cleaned_content column
     *
     * @param removeEmptyContent flag to remove empty content
     * @return true if successful
     */
    def CleanContent(boolean removeEmptyContent=false) {
        // If we create a cleaned_content column, or if it already exists
        if (mDiabetesExchangeTable.CreateCleanedContentColumn()) {
            return mDiabetesExchangeTable.CleanContent(mPreprocessor, true)
        }

        return false;
    }
}
