package com.page6.service;

import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import com.page6.dto.BoardFileDto;
import com.page6.entity.Board;
import com.page6.entity.BoardFile;
import com.page6.repository.BoardFileRepository;
import com.page6.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log
public class BoardFileService {
    @Value("${uploadPath}")
    String uploadPath;

    private final BoardFileRepository boardFileRepository;
    private final BoardRepository boardRepository;

    //파일 저장
    public Long saveFile(MultipartFile uploadFile, Long boardId) throws IOException {
        if(uploadFile.isEmpty()) return null;

        // 파일의 오리지널 네임
        String originalFileName = uploadFile.getOriginalFilename();
        System.out.println("originalFileName=" + originalFileName);

        // 파일의 확장자
        String ext = originalFileName.substring(originalFileName.indexOf("."));

        // 서버에 저장될 때 중복된 파일 이름인 경우를 방지하기 위해 UUID에 확장자를 붙여 새로운 파일 이름을 생성
        String newFileName = UUID.randomUUID() + ext;
        System.out.println("newFileName=" + newFileName);

        // 파일 불러올 때 사용할 파일 경로
        String savePath = uploadPath + "/" +newFileName;
        System.out.println("savePath=" + savePath);

        // 파일 업로드
        uploadFile.transferTo(new File(savePath));

        // 파일 엔티티 생성
        BoardFile file = new BoardFile();
        Board board = boardRepository.findById(boardId).get();
        file.updateFile(originalFileName, newFileName, savePath, board);
        BoardFile savedFile = boardFileRepository.save(file);

        return savedFile.getId();
    }

    //파일 조회
    public List<BoardFile> getFileList(Long bid) {
        Board board = boardRepository.findById(bid).get();
        return boardFileRepository.findAllByBoard(board);
    }

    //파일 다운로드
    public ResponseEntity<UrlResource> downloadFile(Long fileId) throws MalformedURLException {
        Optional<BoardFile> findFile = boardFileRepository.findById(fileId);
        BoardFile file = findFile.orElse(null);
        if(file == null) return null;
        String fileName = file.getFileName();       //storeFileName
        String oriFileName = file.getOriFileName(); //uploadFileName
        String encodedUploadFileName = UriUtils.encode(oriFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        System.out.println("contentDisposition=" + contentDisposition);
        String filePath = file.getFilePath();
//        if(!filePath.endsWith("/")) {
//            filePath += "/";
//        }
        UrlResource resource = new UrlResource("file:" + filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


}
