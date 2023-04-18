package com.page6.controller;

import com.page6.dto.BoardDto;
import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.entity.Member;
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
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final HeartService heartService;
    private final TagService tagService;

    //글 작성 페이지
    @GetMapping("/write") //localhost:8090/board/write
    public String boardWriteForm(){
        return "board/write";
    }

    //글쓰기 요청
    @PostMapping("/write")
    public String boardWritePro(@RequestParam ("tags") String tags, Board board, Principal principal){
        //글 저장
        String email = principal.getName();
        boardService.write(board, email);

        //태그 저장
        String[] tagArr = tags.split("#");
        tagArr = Arrays.stream(tagArr)
                .filter(s -> !s.isEmpty())      //빈칸 없애줌
                .collect(Collectors.toCollection(LinkedHashSet::new))    //중복 없애줌
                .toArray(String[]::new);

        for(int i = 0; i < tagArr.length; i++)
            tagService.addTag(board.getId(), tagArr[i]);

        return "redirect:/board/" + board.getId();
    }

    // 기본 목록
//    @GetMapping("/")
//    public String boardList(Model model) {
//        List<BoardDto> boardList = boardService.getBoardList();
//        model.addAttribute("boardList", boardList);
//
//        return "board/galleryList";
//    }



    //검색 페이징
    @GetMapping({"/search", "/search/{page}"})
    public String searchList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {

        Page<BoardDto> list = boardService.findAll(pageable);

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = list.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        int lastPage = list.getTotalPages() - 1;

        if (nowPage < 5){
            endPage = 10;
        }
        else if (nowPage < 10){
            endPage = 10 + (nowPage - 5);
        }
        if (nowPage == 0) {
            startPage = 1;
        }

        model.addAttribute("boardList", list.getContent());
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "board/galleryList";
    }

//    // 내 페이징
    @GetMapping({"/", "/{page}"})
    public String boardList(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {

        Page<BoardDto> list = boardService.findAll(pageable);

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = list.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        int lastPage = list.getTotalPages() - 1;

        if (nowPage < 5){
            endPage = 10;
        }
        else if (nowPage < 10){
            endPage = 10 + (nowPage - 5);
        }
        if (nowPage == 0) {
            startPage = 1;
        }

        model.addAttribute("boardList", list.getContent());
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);

        return "board/galleryList";
    }

    @GetMapping("/sort/{sortType}")
    public String sortedList(
            @PathVariable("sortType") Long sortType,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id");

        Page<Board> sortPage = null;

        if (sortType == 1) {
            sortPage = boardService.findAllByOrderByName(pageable);
        } else if (sortType == 2) {
            sortPage = boardService.findAllByOrderByTitle(pageable);
        } else if (sortType == 3) {
            sortPage = boardService.findAllByOrderByIdDesc(pageable);
        } else if (sortType == 4) {
            sortPage = boardService.findAllByOrderByLikesDesc(pageable);
        } else if (sortType == 5) {
            sortPage = boardService.findAllByOrderByViewsDesc(pageable);
        }

        List<BoardDto> boardDtoList = sortPage.getContent()
                .stream()
                .map(board -> {
                    BoardDto boardDto = BoardDto.of(board);
                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId())); // 댓글 개수
                    return boardDto;
                })
                .collect(Collectors.toList());

        Page<BoardDto> list  = new PageImpl<>(boardDtoList, pageable, sortPage.getTotalElements());

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        int lastPage = list.getTotalPages() - 1;

        if (nowPage < 5) {
            endPage = 10;
        } else if (nowPage < 10) {
            endPage = 10 + (nowPage - 5);
        }
        if (nowPage == 0) {
            startPage = 1;
        }

        model.addAttribute("boardList", list.getContent());
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("sortType", sortType);

        return "board/galleryList";
    }



// 정렬 레퍼런스 1

//    @GetMapping("/{page}/{sort-type}")
//    public String boardList2(@PathVariable("sort-type") String sortType, Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
//
//        Page<Board> sortPage = null;
//
//        if (sortType.equals("name")) {
//            sortPage = boardService.findAllByOrderByName(pageable);
//        } else if (sortType.equals("title")) {
//            sortPage = boardService.findAllByOrderByTitleAsc(pageable);
//        } else if (sortType.equals("id")) {
//            sortPage = boardService.findAllByOrderByIdDesc(pageable);
//        } else if (sortType.equals("likes")) {
//            sortPage = boardService.findAllByOrderByLikesDesc(pageable);
//        } else if (sortType.equals("views")) {
//            sortPage = boardService.findAllByOrderByViewsDesc(pageable);
//        }
//
//        List<BoardDto> boardDtoList = sortPage.getContent()
//                .stream()
//                .map(board -> {
//                    BoardDto boardDto = BoardDto.of(board);
//                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId())); // 댓글 개수
//                    return boardDto;
//                })
//                .collect(Collectors.toList());
//        Page<BoardDto> list = new PageImpl<>(boardDtoList, pageable, sortPage.getTotalElements());
//
//        //페이지블럭 처리
//        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
//        int nowPage = list.getPageable().getPageNumber() + 1;
//        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
//        int startPage =  Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 5, list.getTotalPages());
//        int lastPage = list.getTotalPages() - 1;
//
//        if (nowPage < 5) {
//            endPage = 10;
//        }
//        else if (nowPage < 10) {
//            endPage = 10 + (nowPage - 5);
//        }
//        if (nowPage == 0) {
//            startPage = 1;
//        }
//
//        model.addAttribute("boardList", list.getContent());
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("lastPage", lastPage);
//
//        return "board/galleryList";
//    }

//    @GetMapping("/sorted-by-title")
//    public String boardListSortedByTitle(Model model,
//                                         @RequestParam(value = "page", defaultValue = "1") int pageNum) {
//        Page<Board> list = boardService.getAllBoardsSortedByTitle(pageNum);
//
//        //페이지블럭 처리
//        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
//        int nowPage = list.getPageable().getPageNumber() + 1;
//        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
//        int startPage =  Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 5, list.getTotalPages());
//        int lastPage = list.getTotalPages() - 1;
//
//        if (nowPage < 5){
//            endPage = 10;
//        }
//        else if (nowPage < 10){
//            endPage = 10 + (nowPage - 5);
//        }
//        if (nowPage == 0) {
//            startPage = 1;
//        }
//
//        model.addAttribute("boardList", list.getContent());
//        model.addAttribute("nowPage", pageNum);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("lastPage", lastPage);
//        return "board/galleryList";
//    }

    //게시글 조회 페이지
    @GetMapping("/board/{id}")
    public String board(@PathVariable("id") long id, Model model, Principal principal) {
        //조회수 증가
        boardService.viewCntUpdate(id);

        //조회 페이지 받기
        BoardDto board = boardService.BoardOne(id);
        board.setComment_cnt(commentService.getCommentCount(id));
        model.addAttribute("board", board);

        //댓글 리스트 받기
        List<CommentFormDto> list = commentService.getCommentList(id);
        model.addAttribute("commentList", list);

        //좋아요 플래그 받기
        boolean likeFlag = false;
        if(principal != null) {
            String email = principal.getName();
            likeFlag = heartService.heartFlag(id, email);
        }
        model.addAttribute("likeFlag", likeFlag);

        //태그 리스트 받기
        List<String> tagList = tagService.getTagList(id);
        model.addAttribute("tagList", tagList);
//        log.info("board={}", board);
        return "/board/board";
    }



    //이미지 오류 났을 시 write.html에서 uploadUrl에 '/image/upload' -> '/board/image/upload'
    @PostMapping(value = "/image/upload")
    public ModelAndView image(MultipartHttpServletRequest request) throws Exception {

        // ckeditor는 이미지 업로드 후 이미지 표시하기 위해 uploaded 와 url을 json 형식으로 받아야 함
        // modelandview를 사용하여 json 형식으로 보내기위해 모델앤뷰 생성자 매개변수로 jsonView 라고 써줌
        // jsonView 라고 쓴다고 무조건 json 형식으로 가는건 아니고 @Configuration 어노테이션을 단
        // WebConfig 파일에 MappingJackson2JsonView 객체를 리턴하는 jsonView 매서드를 만들어서 bean으로 등록해야 함
        ModelAndView mav = new ModelAndView("jsonView");

        // ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어오기 때문에 upload라는 키의 밸류를 받아서 uploadFile에 저장함
        MultipartFile uploadFile = request.getFile("upload");

        // 파일의 오리지널 네임
        String originalFileName = uploadFile.getOriginalFilename();

        // 파일의 확장자
        String ext = originalFileName.substring(originalFileName.indexOf("."));

        // 서버에 저장될 때 중복된 파일 이름인 경우를 방지하기 위해 UUID에 확장자를 붙여 새로운 파일 이름을 생성
        String newFileName = UUID.randomUUID() + ext;

        // 이미지를 현재 경로와 연관된 파일에 저장하기 위해 현재 경로를 알아냄
        String realPath = request.getServletContext().getRealPath("/");

        // 현재경로/upload/파일명이 저장 경로
        String savePath = realPath + "/" + newFileName;

        // 브라우저에서 이미지 불러올 때 절대 경로로 불러오면 보안의 위험 있어 상대경로를 쓰거나 이미지 불러오는 jsp 또는 클래스 파일을 만들어 가져오는 식으로 우회해야 함
        // 때문에 savePath와 별개로 상대 경로인 uploadPath 만들어줌
        String uploadPath = "./" + newFileName;

        // 저장 경로로 파일 객체 생성
        File file = new File(savePath);

        // 파일 업로드
        uploadFile.transferTo(file);

        // uploaded, url 값을 modelandview를 통해 보냄
        mav.addObject("uploaded", true); // 업로드 완료
        mav.addObject("url", uploadPath); // 업로드 파일의 경로

        return mav;
    }

}

