package com.jal.crawler;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.jndi.MongoClientFactory;
import org.bson.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
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
        MongoClient mongoClient=new MongoClient(new ServerAddress("192.168.1.3"), Arrays.asList(
                MongoCredential.createScramSha1Credential("mongo","test","zbbJAL86".toCharArray())));
        MongoDatabase test = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = test.getCollection("renren_vars");
        System.out.println();
        Path path= Paths.get("C:\\test.txt");
        OutputStream outputStream=new FileOutputStream(path.toFile());
        collection.find().limit(9).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                    String commonLeft=document.get("name").toString()+" ";
                    String commonRight=
                            document.get("creditScore").toString()+" "+
                            document.get("age").toString()+" "+
                            document.get("grade").toString()+" "+
                            document.get("marry").toString()+" "+
                            document.get("loadTimes").toString()+" "+
                            document.get("loadSuccessTimes").toString()+" "+
                            document.get("payedOffTimes").toString()+" "+
                            document.get("creditMoney").toString()+" "+
                            document.get("loadAllMoney").toString()+" "+
                            document.get("waitToPayMoney").toString()+" "+
                            document.get("delayMoney").toString()+" "+
                            document.get("delayTimes").toString()+" "+
                            document.get("seriousDelayTimes").toString()+" "+
                            document.get("salary").toString()+" "+
                            document.get("house").toString()+" "+
                            document.get("houseLoad").toString()+" "+
                            document.get("car").toString()+" "+
                            document.get("carLoad").toString()+" ";
                    List<Map<String,Object>> toubiao = (List<Map<String, Object>>) document.get("toubiao");
                    toubiao.forEach(map->{
                        String line =commonLeft+map.get("toubiaoren").toString()+" "
                                    +map.get("toubiaojiner").toString()+" "
                                    +map.get("toubiaoshijian").toString()+
                                    commonRight;
                        try {
                            outputStream.write((line+"\r\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
        });
        outputStream.close();

    }

}
