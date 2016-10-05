package eu.h2020.symbiote.generators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by mateuszl on 05.10.2016.
 */
@Component
public class RepositoryManager {

    private static MongoTemplate mongoTemplate;

    @Autowired
    public RepositoryManager(MongoTemplate mongoTemplate){
        Assert.notNull(mongoTemplate, "Mongo template can not be nulL!");
        this.mongoTemplate=mongoTemplate;
    }

    public static void cleanUp() {
        mongoTemplate.getDb().dropDatabase();

        // OR

//        for (String collectionName : mongoTemplate.getCollectionNames()) {
//            if (!collectionName.startsWith("system.")) {
//                mongoTemplate.getCollection(collectionName).remove(new BasicDBObject());
//            }
//        }

    }
}
