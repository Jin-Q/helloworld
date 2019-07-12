package com.yucheng.cmis.biz01line.cus.group.agent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.group.dao.GroupDao;
import com.yucheng.cmis.biz01line.cus.group.domain.CusGrpInfoApply;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;
/**
 * 
 * @Classname CusGrpInfoApplyAgent.java
 * @Version 4.0
 * @Since 4.0 Mar 9, 2011
 * @Copyright yuchengtech
 * @Author 徐凯希
 * @Description：本类主要代理集团客户申请基本信息相关业务数据的处理
 * @Lastmodified
 * @Author
 */
public class CusGrpInfoApplyAgent extends CMISAgent{
	/**
	 * 插入集团客户申请基本信息
	 * 
	 * @param cusGrpInfoApply
	 *            集团客户申请信息对象
	 * @return String
	 * @throws AgentException
	 */
	public String addRecord(CusGrpInfoApply cusGrpInfoApply) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）

		// 新增记录
		int count = this.insertCMISDomain(cusGrpInfoApply, PUBConstant.CUSGRPINFOAPPLY); // 1成功
																				// 其他失败
		// 如果失败，给标志信息赋值
		if (1 == count) {
			flagInfo = CMISMessage.SUCCESS; // 成功
		}
		return flagInfo;
	}
	/**
	 * 插入集团客户成员申请基本信息（变更）
	 * 
	 * @param CusGrpMemberApply
	 *            集团客户申请信息对象
	 * @return 
	 * @throws AgentException
	 */
	public void addCusGrpMemberApplyForMod(String grpNo,String serno) throws Exception {
		
		Connection con = this.getConnection();
		TableModelDAO tmdao = this.getTableModelDAO();
		try {
			IndexedCollection iColl = tmdao.queryList("CusGrpMember", " where grp_no ='"+grpNo+"'", con);
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				kColl.setName("CusGrpMemberApply");
				kColl.addDataField("serno", serno);
				tmdao.insert(kColl, con);				
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 查询指定的客户码是否作为母公司已存在
	 * 
	 * @param cusId
	 *            客户代码
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String CheakParCusIdApply(String cusId) throws Exception {
		String str = "have";
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		int intStr = dao.CheakParCusIdApply(cusId, conn);
		if (intStr == 0) {
			str = "canInsert";
		}
		return str;
	}
	
	/*
	 * 根据客户ID查找集团客户申请基本信息
	 * 
	 * @param grpNo 集团编号
	 * 
	 * @return 集团客户基本信息
	 * 
	 * @throws EMPException
	 */
	public CusGrpInfoApply findCusGrpInfoApplyByGrpNo(String grpNo) throws EMPException {

		CusGrpInfoApply cusGrpInfoApply = new CusGrpInfoApply();
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("grp_no", grpNo);
		cusGrpInfoApply = (CusGrpInfoApply) this.findCMISDomainByKeywords(cusGrpInfoApply,
				PUBConstant.CUSGRPINFOAPPLY, pk_values);

		return cusGrpInfoApply;
	}
	/**
	 * 判断该集团客户是否有正在进行的一般授信或是一般授信变更操作
	 * @param serno 申请序列号
	 */
	public String checkLmtApplyAndLmtModAppBySerno(String serno) throws AgentException{
		
		String msg = "";
		Connection con = this.getConnection();
		TableModelDAO dao = this.getTableModelDAO();
		//关联变更授信业务
		try {
			IndexedCollection indexColl2 = dao.queryList("LmtModApp", 
					" where cus_id in (select cus_id from cus_grp_member_apply where serno='"+serno+"') and approve_status in('000','111','992')", con);
			if(indexColl2.size()>0){
				 msg = "该集团的成员客户有存在正在做的一般授信变更业务，请先完成该集团相关客户的一般授信变更业务";
				 return msg;
			}
			//关联授信业务
			IndexedCollection indexColl1 = dao.queryList("LmtApply", 
					" where cus_id in (select cus_id from cus_grp_member_apply where serno='"+serno+"') and approve_status in('000','111','992')", con);
			if(indexColl1.size()>0){
				msg = "该集团的成员客户有存在正在做的一般授信业务，请先完成该集团相关客户的一般授信业务";
				return msg;
			}
		
		}catch(Exception e){
			throw new AgentException(e);
		}
		return msg;
	}
	
	/**
	 * 审批通过后将插入到cus_grp_info和cus_grp_member
	 * 关联集团新增申请
	 * @param serno 申请序列号
	 *            
	 * @return 信息编码
	 * @throws AgentException
	 */
	public void insertCusGrpInfoAndCusGrpMember(String serno) throws AgentException{
		
		Connection con = this.getConnection();
		TableModelDAO tmdao= this.getTableModelDAO();
		GroupDao dao = new GroupDao();
		try {
			
			IndexedCollection iColl = tmdao.queryList("CusGrpMemberApply", " where serno='"+serno+"'", con);
			
			//插入集团客户表cus_grp_info
			dao.insertCusGrpInfo(serno, con);
			
			//插入集团客户历史表cus_grp_info_his
//			dao.insertCusGrpInfoHis(serno, con);
			
			//插入集团客户成员表cus_grp_member
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kc = (KeyedCollection) iColl.get(i);
				String cusId = (String) kc.getDataValue("cus_id");
				//更新cus_com里的com_grp_mode/grp_no
				String sql = " update cus_com set com_grp_mode ='"+kc.getDataValue("grp_corre_type")+"' , grp_no='"+kc.getDataValue("grp_no")+"' where cus_id='"+cusId+"'";
				this.executeSql(sql);
				//该客户的授信协议增加进集团授信协议
				//ILmt iLmt = (ILmt)CMISComponentFactory
				//.getComponentFactoryInstance().getComponentInterface("LmtInterface", this.getContext(), con);
				//iLmt.mergerCusLmtToGrpLmt(cusId,(String) kc.getDataValue("grp_no"));
				
				dao.insertCusGrpMember(serno,cusId, con);
//				dao.insertCusGrpMemberHis(serno,cusId, con);
			}
			//更新责任人
			String sql1 = "update cus_grp_info cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.PARENT_CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql1);
			//更新责任人
			String sql2 = "update cus_grp_member cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql2);
			/*//更新责任人
			String sql3 = "update cus_grp_info_his cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.PARENT_CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql3);*/
			//更新责任人
			String sql4 = "update cus_grp_member_his cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql4);
			
		} catch (EMPException e) {
            throw new AgentException("出现错误"+e);
		}
	}
	/**
	 * 审批通过后将修改到cus_grp_info和cus_grp_member，cus_loan_rel
	 * 关联集团变更
	 * @param serno 申请序列号
	 *            
	 * @return 信息编码
	 * @throws AgentException
	 */
	
	public void updateCusGrpInfoAndCusGrpMember(String serno) throws AgentException{
		
		Connection conn = this.getConnection();
		TableModelDAO tmdao = this.getTableModelDAO();
		
		String cusId = null;
		String cusIdApply = null;
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		
		try {
			//取得申请集团
			KeyedCollection kColl = tmdao.queryFirst("CusGrpInfoApply", null, " where serno ='"+serno+"'", conn);
			String grpName = (String) kColl.getDataValue("grp_name");
			String grpDetail = (String) kColl.getDataValue("grp_detail");
			String grpNo = (String) kColl.getDataValue("grp_no"); 
			//取得申请集团的成员
			IndexedCollection iCollApply = tmdao.queryList("CusGrpMemberApply", " where serno='"+serno+"'", conn);
			//取得有效的集团成员
			IndexedCollection iColl = tmdao.queryList("CusGrpMember", " where grp_no='"+grpNo+"'", conn);
			
			/**
			 * 修改cus_grp_info
			 */
			String sql1 ="update cus_grp_info set grp_name='"+grpName+"',grp_detail='"+grpDetail+"' " +
					"where grp_no in (select grp_no from cus_grp_info_apply where serno='"+serno+"')";
			this.executeSql(sql1);
			
			for(int i=0;i<iCollApply.size();i++){
				KeyedCollection kcApply = (KeyedCollection) iCollApply.get(i);
				cusIdApply = (String) kcApply.getDataValue("cus_id");
				list1.add(cusIdApply);
				for(int j=0;j<iColl.size();j++){
					KeyedCollection kc = (KeyedCollection) iColl.get(j);
					cusId = (String) kc.getDataValue("cus_id");
					if(cusId.equals(cusIdApply)){
						list2.add(cusId);
					}
				}
				//插入集团客户成员历史表cus_grp_info_his
//				GroupDao dao0 = new GroupDao();
//				dao0.insertCusGrpMemberHis(serno,cusId, conn);

			}
			/**
			 * 判断cus_grp_member_apply有无增加成员，则将该成员添加到cus_grp_member
			 */
			for(int i=0;i<list1.size();i++){
				boolean isTrue = list2.contains(list1.get(i));
				if(!isTrue){
					String cus_id =list1.get(i); //取得新增加的客户成员id
			
					KeyedCollection kcoll = tmdao.queryFirst("CusGrpMemberApply", null, " where serno='"+serno+"' and cus_id='"+cus_id+"'", conn);
					kcoll.setName("CusGrpMember");
					tmdao.insert(kcoll, conn);
					
					//更新cus_com里的com_grp_mode/grp_no
					String sql = " update cus_com set com_grp_mode ='"+kcoll.getDataValue("grp_corre_type")+"' , " +
								"grp_no='"+kcoll.getDataValue("grp_no")+"' " +
								"where cus_id='"+cus_id+"' ";
					this.executeSql(sql);
				}
			}
			
			
			String cus_grp_member_cus_id ="" ;
			
			for(int i = 0;i<iColl.size();i++)
			{
				KeyedCollection kk = (KeyedCollection)iColl.get(i);
				cus_grp_member_cus_id = cus_grp_member_cus_id +"'"+kk.getDataValue("cus_id")+"',";
			}
			if(cus_grp_member_cus_id.length()>0)
				cus_grp_member_cus_id = cus_grp_member_cus_id.substring(0, cus_grp_member_cus_id.length()-1);
			
			String cus_grp_member_cus_id_apply = "";
			for(int i = 0;i<list2.size();i++)
			{
				cus_grp_member_cus_id_apply = cus_grp_member_cus_id_apply +"'"+list2.get(i)+"',";
			}
			if(cus_grp_member_cus_id_apply.length()>0)
				cus_grp_member_cus_id_apply = cus_grp_member_cus_id_apply.substring(0, cus_grp_member_cus_id_apply.length()-1);

					//String cus_id =list1.get(i); //取得新增加的客户成员id
			
			IndexedCollection icoll = tmdao.queryList("CusGrpMember", null, " where  cus_id in ("+cus_grp_member_cus_id+") and cus_id not in ("+cus_grp_member_cus_id_apply+")", conn);
			icoll.setName("CusGrpMember");
			String delete_cus_grp_member_cus_ids = "";
			for(int i = 0;i<icoll.size();i++)
			{
				KeyedCollection kColl2 = (KeyedCollection)icoll.get(i);
				delete_cus_grp_member_cus_ids = delete_cus_grp_member_cus_ids +"'"+kColl2.getDataValue("cus_id")+"',";
			}
			if(delete_cus_grp_member_cus_ids.length()>0)
			{
				delete_cus_grp_member_cus_ids = delete_cus_grp_member_cus_ids.substring(0, delete_cus_grp_member_cus_ids.length()-1);
				String sqll = "delete from cus_grp_member where cus_id in ("+delete_cus_grp_member_cus_ids+")";
				this.executeSql(sqll);

				/*CusGrpMemberApplyDao cusGrpMemberApplyDao = new CusGrpMemberApplyDao();
				cusGrpMemberApplyDao.deteleSomeCusGrpMember( delete_cus_grp_member_cus_ids, conn);*/
						//更新cus_com里的com_grp_mode/grp_no
				//'STD_ZB_GROUP_TYPE' : {'3':'关联集团核心企业(母公司)', '4':'关联集团附属企业(子公司)', '9':'非集团客户'},
				String sql = " update cus_com set com_grp_mode ='9' , " +
									"grp_no= null" +
									" where cus_id in ("+delete_cus_grp_member_cus_ids+") ";
				this.executeSql(sql);
			}
//			GroupDao dao = new GroupDao();

			//插入集团客户历史表cus_grp_info_his
//			dao.insertCusGrpInfoHis(serno, conn);
			//更新责任人
			String sql11 = "update cus_grp_info cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.PARENT_CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql11);
			//更新责任人
			String sql2 = "update cus_grp_member cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql2);
			/*//更新责任人
			String sql3 = "update cus_grp_info_his cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.PARENT_CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql3);*/
			//更新责任人
			String sql4 = "update cus_grp_member_his cgi set cgi.manager_id =(select cc.CUST_MGR from cus_com cc where cc.cus_id = cgi.CUS_ID ) where cgi.GRP_NO = (select grp_no from cus_grp_info_apply where serno = '"+serno+"') ";
			this.executeSql(sql4);

		} catch (EMPException e) {
            throw new AgentException("出现错误"+e);
		}
		
	}
	/**
	 * 查询该集团下是否存在集团成员 是 返回1，否 返回0
	 * 
	 * @param grp_no 集团编号
	 *            
	 * @return 信息编码
	 * @throws AgentException
	 */
	public int checkCusGrpMember(String serno) throws Exception{
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		int flag;
		try {
			flag = dao.checkCusGrpMember(serno,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flag;
	}
	/**
	 *  判断该申请集团向下的成员是否已经存在别的有效集团中 是 返回1，否 返回0
	 * 
	 * @param grp_no 集团编号
	 *            
	 * @return 信息编码
	 * @throws AgentException
	 */
	public int isExistCusGrpMember(String serno) throws Exception{
		GroupDao dao = new GroupDao();
		Connection conn = this.getConnection();
		int flag;
		try {
			flag = dao.isExistCusGrpMember(serno,conn);
		} catch (DaoException e) {
			throw new AgentException(e.getMessage());
		}
		return flag;
	}
	
}

