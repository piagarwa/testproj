package com.aem.adobe.training.core;

import javax.jcr.Session;

import org.apache.sling.commons.json.JSONArray;

public interface SearchService {
	
	public JSONArray performSearch(Session session, String searchTerm);

}
