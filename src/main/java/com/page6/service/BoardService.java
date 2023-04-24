package com.page6.service;

import com.page6.dto.BoardDto;
import com.page6.dto.BoardFormDto;
import com.page6.entity.Board;
import com.page6.entity.Member;
import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import com.page6.repository.BoardRepository;
import com.page6.repository.MemberRepository;
import com.page6.repository.TagMapRepository;
import com.page6.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;

    //글쓰기 저장 기능
    public void write(Board board, String email){
        board.setMember(memberRepository.findByEmail(email));
        boardRepository.save(board);
    }

    //리스트 조회 기능
//    public List<Board> getBoardList() {
//        return boardRepository.findAll();
//    }
//    public List<BoardDto> getBoardList() {
//        List<Board> list = boardRepository.findAll();
//        return list
//                .stream()
//                .map(BoardDto::of)
//                .collect(Collectors.toList());
//    }

    // 마이페이지 내 글 보기
    public Page<BoardDto> getMyBoard(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email);
        Page<Board> myList = boardRepository.findAllByMember(member, pageable);

        List<BoardDto> boardDtoList = myList.getContent()
                .stream()
                .map(board -> {
                    BoardDto boardDto = BoardDto.of(board);
                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return boardDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(boardDtoList, pageable, myList.getTotalElements());
    }

    public List<BoardDto> getBoardList() {
        List<Board> list = boardRepository.findAll();
        return list
                .stream()
                .map(board -> {
                    BoardDto dto = BoardDto.of(board);
                    dto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return dto;
                        })
                .collect(Collectors.toList());
    }

    //게시글 조회 시 하나 선택하는 기능
//    public Optional<Board> BoardOne(long id) {
//        return boardRepository.findById(id);
//    }

    //게시글 조회 시 하나 선택하는 기능
    public BoardDto BoardOne(long id) {
        Optional<Board> board = boardRepository.findById(id);
        return BoardDto.of(board.get());
    }

    //게시글 조회 시 하나만 선택해서 from으로 넘겨주는 기능
    public BoardFormDto BoardOneEdit(Long id) {
        Board board = boardRepository.findById(id).get();
        return BoardFormDto.of(board);
    }

    //게시물 조회수 증가
    @Transactional
    public void viewCntUpdate(Long id) {
        boardRepository.updateView(id);
    }

   // 페이징 리스트 조회 기능
   public Page<BoardDto> findAll(Pageable pageable) {
       Page<Board> boardPage = boardRepository.findAll(pageable);
       List<BoardDto> boardDtoList = boardPage.getContent()
               .stream()
               .map(board -> {
                   BoardDto boardDto = BoardDto.of(board);
                   boardDto.setComment_cnt(commentService.getCommentCount(board.getId()));
                   return boardDto;
               })
               .collect(Collectors.toList());
       return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
   }

   //키워드 검색
   @Transactional
    public Page<BoardDto> findAll(String keyword, String searchType, Pageable pageable) {
        Page<Board> boardPage = null;
        if(keyword == null || keyword.isEmpty() || "".equals(keyword))
            boardPage = boardRepository.findAll(pageable);
        else {
            switch (searchType) {
                case "title":
                    boardPage = boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
                    break;
                case "content":
                    boardPage = boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
                    break;
                case "titleContent":
                    boardPage = boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
                    break;
                case "member":
                    boardPage = boardRepository.findByMemberNameContainingIgnoreCase(keyword, pageable);
                    break;
                case "tag":
                    String[] tags = keyword.split("[#\\s]+");
                    tags = Arrays.stream(tags)
                            .filter(tag -> !tag.isEmpty())
                            .toArray(String[]::new);
                    boardPage = searchBoardByTags(tags, pageable);
                    break;
            }
        }
        List<BoardDto> boardDtoList = boardPage.getContent()
                .stream()
                .map(board -> {
                    BoardDto boardDto = BoardDto.of(board);
                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return boardDto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
    }

    // 이름 정렬 페이징
//    public Page<BoardDto> findAllByOrderByNameAsc(Pageable pageable) {
//        Page<Board> boardPage = boardRepository.findAllByOrderByNameAsc(pageable);
//        List<BoardDto> boardDtoList = boardPage.getContent()
//                .stream()
//                .map(board -> {
//                    BoardDto boardDto = BoardDto.of(board);
//                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId())); // 댓글 개수
//                    return boardDto;
//                })
//                .collect(Collectors.toList());
//        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
//    }
//    public List<Board> findAllByOrderByName() {
//        return boardRepository.findAllByOrderByName();
//    }

    //BoardDto 이름 찾기
    public Page<BoardDto> findAllByName(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAllByOrderByName(pageable);
        List<BoardDto> boardDtoList = boardPage.getContent()
                .stream()
                .map(board -> {
                    BoardDto boardDto = BoardDto.of(board);
                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return boardDto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
    }
    public Page<Board> findAllByOrderByName(Pageable pageable) {

        return boardRepository.findAllByOrderByName(pageable);
    }
    public Page<Board> findAllByOrderByTitle(Pageable pageable) {
        return boardRepository.findAllByOrderByTitle(pageable);
    }

    public Page<Board> findAllByOrderByIdDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByIdDesc(pageable);
    }

    public Page<Board> findAllByOrderByLikesDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByLikesDesc(pageable);
    }

    public Page<Board> findAllByOrderByViewsDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByViewsDesc(pageable);
    }



//    // 내 페이징
//    public Page<Board> boardList(Pageable pageable){
//        //기존 List<Board>값으로 넘어가지만 페이징 설정을 해주면 Page<Board>로 넘어갑니다.
//        return boardRepository.findAll(pageable);
//    }


    ////////////////////////    서치    ////////////////////////
    //제목 포함 검색
    @Transactional
    public Page<Board> searchByTitleContaing(String title, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    //본문 포함 검색
    @Transactional
    public Page<Board> searchByContentContaing(String content, Pageable pageable) {
        return boardRepository.findByContentContainingIgnoreCase(content, pageable);
    }

    //제목+본문 포함 검색
    @Transactional
    public Page<Board> searchByTitleContentContaing(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    //작성자 포함 검색
    @Transactional
    public Page<Board> searchByWriterContaing(String keyword, Pageable pageable) {
        return boardRepository.findByMemberNameContainingIgnoreCase(keyword, pageable);
    }


    ///////////////////////다중 해시태그 검색////////////////////////
    @Transactional
    public Page<Board> searchBoardByTags(String[] tags, Pageable pageable) {
        //tags에 담긴 keyword를 포함한 태그를 찾아 id 값을 set에 저장(중복값 제거)
        Set<Long> tagIdSet = new HashSet<>();
        for (String tag : tags) {
            List<Tag> foundTags = tagRepository.findByNameContaining(tag);
            for (Tag t : foundTags) {
                tagIdSet.add(t.getId());
            }
        }

        //set에 담긴 태그들과 이어진 board의 id값을 set에 저장(중복값 제거)
        Set<Long> boardIdSet = new HashSet<>();
        for (Long tagId : tagIdSet) {
            Tag tag = tagRepository.findById(tagId).orElse(null);
            if (tag != null) {
                List<TagMap> tagMaps = tagMapRepository.findAllByTag(tag);
                for (TagMap tagMap : tagMaps) {
                    Long boardId = tagMap.getBoard().getId();
//                삭제 기능 추가하면 넣기
//                if (!board.isDeleted()) {
                    boardIdSet.add(boardId);
//                }
                }
            }
        }

        //set에 저장된 값을 모두 Board형으로 찾아서 boardList에 넣기
        List<Long> boardIds = new ArrayList<>(boardIdSet);
        List<Board> boardList = new ArrayList<>();
        Collections.sort(boardIds);
        for(int i = 0; i < boardIdSet.size(); i++) {
            Board board = boardRepository.findById(boardIds.get(i)).get();
            boardList.add(board);
        }

        //Board 객체들을 정렬
        String sortType = pageable.getSort().toString().split(":")[0];
        String sortOrder = pageable.getSort().toString().split(":")[1];
        Comparator<Board> comparator = null;

        switch (sortType) {
            case "id":
                comparator = Comparator.comparing(Board::getId);
                break;
            case "member":
                comparator = Comparator.comparing(b -> b.getMember().getName());
                break;
            case "title":
                comparator = Comparator.comparing(Board::getTitle);
                break;
            case "likes":
                comparator = Comparator.comparing(Board::getLikes);
                break;
            case "views":
                comparator = Comparator.comparing(Board::getViews);
                break;
            default:
                comparator = Comparator.comparing(Board::getId);
                break;
        }

        if (sortOrder.replaceAll(" ", "").equalsIgnoreCase("DESC")) {
            comparator = comparator.reversed();
        }

        // 두 개 이상의 속성으로 정렬
        comparator = comparator.thenComparing(Comparator.comparing(Board::getId));
        boardList.sort(comparator);

        //Page형으로 변환
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Board> currentPageList;

        if (boardList.size() < startItem) {
            currentPageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, boardList.size());
            currentPageList = boardList.subList(startItem, toIndex);
        }

        return new PageImpl<>(currentPageList, pageable, boardList.size());
    }

    //로그인 인물이 글 작성자인지 판정 (true면 작성자, false면 작성자가 아님)
    public boolean isWriter(Long bid, String email) {
        Board board = boardRepository.findById(bid).get();
        boolean flag = board.getMember().getEmail().equals(email);
        System.out.println("작성자인지 여부 = " + flag); //콘솔 로그 확인을 위한 코드
        return flag;
    }
}