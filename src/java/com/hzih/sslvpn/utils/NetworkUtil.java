package com.hzih.sslvpn.utils;

import com.hzih.sslvpn.entity.NetInfo;
import org.apache.log4j.Logger;

import java.util.List;

//import org.apache.log4j.Logger;

public class NetworkUtil{ 
	private Logger log = Logger.getLogger(NetworkUtil.class);

    public String checkInterfaceName(String interfaceName){
        String temp = null;
        try{
            GetListNetInfo getListNetInfo = new GetListNetInfo();
            List<NetInfo> netInfos = getListNetInfo.readInterfaces();
            String gateway = null;
            for(NetInfo netInfo:netInfos){
                if(netInfo.getInterfaceName().equalsIgnoreCase(interfaceName)){
                    if(netInfo.getGateway() != null){
                        gateway = netInfo.getGateway();
                    }
                    break;
                }
            }
            if(gateway != null){
                temp = gateway.split("\\.")[0]  + "."+ gateway.split("\\.")[1] +"." +  gateway.split("\\.")[2] ;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return temp ;
    }

    public String readListInterfaceName() throws Exception{
        GetListNetInfo getListNetInfo = new GetListNetInfo();
        List<String> names = getListNetInfo.readInterfaceName();
        List<NetInfo> netInfos = getListNetInfo.readInterfaces();
        for (NetInfo netInfo:netInfos){
            if(names.contains(netInfo.getInterfaceName())){
                names.remove(netInfo.getInterfaceName());
            }
        }
        log.info("test:"+names);
        int total = names.size();
        String json = "{'success':true,'total':"+total+",'rows':[";
        if(total == 0){
            json += ",";
        }else if(total > 0){
            for(String interfaceName:names){
                json +="{value:'"+interfaceName+"',key:'"+interfaceName+"'},";
            }
        }
        json += "]}";
        return json;
    }

    public String readListNetInfo(Integer start , Integer limit) throws Exception {
        GetListNetInfo getListNetInfo = new GetListNetInfo();
        List<NetInfo> netInfos = getListNetInfo.readInterfaces();
        List<String> ifStates = getListNetInfo.readIfState();
        String json = "{'success':true,'total':"+netInfos.size()+",'rows':[";
        if(netInfos.size()==0){
        	json+=",";
        }if(netInfos.size()>0){
        	int index = 0;
        	int j = 0;
            for (NetInfo netInfo : netInfos) {
            	if(j == start && index < limit){
            		start ++;
            		index ++;
            		json += "{'interfaceName':'"+netInfo.getInterfaceName()+"','encap':'"+netInfo.getEncap()+
            		"','displayName':'"+(netInfo.getDisplayName()==null?"":netInfo.getDisplayName())+"','ip':'"+ (netInfo.getIp()==null?"":netInfo.getIp()) +
            		"','mac':'"+ (netInfo.getMac()==null?"":netInfo.getMac()) +"','gateway':'"+(netInfo.getGateway()==null?"":netInfo.getGateway())+
            		"','subnetMask':'"+ (netInfo.getSubnetMask()==null?"":netInfo.getSubnetMask()) +"','dhcpEnable':'"+netInfo.isDhcpEnabled()+
            		"','autoConfigurationEnable':'"+ netInfo.isAutoConfigurationEnabled()+"','dhcp':'"+(netInfo.getDhcp()==null?"":netInfo.getDhcp())+
            		"','dns':'"+(netInfo.getDns_1()==null?"":netInfo.getDns_1()) +","+ (netInfo.getDns_2()==null?"":netInfo.getDns_2())+"','isUp':"+netInfo.getIsUp()+
            		",'broadCast':'"+(netInfo.getBroadCast()==null?"":netInfo.getBroadCast())+"','flag':'"+getFlag(netInfo,ifStates)+"'},";
            	}
            	j++;
            }
         }
        json += "]}";
        return json;
    }

    private String getFlag(NetInfo netInfo, List<String> ifStates) {
		for (String ifState : ifStates) {
			if(ifState.equals(netInfo.getInterfaceName())){
				return netInfo.getEncap()+"_isUp";
			}
		}
		return netInfo.getEncap()+"_isDown";
	}

	public String readListNetInfoName() throws Exception{
        GetListNetInfo getListNetInfo = new GetListNetInfo();
        List<NetInfo> netInfos = getListNetInfo.readInterfaces();
        int total = netInfos.size();
        String json = "{'success':true,'total':"+total+",'rows':[";
        if(total==0){
           json += ",";
        }else if(total>0){
            for(NetInfo netInfo : netInfos){
                json += "{'key':'"+netInfo.getInterfaceName()+"','value':'"+netInfo.getInterfaceName()+"'},";
            }
        }
        json += "]}";
        return json;
    }

	public String deleteInterface(String interfaceName) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
        String msg = getListNetInfo.deleteInterface(interfaceName);
        return msg;
	}


	public String updateInterface(NetInfo netInfo, Boolean isUp,String gatewayOlder) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
        String msg = getListNetInfo.updateInterface(netInfo,isUp,gatewayOlder);
        return msg;
    }


	public String updateDNS(NetInfo netInfo) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
		String msg = getListNetInfo.updateDNS(netInfo);
        return msg;
	}


	public String saveInterface(NetInfo netInfo) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
        String msg = getListNetInfo.saveInterface(netInfo);
        return msg;
	}

	public String ifUp(String interfaceName) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
        String msg = getListNetInfo.ifUp(interfaceName);
		return msg;
	}

	public String ifDown(String interfaceName) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
        String msg = getListNetInfo.ifDown(interfaceName);
		return msg;
	}

	public String readListRouter(Integer start , Integer limit) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
		List<NetInfo> netInfos = getListNetInfo.readRouter();
		int total = netInfos.size();
        String json = "{'success':true,'total':"+total+",'rows':[";
        if(total==0){
           json += ",";
        }else if(total>0){
            int index = 0;
        	int j = 0;
            for(NetInfo netInfo : netInfos){
                if(j == start && index < limit){
            		start ++;
            		index ++;
                    json += "{'interfaceName':'"+netInfo.getInterfaceName()+"','destination':'"+netInfo.getDestination()+
                    "','gateway':'"+netInfo.getGateway()+"','subnetMask':'"+ (netInfo.getSubnetMask()==null?"":netInfo.getSubnetMask()) +"'},";
                }
                j ++;
            }
        }
        json += "]}";
        return json;
	}

	public String deleteRouter(List<NetInfo> netInfos) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
		return getListNetInfo.deleteRouter(netInfos);
	}

	public String saveRouter(NetInfo netInfo) throws Exception{
		GetListNetInfo getListNetInfo = new GetListNetInfo();
		return getListNetInfo.saveRouter(netInfo);
	}

} 