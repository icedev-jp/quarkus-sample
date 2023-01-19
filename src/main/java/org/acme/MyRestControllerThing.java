package org.acme;

import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.*;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.acme.dto.MyProductDTO;
import org.acme.entities.MyProduct;
import org.jboss.logging.Logger;

import io.quarkus.logging.Log;

@Path("/csv")
public class MyRestControllerThing {
    private static final Logger LOG = Logger.getLogger(MyRestControllerThing.class);

	@Inject
	EntityManager manager; // TODO I should separate this into another layer

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public MyProductDTO get(@QueryParam("RIMARY_KEY") String key) {
		Log.info("getting entity " + key);
		MyProduct entity = manager.find(MyProduct.class, Long.valueOf(key));
		if (entity != null) {
			return convert(entity);
		} else {
			throw new RestException("not such entity", 404);
		}
	}

	@DELETE
	@Transactional
	public void delete(@QueryParam("RIMARY_KEY") String key) {
		MyProduct entity = manager.find(MyProduct.class, Long.valueOf(key));
		if (entity != null) {
			manager.remove(entity);
		} else {
			throw new RestException("not such entity", 404);
		}
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public void upload(@FormParam("csv") File csv) throws WebApplicationException {
		AtomicInteger counter = new AtomicInteger(0);
		
		try {
			Log.info("file upload");
			new MyProductMapperCSV(csv).forEach((dto) -> {
				counter.incrementAndGet();
				MyProduct ent = convert(dto);
				manager.persist(ent);
			});
		} catch (DateTimeParseException | NumberFormatException | PersistenceException e) {
			throw new RestException("There was a problem in row #" + counter.intValue() + "\n" + e.getMessage(), 400);
		}
	}

	private MyProductDTO convert(MyProduct entity) {
		MyProductDTO dto = new MyProductDTO();
		dto.primaryKey = entity.getId().toString();
		dto.name = entity.getName();
		dto.description = entity.getDescription();
		dto.timestamp = DateTimeFormatter.ISO_INSTANT.format(entity.getTimestamp().toInstant());
		return dto;
	}

	private MyProduct convert(MyProductDTO dto) {
		MyProduct ent = new MyProduct();
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