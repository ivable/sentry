package com.ivable.common.sentry.test.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.intershop.beehive.core.capi.log.Logger;
import com.intershop.component.rest.capi.resource.AbstractRestResource;

/**
 * TODO doc
 */
public class SentryTestResource extends AbstractRestResource
{
    public enum StatusRO
    {
        OK, NOTOK
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public StatusRO testResource()
    {
        return StatusRO.OK;
    }

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public StatusRO sendErrorMsg(@QueryParam(value = "msg") String msg, @QueryParam(value = "level") String level)
    {
        level = level == null ? "" : level;
        
        switch(level)
        {
            case "error":
                Logger.error(this, msg);
                break;
            case "warn":
                Logger.warn(this, msg);
                break;
            case "info":
                Logger.info(this, msg);
                break;
            case "debug":
                Logger.debug(this, msg);
                break;
            case "trace":
                Logger.trace(this, msg);
                break;
            default:
                Logger.error(this, msg);
                break;
        }

        return StatusRO.OK;
    }
}