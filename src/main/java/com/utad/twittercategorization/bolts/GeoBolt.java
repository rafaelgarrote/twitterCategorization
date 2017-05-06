package com.utad.twittercategorization.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import beans.TweetFieldsCategorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by utad on 6/05/17.
 */
public class GeoBolt extends BaseRichBolt {

    private OutputCollector _collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        TweetFieldsCategorization tweet = (TweetFieldsCategorization) tuple.getValueByField("tweet");
        HashMap<String, ArrayList<String>> entities = tweet.getEntities();


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("tweet"));
    }

}
