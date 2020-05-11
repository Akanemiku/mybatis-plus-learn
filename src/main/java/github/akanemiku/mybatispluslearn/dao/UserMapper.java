package github.akanemiku.mybatispluslearn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.akanemiku.mybatispluslearn.entity.User;
import org.springframework.stereotype.Repository;

@Repository//不加装载会报错
public interface UserMapper extends BaseMapper<User> {
}
