package fr.mickaelbaron.mysharelatexmanager.dao.mongo;

import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.ACTIVE;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.COLLABORATOR_REFS;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.DESCRIPTION;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.EMAIL;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.ID;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.LAST_UPDATED;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.NAME;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.OWNER_REF;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.PROJECTS;
import static fr.mickaelbaron.mysharelatexmanager.dao.mongo.MongoConstant.USERS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import fr.mickaelbaron.mysharelatexmanager.dao.IProjectDAO;
import fr.mickaelbaron.mysharelatexmanager.dao.SortedData;
import fr.mickaelbaron.mysharelatexmanager.entity.ProjectEntity;
import fr.mickaelbaron.mysharelatexmanager.entity.ShortUserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@ApplicationScoped
public class ProjectDAOMongo implements IProjectDAO {

	@Inject
	IMongoSession refSessionMongo;

	private static final String COLLABORATOR_INFO = "collaboratorinfo";

	private static final String OWNER_INFO = "ownerinfo";

	@Override
	public List<ProjectEntity> getAllProjects(List<SortedData> sorted) {
		return this.getAllProjects(sorted, "");
	}

	private List<ProjectEntity> getAllProjects(List<SortedData> pSorted, String filter, String id, String columnId) {
		final MongoCollection<Document> projectsCollection = refSessionMongo.getMongoDatabase().getCollection(PROJECTS);

		List<Bson> aggregateFilters = new ArrayList<>();

		// Filter.
		if (filter == null) {
			filter = "";
		}

		if (!filter.isEmpty()) {
			final String value = ".*" + filter + ".*";
			Bson nameFilter = new BasicDBObject(NAME, new BasicDBObject("$regex", value).append("$options", "i"));
			BasicDBObject descriptionFilter = new BasicDBObject(DESCRIPTION,
					new BasicDBObject("$regex", value).append("$options", "i"));
			Bson match = new BasicDBObject("$match",
					new BasicDBObject("$or", Arrays.asList(nameFilter, descriptionFilter)));
			aggregateFilters.add(match);
		} else {
			if (id != null && !id.isEmpty()) {
				Bson match = new BasicDBObject("$match", new BasicDBObject(columnId, new ObjectId(id)));
				aggregateFilters.add(match);
			}
		}

		// Sorter.
		if (pSorted == null) {
			pSorted = new ArrayList<>();
		}

		if (!pSorted.isEmpty()) {
			BasicDBObject sortedQuery = new BasicDBObject();
			for (SortedData current : pSorted) {
				sortedQuery.append(current.getName(), current.isAscendant() ? 1 : -1);
			}
			Bson sorted = new Document("$sort", sortedQuery);
			aggregateFilters.add(sorted);
		}

		// First Lookup. LEFT JOIN
		Bson firstLookup = new Document("$lookup", new Document("from", USERS).append("localField", OWNER_REF)
				.append("foreignField", ID).append("as", OWNER_INFO));
		aggregateFilters.add(firstLookup);

		// Collapse.
		Bson unwind = new Document("$unwind", "$ownerinfo");
		aggregateFilters.add(unwind);

		// Collapse.
		Bson secondLookup = new Document("$lookup", new Document("from", USERS).append("localField", COLLABORATOR_REFS)
				.append("foreignField", ID).append("as", COLLABORATOR_INFO));
		aggregateFilters.add(secondLookup);

		// Projection.
		Bson project = new Document("$project",
				new Document(ID, 1).append(NAME, 1).append(DESCRIPTION, 1).append(OWNER_REF, 1).append(ACTIVE, 1)
						.append(COLLABORATOR_REFS, 1).append(LAST_UPDATED, 1).append(OWNER_INFO + "." + EMAIL, 1)
						.append(COLLABORATOR_INFO + "." + EMAIL, 1).append(COLLABORATOR_INFO + "." + ID, 1));
		aggregateFilters.add(project);

		final AggregateIterable<Document> aggregate = projectsCollection.aggregate(aggregateFilters);

		List<ProjectEntity> returnList = new ArrayList<>();
		for (Document document : aggregate) {
			final Optional<ProjectEntity> createProjectFromDocument = this.createProjectFromDocument(document);
			if (createProjectFromDocument.isPresent()) {
				returnList.add(createProjectFromDocument.get());
			}
		}

		return returnList;
	}

	protected Optional<ProjectEntity> createProjectFromDocument(Document doc) {
		if (doc == null) {
			return Optional.empty();
		}

		ProjectEntity newProject = new ProjectEntity();
		newProject.setId(((ObjectId) doc.get(ID)).toHexString());
		newProject.setName(doc.getString(NAME));
		newProject.setDescription(doc.getString(DESCRIPTION));
		newProject.setOwner(((Document) doc.get(OWNER_INFO)).getString(EMAIL));
		newProject.setOwnerId(((ObjectId) doc.get(OWNER_REF)).toHexString());
		newProject.setActive(doc.getBoolean(ACTIVE));
		newProject.setLastUpdated(doc.getDate(LAST_UPDATED));
		List<?> x = (ArrayList<?>) doc.get(COLLABORATOR_INFO);
		List<ShortUserEntity> collaboratorss = new ArrayList<>();
		for (Object object : x) {
			ShortUserEntity newShortUserEntity = new ShortUserEntity();
			newShortUserEntity.setId(((Document) object).get(ID).toString());
			newShortUserEntity.setEmail(((Document) object).getString(EMAIL));
			collaboratorss.add(newShortUserEntity);
		}
		newProject.setCollaborators(collaboratorss);
		x = (ArrayList<?>) doc.get(COLLABORATOR_REFS);
		List<String> collaboratorsId = new ArrayList<>();
		for (Object object : x) {
			collaboratorsId.add(((ObjectId) object).toHexString());
		}
		newProject.setCollaboratorsId(collaboratorsId);

		return Optional.of(newProject);
	}

	@Override
	public List<ProjectEntity> getAllProjects(List<SortedData> pSorted, String filter) {
		return getAllProjects(pSorted, filter, "", ID);
	}

	@Override
	public List<ProjectEntity> getAllProjects(String filter) {
		return this.getAllProjects(new ArrayList<SortedData>(), filter);
	}

	@Override
	public boolean updateProjectsOwner(String oldOwnerId, String newOwnerId) {
		final MongoCollection<Document> projectsCollection = refSessionMongo.getMongoDatabase().getCollection(PROJECTS);
		BasicDBObject append = new BasicDBObject();
		append.put(OWNER_REF, new ObjectId(newOwnerId));

		final BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("$set", append);

		BasicDBObject searchQuery = new BasicDBObject().append(OWNER_REF, new ObjectId(oldOwnerId));

		final UpdateResult updateMany = projectsCollection.updateMany(searchQuery, newDocument);
		return updateMany.getModifiedCount() != 0;
	}

	@Override
	public boolean updateProject(ProjectEntity updateValue) {
		final MongoCollection<Document> projectsCollection = refSessionMongo.getMongoDatabase().getCollection(PROJECTS);

		final BasicDBObject append = new BasicDBObject();
		append.append(OWNER_REF, new ObjectId(updateValue.getOwnerId()));
		append.append(NAME, updateValue.getName());
		append.append(DESCRIPTION, updateValue.getDescription());
		append.append(ACTIVE, updateValue.isActive());
		BasicDBList newList = new BasicDBList();
		for (String current : updateValue.getCollaboratorsId()) {
			newList.add(new ObjectId(current));
		}
		append.append(COLLABORATOR_REFS, newList);

		final BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("$set", append);

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put(ID, new ObjectId(updateValue.getId()));

		final UpdateResult updateOne = projectsCollection.updateOne(searchQuery, newDocument);
		return updateOne.getModifiedCount() != 0;
	}

	@Override
	public Optional<ProjectEntity> getProjectById(String id) {
		if (!ObjectId.isValid(id)) {
			return Optional.empty();
		}

		final List<ProjectEntity> allProjects = this.getAllProjects(new ArrayList<SortedData>(), "", id, ID);

		if (allProjects.size() == 1) {
			return Optional.of(allProjects.get(0));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public List<ProjectEntity> getAllProjects() {
		return this.getAllProjects(new ArrayList<SortedData>(), "");
	}

	private List<ProjectEntity> getAllProjectsById(String ownerId, String attribute) {
		if (attribute == null || ownerId == null || !ObjectId.isValid(ownerId)) {
			return new ArrayList<>();
		}

		return getAllProjects(new ArrayList<SortedData>(), "", ownerId, attribute);
	}

	@Override
	public List<ProjectEntity> getAllProjectsByOwnerId(String ownerId) {
		return getAllProjectsById(ownerId, OWNER_REF);
	}

	@Override
	public List<ProjectEntity> getAllProjectsByCollaboratorsId(String ownerId) {
		return getAllProjectsById(ownerId, COLLABORATOR_REFS);
	}

	@Override
	public List<ProjectEntity> transferAllProjects(String oldOwnerId, String newOwnerId) {
		final List<ProjectEntity> allProjectsByOwnerId = this.getAllProjectsByOwnerId(oldOwnerId);

		allProjectsByOwnerId.stream().forEach(project -> transferProject(project, newOwnerId));

		return getAllProjectsByOwnerId(oldOwnerId);
	}

	// https://www.baeldung.com/java-streams-peek-api
	@Override
	public Optional<Long> removeCollaborator(String collaboratorId) {
		List<ProjectEntity> allProjectsByCollaboratorsId = this.getAllProjectsByCollaboratorsId(collaboratorId);

		long count = allProjectsByCollaboratorsId.stream().filter(project -> (!project.getCollaboratorsId().isEmpty()))
				.peek(project -> {
					project.setCollaboratorsId(
							this.removeCollaboratorUtil(collaboratorId, project.getCollaboratorsId()));
					this.updateProject(project);
				}).count();

		return Optional.of(count);
	}

	@Override
	public List<ProjectEntity> transferProjectsIfExistingCollaborators(String oldOwnerId, String newOwnerId) {
		final List<ProjectEntity> allProjectsByOwnerId = this.getAllProjectsByOwnerId(oldOwnerId);

		allProjectsByOwnerId.stream().forEach(project -> {
			if (!project.getCollaboratorsId().isEmpty()) {
				transferProject(project, newOwnerId);
			}
		});

		return getAllProjectsByOwnerId(oldOwnerId);
	}

	@Override
	public List<ProjectEntity> transferProjectsIfNewOwnerIsCollaborator(String oldOwnerId, String newOwnerId) {
		final List<ProjectEntity> allProjectsByOwnerId = this.getAllProjectsByOwnerId(oldOwnerId);

		allProjectsByOwnerId.stream().forEach(project -> {
			if (isCollaboratorPresent(newOwnerId, project.getCollaboratorsId())) {
				transferProject(project, newOwnerId);
			}
		});

		return getAllProjectsByOwnerId(oldOwnerId);
	}

	protected List<String> removeCollaboratorUtil(String collaboratorId, List<String> collaboratorsId) {
		return collaboratorsId.stream().filter(i -> !i.equals(collaboratorId)).collect(Collectors.toList());
	}

	protected boolean isCollaboratorPresent(String collaboratorId, List<String> collaboratorsId) {
		return collaboratorsId.stream().anyMatch(i -> i.equals(collaboratorId));
	}

	private void transferProject(ProjectEntity currentProject, String newOwnerId) {
		currentProject.setOwnerId(newOwnerId);
		currentProject.setCollaboratorsId(this.removeCollaboratorUtil(newOwnerId, currentProject.getCollaboratorsId()));
		this.updateProject(currentProject);
	}
}
