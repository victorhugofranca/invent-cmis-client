package br.com.inventione.ecm.document;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CmisDocumentSearchResult {

	private String id;
	private String fileName;
	private String path;
	private String documentSubstring;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDocumentSubstring() {
		return documentSubstring;
	}

	public void setDocumentSubstring(String documentSubstring) {
		this.documentSubstring = documentSubstring;
	}

}
