package Servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Constant.Constant;
import Service.UserService;
import domain.User;

/**
 *
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 0.设置编码
		request.setCharacterEncoding("utf-8");

		// 1.获取用户名和密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 2.调用service
		User user= null;
		try {
			user = new UserService().login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 3.判断user是否为空
		if (user == null) {
			request.setAttribute("msg", "用户名和密码不匹配");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		} else {
			// 若不为空，跳转到sucess.jsp
			request.getSession().setAttribute("user", user);
			//判断是否勾选了自动登录。若勾选了需要将用户名和密码放到cookie中写回浏览器
			if(Constant.IS_AUTO_LOGIN.equals(request.getParameter("autologin"))){
				//创建cookie
				Cookie c = new Cookie("autologin", username+"-"+password);
				c.setMaxAge(3600);
				c.setPath(request.getContextPath()+"/");
				response.addCookie(c);
			}
			//页面重定向
			response.sendRedirect(request.getContextPath()+"/sucess.jsp");
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
