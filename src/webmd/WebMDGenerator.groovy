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

    File mDocumentTopicCSV              // CSV file of [document x topic] with weight
    File mTopicTermCSV                  // CSV file of [topic x term] weight weight
    File mTopicTopicCSV                 // CSV file of [topic x topic] with weight

    /**
     * Constructor
     *
     * @param corpus
     * @param database
     */
    WebMDGenerator(WebMDCorpus corpus, Database database, Preprocessor preprocessor) {
        mCorpus = corpus
        mDatabase = database
        mPreprocessor = preprocessor

        mDocTopicTable = new DocumentTopicTable(mDatabase, 'document_label')
        mTermTable = new TermTable(mDatabase, 'term')
        mTopicTable = new TopicTable(mDatabase, 'topic')
        mTopicTermTable = new TopicTermTable(mDatabase, 'topic_term')
        mRelationshipTable = new TopicRelationshipTable(mDatabase, 'topic_relationship')
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

    def AddCleanedContentColumn(String tableName, boolean removeEmptyContent) {
        int added = 0;
        int deleted = 0;

        tableName = mDatabase.GetDatabaseName() + '.' + tableName;

        sql.eachRow("SELECT * FROM $tableName;") {
            def content = it['content']
            def uniqueID = it['uniqueID']

            content = content.toString().toLowerCase()
            content = mPreprocessor.RemoveTags(content)
            content = mPreprocessor.RemoveSlang(content, new File("../text/slangTerms.txt"))
            content = mPreprocessor.RemoveStopwords(content, new File("../text/stopwords/english/ranks-nl/stopwords_english_1.txt"))
            content = mPreprocessor.RemoveNonalphabeticals(content)
            content = mPreprocessor.Stem(content)

            // If this row is of no interest to us... (empty)
            if (content == null || content.size() == 0) {
                if (removeEmptyContent) {
                    // Delete the row from the table
                    sql.execute("DELETE FROM WebMD1.diabetes_exchange WHERE uniqueID=$uniqueID;")
                }

                println "$uniqueID \t\t Deleted \n"
                deleted++
            }
            else
            {
                // Update the cleanedContent column
                sql.executeUpdate("UPDATE WebMD1.diabetes_exchange SET cleaned_content=$content WHERE uniqueID=$uniqueID;")
            }
        }

        println "\n -- Added: $added \n -- Deleted: $deleted"
    }
}
