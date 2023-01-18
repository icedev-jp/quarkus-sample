package org.acme;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.*;
import java.time.temporal.TemporalAccessor;

import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.quarkus.logging.Log;

@Path("/csv")
public class MyProductRestResource {
    private static final Logger LOG = Logger.getLogger(MyProductRestResource.class);

	@Inject
	EntityManager manager; // TODO I should separate this into another layer

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EntryDTO get(@QueryParam("RIMARY_KEY") String key) {
		Log.info("getting entity " + key);
		MyEntity entity = manager.find(MyEntity.class, Long.valueOf(key));
		if (entity != null) {
			return convert(entity);
		} else {
			throw new WebApplicationException(404);
		}
	}

	@DELETE
	@Transactional
	public void delete(@QueryParam("RIMARY_KEY") String key) {
		MyEntity entity = manager.find(MyEntity.class, Long.valueOf(key));
		if (entity != null) {
			manager.remove(entity);
		} else {
			throw new WebApplicationException(404);
		}
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public void upload(@FormParam("csv") File csv) throws WebApplicationException {
		try {
			Log.info("file upload");
			
			new MyEntryCsvMapper(csv).forEach((dto) -> {
				MyEntity ent = convert(dto);
				manager.persist(ent);
			});
		} catch (DateTimeParseException | NumberFormatException | PersistenceException e) {
			// TODO better error handling?
			// 1. error messages are not included in response object, try building custom response
			// 2. need to provide information about which CSV entry was faulty
			// 3. this doesn't seem very elegant as it is now
			throw new WebApplicationException(e.getMessage(), 400);
		}
	}

	private EntryDTO convert(MyEntity entity) {
		EntryDTO dto = new EntryDTO();
		dto.primaryKey = entity.getId().toString();
		dto.name = entity.getName();
		dto.description = entity.getDescription();
		dto.timestamp = DateTimeFormatter.ISO_INSTANT.format(entity.getTimestamp().toInstant());
		return dto;
	}

	private MyEntity convert(EntryDTO dto) {
		MyEntity ent = new MyEntity();
		ent.setId(Long.valueOf(dto.primaryKey));
		ent.setName(dto.name);
		ent.setDescription(dto.description);
		if (!dto.timestamp.isBlank()) {
			TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(dto.timestamp);
			Timestamp timestamp = Timestamp.from(Instant.from(ta));
			ent.setTimestamp(timestamp);
		}
		return ent;
	}

}