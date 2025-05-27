# 用户域DDD项目

基于领域驱动设计（DDD）架构的用户管理系统，采用Spring Boot + MyBatis Plus技术栈实现。

## 项目架构

本项目严格按照DDD分层架构设计：

```
├── op-study-iface/          # 接口层（Interface Layer）
│   ├── controller/          # REST API控制器
│   └── impl/               # 接口实现
├── op-study-application/    # 应用层（Application Layer）
│   ├── service/            # 应用服务
│   ├── model/              # 数据传输对象
│   └── factory/            # 工厂类
├── op-study-domain/         # 领域层（Domain Layer）
│   ├── entity/             # 领域实体
│   ├── service/            # 领域服务
│   ├── repository/         # 仓储接口
│   └── marker/             # 标记接口
├── op-study-infrastructure/ # 基础设施层（Infrastructure Layer）
│   ├── db/                 # 数据库相关
│   ├── repo/               # 仓储实现
│   └── config/             # 配置类
└── op-study-common/         # 公共组件
    ├── enums/              # 枚举类
    ├── filter/             # 过滤器
    ├── result/             # 统一返回结果
    └── utils/              # 工具类
```

## 技术栈

- **框架**: Spring Boot 2.7.9
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.7
- **缓存**: Redis
- **文档**: Swagger 3
- **构建工具**: Maven
- **Java版本**: JDK 1.8

## 核心功能

### 用户管理
- ✅ 用户注册
- ✅ 用户登录
- ✅ 用户信息更新
- ✅ 用户状态管理（激活/停用/删除）
- ✅ 密码管理（修改/重置）
- ✅ 用户查询（单个/分页/搜索）

### 架构特性
- ✅ DDD分层架构
- ✅ 领域驱动设计
- ✅ 过滤器链模式
- ✅ 事件驱动架构
- ✅ 仓储模式
- ✅ 聚合根设计
- ✅ 值对象封装

## 快速开始

### 1. 环境准备

确保你的开发环境已安装：
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化

```sql
-- 执行数据库初始化脚本
source src/main/resources/sql/init.sql
```

### 3. 配置修改

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_domain
    username: your_username
    password: your_password
```

### 4. 启动项目

```bash
# 编译项目
mvn clean compile

# 启动项目
mvn spring-boot:run
```

### 5. 访问接口

- **Swagger文档**: http://localhost:8080/swagger-ui.html
- **API接口**: http://localhost:8080/api/v1/users

## API接口

### 用户注册
```http
POST /api/v1/users/register
Content-Type: application/json

{
    "userName": "testuser",
    "mobilePhone": "13800138000",
    "email": "test@example.com"
}
```

### 用户登录
```http
POST /api/v1/users/login
Content-Type: application/x-www-form-urlencoded

loginName=testuser&password=123456
```

### 查询用户
```http
GET /api/v1/users/{userId}
```

### 更新用户
```http
PUT /api/v1/users/{userId}
Content-Type: application/json

{
    "userName": "newname",
    "mobilePhone": "13800138001"
}
```

### 分页查询
```http
GET /api/v1/users/page?pageNum=1&pageSize=10
```

## 项目特色

### 1. 严格的DDD分层
- **接口层**: 只负责接收请求和返回响应
- **应用层**: 业务流程编排，调用领域服务
- **领域层**: 核心业务逻辑，包含实体和领域服务
- **基础设施层**: 技术实现，如数据库访问

### 2. 过滤器链模式
```java
// 支持灵活的业务规则配置
FilterSelector selector = factory.getFilterSelector(EnumFilterSelectorScene.REGISTER);
UserRequestContext context = buildUserRequestContext(userRequest, selector);
executeFilterChain(context);
```

### 3. 聚合根设计
```java
@Data
@Builder
public class UserEntity implements Aggregate<UserId> {
    // 业务方法封装在实体内部
    public void activate() { ... }
    public void deactivate() { ... }
    public void updateUserInfo(String userName, String email) { ... }
}
```

### 4. 仓储模式
```java
// 领域层定义接口
public interface Repository<T extends Aggregate<ID>, ID extends Identifier> {
    T find(ID id);
    void save(T agg);
    void remove(T agg);
}

// 基础设施层实现
@Repository
public class UserRepositoryImpl extends RepositorySupport<UserEntity, UserId> 
    implements Repository<UserEntity, UserId> {
    // 具体实现
}
```

## 开发规范

### 1. 包命名规范
- `com.example.iface`: 接口层
- `com.example.application`: 应用层
- `domain`: 领域层
- `op.study.infra`: 基础设施层
- `com.example.opstudycommon`: 公共组件

### 2. 类命名规范
- 实体类: `XxxEntity`
- 值对象: `XxxId`, `XxxValue`
- 领域服务: `XxxDomainService`
- 应用服务: `XxxService`
- 仓储: `XxxRepository`
- 数据对象: `XxxDao`

### 3. 代码规范
- 使用Lombok减少样板代码
- 统一异常处理
- 完善的日志记录
- 参数校验
- 事务管理

## 测试

### 单元测试
```bash
mvn test
```

### 集成测试
```bash
mvn integration-test
```

## 部署

### Docker部署
```bash
# 构建镜像
docker build -t user-domain-ddd .

# 运行容器
docker run -p 8080:8080 user-domain-ddd
```

### 传统部署
```bash
# 打包
mvn clean package

# 运行
java -jar target/study-0.0.1-SNAPSHOT.jar
```

## 监控

项目集成了以下监控功能：
- 应用性能监控
- 数据库性能监控
- 业务指标监控
- 日志监控

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目采用MIT许可证。

## 联系方式

- 作者: xxs
- 邮箱: xxs@example.com
- 项目地址: https://github.com/xxs/user-domain-ddd

---

**注意**: 这是一个学习和演示项目，展示了如何使用DDD架构构建用户管理系统。在生产环境中使用前，请确保进行充分的测试和安全评估。 