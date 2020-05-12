package github.akanemiku.mybatispluslearn;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import github.akanemiku.mybatispluslearn.dao.UserMapper;
import github.akanemiku.mybatispluslearn.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void selectPage(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",20);

        Page<User> page = new Page<>(1,2);
        IPage<User> iPage = mapper.selectPage(page,queryWrapper);
        System.out.println("总页数："+iPage.getPages());
        System.out.println("总记录数："+iPage.getTotal());

        List<User> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }

    @Test
    public void selectPage2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",20);

        Page<User> page = new Page<>(1,2);

        IPage<Map<String, Object>> iPage = mapper.selectMapsPage(page,queryWrapper);
        System.out.println("总页数："+iPage.getPages());
        System.out.println("总记录数："+iPage.getTotal());
        List<Map<String, Object>> userList = iPage.getRecords();

        userList.forEach(System.out::println);
    }

    /**
     * 自定义方法分页
     */
    @Test
    public void selectPage3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",20);

        Page<User> page = new Page<>(1,2);

        IPage<User> iPage = mapper.selectUserPage(page,queryWrapper);
        System.out.println("总页数："+iPage.getPages());
        System.out.println("总记录数："+iPage.getTotal());
        List<User> userList = iPage.getRecords();

        userList.forEach(System.out::println);
    }
}
