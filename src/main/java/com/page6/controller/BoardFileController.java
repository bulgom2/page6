package com.page6.controller;

import com.page6.entity.BoardFile;
import com.page6.repository.BoardFileRepository;
import com.page6.service.BoardFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;
import org.slf4j.LoggerFactory;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.nio.charset.StandardCharsets;



@Controller
@RequestMapping(value = "/")
public class BoardFileController {

    @Autowired private BoardFileService boardFileService;
    @Autowired private BoardFileRepository boardFileRepository;
    private static final Logger logger = Logger.getLogger(BoardFileController.class.getName());


    @GetMapping("/board/file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) throws MalformedURLException, FileNotFoundException {
        Optional<BoardFile> optionalBoardFile = boardFileRepository.findById(id);
        BoardFile boardFile = optionalBoardFile.orElseThrow(() -> new FileNotFoundException("File not found"));

        Resource resource = new UrlResource(boardFile.getFilePath());

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + boardFile.getOriFileName() + "\"")
                .body(resource);
    }



    @GetMapping("/attach/{itemId}")
    public ResponseEntity<UrlResource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        return boardFileService.downloadFile(itemId);
    }

}
