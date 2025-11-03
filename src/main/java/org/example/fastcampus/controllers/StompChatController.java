package org.example.fastcampus.controllers;

import com.nimbusds.oauth2.sdk.AbstractAuthenticatedRequest;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fastcampus.dtos.ChatMessage;
import org.example.fastcampus.entities.Message;
import org.example.fastcampus.services.ChatService;
import org.example.fastcampus.vos.CustomOAuth2User;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    //@MessageMapping 추가 설명
    // WebSocket 세션은 “HTTP 요청”이 아니기 때문에 Spring의 OSIV가 적용되지 않아요.
    @MessageMapping("/chats/{chatroomId}") // 클라이언트 -> 서버 : /pub/chats/...
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handleMessage(
            Principal principal,
            @DestinationVariable Long chatroomId,
            @Payload Map<String, String> payload
    ) {
        log.info("{} sent {} in {}", principal.getName(), payload, chatroomId);
        CustomOAuth2User user = (CustomOAuth2User) ((AbstractAuthenticationToken) principal).getPrincipal();
        Message message = chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));
        messagingTemplate.convertAndSend("/sub/chats/updates", chatService.getChatroom(chatroomId));
        return new ChatMessage(principal.getName(), payload.get("message"));
    }

}
