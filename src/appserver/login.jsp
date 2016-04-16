<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html;charset=utf-8" import="com.hzih.sslvpn.web.SiteContext" %>
<%@include file="/taglib.jsp" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>SSL认证网关管理中心</title>

    <script language="JavaScript">

        function reloadVerifyCode() {
            document.getElementById('verifyCodeImg').src = "<c:url value="/RandomCodeCtrl"/>" + "?tmp=" + RndNum(9000);
        }

        function checkForm() {
            var form = document.forms.loginForm;
            if (form.name.value == null || form.name.value.length == 0) {
                alert("用户名不能为空！");
                return false;
            }
            var password = form.pwd.value;
            var re = /^[0-9a-zA-Z!$#%@^&*()~_+]{8,20}$/;
            if (!re.test(password)) {
                alert("密码必须为大写字母、小写字母、数字和特殊字符的组合，且不少于8位！");
                return false;
            }
            //re = /([0-9].*([a-zA-Z].*[!$#%@^&*()~_+]|[!$#%@^&*()~_+].*[a-zA-Z])|[a-zA-Z].*([0-9].*[!$#%@^&*()~_+]|[!$#%@^&*()~_+].*[0-9])|[!$#%@^&*()~_+].*([0-9].*[a-zA-Z]|[a-zA-Z].*[0-9]))/;
            //if(!re.test(password)){
            //alert("密码必须为大写字母、小写字母、数字和特殊字符的组合，且不少于8位！");
            //return false;
            //}
            if (form.vcode.value == null || form.vcode.value.length == 0) {
                alert("验证码不能为空！");
                return false;
            }
            return true;
        }

        function downWindowsX86(){
            document.getElementById('downWindowsX86').href = "<c:url value="/DownLoadWindowsX86"/>";
        }

        function downWindowsX64(){
            document.getElementById('downWindowsX64').href = "<c:url value="/DownLoadWindowsX64"/>";
        }

        function down_android(){
            document.getElementById('downAndroid').href = "<c:url value="/DownLoadAndroid"/>";
        }

    </script>



</head>


<body style="background-color: #2e394b;text-align: center">

<%--<div style=" vertical-align: middle; width: 800px; height: 450px; border: 0px solid #000000;
    margin-left:auto;margin-right:auto;margin-top: 8%;
    background: url(img/login.png) no-repeat;">--%>
<div style="width: 800px; height: 450px; border: 1px solid #01162b;
            margin-left:auto;margin-right:auto;margin-top: 5%;
            background: url(img/loginVPN_ZD.jpg) no-repeat;"
        >
    <div style="margin-left:auto;margin-right:auto;margin-top:220px;border: 0px solid #acd67a;
                /*width: 400px;*/
                /*padding: 100px 0 0 350px;*/">
        <form name="loginForm" action="login.action" method="post" onsubmit="return checkForm();">
            <table border=0px style="margin-left:350px;padding: 10px 10px 10px 10px;" id=myTable>
                <tr>
                    <td align=right>&nbsp;用户名：</td>
                    <td colspan="1"><input type="text" name="name" autocomplete="off" style="width:140px;"/></td>
                </tr>
                <tr>
                    <td align=right>&nbsp;密&nbsp;&nbsp;码：</td>
                    <td colspan="1"><input type="password" name="pwd" autocomplete="off" style="width:140px;"/></td>
                </tr>
                <tr>
                    <td align=right>&nbsp;验证码：</td>
                    <td><INPUT TYPE="text" autocomplete="off" NAME="vcode" style="width: 80px;"><img
                            src="<c:url value="/RandomCodeCtrl"/>" height="23" width="60"
                            align="middle" id="verifyCodeImg" onclick="reloadVerifyCode();"
                            alt="单击更换验证码"/></td>
                </tr>
                <tr>
                    <td/>
                    <td colspan="1"><input type="submit" value="登&nbsp;&nbsp;录"/>&nbsp;&nbsp;<input
                            type="reset" value="取&nbsp;&nbsp;消"/></td>
                </tr>

                <tr>
                    <td/>
                    <td>
                        <a href="javascript:void(0);" id="downWindowsX86" onclick="downWindowsX86();"><font color="#00008b">Windows X86</font></a> &nbsp;
                        <a href="javascript:void(0);" id="downWindowsX64" onclick="downWindowsX64();"><font color="#00008b">Windows X64</font></a> &nbsp;
                        <a href="javascript:void(0);" id="downAndroid" onclick="down_android();"><font color="#00008b">Android</font></a>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>

<%--<body style="background-color:  #00172b">

<div
        style="width: 466px; height: 320px; border: 0px solid #000000; margin: 0 auto; margin-top: 120px; background: url(img/loginVPN_CX.jpg) no-repeat;">
    <form name="loginForm" action="login.action" method="post"
          style="margin: 0; padding: 0;border:0;" onsubmit="return checkForm();">
        <div style="padding: 100px 0 0 200px">
            <table cellspacing=3 cellpadding=0 border=0>
                <tr>
                    <td align=right>用户名：</td>
                    <td><input type="text" name="name" autocomplete="off" style="width:140px;"/></td>
                </tr>
                <tr>
                    <td align=right>密&nbsp;&nbsp;码：</td>
                    <td><input type="password" name="pwd" autocomplete="off" style="width:140px;"/></td>
                </tr>
                <tr>
                    <td align=right>验证码：</td>
                    <td><INPUT TYPE="text" autocomplete="off" NAME="vcode" style="width: 80px;"><img
                            src="<c:url value="/RandomCodeCtrl"/>" height="20" width="60"
                            align="absmiddle" id="verifyCodeImg" onclick="reloadVerifyCode();"
                            alt="单击更换验证码" style="cursor: hand;"/></td>
                </tr>
                <tr>
                    <td/>
                    <td><input type="submit" value="登&nbsp;&nbsp;录"/>&nbsp;&nbsp;<input
                            type="reset" value="取&nbsp;&nbsp;消"/></td>
                </tr>

                &lt;%&ndash; <tr>
                <td/>
                <td>
                <a href="javascript:void(0);" id="downWin32" onclick="down_win32();"><font color="#00008b">Win32</font></a> &nbsp;
                <a href="javascript:void(0);" id="downWin64" onclick="down_win64();"><font color="#00008b">Win64</font></a>&nbsp;
                <a href="javascript:void(0);" id="downAndroid" onclick="down_android();"><font color="#00008b">Android</font></a>
                </tr>&ndash;%&gt;
            </table>
        </div>
    </form>
</div>
</body>--%>
</html>

