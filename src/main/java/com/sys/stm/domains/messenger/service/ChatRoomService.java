package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatRoomMapper;
import com.sys.stm.domains.messenger.dao.ChatRoomParticipantMapper;
import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.dto.request.ChatRoomCreateRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomDataResponseDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomInfoResponseDto;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomMapper chatRoomMapper;
    private final ChatRoomParticipantMapper chatRoomParticipantMapper;

    public List<ChatRoomDataResponseDto> findAllChatRoomsDataById(long id) {
        // 사용자가 속한 채팅방의 기본 정보와 안 읽은 메시지 수를 가져온다.
        List<ChatRoomInfoResponseDto> roomInfos = chatRoomMapper.findChatRoomsByMemberId(id);

        if (roomInfos.isEmpty()) {
            // 참여중인 채팅방이 없으면 빈 리스트 반환
            return List.of();
        }

        // 채팅방 ID 목록을 추출한다.
        List<Long> roomIds = roomInfos.stream()
                .map(ChatRoomInfoResponseDto::getId)
                .toList();

        // 모든 채팅방의 참여자 정보를 한 번에 가져온다.
        List<ParticipantInfoResponseDto> participants = chatRoomMapper.findParticipantsByRoomIds(roomIds);

        // 4. 참여자 정보를 채팅방 ID별로 그룹핑한다. (Map<채팅방ID, List<참여자이름>>)
        Map<Long, List<String>> participantsMap = participants.stream()
                .collect(Collectors.groupingBy(
                        ParticipantInfoResponseDto::getChatRoomId,
                        Collectors.mapping(ParticipantInfoResponseDto::getName, Collectors.toList())
                ));

        return roomInfos.stream()
                .map(roomInfo -> {
                    return ChatRoomDataResponseDto.builder()
                            .name(roomInfo.getName())
                            .id(roomInfo.getId())
                            .recentMessage(roomInfo.getRecentMessage())
                            .unreadMessageCount(roomInfo.getUnreadMessageCount())
                            .memberIdList(participantsMap.get(roomInfo.getId()))
                            .build();

                })
                .collect(Collectors.toList());
    }

    public int createChatRoom(ChatRoom chatRoom) {
        return chatRoomMapper.createChatRoom(chatRoom);
    }


    public void validateChatRoomData(ChatRoomCreateRequestDto chatRoomCreateRequestDto) {

        String chatRoomType = chatRoomCreateRequestDto.getType();
        List<Long> chatRoomMemberIdList = chatRoomCreateRequestDto.getMemberIdList();

        // 1:1 채팅방 인원수가 2명인지 확인
        if (chatRoomType.equalsIgnoreCase("One_on_One") &&
                (chatRoomMemberIdList.size() > 2 || chatRoomMemberIdList.size() == 0)) {
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        // TODO member테이블을 다루는 xml파일 및 member 테이블에서 id로 멤버 검색하는 sql문 만들어지면 교체
        for (Long memberId : chatRoomMemberIdList) {
            int count = chatRoomParticipantMapper.findMemberByMemberId(memberId);

            if (count != 1)
                throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

    }
}
