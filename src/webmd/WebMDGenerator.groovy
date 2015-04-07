package webmd

import model.database.Database
import model.Term


/**
 * Created by charles on 4/7/15.
 */
class WebMDGenerator {

    def mCorpus = new WebMDCorpus()
    def mDatabase = new Database(mCorpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")

    DocumentTopicTable mDocTopicTable
    TermTable mTermTable
    TopicTable mTopicTable
    TopicTermTable mTopicTermTable
    TopicRelationshipTable mRelationshipTable

    // TODO --- THISSS ->
    // Column x Row     (TODO - see if I can get a CSV that has topics as column, term as row, weight as values!!)
    File mDocumentTopicCSV              // CSV file of document x topic
    File mTopicTermCSV                  // CSV file of topic x term
    File mTopicTermWeightCSV            // CSV file of topic x weight
    File mTopicTopicCSV

    WebMDGenerator(WebMDCorpus corpus, Database database) {
        mCorpus = corpus
        mDatabase = database

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

    def BuildTables(File topicTermCSVFile) {
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

}
