package com.utad.twittercategorization.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import beans.TweetFieldsCategorization;
import com.utad.twittercategorization.utils.GeoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by utad on 6/05/17.
 */
public class GeoBolt extends BaseRichBolt {

    private OutputCollector _collector;

    //Location

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        _collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String location = "";

        TweetFieldsCategorization tweet = (TweetFieldsCategorization) tuple.getValueByField("tweet");
        if(tweet.getEntities() != null) {
            HashMap<String, ArrayList<String>> entities = tweet.getEntities();

            for (String key : entities.keySet()) {
                if (entities.get(key).contains("Location")) {
                    location = key;
                    break;
                }
            }

            if (!location.equals("")) {
                String latLong = GeoUtils.getCity(location);
                if (!latLong.equals("")) {
                    tweet.setCoordinates(latLong);
                    _collector.emit(tuple, new Values(tweet));
                    _collector.ack(tuple);
                }
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("tweet"));
    }

}
