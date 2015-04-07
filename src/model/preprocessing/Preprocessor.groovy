package model.preprocessing

/**
 * Created by charles on 2/19/15.
 */

/**
 * * Performs various model.preprocessing tasks to cleaning data
 */
interface Preprocessor {
    /**
     * * Removes all HTML & XML tags from a content
     * @param content DBO SQL Table content
     * @return
     */
    def RemoveTags(String content)

    /**
     * * Removes all punctuation from a content
     * @param content
     * @return
     */
    def RemoveNonalphabeticals(String content)

    /**
     * * Stems all words in a content 
     * @param content
     * @return
     */
    def Stem(String content)

    /**
     * * Removes all slang terms from a content
     * @param content
     * @return
     */
    def RemoveSlang(String content, File slangTerms)

    /**
     * * Removes all (english) stop-words from a content
     * @param content
     * @return
     */
    def RemoveStopwords(String content, File stopWords)

    /**
     * * Removes all emoticons
     * @param content
     * @return
     */
    def RemoveEmoticons(String content)
}