package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.ChatModels.ListChatRoomModel;
import com.ntv.ntvcons_backend.entities.ChatModels.MessageModel;
import com.ntv.ntvcons_backend.services.Chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/createChatRoom")
    public ResponseEntity<?> saveChatRoom(@RequestParam String chatRoomName ){
        String s = chatService.saveUserChatRoom(chatRoomName);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/sendMessage")
    public HttpStatus sendMessage(@RequestParam String chatroomName,
                                  @RequestParam Long userid,
                                  @RequestParam String message){
        boolean b = chatService.sendMessage(chatroomName, userid, message);
        if(b){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getAllMessage")
    public List<MessageModel> getChatRoomMessageByChatRoom(@RequestParam String chatroomName,
                                                           @RequestParam int pageNo,
                                                           @RequestParam int pageSize){
        return chatService.getMessage(chatroomName, pageNo, pageSize);
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/changeStatusMessage")
    public HttpStatus changeStatusMessage(@RequestParam Long messageidStart, @RequestParam Long messageidEnd, @RequestParam String chatRoomName, @RequestParam String userId){
        boolean b = chatService.changeMessageStatus(messageidStart, messageidEnd, chatRoomName, userId);
        if(b){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getNewMessage")
    public List<MessageModel> getNewMessage(@RequestParam String chatroomName,
                                            @RequestParam String userid,
                                            @RequestParam int pageNo,
                                            @RequestParam int pageSize){
        return chatService.getNewMessage(chatroomName, pageNo, pageSize, userid);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getListChatRoom")
    public List<ListChatRoomModel> getListChatRoom(@RequestParam Long userId,
                                                   @RequestParam int pageNo,
                                                   @RequestParam int pageSize){
        return chatService.getListChatRoom(userId, pageNo, pageSize);
    }
}
