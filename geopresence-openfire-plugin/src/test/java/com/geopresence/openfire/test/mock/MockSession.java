package com.geopresence.openfire.test.mock;

import java.net.UnknownHostException;
import java.util.Date;

import org.jivesoftware.openfire.StreamID;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

public class MockSession implements Session {

  private JID jid;

  public MockSession(String jid) {
    this.jid = new JID(jid);
  }

  @Override
  public JID getAddress() {
    return jid;
  }

  @Override
  public int getStatus() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public StreamID getStreamID() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getServerName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Date getCreationDate() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Date getLastActiveDate() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getNumClientPackets() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long getNumServerPackets() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isClosed() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isSecure() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getHostAddress() throws UnknownHostException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getHostName() throws UnknownHostException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void process(Packet packet) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deliverRawText(String text) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean validate() {
    // TODO Auto-generated method stub
    return false;
  }


}