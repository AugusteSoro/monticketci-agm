package com.wizard.monticketci.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStorageTools {
	
	@Autowired
	Environment env;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Path rootLocation = Paths.get("upload-dir");



		
	public String store(MultipartFile file ,String filename , String pathfile) {
		try {
			logger.trace(" filename : "+filename+" ; pathfile : "+pathfile);
			Path rootLocation = Paths.get(pathfile);
			Files.copy(file.getInputStream(), rootLocation.resolve(filename));
			return filename ;

		} catch (Exception e) {	
			logger.error(" error : "+e.getMessage());
			throw new RuntimeException("FAIL!");
		}
	}
	
	
	
	public Resource loadImage(String filename) {
		try {

			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (Exception ex) {
			throw new RuntimeException("FAIL!");
		}
	}
	

}
