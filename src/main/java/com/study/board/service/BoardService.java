package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{

        // 프로젝트 경로 + 파일 저장 경로를 담아주게 됨
       String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

       //식별자 생성
        UUID uuid = UUID.randomUUID();


        // 원래 파일이름에 랜덤 식별자를 붙여서 저장
        String fileName = uuid + "_" + file.getOriginalFilename();

       // 경로와 이름을 파라미터로 넣는다
       File saveFile = new File(projectPath, fileName);

       file.transferTo(saveFile);

       // 실제 db 필드에 저장하기 위해서 set 하는 과정 필요함
        board.setFilename(fileName);
        // 서버에서 접근할 때는 static 아래의 경로로만 접근 가능하다
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    // 그냥 findAll 은 List를 반환하지만
    // pageable이 들어가면 Page를 반환한다
    public Page<Board> boardList(Pageable pageable) {

        return boardRepository.findAll(pageable);
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }
}
