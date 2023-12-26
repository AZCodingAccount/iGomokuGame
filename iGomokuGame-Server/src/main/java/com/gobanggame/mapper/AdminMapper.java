package com.gobanggame.mapper;

import com.gobanggame.pojo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-20 18:40
 * @description:
 **/
@Mapper
public interface AdminMapper {

    /**
     * @author AlbertZhang
     * @description 管理员登录——根据传过来的用户名和密码看看是否有匹配的
     * @date 2023-12-20 18:42
     * @param admin
     * @return void
     **/
    @Select("select * from admin where username=#{username} and password=#{password}")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Admin login(Admin admin);
}
