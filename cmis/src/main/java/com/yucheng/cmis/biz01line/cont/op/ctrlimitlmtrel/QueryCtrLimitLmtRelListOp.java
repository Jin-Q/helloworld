package com.yucheng.cmis.biz01line.cont.op.ctrlimitlmtrel;

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
/**
 * 通过业务流水号查询业务下的合同占用授信明细信息
 * @author Pansq
 * 列表页面需要显示的要素：授信品种、占用金额
 */
public class QueryCtrLimitLmtRelListOp extends CMISOperation {
	private final String modelId = "CtrLimitLmtRel";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = null;
		try{
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
			}
			String conditionStr = " where limit_serno = '"+serno+"'";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/** 查询出关联表中的授信台帐编号,封装成参数传递给授信模块 */
			String param = "";
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			if(iColl != null && iColl.size() > 0){
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
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
			
			/** 遍历取值 */
			if(iColl != null|| iColl.size() > 0){
				/**翻译额度名称   2013-11-29 唐顺岩**/
				String[] args=new String[] { "limit_name" };
				String[] modelIds=new String[]{"PrdBasicinfo"};
				String[] modelForeign=new String[]{"prdid"};
				String[] fieldName=new String[]{"prdname"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(lmtIColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				String lmt_code_name ="";
				/** END */
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kc = (KeyedCollection)iColl.get(i);
					String lmt_code = (String)kc.getDataValue("lmt_code_no");
					if(lmtIColl != null|| lmtIColl.size() > 0){
						for(int j=0;j<lmtIColl.size();j++){
							KeyedCollection kc1 = (KeyedCollection)lmtIColl.get(j);
							String lmt_code1 = (String)kc1.getDataValue("limit_code");
							if(lmt_code.equals(lmt_code1)){
								/**如果额度品种名称翻译不到 直接取原值  2013-11-29 唐顺岩 */
								if(null!=kc1.getDataValue("limit_name_displayname") && !"".equals(kc1.getDataValue("limit_name_displayname"))){
									lmt_code_name = (String)kc1.getDataValue("limit_name_displayname");
								}else{
									lmt_code_name = (String)kc1.getDataValue("limit_name");
								}
								/**END */
								kc.addDataField("lmt_code_name", lmt_code_name);
								kc.addDataField("lmt_code_amt", kc1.getDataValue("crd_amt"));
								kc.addDataField("lmt_code_enable_amt", kc1.getDataValue("enable_amt"));
							}
						}
					}
					
				}
			}
			
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
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
