package com.page6.service;

import com.page6.dto.BoardFileDto;
import com.page6.entity.Board;
import com.page6.entity.BoardFile;
import com.page6.repository.BoardFileRepository;
import com.page6.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log
public class BoardFileService {
    @Value("${uploadPath}")
    String uploadPath;

    private final BoardFileRepository boardFileRepository;
    private final BoardRepository boardRepository;

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
}
