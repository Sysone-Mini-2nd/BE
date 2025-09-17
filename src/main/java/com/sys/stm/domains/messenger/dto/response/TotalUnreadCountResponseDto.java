package com.sys.stm.domains.messenger.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
/** 작성자: 조윤상 */
@AllArgsConstructor
@Getter
public class TotalUnreadCountResponseDto {
    @JsonProperty("totalUnreadCount")
    private long totalUnreadCount;
    @JsonProperty("recentMessage")
    private String recentMessage;
    @JsonProperty("memberId")
    private long memberId;
    @JsonProperty("chatRoomId")
    private long chatRoomId;
}


