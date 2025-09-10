package com.sys.stm.domains.dashBoard.service;

import com.sys.stm.domains.dashBoard.dao.DashBoardRepository;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DashBoardServiceImpl implements DashBoardService {

    private final DashBoardRepository dashBoardRepository;


    @Override
    public int testCount() {

        Integer num =  dashBoardRepository.getDeptCount()
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.MEETING_NOT_FOUND));

        return num;
    }
}
