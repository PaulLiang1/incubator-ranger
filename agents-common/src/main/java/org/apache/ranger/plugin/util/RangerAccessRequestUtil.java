/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.plugin.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.plugin.contextenricher.RangerTagForEval;
import org.apache.ranger.plugin.policyengine.RangerAccessResource;

public class RangerAccessRequestUtil {
	private static final Log LOG = LogFactory.getLog(RangerAccessRequestUtil.class);

	public static final String KEY_CONTEXT_TAGS                = "TAGS";
	public static final String KEY_CONTEXT_TAG_OBJECT          = "TAG_OBJECT";
	public static final String KEY_CONTEXT_RESOURCE            = "RESOURCE";
	public static final String KEY_CONTEXT_REQUESTED_RESOURCES = "REQUESTED_RESOURCES";
	public static final String KEY_TOKEN_NAMESPACE = "token:";
	public static final String KEY_USER = "USER";

	public static void setRequestTagsInContext(Map<String, Object> context, Set<RangerTagForEval> tags) {
		if(CollectionUtils.isEmpty(tags)) {
			context.remove(KEY_CONTEXT_TAGS);
		} else {
			context.put(KEY_CONTEXT_TAGS, tags);
		}
	}

	public static Set<RangerTagForEval> getRequestTagsFromContext(Map<String, Object> context) {
		Set<RangerTagForEval> ret = null;
		Object          val = context.get(RangerAccessRequestUtil.KEY_CONTEXT_TAGS);

		if (val != null && val instanceof Set<?>) {
			try {
				@SuppressWarnings("unchecked")
				Set<RangerTagForEval> tags = (Set<RangerTagForEval>) val;

				ret = tags;
			} catch (Throwable t) {
				LOG.error("getRequestTags(): failed to get tags from context", t);
			}
		}

		return ret;
	}

	public static void setCurrentTagInContext(Map<String, Object> context, RangerTagForEval tag) {
		context.put(KEY_CONTEXT_TAG_OBJECT, tag);
	}

	public static RangerTagForEval getCurrentTagFromContext(Map<String, Object> context) {
		RangerTagForEval ret = null;
		Object    val = context.get(KEY_CONTEXT_TAGS);

		if(val != null && val instanceof RangerTagForEval) {
			ret = (RangerTagForEval)val;
		}

		return ret;
	}

	public static void setRequestedResourcesInContext(Map<String, Object> context, RangerRequestedResources resources) {
		context.put(KEY_CONTEXT_REQUESTED_RESOURCES, resources);
	}

	public static RangerRequestedResources getRequestedResourcesFromContext(Map<String, Object> context) {
		RangerRequestedResources ret = null;
		Object                   val = context.get(KEY_CONTEXT_REQUESTED_RESOURCES);

		if(val != null && val instanceof RangerRequestedResources) {
			ret = (RangerRequestedResources)val;
		}

		return ret;
	}

	public static void setCurrentResourceInContext(Map<String, Object> context, RangerAccessResource resource) {
		context.put(KEY_CONTEXT_RESOURCE, resource);
	}

	public static RangerAccessResource getCurrentResourceFromContext(Map<String, Object> context) {
		RangerAccessResource ret = null;
		Object               val = context.get(KEY_CONTEXT_RESOURCE);

		if(val != null && val instanceof RangerAccessResource) {
			ret = (RangerAccessResource)val;
		}

		return ret;
	}

	public static Map<String, Object> copyContext(Map<String, Object> context) {
		final Map<String, Object> ret;

		if(MapUtils.isEmpty(context)) {
			ret = new HashMap<String, Object>();
		} else {
			ret = new HashMap<String, Object>(context);

			ret.remove(KEY_CONTEXT_TAGS);
			ret.remove(KEY_CONTEXT_TAG_OBJECT);
			ret.remove(KEY_CONTEXT_RESOURCE);
			// don't remove REQUESTED_RESOURCES
		}

		return ret;
	}

	public static void setCurrentUserInContext(Map<String, Object> context, String user) {
		setTokenInContext(context, KEY_USER, user);
	}

	public static String getCurrentUserFromContext(Map<String, Object> context) {
		Object ret = getTokenFromContext(context, KEY_USER);
		return ret != null ? ret.toString() : "";
	}

	public static void setTokenInContext(Map<String, Object> context, String tokenName, Object tokenValue) {
		String tokenNameWithNamespace = KEY_TOKEN_NAMESPACE + tokenName;
		context.put(tokenNameWithNamespace, tokenValue);
	}
	public static Object getTokenFromContext(Map<String, Object> context, String tokenName) {
		String tokenNameWithNamespace = KEY_TOKEN_NAMESPACE + tokenName;
		return MapUtils.isNotEmpty(context) ? context.get(tokenNameWithNamespace) : null;
	}
}
