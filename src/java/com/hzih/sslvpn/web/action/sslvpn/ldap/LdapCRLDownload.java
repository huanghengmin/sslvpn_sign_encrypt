package com.hzih.sslvpn.web.action.sslvpn.ldap;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * Created by Administrator on 15-4-27.
 */
public class LdapCRLDownload {
    private Logger logger = Logger.getLogger(LdapCRLDownload.class);

    public byte[] getByte(String host,String port,String adm,String pass,String search_path,
                          String filter_string,String attribute_name){
        Hashtable<String, String> env = null;
        env= new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://"+host+":"+port);
        env.put(Context.AUTHORITATIVE, "simple");
        if(adm!=null&&adm.length()>0)
            env.put(Context.SECURITY_PRINCIPAL, adm);
        if(pass!=null&&pass.length()>0)
            env.put(Context.SECURITY_CREDENTIALS,pass);
        env.put("com.sun.jndi.ldap.connect.pool", "true");
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            logger.info(e.getMessage(),e);
            return null;
        }
        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        sc.setReturningAttributes(new String[]{attribute_name});
        sc.setReturningObjFlag(true);
        NamingEnumeration results = null;
        try {
            results = ctx.search(search_path,filter_string,sc);
        } catch (NamingException e) {
            logger.info(e.getMessage(),e);
            return null;
        }
        try {
            while (results.hasMore()) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attributes = sr.getAttributes();
                Attribute attr = attributes.get(attribute_name);
                byte[] bytes = (byte[])attr.get();
                if(bytes!=null)
                return bytes;
            }
        } catch (NamingException e) {
            logger.info(e.getMessage(),e);
            return null;
        }
        return null;
    }
}
