package com.chat.component;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.chat.data.service.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Projections;

public class Worker {
	
	public static final String CHAT_USER = "chat_user";
	public static final String CHAT = "chat";
	
	public List<Message> getAllMessages(int chatRoomID) {
		return DataStorage.getChatRoom(chatRoomID).getMsgs();
	}

	public Map<String, UserStatus> getChatRoomUsers(int chatRoomID) {
		return DataStorage.getChatRoom(chatRoomID).getUsers();
	}

	public int createChatRoom(String creator, String chatRoomName) {
		return DataStorage.addChatRoom(chatRoomName, creator);
	}

	public boolean addMessage(int chatRoomID, String msg, String by, String time) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		try {
			return cRoom.addMessage(by, msg.replace("\n", ""), time);
		} catch (ParseException pe) {
		}
		return false;
	}

	public String addUser(int chatRoomID, String user) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		if (cRoom == null) {
			return "0";
		}
		return cRoom.addUser(user) ? "1" : "-1";
	}

	public String getCreator(int chatRoomID) {
		return DataStorage.getChatRoom(chatRoomID).getCreator();
	}

	public void setUserStatus(int chatRoomID, String userName, boolean typing, String time) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		cRoom.setUserStatus(userName, typing, time);
	}

	public String getMessages(int chatRoomID, String user) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		return cRoom.getMsgsAsJSON(user);
	}

	public String getChatRoomName(int chatRoomID) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		return cRoom.getName();
	}

	public String getUserStatus(int chatRoomID, String user, String cTime) {
		ChatRoom cRoom = DataStorage.getChatRoom(chatRoomID);
		return cRoom.getUserStatusAsJSON(user, cTime);
	}
	
	
	public String login(String u, String p) {
		BasicDBObject obj = new BasicDBObject();
    	obj.put("user", new BasicDBObject("$eq", u));
    	obj.put("pass", new BasicDBObject("$eq", p));
		Document doc = MongoDB._cln(CHAT_USER).find(obj).first();
		return doc == null ? "null" : doc.get("name").toString();
	}
	
	
	
	
	public boolean signup(String u, String p, String n) {
		try {
			BasicDBObject obj = new BasicDBObject();
			obj.put("user", u);
			obj.put("name", n);
			obj.put("pass", p);
			Document doc = new Document(obj);
			MongoDB._cln(CHAT_USER).insertOne(doc);
			return true;
		} catch(Exception exp) {
			System.out.println(exp);
			return false;
			
		}
	}
	
	
	public boolean createCR(String json) {
		try {
			Document doc = Document.parse(json);
			MongoDB._cln(CHAT).insertOne(doc);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean joinCR(String json, String cr) {
		try {
			BasicDBObject qry = new BasicDBObject();
			qry.put("slug", cr);
			Document doc = Document.parse(json);
			BasicDBObject obj = new BasicDBObject();
			obj.put("users", doc);
			MongoDB._cln(CHAT).updateOne(qry, new BasicDBObject("$push", obj));
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public String showCollection(String coll) {
		Iterator<Document> itr = MongoDB._cln(coll).find().iterator();
		return toJSON(itr);
		
	}
	
	public String getAllChats(String user) {
		BasicDBObject qry = new BasicDBObject();
		qry.put("creator", user);
		BasicDBObject qry2 = new BasicDBObject().append("users.user", user);
		qry2.append("creator", new BasicDBObject("$ne", user));
		Iterator<Document> itr = MongoDB._cln(CHAT).find(qry).projection(Projections.exclude("msgs")).iterator();
		Iterator<Document> itrw = MongoDB._cln(CHAT).find(qry2).projection(Projections.exclude("msgs")).iterator();
		
		return "{\"0\": " + toJSON(itr) + ", \"1\": " + toJSON(itrw) + "}";
	}
	
	public String getChatRoom(String slug) {
		BasicDBObject qry = new BasicDBObject();
		qry.put("slug", slug);
		return MongoDB._cln(CHAT).find(qry).first().toJson();
	}

	private String toJSON(Iterator<Document> itr) {
		String json = "[";
		while(itr.hasNext()) {
			if(json.length() > 1) {
				json += ",";
			}
			json += itr.next().toJson();
		}
		return json + "]";
	}
	
}

