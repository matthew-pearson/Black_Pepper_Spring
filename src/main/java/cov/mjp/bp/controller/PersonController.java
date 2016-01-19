package cov.mjp.bp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import cov.mjp.bp.model.Person;

@RestController
public class PersonController {
    
	private final static Logger log = LoggerFactory.getLogger(PersonController.class);
	private static final String PATH_TO_PERSON_FILE = "c:\\my output test\\test\\mypostfile.txt";
	private static final String PATH_TO_CHECKSUM_FILE = "c:\\my output test\\test\\checksum.txt";
	
	@RequestMapping(value = "/", method = RequestMethod.POST)//, consumes="application/x-www-form-urlencoded")
    public ResponseEntity<Person> post(Person person) throws IOException {
		log.info("Received person: " + person);
		writeToFile(person);
		return new ResponseEntity<Person>(HttpStatus.CREATED);	// TODO Should really return an id to locate this entity, but we're only storing one.
    }
	
	private void writeToFile(Person person) throws IOException {
		try {
			Files.write(Paths.get(PATH_TO_PERSON_FILE), new Gson().toJson(person).getBytes());
			Files.write(Paths.get(PATH_TO_CHECKSUM_FILE), String.valueOf(person.hashCode()).getBytes());
		} catch (IOException e) {
			log.error("Error writing to file", e);
			throw e;
		}
	}
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public Person get() throws IOException {
		String fileContents = readPersonFromFile();
		Person person = new Gson().fromJson(fileContents, Person.class);
		testCheckSum(person);
		return person;
	}
	
	private String readPersonFromFile() throws IOException {
		try {
			return new String(Files.readAllBytes(Paths.get(PATH_TO_PERSON_FILE)));
		} catch (IOException e) {
			log.error("Failed to read from file " + PATH_TO_PERSON_FILE, e);
			throw e;
		}
	}

	private void testCheckSum(Person person) throws IOException {
		int recordedCheckSum = Integer.parseInt(readChecksumFromFile());
		int newCheckSum = person.hashCode();
		if  (newCheckSum == recordedCheckSum) {
			log.info("Checksum value of " + newCheckSum + " tested and valid.");
			
		} else {
			// I guess we can call this an IOException.
			IOException e =  new IOException("Checksum value of " + newCheckSum + " tested and invalid. It should be " + recordedCheckSum);
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	private String readChecksumFromFile() throws IOException {
		try {
			return new String(Files.readAllBytes(Paths.get(PATH_TO_CHECKSUM_FILE)));
		} catch (IOException e) {
			log.error("Failed to read from file " + PATH_TO_CHECKSUM_FILE, e);
			throw e;
		}
	}
}
