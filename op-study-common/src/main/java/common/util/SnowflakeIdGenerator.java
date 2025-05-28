package common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * @author xxs
 * @Date 2024/12/19
 * 雪花算法ID生成器
 */
@Slf4j
@Component
public class SnowflakeIdGenerator {

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        // 获取机器ID（基于IP地址）
        long workerId = getWorkerId();
        // 获取数据中心ID（可以基于配置或其他方式）
        long datacenterId = getDatacenterId();
        
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        log.info("雪花算法ID生成器初始化完成: workerId={}, datacenterId={}", workerId, datacenterId);
    }

    /**
     * 生成下一个ID
     */
    public long nextId() {
        return snowflake.nextId();
    }

    /**
     * 生成下一个ID字符串
     */
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 获取工作机器ID
     */
    private long getWorkerId() {
        try {
            String hostAddress = NetUtil.getLocalhostStr();
            // 使用IP地址的最后一段作为机器ID
            String[] parts = hostAddress.split("\\.");
            if (parts.length == 4) {
                int lastPart = Integer.parseInt(parts[3]);
                return lastPart % 32;
            }
            
            // 如果IP解析失败，使用IP地址的hashCode
            return Math.abs(hostAddress.hashCode()) % 32;
        } catch (Exception e) {
            log.warn("获取工作机器ID失败，使用默认值: {}", e.getMessage());
            return 1L;
        }
    }

    /**
     * 获取数据中心ID
     */
    private long getDatacenterId() {
        try {
            String hostName = NetUtil.getLocalHostName();
            if (hostName != null && !hostName.isEmpty()) {
                return Math.abs(hostName.hashCode()) % 32;
            }
            
            // 如果主机名获取失败，尝试使用MAC地址
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                String macAddress = localHost.getHostAddress();
                return Math.abs(macAddress.hashCode()) % 32;
            } catch (Exception ex) {
                log.warn("获取MAC地址失败: {}", ex.getMessage());
            }
            
            return 1L;
        } catch (Exception e) {
            log.warn("获取数据中心ID失败，使用默认值: {}", e.getMessage());
            return 1L;
        }
    }

    /**
     * 根据ID获取时间戳
     */
    public long getTimestamp(long id) {
        try {
            return snowflake.getGenerateDateTime(id);
        } catch (Exception e) {
            log.warn("解析ID时间戳失败: id={}, error={}", id, e.getMessage());
            return System.currentTimeMillis();
        }
    }
} 