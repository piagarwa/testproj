package com.aem.adobe.training.core.impl.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.adobe.training.email.EmailService;
import com.day.cq.commons.Externalizer;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@SuppressWarnings("deprecation")
@Service(value = EventHandler.class)
@Component(immediate = true)
@Property(name = "event.topics", value = ReplicationAction.EVENT_TOPIC)
public class ReplicationLogger implements EventHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplicationLogger.class);
	@Reference
	private ResourceResolverFactory ResourceResolverFactory;
	
	@Reference
	private EmailService emailService;
	
	@Reference
	private Externalizer externalizer;
	
	@Override
	public void handleEvent(Event event) {
		LOGGER.info("********handling event");
		process (event);
	}
	
	public boolean process(Event event) {
		LOGGER.info("********processing job");
		ReplicationAction action = ReplicationAction.fromEvent(event);
		ResourceResolver resourceResolver = null;
		if (action.getType().equals(ReplicationActionType.ACTIVATE)) {
			try {
				resourceResolver = ResourceResolverFactory.getAdministrativeResourceResolver(null);
				final PageManager pm = resourceResolver.adaptTo(PageManager.class);
				final Page page = pm.getContainingPage(action.getPath());
				String externalEventLink = externalizer.publishLink(resourceResolver, page.getPath()) + ".html";
				String approvalLink = externalizer.publishLink(resourceResolver, "/content/approvalForm.html?eventPath=" + page.getPath() );
				
				 // Email Parameter map
	            Map<String, String> emailParams = new HashMap<String, String>();
	            emailParams.put("eventUrl", externalEventLink);
	            emailParams.put("firstName","Piyush");
	            emailParams.put("approvalLink", approvalLink);
	            emailParams.put("senderEmailAddress","piyushagarwal2006@gmail.com");
	            emailParams.put("senderName","Piyush Agarwal");
	            
	         // external email ids to which the approval mail would be sent
	            String[] emailTo = {"kolkata_piyush@yahoo.co.in"};

	            List<String> failureList = emailService.sendEmail("/etc/notification/email/approveEmailTemplate/emailtemplate.html", emailParams, emailTo);
	            
	            if (failureList.isEmpty()) {
	                LOGGER.info("Email sent successfully to {} recipients", emailTo.length);
	            } else {
	                LOGGER.error("Email sent failed");
	            }
			}
			catch (LoginException e) {
				e.printStackTrace();
			}
			finally {
				if(resourceResolver != null && resourceResolver.isLive()) {
					resourceResolver.close();
				}
			}
		}
		return true;
	}
}