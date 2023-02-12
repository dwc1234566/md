# 解决用uuid做token时，用户重复登陆的问题

思路：
    第一次用户登陆时生成UUID随机数作为token 查数据库获取用户信息 保存redis中  key token value 用户iduserId，
 同时保存 key userId ，value用户基本信息实体其中包含token字段息， 返回用户基本信息，每次请求时传token去查redis中是否存在
当第二次登陆时 先查数据库获取userId 拿到userId去redis里查询用户实体是否存在，存在说明之前登陆过，拿到其中token，根据key token删除保存在redis中的信息，redis保存新的 key token  和 key userId 

