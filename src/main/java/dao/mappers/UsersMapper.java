package dao.mappers;
import java.util.ArrayList;

import model.Users;

/**
 * The interface RecipesMapper.
 * Provides CRUD operations for Users table.
 * 
 * @author: Pan Zitao
 */

 public interface UsersMapper {

    /**
     * Add a users entry.
     * @return the boolean
     */
    boolean addUsers(Users users);

    /**
     * Delete a users entry.
     * @return the boolean
     */
    boolean deleteUsers(Integer userId);

    /**
     * Update a users entry.
     * @return the boolean
     * */
    boolean updateUsers(Users users);

    /**
     * Get users by id. 
     * @return the users
     * */
    Users getUsersById(int userId);

    /**
     * Get users by name.
     * @return the list of users
     * */
    ArrayList<Users> getUsersByName(String userName);

      /**
     * Get the newest users.
     * @return the newest users
     * */
    Users getNewestUsers();

      /**
     * Get all users.
     * @return the list of all users
     * */
    ArrayList<Users> getAllUsers();
 }