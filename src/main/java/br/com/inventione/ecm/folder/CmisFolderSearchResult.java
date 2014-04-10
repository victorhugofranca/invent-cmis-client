package br.com.inventione.ecm.folder;

public class CmisFolderSearchResult {
	
	private String id;
	private String name;
	private Long documentsCount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDocumentsCount() {
		return documentsCount;
	}
	public void setDocumentsCount(Long documentsCount) {
		this.documentsCount = documentsCount;
	}

}
