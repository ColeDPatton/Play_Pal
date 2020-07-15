package com.example.application.file;

import com.example.application.file.UploadFileResponse;
import com.example.application.file.DBfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * This class create a controlled that is used for downloading files from the user, UserID, and the game. 
 */
@RestController
public class DBfileController {

	@Autowired
	private DBfileService dbFileService;

//	public static class uploadFileRequest {
//		private MultipartFile file;
//		private long id;
//		private String game;
//		
//		public uploadFileRequest(MultipartFile file, long id, String game) {
//			this.file = file;
//			this.id = id;
//			this.game = game;
//		}
//		
//		public MultipartFile getFile() {
//			return file;
//		}
//		public void setFile(MultipartFile file) {
//			this.file = file;
//		}
//		public long getId() {
//			return id;
//		}
//		public void setId(long id) {
//			this.id = id;
//		}
//		public String getGame() {
//			return game;
//		}
//		public void setGame(String game) {
//			this.game = game;
//		}
//	}
	/*
	 * Uploads file to the database
	 */
	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") long id,
			@RequestParam("game") String game) {
		String fileName = dbFileService.storeFile(file, id, game);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());

	}

//    @PostMapping("/uploadMultipleFiles")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestBody uploadFileRequest ufr) {
//        return Arrays.asList(ufr.getFile())
//                .stream()
//                .map(file -> uploadFile(file, id, game))
//                .collect(Collectors.toList());
//    }

	/*
	 * downloads file to the database. Loads the file as a resource, tries to
	 * determine file's content type fallback to the default content type if the
	 * type could not be determined
	 */
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = dbFileService.loadFileAsResource(fileName);
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {

		}
		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/*
	 * downloads file from the User. Loads the file as a resource, tries to
	 * determines the file's content type, and fallbacks to the deault content type
	 * if the could not be determined
	 */
	@GetMapping("/downloadFileFromUser/{id:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable long id, HttpServletRequest request) {
		// Load file as Resource
		List<ResponseEntity<Resource>> images = new ArrayList<ResponseEntity<Resource>>();
		List<String> fileNames = dbFileService.getFileNames(id);

		for (String name : fileNames) {
			Resource resource = dbFileService.loadFileAsResource(name);
			// Try to determine file's content type
			String contentType = null;
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {

			}
			// Fallback to the default content type if type could not be determined
			if (contentType == null) {
				contentType = "application/octet-stream";
			}
			images.add(ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource));
		}
		return images.get(0);
	}

	/*
	 * Downloads file from the game, loads the file as a resource, tries to
	 * determines the file's content type, and fallbacks to the deault content type
	 * if the could not be determined
	 */
	@PostMapping("/downloadFileFromGame")
	public ResponseEntity<Resource> downloadFileFromGame(@RequestParam("id") long id, @RequestParam("game") String game,
			HttpServletRequest request) {
		String fileName = dbFileService.getFileNames(id, game);
		System.out.println(fileName);
		// Load file as Resource
		Resource resource = dbFileService.loadFileAsResource(fileName);
		if(resource==null) {
			System.out.println("FUCKED");
		}
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {

		}

		// Fallback to the default content type if type could not be determined

		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/*
	 * Loads the file as a resource
	 */
	@GetMapping("/getFileNamesFromUser/{id}")
	public List<String> downloadFileNames(@PathVariable long id) {
		// Load file as Resource
		List<String> fileName = dbFileService.getFileNames(id);
		return fileName;
	}

}