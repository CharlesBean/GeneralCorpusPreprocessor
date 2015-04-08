package webmd.scripts

import model.database.Database
import model.preprocessing.Stemmer
import webmd.WebMDCorpus
import webmd.WebMDGenerator
import webmd.WebMDPreprocessor

/**
 * Created by Charles Bean on 4/8/15.
 */
def corpus = new WebMDCorpus()

def database = new Database(corpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")
database.Connect()

def webMDPreprocessor = new WebMDPreprocessor(database.GetDBO(), new Stemmer())
def webMDGenerator = new WebMDGenerator(corpus, database, webMDPreprocessor);

//webMDGenerator.CreatedCleanedContentColumn()