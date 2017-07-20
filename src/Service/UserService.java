package Service;

import java.sql.SQLException;

import dao.UserDao;
import domain.User;

public class UserService {

	public User login(String username, String password) throws SQLException {
		return new UserDao().getUserByUsernameAndPwd(username,password);
	}

}
