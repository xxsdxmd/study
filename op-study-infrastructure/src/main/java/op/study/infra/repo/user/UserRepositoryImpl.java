package op.study.infra.repo.user;

import domain.entity.UserEntity;
import domain.entity.UserId;
import domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import op.study.infra.cache.UserCacheService;
import op.study.infra.db.convert.UserAssembler;
import op.study.infra.db.mp.dao.UserDao;
import op.study.infra.db.mp.service.UserService;
import op.study.infra.diff.EntityDiff;
import op.study.infra.repo.RepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xxs
 * @Date 2024/7/1 22:35
 * 用户仓储实现 - 集成缓存功能
 */
@Slf4j
@Repository
public class UserRepositoryImpl extends RepositorySupport<UserEntity, UserId> implements UserRepository {

    private final UserService userService;
    private final UserAssembler userAssembler;
    private final UserCacheService userCacheService;

    public UserRepositoryImpl(UserService userService, UserAssembler userAssembler, UserCacheService userCacheService) {
        super(UserEntity.class);
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.userCacheService = userCacheService;
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
        
        // 缓存用户信息
        userCacheService.cacheUser(userEntity);
        
        log.info("用户插入成功: userId={}, userName={}", userId, userEntity.getUserName());
    }

    @Override
    protected void onUpdate(UserEntity userEntity, EntityDiff diff) {
        if (userEntity == null || userEntity.getUserId() == null) {
            throw new IllegalArgumentException("用户实体或ID不能为空");
        }

        // 先删除缓存（延迟双删策略第一步）
        userCacheService.evictUserAll(userEntity);

        // 转换为数据对象
        UserDao userDao = userAssembler.convertUserDao(userEntity);
        
        // 更新数据库
        userService.updateUser(userDao);
        
        // 延迟删除缓存（延迟双删策略第二步）
        userCacheService.delayedEvictUserAll(userEntity);
        
        log.info("用户更新成功: userId={}, userName={}", userEntity.getUserId().getUserId(), userEntity.getUserName());
    }

    @Override
    protected void onDelete(UserEntity userEntity) {
        if (userEntity == null || userEntity.getUserId() == null) {
            throw new IllegalArgumentException("用户实体或ID不能为空");
        }

        // 先删除缓存
        userCacheService.evictUserAll(userEntity);

        // 软删除数据库记录
        UserDao userDao = userAssembler.convertUserDao(userEntity);
        userDao.setDeleted(1); // 标记为已删除
        userService.updateUser(userDao);
        
        log.info("用户删除成功: userId={}, userName={}", userEntity.getUserId().getUserId(), userEntity.getUserName());
    }

    @Override
    protected UserEntity onSelect(UserId userId) {
        if (userId == null) {
            return null;
        }

        // 先查缓存
        UserEntity cachedUser = userCacheService.getCachedUser(userId);
        if (cachedUser != null) {
            log.debug("从缓存获取用户: userId={}", userId.getUserId());
            return cachedUser;
        }

        // 缓存未命中，查数据库
        UserDao userDao = userService.findByUserId(userId.getUserId());
        if (userDao == null) {
            return null;
        }

        UserEntity userEntity = userAssembler.convertUserEntity(userDao);
        
        // 缓存查询结果
        if (userEntity != null) {
            userCacheService.cacheUser(userEntity);
        }
        
        log.debug("从数据库获取用户: userId={}", userId.getUserId());
        return userEntity;
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
        if (userName == null || userName.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            // 先查缓存
            UserEntity cachedUser = userCacheService.getCachedUserByName(userName);
            if (cachedUser != null) {
                log.debug("从缓存根据用户名获取用户: userName={}", userName);
                return Optional.of(cachedUser);
            }

            // 缓存未命中，查数据库
            UserDao userDao = userService.findByUserName(userName);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            
            // 缓存查询结果
            if (userEntity != null) {
                userCacheService.cacheUser(userEntity);
            }
            
            log.debug("从数据库根据用户名获取用户: userName={}", userName);
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败: userName={}, error={}", userName, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByMobilePhone(String mobilePhone) {
        if (mobilePhone == null || mobilePhone.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            // 先查缓存
            UserEntity cachedUser = userCacheService.getCachedUserByMobile(mobilePhone);
            if (cachedUser != null) {
                log.debug("从缓存根据手机号获取用户: mobilePhone={}", mobilePhone);
                return Optional.of(cachedUser);
            }

            // 缓存未命中，查数据库
            UserDao userDao = userService.findByMobilePhone(mobilePhone);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            
            // 缓存查询结果
            if (userEntity != null) {
                userCacheService.cacheUser(userEntity);
            }
            
            log.debug("从数据库根据手机号获取用户: mobilePhone={}", mobilePhone);
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据手机号查询用户失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            UserDao userDao = userService.findByEmail(email);
            UserEntity userEntity = userDao != null ? userAssembler.convertUserEntity(userDao) : null;
            
            // 缓存查询结果
            if (userEntity != null) {
                userCacheService.cacheUser(userEntity);
            }
            
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("根据邮箱查询用户失败: email={}, error={}", email, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return false;
        }

        try {
            // 先查缓存
            UserEntity cachedUser = userCacheService.getCachedUserByName(userName);
            if (cachedUser != null) {
                return true;
            }

            // 缓存未命中，查数据库
            return userService.existsByUserName(userName);
        } catch (Exception e) {
            log.error("检查用户名是否存在失败: userName={}, error={}", userName, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByMobilePhone(String mobilePhone) {
        if (mobilePhone == null || mobilePhone.trim().isEmpty()) {
            return false;
        }

        try {
            // 先查缓存
            UserEntity cachedUser = userCacheService.getCachedUserByMobile(mobilePhone);
            if (cachedUser != null) {
                return true;
            }

            // 缓存未命中，查数据库
            return userService.existsByMobilePhone(mobilePhone);
        } catch (Exception e) {
            log.error("检查手机号是否存在失败: mobilePhone={}, error={}", mobilePhone, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

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
            String cacheKey = String.format("page:%d:%d", pageNum, pageSize);
            
            // 先查缓存
            List<UserEntity> cachedUsers = userCacheService.getCachedUserList(cacheKey);
            if (cachedUsers != null) {
                log.debug("从缓存获取分页用户列表: pageNum={}, pageSize={}", pageNum, pageSize);
                return cachedUsers;
            }

            // 缓存未命中，查数据库
            List<UserDao> userDaos = userService.findUsersByPage(pageNum, pageSize).getRecords();
            List<UserEntity> userEntities = userDaos.stream()
                    .map(userAssembler::convertUserEntity)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // 缓存查询结果
            if (!userEntities.isEmpty()) {
                userCacheService.cacheUserList(cacheKey, userEntities);
            }
            
            log.debug("从数据库获取分页用户列表: pageNum={}, pageSize={}, count={}", 
                    pageNum, pageSize, userEntities.size());
            return userEntities;
        } catch (Exception e) {
            log.error("分页查询用户失败: pageNum={}, pageSize={}, error={}", 
                    pageNum, pageSize, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserEntity> findUsersByStatus(Integer status) {
        try {
            String cacheKey = "status:" + status;
            
            // 先查缓存
            List<UserEntity> cachedUsers = userCacheService.getCachedUserList(cacheKey);
            if (cachedUsers != null) {
                log.debug("从缓存根据状态获取用户列表: status={}", status);
                return cachedUsers;
            }

            // 缓存未命中，查数据库
            List<UserDao> userDaos = userService.findByStatus(status);
            List<UserEntity> userEntities = userDaos.stream()
                    .map(userAssembler::convertUserEntity)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // 缓存查询结果
            if (!userEntities.isEmpty()) {
                userCacheService.cacheUserList(cacheKey, userEntities);
            }
            
            log.debug("从数据库根据状态获取用户列表: status={}, count={}", status, userEntities.size());
            return userEntities;
        } catch (Exception e) {
            log.error("根据状态查询用户失败: status={}, error={}", status, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
