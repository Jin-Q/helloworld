package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 额度合同担保信息列表页面查询
 * @author QZCB
 */
public class QueryCtrLimitGuarListOp extends CMISOperation {

	/**
	 * 通过业务流水号查询业务流水号下面所配置的合同占用授信明细，
	 * 查询合同占用授信明细表下面的担保合同信息，并且以列表形式显示
	 * 注：此处需要调到授信模块接口与担保模块接口
	 * */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		DataSource dataSource = null;
		try {
			connection = this.getConnection(context);
			dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String serno = null;
			String cont_no = null;
			String limitCodeStr = "";
			String connditionStr = "";
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			if(cont_no == null || cont_no.trim().length() == 0){
				if(context.containsKey("serno")){
					serno = (String)context.getDataValue("serno");
					connditionStr = " where limit_serno = '"+serno+"'";
				}
			}else {
				connditionStr = " where limit_cont_no = '"+cont_no+"'";
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			/** 通过业务流水号查询合同占用授信表ctr_limit_lmt_rel中的配置数据 */
			
			
			IndexedCollection relIColl = dao.queryList("CtrLimitLmtRel", null, connditionStr, connection);
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)relIColl.get(i);
					String lmtCodeNo = (String)kc.getDataValue("lmt_code_no");
					limitCodeStr += ("'"+lmtCodeNo+"',");
				}
				limitCodeStr = limitCodeStr.substring(0, limitCodeStr.length()-1);
			}
			if(limitCodeStr == null || limitCodeStr.trim().length() == 0){
				limitCodeStr = "''";
			}
			
			PageInfo pageInfo = new PageInfo();
			
			/** 调用授信模块提供的接口查询额度品种所挂接的担保信息 */
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			String guarNoList = lmtServiceInterface.queryGuarNoListByLimiCodeList(limitCodeStr, pageInfo, dataSource);
			if(guarNoList == null || guarNoList.trim().length() == 0){
				guarNoList = "''";
			}
			
			/** 调用担保模块提供的接口查询出担保模块的详细信息 */
			GrtServiceInterface grtServiceInterface = (GrtServiceInterface)serviceJndi.getModualServiceById("grtServices", "grt");
			IndexedCollection ybIColl = grtServiceInterface.getGrtListByGuarNoListAndGuarWay(guarNoList, "00", pageInfo, dataSource);
			ybIColl.setName("GrtLoanRGurListYb");
			this.putDataElement2Context(ybIColl, context);
			IndexedCollection zgIColl = grtServiceInterface.getGrtListByGuarNoListAndGuarWay(guarNoList, "01", null, dataSource);
			ybIColl.setName("GrtLoanRGurListZge");
			this.putDataElement2Context(zgIColl, context);
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
