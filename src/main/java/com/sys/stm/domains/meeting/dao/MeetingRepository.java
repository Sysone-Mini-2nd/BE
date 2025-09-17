package com.sys.stm.domains.meeting.dao;

import com.sys.stm.domains.meeting.domain.Meeting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
/** 작성자: 배지원 */
@Mapper
public interface MeetingRepository {
    // 회의록 Entity 조회
    Optional<Meeting> getMeetingById(Long meetingId);


    // 회의 생성
    int createMeeting(Meeting meeting);

    // 회의 세부 페이지 조회
    Optional<Meeting> findByIdWithParticipants(Long id);

    // 프로젝트별 회의 목록 조회
    List<Meeting> findByProjectId(Map<String, Object> params);

    // 프로젝트별 회의 목록 조회 (Oracle ROWNUM 페이지네이션 + 검색)
    List<Meeting> findByProjectIdWithPagination(Map<String, Object> params);

    // 전체 개수 조회
    int countByProjectId(Long projectId);


    // 검색 조건을 포함한 전체 개수 조회
    int countByProjectIdWithSearch(Map<String, Object> params);

    void deleteMeeting(Meeting meeting);

//    // 회의 조회 (ID로)
//    Optional<Meeting> findById(Long id);
//


//    // 회의 수정
//    int updateMeeting(Meeting meeting);
//
//    // 회의 삭제 (논리 삭제)
//    int deleteMeeting(Long id);
//
//    // 회의 개수 조회
//    int countByProjectId(Long projectId);
}
