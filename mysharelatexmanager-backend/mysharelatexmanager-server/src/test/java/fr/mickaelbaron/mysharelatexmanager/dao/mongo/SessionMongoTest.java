package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant.SHARELATEX_DATABASE_NAME;

import javax.inject.Singleton;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Singleton
public class SessionMongoTest implements IMongoSession {

	protected GenericContainer<?> mongodb;

	protected MongoClient mongoClient;

	final String SHARELATEX_MONGODB_URL = "mongodb://localhost:";

	public SessionMongoTest() {
		mongodb = new GenericContainer<>(DockerImageName.parse("mongo:4.4.19")).withExposedPorts(27017);
		assert null != mongodb;

		mongodb.start();

		mongoClient = MongoClients.create(SHARELATEX_MONGODB_URL + mongodb.getFirstMappedPort());// new MongoClient(new MongoClientURI(SHARELATEX_MONGODB_URL + mongodb.getFirstMappedPort()));
	}

	public void stopContainer() {
		if (mongodb.isRunning()) {
			mongoClient.close();
			mongodb.stop();
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
