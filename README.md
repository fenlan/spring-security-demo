## Spring Security权限控制Demo项目
> 极简的Demo，仅作学习使用

### 创建数据库 test
``` sql
CREATE DATABASE test;
```

### 运行项目，项目自动创建表

### 运行security.sql插入数据
```sql
USE test;
INSERT INTO `sys_role`(`id`,`name`) VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');
INSERT INTO `sys_user`(`id`,`password`,`username`) VALUES (1,'root','root'),(2,'sang','sang');
INSERT INTO `sys_user_roles`(`sys_user_id`,`roles_id`) VALUES (1,1),(2,2);
```

### 使用Restful风格借口访问
借助postman进行接口访问，GET访问[http://localhost:8080/](http://localhost:8080/)获得主页，POST访问[http://localhost:8080/login](http://localhost:8080/login)进行登录，POST参数为`username`和`password`