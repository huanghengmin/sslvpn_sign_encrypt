goto start
  自签名根证书
  参数%1表示需要吊销的证书
  参数%2表示上级ca私钥文件
  参数%3表示上级ca证书文件
  参数%4表示上级ca配置文件
:start
openssl ca -revoke "%1" -keyfile "%2" -cert "%3"   -utf8 -config "%4"