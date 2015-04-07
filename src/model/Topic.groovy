package model

/**
 * Created by charles on 4/7/15.
 */

/**
 * Class representing a topic
 */
class Topic {

    int mID             // The topic id

    /**
     * Constructor
     * @param id
     */
    Topic(int id) {
        mID = id
    }

    int GetID() { return mID }
    void SetID(int mID) { this.mID = mID }
}
