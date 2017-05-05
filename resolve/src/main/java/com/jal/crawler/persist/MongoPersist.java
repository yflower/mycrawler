package com.jal.crawler.persist;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bson.Document;

import java.util.*;

/**
 * Created by home on 2017/1/20.
 */
public class MongoPersist implements Persist {

    private MongoClient mongoClient;

    private MongoCredential mongoCredential;

    public MongoPersist(MongoCredential credential, String host, int port) {
        this.mongoCredential = credential;
        mongoClient = new MongoClient(
                new ServerAddress(host, port),
                Arrays.asList(mongoCredential)
        );
    }

    @Override
    public void persist(String taskTag, Map<String, Object> map) {
        List<String> links = (List<String>) map.getOrDefault("links", new ArrayList<String>());
        mongoClient.getDatabase(mongoCredential.getSource()).getCollection(taskTag + "_links").insertOne(
                new Document(new HashMap<String, Object>() {{
                    put("links", links);
                }})
        );
        map.remove("links");
        mongoClient.getDatabase(mongoCredential.getSource()).getCollection(taskTag + "_vars").insertOne(new Document(map));
        map = null;
    }
}
