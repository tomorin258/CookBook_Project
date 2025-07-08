
/**
 * Service class for handling user-related business logic.
 * Provides methods for user login and registration.
 */
package service;

import org.apache.ibatis.session.SqlSession;

import config.MyBatisUtil;
import dao.mappers.UsersMapper;
import model.Users;

public class UserService {
    /**
     * Login check.
     */
    /**
     * Checks user login credentials.
     *
     * @param username the username
     * @param password the password
     * @return the user object if credentials are valid, otherwise null
     */
    public Users login(String username, String password) {
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UsersMapper usersMapper = session.getMapper(UsersMapper.class);
            Users user = usersMapper.getUsersByName(username).stream().findFirst().orElse(null);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        }
    }

    /**
     * Register a new user.
     */
    /**
     * Registers a new user.
     *
     * @param username the username
     * @param password the password
     * @param confirm the password confirmation
     * @return a message indicating the result of registration
     */
    public String register(String username, String password, String confirm) {
        if (username == null || username.isEmpty()) {
            return "account name cannot be empty!";
        }
        if (password == null || password.isEmpty()) {
            return "password cannot be empty!";
        }
        if (!password.equals(confirm)) {
            return "the two passwords do not match!";
        }
        try (SqlSession session = MyBatisUtil.getSqlSession(true)) {
            UsersMapper usersMapper = session.getMapper(UsersMapper.class);
            Users existUser = usersMapper.getUsersByName(username).stream().findFirst().orElse(null);
            if (existUser != null) {
                return "account name already exists, please choose another one!";
            }
            Users newUser = new Users();
            newUser.setUserName(username);
            newUser.setPassword(password);
            usersMapper.addUsers(newUser);
            return "sign up successfully, please login now!";
        }
    }
}
