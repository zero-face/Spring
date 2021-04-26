package com.zero.dao;

import com.zero.dataobject.UserPasswordDao;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserPasswordDaoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Thu Apr 01 23:42:27 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Thu Apr 01 23:42:27 CST 2021
     */
    int insert(UserPasswordDao record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Thu Apr 01 23:42:27 CST 2021
     */
    UserPasswordDao selectByPrimaryKey(Integer id);
    UserPasswordDao selectByUserId(Integer UserId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Thu Apr 01 23:42:27 CST 2021
     */
    List<UserPasswordDao> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Thu Apr 01 23:42:27 CST 2021
     */
    int updateByPrimaryKey(UserPasswordDao record);
}