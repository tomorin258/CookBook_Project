package dao.mappers;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

/**
 * The interface RecipesMapper.
 * Provides CRUD operations for Users table.
 * 
 * @author: Pan Zitao
 */

 public interface UsersMapper {

    /**
     * Add a users entry.
     * @param users the users object
     * @return the boolean
     */
    boolean addUsers(@Param("users") Users users);

    /**
     * Delete a users entry.
     * @param userId the user id
     * @return the boolean
     */
    boolean deleteUsers(@Param("userId") Integer userId);

    /**
     * Update a users entry.
     * @param users the users object
     * @return the boolean
     * */
    boolean updateUsers(@Param("users") Users users);

    /**
     * Get users by id. 
     * @param userId the user id
     * @return the users
     * */
    Users getUsersById(@Param("userId") int userId);

    /**
     * Get users by name.
     * @param userName the user name
     * @return the list of users
     * */
    ArrayList<Users> getUsersByName(@Param("userName") String userName);

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