package com.yucheng.cmis.biz01line.lmt.op.lmtagrjointcoop;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrJointCoopListOp extends CMISOperation {

	private final String modelId = "LmtAgrJointCoop";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
			String cusMemberId="";
			String cusIdList="";
			IndexedCollection iCollSelect = new IndexedCollection();
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
			if(queryData !=null && !"".equals(queryData)){
				cusMemberId = (String) queryData.getDataValue("cus_member_id");
				if(cusMemberId !=null && !"".equals(cusMemberId)){
					String conSelect ="SELECT la.cus_id FROM lmt_agr_joint_coop la,lmt_name_list t " 
							   +" WHERE la.agr_no=t.agr_no and t.sub_type='03' and t.cus_id='"+cusMemberId+"' ";
					iCollSelect = TableModelUtil.buildPageData(null, dataSource, conSelect);
					if(iCollSelect !=null && iCollSelect.size() > 0){
						for(int i=0;i<iCollSelect.size();i++){
							KeyedCollection kColl = (KeyedCollection) iCollSelect.get(i);
							cusIdList += kColl.getDataValue("cus_id")+",";
						}
							conditionStr += " and instr('"+ cusIdList+"', cus_id)>0 ";						
					}else{
						conditionStr +=" and cus_id='' ";
					}
				}
			}
			/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
			//COOP_TYPE=010为联保小组协议 其他都为合作方
			conditionStr += " AND COOP_TYPE='010' " + "order by agr_no desc";
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, pageInfo,connection);

			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用	
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addUSerName(iColl, new String[]{"input_id","manager_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id","manager_br_id"});
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		}catch (EMPException ee) {
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
