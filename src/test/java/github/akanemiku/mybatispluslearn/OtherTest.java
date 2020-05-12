package github.akanemiku.mybatispluslearn;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import github.akanemiku.mybatispluslearn.dao.UserMapper;
import github.akanemiku.mybatispluslearn.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OtherTest {
    @Autowired
    private UserMapper mapper;

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
        int rows = mapper.insert(user);
        System.out.println(rows);
    }

    /**
     * 根据id更新
     */
    @Test
    public void updateById(){
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(28);
        user.setEmail("abc@baomidou.com");
        int rows = mapper.updateById(user);
        System.out.println(rows);
    }

    /**
     * 根据实体更新
     */
    @Test
    public void updateByWrapper(){
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<User>();
        userUpdateWrapper.eq("name","李艺伟").eq("age",28);
        User user = new User();
        user.setEmail("lyw2@baomidou.com");
        user.setAge(27);
        int rows = mapper.update(user,userUpdateWrapper);
        System.out.println(rows);
    }

    /**
     * 字段少时
     */
    @Test
    public void updateByWrapper2(){
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<User>();
        userUpdateWrapper.eq("name","李艺伟").eq("age",26).set("age",30);
        int rows = mapper.update(null,userUpdateWrapper);
        System.out.println(rows);
    }

    /**
     * lambda表达式版
     */
    @Test
    public void updateByWrapperLambda(){
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper.eq(User::getName,"李艺伟").eq(User::getAge,30).set(User::getAge,31);
        int rows = mapper.update(null,lambdaUpdateWrapper);
        System.out.println(rows);
    }

    /**
     * 新版写法
     */
    @Test
    public void updateByWrapperLambdaChain(){
        boolean update = new LambdaUpdateChainWrapper<User>(mapper)
                .eq(User::getName,"李艺伟").eq(User::getAge,30).set(User::getAge,31).update();
        System.out.println(update);
    }

}
