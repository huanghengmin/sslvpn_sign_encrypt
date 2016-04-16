goto start
  重新生成证书请求文件
  参数%1表示使用的私钥文件
  参数%2表示生成的证书请求文件
  参数%3表示请求的配置文件
:start
openssl req -new -key "%1" -out "%2" -utf8 -config "%3"