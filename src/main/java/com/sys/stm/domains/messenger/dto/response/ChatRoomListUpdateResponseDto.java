package com.sys.stm.domains.messenger.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/** 작성자: 조윤상 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomListUpdateResponseDto {

    @JsonProperty("chatRoomId")
    private long chatRoomId;
    @JsonProperty("totalUnreadCount")
    private long totalUnreadCount;
    @JsonProperty("recentMessage")
    private String recentMessage;
    @JsonProperty("memberId")
    private long memberId;

}