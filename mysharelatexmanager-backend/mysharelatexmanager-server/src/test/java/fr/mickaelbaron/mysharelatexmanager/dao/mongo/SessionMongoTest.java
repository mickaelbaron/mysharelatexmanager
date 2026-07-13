package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant.SHARELATEX_DATABASE_NAME;

import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class SessionMongoTest implements IMongoSession {

    protected MongoDBContainer mongoDBContainer;

    protected MongoClient mongoClient;

    public SessionMongoTest() {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.24"));
        mongoDBContainer.start();

        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

	public void stopContainer() {
		if (mongoDBContainer.isRunning()) {
			mongoClient.close();
			mongoDBContainer.stop();
		}
	}

    @Override
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(SHARELATEX_DATABASE_NAME);
    }
}
