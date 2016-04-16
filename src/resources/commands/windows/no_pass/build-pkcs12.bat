goto start
  导出为pkcs文件
  参数%1表示导出文件私钥
  参数%2表示导出证书
  参数%3表示导出的pkcs文件
:start
openssl pkcs12 -nodes -export -inkey "%1" -in "%2" -out "%3" -passout pass: