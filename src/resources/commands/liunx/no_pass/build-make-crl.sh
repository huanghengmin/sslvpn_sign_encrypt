goto start
  构建crl列表
  参数$1表示输出的crl列表文件
  参数$2表示生成crl的ca私钥
  参数$3表示生成crl的ca证书文件
  参数$4表示生成crl的ca配置文件
:start
openssl ca -gencrl  -out "$1" -keyfile "$2" -cert "$3" -utf8 -config "$4"