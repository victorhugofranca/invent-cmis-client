package br.com.inventione.ecm.folder;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;

import br.com.inventione.ecm.session.CmisSessionService;

public class CmisFolderService {

	public List<CmisFolderSearchResult> search(String cmisAtompubUrl,
			String repoId, String parentFolder, String searchParam) {
		CmisSessionService cmisSessionService = new CmisSessionService();
		Session session = cmisSessionService.connect(cmisAtompubUrl, repoId);

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
					.query("SELECT cmis:objectId from cmis:document where contains('"
							+ searchParam
							+ "') "
							+ "and in_tree('"
							+ subFolderId + "')", false);
			long numberOfDocuments = documentsUnderSubFolderResultSet
					.getTotalNumItems();

			CmisFolderSearchResult folderSearchResult = new CmisFolderSearchResult();
			folderSearchResult.setId(String.valueOf(subFolderId));
			folderSearchResult.setName(String.valueOf(subFolderName));
			folderSearchResult.setDocumentsCount(numberOfDocuments);

			foldersList.add(folderSearchResult);
		}

		return foldersList;

	}

}
