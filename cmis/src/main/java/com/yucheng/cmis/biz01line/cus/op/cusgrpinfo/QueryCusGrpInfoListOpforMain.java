package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

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
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGrpInfoListOpforMain extends CMISOperation {

	private final String modelId = "CusGrpInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}

			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			conditionStr = StringUtil.transConditionStr(conditionStr, "grp_name");
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			/**add by lisj 2015-3-24   支持输入集团客户名称，集团成员名称模糊查询，于2015-3-24 上线 begin**/
			String GrpMemberName="";
			String grpIdList="";
			IndexedCollection iCollSelect4GM = new IndexedCollection();
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);	
			if(queryData !=null && !"".equals(queryData)){
				//增加通过集团成员查询集团客户的功能 2015-3-24 by lisj
				GrpMemberName = (String) queryData.getDataValue("grp_member_name");
				if(GrpMemberName !=null && !"".equals(GrpMemberName)){
					String conSelect ="select p1.grp_no from cus_grp_member p1, cus_base p2 where  p2.cus_id = p1.cus_id "
							         +"and p2.cus_name like '%"+GrpMemberName+"%'";
					iCollSelect4GM = TableModelUtil.buildPageData(null, dataSource, conSelect);
					if(iCollSelect4GM !=null && iCollSelect4GM.size() > 0){
						for(int i=0;i<iCollSelect4GM.size();i++){
							KeyedCollection kColl = (KeyedCollection) iCollSelect4GM.get(i);
							grpIdList += kColl.getDataValue("grp_no")+",";
						}
							conditionStr += " and instr('"+ grpIdList+"', grp_no)>0";						
					}else{
						conditionStr +=" and grp_no=''";
					}
				}
			}
			/**add by lisj 2015-3-24   支持输入集团客户名称，集团成员名称模糊查询，于2015-3-24 上线 end**/
			conditionStr = conditionStr+" order by grp_no desc";
		
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "parent_cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
            SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
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
