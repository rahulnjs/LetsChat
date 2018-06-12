package com.chat.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message
{
  private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
  private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private static SimpleDateFormat sdf3 = new SimpleDateFormat("MMMMMMMMMM dd, yyyy");
  private String msg;
  private String msgBy;
  private Date time;
  
  public Message(String msg, String msgBy, String time)
    throws ParseException
  {
    this.msg = msg;
    this.msgBy = msgBy;
    this.time = sdf2.parse(time);
  }
  
  public String getMsg()
  {
    return this.msg;
  }
  
  public Date getTime()
  {
    return this.time;
  }
  
  public String getMsgTime()
  {
    return sdf.format(this.time);
  }
  
  public String getMsgBy()
  {
    return this.msgBy;
  }
  
  public String asJSONObject()
  {
    return 
      "{\"by\": \"" + this.msgBy + "\"," + "\"msg\": \"" + this.msg + "\"," + "\"at\": \"" + sdf.format(this.time).toLowerCase() + "\"" + "}";
  }
  
  public String toString()
  {
    return asJSONObject();
  }
  
  public String getFormattedDate()
  {
    return sdf3.format(this.time);
  }
}
