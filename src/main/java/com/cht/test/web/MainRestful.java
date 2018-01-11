package com.cht.test.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/service")
public class MainRestful {

    @Path("/queryDate/{querydate}")
    @GET
    @Produces("text/plain")
    public String queryDate(@PathParam("querydate") String date) {
        System.out.println("test:" + date);
        return "test:" + date;
    }
}
