package com.chat.controller.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chat.component.Worker;


@Controller
public class ApplicationController {
	
	Worker worker = new Worker();


	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	
	@RequestMapping("/login")
	@ResponseBody
	public String login(@RequestParam String un, @RequestParam String pass, HttpServletRequest req) {
		String name = worker.login(un, pass);
		if(true) {
			HttpSession s = req.getSession(true);
			s.setAttribute("user", un);
		}
		return name;
	}
	
	
	@RequestMapping("/signup")
	public String signUpForm( ) {
		return "sign-up";
	}
	
	@RequestMapping("/user/{user}")
	public String dashboard(@PathVariable String user, HttpServletRequest req) {
		HttpSession s = req.getSession(false);
		if(s == null || !s.getAttribute("user").equals(user)) {
			return "redirect:/";
		} else {
			return "dashboard";
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/user/{user}/create-cr")
	public String createCR(@PathVariable String user, HttpServletRequest req, @RequestParam String cr) {
		HttpSession s = req.getSession(false);
		if(s == null || !s.getAttribute("user").equals(user)) {
			return "un_auth";
		} else {
			return worker.createCR(cr) ? "c" : "e";
		}
		
	}

	@ResponseBody
	@RequestMapping("/user/{user}/join-cr")
	public String createCR(@RequestParam String cr, @RequestParam String u) {
		return "" + worker.joinCR(u, cr);
	}
	
	
	@ResponseBody
	@RequestMapping("/user/{user}/all-chat")
	public String allChats(@PathVariable String user) {
		return worker.getAllChats(user);
	}
	
	@RequestMapping("/chat/{chatRoom}")
	public String showChatRoom(@PathVariable String chatRoom, HttpServletRequest req) {
		HttpSession s = req.getSession(false);
		if(s == null) {
			return "redirect:/ ";
		} else {
			req.setAttribute("cr", worker.getChatRoom(chatRoom));
			return "chat-room";
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/me-signup")
	public String signUp(@RequestParam String name, @RequestParam String user,
			@RequestParam String pass, HttpServletRequest req) {
		boolean result = worker.signup(user, pass, name);
		if(true) {
			HttpSession s = req.getSession(true);
			s.setAttribute("user", user);
		}
		return result ? name : "null";
	}
	
	
	@ResponseBody
	@RequestMapping("/sc/{coll}")
	public String showColl(@PathVariable String coll) {
		return worker.showCollection(coll);
	}
	
}
