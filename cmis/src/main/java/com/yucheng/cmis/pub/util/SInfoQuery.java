package com.yucheng.cmis.pub.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.domains.SRowright;
import com.yucheng.cmis.platform.organization.domains.SUser;

public class SInfoQuery {

	public SInfoQuery() {

	}

	/**
	 * 读取数据库中的机构信息
	 * 
	 * @param context
	 * @param conn
	 * @return
	 */
	public Map<String, SOrg> getSOrg(Context context, Connection conn){

		Map<String, SOrg> sOrgMap = new HashMap<String, SOrg>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据START..." );
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			sb.append("ORGANNO,");
			sb.append("SUPORGANNO," );
			sb.append("LOCATE," );
			sb.append("ORGANNAME," );
			sb.append("ORGANSHORTFORM," );
			sb.append("ENNAME,");
			sb.append("ORDERNO," );
			sb.append("DISTNO," );
			sb.append("LAUNCHDATE," );
			sb.append("ORGANLEVEL,");
			sb.append("FINCODE," );
			sb.append("STATE," );
			sb.append("ORGANCHIEF," );
			sb.append("TELNUM," );
			sb.append("ADDRESS," );
			sb.append("POSTCODE," );
			sb.append("CONTROL," );
			sb.append("ARTI_ORGANNO," );
			sb.append("DISTNAME ");
			sb.append(" from  S_ORG ");
			String sql=sb.toString();
			
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"为什么加载不了ITEM?");
			while(rs.next()){
				SOrg sOrg = new SOrg();
				sOrg.setOrganno(rs.getString("ORGANNO"));
				sOrg.setSuporganno(rs.getString("SUPORGANNO"));
				sOrg.setLocate(rs.getString("LOCATE"));
				sOrg.setOrganname(rs.getString("ORGANNAME"));
				sOrg.setOrganshortform(rs.getString("ORGANSHORTFORM"));
				sOrg.setEnname(rs.getString("ENNAME"));
				sOrg.setOrderno(rs.getInt("ORDERNO"));
				sOrg.setDistname(rs.getString("DISTNAME"));
				sOrg.setLaunchdate(rs.getString("LAUNCHDATE"));
				sOrg.setOrganlevel(rs.getInt("ORGANLEVEL"));
				sOrg.setFincode(rs.getString("FINCODE"));
				sOrg.setState(rs.getInt("STATE"));
				sOrg.setOrganchief(rs.getString("ORGANCHIEF"));
				sOrg.setTelnum(rs.getString("TELNUM"));
				sOrg.setAddress(rs.getString("ADDRESS"));
				sOrg.setPostcode(rs.getString("POSTCODE"));
				sOrg.setControl(rs.getString("CONTROL"));
				sOrg.setArtiOrganno(rs.getString("ARTI_ORGANNO"));
				sOrg.setDistno(rs.getString("DISTNO"));
				//sOrg.setOrganchilds("'"+sOrg.getOrganno()+"'");
				
				//增加机构所有下属机构串'','','' 其中s_org表中的orderno必须按照上级机构-下级机构的顺序排序，保证先加载了上级机构再加载下级机构
				String suporOrganno=rs.getString("SUPORGANNO");
				if(suporOrganno!=null&&!suporOrganno.trim().equals("")){
					SOrg sSuporOrg=sOrgMap.get(suporOrganno);
					if(sSuporOrg!=null){
						/*if(sSuporOrg.getOrganchilds()==null||sSuporOrg.getOrganchilds().trim().equals(""))
					      sSuporOrg.setOrganchilds("'"+rs.getString("ORGANNO")+"'");
						else
						  sSuporOrg.setOrganchilds(sSuporOrg.getOrganchilds()+",'"+rs.getString("ORGANNO")+"'");*/
						sOrgMap.put(sSuporOrg.getOrganno(), sSuporOrg);	
					}
					
				}
			/*	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
				
*/				
				sOrgMap.put(rs.getString("ORGANNO"), sOrg);
			}
		
		
			/*Set<Entry<String, SOrg>> set = sOrgMap.entrySet(); 
			Iterator<Entry<String, SOrg>> itor = set.iterator(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************item map *****************");
			while(itor.hasNext()) 
			{ 
			Entry<String, SOrg> entry = itor.next(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getOrganno()+"---"+(entry.getValue()).getOrganname()); 
			} */
		
			ps.close();
			rs.close();
			//conn.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据失败SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据失败！" );
			e.printStackTrace();

	}
		return sOrgMap;
	}

	/**
	 * 读取数据库中的机构信息
	 * 
	 * @param context
	 * @param conn
	 * @return
	 */
	public Map<String, SUser> getSUser(Context context, Connection conn){

		Map<String, SUser> sUerMap = new HashMap<String, SUser>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载用户数据START..." );
			
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			sb.append("ACTORNO,");
			sb.append("ACTORNAME,");
			sb.append("NICKNAME,");
			sb.append("STATE,");
			sb.append("PASSWORD,");
			sb.append("STARTDATE,");
			sb.append("PASSWVALDA,");
			sb.append("FIREDATE,");
			sb.append("BIRTHDAY,");
			sb.append("TELNUM,");
			sb.append("IDCARDNO,");
			sb.append("ALLOWOPERSYS,");
			sb.append("LASTLOGDAT,");
			sb.append("CREATER,");
			sb.append("CREATTIME,");
			sb.append("USERMAIL,");
			sb.append("WRONGPINNUM,");
			sb.append("ISADMIN,");
			sb.append("MEMO,");
			sb.append("IPMASK,");
			sb.append("ORDERNO,");
			sb.append("QUESTION,");
			sb.append("ANSWER,");
			sb.append("ORGID, ");
			sb.append("ACTORRIGHT ");
			sb.append(" from  S_USER "); 		

			String sql=sb.toString();
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			//EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"为什么加载不了ITEM?");
			while(rs.next()){
				SUser sUser = new SUser();
				
				sUser.setActorname(rs.getString("ACTORNAME"));
				sUser.setActorno(rs.getString("actorno"));
				sUser.setAllowopersys(rs.getString("allowopersys"));
				sUser.setAnswer(rs.getString("answer"));
				sUser.setBirthday(rs.getString("birthday"));
				sUser.setBirthday(rs.getString("birthday"));
				sUser.setCreater(rs.getString("creater"));
				sUser.setCreattime(rs.getString("creattime"));
				sUser.setFiredate(rs.getString("firedate"));
				sUser.setIdcardno(rs.getString("idcardno"));
				sUser.setIpmask(rs.getString("ipmask"));
				sUser.setIsadmin(rs.getString("isadmin"));
				sUser.setLastlogdat(rs.getString("lastlogdat"));
				sUser.setMemo(rs.getString("memo"));
				sUser.setNickname(rs.getString("nickname"));
				//sUser.setOrderno(rs.getString("orderno"));
				sUser.setState(rs.getString("state"));
				sUser.setTelnum(rs.getString("telnum"));
				sUser.setUsermail(rs.getString("usermail"));
				//sUser.setActorright(rs.getString("actorright"));
				
			/*	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"FNC_CONF_TYP:"+rs.getString("FNC_CONF_TYP"));
				
*/				
				sUerMap.put(rs.getString("ACTORNO"), sUser);
			}
		
		
			/*Set<Entry<String, SUser>> set = sUerMap.entrySet(); 
			Iterator<Entry<String, SUser>> itor = set.iterator(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"*************item map *****************");
			while(itor.hasNext()) 
			{ 
			Entry<String, SUser> entry = itor.next(); 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,entry.getKey()+"**"+(entry.getValue()).getActorname()+"---"+(entry.getValue()).getActorno()); 
			} */
		
			ps.close();
			rs.close();
			//conn.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载用户END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载用户SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据失败！" );
			e.printStackTrace();

	}
		return sUerMap;
	}
	
	/**
	 * 读取数据库中的机构信息
	 * 
	 * @param context
	 * @param conn
	 * @return
	 */
	public  Map<String, SRowright> initSrowright(Context context, Connection conn){

		Map<String, SRowright> sRowrightMap = new HashMap<String, SRowright>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载资源定义数据START..." );
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			sb.append(" PK1 ,RESOURCEID,CNNAME ,READTEMP,WRITETEMP ,ACTORRIGHT ");			 
			sb.append(" from  s_rowright    ");
			String sql=sb.toString();
			
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			 
			while(rs.next()){
				SRowright sRowright=new SRowright();
				sRowright.setPk1(rs.getString("PK1"));
				sRowright.setResourceid(rs.getString("RESOURCEID"));
				sRowright.setReadtemp(rs.getString("READTEMP"));
				sRowright.setWritetemp(rs.getString("WRITETEMP"));
				sRowright.setActorright(rs.getString("ACTORRIGHT"));
		 	
				sRowrightMap.put(rs.getString("RESOURCEID").trim()+rs.getString("ACTORRIGHT").trim(),sRowright );
			}
		 
		
			ps.close();
			rs.close();
			 
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载资源定义数据END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载资源定义数据失败SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载资源定义数据失败！" );
			e.printStackTrace();

	}
		return sRowrightMap;
	}

	public Map<String, SDuty> getSDuty(Context context, Connection conn) {

		Map<String, SDuty> sDutyMap = new HashMap<String, SDuty>();
		PreparedStatement ps = null;
		try {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载岗位数据START..." );
			
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			sb.append("DUTYNO,");
			sb.append("DUTYNAME,");
			sb.append("ORGANNO,");
			sb.append("DEPNO,");
			sb.append("ORDERNO,");
			sb.append("TYPE,");
			sb.append("MEMO,");
			sb.append("ORGID");
			sb.append(" from  S_DUTY "); 		

			String sql=sb.toString();
			System.out.println(sql);
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SDuty sDuty = new SDuty();
				sDuty.setDutyno(rs.getString("DUTYNO"));
				sDuty.setDutyname(rs.getString("DUTYNAME"));
				sDuty.setOrganno(rs.getString("ORGANNO"));
				sDuty.setDepno(rs.getString("DEPNO"));
				sDuty.setOrderno(rs.getString("ORDERNO"));
				sDuty.setType(rs.getString("TYPE"));
				sDuty.setMemo(rs.getString("MEMO"));
				sDuty.setOrgid(rs.getString("ORGID"));
						
				sDutyMap.put(rs.getString("DUTYNO"), sDuty);
			}		
			ps.close();
			rs.close();
			//conn.close();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载岗位END..." );
		} catch (SQLException e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载岗位SQL！" );
				e.printStackTrace();
	
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
			"加载机构数据失败！" );
			e.printStackTrace();

	}
		return sDutyMap;
	}
}
