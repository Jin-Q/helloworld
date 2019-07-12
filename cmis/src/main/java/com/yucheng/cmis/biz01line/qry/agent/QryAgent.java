package com.yucheng.cmis.biz01line.qry.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.qry.dao.QryDao;
import com.yucheng.cmis.biz01line.qry.domain.QryParam;
import com.yucheng.cmis.biz01line.qry.domain.QryTemplet;
import com.yucheng.cmis.biz01line.qry.qryinterface.qryInterface;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.exception.OrganizationException;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISConstant;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.pub.exception.AgentException;

public class QryAgent extends CMISAgent {
    private static final Logger logger = Logger.getLogger(CMISAgent.class);
    private static final char CIRCUMSCRIPTION = '|';
    private static final char FLAG = ':';

    /**
     * 删除某查询模板下的所有查询参数信息
     * 
     * @param tempNo
     * @return
     * @throws AgentException
     */
    public String deleteQryParamByTempNo(String tempNo) throws AgentException {
        // 创建业务代理类
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryDao.deleteQryParamByTempNo(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
        return CMISMessage.SUCCESS;
    }

    /**
     * 删除某查询模板下的所有查询返回值信息
     * 
     * @param tempNo
     * @return
     * @throws AgentException
     */
    public String deleteQryResultByTempNo(String tempNo) throws AgentException {
        // 创建业务代理类
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryDao.deleteQryResultByTempNo(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
        return CMISMessage.SUCCESS;
    }

    /**
     * 删除某查询模板下的所有查询参数和返回值信息
     * 
     * @param tempNo
     * @return
     * @throws AgentException
     */
    public String deleteQryStaffByTempNo(String tempNo) throws AgentException {
        // 创建业务代理类
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryDao.deleteQryResultByTempNo(tempNo, connection);
            qryDao.deleteQryParamByTempNo(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
        return CMISMessage.SUCCESS;
    }

    /**
     * 获取查询参数列表
     * 
     * @param tempNo
     * @return
     * @throws AgentException
     */
    public ArrayList getQryParamList(String tempNo) throws AgentException {
        ArrayList qryParamList = null;
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryParamList = qryDao.getQryParamList(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }

        return qryParamList;
    }

    public Collection<HashMap> getDBShowColumns(String tempNo, HashMap<String, String> translate, HashMap translateNum) throws AgentException {
        Collection qryShowColumns = null;
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryShowColumns = qryDao.getDBShowColumns(tempNo, translate, translateNum, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }

        return qryShowColumns;

    }

    /**
     * 获取查询结果列表
     * 
     * @param tempNo
     * @return
     * @throws AgentException
     */
    public ArrayList getQryResultList(String tempNo) throws AgentException {
        ArrayList qryResultList = null;
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryResultList = qryDao.getQryResultList(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }

        return qryResultList;
    }

    public QryTemplet getQryTemplet(String tempNo) throws AgentException {
        QryTemplet qryTemplet = null;
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            qryTemplet = qryDao.getQryTemplet(tempNo, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
        return qryTemplet;
    }

    /**
     * 通过自定义sql查询翻译字典 sql中select 字段需要用enname,cnname为别名来返回对应的字典字段 因为只取这两个值。
     * 返回值为arrayList中包含 hashMap, hashMap里边分别以enname,cnname为key来保存对应数据。
     * 
     * @param sql
     * @return
     * @throws AgentException
     */
    public ArrayList getDictList(String sql) throws AgentException {
        ArrayList dictList = null;
        QryDao qryDao = null;
        Connection connection = null;
        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            dictList = qryDao.getDictList(sql, connection);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
        return dictList;
    }

    /**
     * 本方法将通过opttype 从context中取出对应名称的字典 字典opttype是存放在context的同名
     * icoll,内部保存有kcoll对象,分别存有enname,cnname等信息 在此,将把icoll中的kcoll
     * 变成arrayList中的hashMap对象返回,保存的数据不变.
     * 
     * @param opttype
     * @return
     * @throws AgentException
     */
    public ArrayList getDictListFromContext(String opttype) throws AgentException {
        Context context = this.getContext();
        ArrayList dictList = new ArrayList();
        HashMap dictMap = null;
        try {
            KeyedCollection dictColl = (KeyedCollection) context.getDataElement("dictColl");
            IndexedCollection dictIcoll = (IndexedCollection) dictColl.getDataElement(opttype.toUpperCase().trim());
            for (int i = 0; i < dictIcoll.size(); i++) {
                KeyedCollection dictKcoll = (KeyedCollection) dictIcoll.get(i);
                dictMap = new HashMap();
                dictMap.put("enname", dictKcoll.get("enname"));
                dictMap.put("cnname", dictKcoll.get("cnname"));
                dictList.add(dictMap);
            }
        } catch (ObjectNotFoundException e) {
            String errMsg = "通过OPTTYPE[" + opttype + "]找不到对应的字典项" + e.getMessage();
            logger.error(errMsg, e);
            throw new AgentException(errMsg);
        } catch (InvalidArgumentException e) {
            logger.error(e.getMessage(), e);
        }
        return dictList;
    }

    /**
     * 通过查询出来的dbConditionCol 查询条件信息,寻找前台传来的查询条件具体数值 并打包入conditionMap
     * 以及传来的条件操作信息(大于,小于,等于,在...之间)存入operationMap返回.
     * 
     * @param conditionMap
     * @param operationMap
     * @param dbConditionCol
     * @throws AgentException
     */
    public void packData2Map(HashMap<String, String> conditionMap, HashMap<String, String> operationMap, Collection dbConditionCol, int max) throws AgentException {
        Context context;
        try {
            context = this.getContext();
            for (Iterator itr = dbConditionCol.iterator(); itr.hasNext();) {
                QryParam qryParam = (QryParam) itr.next();
                String PAGE_ID = qryParam.getEnname();
                PAGE_ID = PAGE_ID.toLowerCase();
                if ("".equals(((String) context.getDataValue(PAGE_ID + "_VALUE")).trim())) {
                    continue;
                }
                conditionMap.put(PAGE_ID + "_VALUE", (String) context.getDataValue(PAGE_ID + "_VALUE"));
                try {
                    conditionMap.put(PAGE_ID + "_VALUE2", (String) context.getDataValue(PAGE_ID + "_VALUE2"));
                } catch (Exception e) {
                }
                operationMap.put(PAGE_ID + "_OPER", (String) context.getDataValue(PAGE_ID + "_OPER"));

                /* 添加搜索条数限制,需前台配置支持 */
                operationMap.put("analyse_max_OPER", QryPubConstant.SYS_DIC_OPER_5);
                conditionMap.put("analyse_max_VALUE", max + "");

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException("组装前台页面数据错误,可能的引起原因是开发人员未配置某个条件项");
        }
    }

    /**
     * 对于“扩展查询”类型的查询调用本方法 本方法调用扩展类来整理数据,传入conditionMap,operationMap 执行扩展类插入临时表
     * 此方法仅支持Oracle数据库 调用扩展类,由于需要使用context所以放在此层
     * 
     * @param conditionMap
     * @param operationMap
     * @param ExtendClass
     * @param context
     * @param con
     * @throws AgentException
     */
    public void invokeExtendClass(HashMap<String, String> conditionMap, HashMap<String, String> operationMap, String ExtendClass) throws AgentException {
        Context context;
        Connection con;
        qryInterface analyse = null;
        try {
            context = this.getContext();
            con = this.getConnection();
            analyse = (qryInterface) Class.forName(ExtendClass).newInstance();
            analyse.analyseData(conditionMap, operationMap, context, con);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
    }

    /**
     * 通过使用查询条件conditionMap中的数据和operationMap中的数据比较方式(大于，等于，小于 等等) 组合成完整的Sql语句返回。
     * 
     * @param conditionMap
     * @param operationMap
     * @param data
     * @param sql
     * @param con
     * @return
     * @throws AgentException
     */
    public String analyseSql(HashMap<String, String> conditionMap, HashMap<String, String> operationMap, Collection<String> data, String sql) throws AgentException {

        String ORGANCODE;
        Connection connection;
        Context context;
        String str_before = "";
        String str_after = "";
        String str_middle = "";
        String str_column = "";

        int before_idx = -1;
        int flag_idx = -1;
        int after_idx = -1;
        QryDao qryDao;

        try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            connection = this.getConnection();
            // dictList=qryDao.getDictList(sql, connection);
            context = this.getContext();

            ORGANCODE = (String) context.getDataValue("organNo");

            while ((flag_idx = sql.indexOf(FLAG)) != -1) {
                after_idx = sql.indexOf(CIRCUMSCRIPTION, flag_idx);
                /* 增加对SQL中||操作符的支持before_idx = sql.indexOf(CIRCUMSCRIPTION); */
                before_idx = sql.lastIndexOf(CIRCUMSCRIPTION, flag_idx);

                if (after_idx == -1 || before_idx == -1 || after_idx == before_idx) {
                    throw new AgentException("缺少分界配置符『|』");
                }
                str_before = sql.substring(0, before_idx);
                str_column = sql.substring(before_idx + 1, flag_idx);
                str_middle = sql.substring(flag_idx + 1, after_idx).trim();
                str_after = sql.substring(after_idx + 1);
                
                String temp_param = sql.substring(before_idx+1,after_idx);  //截取到各个参数部分，用于判断是否包含系统内置参数

                //替换非标准的字段名 add by ocean20100725
                //主要针对where子句中还是用了select等嵌套的sql语句，目前只对一个后括号处理，没有对多个后括号处理
                //如：select * from table a where ... and  |a.col in (select  b.col from b where a.id=b.id and b.col2 :col2)| ...
                //其中  b.col2 :col2) 可能有多个括号，目前只处理一个括号的情况
                
              //判断是否是系统内置变量  2014-04-01  唐顺岩
                if(temp_param.indexOf("$")<0) {  
	                String str_middle_tmp = str_middle;
	                str_middle=str_middle.replace(")", "");
	                
	                String oper = (String) operationMap.get(str_middle + "_OPER");
	                String value = (String) conditionMap.get(str_middle + "_VALUE");
	                String value2 = (String) conditionMap.get(str_middle + "_VALUE2");
	
	                if ("".equals(str_middle)) {
	                    throw new AgentException("SQL语句中配置的条件字段不能为空");
	                } else if (value == null || "".equals(value)) {
	                    sql = str_before + " (1=1) " + str_after;
	                    continue;
	                }else {
	                	//替换非标准的字段名 add by ocean20100725
	                    if ( str_middle_tmp.indexOf(")")>-1 ){
	                    	str_after = ")" + str_after;
	                    }
	                }
	                /*
	                 * 由于机构码的字段名称已经不再一致，所以，去掉此判断，需要查询子机构的时候直接根据oper来进行判断 else if(
	                 * "jgm".equals(str_middle) ){ sql = str_before +
	                 * checkRight(str_column, ORGANCODE, value, oper, data, con) +
	                 * str_after; continue; }
	                 */
	
	                if (QryPubConstant.SYS_DIC_OPER_1.equals(oper)) {
	                    sql = str_before + " (" + str_column + "=?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_2.equals(oper)) {
	                    sql = str_before + " (" + str_column + ">?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_3.equals(oper)) {
	                    sql = str_before + " (" + str_column + "<?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_4.equals(oper)) {
	                    sql = str_before + " (" + str_column + ">=?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_5.equals(oper)) {
	                    sql = str_before + " (" + str_column + "<=?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_6.equals(oper)) {
	                    if (value2 == null || "".equals(value2)) {
	                        sql = str_before + " (1=1) " + str_after;
	                        continue;
	                    }
	                    sql = str_before + " (" + str_column + " between ? and ? ) " + str_after;
	                    data.add(value);
	                    data.add(value2);
	                } else if (QryPubConstant.SYS_DIC_OPER_7.equals(oper)) {
	                    sql = str_before + " (" + str_column + " like ?) " + str_after;
	                    if(str_column.equalsIgnoreCase("loan_direction")) {
	                    	data.add(value + "%");
	                    } else {
	                    	data.add("%" + value + "%");
	                    }
	                } else if (QryPubConstant.SYS_DIC_OPER_8.equals(oper)) {
	                    // 对于机构的pop 可以设置查询下属机构的方法，由checkRight来产生返回的sql语句
	                    sql = str_before + qryDao.checkRight(str_column, ORGANCODE, value, oper, data, connection) + str_after;
	                } else if (QryPubConstant.SYS_DIC_OPER_9.equals(oper)) {
	                    sql = str_before + " (" + str_column + " != ?) " + str_after;
	                    data.add(value);
	                } else if (QryPubConstant.SYS_DIC_OPER_10.equals(oper)) {
	                    sql = str_before + qryDao.checkRight(str_column, ORGANCODE, value, oper, data, connection) + str_after;
	                } else {
	                    throw new AgentException("传入错误的OPER类型『" + oper + "』");
	                }
                }else{  //系统内置变量时，直接用系统参数替换    2014-04-01  唐顺岩
                	String _condiStr = temp_param;
            		int beg = -1;
            		int end = -1;
            		while ((_condiStr != null) && (_condiStr.lastIndexOf("$") > end)) {
            			beg = _condiStr.indexOf("$", end + 1);
            			end = _condiStr.indexOf("$", beg + 1);
            			String var = _condiStr.substring(beg + 1, end);

            			String varValue =getTempValue(context, connection, var); //从context中取出内置参数值后加''
            			_condiStr = _condiStr.replaceAll("\\$" + var + "\\$", varValue)+")"; //将内置参数变量替换成系统内置值
            			_condiStr = _condiStr.replace(":", " in (").replaceAll("\\|", ""); //将参数设置的：值替换成 IN 语句
            		}
            		sql = str_before + _condiStr + str_after;
                }
                logger.error("query sql ====" + sql + "================================");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }

        return sql;
    }
    
    /*
	 * 替换系统内置变量
	 */
	public String getTempValue(Context context, Connection connection, String temp) throws EMPException {
		if ((temp == null) || (temp.trim().equals(""))){
			return "";
		}
		String tempValue = "";
		if (context.containsKey(temp)){
			tempValue = (String) context.getDataValue(temp);
		}
		if (temp.equals("S_orgchilds")) {// 本机构以及下级机构的字符串 在加载机构时产生
			String s_organno = (String) context.getDataValue("organNo");
			// 调用组织机构管理模块对外提供的服务：取得机构向下的所有子机构
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface orgService = null;
			try {
				orgService = (OrganizationServiceInterface) serviceJndi.getModualServiceById("organizationServices",CMISConstant.ORGANIZATION_MODUAL_ID);
			} catch (Exception e) {
				EMPLog.log(CMISConstance.CMIS_PERMISSION, EMPLog.ERROR, 0, "getTempValue error!", e);
				throw new EMPException(e);
			}
			try {
				List<SOrg> sorgList = orgService.getAllSubOrgs(s_organno, connection);
				for(int i=0;i<sorgList.size();i++){
					SOrg sorg = sorgList.get(i);
					tempValue +=  "'"+sorg.getOrganno()+"',";
				}
				tempValue = tempValue.substring(0,tempValue.length()-1);
			} catch (OrganizationException e) {
				e.printStackTrace();
			}
		}
		return tempValue;
	}
    
    /**
     * 生成查询结果文件。
     * 
     * @param sql
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @return
     * @throws AgentException
     */
    public String genFile(String sql, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data) throws AgentException {
        Context context;
        Connection con;
        QryDao qryDao;
        KeyedCollection dictColl;
        try {
            EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            context = this.getContext();
            String fileName = context.getDataValue("currentUserId") + "_" + context.getDataValue("EMP_SID");
            con = this.getConnection();
            dictColl = (KeyedCollection) context.getDataElement("dictColl");
            String size = qryDao.genFile(sql, fileName, ChineseName, max, translate, translateNum, data, dictColl, con);
            return fileName + "@" + size;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
    }
    
    
    /**
     * 生成查询结果文件，加重载 多传入查询模板编号。
     * @param sql 查询结果SQL
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @param tempno 查询模板编号
     * @return 
     * @throws AgentException
     */
    public String genFile(String sql, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data, String tempno) throws AgentException {
        Context context;
        Connection con;
        QryDao qryDao;
        KeyedCollection dictColl;
        try {
            EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            context = this.getContext();
            String fileName = context.getDataValue("currentUserId") + "_" + context.getDataValue("EMP_SID");
            con = this.getConnection();
            dictColl = (KeyedCollection) context.getDataElement("dictColl");
            String size = qryDao.genFile(sql, fileName, ChineseName, max, translate, translateNum, data, dictColl, con, tempno);
            return fileName + "@" + size;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
    }
    
  /**
     * 生成查询结果文件。
     * 
     * @param sql
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @return
     * @throws AgentException
     */
    public Collection genResult(String sql, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data) throws AgentException {
        Context context;
        Connection con;
        Collection col = null;
        QryDao qryDao;
        KeyedCollection dictColl;
        try {
        	EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            context = this.getContext();
            String fileName = context.getDataValue("currentUserId") + "_" + context.getDataValue("EMP_SID");
            con = this.getConnection();
            dictColl = (KeyedCollection) context.getDataElement("dictColl");
            col = qryDao.genResult(sql, fileName, ChineseName, max, translate, translateNum, data, dictColl, con);
            return col;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
    }

    
    /**
     * 判断查询模板下查询条件的数量
     * @param tempno 查询模板编号
     * @return
     */
    public String selectParamCountByTempno(String tempno)throws AgentException {
    	QryDao qryDao = null;
    	try {
            qryDao = (QryDao) this.getDaoInstance(QryPubConstant.QRYDAO);
            return qryDao.selectParamCountByTempno(tempno);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AgentException(e);
        }
    }
}
