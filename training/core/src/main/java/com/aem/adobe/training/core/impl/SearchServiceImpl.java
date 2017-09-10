package com.aem.adobe.training.core.impl;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.jcr.api.SlingRepository;

import com.aem.adobe.training.core.SearchService;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;

@Service(value= SearchService.class)
@Component(immediate=true)
public class SearchServiceImpl implements SearchService{
	
	@Reference
	QueryBuilder queryBuiler;
	
	@Reference
	SlingRepository repository;

	@Override
	public JSONArray performSearch(Session session , String searchTerm) {
		
		Query query = queryBuiler.createQuery(session);
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
