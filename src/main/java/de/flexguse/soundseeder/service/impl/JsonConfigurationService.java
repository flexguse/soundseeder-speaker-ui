/**
 * 
 */
package de.flexguse.soundseeder.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.flexguse.soundseeder.model.SpeakerConfiguration;
import de.flexguse.soundseeder.service.ConfigurationService;
import de.flexguse.soundseeder.service.ConfigurationServiceException;

/**
 * This implementation writes the configuration as JSON to the /data folder.
 * 
 * @author Christoph Guse, info@flexguse.de
 *
 */
public class JsonConfigurationService implements ConfigurationService {

	private ObjectMapper objectMapper;

	/**
	 * The name of the JSON file in which the speaker configuration is stored.
	 */
	private final String dataFileName = "speaker.conf";

	// the absolute path to the data folder
	private Path dataFile;

	public JsonConfigurationService() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		dataFile = Paths.get(System.getProperty("user.dir"), "data", dataFileName);

	}

	/**
	 * Deletes the configuration file. Needed just for testing purposes.
	 */
	public void removeConfigurationFile() throws ConfigurationServiceException {

		try {
			Files.delete(dataFile);
			Files.delete(dataFile.getParent());

		} catch (NoSuchFileException e) {
			// intended to do nothing
		} catch (IOException e) {
			throw new ConfigurationServiceException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.flexguse.soundseeder.service.ConfigurationService#saveConfiguration(de
	 * .flexguse.soundseeder.model.SpeakerConfiguration)
	 */
	@Override
	public void saveConfiguration(SpeakerConfiguration speakerConfiguration) throws ConfigurationServiceException {

		try {

			if (!Files.exists(dataFile.getParent())) {
				Files.createDirectory(dataFile.getParent());
			}

			if (!Files.exists(dataFile)) {
				Files.createFile(dataFile);
			}

			objectMapper.writeValue(dataFile.toFile(), speakerConfiguration);
		} catch (IOException e) {
			throw new ConfigurationServiceException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.flexguse.soundseeder.service.ConfigurationService#loadConfiguration()
	 */
	@Override
	public SpeakerConfiguration loadConfiguration() throws ConfigurationServiceException {

		try {
			return objectMapper.readValue(dataFile.toFile(), SpeakerConfiguration.class);
		} catch (FileNotFoundException e) {
			return null;
		}

		catch (IOException e) {
			throw new ConfigurationServiceException(e);
		}

	}

}
