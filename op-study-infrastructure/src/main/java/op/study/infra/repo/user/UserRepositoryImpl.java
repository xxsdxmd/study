package op.study.infra.repo.user;

import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import op.study.infra.db.convert.UserAssembler;
import op.study.infra.db.mp.dao.UserDao;
import op.study.infra.db.mp.service.UserService;
import op.study.infra.diff.EntityDiff;
import op.study.infra.repo.RepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xxs
 * @Date 2024/7/1 22:35
 * 用户仓储实现
 */
@Slf4j
@Repository
public class UserRepositoryImpl extends RepositorySupport<UserEntity, UserId> implements UserRepository {

    private final UserService userService;
    private final UserAssembler userAssembler;

    public UserRepositoryImpl(UserService userService, UserAssembler userAssembler) {
        super(UserEntity.class);
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @Override
    protected void onInsert(UserEntity userEntity) {
        if (userEntity == null) {
            throw new IllegalArgumentException("用户实体不能为空");
        }
        
        // 转换为数据对象
        UserDao userDao = userAssembler.convertUserDao(userEntity);
        
        // 生成业务ID
        if (userEntity.getUserId() == null) {
            Long businessId = System.currentTimeMillis(); // 简单的ID生成策略
            userDao.setUserId(businessId);
            userEntity.setUserId(new UserId(businessId));
        }
        
        // 保存到数据库
        Long userId = userService.create(userDao);
        log.info("用户插入成功: userId={}, userName={}", userId, userEntity.getUserName());
    }

    @Override
    protected UserEntity onSelect(UserId userId) {
        if (Objects.isNull(userId) || Objects.isNull(userId.getUserId())) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 从数据库查询
        UserDao userDao = userService.findByUserId(userId.getUserId());
        if (userDao == null) {
            log.warn("用户不存在: userId={}", userId.getUserId());
            return null;
        }
        
        // 转换为领域实体
        UserEntity userEntity = userAssembler.convertUserEntity(userDao);
        log.debug("用户查询成功: userId={}, userName={}", userId.getUserId(), userEntity.getUserName());
        
        return userEntity;
    }

    @Override
    protected void onUpdate(UserEntity userEntity, EntityDiff diff) {
        if (userEntity == null || userEntity.getUserId() == null) {
            throw new IllegalArgumentException("用户实体或用户ID不能为空");
        }
        
        // 转换为数据对象
        UserDao userDao = userAssembler.convertUserDao(userEntity);
        
        // 更新到数据库
        userService.updateUser(userDao);
        log.info("用户更新成功: userId={}, userName={}, diff={}", 
                userEntity.getUserId().getUserId(), userEntity.getUserName(), diff);
    }

    @Override
    protected void onDelete(UserEntity userEntity) {
        if (userEntity == null || userEntity.getUserId() == null) {
            throw new IllegalArgumentException("用户实体或用户ID不能为空");
        }
        
        // 软删除
        boolean result = userService.softDeleteUser(userEntity.getUserId().getUserId());
        if (result) {
            log.info("用户删除成功: userId={}, userName={}", 
                    userEntity.getUserId().getUserId(), userEntity.getUserName());
        } else {
            log.warn("用户删除失败: userId={}", userEntity.getUserId().getUserId());
        }
    }

    /**
     * 生成业务ID
     * 这里可以使用雪花算法、UUID或其他ID生成策略
     */
    private Long generateBusinessId() {
        // 简单实现，实际项目中应该使用更复杂的ID生成策略
        return System.currentTimeMillis();
    }

    @Override
    public Optional<UserEntity> findByUserName(String userName) {
        try {
            UserDao userDao = userService.findByUserName(userName);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败: userName={}, error={}", userName, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByMobilePhone(String mobilePhone) {
        try {
            UserDao userDao = userService.findByMobilePhone(mobilePhone);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据手机号查询用户失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        try {
            UserDao userDao = userService.findByEmail(email);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据邮箱查询用户失败: email={}, error={}", email, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUserName(String userName) {
        try {
            return userService.existsByUserName(userName);
        } catch (Exception e) {
            log.error("检查用户名是否存在失败: userName={}, error={}", userName, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByMobilePhone(String mobilePhone) {
        try {
            return userService.existsByMobilePhone(mobilePhone);
        } catch (Exception e) {
            log.error("检查手机号是否存在失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return userService.existsByEmail(email);
        } catch (Exception e) {
            log.error("检查邮箱是否存在失败: email={}, error={}", email, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<UserEntity> findUsersByPage(int pageNum, int pageSize) {
        try {
            // 这里可以调用基础设施层的分页查询服务
            // 暂时返回空列表，实际实现需要在UserService中添加分页方法
            log.info("分页查询用户列表: pageNum={}, pageSize={}", pageNum, pageSize);
            return com.google.common.collect.Lists.newArrayList();
        } catch (Exception e) {
            log.error("分页查询用户失败: pageNum={}, pageSize={}, error={}", pageNum, pageSize, e.getMessage(), e);
            return com.google.common.collect.Lists.newArrayList();
        }
    }

    @Override
    public List<UserEntity> findUsersByStatus(Integer status) {
        try {
            // 这里可以调用基础设施层的状态查询服务
            // 暂时返回空列表，实际实现需要在UserService中添加状态查询方法
            log.info("根据状态查询用户列表: status={}", status);
            return com.google.common.collect.Lists.newArrayList();
        } catch (Exception e) {
            log.error("根据状态查询用户失败: status={}, error={}", status, e.getMessage(), e);
            return com.google.common.collect.Lists.newArrayList();
        }
    }
}
