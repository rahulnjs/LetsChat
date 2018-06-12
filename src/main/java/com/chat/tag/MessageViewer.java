package com.chat.tag;

import com.chat.component.Message;
import com.chat.component.UserStatus;
import com.chat.component.Worker;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class MessageViewer extends SimpleTagSupport {
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	public void doTag() throws JspException, IOException {
		String user = (String) getJspContext().findAttribute("user");
		String chatID = (String) getJspContext().findAttribute("cid");
		Worker worker = new Worker();
		List<Message> msgs = worker.getAllMessages(Integer.parseInt(chatID));
		int msgCount = msgs.size();
		Date lastDate = null;
		for (int i = 0; i < msgCount; i++) {
			Message msg = (Message) msgs.get(i);
			Boolean dateBarNeeded = Boolean.valueOf(false);
			if (i == 0) {
				dateBarNeeded = Boolean.valueOf(true);
			} else {
				try {
					if (dateChanged(lastDate, msg.getTime())) {
						dateBarNeeded = Boolean.valueOf(true);
					}
				} catch (ParseException localParseException) {
				}
			}
			// System.out.println("dateBar " + dateBarNeeded);
			getJspContext().setAttribute("dbNeeded", dateBarNeeded);
			if (dateBarNeeded.booleanValue()) {
				getJspContext().setAttribute("date", msg.getFormattedDate());
				lastDate = msg.getTime();
			}
			boolean outgoing = msg.getMsgBy().equals(user);
			getJspContext().setAttribute("align", outgoing ? "outgoing" : "incoming");
			getJspContext().setAttribute("color", outgoing ? "out" : "in");
			getJspContext().setAttribute("show", Boolean.valueOf(!outgoing));
			getJspContext().setAttribute("msg", msg.getMsg());
			getJspContext().setAttribute("msgBy", msg.getMsgBy());
			getJspContext().setAttribute("at", msg.getMsgTime());
			if (i == msgCount - 1) {
				Map<String, UserStatus> users = worker.getChatRoomUsers(Integer.parseInt(chatID));
				UserStatus status = (UserStatus) users.get(user);
				status.setMsgsRead(i);
			}
			getJspBody().invoke(null);
		}
	}

	private boolean dateChanged(Date last, Date curr) throws ParseException {
		return !sdf3.parse(last.toString()).equals(sdf3.parse(curr.toString()));
	}
}
