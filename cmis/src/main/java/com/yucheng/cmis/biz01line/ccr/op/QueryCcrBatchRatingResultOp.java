package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppAll;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.exception.AgentException;

public class QueryCcrBatchRatingResultOp extends CMISOperation {
	

	private final String serno_name = "serno";
	
	
	@Override

	public String doExecute(Context context) throws EMPException {
		ComponentHelper cHelper =new ComponentHelper();
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			
			String serno_value = null;
			String model_no = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {
				throw new EMPException("找不到业务编码\n"+e);
			}			
			try {
				model_no = (String)context.getDataValue("model_no");
			} catch (Exception e) {
				throw new EMPException("找不到模型编码\n"+e);
			}
			
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			
			String currentUserId = (String) context.getDataValue("currentUserId");
			//构建业务处理类
			try{
				
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			CcrAppAll ccrAppAll = ccrComponent.loadScore(serno_value, currentUserId,model_no);
			//CcrAppInfo ccrAppInfo = ccrAppAll.getCcrAppInfo();
		//	CcrAppDetail ccrAppDetail = ccrAppAll.getCcrAppDetail();
//			ArrayList ccrMGroupScoreList = ccrAppAll.getCcrMGroupScoreList();
			ArrayList ccrGIndScoreList =  ccrAppAll.getCcrGIndScoreList();
			
			/**
			 *将指标的得分保存入 id 为 组名的kcoll中。
			 *格式为
			 *	kcoll       (-id:组名)
			 *		-指标名	:	指标选项
			 *		-指标名2	：	指标选项2
			 *		-指标名3	：	指标选项3
			 * 		-指标名_score:	指标得分
			 * 		-指标名2_score:	指标得分2
			 * 		-指标名3_score:	指标得分3
			 * 		-指标名_orgVal: 指标原始值
			 * 		-指标名2_orgVal: 指标原始值2
			 * 		-指标名3_orgVal: 指标原始值3
			 * 下方已弃用
			 * 		-指标名_readonly:	指标是否只读
			 * 		-指标名2_readonly:	指标2是否只读
			 * 		-指标名3_readonly:	指标3是否只读
			 *
			 */
			Iterator indScoreIter = ccrGIndScoreList.iterator();
			String groupNoOld="";
			KeyedCollection groupKcoll = null;//组kcoll 第一次循环时实例化
			while (indScoreIter.hasNext()){
				CcrGIndScore ccrGIndScore=(CcrGIndScore)indScoreIter.next();
				String groupNo = ccrGIndScore.getGroupNo();
				String indexScore = ccrGIndScore.getIndexScore();
				//String indReadonly = ccrGIndScore.getGroupName();//用GroupName来保存是否只读。
				String indexNo = ccrGIndScore.getIndexNo();
				String indexValue = ccrGIndScore.getIndexValue();
				String indOrgVal = ccrGIndScore.getIndOrgVal();				
				if(!groupNo.equals(groupNoOld)){
					if(groupKcoll!=null){
						context.addDataElement(groupKcoll);//当组名称发生变化将老的组存入context,并实例化新的groupKcoll
					}
					groupKcoll=new KeyedCollection();
					groupKcoll.setId(groupNo);
					groupNoOld=groupNo;//将当前新的groupNo存入old用作比较
				}
				groupKcoll.addDataField(indexNo, indexValue);
				groupKcoll.addDataField(indexNo+"_score", indexScore);
				groupKcoll.addDataField(indexNo+"_orgVal", indOrgVal);
				//groupKcoll.addDataField(indexNo+"_readonly", indReadonly);
				
				
			}
			context.addDataElement(groupKcoll);//把最后一个kcoll加入context
		//	KeyedCollection ccrAppDetailKcoll = cHelper.domain2kcol(ccrAppDetail, CcrPubConstant.CCR_APPDETAIL);
			//需要时放开,将ccrMGroupScoreIcoll 转换格式后放入context
			//IndexedCollection ccrMGroupScoreIcoll =(IndexedCollection)cHelper.domain2icol(ccrMGroupScoreList, CcrPubConstant.CCR_MGROUPSCORE);
			//@todo 没制作转换格式部分
			//this.putDataElement2Context(ccrMGroupScoreIcoll, context);
		//	this.putDataElement2Context(ccrAppDetailKcoll, context);
			}catch(AgentException e){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
				throw new CMISException(e);
			}
			
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
