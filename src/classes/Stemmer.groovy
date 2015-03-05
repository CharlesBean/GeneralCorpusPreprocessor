package classes

import org.tartarus.snowball.EnglishSnowballStemmerFactory

/**
 * Created by charles on 2/19/15.
 */

public class Stemmer {
    def snowballStem = {
        try {
            return EnglishSnowballStemmerFactory.getInstance().process(it)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}