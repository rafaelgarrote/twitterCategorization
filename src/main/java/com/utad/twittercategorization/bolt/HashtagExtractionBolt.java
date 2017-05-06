package com.utad.twittercategorization.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import beans.TweetFieldsCategorization;
import org.apache.commons.lang.StringUtils;
import twitter4j.Status;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: qadeer
 * Date: 06.09.13
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class HashtagExtractionBolt extends BaseRichBolt {
    private String hashTagFilter;
    private OutputCollector _collector;

    public HashtagExtractionBolt(String hashTagFilter) {
        this.hashTagFilter = hashTagFilter;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;

    }

    @Override
    public void execute(Tuple tuple) {
        Status status = (Status) tuple.getValueByField("message");

        TweetFieldsCategorization tweetFieldsCategorization = new TweetFieldsCategorization();
        StringTokenizer st = new StringTokenizer(status.getText());

        while (st.hasMoreElements()) {

            String term = (String) st.nextElement();
            if (StringUtils.startsWith(term, "#")){
                String hashtag = term;
                if (hashtag.contains(hashTagFilter)){
                    tweetFieldsCategorization.setIdUserTwitter(status.getUser().getId()+"");
                    tweetFieldsCategorization.setCoordinates(status.getGeoLocation().toString());
                    tweetFieldsCategorization.setHashTag(hashtag);
                    tweetFieldsCategorization.setText(status.getText());
                }
            }
        }

        _collector.emit(new Values(tweetFieldsCategorization));
        // Confirm that this tuple has been treated.
        _collector.ack(tuple);

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("entity","text"));
    }

    @Override
    public void cleanup() {
        super.cleanup();

    }
}
