package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 5266245672610741252L;
	
	Map<String, Map<String, Member>> _rooms = new HashMap<String, Map<String, Member>>();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String message = request.getParameter("message");
		String username = request.getParameter("user");

		if (action.equals("join"))
			join(request, response, username);
		else if (action.equals("poll"))
			poll(request, response, username);
		else if (action.equals("chat"))
			chat(request, response, username, message);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("action") != null)
			doPost(request, response);
		else
			getServletContext().getNamedDispatcher("default").forward(request, response);
	}	
	
	/**
	 * 
	 * @throws IOException
	 */
	private synchronized void join(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
		Member member = new Member();
		member._name = username;
		Map<String, Member> room = (Map<String, Member>) this._rooms.get(request.getPathInfo());
		if (room == null) {
			room = new HashMap<String, Member>();
			this._rooms.put(request.getPathInfo(), room);
		}
		room.put(username, member);
		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("{action:\"join\"}");
	}

	private synchronized void poll(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
		Map<String, Member> room = (Map<String,Member>) this._rooms.get(request.getPathInfo());
		if (room == null) {
			response.sendError(503);
			return;
		}
		Member member = (Member) room.get(username);
		if (member == null) {
			response.sendError(503);
			return;
		}

		synchronized (member) {
			if (member._queue.size() > 0) {
				response.setContentType("text/json;charset=utf-8");
				StringBuilder buf = new StringBuilder();

				buf.append("{\"action\":\"poll\",");
				buf.append("\"from\":\"");
				buf.append((String) member._queue.poll());   //每一个用户持有一个队列，队列里的元素成对出现（第一个用户名，第二个是消息。见chat方法）
				buf.append("\",");

				String message = (String) member._queue.poll();
				
				//把字符串的双引号"，加上转意符变成：\\"
				int quote = message.indexOf('"');
				while (quote >= 0) {
					message = message.substring(0, quote) + '\\' + message.substring(quote);
					quote = message.indexOf('"', quote + 2);
				}
				
				buf.append("\"chat\":\"");
				buf.append(message);
				buf.append("\"}");
				byte[] bytes = buf.toString().getBytes("utf-8");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
			} else {
				Continuation continuation = ContinuationSupport.getContinuation(request);
				if (continuation.isInitial()) {
					//阴塞请求
					continuation.setTimeout(20000L);
					continuation.suspend();
					member._continuation = continuation;
				} else {
					//让请求超时。
					response.setContentType("text/json;charset=utf-8");
					PrintWriter out = response.getWriter();
					out.print("{action:\"poll\"}");
				}
			}
		}
	}
	
	/**
	 *用户有聊天记录时，把聊天记录加到用户自己的队列，如果用户的在server上有阴塞的标记，且 ？？？
	 */
	private synchronized void chat(HttpServletRequest request, HttpServletResponse response, String username, String message) throws IOException {
		Map<String, Member> room = (Map<String, Member>) this._rooms.get(request.getPathInfo());
		if (room != null) {
			for (Member m : room.values()) {
				synchronized (m) {
					m._queue.add(username);
					m._queue.add(message);

					if (m._continuation != null) {
						m._continuation.resume();
						m._continuation = null;
					}
				}
			}
		}

		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("{action:\"chat\"}");
	}

	class Member {
		String _name;
		Continuation _continuation;
		Queue<String> _queue = new LinkedList();

		Member() {
		}
	}
}