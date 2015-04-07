package model

/**
 * Created by charles on 4/7/15.
 */

/**
 * Class representing a term/word
 */
class Term {

    int mID                 // Primary key
    String mTerm            // The word (VARCHAR(50))

    /**
     * Constuctor
     * @param mID
     * @param mTerm
     */
    Term(int id, String term) {
        mID = id
        mTerm = term
    }

    String GetTerm() { return mTerm }
    void SetTerm(String mTerm) { this.mTerm = mTerm }
    int GetID() { return mID }
    void SetID(int mID) { this.mID = mID }
}
