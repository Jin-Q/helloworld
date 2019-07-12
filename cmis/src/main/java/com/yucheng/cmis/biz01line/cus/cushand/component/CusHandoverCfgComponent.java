package com.yucheng.cmis.biz01line.cus.cushand.component;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yucheng.cmis.biz01line.cus.cushand.agent.CusHandoverCfgAgent;
import com.yucheng.cmis.biz01line.cus.cushand.domain.CusHandoverCfg;
import com.yucheng.cmis.biz01line.cus.cushand.domain.CusHandoverDetail;
import com.yucheng.cmis.biz01line.cus.cushand.extInterface.HandoverInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusHandoverCfgComponent extends CMISComponent {
	/**
	 * 根据移交方式和移交范围返回流水号
	 * 
	 * @author HuChunyan
	 * @param 2011-3-2
	 * @throws AgentException
	 */
	public String getCusHandoverCfgSerno(String tableMode, String tableScope) {
		String serno = null;
		try {
			CusHandoverCfgAgent cushandoveragent = (CusHandoverCfgAgent) this
					.getAgentInstance("cushandoveragent");
			serno = cushandoveragent.getSerno(tableMode, tableScope);
		} catch (Exception e) {
		}
		return serno;
	}
	/**
	 * 根据移交方式和移交范围返回主表对象
	 * @param tableMode
	 * @param tableScope
	 * @return
	 */
	public CusHandoverCfg getCusHandoverCfg(String tableMode,String tableScope){
		CusHandoverCfg domain = new CusHandoverCfg();
		try {
			CusHandoverCfgAgent cushandoveragent = (CusHandoverCfgAgent) this
					.getAgentInstance("cushandoveragent");
			domain=cushandoveragent.geCusHandoverCfg(tableMode, tableScope);
		} catch (Exception e) {
		}
		return domain;
	}
	/**
	 * 根据申请的移交方式和移交范围确定配置中的一套移交方案
	 * @param handoverscope 移交范围
	 * @param handovermode  移交方式
	 * @param handoverid 移出人
	 * @param handoversorg 移出机构
	 * @param superviseid 兼交人
	 * @param supervisesorg 兼交机构
	 * @param receiveid 接收人
	 * @param receivesorg 接收机构
	 * @param cusid 客户号
	 * @throws Exception 
	 */
/*	public void middle(String handovermode,String handoverscope,String handoverid,String handoversorg,
			String superviseid,String supervisesorg,String receiveid,String receivesorg,List cusidList) throws Exception{
		Connection conn=this.getConnection();
		String extClass=null;
		String serNo=null;
		CusHandoverCfgAgent cushandoveragent = (CusHandoverCfgAgent) this
		.getAgentInstance("CusHandoverCfg");
		CusHandoverCfg domain=cushandoveragent.geCusHandoverCfg(handovermode,handoverscope);
		extClass=domain.getExtClass(); //得到要执行的扩展类
		Map map = new HashMap<String, Object>();
			
		if(extClass!=null){ //前置动作
			if(extClass.trim().length()!=0){
				HandoverInterface  handoverinterface=(HandoverInterface) Class.forName(extClass).newInstance();
				map=handoverinterface.beforAction(handovermode, handoverscope, handoverid, handoversorg, superviseid, supervisesorg, receiveid, receivesorg, conn);
				}
		}
		map.put("handovermode", handovermode);
		map.put("handoverscope", handoverscope);
		map.put("handoverid", handoverid);
		map.put("handoversorg", handoversorg);
		map.put("superviseid", superviseid);
		map.put("supervisesorg", supervisesorg);
		map.put("receiveid", receiveid);
		map.put("receivesorg", receivesorg);
		
		map.put("openday",this.getOpenDay());
		serNo=domain.getSerno();
		CusHandoverDetailAgent cushandoverdetailagent = (CusHandoverDetailAgent) this
		.getAgentInstance("CusHandoverDetail");
		List<CusHandoverDetail> detaillist=cushandoverdetailagent.getCusHandoverDetailListBySerno(serNo);//根据方案流水号得到要执行的sql语句
	   //根据移交的客户号，循环执行sql
		for(Iterator<String> cusIter=cusidList.iterator();cusIter.hasNext(); ){
			String cusid=cusIter.next();
			map.put("cusid", cusid);
		  for(Iterator<CusHandoverDetail> iter=detaillist.iterator();iter.hasNext();){
			CusHandoverDetail handoverDetail = iter.next();
			String extSql=handoverDetail.getExtSql();
			if(extSql!=null&&extSql!=""){
				executeSQL(extSql,handoverDetail,map,conn);
				
			}
			
		}
		}
		if(extClass!=null){
			if(extClass.trim().length()!=0){
				//后置动作
				HandoverInterface handoverinterface=(HandoverInterface) Class.forName(extClass).newInstance();
				handoverinterface.afterAction(handovermode, handoverscope, handoverid, handoversorg, superviseid, supervisesorg, receiveid, receivesorg, map, conn);
			}
			
		}
	}*/
	public void middle(Map map) throws Exception{
		Connection conn=this.getConnection();
		String extClass=null;
		String serNo=null;
		String handovermode = (String)map.get("handoverMode");//把移交方式移交范围放在map中
		String handoverscope = (String)map.get("handoverScope");
		CusHandoverCfgAgent cushandoveragent = (CusHandoverCfgAgent) this.getAgentInstance(PUBConstant.CUSHANDOVERCFG);
		CusHandoverCfg domain=cushandoveragent.geCusHandoverCfg(handovermode,handoverscope);//根据移交方式和移交范围得到domain对象
		extClass=domain.getExtClass(); //得到要执行的扩展类
		//前置动作		
		Map mapTmp = new HashMap<String, Object>();
		if(extClass!=null){
			if(extClass.trim().length()!=0){
				HandoverInterface handoverinterface=(HandoverInterface) Class.forName(extClass).newInstance();
				mapTmp=handoverinterface.beforAction(map, conn);//
			}
		}
		map.putAll(mapTmp);
		serNo=domain.getSerno();
		
		List<CusHandoverDetail> detaillist=cushandoveragent.getCusHandoverDetailListBySerno(serNo);//根据方案流水号得到要执行的squeal语句
	    //判断移交范围，如果移交范围是单个客户移交，按客户经理所有客户，按客户所属区域  或者 客户合并  这4种的话，则取出客户号
		/**按客户经理所有客户移交 ，不需要循环遍历所有客户信息     2014-05-23  唐顺岩 
		//if(handoverscope.equals("1")||handoverscope.equals("2")||handoverscope.equals("3") || "6".equals(handoverscope)){
		 * */
		if(handoverscope.equals("1")||handoverscope.equals("3") || "6".equals(handoverscope)){
			//得到map中的客户号
			List<String> cusidList=new ArrayList<String>();
			cusidList=(List<String>)map.get("cusidlist");
			   //根据移交的客户号，循环执行sql
			for(Iterator<String> cusIter=cusidList.iterator();cusIter.hasNext(); ){
				String cusid=cusIter.next();
				map.put("cusid", cusid);
				for(Iterator<CusHandoverDetail> iter=detaillist.iterator();iter.hasNext();){
					CusHandoverDetail handoverDetail = iter.next();
					String extSql=handoverDetail.getExtSql();
					if(extSql!=null&&extSql!=""){
						executeSQL(extSql,handoverDetail,map,conn);//根据Map中传的参数修改配置中要update的字段
					}
				}
			}
		/* 按客户经理所有客户移交 ，不需要循环遍历所有客户信息     2014-05-23  唐顺岩   */
		}else if("2".equals(handoverscope)){ 
			for(Iterator<CusHandoverDetail> iter=detaillist.iterator();iter.hasNext();){
				CusHandoverDetail handoverDetail = iter.next();
				String extSql=handoverDetail.getExtSql();
				if(extSql!=null&&extSql!=""){
					executeSQL(extSql,handoverDetail,map,conn);//根据Map中传的参数修改配置中要update的字段
				}
			}
		}else{
			 for(Iterator<CusHandoverDetail> iter=detaillist.iterator();iter.hasNext();){
				CusHandoverDetail handoverDetail = iter.next();
				String extSql=handoverDetail.getExtSql();
				if(extSql!=null&&extSql!=""){
					executeSQL(extSql,handoverDetail,map,conn);
				}
			}
		}
		
		//执行后续方法
		if(extClass!=null){
			if(extClass.trim().length()!=0){
				HandoverInterface handoverinterface=(HandoverInterface) Class.forName(extClass).newInstance();
				handoverinterface.afterAction(map, conn);
			}
		}
	}
	/**
	 * 执行sql的方法
	 * @param sql
	 * @param map
	 * @param conn
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void executeSQL(String sql,CusHandoverDetail handoverDetail, Map map,Connection conn) throws Exception {
        for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            Object value = map.get(key);
            String val="";
            if (value != null&&value!="") {
                if (isBasicDataType(value.getClass().getName())) {
                    val=value.toString();
                }else{
                    val="'"+value.toString()+"'";
                }
                sql=sql.replaceAll(":" + key, val);
            }
        }
        checkSQLInfo(sql,handoverDetail);
        System.err.println(sql);
        Statement stmt=null;
        try{
            stmt=conn.createStatement();
            stmt.executeUpdate(sql);
        }catch (Exception e) {
            throw new Exception("SQL执行异常",e);
        }finally{
            try{
                if(stmt!=null){
                    stmt.close();
                }
            }catch (SQLException e) {
                throw new SQLException("");
            }
        }
    }
	/**
     * 校验sql 变量是否已经全部绑定
     * @param sql
     * @throws ComponentException
     */
    private void checkSQLInfo(String sql,CusHandoverDetail handoverDetail)throws ComponentException{
        // 移除影响分析的字符串
        String sqlContext = sql.replaceAll("([\"][^\"]*[\"])|(['][^']*['])", "");
        Pattern pattern = Pattern.compile(":\\w*");// 匹配变量
        // 不能以数字开头
        Matcher matcher = pattern.matcher(sqlContext);
        List<String> list=new ArrayList<String>();
        while (matcher.find())// 查找符合pattern的变量
        {
            String key = matcher.group();
            if(!list.contains(key)){
                list.add(key);
            }
            
        }
        if(list.size()>=1){
            throw new ComponentException("SQL方案:"+handoverDetail.getMemo()+"中：["+sql+"]--变量:"+list+"未绑定!");
        }
    }

    public boolean isBasicDataType(String Classname) {
        boolean flag = false;
        if (Classname.equals("java.lang.Double") || Classname.equals("java.lang.Integer") || Classname.equals("java.lang.Float") ||  Classname.equals("java.math.BigDecimal")) {
            flag = true;
        }
        return flag;
    }

}
