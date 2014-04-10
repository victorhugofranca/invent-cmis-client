package br.com.inventione.ecm.document;

import java.util.ArrayList;
import java.util.List;

public class CmisDocumentSearchResultList {

	private List<CmisDocumentSearchResult> cmisDocumentSearchResultList;
	private Long totalNumItens;
	private Long pageNumItens;
	private Long pageIndex;
	private Long pagesNum;

	public CmisDocumentSearchResultList() {
		cmisDocumentSearchResultList = new ArrayList<CmisDocumentSearchResult>();
	}

	public void add(CmisDocumentSearchResult cmisDocumentSearchResult) {
		cmisDocumentSearchResultList.add(cmisDocumentSearchResult);
	}

	public Long getTotalNumItens() {
		return totalNumItens;
	}

	public void setTotalNumItens(Long totalNumItens) {
		this.totalNumItens = totalNumItens;
	}

	public Long getPageNumItens() {
		return pageNumItens;
	}

	public void setPageNumItens(Long pageNumItens) {
		this.pageNumItens = pageNumItens;
	}

	public Long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public List<CmisDocumentSearchResult> getCmisDocumentSearchResultList() {
		return cmisDocumentSearchResultList;
	}
	
	public Long getPagesNum() {
		return pagesNum;
	}
	
	public void setPagesNum(Long pagesNum) {
		this.pagesNum = pagesNum;
	}

}
