package webmd.scripts

import model.Term
import model.database.Database
import model.preprocessing.Stemmer
import webmd.WebMDCorpus
import webmd.WebMDGenerator
import webmd.WebMDPreprocessor

/**
 * Created by charles on 4/7/15.
 */

def corpus = new WebMDCorpus()
def database = new Database(corpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")
def preprocessor = new WebMDPreprocessor(database.GetDBO(), new Stemmer())
database.Connect()

WebMDGenerator webMDGenerator = new WebMDGenerator(corpus, database, preprocessor)

File topicDocumentCSV = new File("../../../text/csv/topicDocumentMatrix.csv")
File termTopicCSV = new File("../../../text/csv/termTopicMatrix.csv")
File topicTopicCSV = new File("../../../text/csv/topicTopicMatrix.csv")

webMDGenerator.SetFiles(topicDocumentCSV, termTopicCSV, topicTopicCSV)

