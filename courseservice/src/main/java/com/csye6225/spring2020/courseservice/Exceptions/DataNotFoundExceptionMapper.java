package com.csye6225.spring2020.courseservice.Exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.csye6225.spring2020.courseservice.datamodel.ErrorMessage;

@Provider

public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {
	@Override
	public Response toResponse(DataNotFoundException ex) {
		ErrorMessage erMessage = new ErrorMessage(ex.getMessage(), 404);
		return Response.status(Status.NOT_FOUND).entity(erMessage).build();
	}

}
