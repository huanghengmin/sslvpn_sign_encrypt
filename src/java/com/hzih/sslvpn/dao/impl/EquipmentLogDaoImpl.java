package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import cn.collin.commons.utils.DateUtils;
import com.hzih.sslvpn.dao.EquipmentLogDao;
import com.hzih.sslvpn.domain.EquipmentLog;
import com.hzih.sslvpn.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EquipmentLogDaoImpl extends MyDaoSupport implements EquipmentLogDao {

	@Override
	public void setEntityClass() {
		this.entityClass = EquipmentLog.class;
	}

	/**
	 * 分页查询EquipmentLog
	 * 
	 * @param pageIndex
	 * @param pageLength
	 * @param startDate
	 * @param endDate
	 * @param logLevel
	 * @param equipmentName
	 * @return
	 */
	public PageResult listLogsByParams(int pageIndex, int pageLength,
			Date startDate, Date endDate, String logLevel, String equipmentName) {
		StringBuffer sb = new StringBuffer(" from EquipmentLog s where 1=1");
		List params = new ArrayList(4);// 手动指定容量，避免多次扩容
		if(startDate!=null){
			sb.append(" and date_format(logTime,'%Y-%m-%d')>= date_format(?,'%Y-%m-%d')");
			params.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and date_format(logTime,'%Y-%m-%d')<= date_format(?,'%Y-%m-%d')");
			params.add(endDate);
		}

		if (StringUtils.isNotBlank(logLevel)) {
			sb.append(" and level = ?");
			params.add(logLevel);
		}
		if (StringUtils.isNotBlank(equipmentName)) {
			sb.append(" and equipmentName = ?");
			params.add(equipmentName);
		}
        sb.append(" order by id desc");
		String countString = "select count(*) " + sb.toString();
		String queryString = sb.toString();

		PageResult ps = this.findByPage(queryString, countString, params
				.toArray(), pageIndex, pageLength);
		logger.debug(ps == null ? "ps=null" : "ps.results.size:"
				+ ps.getResults().size());
		return ps;
	}

    @Override
    public void truncate() throws Exception {
        String sql = "truncate equipment_log";
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.addEntity(EquipmentLog.class);
        sqlQuery.executeUpdate();
    }

    @Override
    public boolean findByDateAndLogInfo(EquipmentLog equipmentLog) throws Exception{
        String time = DateUtils.format(equipmentLog.getLog_time(), "yyyy-MM-dd");
        String equipmentName = equipmentLog.getEquipment_name();
        String diskMsg = equipmentLog.getLog_info();
        String sql = "select * from equipment_log where equipment_name ='"+equipmentName +
                    "' and date_format(log_time,'%Y-%m-%d') = '"+time+"' and log_info='"+diskMsg+"';";
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        List<EquipmentLog> list = sqlQuery.addEntity(EquipmentLog.class).list();
        if(list.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean add(EquipmentLog equipmentLog) throws Exception {
        this.getHibernateTemplate().save(equipmentLog);
        return true;
    }

    @Override
    public void delete(String startDate, String endDate, String logLevel, String equipmentName) throws Exception {
        String sql = "delete from equipment_log where" ;
        if(StringUtils.isNotBlank(startDate)){
            sql += " 1=1 and date_format(logTime,'%Y-%m-%d')>= '"+startDate+"'";
        }
        if(StringUtils.isNotBlank(endDate)){
            sql += " 1=1 and date_format(logTime,'%Y-%m-%d')<= '"+endDate+"'";
        }

        if (StringUtils.isNotBlank(logLevel)) {
            sql += " 1=1 and level = '"+logLevel+"'";
        }
        if (StringUtils.isNotBlank(equipmentName)) {
            sql += " 1=1 and equipmentName = '"+equipmentName+"'";
        }
        Session session = getSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.addEntity(EquipmentLog.class);
        sqlQuery.executeUpdate();
    }
}
