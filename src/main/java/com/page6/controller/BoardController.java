package com.page6.controller;

import com.page6.dto.BoardDto;
import com.page6.dto.BoardFormDto;
import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.entity.BoardFile;
import com.page6.entity.Member;
import com.page6.entity.TagMap;
import com.page6.repository.BoardFileRepository;
import com.page6.repository.BoardRepository;
import com.page6.repository.TagMapRepository;
import com.page6.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/")
public class BoardController {

    @Autowired private BoardService boardService;
    @Autowired private CommentService commentService;
    @Autowired private HeartService heartService;
    @Autowired private TagService tagService;

    @Autowired  private BoardFileService boardFileService;
    @Autowired private BoardFileRepository boardFileRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private TagMapRepository tagMapRepository;


    //글 작성 페이지
    @GetMapping("/write") //localhost:8090/board/write
    public String boardWriteForm(){
        return "board/write";
    }

    //글쓰기 요청
    @PostMapping("/write")
    public String boardWritePro(@RequestParam ("files") List<MultipartFile> files,
                                @RequestParam ("tags") String tags, Board board, Principal principal) throws IOException {
        //글 저장
        String email = principal.getName();
        boardService.write(board, email);

        //태그 저장
        //태그 나누기
        String[] tagArr = tags.split("#");
        tagArr = Arrays.stream(tagArr)
                .filter(s -> !s.isEmpty())      //빈칸 없애줌
                .collect(Collectors.toCollection(LinkedHashSet::new))    //중복 없애줌
                .toArray(String[]::new);

        List<TagMap> tagMaps = tagMapRepository.findAllByBoardId(board.getId());

        //태그 저장
        for(int i = 0; i < tagArr.length; i++) {
            //게시물에 태그가 없으면, 태그 추가
            if(!tagService.hasTag(board.getId(), tagArr[i]))
                tagService.addTag(board.getId(), tagArr[i]);
        }

        //존재하지 않는 태그 삭제
        for (TagMap tagMap : tagMaps) {
            String tagName = tagMap.getTag().getName();
            if (!Arrays.asList(tagArr).contains(tagName)) {
                // tagArr에 해당 tagName이 포함되어 있지 않은 경우
                Long tagMapId = tagMap.getId();
                tagMapRepository.deleteById(tagMapId);
            }
        }

        //파일 업로드
        for(MultipartFile file : files) {
            boardFileService.saveFile(file, board.getId());
        }

        return "redirect:/board/" + board.getId();
    }

    //수정 페이지
    @GetMapping("/edit/{id}")
    public String boardEditForm(@PathVariable("id") long id, Model model) {
        //TODO: 접근하려는 사람이 작성자인지 확인하기
        //Board, Tag, 파일정보 받아오기
        BoardFormDto board = boardService.BoardOneEdit(id);
        model.addAttribute("board", board);

        List<String> tagList = tagService.getTagList(id);
        String tags = "#" + String.join("#", tagList);
        model.addAttribute("tags", tags);
        return "board/edit";
    }


    //검색&정렬&페이징
    @GetMapping({"/", "/{page}"})
    public String searchList(Model model,
                                 @RequestParam(defaultValue = "desc") String sortOrder,
                                 @RequestParam(defaultValue = "id") String sortType,
                                 @RequestParam(defaultValue = "title") String searchType,
                                 @RequestParam(defaultValue = "") String keyword,
                                 @RequestParam(defaultValue = "1") int page) {
        Sort sort;
        if (sortType.equals("member")) { // member의 name으로 정렬하는 경우
            sort = sortOrder.equalsIgnoreCase("desc") ?
                    Sort.by("member.name").descending() : Sort.by("member.name").ascending();
        } else { // 기본적으로 sortType으로 정렬하는 경우
            sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortType).descending() : Sort.by(sortType);
        }

        page = page < 1 ? 1 : page; //페이지가 1보다 작으면 1로 만들어주기
        Pageable pageable = PageRequest.of(page - 1, 12, sort);

        Page<BoardDto> list = boardService.findAll(keyword, searchType, pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage;
        int lastPage = list.getTotalPages();

        int maxPage = 10; // 최대 페이지 수
        if (list.getTotalPages() < maxPage) {
            endPage = list.getTotalPages();
        } else {
            endPage = Math.min(nowPage + 5, list.getTotalPages());
            if (nowPage < 5)
                endPage = maxPage;
            else if (nowPage < maxPage - 5)
                endPage = maxPage;
        }

        if (nowPage == 0) startPage = 1;

        model.addAttribute("boardList", list.getContent());
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortType", sortType);
        model.addAttribute("searchType", searchType);
        model.addAttribute("sortOrder", sortOrder);

        return "board/galleryList";
    }

    //게시글 조회 페이지
    @GetMapping("/board/{id}")
    public String board(@PathVariable("id") long id, Model model, Principal principal) {
        String email = "";

        //댓글 리스트 받기
        List<CommentFormDto> list = commentService.getCommentList(id);
        model.addAttribute("commentList", list);

        //좋아요 플래그 받기
        boolean likeFlag = false;
        if(principal != null) {
            email = principal.getName();
            likeFlag = heartService.heartFlag(id, email);
        }
        model.addAttribute("likeFlag", likeFlag);

        //태그 리스트 받기
        List<String> tagList = tagService.getTagList(id);
        model.addAttribute("tagList", tagList);

        //파일 리스트 받기
        List<BoardFile> fileList = boardFileService.getFileList(id);
        model.addAttribute("fileList", fileList);

        //조회수 증가 (로그인한 유저가 아니거나 글 작성자가 아니라면)
        if(email.length() == 0 || !boardService.isWriter(id, email))
            boardService.viewCntUpdate(id);

        //조회 페이지 받기
        BoardDto board = boardService.BoardOne(id);
        board.setComment_cnt(commentService.getCommentCount(id));
        model.addAttribute("board", board);

        return "/board/board";
    }

    //이미지 오류 났을 시 write.html에서 uploadUrl에 '/image/upload' -> '/board/image/upload'
    @PostMapping(value = "/uploadImage")
    public ModelAndView image(MultipartHttpServletRequest request) throws Exception {

        // ckeditor는 이미지 업로드 후 이미지 표시하기 위해 uploaded 와 url을 json 형식으로 받아야 함
        // modelandview를 사용하여 json 형식으로 보내기위해 모델앤뷰 생성자 매개변수로 jsonView 라고 써줌
        // jsonView 라고 쓴다고 무조건 json 형식으로 가는건 아니고 @Configuration 어노테이션을 단
        // WebConfig 파일에 MappingJackson2JsonView 객체를 리턴하는 jsonView 매서드를 만들어서 bean으로 등록해야 함
        ModelAndView mav = new ModelAndView("jsonView");

        // ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어오기 때문에 upload라는 키의 밸류를 받아서 uploadFile에 저장함
        MultipartFile uploadFile = request.getFile("upload");
        Long imgId = boardFileService.saveFile(uploadFile, 1L);
        String uploadPath = boardFileRepository.findById(imgId).get().getFilePath();

//        // 파일의 오리지널 네임
//        String originalFileName = uploadFile.getOriginalFilename();
//
//        // 파일의 확장자
//        String ext = originalFileName.substring(originalFileName.indexOf("."));
//
//        // 서버에 저장될 때 중복된 파일 이름인 경우를 방지하기 위해 UUID에 확장자를 붙여 새로운 파일 이름을 생성
//        String newFileName = UUID.randomUUID() + ext;
//
//        // 이미지를 현재 경로와 연관된 파일에 저장하기 위해 현재 경로를 알아냄
//        String realPath = request.getServletContext().getRealPath("/");
//
//        // 현재경로/upload/파일명이 저장 경로
//        String savePath = realPath + "/" + newFileName;
//
//        // 브라우저에서 이미지 불러올 때 절대 경로로 불러오면 보안의 위험 있어 상대경로를 쓰거나 이미지 불러오는 jsp 또는 클래스 파일을 만들어 가져오는 식으로 우회해야 함
//        // 때문에 savePath와 별개로 상대 경로인 uploadPath 만들어줌
//        String uploadPath = "./" + newFileName;
//
//        // 저장 경로로 파일 객체 생성
//        File file = new File(savePath);
//
//        // 파일 업로드
//        uploadFile.transferTo(file);

        // uploaded, url 값을 modelandview를 통해 보냄
        mav.addObject("uploaded", true); // 업로드 완료
        mav.addObject("url", uploadPath); // 업로드 파일의 경로

        return mav;
    }

}

