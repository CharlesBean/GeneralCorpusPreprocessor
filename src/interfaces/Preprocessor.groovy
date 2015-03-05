package interfaces

/**
 * Created by charles on 2/19/15.
 */

/**
 * * Performs various preprocessing tasks to cleaning data
 */
interface Preprocessor {
    /**
     * * Removes all HTML & XML tags from a content
     * @param content DBO SQL Table content
     * @return
     */
    def removeTags(String content)

    /**
     * * Removes all punctuation from a content
     * @param content
     * @return
     */
    def removeNonAlphabeticals(String content)

    /**
     * * Stems all words in a content 
     * @param content
     * @return
     */
    def stem(String content)

    /**
     * * Removes all slang terms from a content
     * @param content
     * @return
     */
    def removeSlang(String content, File slangTerms)

    /**
     * * Removes all (english) stop-words from a content
     * @param content
     * @return
     */
    def removeStopwords(String content, File stopWords)

    /**
     * * Removes all emoticons
     * @param content
     * @return
     */
    def removeEmoticons(String content)
}