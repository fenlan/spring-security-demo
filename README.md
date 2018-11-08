## Spring Security权限控制Demo项目
> 极简的Demo，仅作学习使用

### 创建数据库 test
``` sql
CREATE DATABASE test;
```

### 运行项目，项目自动创建表

### 运行security.sql插入数据
```sql
use test;
insert  into `sys_role`(`name`) values ('ROLE_ADMIN'),('ROLE_USER');
```

### 使用Restful风格借口访问
借助postman进行接口访问，GET访问[http://localhost:8080/](http://localhost:8080/)获得主页，POST访问[http://localhost:8080/login](http://localhost:8080/login)进行登录，
POST参数为`username`、`password`和`remember-me`

### password Encode
在`WebSecurityConfig`中注册一个`BCryptPasswordEncoder`
```java
@SuppressWarnings("deprecation")
@Bean
public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
此时用户登录时会将输入的密码进行加密，并与数据库中已经同样算法加密的密码进行匹配。
在用户注册时使用相同算法加密
```java
SysUser newUser = new SysUser();
SysRole role = roleRep.findByName("ROLE_USER");
newUser.setUsername(username);
newUser.setPassword(new BCryptPasswordEncoder().encode(password));
newUser.setRoles(Arrays.asList(role));
userRep.save(newUser);
```

### Remember Me
基于数据库的持久化token
`persistent_logins`表(可以使用jpa自动化建表)
```sql
create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null)
```
自动化建表
```java
@Entity
public class PersistentLogins {

    @NotNull
    private String username;
    @Id
    private String series;
    @NotNull
    private String token;
    @NotNull
    private Timestamp lastUsed;
    
    ...set & get function...
}
```

主要注册三个Bean，分别是 `rememberMeServices`、`rememberMeAuthenticationFilter`、`rememberMeAuthenticationProvider`，三者的作用可以仔细查看spring security官方文档

#### TokenBasedRememberMeServices
> `TokenBasedRememberMeServices` generates a `RememberMeAuthenticationToken`, 
> which is processed by `RememberMeAuthenticationProvider`. A `key` is shared between 
> this authentication provider and the `TokenBasedRememberMeServices`. In addition, 
> `TokenBasedRememberMeServices` requires A `UserDetailsService` from which it can 
> retrieve the username and password for signature comparison purposes, and generate 
> the `RememberMeAuthenticationToken` to contain the correct `GrantedAuthority` s. 
> Some sort of logout command should be provided by the application that invalidates 
> the cookie if the user requests this. `TokenBasedRememberMeServices` also implements 
> Spring Security’s `LogoutHandler` interface so can be used with `LogoutFilter` to have 
> the cookie cleared automatically.

> The beans required in an application context to enable remember-me services are as follows:
```xml
<bean id="rememberMeFilter" class=
"org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter">
<property name="rememberMeServices" ref="rememberMeServices"/>
<property name="authenticationManager" ref="theAuthenticationManager" />
</bean>

<bean id="rememberMeServices" class=
"org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices">
<property name="userDetailsService" ref="myUserDetailsService"/>
<property name="key" value="springRocks"/>
</bean>

<bean id="rememberMeAuthenticationProvider" class=
"org.springframework.security.authentication.RememberMeAuthenticationProvider">
<property name="key" value="springRocks"/>
</bean>
```

> Don’t forget to add your `RememberMeServices` implementation to your
> `UsernamePasswordAuthenticationFilter.setRememberMeServices()` property, include the
> `RememberMeAuthenticationProvider` in your `AuthenticationManager.setProviders()` list, 
> and add `RememberMeAuthenticationFilter` into your `FilterChainProxy` (typically immediately
> after your `UsernamePasswordAuthenticationFilter`).

#### PersistentTokenBasedRememberMeServices
> This class can be used in the same way as `TokenBasedRememberMeServices`, but it additionally
> needs to be configured with a `PersistentTokenRepository` to store the tokens. There are 
> two standard implementations.
> - `InMemoryTokenRepositoryImpl` which is intended for testing only(当用户量大时，内存不够存储token).
> - `JdbcTokenRepositoryImpl` which stores the tokens in a database.

基于java config的例子
```java
@Bean
public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
    return new RememberMeAuthenticationProvider("TEST");
}

@Bean
public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
    return new RememberMeAuthenticationFilter(authenticationManager(), rememberMeServices());
}

@Bean
public RememberMeServices rememberMeServices() {
    JdbcTokenRepositoryImpl rememberMeTokenRepository = new JdbcTokenRepositoryImpl();
    // 此处需要设置数据源，否则无法从数据库查询验证信息
    rememberMeTokenRepository.setDataSource(dataSource);

    // 此处的 key 可以为任意非空值(null 或 "")，单必须和起前面
    // rememberMeServices(RememberMeServices rememberMeServices).key(key)的值相同
    PersistentTokenBasedRememberMeServices rememberMeServices =
           new PersistentTokenBasedRememberMeServices("TEST", customUserService(), rememberMeTokenRepository);

    // 该参数不是必须的，默认值为 "remember-me", 但如果设置必须和页面复选框的 name 一致
    rememberMeServices.setParameter("remember-me");
    return rememberMeServices;
    }
```
http config
```java
http.rememberMe().rememberMeServices(rememberMeServices()).userDetailsService(customUserService()).key("TEST")
```
