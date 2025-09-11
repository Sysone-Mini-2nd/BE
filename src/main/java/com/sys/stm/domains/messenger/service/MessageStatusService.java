package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatMessageRepository;
import com.sys.stm.domains.messenger.dao.ChatRoomParticipantRepository;
import com.sys.stm.domains.messenger.dao.MessageStatusRepository;
import com.sys.stm.domains.messenger.domain.MessageStatus;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageStatusService {

    private final ChatMessageRepository chatMessageRepository;
    private final SqlSessionFactory sqlSessionFactory;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;


    // 채팅방의 모든 메시지를 읽음으로 표시 (HTTP 진입 시)
    public int markAllMessagesInChatRoomAsRead(long chatRoomId, long readerId, long lastReadMessageId) {

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            LocalDateTime currentTime = LocalDateTime.now();

            try {

                // 채팅방 참여자의 마지막 읽은 시간 업데이트
                chatRoomParticipantRepository.updateLastReadAt(chatRoomId, readerId, Timestamp.valueOf(currentTime));

                // 마지막으로 읽은 메시지 id를 기반으로 해당 채팅방에 아직 안 읽은 메시지 id 리스트 가져오기
                List<Long> messageIdList = chatMessageRepository.findAllMessageIdsByChatRoomIdAndMessageId(chatRoomId, lastReadMessageId);


                List<MessageStatus> messageStatusList = messageIdList.stream().map(
                        (messageId) -> {
                            return MessageStatus.builder()
                                    .messageId(messageId)
                                    .readAt(currentTime)
                                    .readerId(readerId)
                                    .build();

                        }
                ).toList();

                int returnValue = 0;

                MessageStatusRepository messageStatusRepository = sqlSession.getMapper(MessageStatusRepository.class);

                for (MessageStatus item : messageStatusList) {
                    returnValue += messageStatusRepository.createMessageStatus(item);
                }

                sqlSession.flushStatements();
                sqlSession.commit();


                return returnValue;
            } catch (Exception e) {
                sqlSession.rollback();
                throw new RuntimeException("다 건의 메시지 읽음 처리 중 오류 발생");
            }
        }
    }

    // 채팅방에서 하나의 메시지를 읽음으로 표시 (Websocket 진입 시)
    public void markMessageAsRead(long memberId, long chatRoomId, long messageId) {

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            MessageStatusRepository messageStatusRepository = sqlSession.getMapper(MessageStatusRepository.class);

            LocalDateTime currentTime = LocalDateTime.now();
            // 채팅방 참여자의 마지막 읽은 시간 업데이트
            chatRoomParticipantRepository.updateLastReadAt(chatRoomId, memberId, Timestamp.valueOf(currentTime));

            messageStatusRepository.createMessageStatus(MessageStatus.builder()
                    .messageId(messageId)
                    .readAt(currentTime)
                    .readerId(memberId)
                    .build()
            );
        }
    }
}
