package com.jal.crawler.fetch;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by jianganlan on 2017/5/2.
 */
public class MongoLinkFetch implements LinkFetch {
    private MongoLinkFetch() {
    }

    private MongoClient mongoClient;

    private MongoDatabase mongoDatabase;

    @Override
    public Optional<List<String>> fetch(String taskTag) {
        if (mongoClient == null || mongoDatabase == null) {
            throw new IllegalStateException("mongo数据库连接信息没有初始化");
        }
        MongoCollection<Document> collection = mongoDatabase.getCollection(taskTag + "_links");
        Document document = collection.findOneAndDelete(new BsonDocument());

        if (document == null) {
            return Optional.empty();
        }

        return Optional.of(document.get("links", List.class));
    }

    public static MongoLinkFetch build(String host, int port, String user, String password, String database) {
        if (StringUtils.isEmpty(host) || port <= 0 || StringUtils.isEmpty(user) || StringUtils.isEmpty(password) || StringUtils.isEmpty(database)) {
            throw new IllegalArgumentException("mongo数据库设置参数错误");
        }
        MongoLinkFetch mongoLinkFetch = new MongoLinkFetch();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(
                MongoCredential.createScramSha1Credential(user, database, password.toCharArray())));

        mongoLinkFetch.mongoClient = mongoClient;
        mongoLinkFetch.mongoDatabase = mongoClient.getDatabase(database);
        return mongoLinkFetch;
    }

}
