package com.aem.adobe.training.core.impl.listeners;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;

@Service
@Component(immediate = true, metatype = false)
public class PropertyChangeObserver implements EventListener {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Reference
	private SlingRepository repository;

	@Reference
	Replicator replicator;

	@Reference
	SlingSettingsService settingsService;


	private Session session;
	private ObservationManager observationManager;

	@SuppressWarnings("deprecation")
	protected void activate(ComponentContext context) throws Exception {
		session = repository.loginAdministrative(null);
		// Listen for changes to our orders
		if (repository.getDescriptor(Repository.OPTION_OBSERVATION_SUPPORTED).equals("true")) {
			observationManager = session.getWorkspace().getObservationManager();
			final String path = "/content/geometrixx/en";
			observationManager.addEventListener(this, Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED, path, true, null,
					null, false);
		}

	}

	protected void deactivate(ComponentContext componentContext) throws RepositoryException {

		if (observationManager != null) {
			observationManager.removeEventListener(this);
		}
		if (session != null) {
			session.logout();
			session = null;

		}
	}

	public void onEvent(EventIterator itr) {

		try {
			while (itr.hasNext()) {

				Event event = itr.nextEvent();
				String eventPath = event.getPath();
				if (eventPath.contains("action")) {
					eventPath = eventPath.replace("/action", "");
					Node eventNode = session.getNode(eventPath);
					String eventPagePath = eventNode.getParent().getPath();
					String action = eventNode.getProperty("action").getValue().getString();
					if (action.equalsIgnoreCase("disapproved") && settingsService.getRunModes().contains("author")) {
						replicator.replicate(session, ReplicationActionType.DEACTIVATE, eventPagePath);
					}
				}
				log.info("something has been added : {}", event.getPath());
			}
		} catch (RepositoryException e) {
			log.error("Error while treating events", e.getMessage());
		} catch (ReplicationException e) {
			log.error("Error while deactivating", e.getMessage());
		}

	}

}