package github.akanemiku.mybatispluslearn;

import github.akanemiku.mybatispluslearn.dao.UserMapper;
import github.akanemiku.mybatispluslearn.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleTest {

    @Autowired
    private UserMapper mapper;

    /**
     * select * 操作
     */
    @Test
    public void select(){
        List<User> userList = mapper.selectList(null);
        userList.forEach(System.out::println);
    }


    /**
     * 插入操作
     * 默认字段验证策略会忽略非空字段
     * 见：https://mp.baomidou.com/guide/faq.html
     */
    @Test
    public void insert(){
        User user = new User();
        user.setName("王小明");
        user.setAge(17);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        System.out.println(mapper.insert(user));
    }
}
