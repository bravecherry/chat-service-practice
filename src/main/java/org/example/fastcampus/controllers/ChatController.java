package org.example.fastcampus.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fastcampus.dtos.ChatMessage;
import org.example.fastcampus.dtos.ChatroomDto;
import org.example.fastcampus.entities.Chatroom;
import org.example.fastcampus.services.ChatService;
import org.example.fastcampus.vos.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatroomDto createChatroom (
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String title
    ) {
        Chatroom chatroom = chatService.createChatroom(user.getMember(), title);
        return ChatroomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long chatroomId,
            @RequestParam(required = false) Long currentChatroomId
    ) {
        return chatService.joinChatroom(user.getMember(), chatroomId, currentChatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable Long chatroomId
    ) {
        return chatService.leaveChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<ChatroomDto> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {
        return chatService.getChatroomList(user.getMember()).stream()
                .map(ChatroomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessageList(@PathVariable Long chatroomId) {
        return chatService.getMessageList(chatroomId).stream()
                .map(ChatMessage::from)
                .toList();
    }

}
