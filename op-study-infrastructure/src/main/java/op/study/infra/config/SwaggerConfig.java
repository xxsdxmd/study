package op.study.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxs
 * @Date 2024/12/19
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户域DDD项目API")
                        .description("基于领域驱动设计的用户管理系统API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("xxs")
                                .email("xxs@example.com")
                                .url("https://github.com/xxs/user-domain-ddd"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 