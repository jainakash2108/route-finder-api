package no.route.finder.routefinderapi.controller;

import no.route.finder.routefinderapi.exception.CsvFileNotFoundException;
import no.route.finder.routefinderapi.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new CsvFileNotFoundException("There is no file");
        }
        boolean validExtension = file.getOriginalFilename().endsWith(".csv") || file.getOriginalFilename().endsWith(".CSV");
        if (!validExtension) {
            throw new CsvFileNotFoundException("File extension must be .csv or .CSV");
        }
        fileService.process(file);
        return ok().build();
    }

}
