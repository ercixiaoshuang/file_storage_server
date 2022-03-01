package com.woven.challenge.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.woven.challenge.message.ResponseMessage;
import com.woven.challenge.model.FileMeta;
import com.woven.challenge.service.FileStorageService;

@Controller
@CrossOrigin("http://localhost:8081")
public class FilesController {
	@Autowired
	public FileStorageService storageService;

	@PostMapping("/files")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

		String message = "";
		try {
			storageService.save(file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileMeta>> getListFiles(Model model) throws IOException {

		List<FileMeta> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.toAbsolutePath().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
			return new FileMeta(filename, url);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@DeleteMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
		storageService.delete(filename);
		String message = "Deleted the file successfully: " + filename;
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}

}
