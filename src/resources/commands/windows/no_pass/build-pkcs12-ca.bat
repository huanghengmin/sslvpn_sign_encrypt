goto start
  导出为pkcs文件(带入证书链)
  参数%1表示导出文件私钥
  参数%2表示导出证书
  参数%3表示经过cat后的证书链
  参数%4表示导出的pkcs文件
:start
openssl pkcs12 -export -inkey "%1" -in "%2" -certfile "%3" -out "%4" -passout pass: