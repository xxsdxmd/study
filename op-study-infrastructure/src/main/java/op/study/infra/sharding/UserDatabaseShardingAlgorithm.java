package op.study.infra.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Properties;

/**
 * @author xxs
 * @Date 2024/12/19
 * 用户分库路由算法
 */
@Slf4j
public class UserDatabaseShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    private static final int DATABASE_COUNT = 16;  // 16个数据库

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long userId = shardingValue.getValue();
        
        // 分库逻辑：用户ID % 16 得到数据库索引
        int dbIndex = Math.abs(userId.hashCode()) % DATABASE_COUNT;
        String actualDataSourceName = "ds" + dbIndex;
        
        log.debug("分库路由: userId={}, dataSource={}", userId, actualDataSourceName);
        
        // 确保返回的数据源名称在可用列表中
        if (availableTargetNames.contains(actualDataSourceName)) {
            return actualDataSourceName;
        }
        
        // 如果计算出的数据源不在可用列表中，返回第一个可用的
        return availableTargetNames.iterator().next();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        // 范围查询时返回所有数据源
        Collection<String> result = new LinkedHashSet<>();
        
        for (int i = 0; i < DATABASE_COUNT; i++) {
            String dataSourceName = "ds" + i;
            if (availableTargetNames.contains(dataSourceName)) {
                result.add(dataSourceName);
            }
        }
        
        log.debug("范围分库路由: dataSources={}", result);
        return result.isEmpty() ? availableTargetNames : result;
    }

    @Override
    public void init(Properties props) {
        log.info("用户分库算法初始化完成");
    }

    @Override
    public String getType() {
        return "USER_DATABASE_SHARDING";
    }

    @Override
    public Properties getProps() {
        return new Properties();
    }
} 