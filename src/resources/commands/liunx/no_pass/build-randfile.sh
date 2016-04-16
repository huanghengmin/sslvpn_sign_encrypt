goto start
  生成随机数文件
  参数$1表示生成随机数文件路径
  参数$2表示生成随机数大小
:start
openssl rand -out "$1" "$2"