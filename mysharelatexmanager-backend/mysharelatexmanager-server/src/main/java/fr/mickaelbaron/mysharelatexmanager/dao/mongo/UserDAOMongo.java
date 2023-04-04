package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static com.mongodb.client.model.Filters.or;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.FIRST_NAME;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.HASHED_PASSWORD;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_LOGGED_IN;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LOGIN_COUNT;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.ID;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.INSTITUTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.IS_ADMIN;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_NAME;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.USERS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import fr.mickaelbaron.mysharelatexmanager.dao.IUserDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.UserEntity;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ApplicationScoped
public class UserDAOMongo implements IUserDAO {

	@Inject
	IMongoSession refSessionMongo;

	@Override
	public List<UserEntity> getAllUsers(List<SortedData> sorted, String filter) {
		final MongoCollection<Document> usersCollection = refSessionMongo.getMongoDatabase().getCollection(USERS);

		BasicDBObject projectionQuery = new BasicDBObject();
		projectionQuery.put(ID, 1);
		projectionQuery.put(LAST_NAME, 1);
		projectionQuery.put(IS_ADMIN, 1);
		projectionQuery.put(INSTITUTION, 1);
		projectionQuery.put(FIRST_NAME, 1);
		projectionQuery.put(EMAIL, 1);
		projectionQuery.put(LOGIN_COUNT, 1);
		projectionQuery.put(HASHED_PASSWORD, 1);
		projectionQuery.put(LAST_LOGGED_IN, 1);

		BasicDBObject sortedQuery = new BasicDBObject();
		for (SortedData current : sorted) {
			sortedQuery.put(current.getName(), current.isAscendant() ? 1 : -1);
		}

		if (filter == null) {
			filter = "";
		}
		final String value = ".*" + filter + ".*";
		final String regexCst = "$regex";
		final String optionsCst = "$options";
		BasicDBObject lastNameFilter = new BasicDBObject(LAST_NAME,
				new BasicDBObject(regexCst, value).append(optionsCst, "i"));
		BasicDBObject firstNameFilter = new BasicDBObject(FIRST_NAME,
				new BasicDBObject(regexCst, value).append(optionsCst, "i"));
		BasicDBObject institutionFilter = new BasicDBObject(INSTITUTION,
				new BasicDBObject(regexCst, value).append(optionsCst, "i"));
		BasicDBObject emailFilter = new BasicDBObject(EMAIL,
				new BasicDBObject(regexCst, value).append(optionsCst, "i"));

		final MongoCursor<Document> iterator = usersCollection
				.find(or(lastNameFilter, firstNameFilter, institutionFilter, emailFilter)).sort(sortedQuery)
				.projection(projectionQuery).iterator();

		List<UserEntity> returnList = new ArrayList<>();
		while (iterator.hasNext()) {
			Document doc = iterator.next();

			final Optional<UserEntity> createUserFromDocument = this.createUserFromDocument(doc);
			if (createUserFromDocument.isPresent()) {
				returnList.add(createUserFromDocument.get());
			}
		}

		return returnList;
	}

	@Override
	public List<UserEntity> getAllUsers(List<SortedData> sorted) {
		return this.getAllUsers(sorted, "");
	}

	@Override
	public List<UserEntity> getAllUsers(String filter) {
		return this.getAllUsers(new ArrayList<SortedData>(), filter);
	}

	@Override
	public boolean updateUser(UserEntity updateValue) {
		final MongoCollection<Document> usersCollection = refSessionMongo.getMongoDatabase().getCollection(USERS);

		final BasicDBObject append = new BasicDBObject();
		append.append(LAST_NAME, updateValue.getLastName());
		append.append(EMAIL, updateValue.getEmail());
		append.append(FIRST_NAME, updateValue.getFirstName());
		append.append(INSTITUTION, updateValue.getInstitution());
		append.append(IS_ADMIN, updateValue.isAdmin());
		if (updateValue.getHashedPassword() != null && !updateValue.getHashedPassword().isEmpty()) {
			append.append(HASHED_PASSWORD, updateValue.getHashedPassword());
		}
		final BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("$set", append);

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(ID, new ObjectId(updateValue.getId()));

		final UpdateResult updateOne = usersCollection.updateOne(searchQuery, newDocument);
		return updateOne.getModifiedCount() != 0;
	}

	@Override
	public Optional<UserEntity> getUserById(String id) {
		if (!ObjectId.isValid(id)) {
			return Optional.empty();
		}

		final MongoCollection<Document> usersCollection = refSessionMongo.getMongoDatabase().getCollection(USERS);

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(ID, new ObjectId(id));

		final Document doc = usersCollection.find(searchQuery).first();

		return this.createUserFromDocument(doc);
	}

	protected Optional<UserEntity> createUserFromDocument(Document doc) {
		if (doc == null) {
			return Optional.empty();
		}

		UserEntity newUser = new UserEntity();
		newUser.setId(((ObjectId) doc.get(ID)).toHexString());
		newUser.setLastName(doc.getString(LAST_NAME));
		newUser.setAdmin(doc.getBoolean(IS_ADMIN));
		newUser.setInstitution(doc.getString(INSTITUTION));
		newUser.setFirstName(doc.getString(FIRST_NAME));
		newUser.setEmail(doc.getString(EMAIL));
		newUser.setLoginCount(doc.getInteger(LOGIN_COUNT));
		newUser.setLastLoggedIn(doc.getDate(LAST_LOGGED_IN));

		return Optional.of(newUser);
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return this.getAllUsers(new ArrayList<SortedData>(), "");
	}

	@Override
	public boolean deleteUser(String id) {
		if (!ObjectId.isValid(id)) {
			return false;
		}

		final MongoCollection<Document> usersCollection = refSessionMongo.getMongoDatabase().getCollection(USERS);

		BasicDBObject document = new BasicDBObject();
		document.put(ID, new ObjectId(id));
		final DeleteResult deleteOne = usersCollection.deleteOne(document);
		return deleteOne.getDeletedCount() == 1;
	}
}
