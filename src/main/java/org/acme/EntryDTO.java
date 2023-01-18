package org.acme;

public class EntryDTO {
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
