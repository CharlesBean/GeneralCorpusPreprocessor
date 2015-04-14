import webmd.DiabetesExchangeTable
import webmd.DocumentTopicTable
import webmd.TermTable
import webmd.TopicRelationshipTable
import webmd.TopicTable
import webmd.TopicTermTable
import webmd.WebMDCorpus
import model.database.Database
import webmd.WebMDDatabase

/**
 * Created by charles on 4/1/15.
 */

def mCorpus = new WebMDCorpus()
def mDatabase = new WebMDDatabase(mCorpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")

mDatabase.Connect()

DocumentTopicTable docTopicTable = new DocumentTopicTable(mDatabase, 'document_label')
TermTable termTable = new TermTable(mDatabase, 'term')
TopicTable topicTable = new TopicTable(mDatabase, 'topic')
TopicRelationshipTable relationshipTable = new TopicRelationshipTable(mDatabase, 'topic_relationship')
TopicTermTable topicTermTable = new TopicTermTable(mDatabase, 'topic_term')
DiabetesExchangeTable diabetesExchangeTable = new DiabetesExchangeTable(mDatabase, 'diabetes_exchange')

//docTopicTable.RemoveRow('test')


//termTable.AddRow('top ')
//termTable.AddRow() // fails


//topicTable.AddRow()


//relationshipTable.AddRow(1, 3)
//relationshipTable.AddRow(1, 2)
//relationshipTable.RemoveRow 1
//relationshipTable.AddRow(1, 2, .5)


//topicTermTable.AddRow(1, 3, 0.2)
//topicTermTable.RemoveRow(1, 2)


//diabetesExchangeTable.AddRow("9900_top", "9900", -1, "Test", 'Charles Bean', new Date(), "This is a test add")
//diabetesExchangeTable.RemoveRow("9900_top")
//diabetesExchangeTable.CreateCleanedContentColumn()