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
 * 用户分库分表路由算法
 */
@Slf4j
public class UserShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    private static final int DATABASE_COUNT = 16;  // 16个数据库
    private static final int TABLE_COUNT_PER_DB = 128;  // 每个数据库128张表

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long userId = shardingValue.getValue();
        String logicTableName = shardingValue.getLogicTableName();
        
        if ("t_user".equals(logicTableName)) {
            // 分表逻辑：用户ID % 2048 得到表后缀
            long tableIndex = Math.abs(userId.hashCode()) % (DATABASE_COUNT * TABLE_COUNT_PER_DB);
            String actualTableName = logicTableName + "_" + String.format("%04d", tableIndex);
            
            log.debug("分表路由: userId={}, logicTable={}, actualTable={}", 
                    userId, logicTableName, actualTableName);
            return actualTableName;
        }
        
        return availableTargetNames.iterator().next();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        // 范围查询时返回所有可能的表
        Collection<String> result = new LinkedHashSet<>();
        String logicTableName = shardingValue.getLogicTableName();
        
        if ("t_user".equals(logicTableName)) {
            // 对于范围查询，需要查询所有表
            for (int i = 0; i < DATABASE_COUNT * TABLE_COUNT_PER_DB; i++) {
                String tableName = logicTableName + "_" + String.format("%04d", i);
                if (availableTargetNames.contains(tableName)) {
                    result.add(tableName);
                }
            }
        }
        
        log.debug("范围分表路由: logicTable={}, actualTables={}", logicTableName, result.size());
        return result.isEmpty() ? availableTargetNames : result;
    }

    @Override
    public void init(Properties props) {
        log.info("用户分表算法初始化完成");
    }

    @Override
    public String getType() {
        return "USER_TABLE_SHARDING";
    }

    @Override
    public Properties getProps() {
        return new Properties();
    }
} 