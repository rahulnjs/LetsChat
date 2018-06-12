package com.chat.tag;

import com.chat.component.UserStatus;
import com.chat.component.Worker;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class UserViewer
  extends SimpleTagSupport
{
  public void doTag()
    throws JspException, IOException
  {
    String user = (String)getJspContext().findAttribute("user");
    String chatID = (String)getJspContext().findAttribute("cid");
    
    Worker worker = new Worker();
    Map<String, UserStatus> users = worker.getChatRoomUsers(Integer.parseInt(chatID));
    String creator = worker.getCreator(Integer.parseInt(chatID));
    int nUsers = users.keySet().size() - 1;
    Iterator<String> itr = users.keySet().iterator();
    for (int i = 0; i <= nUsers; i++)
    {
      String key;
      if (i == nUsers)
      {
        key = user;
      }
      else
      {
        key = (String)itr.next();
        if (key.equals(user)) {
          key = (String)itr.next();
        }
      }
      String status = ((UserStatus)users.get(key)).getUserStatus("");
      getJspContext().setAttribute("userName", key);
      getJspContext().setAttribute("userStatus", status);
      getJspContext().setAttribute("creator", Boolean.valueOf(key.equals(creator)));
      getJspContext().setAttribute("me", Boolean.valueOf(user.equals(key)));
      getJspBody().invoke(null);
    }
  }
}
