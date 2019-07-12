package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/***
 * 合作方授信修改：不判断是否存在有效授信，每次都直接生成新授信，原因：一个合作方客户可能存在多个项目，
 * 如：房地产开发商，可能存在多个楼盘。
 * @author 唐顺岩
 * @date 2014-09-25
 */
public class LmtJointCoopLeadFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		
		Connection connection = null;
		try{
			connection = this.getConnection();
			String openDate = context.getDataValue("OPENDAY").toString();
		
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl_agr = dao.queryDetail("LmtAppJointCoop", serno_value, connection);
			
			kColl_agr.setDataValue("over_date", openDate);
			//修改申请表的办结日期、申请状态
			dao.update(kColl_agr, connection);
			
			String lmt_agr_no = "";
			String coop_type = (String)kColl_agr.getDataValue("coop_type");//合作方类别
			
			/**合作方授信修改：不判断是否存在有效授信，每次都直接生成新授信，原因：一个合作方客户可能存在多个项目   2014-09-25  唐顺岩 
			//根据合作方类型、客户号、判断是否存在有效的合作方授信协议
			String cus_id = kColl_agr.getDataValue("cus_id").toString();
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			list.add("start_date");
			StringBuffer condition = new StringBuffer(" WHERE CUS_ID='"+cus_id+"' AND COOP_TYPE='"+kColl_agr.getDataValue("coop_type")+"'");
			condition.append(" AND END_DATE > '"+openDate+"'");  //授信到期顺延6个月
			KeyedCollection kColl = dao.queryFirst("LmtAgrJointCoop", list, condition.toString(), connection);
			END*/
			String end_date = LmtUtils.computeEndDate(openDate, kColl_agr.getDataValue("term_type").toString(), kColl_agr.getDataValue("term").toString());
			kColl_agr.put("end_date", end_date);  //设置到期日期
			kColl_agr.removeDataElement("over_date");  //清除到期日期
			kColl_agr.setName("LmtAgrJointCoop");  //将申请的kColl转换为协议
			kColl_agr.put("agr_status","002");
			
			//生成授信协议编号
			lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
			kColl_agr.put("agr_no", lmt_agr_no);
			kColl_agr.put("start_date", openDate);
			
			dao.insert(kColl_agr, connection);
			/**合作方授信修改：不判断是否存在有效授信，每次都直接生成新授信，原因：一个合作方客户可能存在多个项目   2014-09-25  唐顺岩 
			if(null != kColl && (null==kColl.getDataValue("agr_no") || "".equals(kColl.getDataValue("agr_no")))){  //如果协议号不存在说明是新增
				//生成授信协议编号
				lmt_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				kColl_agr.put("agr_no", lmt_agr_no);
				kColl_agr.put("start_date", openDate);
				
				dao.insert(kColl_agr, connection);
			}else{   //变更
				lmt_agr_no = (String)kColl.getDataValue("agr_no");
				kColl_agr.put("agr_no", lmt_agr_no);
				kColl_agr.put("start_date", kColl.getDataValue("start_date"));  //变更时直接取原起始日期为协议起始日
				dao.update(kColl_agr, connection);
				//变更时先将原项目信息与协议关系解除
				String condTmp = "where agr_no='"+lmt_agr_no+"'";
				if("001".equals(coop_type)){//房地产
					KeyedCollection kCollBuil = dao.queryFirst("LmtCoopBuilding", null, condTmp, connection);
					if(kCollBuil!=null&&kCollBuil.getDataValue("pro_no")!=null&&!"".equals(kCollBuil.getDataValue("pro_no"))){
						kCollBuil.put("agr_no", "");
						dao.update(kCollBuil, connection);
					}
				}else if("002".equals(coop_type)){//汽车
					KeyedCollection kCollCar = dao.queryFirst("LmtCoopCar", null, condTmp, connection);
					if(kCollCar!=null&&kCollCar.getDataValue("pro_no")!=null&&!"".equals(kCollCar.getDataValue("pro_no"))){
						kCollCar.put("agr_no", "");
						dao.update(kCollCar, connection);
					}
				}else {
					KeyedCollection kCollMac = dao.queryFirst("LmtCoopMachine", null, condTmp, connection);
					if(kCollMac!=null&&kCollMac.getDataValue("pro_no")!=null&&!"".equals(kCollMac.getDataValue("pro_no"))){
						kCollMac.put("agr_no", "");
						dao.update(kCollMac, connection);
					}
				}
			}
			END*/
			//重新挂项目与协议关系
			String condTmp = "where serno='"+serno_value+"'";
			if("001".equals(coop_type)){//房地产
				KeyedCollection kCollBuil = dao.queryFirst("LmtCoopBuilding", null, condTmp, connection);
				if(kCollBuil!=null&&kCollBuil.getDataValue("pro_no")!=null&&!"".equals(kCollBuil.getDataValue("pro_no"))){
					kCollBuil.put("agr_no", lmt_agr_no);
					dao.update(kCollBuil, connection);
				}
			}else if("002".equals(coop_type)){//汽车
				KeyedCollection kCollCar = dao.queryFirst("LmtCoopCar", null, condTmp, connection);
				if(kCollCar!=null&&kCollCar.getDataValue("pro_no")!=null&&!"".equals(kCollCar.getDataValue("pro_no"))){
					kCollCar.put("agr_no", lmt_agr_no);
					dao.update(kCollCar, connection);
				}
			}else {
				KeyedCollection kCollMac = dao.queryFirst("LmtCoopMachine", null, condTmp, connection);
				if(kCollMac!=null&&kCollMac.getDataValue("pro_no")!=null&&!"".equals(kCollMac.getDataValue("pro_no"))){
					kCollMac.put("agr_no", lmt_agr_no);
					dao.update(kCollMac, connection);
				}
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection retKColl = new KeyedCollection();
//			KeyedCollection kColl = new KeyedCollection();
//			kColl.put("CLIENT_NO", kColl_agr.getDataValue("cus_id"));
//			kColl.put("BUSS_SEQ_NO", "");
//			kColl.put("TASK_ID", "");
//			try{
//				retKColl = serviceRel.tradeIMAGELOCKED(kColl, context, connection);	//调用影像锁定接口
//			}catch(Exception e){
//				throw new Exception("影像锁定接口失败!");
//			}
//			if(!TagUtil.haveSuccess(retKColl, context)){
//				//交易失败信息
//				throw new Exception("影像锁定接口失败!");
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("合作方授信流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		Map<String, String> param = new HashMap<String, String>();
		param.put("cus_id",cus_id);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}

}
