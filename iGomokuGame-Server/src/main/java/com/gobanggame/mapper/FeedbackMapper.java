package com.gobanggame.mapper;

import com.gobanggame.pojo.entity.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-18 11:19
 * @description:
 **/
@Mapper
public interface FeedbackMapper {
    /**
     * @author AlbertZhang
     * @description 获取所有反馈列表
     * @date 2023-12-18 11:24
     * @param
     * @return java.util.List<com.gobanggame.pojo.entity.Feedback>
     **/
    @Select("select * from feedback")
    List<Feedback> list();

    /**
     * @author AlbertZhang
     * @description 条件更新feedback（后续扩展），目前只更新两个字段
     * @date 2023-12-19 17:43
     * @param feedback
     * @return void
     **/
    @Update("update feedback set fixed=#{fixed} ,fixed_time=#{fixedTime} where id=#{id}")
    void update(Feedback feedback);

    /**
     * @author AlbertZhang
     * @description 新增反馈
     * @date 2023-12-19 17:49
     * @param feedback
     * @return void
     **/
    @Insert("insert into feedback(user_id,feedback_content,feedback_time) values (#{userId},#{feedbackContent},#{feedbackTime})")
    void insert(Feedback feedback);
}
