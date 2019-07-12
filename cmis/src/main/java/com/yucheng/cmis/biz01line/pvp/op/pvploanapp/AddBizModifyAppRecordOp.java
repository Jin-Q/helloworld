package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class AddBizModifyAppRecordOp extends CMISOperation {
	
	private final String modelId = "PvpBizModifyRel";
	private final String contModelId = "CtrLoanCont";
	private final String IqpExtModelId = "IqpExtensionAgr";
	private final String modelHisId = "CtrLoanContHis";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = "";
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			String biz_serno ="";
			if(context.containsKey("serno")){
				biz_serno = (String)context.getDataValue("serno");
			}
			String biz_cate ="";
			if(context.containsKey("biz_cate")){
				biz_cate = (String)context.getDataValue("biz_cate");
			}
			
			String cus_id ="";
			if(context.containsKey("cus_id")){
				cus_id = (String)context.getDataValue("cus_id");
			}
			String fount_serno ="";
			if(context.containsKey("fount_serno")){
				fount_serno = (String)context.getDataValue("fount_serno");
			}
			String approve_status ="000";
			//生成流水号
			String modify_rel_serno = "M"+CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			if(cus_id!=null && !"".equals(cus_id)){
				if(biz_cate ==null || "".equals(biz_cate)){
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					CusBase cus = service.getCusBaseByCusId(cus_id, context, connection);
					String beLgLine = cus.getBelgLine();
					if("BL300".equals(beLgLine)){
						biz_cate ="0012";
					}else{
						biz_cate ="0011";
					}
				}
			}
			//新增打回业务修改关联信息	
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection PBMR = new KeyedCollection(modelId);
			PBMR.put("modify_rel_serno", modify_rel_serno);
			PBMR.put("cont_no", cont_no);
			PBMR.put("biz_serno", biz_serno);
			PBMR.put("approve_status", approve_status);
			PBMR.put("biz_cate", biz_cate);//业务类型
			dao.insert(PBMR, connection);
			
			//将合同信息载入缓存表中
			if("0011".equals(biz_cate) || "0012".equals(biz_cate)){
				KeyedCollection  CLCTMP = dao.queryDetail(contModelId, cont_no, connection);
				KeyedCollection  CLCSTMP = dao.queryDetail("CtrLoanContSub", cont_no, connection);
				CLCTMP.put("modify_rel_serno", modify_rel_serno);
				CLCSTMP.put("modify_rel_serno", modify_rel_serno);
				CLCTMP.setName("CtrLoanContTmp");
				CLCSTMP.setName("CtrLoanContSubTmp");
				try {
					dao.insert(CLCTMP, connection);
					dao.insert(CLCSTMP, connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				KeyedCollection kColl4QrCont = new KeyedCollection();
				KeyedCollection kColl4QrSerno = new KeyedCollection();
				KeyedCollection valueKcoll = new KeyedCollection();
				kColl4QrCont.addDataField("CONT_NO", cont_no);
				kColl4QrSerno.addDataField("FOUNT_SERNO", fount_serno);
				valueKcoll.addDataField("MODIFY_REL_SERNO", modify_rel_serno);
				//银承载入明细缓存信息
				SqlClient.executeUpd("createIqpAccpDetailTmpRecord", kColl4QrSerno, valueKcoll, null,connection);
				//银承载入明细历史信息
				SqlClient.executeUpd("createIqpAccpDetailHisRecord", kColl4QrSerno, valueKcoll, null,connection);
				
				//载入保证金缓存信息
				SqlClient.executeUpd("createPubBailInfoTmpRecord", kColl4QrSerno, valueKcoll, null,connection);
				//载入保证金历史信息
				SqlClient.executeUpd("createPubBailInfoHisRecord", kColl4QrSerno, valueKcoll, null,connection);
				
				//载入附加条款缓存信息
				SqlClient.executeUpd("createIqpAppendTermsTmpRecord", kColl4QrSerno, valueKcoll, null,connection);
				//载入附加条款历史信息
				SqlClient.executeUpd("createIqpAppendTermsHisRecord", kColl4QrSerno, valueKcoll, null,connection);
				String repay_type = (String) CLCSTMP.getDataValue("repay_type");
				if(repay_type!=null&&!"".equals(repay_type)&&"A001".equals(repay_type)){
					//载入还款计划登记信息
					SqlClient.executeUpd("createIqpFreedomPayInfoTmpRecord", kColl4QrSerno, valueKcoll, null,connection);
					SqlClient.executeUpd("createIqpFreedomPayInfoHisRecord", kColl4QrSerno, valueKcoll, null,connection);
				}
				//载入账户缓存信息
				SqlClient.executeUpd("createIqpCusAcctTmpRecord", kColl4QrCont, valueKcoll, null,connection);
				SqlClient.executeUpd("createIqpCusAcctHisRecord", kColl4QrCont, valueKcoll, null,connection);
				//载入担保缓存信息
				SqlClient.executeUpd("createGrtLoanRGurTmpRecord", kColl4QrCont, valueKcoll, null,connection);

			}else if("016".equals(biz_cate)){
				KeyedCollection  IEATMP = dao.queryFirst(IqpExtModelId,null, "where agr_no='"+cont_no+"'", connection);
				IEATMP.put("modify_rel_serno", modify_rel_serno);
				IEATMP.setName("IqpExtensionAgrTmp");
				dao.insert(IEATMP, connection);
			}
			context.put("modify_rel_serno", modify_rel_serno);
			context.put("flag", "success");	
		}catch (EMPException ee) {
			context.put("flag", "failed");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
