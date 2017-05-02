package com.jal.crawler.resource;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class MongoDataFetch implements DataFetch {
    private MongoDataFetch() {
    }

    private MongoClient mongoClient;

    private MongoDatabase mongoDatabase;

    @Override
    public Optional<List<Map<String, Object>>> fetch(String taskTag) {
        if (mongoClient == null || mongoDatabase == null) {
            throw new IllegalStateException("mongo数据库连接信息没有初始化");
        }
        MongoCollection<Document> collection = mongoDatabase.getCollection(taskTag);
        List<Map<String, Object>> result = new ArrayList<>();
        collection.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                result.add(document);
            }
        });

        return Optional.of(result);
    }

    public static MongoDataFetch build(String host, int port, String user, String password, String database) {
        if(StringUtils.isEmpty(host)||port<=0||StringUtils.isEmpty(user)||StringUtils.isEmpty(password)||StringUtils.isEmpty(database)){
            throw new IllegalArgumentException("mongo数据库设置参数错误");
        }
        MongoDataFetch mongoDataFetch = new MongoDataFetch();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(
                MongoCredential.createScramSha1Credential(user, database, password.toCharArray())));

        mongoDataFetch.mongoClient = mongoClient;
        mongoDataFetch.mongoDatabase = mongoClient.getDatabase(database);
        return mongoDataFetch;
    }

}
