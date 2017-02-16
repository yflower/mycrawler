package com.jal.crawler;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jal on 2017/2/15.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.1.3"), Arrays.asList(
                MongoCredential.createScramSha1Credential("mongo", "test", "zbbJAL86".toCharArray())));
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = test.getCollection("renren_vars");
        Path path = Paths.get("D:\\test.txt");
        Path path1 = Paths.get("D:\\test1.txt");
        System.out.println(path.toFile().exists());
        System.out.println(path1.toFile().exists());
        OutputStream outputStream = new FileOutputStream(path.toFile());
        OutputStream outputStream1 = new FileOutputStream(path1.toFile());
        collection.find().limit(9).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                String commonLeft = document.get("name").toString() + " ";
                String commonRight =
                        document.get("creditScore").toString() + " " +
                                document.get("age").toString() + " " +
                                document.get("grade").toString() + " " +
                                document.get("marry").toString() + " " +
                                document.get("loadTimes").toString() + " " +
                                document.get("loadSuccessTimes").toString() + " " +
                                document.get("payedOffTimes").toString() + " " +
                                document.get("creditMoney").toString() + " " +
                                document.get("loadAllMoney").toString() + " " +
                                document.get("waitToPayMoney").toString() + " " +
                                document.get("delayMoney").toString() + " " +
                                document.get("delayTimes").toString() + " " +
                                document.get("seriousDelayTimes").toString() + " " +
                                document.get("salary").toString() + " " +
                                document.get("house").toString() + " " +
                                document.get("houseLoad").toString() + " " +
                                document.get("car").toString() + " " +
                                document.get("carLoad").toString() + " ";
                List<Map<String, Object>> toubiao = (List<Map<String, Object>>) document.get("toubiao");
                toubiao.forEach(map -> {
                    String line = commonLeft + map.get("toubiaoren").toString() + " "
                            + map.get("toubiaojiner").toString() + " "
                            + map.get("toubiaoshijian").toString();
                    try {
                        outputStream.write((line + "\r\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    outputStream1.write((commonLeft+commonRight + "\r\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        outputStream.close();
        outputStream1.close();

    }

}
