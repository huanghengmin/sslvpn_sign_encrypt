goto start
  列出crl列表信息
  参数%1表示输出的crl列表文件
:start
openssl crl -in "%1" -text -noout