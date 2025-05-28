package op.study.infra.service;

import common.util.SnowflakeIdGenerator;
import domain.entity.UserId;
import domain.service.user.UserIdGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户ID生成服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserIdGeneratorServiceImpl implements UserIdGeneratorService {

    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public UserId generateUserId() {
        long id = snowflakeIdGenerator.nextId();
        log.debug("生成新用户ID: {}", id);
        return new UserId(id);
    }

    @Override
    public UserId createUserId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return new UserId(id);
    }
} 