goto start
  生成证书请求文件
  参数$1表示生成私钥长度
  参数$2表示生成私钥文件
  参数$3表示生成证书请求文件
  参数$4表示签发ca配置文件
:start
openssl req -nodes -newkey rsa:"$1" -keyout "$2"  -out "$3" -utf8 -config "$4"