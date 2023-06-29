# CraftFrameBoot 

#### 介绍
精心打造的Spring框架，涵盖Web、代码生成和权限等多个模块，为开发者提供全方位的支持

```yaml
security:
  config:
    enable: true #是否开启spring security 如果关闭 则不可使用@PreAuthorize注解
    ignore-interface: [ /test/login ] #Security 不拦截接口
    logout-path: /test/exit #定义退出接口路径 若定义该路径则不会走自己定义的controller方法,可以实现LogoutSuccessHandler接口进行重写
jwt:
  config:
    prefix: Bearer #JWT前缀
    header: Authorization #JWT token 请求头
    expiration: 60 #默认过期时间
    expiration-remember: 604800 #勾选记住我的过期时间
    secret: 441E0738EC706461B33D9FC3CF86B2ADFA27FDD6DD90A7426660D1304804DD5C #JWT密钥(SHA256)
```

