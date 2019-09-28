import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @auther litan
 * @date:2019/9/28
 */
@SpringBootApplication
@MapperScan("com.cjyc.common.dao")
@ComponentScan({"com.cjyc.common"})
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}
