package webmd

import groovy.sql.Sql
import model.preprocessing.Preprocessor
import model.preprocessing.Stemmer

/**
 * Created by charles on 2/19/15.
 */
class WebMDPreprocessor implements Preprocessor{

    def mStemmer = new Stemmer();               // Stemming object that does any lifting
    def mDBO;                                   // Our database object

    /**
     * * Non-default Constructor
     * @param DBO - A database object
     * @return 
     */
    def WebMDPreprocessor(Sql DBO, Stemmer stemmer) {
        mDBO = DBO;
        mStemmer = stemmer;
    }
    
    /**
     * * Removes all HTML & XML tags
     * @param string - DBO SQL Table row
     * @return
     */
    String RemoveTags(String content) {
        def tagRegex = ~"<.*?>"
        def matcher = (content =~ tagRegex)
        
        // Replacing with null string
        def taglessContent = matcher.replaceAll("")
        
        return taglessContent
    }

    /**
     * * Removes all non-alphabetical content
     * @param string - DBO SQL Table row
     * @return
     */
    def RemoveNonalphabeticals(String content) {
        def nonAlphabeticalRegex = ~"[^a-zA-Z ]"
        def matcher = (content =~ nonAlphabeticalRegex)

        // Replacing with null string
        def alphabeticalContent = matcher.replaceAll("")

        return alphabeticalContent
    }

    /**
     * * Stems all words
     * @param string - DBO SQL Table row
     * @return
     */
    def Stem(String content) {
        String stemmedContent = ""
        
        for (word in content.split(" ")) {
            if (word != null && word != "") {
                stemmedContent += mStemmer.SnowballStem(word) + " "
            }
        }
        
        return stemmedContent
    }

    /**
     * * Removes all slang terms
     * @param string - DBO SQL Table row
     * @return
     */
    def RemoveSlang(String content, File slangTerms) {
        def lines =  slangTerms.readLines()
        def slangMap = new HashMap()
        def cleanedContent = ""
        
        // Creating slang map (slang->meaning)
        for (line in lines) {
            line = line.split("->")
            slangMap.put(line[0], line[1])
        }
        
        // If a word is a key in slangMap, add its value instead
        for (word in content.split(" ")) {
            if (word in slangMap.keySet()) {
                cleanedContent += slangMap[word] + " "
            }
            else {
                cleanedContent += word + " "
            }
        }
        
        return cleanedContent
    }

    /**
     * * Removes all (english) stop-words
     * @param string - DBO SQL Table row
     * @return
     */
    def RemoveStopwords(String content, File stopWords) {
        def lines = stopWords.readLines()
        def cleanedContent = ""

        // If a word is a stopword, replace it with null string
        for (word in content.split(" ")) {
            if (word in lines) {
                cleanedContent += ""
            }
            else {
                cleanedContent += word + " "
            }
        }
        
        return cleanedContent
    }

    /**
     * * Removes all emoticons
     * @param content
     * @return
     */
    def RemoveEmoticons(String content) {
        
    }


}

