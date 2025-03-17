package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.UserNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    public MessageService (MessageRepository messageRepository , AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    public Optional<Message> createMessage(Message message){
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            return Optional.empty();
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new UserNotFoundException("User with ID " + message.getPostedBy() + " does not exist.");
        }
        return Optional.of(messageRepository.save(message));
    }
    public List<Message> getAllMessage(){
        return messageRepository.findAll();
        
    }
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findBypostedBy(messageId);
    }
    public boolean deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true; 
        }
        return false; 
    }
    public Optional<Integer> updateMessageText(Integer messageId, String newMessageText) {
        // Validation: Check if message exists
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty() || newMessageText.isBlank() || newMessageText.length() > 255) {
            return Optional.empty(); 
        }

        // Update the message
        Message message = messageOptional.get();
        message.setMessageText(newMessageText);
        messageRepository.save(message);
        
        return Optional.of(1); 
    }
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId); 
    }
}
