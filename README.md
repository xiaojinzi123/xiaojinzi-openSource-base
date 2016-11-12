[TOC]
#第一个
##第二个

# Game Master 接口说明 V0.0.1

###目录
- [基本介绍](#基本介绍)
- [登录注册](#登录注册)
- [主页](#主页)
- [发现](#发现)
- [我的](#我的)
- [发布](#发布)
- [消息](#消息)
- [Master](#Master)
- [通用接口](#通用接口)


#基本介绍

- 除特殊说明外，HTTP 方法均为 POST

- 请求均使用 Json 数据

- 响应除特殊说明，均为 Json

- 字段基本为 String 或 Number 类型

- Status 状态码

	- 0 请求正常
	- 1 request 格式错误
	- 2 需重新登录
	- 4 服务器错误


- 接口分为三种情况：
	
	- 需要 session_id
	- 需要 session\_id 和 object\_id
	- 都不需要

#### 2、登录注册           [返回目录](#目录)
- ##### 2.2 注册用户

- 请求地址：auth\_resginer\_message 
- 请求方法：POST

- 请求参数：

参数       | 参数类型 | 是否必选 | 说明
------------ | --- | --- | --- | --- 
session_id   | String |  是 | 
phone_number   | String |  是 | 手机号
password   | String |  是 | 密码
code   | String |  是 | 短信验证码

- 响应参数

参数       | 参数类型 | 是否必选 | 说明
------------ | --- | --- | --- | --- 
session_id   | String |  是 | 非 Json
user_id   | String |  是 |  用户唯一 id

#### 3、主页           [返回目录](#目录)

#### 4、发现           [返回目录](#目录)

#### 5、我的            [返回目录](#目录)

#### 6、发布           [返回目录](#目录)

#### 7、消息           [返回目录](#目录)

#### 8、 Master           [返回目录](#目录)

#### 9、 通用接口           [返回目录](#目录)

- ##### 9.1 获取 session_id

- 请求地址：acquire\_session\_id 
- 请求方法：GET

- 请求参数：无

- 响应参数

参数       | 参数类型 | 是否必选 | 说明
------------ | --- | --- | --- | --- 
session_id   | String |  是 | 非 Json

- ##### 9.2 获取 session_id

- 请求地址：acquire\_session\_id 
- 请求方法：GET

- 请求参数：无

- 响应参数

参数       | 参数类型 | 是否必选 | 说明
------------ | --- | --- | --- | --- 
session_id   | String |  是 | 非 Json
