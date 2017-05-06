package beans;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by agustin on 5/05/17.
 */
public class TweetFieldsCategorization implements java.io.Serializable {
    private String idUserTwitter;
    private String coordinates;
    private String hashTag;
    private String text;
    private HashMap<String,ArrayList<String>> entities;

    private Status tweet;

    public String getIdUserTwitter() {
        return idUserTwitter;
    }

    public void setIdUserTwitter(String idUserTwitter) {
        this.idUserTwitter = idUserTwitter;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashMap<String, ArrayList<String>> getEntities() {
        return entities;
    }

    public void setEntities(HashMap<String, ArrayList<String>> entities) {
        this.entities = entities;
    }

    public Status getTweet() {
        return tweet;
    }

    public void setTweet(Status tweet) {
        this.tweet = tweet;
    }
}
