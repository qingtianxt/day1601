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
		// 0.���ñ���
		request.setCharacterEncoding("utf-8");

		// 1.��ȡ�û���������
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 2.����service
		User user= null;
		try {
			user = new UserService().login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 3.�ж�user�Ƿ�Ϊ��
		if (user == null) {
			request.setAttribute("msg", "�û��������벻ƥ��");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		} else {
			// ����Ϊ�գ���ת��sucess.jsp
			request.getSession().setAttribute("user", user);
			//�ж��Ƿ�ѡ���Զ���¼������ѡ����Ҫ���û���������ŵ�cookie��д�������
			if(Constant.IS_AUTO_LOGIN.equals(request.getParameter("autologin"))){
				//����cookie
				Cookie c = new Cookie("autologin", username+"-"+password);
				c.setMaxAge(3600);
				c.setPath(request.getContextPath()+"/");
				response.addCookie(c);
			}
			//ҳ���ض���
			response.sendRedirect(request.getContextPath()+"/sucess.jsp");
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
