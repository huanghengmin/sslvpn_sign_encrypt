goto start
  签发证书请求
  参数%1表示生成证书UUID序列号
  参数%2表示csr请求文件
  参数%3上级ca配置文件
  参数%4表示生成证书扩展选项
  参数%5表示上级ca证书文件
  参数%6表示上级ca私钥文件
  参数%7表示生成证书文件
  参数%8表示证书有效期天数
:start
openssl x509 -set_serial "%1" -req -in "%2" -sha1 -extfile "%3" -extensions "%4" -CA "%5" -CAkey "%6" -CAcreateserial -out "%7" -days "%8"
