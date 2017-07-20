package filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.UserService;
import Utils.CookieUtils;
import domain.User;

public class AutoLoginFilter implements Filter{
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException{
		//1.ǿת
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		//2 ����Զ���¼
		//2.1 �ж�session�����޵�¼���û������û�еĻ������Զ���¼
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			//û���û� ��Ҫ�Զ���¼
			//2.2�жϷ��ʵ���Դ�Ƿ�͵�¼��ע����أ�û�еĻ��Զ���¼
			String path= request.getRequestURI();
			if(!path.contains("/login")){
				//2.3��ȡָ��cookie
				Cookie c= CookieUtils.getCookieByName("autologin", request.getCookies());
				//�ж�cookie�Ƿ�Ϊ��
				//����Ϊ�գ���ȡֵ��username��password�� ����service��ɵ�¼���ж�user�Ƿ�Ϊ�գ���Ϊ�գ�����session
				if(c!=null){
					String username=c.getValue().split("-")[0];
					String password=c.getValue().split("-")[1];
					
					//����service��ɵ�¼
					try {
						user = new UserService().login(username, password);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if(user!=null){
						//��User����session��
						request.getSession().setAttribute("user", user);
					}
				}
			}
		}
		
		
	
		//3.����
		chain.doFilter(request, response);
	}
	@Override
	public void destroy() {
		
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
