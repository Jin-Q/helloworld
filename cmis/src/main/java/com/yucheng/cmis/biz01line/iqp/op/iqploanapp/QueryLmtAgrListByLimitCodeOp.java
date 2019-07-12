package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 通过额度合同编号，担保方式获取选择的额度合同项下所引用的额度合同
 * @author Pansq
 */
public class QueryLmtAgrListByLimitCodeOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection queryData = null;
		IndexedCollection newLmtIColl = new IndexedCollection();
		try {
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = this.getConnection(context);
			String limit_cont_no = (String)context.getDataValue("limit_cont_no");//额度合同编号
			String guar_type = (String)context.getDataValue("guar_type");//担保方式
			String limit_type = (String)context.getDataValue("limit_type");//额度类型
			try {
				queryData = (KeyedCollection)context.getDataElement("LmtAgr");
				queryData.setName("CtrLimitLmtRelTemp");
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition_bak("CtrLimitLmtRelTemp", queryData, context, false, true, false);
			if(conditionStr != null && !"".equals(conditionStr)){
				conditionStr +=" and limit_cont_no ='"+limit_cont_no+"'";
			}else{
				conditionStr +=" where limit_cont_no ='"+limit_cont_no+"'";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			String param = "";
			IndexedCollection ctrIColl = dao.queryList("CtrLimitLmtRelTemp", null, conditionStr, connection);
			if(ctrIColl != null && ctrIColl.size() > 0){
				for(int i=0;i<ctrIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)ctrIColl.get(i);
					String lmt_code = (String)kc.getDataValue("lmt_code_no");
					param += ("'"+lmt_code+"',");
				}
			}
			if(param.trim().length() > 0){
				param = param.substring(0, param.length()-1);
			}
			if(param == null || param.trim() == ""){
				param = "''";
			}
			PageInfo pageInfo = new PageInfo();
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IndexedCollection lmtIColl = lmtServiceInterface.queryLmtAgrDetailsByLimitCodeStr(param, pageInfo, dataSource);
			if(lmtIColl != null && lmtIColl.size() > 0){
				for(int i=0;i<lmtIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)lmtIColl.get(i);
					String guarType = (String)kc.getDataValue("guar_type");
					String limitType = (String)kc.getDataValue("limit_type");
					if(guar_type.equals(guarType) && limit_type.equals(limitType)){
						newLmtIColl.add(kc);
					}
				}
			}
			newLmtIColl.setName("LmtAgrList");
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(newLmtIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(newLmtIColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
		    this.putDataElement2Context(newLmtIColl, context);
		    TableModelUtil.parsePageInfo(context, pageInfo);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
