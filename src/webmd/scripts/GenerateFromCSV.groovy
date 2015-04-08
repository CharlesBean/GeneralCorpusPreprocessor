package webmd.scripts

import model.Term
import model.database.Database
import webmd.WebMDCorpus

/**
 * Created by charles on 4/7/15.
 */

// TODO - To handle topic-term weight....

def mCorpus = new WebMDCorpus()
def mDatabase = new Database(mCorpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")

mDatabase.Connect()

//WebMDGenerator webMDGenerator = new WebMDGenerator...