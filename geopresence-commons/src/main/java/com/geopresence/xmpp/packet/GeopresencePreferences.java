package com.geopresence.xmpp.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class GeopresencePreferences extends IQ implements PacketExtension, PacketExtensionProvider {

  private Double proximity;

  public Double getProximity() {
    return proximity;
  }

  public void setProximity(Double proximity) {
    this.proximity = proximity;
  }

  @Override
  public String getChildElementXML() {
    StringBuffer sb = new StringBuffer();
    sb.append("<geopresence xmlns='http://geopresence.com/protocol/geopresence' xml:lang='en'>");
    sb.append("	<proximity>" + getProximity() + "</proximity>");
    sb.append("</geopresence>");
    return sb.toString();
  }

  public String getElementName() {
    return "geopresence";
  }

  public String getNamespace() {
    return "http://geopresence.com/protocol/geopresence";
  }

  public PacketExtension parseExtension(XmlPullParser parser) throws Exception {

    GeopresencePreferences prox = new GeopresencePreferences();

    for (; ; ) {
      switch (parser.next()) {
        case XmlPullParser.START_TAG:
          String name = parser.getName();
          if (name.equals("proximity")) {
            prox.proximity = Double.valueOf(parser.nextText());
            break;
          }
      }
    }
  }

}
