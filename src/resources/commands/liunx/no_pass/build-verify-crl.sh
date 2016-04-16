goto start
  签发用户证书
  参数$1表示cat后的ca证书(多级ca需要cat到一个文件)
  参数$2表示需要吊销检查的证书文件名(包含路径)
:start
openssl verify -CAfile "$1" -crl_check "$2"