package com.gobanggame.service;

import com.gobanggame.pojo.dto.UserRegisterDTO;
import com.gobanggame.pojo.dto.UserUpdateDTO;
import com.gobanggame.pojo.entity.User;
import com.gobanggame.pojo.vo.UserLoginVO;
import com.gobanggame.pojo.vo.UserSelectByIdVO;

/**
 * @program: gobang
 * @author: AlbertZhang
 * @create: 2023-12-09 17:43
 * @description: 用户Service
 **/
public interface UserService {

    /**
     * @author AlbertZhang
     * @description 用户注册
     * @date 2023-12-09 21:24
     * @param userRegisterDTO
     * @return void
     **/
     void register(UserRegisterDTO userRegisterDTO);


     /**
      * @author AlbertZhang
      * @description 用户登录
      * @date 2023-12-09 22:07
      * @param userLoginDTO
      * @return com.gobanggame.pojo.vo.UserLoginVO
      **/
    UserLoginVO login(UserRegisterDTO userLoginDTO);

    /**
     * @author AlbertZhang
     * @description 根据id查询用户信息
     * @date 2023-12-09 23:15
     * @param id
     * @return com.gobanggame.pojo.vo.UserSelectByIdVO
     **/
    UserSelectByIdVO getById(Long id);

    /**
     * @author AlbertZhang
     * @description 更新用户信息
     * @date 2023-12-09 23:22
     * @param userUpdateDTO
     * @return void
     **/
    void update(UserUpdateDTO userUpdateDTO);


    /**
     * @author AlbertZhang
     * @description 用户注销
     * @date 2023-12-09 23:41
     * @param id
     * @return void
     **/
    void deleteById(Long id);
}
