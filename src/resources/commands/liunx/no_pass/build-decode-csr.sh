goto start
  读取CSR证书请求
  参数$1表示csr证书请求文件
:start
openssl req -in "$1" -noout -text
