import webmd.WebMDCorpus
import model.database.Database
import webmd.DocumentTopicTable

/**
 * Created by charles on 4/1/15.
 */
class DocumentTopicTableTest {

    private mCorpus = new WebMDCorpus()
    private mDatabase = new Database(mCorpus, "jdbc:mysql://localhost:3306/", "root", "", "WebMD1")

    void TestConstructor() {
        mDatabase.Connect()

        DocumentTopicTable docTopicTable = new DocumentTopicTable(mDatabase, 'document_label')

        //assert .. TODO - implement test classes
    }

}

