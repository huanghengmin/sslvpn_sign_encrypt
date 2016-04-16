package com.hzih.sslvpn.web.action.sslvpn.crl;

import com.hzih.sslvpn.web.action.audit.AuditFlagAction;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Date;

/**
 * Created by Administrator on 15-4-27.
 */
public class HttpCRLDownLoad {
    private Logger logger = Logger.getLogger(HttpCRLDownLoad.class);

    public InputStream download(String url) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(5 * 1000);
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5 * 1000);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(post);
            if (statusCode == 200) {
                InputStream data = post.getResponseBodyAsStream();
                return data;
            }
        } catch (Exception e) {
            if(AuditFlagAction.getAuditFlag()) {
//                logger.error(e.getMessage(),e);
                logger.error("Http下载点下载CRL列表失败,时间:" + new Date() + "," + e.getMessage(),e);
            }
        }
        return null;
    }
}
