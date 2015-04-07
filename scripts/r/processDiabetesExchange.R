## Written by Charles Bean
## February, 2015
## Michigan State University

## Preprocessing done in Groovy

## See This Webpage for a lot of help :)
## http://www.jstatsoft.org/v40/i13/paper

## Takes about 3 minutes when limited to 1000 rows
## Change the CSV Output Path Var!!


#######  Setup  #######

## Loading RMySQL and DBI
library(RMySQL)
library(tm)
library(topicmodels)
library(igraph)
library(slam)

## Creating a connection to the MySQL Local DB
connection <- dbConnect(dbDriver("MySQL"), dbname = "WebMD1", user = "root", password="")

## Queries the db for parsed content
query <- "SELECT cleanedContent FROM WebMD1.diabetes_exchange WHERE cleanedContent IS NOT NULL ORDER BY date LIMIT 2000;"
x <- dbGetQuery(connection, paste(query))

## NOTE : Change me! CSV OuptutPath (use # topics and # documents for naming instead???)
csvOutputPath = "~/Programming/Research/Projects/TopicEvolutionMSU/CTM/Output/diabetesExchange/"




#######  Document-Term Matrix Construction  #######

## Creating a Volatile Corpus with the vector of documents (posts)
webMD <- VCorpus(VectorSource(x[['cleanedContent']]))

## Creating document-term matrix
dtm <- DocumentTermMatrix(
  webMD,
  control = list(
    weight = weightTfIdf,           # Term-frequency weight
    tolower=TRUE,                   # Ensuring everything is lowercase, alphabetical, etc. (min word length 3)
    removeNumbers = TRUE,           
    minWordLength = 3,              
    removePunctuation = TRUE,
    stopwords = TRUE
  ))

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


#######  Display & Output  #######6322222222

## Need this until I understand the [["..."]] notation below...
CTMList <- list(CTM = correlatedTopicModel)

## Most likely topic (page 15)
Topic <- topics(CTMList[["CTM"]], 1)

## 'x' Most common terms
Terms <- terms(CTMList[["CTM"]], 50)  ## 20 terms

## 'y' Most common topics
Terms [, 1:30]  ## 30 topics




# make a data frame with topics as cols, docs as rows and
# cell values as posterior topic distribution for each document
gammaDF <- as.data.frame(correlatedTopicModel@gamma) 
names(gammaDF) <- c(1:topics)

betaDF <- as.data.frame(correlatedTopicModel@beta)
names(betaDF) <- c(1:topics)

# Now for each doc, find just the top-ranked topic   
toptopics <- as.data.frame(cbind(document = row.names(gammaDF), topic = apply(gammaDF, 1, function(x) { names(gammaDF)[which(x==max(x))] })))

# inspect...
toptopics 



# Rows = docs, cols = topics, connection is weight
write.csv(gammaDF, file = paste(csvOutputPath, 'posteriorTopicDistribution1'))

## Write to csv
write.csv(Terms, file = paste(csvOutputPath, 'test'))




#######  Closing  #######

## Closing connection
dbDisconnect(connection)

