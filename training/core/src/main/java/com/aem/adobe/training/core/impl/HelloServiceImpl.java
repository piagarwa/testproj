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
package com.aem.adobe.training.core.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.adobe.training.core.HelloService;

@Service(value = HelloService.class)
@Component(immediate = true)
public class HelloServiceImpl implements HelloService {

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Reference
	private SlingSettingsService settings;
	
	@Override
	public String getMessage() {
		log.info("piyush in hello world service...");
		return "piyush" + settings.getSlingId();
	}

}
