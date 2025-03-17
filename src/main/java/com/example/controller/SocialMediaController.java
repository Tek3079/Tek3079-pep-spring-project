package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.UserNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping("/")
public class SocialMediaController {
AccountService accountService;
MessageService messageService;
public SocialMediaController(AccountService accountService, MessageService messageService){
    this.accountService = accountService;
    this.messageService = messageService;
}


@PostMapping("register")
public ResponseEntity<?> register(@RequestBody Account account) {
    return accountService.register(account)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
}
@PostMapping("login")
public ResponseEntity<?> login(@RequestBody Account account) {
    return accountService.login(account.getUsername(), account.getPassword())
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
}
@PostMapping("messages")
public ResponseEntity<?> createMessage(@RequestBody Message message){
    try {
        return messageService.createMessage(message)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    } catch (UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
@GetMapping("messages")
public ResponseEntity<List<Message>> getAllMessage(){
    return ResponseEntity.ok(messageService.getAllMessage());
}
@GetMapping("messages/{messageId}")

public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.ok().build()); 
    }
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        return deleted ? ResponseEntity.ok(1) : ResponseEntity.ok().build();
    }
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> requestBody) {
        String newMessageText = requestBody.get("messageText"); // Extract messageText from request

        Optional<Integer> updatedRows = messageService.updateMessageText(messageId, newMessageText);
        return updatedRows.map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.badRequest().build()); 
    }
    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUser(accountId);
        return ResponseEntity.ok(messages);

}
}