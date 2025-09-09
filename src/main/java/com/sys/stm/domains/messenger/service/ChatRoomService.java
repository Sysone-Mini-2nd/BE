package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatRoomRepository;
import com.sys.stm.domains.messenger.dao.ChatRoomParticipantRepository;
import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.dto.request.ChatRoomCreateRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomUpdateRequestDto;
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

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantService chatRoomParticipantService;

    public List<ChatRoomDataResponseDto> findAllChatRoomsDataById(long id) {
        // 사용자가 속한 채팅방의 기본 정보와 안 읽은 메시지 수를 가져온다.
        List<ChatRoomInfoResponseDto> roomInfos = chatRoomRepository.findChatRoomsByMemberId(id);

        if (roomInfos.isEmpty()) {
            // 참여중인 채팅방이 없으면 빈 리스트 반환
            return List.of();
        }

        // 채팅방 ID 목록을 추출한다.
        List<Long> roomIds = roomInfos.stream()
                .map(ChatRoomInfoResponseDto::getId)
                .toList();

        Map<Long, List<String>> participantsMap =
                chatRoomParticipantService.findParticipantNamesMapByRoomIds(roomIds);

        return roomInfos.stream()
                .map(roomInfo -> {
                    return ChatRoomDataResponseDto.builder()
                            .name(roomInfo.getName())
                            .id(roomInfo.getId())
                            .recentMessage(roomInfo.getRecentMessage())
                            .unreadMessageCount(roomInfo.getUnreadMessageCount())
                            .memberNameList(participantsMap.get(roomInfo.getId()))
                            .build();

                })
                .collect(Collectors.toList());
    }

    public int createChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.createChatRoom(chatRoom);
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
            int count = chatRoomParticipantService.findMemberByMemberId(memberId);

            if (count != 1)
                throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

    }

    public int updateChatRoom(long id, ChatRoomUpdateRequestDto dto) {
        // 채팅방 이름 혹은 최근 메시지 업데이트
        return chatRoomRepository.updateChatRoom(id, dto);
    }
}
