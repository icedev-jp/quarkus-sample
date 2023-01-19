package org.acme.dto;

public class MyProductDTO {
	public String primaryKey;
	public String name;
	public String description;
	public String timestamp;
	
	
	
	@Override
	public String toString() {
		return "EntryDTO [primaryKey=" + primaryKey + ", name=" + name + ", description=" + description + ", timestamp="
				+ timestamp + "]";
	}
}
