package br.com.inventione.ecm.document;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;

import br.com.inventione.ecm.session.CmisSessionService;

@Path("/documentservice")
public class CmisDocumentService {

	private Session session;

	@GET
	@Path("/search/{parentFolder}/{searchParam}/{pageIndex}/{maxPageSize}")
	@Produces({ MediaType.APPLICATION_JSON })
	public CmisDocumentSearchResultList search(
			@PathParam("parentFolder") String parentFolder,
			@PathParam("searchParam") String searchParam,
			@PathParam("pageIndex") int pageIndex,
			@PathParam("maxPageSize") int maxPageSize) {

		session = CmisSessionService.connect();

		CmisDocumentSearchResultList cmisDocumentSearchResultList = new CmisDocumentSearchResultList();

		String queryString = "SELECT cmis:objectId, cmis:name from cmis:document where"
				+ " =content:"
				+ searchParam
				+ " and IN_TREE('"
				+ "workspace://SpacesStore/" + parentFolder + "')";

		// Load documents under the target folder
		ItemIterable<QueryResult> documentsResultSet = session
				.query(queryString, false).skipTo(maxPageSize * pageIndex)
				.getPage(maxPageSize);

		long totalItensNum = documentsResultSet.getTotalNumItems();

		// Build CmisDocumentSearchList with documents under the target folder
		for (QueryResult documentsResult : documentsResultSet) {
			Object documentId = documentsResult
					.getPropertyValueByQueryName("cmis:objectId");

			Object documentName = documentsResult
					.getPropertyValueByQueryName("cmis:name");

			Document document = (Document) session.getObject(String
					.valueOf(documentId));
			List<String> paths = document.getPaths();
			StringBuffer pathString = new StringBuffer();
			for (Iterator<String> iterator = paths.iterator(); iterator
					.hasNext();) {
				pathString.append(iterator.next());
			}

			CmisDocumentSearchResult documentSearchResult = new CmisDocumentSearchResult();
			documentSearchResult.setId(String.valueOf(documentId).replaceAll(
					"workspace://SpacesStore/", ""));
			documentSearchResult.setPath(pathString.toString());
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

	@GET
	@Path("/document/{objectId}")
	@Produces({ "application/pdf" })
	public InputStream getDocumentContent(@PathParam("objectId") String objectId) {
		session = CmisSessionService.connect();

		CmisObject object = session.getObject("workspace://SpacesStore/"
				+ objectId);
		Document document = (Document) object;
		return document.getContentStream().getStream();
	}

}
