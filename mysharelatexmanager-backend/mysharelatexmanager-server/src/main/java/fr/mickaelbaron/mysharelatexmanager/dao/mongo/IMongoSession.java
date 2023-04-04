package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import fr.mickaelbaron.mysharelatexmanager.dao.ISession;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public interface IMongoSession extends ISession {

	MongoClient getMongoClient();

	MongoDatabase getMongoDatabase();
}
