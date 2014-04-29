// Overall viewmodel for this screen, along with initial state
function SearhPageViewModel() {
	var self = this;

	self.nbPerPage = 10;
	self.pageNumber = ko.observable(1);
	self.totalPages = ko.observable(0);
	self.totalNumItens = ko.observable(0);

	self.hostUrl = 'http://localhost:8180/cmis-client/rest';
	self.searchParam = ko.observable();

	self.allFolders = ko.observableArray();

	self.folders = ko.observableArray();
	self.folderId = '4b7866f6-624e-462c-a7a7-50f801283bc3';

	self.documents = ko.observableArray();
	self.subFolder = ko.observable();
	self.subFolderId = ko.observable('4b7866f6-624e-462c-a7a7-50f801283bc3');

	self.folderServiceRetriveAll = self.hostUrl + '/folderservice/retrieveAll/'
			+ self.folderId;
	$.getJSON(self.folderServiceRetriveAll, function(data) {
		self.allFolders.removeAll();
		for (var i = 0; i < data.length; i++) {
			self.allFolders.push(data[i]);
		}
	});

	self.searchPerParam = function() {
		self.pageNumber(1);

		if (self.subFolder != null) {
			self.subFolderId(self.subFolder().id);
			self.folderId = self.subFolder().id;
		} else {
			self.subFolderId('4b7866f6-624e-462c-a7a7-50f801283bc3');
			self.folderId = '4b7866f6-624e-462c-a7a7-50f801283bc3';
		}

		self.search();
	};

	self.searchPerPage = function(pPageIndex) {
		self.pageNumber(pPageIndex);
		self.search();
	};

	self.searchPerFolder = function(pFolder) {
		if (pFolder != null && pFolder.id != '') {
			self.subFolderId(pFolder.id);
			self.folderId = pFolder.id;
		} else if (self.subFolder != null) {
			self.subFolderId(self.subFolder().id);
			self.folderId = self.subFolder().id;
		}

		self.pageNumber(1);
		self.search();
	};

	self.search = function() {

		if (self.searchParam() != null && self.searchParam() != '') {

			if (self.pageNumber() < 1)
				self.pageNumber(1);
			if (self.pageNumber() > self.totalPages)
				self.pageNumber(self.totalPages);

			self.folderServiceSearchURI = self.hostUrl
					+ '/folderservice/search/' + self.folderId + '/'
					+ self.searchParam();

			self.documentServiceSearchURI = self.hostUrl
					+ '/documentservice/search/' + self.subFolderId() + '/'
					+ self.searchParam() + '/' + (self.pageNumber() - 1) + '/'
					+ self.nbPerPage;

			$
					.getJSON(
							self.documentServiceSearchURI,
							function(data) {
								self.documents.removeAll();
								for (var i = 0; i < data.cmisDocumentSearchResultList.length; i++) {
									self.documents
											.push(data.cmisDocumentSearchResultList[i]);
								}
								self.pageNumber(data.pageIndex + 1);
								self.totalPages(data.pagesNum);
								self.totalNumItens(data.totalNumItens);
							});

			$.getJSON(self.folderServiceSearchURI, function(data) {
				self.folders.removeAll();
				for (var i = 0; i < data.length; i++) {
					self.folders.push(data[i]);
				}
			});

		}
		;
	};

}

var searchPageViewModel = new SearhPageViewModel();
ko.applyBindings(searchPageViewModel);