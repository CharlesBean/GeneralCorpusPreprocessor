import webmd.DocumentTopicTable
import webmd.TermTable
import webmd.TopicRelationshipTable
import webmd.TopicTable
import webmd.TopicTermTable
import webmd.WebMDCorpus
import model.database.Database

/**
 * Created by charles on 4/1/15.
 */

def mCorpus = new WebMDCorpus()
def mDatabase = new Database(mCorpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")

mDatabase.Connect()

DocumentTopicTable docTopicTable = new DocumentTopicTable(mDatabase, 'document_label')
TermTable termTable = new TermTable(mDatabase, 'term')
TopicTable topicTable = new TopicTable(mDatabase, 'topic')
TopicRelationshipTable relationshipTable = new TopicRelationshipTable(mDatabase, 'topic_relationship')
TopicTermTable topicTermTable = new TopicTermTable(mDatabase, 'topic_term')


//docTopicTable.RemoveRow('test')


termTable.AddRow('top ')
//termTable.AddRow() // fails


//topicTable.AddRow()


//relationshipTable.AddRow(1, 3)
//relationshipTable.AddRow(1, 2)
//relationshipTable.RemoveRow 1
//relationshipTable.AddRow(1, 2, .5)


//topicTermTable.AddRow(1, 3, 0.2)
topicTermTable.RemoveRow(1, 2)