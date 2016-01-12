package com.geopresence.xmpp.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class GeoLoc extends IQ implements PacketExtension, PacketExtensionProvider {

  public static enum Type {CURRENT, NEXT, PREV}

  ;

  private double lat;
  private double lon;
  private double accuracy;

  private double maxProximity;

  private String text;
  private String area;
  private String locality;
  private String region;
  private String country;
  private String postalcode;
  private String uri;
  private Type locType;

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public double getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
  }

  public double getMaxProximity() {
    return maxProximity;
  }

  public void setMaxProximity(double maxProximity) {
    this.maxProximity = maxProximity;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void fromXML() {

  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public String getPostalcode() {
    return postalcode;
  }

  public void setPostalcode(String postalcode) {
    this.postalcode = postalcode;
  }

  public Type getLocType() {
    return locType;
  }

  public void setLocType(Type type) {
    this.locType = type;
  }

  public String getElementName() {
    return "geoloc";
  }

  public String getNamespace() {
    return "http://jabber.org/protocol/geoloc";
  }

  @Override
  public String getChildElementXML() {
    StringBuffer sb = new StringBuffer();
    sb.append("<geoloc xmlns='http://jabber.org/protocol/geoloc' xml:lang='en'>");
    sb.append("	<lat>" + getLat() + "</lat>");
    sb.append("	<lon>" + getLon() + "</lon>");
    sb.append("	<maxProximity>" + getMaxProximity() + "</maxProximity>");
    sb.append("</geoloc>");
    return sb.toString();
  }

  public PacketExtension parseExtension(XmlPullParser parser)
      throws Exception {
    GeoLoc loc = new GeoLoc();

    for (; ; ) {
      switch (parser.next()) {
        case XmlPullParser.START_TAG:
          String name = parser.getName();
          if (name.equals("text")) {
            loc.text = parser.nextText();
            break;
          }
          if (name.equals("area")) {
            loc.area = parser.nextText();
            break;
          }
          if (name.equals("locality")) {
            loc.locality = parser.nextText();
            break;
          }
          if (name.equals("region")) {
            loc.region = parser.nextText();
            break;
          }
          if (name.equals("country")) {
            loc.country = parser.nextText();
            break;
          }
          if (name.equals("postalcode")) {
            loc.postalcode = parser.nextText();
            break;
          }
          if (name.equals("uri")) {
            loc.uri = parser.nextText();
            break;
          }
          if (name.equals("lat")) {
            loc.lat = Double.parseDouble(parser.nextText().trim());
            break;
          }
          if (name.equals("lon")) {
            loc.lon = Double.parseDouble(parser.nextText().trim());
            break;
          }
          if (name.equals("accuracy")) {
            loc.accuracy = Double.parseDouble(parser.nextText().trim());
            break;
          }
          // OK, we didn't get it :-(
          //Log.e("SMACK", "Unknown geoloc-type " + name);
          int stack = 1;
          do {
            switch (parser.next()) {
              case XmlPullParser.END_TAG:
                stack--;
                break;
              case XmlPullParser.START_TAG:
                stack++;
                break;
            }
          } while (stack > 0);
          break;
        case XmlPullParser.TEXT:
          break;
        case XmlPullParser.END_TAG:
          if (parser.getName().equals("geoloc")) {
            //Log.d("GEO", "parsed geoloc");
            return loc;
          }
      }
    }

  }

}
