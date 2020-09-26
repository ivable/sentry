package com.ivable.common.sentry.internal;

import java.util.Map;
import java.util.Set;

import com.intershop.beehive.core.capi.request.Request;

import io.sentry.event.EventBuilder;
import io.sentry.event.helper.EventBuilderHelper;

/**
 * ICM aware Event Builder. Logs the pipeline dictionary content with the event.
 * 
 * @author willem
 */
public class ICMEventBuilderHelperImpl implements EventBuilderHelper
{
    @Override
    public void helpBuildingEvent(EventBuilder eventBuilder)
    {
        if (Request.getCurrent() == null || Request.getCurrent().getPipelineDictionary() == null)
        {
            return;
        }

        @SuppressWarnings("unchecked")
        Set<Map.Entry<String, Object>> entrySet = Request.getCurrent().getPipelineDictionary().entrySet();
        // add pipeline dict key/values to event
        entrySet.stream().forEach(e -> eventBuilder.withExtra(e.getKey(), e.getValue()));
    }
}
