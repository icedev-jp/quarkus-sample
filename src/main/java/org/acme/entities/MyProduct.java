package org.acme.entities;

import java.sql.Timestamp;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;

/**
 * Example JPA entity.
 *
 * To use it, get access to a JPA EntityManager via injection.
 *
 * {@code @Inject EntityManager em;
 *
 * public void doSomething() { MyEntity entity1 = new MyEntity();
 * entity1.setField("field-1"); em.persist(entity1);
 *
 * List<MyEntity> entities = em.createQuery("from MyEntity",
 * MyEntity.class).getResultList(); } }
 */
@Entity
public class MyProduct {
	private Long id;
	private String name;
	private String description;

//	@CreationTimestamp
//	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp timestamp;

	@Id
//	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String field) {
		this.name = field;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	

    @PrePersist
    protected void onCreate() {
    	if(timestamp == null) {
    		timestamp = new Timestamp(System.currentTimeMillis());
    	}
    }
}
