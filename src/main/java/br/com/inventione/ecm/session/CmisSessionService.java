package br.com.inventione.ecm.session;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

public class CmisSessionService {
	
	/**
	 * Connect to alfresco repository
	 * 
	 * @return root folder object
	 */
	public Session connect(String cmisAtompubUrl, String repoId) {
		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");
		parameter.put(SessionParameter.ATOMPUB_URL, cmisAtompubUrl);
		parameter.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());
		parameter.put(SessionParameter.REPOSITORY_ID, repoId);

		return sessionFactory.createSession(parameter);
	}


}
