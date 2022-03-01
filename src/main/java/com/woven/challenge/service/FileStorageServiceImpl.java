package com.woven.challenge.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.woven.challenge.exceptions.FileNotFoundException;
import com.woven.challenge.exceptions.UnexpectedStorageException;

@Service
@PropertySource("classpath:application.properties")
public class FileStorageServiceImpl implements FileStorageService {

	@Value("${file.storage.upload.folder}")
	private String uploadFolder;

	private Path rootLocation;

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new UnexpectedStorageException("Could not initialize storage", e);
		}
	}

	@Override
	public void save(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new UnexpectedStorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new UnexpectedStorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new UnexpectedStorageException("Failed to store file.", e);
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new FileNotFoundException(
						"Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new UnexpectedStorageException("Failed to read stored files", e);
		}

	}

	@Override
	public void delete(String filename) {
		Path file = load(filename);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new UnexpectedStorageException("Could not delete file" + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		rootLocation = Paths.get(uploadFolder);
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
}
