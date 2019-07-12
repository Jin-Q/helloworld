package com.yucheng.cmis.biz01line.lmt.op.lmtappgrp;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

/** 集团成员变更条线
 * @author 唐顺岩
 * @date 2014-01-06
 */
public class UpdateLine4GrpMemberApp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(UpdateLine4GrpMemberApp.class);

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try {
			String lmt_type = "";
			if(context.containsKey("serno")){
				serno = context.getDataValue("serno").toString();
			}
			if(context.containsKey("lmt_type")){
				lmt_type = context.getDataValue("lmt_type").toString();
			}
			
			connection = this.getConnection(context);
			
			KeyedCollection kColl_app = new KeyedCollection("LmtApply");
			
			logger.info("根据流水号查询集团授信下成员申请信息……");
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl_app = dao.queryFirst("LmtApply", null, " WHERE SERNO='"+ serno +"'", connection);
		
			//授信类别不一样时
			if(!lmt_type.equals(kColl_app.getDataValue("lmt_type"))){
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				
				//删除申请分项及分项与担保合同关系  2014-01-06   唐顺岩  
				logger.info("删除申请分项及分项与担保合同关系 ……");
				lmtComponent.deleteLmtApplyDetailsBySerno(serno);
				
				logger.info("将对应台账数据复制数据到申请分项表 ……");
				//复制数据到申请分项表
				lmtComponent.createLmtAppDetailsRecord((String)kColl_app.getDataValue("agr_no"), serno, "20");
				/** END */
				
				logger.info("更新授信类别，新类别为："+lmt_type);
				
				/**如果客户存在有效授信协议 从台账中实时汇总循环额度、一次性额度    2014-01-19  唐顺岩   */
				if(null != kColl_app && null != kColl_app.getDataValue("agr_no") && !"".equals(kColl_app.getDataValue("agr_no"))){
					List<String> list = new ArrayList<String>();
					String condition = " WHERE CUS_ID='"+kColl_app.getDataValue("cus_id")+"' AND AGR_STATUS='002' AND to_char(add_months(to_date(END_DATE,'yyyy-mm-dd'),6),'yyyy-mm-dd') >= (SELECT OPENDAY FROM PUB_SYS_INFO) ";
					KeyedCollection kColl = dao.queryFirst("LmtAgrInfo", list, condition, connection);
					//如果存在有效授信协议将协议编号赋值给申请中的协议编号
					KeyedCollection kColl_details = lmtComponent.selectLmtAgrDetailsAmt(kColl_app.getDataValue("agr_no").toString());
					//原有
					kColl_app.put("org_crd_totl_amt", kColl.getDataValue("crd_totl_amt")); //授信总额    --从协议中取，原有协议多少就多少  2014-01-04
					kColl_app.put("org_crd_cir_amt", kColl.getDataValue("crd_cir_amt"));//循环总额    --从协议中取，原有协议多少就多少  2014-01-04
					kColl_app.put("org_crd_one_amt", kColl.getDataValue("crd_one_amt"));//一次性总额    --从协议中取，原有协议多少就多少  2014-01-04
					if(null != kColl_details){
						//现有
						kColl_app.put("crd_totl_amt", kColl_details.getDataValue("total_amt"));
						kColl_app.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
						kColl_app.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
					}
				}else{   //如果不存在授信协议 直接赋值为0
					kColl_app.put("crd_totl_amt", "0.00");
					kColl_app.put("crd_cir_amt", "0.00");
					kColl_app.put("crd_one_amt", "0.00");
				}
				/** END */
				kColl_app.put("lmt_type", lmt_type);
				dao.update(kColl_app, connection);
			}
			context.addDataField("flag", "success");
			context.put("serno", serno);
			context.addDataField("msg", "success");
		} catch (Exception e) {
			context.addDataField("flag", "error");
			context.put("serno", "");
			context.addDataField("msg", "变更集团成员授信类别失败，失败原因："+e.getMessage());			
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
