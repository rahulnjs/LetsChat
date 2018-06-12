package com.chat.component;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatRoom {
	private int id;
	private String name;
	private String creator;
	private Map<String, UserStatus> users;
	private List<Message> msgs;

	public Map<String, UserStatus> getUsers() {
		return this.users;
	}

	public List<Message> getMsgs() {
		return this.msgs;
	}

	public ChatRoom(String name, String creator, int id) {
		this.name = name;
		this.creator = creator;
		this.id = id;
		this.users = new LinkedHashMap<>();
		addUser(creator);
		this.msgs = new LinkedList<>();
	}

	public boolean addUser(String name) {
		if (this.users.get(name) == null) {
			this.users.put(name, new UserStatus());
			return true;
		}
		return false;
	}

	public void setUserStatus(String userName, boolean typing, String time) {
		((UserStatus) this.users.get(userName)).updateStatus(typing, time);
	}

	public boolean addMessage(String by, String msg, String time) throws ParseException {
		return this.msgs.add(new Message(msg, by, time));
	}

	public String getCreator() {
		return this.creator;
	}

	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.id;
	}

	public String getMsgsAsJSON(String user) {
		UserStatus status = (UserStatus) this.users.get(user);
		int startAt = status.getMsgsRead();
		int till = this.msgs.size() - 1;
		if (till > startAt) {
			status.setMsgsRead(till);
		}
		String jsonMsg = "[";
		while (startAt < till) {
			Message m = (Message) this.msgs.get(startAt + 1);
			if (!m.getMsgBy().equals(user)) {
				if (jsonMsg.length() != 1) {
					jsonMsg = jsonMsg + ",";
				}
				jsonMsg = jsonMsg + m.asJSONObject();
			}
			startAt++;
		}
		// System.out.println(jsonMsg + "]");
		return jsonMsg + "]";
	}

	public String getUserStatusAsJSON(String user, String cTime) {
		Set<String> keySet = this.users.keySet();
		String freshObj = "[";
		for (String key : keySet) {
			if (!key.equals(user)) {
				freshObj = freshObj + (freshObj.length() != 1 ? "," : "");
				freshObj = freshObj + "{\"name\":\"" + key + "\"" + ", \"status\":\""
						+ ((UserStatus) this.users.get(key)).getUserStatus(cTime) + "\"}";
			}
		}
		return freshObj + "]";
	}
}
