package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppAll;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.domain.CcrGIndScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrMGroupScore;
import com.yucheng.cmis.biz01line.ccr.domain.CcrModelScore;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.util.CMISJSONUtil;

public class CreditRatingOperation extends CMISOperation {

	private static String dataName = "ccrScore";
	@Override
	public String doExecute(Context context) throws EMPException {
			Connection connection = null;
			try{
				connection = this.getConnection(context);
				ComponentHelper cHelper =new ComponentHelper();
				CcrAppAll ccrAppAll = new CcrAppAll();
				CcrAppInfo ccrAppInfo = new CcrAppInfo();
				CcrAppDetail ccrAppDetail = new CcrAppDetail();
				CcrModelScore ccrModelScore =new CcrModelScore();
				String scoringManager=(String)context.getDataValue("currentUserId");
				String serno = (String)context.getDataValue("serno");
				String modelNo=(String)context.getDataValue("model_no");
				String cusId = (String)context.getDataValue("cus_id");
				String fnc_year = (String)context.getDataValue("fnc_year");
				String fnc_month = (String)context.getDataValue("fnc_month");//会计月份
				String stat_prd_style = (String)context.getDataValue("stat_prd_style");//报表周期类型
				ccrAppInfo.setSerno(serno); 
				ccrAppInfo.setCusId(cusId); 
				ccrAppDetail.setSerno(serno); 
				ccrAppDetail.setModelNo(modelNo);  //将模型ID放到评级申请附表domain中
				ccrAppDetail.setFncYear(fnc_year);
				ccrAppDetail.setFncMonth(fnc_month);
				ccrAppDetail.setStatPrdStyle(stat_prd_style);
				ccrModelScore.setSerno(serno);
				ccrModelScore.setCusId(cusId);
				ccrModelScore.setModelNo(modelNo);
				ccrModelScore.setScoringManager(scoringManager);

				ccrAppAll.setCcrAppInfo(ccrAppInfo); 
				ccrAppAll.setCcrAppDetail(ccrAppDetail); 
				ccrAppAll.setCcrModelScore(ccrModelScore);
				CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
				/**
				 * 为ccrAppAll中填入选项值
				 * step1 通过serno和 modelNo读取指标分组列表
				 * step2 通过分组ID,从context中取出同名Kcoll
				 * step3 从kcoll中取出所有的字段(一个字段对应一个指标)
				 * step4 将每个指标都存入(ArrayList)indList中
				 */
				ArrayList ccrMGroupScoreList = ccrComponent.loadCcrMGroupScore(serno, modelNo,cusId);//通过模型编号,获取该模型的所有组的得分.以列表返回
				ccrAppAll.setCcrMGroupScoreList(ccrMGroupScoreList);
				Iterator groupIter = ccrMGroupScoreList.iterator();
				ArrayList indList=new ArrayList();
				ArrayList indAllList=ccrComponent.loadCcrGIndScore(serno, cusId, modelNo);//通过传入的模型编号,返回该模型下的所有指标得分
				HashMap indOrgList=new HashMap();
				while (groupIter.hasNext()){
					CcrMGroupScore ccrMGroupScore=(CcrMGroupScore)groupIter.next();
					KeyedCollection groupKcoll=null;
					try{
						groupKcoll = (KeyedCollection)context.getDataElement(ccrMGroupScore.getGroupNo());
					}catch(Exception e){
						//如果是指标组全为可选项，且可选项都未选择重置指标组得分
						SqlClient.update("resetCcrMGroupScore", ccrMGroupScore, null, null, this.getConnection(context));
						continue;
					}
					
					Iterator indKcollIter =groupKcoll.keySet().iterator();
					while(indKcollIter.hasNext()){
						String indexNo=(String)indKcollIter.next();
//					if(indexNo.length()>=12&&"_orgVal".equals(indexNo.substring(12))){
//							//原始值或者其他什么东西
//							continue;
//						}else{
							//如果不是原始值,则将其存入得分数组
							CcrGIndScore indexScore = new CcrGIndScore();
							indexScore.setGroupNo(ccrMGroupScore.getGroupNo());
							indexScore.setIndexNo(indexNo);
							indexScore.setCusId(cusId);
							indexScore.setSerno(serno);
							indexScore.setGroupName(ccrMGroupScore.getGroupName());
							indexScore.setScoringManager(scoringManager);
							String orgVal="";
							try{
								orgVal = (String)groupKcoll.getDataValue(indexNo+"_orgVal");
								indexScore.setIndOrgVal(orgVal);
							}catch(Exception e){
								//没有原始值，就当没发生~
							}
							indexScore.setIndexValue((String)groupKcoll.getDataValue(indexNo));
							indList.add(indexScore);
						//}
					}
					
					
				}
				//取得缺失指标项
				for(int i=0;i<indList.size();i++){
					CcrGIndScore indScore=(CcrGIndScore) indList.get(i);
					for(int j=0;j<indAllList.size();j++){
						CcrGIndScore temp=(CcrGIndScore) indAllList.get(j);
						if(temp.getIndexNo().equals(indScore.getIndexNo())){
							indAllList.remove(j);
						}
					}
				}
				//重置缺失指标得分
				for(int i=0;i<indAllList.size();i++){
					CcrGIndScore temp=(CcrGIndScore) indAllList.get(i);
					SqlClient.update("resetCcrGIndScore", temp, null, null, this.getConnection(context));
				}
				
				ccrAppAll.setCcrGIndScoreList(indList);
				/**
				 * 调用component的保存+算分方法。
				 * 将得分放入ccrAppAll中,返回
				 */
				
				ccrComponent.saveAndRating(ccrAppAll);
				
				/**
				 * 下方代码是将 数据保存成json格式，并且保存入context中。
				 * 首先获取指标得分
				 */
				ccrMGroupScoreList = ccrAppAll.getCcrMGroupScoreList();
				ArrayList ccrGIndScoreList =  ccrAppAll.getCcrGIndScoreList();
				StringBuffer jsonBuf = new StringBuffer();
				/**
				 * 测试用json串
				 * jsonBuf.append("G20090300042:{id: 'G20090300042',fields: ['I20090300134','I20090300135_score','I20090300134_score','I20090300135'],I20090300134:'1',I20090300135_score:'100',I20090300134_score:'100',I20090300135:'1'},G20090300043:{id: 'G20090300043',fields: ['I20090300137_score','I20090300138','I20090300137','I20090300138_score'],I20090300137_score:'100',I20090300138:'1',I20090300137:'1',I20090300138_score:'100'},G20090300046:{id: 'G20090300046',fields: ['I20090300119_score','I20090300120_score','I20090300119','I20090300128_score','I20090300128','I20090300123_score','I20090300121_score','I20090300124_score','I20090300123','I20090300124','I20090300121','I20090300120'],I20090300119_score:'100',I20090300120_score:'100',I20090300119:'2',I20090300128_score:'100',I20090300128:'1',I20090300123_score:'100',I20090300121_score:'100',I20090300124_score:'100',I20090300123:'1',I20090300124:'1',I20090300121:'1',I20090300120:'10'},G20090300050:{id: 'G20090300050',fields: ['I20090300140_score','I20090300139_score','I20090300140','I20090300157_score','I20090300139','I20090300157'],I20090300140_score:'100',I20090300139_score:'100',I20090300140:'2',I20090300157_score:'100',I20090300139:'1',I20090300157:'1'},G20090300051:{id: 'G20090300051',fields: ['I20090300160','I20090300159_score','I20090300160_score','I20090300159','I20090300158','I20090300158_score'],I20090300160:'1',I20090300159_score:'100',I20090300160_score:'100',I20090300159:'1',I20090300158:'1',I20090300158_score:'100'},CcrAppInfo:{id: 'CcrAppInfo',fields: ['serno'],serno:'00000903102220000309'},CcrAppDetail:{id: 'CcrAppDetail',fields: ['model_no','auto_score','serno'],model_no:'M20090300040',auto_score:'50.3',serno:'00000903102220000309'}");
				 */
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
					String indexNo = ccrGIndScore.getIndexNo();//编号
					String indexValue = ccrGIndScore.getIndexValue();//指标值
					//String indOrgVal = ccrGIndScore.getIndOrgVal();//原始值
					if(!groupNo.equals(groupNoOld)){
						if(groupKcoll!=null){
							//context.addDataElement(groupKcoll);//当组名称发生变化将老的组存入context,并实例化新的groupKcoll
							/**
							 * 将此代码修改为保存成json串
							 */
							this.getKeyedCollectionStr(groupKcoll, groupKcoll, false, jsonBuf);
							jsonBuf.append(",");
						}
						groupKcoll=new KeyedCollection();
						groupKcoll.setId(groupNo);
						groupNoOld=groupNo;//将当前新的groupNo存入old用作比较
					}
					groupKcoll.addDataField(indexNo, indexValue);
					groupKcoll.addDataField(indexNo+"_score", indexScore);
					//groupKcoll.addDataField(indexNo+"_orgVal", indOrgVal);
					//groupKcoll.addDataField(indexNo+"_readonly", indReadonly);
				}
				this.getKeyedCollectionStr(groupKcoll, groupKcoll, false, jsonBuf);
				jsonBuf.append(",");
				//context.addDataElement(groupKcoll);//把最后一个kcoll加入context
				KeyedCollection ccrAppInfoKcoll = cHelper.domain2kcol(ccrAppInfo, CcrPubConstant.CCR_APPINFO);
				KeyedCollection ccrAppDetailKcoll = cHelper.domain2kcol(ccrAppDetail, CcrPubConstant.CCR_APPDETAIL);
				//需要时放开,将ccrMGroupScoreIcoll 转换格式后放入context
				IndexedCollection ccrMGroupScoreIcoll =(IndexedCollection)cHelper.domain2icol(ccrMGroupScoreList, CcrPubConstant.CCR_MGROUPSCORE);
				//@todo 没制作转换格式部分
				this.putDataElement2Context(ccrMGroupScoreIcoll, context);
				
				this.getKeyedCollectionStr(ccrAppInfoKcoll, ccrAppInfoKcoll, false, jsonBuf);
				jsonBuf.append(",");
				this.getKeyedCollectionStr(ccrAppDetailKcoll, ccrAppDetailKcoll, false, jsonBuf);
				//System.out.println("标注："+ccrAppDetailKcoll.toJSon());
				String jsonStr = jsonBuf.toString();
				System.out.println(jsonStr);
				context.addDataField(dataName, jsonStr);
				}catch(AgentException e){
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
					if(context.containsKey(dataName)){
						context.setDataValue(dataName, "errMsg:"+e.getMessage());
					}else{
						context.addDataField(dataName, "errMsg:"+e.getMessage());
					}
					throw e;
				} catch (Exception e) {
					EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, e.toString());
					if(context.containsKey(dataName)){
						context.setDataValue(dataName, "errMsg:"+e.getMessage());
					}else{
						context.addDataField(dataName, "errMsg:"+e.getMessage());
					}
					e.printStackTrace();
				}finally{
					if (connection != null)
					this.releaseConnection(context, connection);
				}
			
			return "0";
		}
	
	private void getKeyedCollectionStr(KeyedCollection outputKColl, KeyedCollection sKColl, boolean isArray, StringBuffer buffer) throws Exception{
		if(sKColl.getName() != null && sKColl.getName().length() > 0 && !isArray)
			buffer.append(sKColl.getName()+":{");
		else
			buffer.append("{");
		
		if(outputKColl.size() == 0)
			outputKColl = sKColl;
		Object[] names = outputKColl.keySet().toArray();
			buffer.append("id: '"+sKColl.getName()+"',");
			buffer.append("fields: [");
			for (int k = 0; k < names.length-1; k++) {
				buffer.append("'"+names[k].toString()+"',");
			}
			buffer.append("'"+names[names.length-1].toString()+"'],");
		for (int k = 0; k < names.length; k++) {
			String name = names[k].toString();
			DataElement element = outputKColl.getDataElement(name);
			if(element instanceof DataField){
				DataField field = (DataField)sKColl.getDataElement(name);
				this.getDataFieldStr(field, buffer);
			}else if(element instanceof IndexedCollection){
				IndexedCollection iColl = (IndexedCollection)sKColl.getDataElement(name);
				this.getIndexedCollectionStr((IndexedCollection)element, iColl, buffer);
			}else if(element instanceof KeyedCollection){
				KeyedCollection kColl = (KeyedCollection)sKColl.getDataElement(name);
				this.getKeyedCollectionStr((KeyedCollection)element, kColl, false, buffer);
			}
			if (k < names.length - 1)
				buffer.append(",");
		}
		buffer.append("}");
	}
	
	private void getDataFieldStr(DataField field, StringBuffer buffer) throws Exception{
		buffer.append(field.getName()+":");
		Object value = field.getValue();
		if(value != null)
			buffer.append("'"+CMISJSONUtil.normalizeString(value.toString())+"'");
		else
			buffer.append("null");
	}
	private void getIndexedCollectionStr(IndexedCollection outputIColl, IndexedCollection iColl, StringBuffer buffer) throws Exception{
		
		buffer.append(iColl.getName()+":[");
		KeyedCollection outputKColl = (KeyedCollection)outputIColl.getDataElement();
		if(outputKColl == null)
			outputKColl = (KeyedCollection)iColl.getDataElement();
		if (iColl != null)
			for (int i = 0; i < iColl.size(); i++) {
				KeyedCollection sKColl = (KeyedCollection) iColl.get(i);
				
				this.getKeyedCollectionStr(outputKColl, sKColl, true, buffer);
				
				if (i < iColl.size() - 1)
					buffer.append(",");
			}
		buffer.append("]");
	}
	
	}