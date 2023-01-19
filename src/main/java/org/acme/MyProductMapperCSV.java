package org.acme;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import javax.ws.rs.WebApplicationException;

import org.acme.dto.MyProductDTO;
import org.apache.commons.csv.*;

/**
 * This abomination was created because I couldn't find a way to auto unmarshall a CSV file.
 */
public class MyProductMapperCSV {
	private final File csv;

	public MyProductMapperCSV(File csv) {
		this.csv = csv;
	}
	
	protected MyProductDTO mapRecordToDto(CSVRecord record) {
		MyProductDTO dto = new MyProductDTO();
		dto.primaryKey = record.get(0).strip();
		dto.name = record.get(1).strip();
		dto.description = record.get(2).strip();
		dto.timestamp = record.get(3).strip();
		return dto;
	}
	
	private static final List<String> HEADERS = List.of("PRIMARY_KEY", "NAME", "DESCRIPTION", "UPDATED_TIMESTAMP");
	
	private void verifyHeader(CSVRecord record) {
		for(int i=0; i<HEADERS.size(); i++) {
			
			String entry = record.get(i).strip();
			String valid = HEADERS.get(i);
			
			if(!entry.equals(valid)) {
				// TODO maybe throw some sort of parse exception instead
				throw new WebApplicationException("Wrong header field " + entry + " was expecting " + valid);
			}
		}
	}
	
	public void forEach(Consumer<MyProductDTO> fun) {
    	try (CSVParser parser = new CSVParser(new FileReader(csv), CSVFormat.DEFAULT)) {
			Spliterator<CSVRecord> spliter = parser.spliterator();
			
			spliter.tryAdvance(this::verifyHeader);
			
			StreamSupport.stream(spliter, false)
				.map(this::mapRecordToDto)
				.forEach(fun);
			
    	} catch (IOException e) {
    		throw new WebApplicationException(e.getMessage(), 500);
		}
	}

}
