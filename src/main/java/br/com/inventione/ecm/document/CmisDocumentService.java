package br.com.inventione.ecm.document;

import java.io.InputStream;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;

import br.com.inventione.ecm.session.CmisSessionService;

public class CmisDocumentService {

	private Session session;

	public CmisDocumentSearchResultList search(String cmisAtompubUrl,
			String repoId, String parentFolder, String searchParam,
			int pageIndex, int maxPageSize) {

		CmisSessionService cmisSessionService = new CmisSessionService();
		session = cmisSessionService.connect(cmisAtompubUrl, repoId);

		long totalItensNum = getItensTotal(parentFolder, searchParam);

		CmisDocumentSearchResultList cmisDocumentSearchResultList = new CmisDocumentSearchResultList();

		// Load documents under the target folder
		ItemIterable<QueryResult> documentsResultSet = session
				.query("SELECT cmis:objectId, cmis:name from cmis:document where "
						+ "in_folder('"
						+ parentFolder
						+ "') and contains('"
						+ searchParam + "') ", false)
				.skipTo(maxPageSize * pageIndex).getPage(maxPageSize);

		// Build CmisDocumentSearchList with documents under the target folder
		for (QueryResult documentsResult : documentsResultSet) {
			Object documentId = documentsResult
					.getPropertyValueByQueryName("cmis:objectId");

			Object documentName = documentsResult
					.getPropertyValueByQueryName("cmis:name");

			CmisDocumentSearchResult documentSearchResult = new CmisDocumentSearchResult();
			documentSearchResult.setId(String.valueOf(documentId));
			documentSearchResult.setFileName(String.valueOf(documentName));

			cmisDocumentSearchResultList.add(documentSearchResult);
		}

		cmisDocumentSearchResultList.setPageNumItens(documentsResultSet
				.getPageNumItems());
		cmisDocumentSearchResultList.setTotalNumItens(totalItensNum);
		cmisDocumentSearchResultList.setPageIndex(new Long(pageIndex));
		cmisDocumentSearchResultList
				.setPagesNum(totalItensNum % maxPageSize > 0 ? (totalItensNum / maxPageSize) + 1
						: totalItensNum / maxPageSize);

		return cmisDocumentSearchResultList;

	}

	public InputStream getDocumentContent(String objectId) {
		CmisObject object = session.getObject(objectId);
		Document document = (Document) object;
		return document.getContentStream().getStream();
	}

	private long getItensTotal(String parentFolder, String searchParam) {

		ItemIterable<QueryResult> documentsResultSet = session.query(
				"SELECT cmis:objectId from cmis:document where "
						+ "in_folder('" + parentFolder + "') and contains('"
						+ searchParam + "') ", false);

		return documentsResultSet.getTotalNumItems();

	}

}
