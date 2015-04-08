package webmd

import groovy.sql.Sql
import model.database.Database
import model.preprocessing.Preprocessor
import webmd.WebMDPreprocessor


import java.sql.SQLException


/**
 * Created by charles on 4/7/15.
 */
class WebMDGenerator {

    def mCorpus
    def mDatabase
    def mPreprocessor

    DocumentTopicTable mDocTopicTable
    TermTable mTermTable
    TopicTable mTopicTable
    TopicTermTable mTopicTermTable
    TopicRelationshipTable mRelationshipTable
    DiabetesExchangeTable mDiabetesExchangeTable

    File mDocumentTopicCSV              // CSV file of [document x topic] with weight
    File mTopicTermCSV                  // CSV file of [topic x term] weight weight
    File mTopicTopicCSV                 // CSV file of [topic x topic] with weight

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

    // TODO - remove null
    def SetFiles(File documentTopicCSV=null, File topicTermCSV, File topicTermWeightCSV=null, File topicTopicCSV=null) {
        mTopicTermCSV = topicTermCSV
    }

    def BuildTopicModelTables(File topicTermCSVFile) {
        // TODO - To handle topic-term weight....

        mDatabase.Connect()
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


        def test = true
    }

    def CleanContent(boolean removeEmptyContent=false) {
        // If we create a cleaned_content column, or if it already exists
        if (mDiabetesExchangeTable.CreateCleanedContentColumn()) {
            mDiabetesExchangeTable.CleanContent(mPreprocessor, true)
        }
    }
}
