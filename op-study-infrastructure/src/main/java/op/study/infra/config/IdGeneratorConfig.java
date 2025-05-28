package op.study.infra.config;

import common.util.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成器配置
 */
@Configuration
public class IdGeneratorConfig {

    /**
     * 雪花算法ID生成器
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator();
    }
} 