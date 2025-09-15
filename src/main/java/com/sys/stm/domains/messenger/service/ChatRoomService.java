package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatRoomRepository;
import com.sys.stm.domains.messenger.dao.MemberProfileRepository;
import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.request.ChatRoomCreateRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomDataResponseDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomInfoResponseDto;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantService chatRoomParticipantService;
    private final MessageStatusService messageStatusService;
    private final MemberProfileRepository memberProfileRepository;
    private final SimpMessagingTemplate messagingTemplate; // SimpMessagingTemplate 주입

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
                            .messageCreatedAt(roomInfo.getMessageCreatedAt())
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

    public void inviteMembers(List<Long> memberIdList, long chatRoomId, long memberId) {

        // 채팅방에 초대된 사원 리스트 저장
        // 사원 id로 ChatRoomParticipant 만들기, memberId,chatRoomId, lastReadAt만 하면 됨
        LocalDateTime currentTime = LocalDateTime.now();
        List<ChatRoomParticipant> chatRoomParticipants = memberIdList.stream().map((id) -> {
            return ChatRoomParticipant.builder()
                    .memberId(id)
                    .chatRoomId(chatRoomId)
                    .lastReadAt(Timestamp.valueOf(currentTime))
                    .build();
        }).toList();

        chatRoomParticipantService.createChatRoomParticipants(chatRoomParticipants);

        // 채팅방에 초대된 사원들... 입장하셨습니다. 메시지 생성
        List<String> nameList = chatRoomParticipantService.findNamesByChatRoomId(chatRoomId);
        messageStatusService.createInitialInvitationMessage(nameList, chatRoomId, memberId);
    }

    public long getTotalUnreadCount(long memberId) {
        // 기존의 안 읽은 메시지 수를 포함한 채팅방 정보 조회 메소드를 재사용
        List<ChatRoomInfoResponseDto> roomInfos = chatRoomRepository.findChatRoomsByMemberId(memberId);

        if (roomInfos.isEmpty()) {
            return 0;
        }

        // 각 채팅방의 안 읽은 메시지 수를 모두 합산
        return roomInfos.stream()
                .mapToLong(ChatRoomInfoResponseDto::getUnreadMessageCount)
                .sum();
    }

    public int updateRecentMessage(long chatRoomId, String message){
        return chatRoomRepository.updateRecentMessage(chatRoomId, message);
    }
}
