package com.geopresence.openfire.test.mock;

import java.util.Date;

import org.jivesoftware.openfire.user.User;

public class MockUser extends User {

  private String username;

  public MockUser(String username, String name, String email, Date creationDate, Date modificationDate) {

    this.username = username;

  }

  @Override
  public String getUsername() {

    return this.username;

  }

}
