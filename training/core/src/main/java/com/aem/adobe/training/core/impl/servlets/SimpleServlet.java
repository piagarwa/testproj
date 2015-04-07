/*
 *  Copyright 2014 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.adobe.training.core.impl.servlets;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.ServletException;

import java.io.IOException;

@SuppressWarnings("serial")
@SlingServlet(paths = "/bin/eventApproveServlet", methods = { "POST", "GET" })
public class SimpleServlet extends SlingAllMethodsServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServlet.class);

	@Reference
	private transient ResourceResolverFactory resourceResolverFactory;

	@Reference
	private SlingSettingsService settingsService;

	@Reference
	private Replicator replicator;

	/**
	 * filter for replicate on modification agents that should also include
	 * reverse replication ("distribute") agents
	 */
	private static final AgentFilter MOD_DISTRIBUTE_FILTER = new AgentFilter() {
		public boolean isIncluded(Agent agent) {
			return agent.getConfiguration().isTriggeredOnDistribute();
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		String eventPath = req.getParameter("eventPath");
		String action = req.getParameter("eventModerated");
		ResourceResolver adminResolver = null;

		try {
			adminResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
			Session session = adminResolver.adaptTo(Session.class);
			Node eventNode = session.getNode(eventPath);
			Node contentNode = eventNode.getNode("jcr:content");
			contentNode.setProperty("eventModerated", action);
			session.save();

			ReplicationOptions opts = new ReplicationOptions();
			if (settingsService.getRunModes().contains("publish")) {
				opts.setFilter(MOD_DISTRIBUTE_FILTER);
				opts.setSuppressStatusUpdate(true);
				replicator.replicate(session, ReplicationActionType.ACTIVATE, eventPath, opts);
			}
			if (action.equalsIgnoreCase("true")) {
				resp.getWriter().println("The event is approved and would be available in the publish site");
			} else {
				resp.getWriter().println("The event is not approved and would not be available in the publish site");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (adminResolver != null) {
				adminResolver.close();
			}
		}

	}
}
