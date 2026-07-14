package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class AbstractDAOTest {

    protected SessionMongoTest refSessionMongo;

    public void setUp() throws Exception {
        this.refSessionMongo = new SessionMongoTest();
        final MongoDatabase database = refSessionMongo.getMongoClient().getDatabase("sharelatex");

        final MongoCollection<Document> usersCollection = database.getCollection("users");
        usersCollection.insertMany(createDocuments("userssample.json"));

        final MongoCollection<Document> projectsCollection = database.getCollection("projects");
        projectsCollection.insertMany(createDocuments("projectssample.json"));
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
            fail("Can't load data from file");
        }

        return lines;
    }
}
