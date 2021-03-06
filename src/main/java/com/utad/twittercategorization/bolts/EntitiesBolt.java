package com.utad.twittercategorization.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import beans.TweetFieldsCategorization;
import com.utad.twittercategorization.utils.EntitiesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by utad on 6/05/17.
 */
public class EntitiesBolt extends BaseRichBolt {
    OutputCollector _collector;

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            TweetFieldsCategorization tweet = (TweetFieldsCategorization) tuple.getValueByField("tweet");

            //Del object extraer el TweetText (getText)
            String tweetText = tweet.getText();

            try {
                HashMap myEntities = EntitiesUtils.hagoGet(tweetText);
                //Añadir el Hashmap al objeto tweet
                tweet.setEntities(myEntities);
            } catch (Exception e) {
                e.printStackTrace();
            }

            _collector.emit(tuple, new Values(tweet));
            _collector.ack(tuple);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tweet"));
    }

}
