package com.utad.twittercategorization;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import com.utad.twittercategorization.bolts.EntitiesBolt;
import com.utad.twittercategorization.bolts.FileWriterBolt;
import com.utad.twittercategorization.bolts.GeoBolt;
import com.utad.twittercategorization.utils.EntitiesUtils;
import twitter4j.FilterQuery;
import com.utad.twittercategorization.spouts.TwitterSpout;
import com.utad.twittercategorization.bolts.HashtagExtractionBolt;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by utad on 5/05/17.
 */

public class Main {

    public static void main(String args[]) throws Exception{

        String consumerKey = "";
        String consumerSecret = "";
        String accessToken = "";
        String accessTokenSecret = "";

        /**************** SETUP ****************/
        String remoteClusterTopologyName = null;
        if (args != null) {
            if (args.length == 1) {
                remoteClusterTopologyName = args[0];
            }
            // If credentials are provided as commandline arguments
            else if (args.length == 4) {
                consumerKey = args[0];
                consumerSecret = args[1];
                accessToken = args[2];
                accessTokenSecret = args[3];
            }

        }
        /****************       ****************/
        String hashtagFilter = "FeriaDeUteros";


        TopologyBuilder builder = new TopologyBuilder();

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(new String[]{hashtagFilter});

        TwitterSpout spout = new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret, tweetFilterQuery);

        HashtagExtractionBolt hashtag = new HashtagExtractionBolt(hashtagFilter);
        EntitiesBolt entities = new EntitiesBolt();
        GeoBolt geo = new GeoBolt();
        FileWriterBolt writer = new FileWriterBolt("/tmp/tweet");

        builder.setSpout("spoutLeerTwitter",spout,1);
        builder.setBolt("hashtag",hashtag,1).shuffleGrouping("spoutLeerTwitter");
        builder.setBolt("entities", entities).shuffleGrouping("hashtag");
        builder.setBolt("geo", geo).shuffleGrouping("entities");
        builder.setBolt("writer", writer).shuffleGrouping("geo");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("twitter-fun", conf, builder.createTopology());

            Thread.sleep(460000);

            cluster.shutdown();
        }

    }
}
