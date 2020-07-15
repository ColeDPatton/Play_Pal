package com.example.application.file;

import com.example.application.file.FileStorageProperties;
import com.example.application.user.User;
import com.example.application.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/*
 * This class creates a file service that is used for storing the files and creating the path for storage. 
 */
@Service
public class DBfileService {

	@Autowired
	UserService userService;
	@Autowired
	private DBfileRepository dbFileRepository;
	private final Path fileStorageLocation;

	@Autowired
	public DBfileService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		//this.fileStorageLocation = Paths.get("./home/cdpatton/uploads");
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
		}
	}

	/*
	 * Stores file along with id and game. First Normalize file name, and then is
	 * copies the file to the target location, replacing the existing file with the
	 * same name
	 */
	public String storeFile(MultipartFile file, long id, String game) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			System.out.println(fileName + " " + game + " " + id);
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			User temp = userService.get(id).get();
			dbFileRepository.save(new DBfile(fileName, file.getContentType(), file.getBytes(), temp, game));
			return fileName;
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
			return null;
		}
	}

	/*
	 * Loads the file a resource, returns null if resource does not exist
	 */
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			System.out.println(filePath.toUri().toString());
			Resource resource = new UrlResource(filePath.toUri());
			
			if (resource.exists()) {
				return resource;
			} else {
				return null;
			}
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/*
	 * returns all files by name id
	 */
	public List<String> getFileNames(long id) {
		return dbFileRepository.getListOfFileNames(id);
	}

	/*
	 * return file by ID and game
	 */
	public String getFileNames(long id, String game) {
		return dbFileRepository.getFileName(id, game);
	}
//	public String getFileNames(String name) {
//    	return dbFileRepository.getFileName(name);
//	}
}
