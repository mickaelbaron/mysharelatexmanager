package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.MySharelatexManagerConstant.SHARELATEX_DATABASE_NAME;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import fr.mickaelbaron.mysharelatexmanager.cfg.IConfigExecution;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Singleton
public class SessionMongo implements IMongoSession {

	protected MongoClient mongoClient;

	@Inject
	IConfigExecution config;

	public MongoClient getMongoClient() {
		if (mongoClient == null) {
			mongoClient = MongoClients.create(config.getMongoDBUrl());
		}
		return mongoClient;
	}

	@Override
	public MongoDatabase getMongoDatabase() {
		return getMongoClient().getDatabase(SHARELATEX_DATABASE_NAME);
	}
}
