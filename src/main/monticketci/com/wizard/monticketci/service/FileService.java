package com.wizard.monticketci.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.tools.StorageException;




@Service
public class FileService {
	
	@Log
	private Logger log;

	@Value("${event.upload.path}")
	private String pathEvent;
	
	@Value("${typeevent.upload.path}")
	private String pathTypeEvent;
	
	/*@Value("${joueur.upload.path}")
	private String pathJoueur;
	
	@Value("${info.upload.path}")
	private String pathInfo;*/

	public boolean uploadFile(MultipartFile file, String type) {

		if (file.isEmpty()) {
			return false;
		}

		try {
			String fileName = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			String path = "event";
			switch (type) {
			case "event":
				path = pathEvent;
				break;
			case "typeevent":
				path = pathTypeEvent;
				break;
			/*case "joueur":
				path = pathJoueur;
				break;*/

			default:
				break;
			}
			

			Files.copy(is, Paths.get(path + fileName), StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {

			log.error("Erreur d'enregistrement(Exception): "+e.getMessage());

			//String msg = String.format("Failed to store file", file.getName());

			return false;
			
		}

	}

	public Resource loadFileAsResource(String fileName) {
		try {
			// Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			
			log.debug("image path to load,{}" ,fileName);
			
		  Path	filePath = Paths.get( "var/opt/wizard/monticketci/image/default.png").normalize();
			log.debug("image path to load normalize,{}" ,filePath);
			log.debug("image path to load toUri ,{}" ,filePath.toUri());

			
		//	log.debug("xxxxx image path to load Uri ,{}" ,aa);
			
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new StorageException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new StorageException("File not found " + fileName, ex);
		}
	}

}
