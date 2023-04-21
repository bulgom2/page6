package com.page6.controller;

import com.page6.dto.BoardDto;
import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.by;

//@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HeartService heartService;
    @Autowired
    private TagService tagService;

    @Autowired
    private BoardFileService boardFileService;

    // 마이페이지
    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }



    // 내 글 보기
    @GetMapping({"/mypage/myboard", "/mypage/myboard/{page}"})
    public String myBoardList(Model model, Principal principal, @RequestParam(defaultValue = "1") int page) {

        // 한 페이지당 보여줄 게시물 수
        int size = 10;
        page = page < 1 ? 1 : page;

        // 페이지 번호는 0부터 시작하므로 1을 빼준다
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        // 리스트 불러오기
        String email = principal.getName();
        Page<BoardDto> myBoardPage = boardService.getMyBoard(email, pageable);

        // 페이징 번호
        int nowPage = myBoardPage.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage;
        int lastPage = myBoardPage.getTotalPages();

        int maxPage = 10; // 최대 페이지 수
        if (myBoardPage.getTotalPages() < maxPage) {
            endPage = myBoardPage.getTotalPages();
        } else {
            endPage = Math.min(nowPage + 5, myBoardPage.getTotalPages());
            if (nowPage < 5)
                endPage = maxPage;
            else if (nowPage < maxPage - 5)
                endPage = maxPage;
        }

        if (nowPage == 0) startPage = 1;


        model.addAttribute("myBoardList", myBoardPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "board/myboard";
    }

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
        String[] tagArr = tags.split("#");
        tagArr = Arrays.stream(tagArr)
                .filter(s -> !s.isEmpty())      //빈칸 없애줌
                .collect(Collectors.toCollection(LinkedHashSet::new))    //중복 없애줌
                .toArray(String[]::new);

        for(int i = 0; i < tagArr.length; i++)
            tagService.addTag(board.getId(), tagArr[i]);

        //파일 업로드
        for(MultipartFile file : files) {
            boardFileService.saveFile(file, board.getId());
        }

        return "redirect:/board/" + board.getId();
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
                    by("member.name").descending() : by("member.name").ascending();
        } else { // 기본적으로 sortType으로 정렬하는 경우
            sort = sortOrder.equalsIgnoreCase("desc") ? by(sortType).descending() : by(sortType);
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
    @PostMapping(value = "/uploadImage")
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

