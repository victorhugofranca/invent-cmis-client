package br.com.inventione.ecm.session;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class CmisSessionService {

	private static final String CMIS_ATOMPUB_URL = "http://localhost:8080/alfresco/service/cmis";
	private static final String REPOSITORY_ID = "3e020bb1-b62c-49e0-9754-5d3de8eb7653";

	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "admin";

	private static Session session;

	private CmisSessionService() {

	}

	/**
	 * Connect to alfresco repository
	 * 
	 * @return root folder object
	 */
	public static Session connect() {
		if (session == null) {
			SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put(SessionParameter.USER, USER_NAME);
			parameter.put(SessionParameter.PASSWORD, PASSWORD);
			parameter.put(SessionParameter.ATOMPUB_URL, CMIS_ATOMPUB_URL);
			parameter.put(SessionParameter.BINDING_TYPE,
					BindingType.ATOMPUB.value());
			parameter.put(SessionParameter.REPOSITORY_ID, REPOSITORY_ID);
			session = sessionFactory.createSession(parameter);
		}

		return session;
	}

}
