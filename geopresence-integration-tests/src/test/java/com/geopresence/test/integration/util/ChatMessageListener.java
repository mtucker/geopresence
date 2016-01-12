package com.geopresence.test.integration.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessageListener implements MessageListener {

  private static final Logger log = LoggerFactory.getLogger(ChatMessageListener.class);

  private HashMap<String, List<Message>> chats = new HashMap<String, List<Message>>();

  @Override
  public void processMessage(Chat chat, Message message) {

    String chatParticipant = chat.getParticipant();

    if (chats.get(chatParticipant) == null) {

      chats.put(chatParticipant, new ArrayList<Message>());

    }

    log.info("Received Chat Message from " + chatParticipant + ": " + message.getBody());

    chats.get(chatParticipant).add(message);

  }

  public List<Message> getChats(String participant) {

    return chats.get(participant);

  }

}
