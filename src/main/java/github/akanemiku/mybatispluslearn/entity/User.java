package github.akanemiku.mybatispluslearn.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 与JPA类似，数据库下划线字段可对应实体类的驼峰，可通过注解指定
 * 见：https://mp.baomidou.com/guide/annotation.html
 */
@Data
@TableName("User")
public class User implements Serializable {
    /**
     * 主键
     * 默认是自增Long类型id，若数据库主键自增，需要注解指定type = IdType.AUTO
     */
    @TableId("id")
    private Long id;
    /**
     * 姓名
     */
    //@TableField(value = "name",condition = SqlCondition.LIKE)
    //SqlCondition不支持的可以看源码改
    @TableField(value = "name")
    private String name;
    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 直属上级
     */
    private Long managerId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     * 数据库中无对应字段
     */
    @TableField(exist = false)
    private String remark;
}
