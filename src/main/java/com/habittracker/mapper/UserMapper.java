package com.habittracker.mapper;

import com.habittracker.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(Long id);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(String username);
    
    @Select("SELECT * FROM users")
    List<User> findAll();
    
    @Insert("INSERT INTO users (username, email, created_at, updated_at) " +
            "VALUES (#{username}, #{email}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
    
    @Update("UPDATE users SET username = #{username}, email = #{email}, " +
            "updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void update(User user);
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(Long id);
}
