goto start
  自签名根证书
  参数$1表示证书有效期(天数)
  参数$2表示证书UUID序列号
  参数$3表示生成私钥长度
  参数$4表示生成私钥文件(包含目录)
  参数$5表示生成证书文件(包含目录)
  参数$6表示根证书的请求配置文件
:start
openssl req -days "$1" -nodes -set_serial "$2" -x509 -newkey rsa:"$3" -keyout "$4" -out "$5" -utf8 -config "$6"