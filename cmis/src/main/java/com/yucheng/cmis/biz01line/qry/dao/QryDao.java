package com.yucheng.cmis.biz01line.qry.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;
import com.yucheng.cmis.biz01line.qry.domain.QryParam;
import com.yucheng.cmis.biz01line.qry.domain.QryResult;
import com.yucheng.cmis.biz01line.qry.domain.QryTemplet;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.pub.util.TranslateDic;

public class QryDao extends CMISDao {
    private static final Logger logger = Logger.getLogger(CMISDao.class);

    public String deleteQryParamByTempNo(String tempNo, Connection conn) throws DaoException {
        // 创建业务代理类
        PreparedStatement ps = null;
        String sql = "delete from qry_param where temp_no =?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            int result = ps.executeUpdate();
            logger.info(result + " rows deleted.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return CMISMessage.SUCCESS;

    }

    public String deleteQryResultByTempNo(String tempNo, Connection conn) throws DaoException {
        // 创建业务代理类
        PreparedStatement ps = null;
        String sql = "delete from qry_result where temp_no =?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            int result = ps.executeUpdate();
            logger.info(result + " rows deleted.");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return CMISMessage.SUCCESS;

    }

    /**
     * 返回QryParamList信息
     * 
     * @param tempNo
     * @param conn
     * @return
     * @throws DaoException
     */
    public ArrayList getQryParamList(String tempNo, Connection conn) throws DaoException {

        ArrayList qryParamList = new ArrayList();
        PreparedStatement ps = null;
        String sql = "select a.TEMP_NO, a.PARAM_NO,a.ENNAME,a.CNNAME,a.PARAM_TYPE,a.PARAM_DIC_NO,a.ORDERID,b.OPTTYPE,b.QUERY_SQL,b.POPNAME from qry_param a left outer join qry_param_dic b on (a.param_dic_no = b.param_dic_no) where a.temp_no=? order by to_number(a.orderid)";
        ResultSet rs = null;
        QryParam qryParam = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                qryParam = new QryParam();
                String temp_no = rs.getString("TEMP_NO");
                qryParam.setTempNo(temp_no!=null?temp_no.toLowerCase():"");
                String param_no = rs.getString("PARAM_NO");
                qryParam.setParamNo(param_no!=null?param_no.toLowerCase():"");
                String cnname = rs.getString("CNNAME");
                qryParam.setCnname(cnname!=null?cnname.toLowerCase():"");
                String enname = rs.getString("ENNAME");
                qryParam.setEnname(enname!=null?enname.toLowerCase():"");
                String param_type = rs.getString("PARAM_TYPE");
                qryParam.setParamType(param_type!=null?param_type.toLowerCase():"");
                String param_dic_no = rs.getString("PARAM_DIC_NO");
                qryParam.setParamDicNo(param_dic_no!=null?param_dic_no.toLowerCase():"");
                String orderid = rs.getString("ORDERID");
                qryParam.setOrderid(orderid!=null?orderid.toLowerCase():"");
                String optype = rs.getString("OPTTYPE");
                qryParam.setOpttype(optype!=null?optype:"");
                String query_sql = rs.getString("QUERY_SQL");
                qryParam.setQuerySql(query_sql!=null?query_sql.toLowerCase():"");
                String popname = rs.getString("POPNAME");
                qryParam.setPopname(popname!=null?popname:"");
                qryParamList.add(qryParam);
            }
            logger.info(qryParamList.size() + "QryParams return");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return qryParamList;
    }

    /**
     * 查询查询模块下设置了外部链接的结果字段
     * @param tempNo  查询模板编号
     * @param conn 数据库连接
     * @return
     * @throws DaoException
     */
    public ArrayList getQryResultLinkList(String tempNo, Connection conn) throws DaoException {
        ArrayList qryResultLinkList = new ArrayList();
        PreparedStatement ps = null;
        String sql = "SELECT FROM QRY_RESULT WHERE LINK_TEMP_NO IS NOT NULL ORDER BY ORDERID";
        ResultSet rs = null;
        QryParam qryParam = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                qryParam = new QryParam();
                String temp_no = rs.getString("TEMP_NO");
                qryParam.setTempNo(temp_no!=null?temp_no.toLowerCase():"");
                String param_no = rs.getString("PARAM_NO");
                qryParam.setParamNo(param_no!=null?param_no.toLowerCase():"");
                String cnname = rs.getString("CNNAME");
                qryParam.setCnname(cnname!=null?cnname.toLowerCase():"");
                String enname = rs.getString("ENNAME");
                qryParam.setEnname(enname!=null?enname.toLowerCase():"");
                String param_type = rs.getString("PARAM_TYPE");
                qryParam.setParamType(param_type!=null?param_type.toLowerCase():"");
                String param_dic_no = rs.getString("PARAM_DIC_NO");
                qryParam.setParamDicNo(param_dic_no!=null?param_dic_no.toLowerCase():"");
                String orderid = rs.getString("ORDERID");
                qryParam.setOrderid(orderid!=null?orderid.toLowerCase():"");
                String optype = rs.getString("OPTTYPE");
                qryParam.setOpttype(optype!=null?optype:"");
                String query_sql = rs.getString("QUERY_SQL");
                qryParam.setQuerySql(query_sql!=null?query_sql.toLowerCase():"");
                String popname = rs.getString("POPNAME");
                qryParam.setPopname(popname!=null?popname:"");
                qryResultLinkList.add(qryParam);
            }
            logger.info(qryResultLinkList.size() + "QryResultLink return");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return qryResultLinkList;
    }
    
    
    /**
     * 查询用获取查询结果显示字段方法 返回对象的同时也返回了字典翻译字段和数字格式转换字段信息
     * 
     * @param tempNo
     * @param translate
     * @param translateNum
     * @param connection
     * @return
     * @throws DaoException
     */
    public Collection<HashMap> getDBShowColumns(String tempNo, HashMap<String, String> translate, HashMap translateNum, Connection connection) throws DaoException {
        Collection<HashMap> col = null;
        HashMap<String, String> hm = null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("select ENNAME,CNNAME,NVL((SELECT OPTTYPE FROM QRY_PARAM_DIC WHERE PARAM_DIC_NO=RESULT_TITLE),RESULT_TITLE) RESULT_TITLE,ENNAME2,RESULT_TYPE from QRY_RESULT where temp_no=? order by to_number(ORDERID)");
            ps.clearParameters();
            ps.setString(1, tempNo);

            rs = ps.executeQuery();
            if (rs != null) {
                col = new ArrayList<HashMap>();
                while (rs.next()) {
                    hm = new HashMap<String, String>();
                    hm.put("ENNAME", rs.getString(1).toLowerCase());
                    hm.put("CNNAME", rs.getString(2).toLowerCase());
                    if (rs.getString(3) != null && !"".equals(rs.getString(3).trim())) {
                        translate.put(rs.getString("ENNAME2").toLowerCase(), rs.getString(3).toLowerCase());
                    }
                    translateNum.put(rs.getString("ENNAME2").toLowerCase(), rs.getString("RESULT_TYPE").toLowerCase());
                    col.add(hm);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (Exception e) {
            }
            try {
                if (ps != null)
                    ps.close();
                ps = null;
            } catch (Exception e) {
            }
        }
        return col;
    }

    /**
     * 返回QryResultList信息
     * 
     * @param tempNo
     * @param conn
     * @return
     * @throws DaoException
     */
    public ArrayList getQryResultList(String tempNo, Connection conn) throws DaoException {
        ArrayList qryResultList = new ArrayList();
        PreparedStatement ps = null;
        String sql = "select TEMP_NO,RESULT_NO,CNNAME,ENNAME,ENNAME2,RESULT_TYPE,RESULT_TITLE,ORDERID from qry_result where temp_no =? order by to_number(orderid)";
        ResultSet rs = null;
        QryResult qryResult = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    qryResult = new QryResult();
                    qryResult.setTempNo(rs.getString("TEMP_NO"));
                    qryResult.setResultNo(rs.getString("RESULT_NO"));
                    qryResult.setCnname(rs.getString("CNNAME"));
                    qryResult.setEnname(rs.getString("ENNAME"));
                    qryResult.setEnname2(rs.getString("ENNAME2"));
                    qryResult.setResultType(rs.getString("RESULT_TYPE"));
                    qryResult.setResultTitle(rs.getString("RESULT_TITLE"));
                    EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0,
                    		"=====" + rs.getString("ORDERID"));
                    qryResult.setOrderid(rs.getString("ORDERID"));
                    qryResultList.add(qryResult);
                }
            }
            logger.info(qryResultList.size() + "QryResults return");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return qryResultList;
    }

    /**
     * 返回查询模板信息
     * 
     * @param tempNo
     * @param conn
     * @return
     * @throws DaoException
     */

    public QryTemplet getQryTemplet(String tempNo, Connection conn) throws DaoException {
        PreparedStatement ps = null;
        String sql = "select * from qry_templet where temp_no =?";
        ResultSet rs = null;
        QryTemplet qryTemplet = new QryTemplet();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, tempNo);
            logger.info("SQL: " + sql + " ?=" + tempNo);
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    qryTemplet.setTempNo(rs.getString("TEMP_NO"));
                    qryTemplet.setTempName(rs.getString("TEMP_NAME"));
                    qryTemplet.setOrganlevel(rs.getString("ORGANLEVEL"));
                    qryTemplet.setTempletType(rs.getString("TEMPLET_TYPE"));
                    qryTemplet.setTempPattern(rs.getString("TEMP_PATTERN"));
                    qryTemplet.setClasspath(rs.getString("CLASSPATH"));
                    qryTemplet.setTempEnable(rs.getString("TEMP_ENABLE"));
                    qryTemplet.setQuerySql(rs.getString("QUERY_SQL"));
                    qryTemplet.setJspFileName(rs.getString("JSP_FILE_NAME"));
                    qryTemplet.setOrderId(rs.getString("ORDER_ID"));
                }
            }
            logger.info(qryTemplet.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return qryTemplet;
    }

    /**
     * 通过自定义sql查询翻译字典 sql中select 字段需要用enname,cnname为别名来返回对应的字典字段 因为只取这两个值。
     * 返回值为arrayList中包含 hashMap, hashMap里边分别以enname,cnname为key来保存对应数据。
     * 
     * @param sql
     * @param conn
     * @return
     * @throws DaoException
     */
    public ArrayList getDictList(String sql, Connection conn) throws DaoException {
        ArrayList dictList = new ArrayList();
        PreparedStatement ps = null;
        HashMap dictInfo = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            logger.info("SQL: " + sql);
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    dictInfo = new HashMap();
                    dictInfo.put(CMISDataDicService.ATTR_ENNAME, rs.getString(CMISDataDicService.ATTR_ENNAME));
                    dictInfo.put(CMISDataDicService.ATTR_CNNAME, rs.getString(CMISDataDicService.ATTR_CNNAME));
                    dictList.add(dictInfo);
                }
            }
            logger.info(dictList.size() + "Dicts return");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("字典查询失败" + e.getMessage(), e);
            throw new DaoException(e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
                ps = null;
            }
        }
        return dictList;

    }

    /**
     * 对条件为机构码的处理
     * @param column
     * @param ORGANCODE  登录机构
     * @param value  Pop选择地机构
     * @param oper
     * @param data
     * @param con
     * @return
     * @throws DaoException
     */
    public String checkRight(String column, String ORGANCODE, String value, String oper, Collection<String> data, Connection con) throws DaoException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String code = "";
        String str = "";

        try {
            ps = con.prepareStatement("select organno from s_org where organno = ?");
            ps.clearParameters();

            ps.setString(1, value);
            rs = ps.executeQuery();
            if (rs == null || !rs.next()) {
                throw new DaoException("无此机构『" + value + "』信息");
            }
            code = rs.getString(1); 
            /*
            if (QryPubConstant.SYS_DIC_OPER_10.equals(oper)) {
                //等于某个机构
                    str = " (" + column + "=?) ";
                    data.add(value);                    
            } else if (QryPubConstant.SYS_DIC_OPER_8.equals(oper)) {
                //在某个机构内
                str = " (" + column + " in (select ORGANNO from s_org where locate like ?)) ";
                    data.add("%"+code + "%");
            }*/
            
            //等于某个机构
            if(QryPubConstant.SYS_DIC_OPER_10.equals(oper)) {
            	if(ORGANCODE.substring(4, 6).equals("00") || ORGANCODE.substring(4, 6).equals("01")) {       //等于总行
            		/*if(value.substring(4, 6).equals("00") || value.substring(4, 6).equals("01")) {
            			str = " 1=1 "; 
            		}*/
            		//
            		if(value.equals("4501000000")) {
            			str = " 1=1 "; 
            		}
            		//等于某个机构的汇总行的时候，查询当前支行的所有信息
            		else if(value.substring(6,value.length()).equals("0000")) {
            			str = " (" + column + "='2' or substr(" + column + ",1,6)=substr('" + value + "',1,6))";
            		} else {
	            		str = " (" + column + "=?) ";
	            		data.add(value); 
            		}
            	} 
            	else {         //不等于总行
            		//等于某个机构的汇总行的时候，查询当前支行的所有信息
            		if(value.substring(6,value.length()).equals("0000")) {
            			str = " (" + column + "='2' or substr(" + column + ",1,6)=substr('" + value + "',1,6))";
            		} else {
	            		str = " (" + column + "=?) ";
	            		data.add(value); 
            		}
            	}
            }
            //在某个机构中
            else {
            	if(ORGANCODE.substring(4, 6).equals("00") || ORGANCODE.substring(4, 6).equals("01")) {   // 总行
            		//在总行中，所选机构不是总行 ,即第5位和每六位，不是’00‘和‘01’
            		//if(!ORGANCODE.substring(4, 6).equals("00") && !ORGANCODE.substring(4, 6).equals("01")){
            		/*if(value.substring(4, 6).equals("00") || value.substring(4, 6).equals("01")){
            			str = " 1=1 ";
            		}*/
            		if(value.equals("4501000000")) {
            			str = " 1=1 "; 
            		}
            		else{
            			//在某个机构中的汇总行的时候，查询当前支行的所有信息
            			if(value.substring(6,value.length()).equals("0000")) { 
                			str = " (" + column + "='2' or substr(" + column + ",1,6)=substr('" + value + "',1,6))";
                		}
//            			else if(!ORGANCODE.substring(4,6).equals(value.substring(4,6))){
//                			throw new DaoException("不允许查询此机构[" + value + "]数据");
//                		} 
            			else {
     	            		str = " (" + column + "=?) ";
     	            		data.add(value);
            			}	
//                		else {
//                			//在某个机构中的汇总行的时候，查询当前支行的所有信息
//                    		if(value.substring(6,value.length()).equals("0000")) { 
//                    			str = " (" + column + "=2 or substr(" + column + ",1,6)=substr('" + value + "',1,6))";
//                    		}data.add(value); 
//                    		}
                		
            		} 
            	}
            	else {            //支行
            		if(value.substring(6,value.length()).equals("0000")) {
            			str = " (" + column + "='2' or substr(" + column + ",1,6)=substr('" + ORGANCODE + "',1,6))";
            		} else {
	            		str = " (" + column + "=?) ";
	            		data.add(value);  
            		}
            	}
            		
            }
     
   
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (Exception e) {
            }
            try {
                if (ps != null)
                    ps.close();
                ps = null;
            } catch (Exception e) {
            }
        }
        return str;
    }

    private void setValues(Collection<String> data, PreparedStatement st) throws Exception {
        int i = 0;
        for (Iterator<String> itr = data.iterator(); itr.hasNext();) {
            st.setString(++i, (String) itr.next());
        }
    }

    /**
     * 即将执行语句
     * @param sql
     * @param fileName
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @param dictColl
     * @param con
     * @return
     * @throws DaoException
     */
    public String genFile(String sql, String fileName, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data, KeyedCollection dictColl, Connection con) throws DaoException {
        PreparedStatement st = null;
        ResultSet rs = null;
        int size = max;
        try {
            EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            st = con.prepareStatement(sql);
            setValues(data, st);
            rs = st.executeQuery();
            /* 根据结果集生成文件 */
            size = genFile(fileName, ChineseName, max, rs, translate, translateNum, dictColl);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (Exception e) {
            }
            try {
                if (st != null)
                    st.close();
                st = null;
            } catch (Exception e) {
            }

        }
        return String.valueOf(size);
    }
    
    
    /**
     * 即将执行语句，加重载，传入查询模板编号
     * @param sql
     * @param fileName
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @param dictColl
     * @param con
     * @param tempno 查询模板编号
     * @return
     * @throws DaoException
     */
    public String genFile(String sql, String fileName, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data, KeyedCollection dictColl, Connection con,String tempno) throws DaoException {
        PreparedStatement st = null;
        ResultSet rs = null;
        int size = max;
        try {
            EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            st = con.prepareStatement(sql);
            setValues(data, st);
            rs = st.executeQuery();
            /* 根据结果集生成文件 */
            size = genFile(fileName, ChineseName, max, rs, translate, translateNum, dictColl,tempno);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (Exception e) {
            }
            try {
                if (st != null)
                    st.close();
                st = null;
            } catch (Exception e) {
            }

        }
        return String.valueOf(size);
    }
    
/**
     * 即将执行语句,得到的查询结果
     * @param sql
     * @param fileName
     * @param ChineseName
     * @param max
     * @param translate
     * @param translateNum
     * @param data
     * @param dictColl
     * @param con
     * @return
     * @throws DaoException
     */
    public Collection genResult(String sql, String fileName, String ChineseName, int max, HashMap<String, String> translate, HashMap translateNum, Collection<String> data, KeyedCollection dictColl, Connection con) throws DaoException {
       Collection col = null;
    	PreparedStatement st = null;
        ResultSet rs = null;
        int size = max;
        try {
             EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.INFO, 0, "即将执行语句："+sql, null);
            st = con.prepareStatement(sql);
            setValues(data, st);
            rs = st.executeQuery();
            /* 根据结果集生成文件 */
            col = genResult(ChineseName, max, rs, translate, translateNum, dictColl);
            EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "即将执行语句："+sql, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (Exception e) {
            }
            try {
                if (st != null)
                    st.close();
                st = null;
            } catch (Exception e) {
            }

        }
        return col;
    }

    /**
     * 翻译数据字典
     * @param fileName
     * @param ChineseName
     * @param max
     * @param rs
     * @param translate
     * @param translateNum
     * @param dictColl
     * @return
     * @throws DaoException
     */
    private List genResult(String ChineseName, int max, ResultSet rs, HashMap<String, String> translate, HashMap translateNum, KeyedCollection dictColl) throws DaoException {
        int count = 0;
        Map map = null;
        //Collection iColl = null;
        List col = new ArrayList();
        ResultSetMetaData rm = null;
        TranslateDic trans = null;
        int size = max;
        try {

            rm = rs.getMetaData();
            trans = new TranslateDic();
            String newKeyStr = "";

            /* 此处可以考虑写多个文件，以应对记录数过大时翻页过慢的问题，目前不存在很大压力，暂不考虑 */
            while (rs.next()) {
            	map = new LinkedHashMap();
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                    String keyStr = rs.getString(i);
           /*         if(rm.getColumnDisplaySize(i) > 11 && ("double".equals(rm.getColumnTypeName(i).toLowerCase()) || "number".equals(rm.getColumnTypeName(i).toLowerCase()))) {
                    	BigDecimal bigStr =  rs.getBigDecimal(i);
                    	keyStr = bigStr+"";
                    }
          */
                    String lowerCase = rm.getColumnName(i).toLowerCase();
                    if (keyStr != null && !"".equals(keyStr) && translate.containsKey(lowerCase)) {
                    	newKeyStr = trans.getCnnameByOpttypeAndEnname(dictColl, translate.get(lowerCase).toUpperCase(), new String(keyStr.getBytes(), "UTF-8"));
                        map.put(lowerCase, newKeyStr);
                    } else {
                    	keyStr = this.translateStrToNum(translateNum, rs, rm, i);
                    	if(keyStr ==null ) {
                    		keyStr = "";
                    	}
                        map.put(lowerCase, keyStr);
                    }
                    
                }
                col.add(map);
              /*  if ((++count) == max || count > 50000) {
                    break;
                }*/
               //修改查询限制的最大条数
                if((++count) == max) {
                	break;
                }
               
            }
            
//            for(int j = 0;j < col.size();j++) {
//            	HashMap maps = new HashMap();
//            	maps = (HashMap)col.get(j);
//	            for(Iterator it = maps.keySet().iterator();it.hasNext();) {
//	            	String ss = (String)it.next();
//	            	System.out.print(maps.get(ss));
//	            }
//            }
            if (count < max)
                size = count;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException("生成文件失败!" + e.getMessage());
        }
        return col;
    }

    /**
     * 移除用户搜索记录并把返回记录条数加到文件名中
     * 
     * @param path
     * @param fileName
     * @param size
     * @param file
     */
    private void clearUserFile(String path, String fileName, int size, File file) {
        File nfile = new File(path + fileName + "@" + size);
        if (nfile.exists())
            Validate.isTrue(nfile.delete(),"文件删除失败！");
        Validate.isTrue(file.renameTo(nfile),"文件重名名失败！");
        File files = new File(path);
        for (File iter : files.listFiles()) {
            String name = iter.getName();
            if (name.contains(fileName) && !name.contains(fileName + "@" + size)) {
                Validate.isTrue(iter.delete(),"文件删除失败！");
            }
        }
    }

    /**
     * 去掉 sql中的换行符和竖线
     * 
     * @param str
     * @return
     */
    private String filter(String str) {
        if (str == null) {
            str = "";
        } else {
            str = str.trim();
        }
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\\|", "");
        return str;
    }

    /** 将查询结果转化为对应类型的值 */
    private void translateStrToNum(HashMap translateNum, OutputStream writer, ResultSet rs, ResultSetMetaData rm, int i) throws Exception {
        String col_dic = "";
        try {
        	String cName=rm.getColumnName(i).toLowerCase();
        	
            col_dic = (String) translateNum.get(cName);
            // col_dic: 01-数字型,04-百分比型,05-千分比型
            if ("01".equals(col_dic)) {
                writer.write(filter(this.changeStrToNum(rs.getObject(i))).getBytes("UTF-8"));
            } else if ("04".equals(col_dic)) {
                writer.write(filter(this.changeStrToPercent(rs.getObject(i))).getBytes("UTF-8"));
            } else if ("05".equals(col_dic)) {
                writer.write(filter(this.permillageNum(rs.getObject(i))).getBytes("UTF-8"));
            } else {
            	String value=rs.getString(i);
            	if(value==null ||("").equals(value)){
            		//value=" ";
            		value="&nbsp;";   //为空时将，填入空格，否决样式存在问题   2014-06-12 唐顺岩
            	}else{
            		value=value.replace("\n", "").replace("\r", "");
            	}
                writer.write(filter(value).getBytes("UTF-8"));
            }

        } catch (Exception e) {
            String errMsg = "col_dic:" + col_dic + "\n" + "rm.getColumnName(i).toLowerCase():" + rm.getColumnName(i).toLowerCase() + "\n" + "rs.getString(1):" + rs.getString(i) + "\n" + e.getMessage();

            logger.error(errMsg, e);
            throw new DaoException(errMsg);
        }

    }
    
    
    /** 将查询结果转化为对应类型的值，加重载根据内部查询编号是否为空判断  */
    private void translateStrToNum(HashMap translateNum, OutputStream writer, ResultSet rs, ResultSetMetaData rm, int i,String t_no) throws Exception {
        String col_dic = "";
        try {
        	String cName=rm.getColumnName(i).toLowerCase();
        	
            col_dic = (String) translateNum.get(cName);
            // col_dic: 01-数字型,04-百分比型,05-千分比型
            if ("01".equals(col_dic)) {
                writer.write(filter(this.changeStrToNum(rs.getObject(i))).getBytes("UTF-8"));
            } else if ("04".equals(col_dic)) {
                writer.write(filter(this.changeStrToPercent(rs.getObject(i))).getBytes("UTF-8"));
            } else if ("05".equals(col_dic)) {
                writer.write(filter(this.permillageNum(rs.getObject(i))).getBytes("UTF-8"));
            } else {
            	String value=rs.getString(i);
            	if(value==null ||("").equals(value)){
            		//value=" ";
            		value="&nbsp;";   //为空时将，填入空格，否决样式存在问题   2014-06-12 唐顺岩
            	}else{
            		value=value.replace("\n", "").replace("\r", "")+t_no;  //将内部查询编号拼接到显示值后面
            	}
                writer.write(filter(value).getBytes("UTF-8"));
            }

        } catch (Exception e) {
            String errMsg = "col_dic:" + col_dic + "\n" + "rm.getColumnName(i).toLowerCase():" + rm.getColumnName(i).toLowerCase() + "\n" + "rs.getString(1):" + rs.getString(i) + "\n" + e.getMessage();
            logger.error(errMsg, e);
            throw new DaoException(errMsg);
        }
    }
    
    /** 将查询结果转化为对应类型的值 */
    private String translateStrToNum(HashMap translateNum, ResultSet rs, ResultSetMetaData rm, int i) throws Exception {
        String col_dic = "";
        String str = "";
        try {
            col_dic = (String) translateNum.get(rm.getColumnName(i).toLowerCase());
            // col_dic: 01-数字型,04-百分比型,05-千分比型
            if ("01".equals(col_dic)) {
                str = this.changeStrToNum(rs.getObject(i));
            } else if ("04".equals(col_dic)) {
                str = this.changeStrToPercent(rs.getObject(i));
            } else if ("05".equals(col_dic)) {
                str = this.permillageNum(rs.getObject(i));
                
            } else {
               str = rs.getString(i);
            }
        } catch (Exception e) {
            String errMsg = "col_dic:" + col_dic + "\n" + "rm.getColumnName(i).toLowerCase():" + rm.getColumnName(i).toLowerCase() + "\n" + "rs.getString(1):" + rs.getString(i) + "\n" + e.getMessage();

            logger.error(errMsg, e);
            throw new DaoException(errMsg);
        }
        return str;

    }

    /** 把字符串转换为数字型 */
    private String changeStrToNum(Object value) {
        if (null == value)
            return "";
        DecimalFormat df = new DecimalFormat("#.##");

        try {
            return df.format(value)+"-num";//加个标记，说明是数值。
        } catch (Exception e) {
            return value + "";
        }

    }

    /** 把字符串转换为百分数 */
    private String changeStrToPercent(Object value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (null == value)
            return "";

        try {
            return numberFormat.format(Double.parseDouble(value + "") * 100) + "%";
        } catch (Exception e) {
            return value + "";
        }
    }

    /** 把字符串转换为千分数 */
    private String permillageNum(Object value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        if (null == value)
            return "";

        try {
            return numberFormat.format(Double.parseDouble(value + "") * 1000) + "‰";
        } catch (Exception e) {
            return value + "";
        }

    }
    
    /**
     * 生成临时文件
     * @param fileName
     * @param ChineseName
     * @param max
     * @param rs
     * @param translate
     * @param translateNum
     * @param dictColl
     * @return
     * @throws DaoException
     */
    private int genFile(String fileName, String ChineseName, int max, ResultSet rs, HashMap<String, String> translate, HashMap translateNum, KeyedCollection dictColl) throws DaoException {
        
    	File file = null;
        OutputStream writer = null;
        int count = 0;
        
        ResultSetMetaData rm = null;
        TranslateDic trans = null;
        String path;
        ResourceBundle res = ResourceBundle.getBundle("cmis");
        String dir = "";
        try{
		   dir = res.getString("qry.result.path");  
	   }catch (Exception e) {
		   logger.error("未找到统计查询模块配置路径参数[qry.result.path]，请联系管理员！");
		   throw new DaoException("未找到统计查询模块配置路径参数[qry.result.path]，请联系管理员！");
	   }
	   if("".equals(dir)){
		   logger.error("统计查询模块配置路径参数[qry.result.path]为空，请联系管理员！");
		   throw new DaoException("统计查询模块配置路径参数[qry.result.path]为空，请联系管理员！");
	   }
        URL url = QryGenPageComponent.class.getResource("");
        path = url.getPath();
        path = path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", "");
        int size = max;
        try {

            File folder = new File(path + File.separator + dir);
            folder.mkdirs();                
            path = path + File.separator + dir + File.separator;
            logger.info("临时文件的路径" + path); 
            logger.info("生成文件临时:" + fileName);
            file = new File(path + fileName);
            if (file.exists()) {
                Validate.isTrue(file.delete(),"文件删除失败");                        
            }
            if (!file.createNewFile()) {
                throw new DaoException("创建文件失败!");
            }
            /* 初始化文件生成类 */
            writer = new BufferedOutputStream(new FileOutputStream(file));

            /* 写入文件头 */
            writer.write((ChineseName + "\n").getBytes("UTF-8"));

            rm = rs.getMetaData();
            
            trans = new TranslateDic();

            /* 此处可以考虑写多个文件，以应对记录数过大时翻页过慢的问题，目前不存在很大压力，暂不考虑 */
            while (rs.next()) {
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                    if (rm.getColumnDisplaySize(i) > 11 && ("char".equals(rm.getColumnTypeName(i).toLowerCase()) || "varchar".equals(rm.getColumnTypeName(i).toLowerCase()) || "varchar2".equals(rm.getColumnTypeName(i).toLowerCase()))) {
                        writer.write((byte) 9);// 搞定CSV导出时科学计数法问题
                    }
                    String keyStr = rs.getString(i);
                    String lowerCase = rm.getColumnName(i).toLowerCase();
                    if (keyStr != null && !"".equals(keyStr) && translate.containsKey(lowerCase)) {
                        writer.write(filter(trans.getCnnameByOpttypeAndEnname(dictColl, translate.get(lowerCase).toUpperCase(), new String(keyStr.getBytes(), "UTF-8"))).replaceAll("\\<|\\>|\\|", "").getBytes("UTF-8"));
                    } else {
                        this.translateStrToNum(translateNum, writer, rs, rm, i);
                        
                    }
                    writer.write('|');
                }
                writer.write('\n');
                /*if ((++count) == max || count > 50000) {
                    break;
                }*/
                //修改限制查询最大条数
                if ((++count) == max) {
                    break;
                }
            }
          
            writer.flush();
            if (count < max)
                size = count;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException("生成文件失败!" + e.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
                /**
                 * 清除该用户搜索的文件
                 */
                clearUserFile(path, fileName, size, file);
            } catch (Exception e) {
            }
        }
        return count;
    }
    
    
    /**
     * 生成临时文件，通过查询模板编号查询有配置内部链接字段
     * @param fileName 文件名
     * @param ChineseName 显示中文名称
     * @param max 每页最大记录数
     * @param rs 结果集
     * @param translate 
     * @param translateNum 结果展示字段类型
     * @param dictColl
     * @param tempno 查询模板编号
     * @return
     * @throws DaoException
     * @date 2014-03-29
     * @author tsy
     */
    private int genFile(String fileName, String ChineseName, int max, ResultSet rs, HashMap<String, String> translate, HashMap translateNum, KeyedCollection dictColl,String tempno) throws DaoException {
    	File file = null;
        OutputStream writer = null;
        int count = 0;
        
        ResultSetMetaData rm = null;
        ResultSet rs_link = null;
        TranslateDic trans = null;
        String path;
        ResourceBundle res = ResourceBundle.getBundle("cmis");
        String dir = "";
        try{
		   dir = res.getString("qry.result.path");  
	   }catch (Exception e) {
		   logger.error("未找到统计查询模块配置路径参数[qry.result.path]，请联系管理员！");
		   throw new DaoException("未找到统计查询模块配置路径参数[qry.result.path]，请联系管理员！");
	   }
	   if("".equals(dir)){
		   logger.error("统计查询模块配置路径参数[qry.result.path]为空，请联系管理员！");
		   throw new DaoException("统计查询模块配置路径参数[qry.result.path]为空，请联系管理员！");
	   }
        URL url = QryGenPageComponent.class.getResource("");
        path = url.getPath();
        path = path.replaceAll("/classes/com/yucheng/cmis/biz01line/qry/component/", "");
        int size = max;
        
        try {
        	/*查询模板下是否存在设置内部链接的结果字段，将结果放入ArrayList    2014-03-28 唐顺岩 */
        	ArrayList<Object> link_list = new ArrayList<Object>();
        	String sql = "SELECT ENNAME,LINK_TEMP_NO FROM QRY_RESULT WHERE LINK_TEMP_NO IS NOT NULL AND TEMP_NO= ? ORDER BY ORDERID";
            logger.info("SQL: SELECT ENNAME,LINK_TEMP_NO FROM QRY_RESULT WHERE LINK_TEMP_NO IS NOT NULL AND TEMP_NO= '"+tempno+"' ORDER BY ORDERID");
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, tempno);
            Map<String,String> link_map = new HashMap<String,String> ();
            rs_link = ps.executeQuery();
            if (rs_link != null) {
                while (rs_link.next()) {
                	link_map.put(rs_link.getString("ENNAME"), rs_link.getString("LINK_TEMP_NO"));
                	link_list.add(link_map);
                }
            }
            logger.info("查询模板编号："+tempno+"，设置内部链接的字段个数为："+link_list.size());
            /*END*/
            File folder = new File(path + File.separator + dir);
            folder.mkdirs();                
            path = path + File.separator + dir + File.separator;
            logger.info("临时文件的路径" + path); 
            logger.info("生成临时文件名称:" + fileName);
            file = new File(path + fileName);
            if (file.exists()) {
                Validate.isTrue(file.delete(),"文件"+fileName+"删除失败");                        
            }
            if (!file.createNewFile()) {
                throw new DaoException("创建文件失败!");
            }
            /* 初始化文件生成类 */
            writer = new BufferedOutputStream(new FileOutputStream(file));

            /* 写入文件头 */
            writer.write((ChineseName + "\n").getBytes("UTF-8"));

            rm = rs.getMetaData();
            
            trans = new TranslateDic();

            /* 此处可以考虑写多个文件，以应对记录数过大时翻页过慢的问题，目前不存在很大压力，暂不考虑 */
            while (rs.next()) {
                for (int i = 1; i <= rm.getColumnCount(); i++) {
                	String keyStr = rs.getString(i);
                    String lowerCase = rm.getColumnName(i).toLowerCase();
                    if (rm.getColumnDisplaySize(i) > 11 && ("char".equals(rm.getColumnTypeName(i).toLowerCase()) || "varchar".equals(rm.getColumnTypeName(i).toLowerCase()) || "varchar2".equals(rm.getColumnTypeName(i).toLowerCase()))) {
                        //writer.write((byte) 9);// 搞定CSV导出时科学计数法问题
                    }
                    
                	 //判断内部链接字段   2014-03-28
                    String link_temp_no = ""; 
                	for (Iterator iterator = link_list.iterator(); iterator.hasNext();) {
                		Map<String,String> array_element = (Map<String,String>) iterator.next();
							
						Set set = array_element.keySet();  //得到MAP键列表
						for (Iterator iterator_key = set.iterator(); iterator_key.hasNext();) {
							String enname = (String) iterator_key.next();
							if(lowerCase.equalsIgnoreCase(enname)){   //如果结果字段与设置内部链接字段一致
								link_temp_no = array_element.get(enname);  //内部链接查询编号
								break;
							}							
						}
                	}
                	String t_no = "";
                	if(!"".equals(link_temp_no)){ //内部链接查询编号不为空
                		t_no = "&@&"+link_temp_no; //分隔符+内部链接查询编号
                	}
                	if (keyStr != null && !"".equals(keyStr) && translate.containsKey(lowerCase)) {
                        writer.write(filter(trans.getCnnameByOpttypeAndEnname(dictColl, translate.get(lowerCase).toUpperCase(), new String(keyStr.getBytes(), "UTF-8"))+t_no).replaceAll("\\<|\\>|\\|", "").getBytes("UTF-8"));
                    } else {
                        this.translateStrToNum(translateNum, writer, rs, rm, i,t_no);
                        
                    }
                    writer.write('|');
                }
                writer.write('\n');
                /*if ((++count) == max || count > 50000) {
                    break;
                }*/
                //修改限制查询最大条数
                if ((++count) == max) {
                    break;
                }
            }
          
            writer.flush();
            if (count < max)
                size = count;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException("生成文件失败!" + e.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if(null!=rs_link){  
                	rs_link.close();
                }
                /**
                 * 清除该用户搜索的文件
                 */
                clearUserFile(path, fileName, size, file);
            } catch (Exception e) {
            }
        }
        return count;
    }

    /**
     * 判断查询模板下查询条件的数量
     * @param tempno 查询模板编号
     * @return
     */
    public String selectParamCountByTempno(String tempno)throws DaoException {
    	ResultSet rs_link = null;
    	String reurn_value = null;
    	try{
	    	String sql = "SELECT COUNT(1) FROM QRY_PARAM WHERE TEMP_NO= ?";
	        logger.info("查询查询分析对应参数个数SQL: SELECT COUNT(1) FROM QRY_PARAM WHERE TEMP_NO= '"+tempno+"'");
	        PreparedStatement ps = this.getConnection().prepareStatement(sql);
	        ps.setString(1, tempno);
	        rs_link = ps.executeQuery();
	        if (rs_link != null) {
	            while (rs_link.next()) {
	            	reurn_value = rs_link.getString(1);
	            }
	        }
	        logger.info("查询模板编号："+tempno+"，查询条件字段个数为："+reurn_value);
    	}catch(Exception e){
    		throw new DaoException("根据查询模板查询该模板下查询参数个数错误，错误描述："+e.getMessage());
    	}finally{
    		if(null != rs_link){
    			try {
					rs_link.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    	return reurn_value;
    }
}
