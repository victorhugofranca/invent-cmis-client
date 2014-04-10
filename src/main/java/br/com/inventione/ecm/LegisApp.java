package br.com.inventione.ecm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import br.com.inventione.ecm.document.CmisDocumentSearchResult;
import br.com.inventione.ecm.document.CmisDocumentSearchResultList;
import br.com.inventione.ecm.document.CmisDocumentService;
import br.com.inventione.ecm.folder.CmisFolderSearchResult;
import br.com.inventione.ecm.folder.CmisFolderService;

public class LegisApp {

	private static final String CMIS_ATOMPUB_URL = "http://localhost:8080/alfresco/service/cmis";
	private static final String REPOSITORY_ID = "3e020bb1-b62c-49e0-9754-5d3de8eb7653";

	private static String PARENT_FOLDER = "workspace://SpacesStore/4b7866f6-624e-462c-a7a7-50f801283bc3";
	private static String SEARCH_PARAM = "fazenda";
	private static int PAGE_INDEX = 0;
	private static int PAGE_MAX_SIZE = 5;

	public static void main(String[] args) {

		// ===================== Sub folder structure list ====================
		CmisFolderService cmisFolderService = new CmisFolderService();
		List<CmisFolderSearchResult> foldersList = cmisFolderService.search(
				CMIS_ATOMPUB_URL, REPOSITORY_ID, PARENT_FOLDER, SEARCH_PARAM);

		System.out.println("=======Found sub folders =====");
		System.out.println("==============================");
		for (Iterator<CmisFolderSearchResult> iterator = foldersList.iterator(); iterator
				.hasNext();) {
			CmisFolderSearchResult cmisFolderSearchResult = (CmisFolderSearchResult) iterator
					.next();
			System.out.println(" Sub folder name: "
					+ cmisFolderSearchResult.getName());
			System.out.println(" Documents for this sub folder: "
					+ cmisFolderSearchResult.getDocumentsCount());
			System.out.println("==============================");
		}
		System.out.println("==============================");

		// =================Documents list for first sub folder
		// ===================
		CmisDocumentService cmisDocumentService = new CmisDocumentService();
		CmisDocumentSearchResultList cmisDocumentSearchResultList = cmisDocumentService
				.search(CMIS_ATOMPUB_URL, REPOSITORY_ID, foldersList.get(0)
						.getId(), SEARCH_PARAM, PAGE_INDEX, PAGE_MAX_SIZE);
		System.out.println("=========Paging information==========");

		System.out.println("page index: "
				+ cmisDocumentSearchResultList.getPageIndex());
		System.out.println("page num itens: "
				+ cmisDocumentSearchResultList.getPageNumItens());
		System.out.println("total num itens: "
				+ cmisDocumentSearchResultList.getTotalNumItens());
		System.out.println("total pages: "
				+ cmisDocumentSearchResultList.getPagesNum());
		
		System.out.println("==============================");

		List<CmisDocumentSearchResult> cmisDocumentSearchResults = cmisDocumentSearchResultList
				.getCmisDocumentSearchResultList();

		System.out.println("======= Documents for sub folder: " + foldersList.get(0).getName() + "=======");
		for (Iterator<CmisDocumentSearchResult> iterator = cmisDocumentSearchResults
				.iterator(); iterator.hasNext();) {
			CmisDocumentSearchResult cmisDocumentSearchResult = (CmisDocumentSearchResult) iterator
					.next();

			System.out.println("File id: " + cmisDocumentSearchResult.getId());
			System.out.println("File name: " + cmisDocumentSearchResult.getFileName());

			// Downloading file
			writeFile(
					cmisDocumentService.getDocumentContent(cmisDocumentSearchResult
							.getId()), cmisDocumentSearchResult.getFileName());

			System.out.println();
		}
	}

	public static void writeFile(InputStream inputStream, String fileName) {

		OutputStream outputStream = null;

		try {
			// write the inputStream to a FileOutputStream
			outputStream = new FileOutputStream(new File(fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			System.out.println("Arquivo salvo: " + fileName);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
