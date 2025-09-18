package com.sys.stm.domains.meeting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/** 작성자: 배지원 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingListPageResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
