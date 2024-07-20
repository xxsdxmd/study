package op.study.infra.db.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import op.study.infra.db.mp.dao.UserDao;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author xxs
 * @Date 2024/6/30 15:02
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDao> {

}
