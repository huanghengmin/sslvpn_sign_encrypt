package com.hzih.sslvpn.web.action;

import com.hzih.sslvpn.utils.CheckTimeResult;
import com.hzih.sslvpn.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ActionBase {

	private static final long serialVersionUID = -3517772370262338399L;

	public String actionBegin(HttpServletRequest request){
		request.getCharacterEncoding();
		String result = new CheckTimeResult().getResult(request);
		return result;
	}
	
	public void actionEnd(HttpServletResponse response,String json,String result) throws IOException {
		response.setCharacterEncoding("utf-8");
		StringUtils st = new StringUtils();
		response.setContentType("text/html");//上传文件回调函数处理时需要
		response.getWriter().print(result);
		
		response.getWriter().write(st.trim(json));
		response.getWriter().close();
	}

	public void actionEnd(HttpServletResponse response, String json)throws IOException{
		response.setCharacterEncoding("utf-8");
		StringUtils st = new StringUtils();
		response.getWriter().write(st.trim(json));
		response.getWriter().close();
	}
}
