package com.woven.challenge.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

	public void init();

	public void save(MultipartFile file);

	public Path load(String filename);

	public Resource loadAsResource(String filename);

	public void deleteAll();

	public Stream<Path> loadAll();

	public void delete(String filename);

}
