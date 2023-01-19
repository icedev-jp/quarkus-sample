package org.acme;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;

public class RestException extends WebApplicationException {
	private static final long serialVersionUID = 8239006508417915824L;

	public RestException(String message, int status) {
		super(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
