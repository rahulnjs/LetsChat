package com.chat.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserStatus {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm a");
	private static SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM");
	
	private Date lastSeen;
	private boolean typing;
	private int msgsRead = -1;

	public UserStatus() {
		updateStatus(false, null);
	}

	public void updateStatus(boolean typing, String time) {
		this.typing = typing;
		setLastSeen(time);
	}

	private void setLastSeen(String time) {
		try {
			this.lastSeen = sdf.parse(time);
		} catch (ParseException localParseException) {
		} catch (Exception localException) {
		}
	}

	public void setMsgsRead(int msgsRead) {
		this.msgsRead = msgsRead;
	}

	public int getMsgsRead() {
		return this.msgsRead;
	}

	public String getUserStatus(String cT) {
		try {
			if (this.typing) {
				return "Typing...";
			}
			long elapsedTime = sdf.parse(cT).getTime() - this.lastSeen.getTime();
			elapsedTime /= 1000L;
			if (elapsedTime > 30L) {
				return "Last seen " + getLastSeenAt(cT) + " at " + sdf3.format(this.lastSeen).toLowerCase();
			}
			return "Online";
		} catch (Exception exp) {
		}
		return "Last seen just now";
	}

	private String getLastSeenAt(String cT) throws ParseException {
		Date cur = sdf2.parse(cT.split(" ")[0]);
		Date lst = sdf2.parse(sdf.format(this.lastSeen).split(" ")[0]);
		return cur.equals(lst) ? "today" : sdf4.format(this.lastSeen);
	}
}
