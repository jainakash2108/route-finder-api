package no.route.finder.routefinderapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {


    void process(MultipartFile file);
}
