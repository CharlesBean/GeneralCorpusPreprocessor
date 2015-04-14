## Written by Charles Bean
## February, 2015
## Michigan State University

## Preprocessing done in Groovy

## See This Webpage for a lot of help :)
## http://www.jstatsoft.org/v40/i13/paper

## Takes about 3 minutes when limited to 2000 rows (8GB Ram, i7)
## Change the CSV Output Path Var!!



#######  Setup  #######

## Loading RMySQL and DBI
library(RMySQL)
library(tm)
library(topicmodels)
library(igraph)
library(slam)
library(lasso2)

## Creating a connection to the MySQL Local DB
connection <- dbConnect(dbDriver("MySQL"), dbname = "WebMD1", user = "root", password="")

## Queries the db for parsed content
query <- "SELECT * FROM WebMD1.diabetes_exchange WHERE cleanedContent IS NOT NULL ORDER BY date DESC LIMIT 2000;"
table <- dbGetQuery(connection, paste(query))

## NOTE : Change me! CSV OuptutPath (use # topics and # documents for naming instead???)
csvOutputPathDesktop = "~/Programming/Research/Projects/TopicEvolutionMSU/GeneralCorpusPreprocessor/text/csv/"
csvOutputPathMac = "/Users/chuck1/Chas/Programming/Research/TopicEvolutionMSU/GeneralCorpusPreprocessor/GeneralCorpusPreprocessor/text/csv/"

csvOutputPath = csvOutputPathMac



#######  Document-Term Matrix Construction  #######

## Creating some lists
uniqueIDList <- table['uniqueID']
contentListt <- table['cleanedContent']
contentDocumentMatrix <- table['cleanedContent']
documentContentMatrix <- t(contentDocumentMatrix)

## Creating a Volatile Corpus with the vector of documents (posts) //table[['cleanedContent']]
webMD <- VCorpus(VectorSource(as.vector(documentContentMatrix)))

## Creating document-term matrix
dtm <- DocumentTermMatrix(
  webMD,
  control = list(
    weight = weightTf,              # Term-frequency weight
    tolower=TRUE,                   # Ensuring everything is lowercase, alphabetical, etc. (min word length 3)
    removeNumbers = TRUE,           
    minWordLength = 3,              
    removePunctuation = TRUE,
    stopwords = TRUE
  ))

# Set column names (transpose to fit) - for some reason the dtm is transposed right now..
rownames(dtm) <- t(uniqueIDList['uniqueID'])        

## term frequency-inverse document frequency (tf-idf) calculation (function)
term_tfidf <- tapply(dtm$v/row_sums(dtm)[dtm$i], dtm$j, mean) * log2(nDocs(dtm)/col_sums(dtm > 0))
summary(term_tfidf)

## Including terms with minimum tf-idf value of .09 (a little under our median)
dtm <- dtm[, term_tfidf >= 0.09]
dtm <- dtm[row_sums(dtm) > 0,]
summary(col_sums(dtm))

## Removing any rows without non-zero indices
rowTotals <- rollup(dtm, 2, na.rm=TRUE, FUN = sum)  # Matrix of Terms per row
rowSums <- (as.matrix(rowTotals))                   # Calculating the totals
dtm   <- dtm[rowSums> 0, ]                          # Remove all docs without words



#######  Creating a Correlated-Topic Model  #######

## Running CTM (what to choose for k-topics?) - Check out k-fold cross-validation
topics  <- 30
SEED    <- 2015
correlatedTopicModel <- CTM(dtm, k = topics, control = list(seed = SEED, var = list(tol = 10^-4), em = list(tol = 10^-3)))



#######  Display & Output  #######

terms <- as.vector(correlatedTopicModel@terms)
documents <- as.vector(correlatedTopicModel@documents)

## data frame with terms as columns, and topics as terms/words
## value is the weight logarithmized (log base 10?)
termTopicMatrix <- as.data.frame(correlatedTopicModel@beta)
names(termTopicMatrix) <- c(1:topics)

## make a data frame with topics as cols, docs as rows and
## cell values as weight
topicDocumentMatrix <- as.data.frame(correlatedTopicModel@gamma)
names(topicDocumentMatrix) <- c(1:topics)

## adjacency matrix of topics to topics, no weighting
topicTopicMatrix <- as.data.frame(build_graph(correlatedTopicModel, 1))

## changing column names for termTopic and rownames for topicDocument [col-Row]
colnames(termTopicMatrix) <- terms
row.names(topicDocumentMatrix) <- documents

# Writing
write.csv(termTopicMatrix, file = paste(csvOutputPath, 'termTopicMatrix.csv', sep = ''))
write.csv(topicDocumentMatrix, file = paste(csvOutputPath, 'topicDocumentMatrix.csv', sep = ''))
write.csv(topicTopicMatrix, file = paste(csvOutputPath, 'topicTopicMatrix.csv', sep = ''))



#######  Closing  #######

## Closing connection
dbDisconnect(connection)

