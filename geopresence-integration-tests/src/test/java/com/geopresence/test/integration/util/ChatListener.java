package com.geopresence.test.integration.util;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;

public class ChatListener implements ChatManagerListener {

  private ChatMessageListener messageListener = new ChatMessageListener();

  @Override
  public void chatCreated(Chat chat, boolean createdLocally) {

    if (!createdLocally) {

      chat.addMessageListener(messageListener);

    }

  }

  public ChatMessageListener getMessageListener() {
    return this.messageListener;
  }

}
