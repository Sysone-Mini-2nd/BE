package com.sys.stm.domains.board.service;

import com.sys.stm.domains.board.dao.BoardRepository;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;


    @Override
    public int testCount() {

        Integer num =  boardRepository.getDeptCount()
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));

        return num;
    }
}
