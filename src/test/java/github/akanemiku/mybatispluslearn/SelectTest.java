package github.akanemiku.mybatispluslearn;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import github.akanemiku.mybatispluslearn.dao.UserMapper;
import github.akanemiku.mybatispluslearn.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SelectTest {

    @Autowired
    private UserMapper mapper;

    /**
     * select * 操作
     */
    @Test
    public void select1() {
        List<User> userList = mapper.selectList(null);
        userList.forEach(System.out::println);
    }

    /**
     * select by id操作
     */
    @Test
    public void select2() {
        User user = mapper.selectById(1094592041087729666L);
        System.out.println(user.toString());
    }

    /**
     * select batch 操作
     */
    @Test
    public void select3() {
        List<Long> ids = Arrays.asList(1088248166370832385L, 1088250446457389058L, 1094590409767661570L);
        List<User> userList = mapper.selectBatchIds(ids);
        userList.forEach(System.out::println);
    }

    /**
     * select map 操作
     */
    @Test
    public void select4() {
        Map<String, Object> map = new HashMap<>();
        //map.put("name","张雨琪");
        map.put("age", 24);//注意是数据库中的列名
        List<User> userList = mapper.selectByMap(map);
        userList.forEach(System.out::println);
    }

    /**
     * 名字中包含雨并且年龄小于40
     * name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapper1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //QueryWrapper<User> queryWrapper = Wrappers.<User>query();
        queryWrapper.select("id", "name", "age").like("name", "雨").lt("age", 40);
        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * select id,name,age,email
     */
    @Test
    public void selectByWrapper1_2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time")
                && !info.getColumn().equals("manager_id"))
                .like("name", "雨").lt("age", 40);
        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     * name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    public void selectByWrapper2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .or().ge("age", 25)
                .orderByDesc("age").orderByAsc("id");
        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
     * <p>
     * 使用{0}对应参数，避免sql注入
     * apply见，https://mp.baomidou.com/guide/wrapper.html#apply
     */
    @Test
    public void selectByWrapper3() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14")
                .inSql("manager_id", "select id from user where name like '王%'");

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectByWrapper4() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .and(qw -> qw.lt("age", 40).or().isNotNull("email"));

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     * name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void selectByWrapper5() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .or(qw -> qw.lt("age", 40).gt("age", 20).isNotNull("email"));

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * （年龄小于40或邮箱不为空）并且名字为王姓（and优先级大于or）
     * (age<40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper6() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 年龄为24,26,28,40
     * age in (24,26,28,40)
     */
    @Test
    public void selectByWrapper7() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(24, 26, 28, 40));

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 只返回满足条件的其中一条语句
     * limit 1
     * <p>
     * last有注入风险
     */
    @Test
    public void selectByWrapper8() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(24, 26, 28, 40)).last("limit 1");

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 条件构造
     */
    @Test
    public void teatCondition() {
        String name = "张";
        String email = "";
        condition(name, email);
    }

    private void condition(String name, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 实体构造
     */
    @Test
    public void selectByWrapperEntity() {
        User user = new User();
        user.setName("刘红雨");
        user.setAge(24);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        queryWrapper.lt("age", 40);

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 见，https://mp.baomidou.com/guide/wrapper.html#alleq
     */
    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王昊天");
        map.put("age", null);
        //queryWrapper.allEq(map);
        //queryWrapper.allEq(map,false);
        queryWrapper.allEq((k, v) -> !k.equals("name"), map);

        List<User> userList = mapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
     * 并且只取年龄总和小于500的组。
     * select avg(age) avg_age,min(age) min_age,max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) <500
     */
    @Test
    public void selectByWrapper9() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id").having("sum(age) < {0}", 500);

        List<Map<String, Object>> userList = mapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 记录数
     */
    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨").lt("age", 40);
        Integer count = mapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    /**
     * 只能返回一条，返回多条报错
     */
    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "刘红雨").lt("age", 40);
        User user = mapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    /**
     * lambda查询
     *
     * 普通方式查询若列名写错，编译后才会报错
     * lambda写错时会直接报错
     */
    @Test
    public void selectLambda(){
        //LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        //LambdaQueryWrapper<User> lambda2 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.like(User::getName,"雨").lt(User::getAge,40);
        List<User> userList = mapper.selectList(lambdaQuery);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectLambda2(){
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.likeRight(User::getName,"王")
                .and(lqw->lqw
                        .lt(User::getAge,40)
                        .or()
                        .isNotNull(User::getEmail));
        List<User> userList = mapper.selectList(lambdaQuery);
        userList.forEach(System.out::println);
    }

    /**
     * 新版写法
     */
    @Test
    public void selectLambda3(){
        List<User> userList = new LambdaQueryChainWrapper<User>(mapper)
                .like(User::getName,"雨")
                .ge(User::getAge,20).list();
        userList.forEach(System.out::println);
    }

}
