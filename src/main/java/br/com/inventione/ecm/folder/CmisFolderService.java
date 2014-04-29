package br.com.inventione.ecm.folder;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;

import br.com.inventione.ecm.session.CmisSessionService;

@Path("/folderservice")
public class CmisFolderService {

	@GET
	@Path("/retrieveAll/{parentFolder}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CmisFolderSearchResult> retrieveAll(
			@PathParam("parentFolder") String parentFolder) {

		Session session = CmisSessionService.connect();

		List<CmisFolderSearchResult> foldersList = new ArrayList<CmisFolderSearchResult>();

		Folder folder = (Folder) session.getObject("workspace://SpacesStore/"
				+ parentFolder);
		CmisFolderSearchResult cmisFolderSearchResult = new CmisFolderSearchResult();
		cmisFolderSearchResult.setId(folder.getId().replaceAll(
				"workspace://SpacesStore/", ""));

		cmisFolderSearchResult.setName(folder.getName());
		foldersList.add(cmisFolderSearchResult);

		// Load folders under the target folder
		ItemIterable<QueryResult> subFoldersResultSet = session.query(
				"SELECT cmis:objectId, cmis:name from cmis:folder where "
						+ "in_folder('" + parentFolder + "')", false);

		// Count search results for each folder under the target folder
		for (QueryResult subFolderResult : subFoldersResultSet) {
			Object subFolderId = subFolderResult
					.getPropertyValueByQueryName("cmis:objectId");

			Object subFolderName = subFolderResult
					.getPropertyValueByQueryName("cmis:name");

			ItemIterable<QueryResult> documentsUnderSubFolderResultSet = session
					.query("SELECT cmis:objectId from cmis:document where in_tree('"
							+ subFolderId + "')", false);
			long numberOfDocuments = documentsUnderSubFolderResultSet
					.getTotalNumItems();
			if (numberOfDocuments > 0) {
				CmisFolderSearchResult folderSearchResult = new CmisFolderSearchResult();
				folderSearchResult.setId(String.valueOf(subFolderId)
						.replaceAll("workspace://SpacesStore/", ""));
				folderSearchResult.setName(String.valueOf(subFolderName));
				folderSearchResult.setDocumentsCount(numberOfDocuments);

				foldersList.add(folderSearchResult);
			}
		}

		return foldersList;

	}

	@GET
	@Path("/search/{parentFolder}/{searchParam}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<CmisFolderSearchResult> search(
			@PathParam("parentFolder") String parentFolder,
			@PathParam("searchParam") String searchParam) {
		Session session = CmisSessionService.connect();

		List<CmisFolderSearchResult> foldersList = new ArrayList<CmisFolderSearchResult>();

		// Load folders under the target folder
		ItemIterable<QueryResult> subFoldersResultSet = session.query(
				"SELECT cmis:objectId, cmis:name from cmis:folder where "
						+ "in_folder('" + parentFolder + "')", false);

		// Count search results for each folder under the target folder
		for (QueryResult subFolderResult : subFoldersResultSet) {
			Object subFolderId = subFolderResult
					.getPropertyValueByQueryName("cmis:objectId");

			Object subFolderName = subFolderResult
					.getPropertyValueByQueryName("cmis:name");

			ItemIterable<QueryResult> documentsUnderSubFolderResultSet = session
					.query("SELECT cmis:objectId from cmis:document where contains('ALL:"
							+ searchParam
							+ "') "
							+ "and in_tree('"
							+ subFolderId + "')", false);
			long numberOfDocuments = documentsUnderSubFolderResultSet
					.getTotalNumItems();
			if (numberOfDocuments > 0) {
				CmisFolderSearchResult folderSearchResult = new CmisFolderSearchResult();
				folderSearchResult.setId(String.valueOf(subFolderId)
						.replaceAll("workspace://SpacesStore/", ""));
				folderSearchResult.setName(String.valueOf(subFolderName));
				folderSearchResult.setDocumentsCount(numberOfDocuments);

				foldersList.add(folderSearchResult);
			}
		}

		return foldersList;

	}

}
