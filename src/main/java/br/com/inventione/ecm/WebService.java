package br.com.inventione.ecm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.inventione.ecm.document.CmisDocumentSearchResult;
import br.com.inventione.ecm.document.CmisDocumentSearchResultList;

@Path("/webservice")
public class WebService {

	@GET
	@Path("/hello")
	@Produces({ MediaType.APPLICATION_JSON })
	public CmisDocumentSearchResultList hello() {

		CmisDocumentSearchResultList cmisDocumentSearchResultList = new CmisDocumentSearchResultList();
		
		CmisDocumentSearchResult cmisDocumentSearchResult = new CmisDocumentSearchResult();
		cmisDocumentSearchResult.setFileName("teste!!!");
		
		cmisDocumentSearchResultList.add(cmisDocumentSearchResult);
		
		return cmisDocumentSearchResultList;
	}

}