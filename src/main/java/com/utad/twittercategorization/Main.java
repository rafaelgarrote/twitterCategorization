package com.utad.twittercategorization;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import com.utad.twittercategorization.utils.EntitiesUtils;
import twitter4j.FilterQuery;
import com.utad.twittercategorization.spout.TwitterSpout;
import com.utad.twittercategorization.bolt.HashtagExtractionBolt;
import beans.TweetFieldsCategorization;

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
        String hashtagFilter = "cultura";


        TopologyBuilder builder = new TopologyBuilder();

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(new String[]{hashtagFilter});

        TwitterSpout spout = new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret, tweetFilterQuery);

        HashtagExtractionBolt hashtag = new HashtagExtractionBolt(hashtagFilter);


        //FileWriterBolt fileWriterBolt = new FileWriterBolt("/tmp/contador.txt");
        //LanguageDetectionBolt lenguaje= new LanguageDetectionBolt();
        //RollingCountBolt rollingCountBolt = new RollingCountBolt(9,3);

        builder.setSpout("spoutLeerTwitter",spout,1);
   //     builder.setBolt("lenguaje",lenguaje,1).shuffleGrouping("spoutLeerTwitter");
        builder.setBolt("hashtag",hashtag,1).shuffleGrouping("lenguaje");
        //builder.setBolt("cont",rollingCountBolt, 1).shuffleGrouping("hashtag");
     //   builder.setBolt("cont",rollingCountBolt, 1).fieldsGrouping("hashtag",new Fields("entity"));
       //    builder.setBolt("escribirFichero",fileWriterBolt,1).shuffleGrouping("cont");
//
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



        HashMap myEntities = EntitiesUtils.hagoGet("Bonito viaje en París, ha sido mejor que el de Londres. Me ha gustado el Louvre y los cuadros de Da Vinci");

        for (Object name: myEntities.keySet()){
            String key =name.toString();
            ArrayList value = (ArrayList) myEntities.get(name);
            System.out.println(key + " " + value);
        }
    }
}
