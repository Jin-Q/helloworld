package com.yucheng.cmis.biz01line.dth.op.dthApproveQuery;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetDthApproveQueryPageOp extends CMISOperation {
	
	/**
	 * 审批报备是取时间区间上周与本周的数据，目前就时间范围这个问题还有争议
	 * 旧系统的取数方式是：按审批权节点的办结时间来取。这个取法的缺陷是：[审批节点办结时间]在[业务审结时间]上周之前的话，
	 * 					那这个节点就无法出现在报备记录中了。
	 * 还有一种取数方式是：按整个业务审批完结的时间来取。这能避免上一取法的缺陷，但在[业务审结时间]会同时取到这笔业务
	 * 					所有节点的记录，意味着会把不在这周的[节点审批记录]也在这一周报备了。
	 * 目前还是沿用旧系统的取法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			/*** 1.调用命名sql先取出客户表与授信表中的数据 ***/
			Object submitType = context.getDataValue("submitType");
			DthPubAction cmisOp = new DthPubAction();
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("submitType", submitType);
			IndexedCollection iColl_wf = cmisOp.delSqlReturnIcoll(kColl,context);

			/*** 2.审批意见有加密，要单独处理***/
			for(int i = 0 ; i < iColl_wf.size() ; i++){
				KeyedCollection kColl_wf = (KeyedCollection) iColl_wf.get(i);
				
				/*** 3.授信下业务品种与担保类型特殊翻译处理 ***/
				if(submitType.equals("getDthApproveLmtLast") || submitType.equals("getDthApproveLmtThis")){							
					if((kColl_wf.getDataValue("prd_id").toString()).length()>2){	//处理产品编号时要手工去重复
						String[] prd_ids = (kColl_wf.getDataValue("prd_id").toString()).split(",");
						prd_ids = cmisOp.removeRepeat(prd_ids);
						String prd_id = "";
						for(int m = 0 ; m < prd_ids.length ; m++){
							prd_id = prd_id + prd_ids[m]+",";
						}
						kColl_wf.setDataValue("prd_id", prd_id.substring(0, prd_id.length()-1));
						SInfoUtils.getPrdPopName(kColl_wf, "prd_id", connection);  	//翻译产品
						String prd_id_show = kColl_wf.getDataValue("prd_id_displayname").toString();
						prd_id_show = prd_id_show.length()>=10?prd_id_show.substring(0, 10):prd_id_show;
						kColl_wf.addDataField("prd_id_show", prd_id_show);
					}
					if((kColl_wf.getDataValue("guar_type").toString()).length()>2){
						cmisOp.getDicsName(kColl_wf, "guar_type","STD_ZB_ASSURE_MEANS", connection);  	//翻译担保方式
					}
				}
				/*** 审批意见截取 ***/
				String commentcontent_show = kColl_wf.getDataValue("commentcontent").toString();
				commentcontent_show = commentcontent_show.length()>=10?commentcontent_show.substring(0, 10):commentcontent_show;
				kColl_wf.addDataField("commentcontent_show", commentcontent_show);
			}

			SInfoUtils.addSOrgName(iColl_wf, new String[]{"orgid"});
			iColl_wf.setName("ApproveQueryList");
			this.putDataElement2Context(iColl_wf, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
		
}