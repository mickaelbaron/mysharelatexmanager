package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import fr.mickaelbaron.mysharelatexmanager.AbstractCDIUnitTest;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class AbstractDAOTest extends AbstractCDIUnitTest {

	@Inject
	private IMongoSession currentSession;

	@Before
	public void setUp() throws Exception {
		final MongoDatabase database = currentSession.getMongoClient().getDatabase("sharelatex");

		final MongoCollection<Document> usersCollection = database.getCollection("users");
		usersCollection.insertMany(createDocuments("userssample.json"));

		final MongoCollection<Document> projectsCollection = database.getCollection("projects");
		projectsCollection.insertMany(createDocuments("projectssample.json"));
	}

	@After
	public void tearDown() throws NamingException {
		super.tearDown();
		
		((SessionMongoTest) currentSession).stopContainer();
	}

	private List<Document> createDocuments(String filename) {
		final List<String> extractLinesFromFile = extractLinesFromFile(filename);

		List<Document> usersDocuments = new ArrayList<Document>();
		for (String string : extractLinesFromFile) {
			usersDocuments.add(Document.parse(string));
		}

		return usersDocuments;
	}

	private List<String> extractLinesFromFile(String filename) {
		List<String> lines = new ArrayList<String>();
		InputStream resourceAsStream = getClass().getResourceAsStream("/" + filename);
		final InputStreamReader reader = new InputStreamReader(resourceAsStream);

		LineNumberReader lineReader = new LineNumberReader(reader);
		String line = null;
		try {
			while ((line = lineReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			Assert.fail("Can't load data from file");
		}

		return lines;
	}
}
