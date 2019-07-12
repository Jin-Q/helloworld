package com.yucheng.cmis.biz01line.lmt.dao.intbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtIntbankDao extends CMISDao {
	
     /**
      * 将批量客户管理中选取的客户插入关联表中
      * @param map 插入表中的值
      * @return count 结果 (1 成功)
      * @throws EMPException 
      * */
	  public void insertcus2batchlist (Map<String,String> map) throws EMPException{
		  try{
			  int count =SqlClient.insert("InsertCus2BatchList", map, this.getConnection());
			  if(count !=1){
				  throw new EMPException("插入失败，请检查数据是否正确！");
			  }
		  }catch(Exception e){
			  throw new EMPException("插入数据失败！"+e.getMessage());
		  }
		  
	  }
	  /**
	   * 根据批量客户编号查询该批量客户包中所有的客户信息，
	   * @param batchNo 批量客户编号
	   * @return iColl  根据批量客户编号获取的数据
	   * @throws EMPException 
	   * 
	   * */
	  public IndexedCollection QueryCusInfoList(String batchNo) throws EMPException{
		  Connection connection = null;
		  IndexedCollection iColl = new IndexedCollection();
		  try{
			  connection = this.getConnection();			 
			  iColl = (IndexedCollection)SqlClient.queryList4IColl("QueryCusInfo", batchNo, connection);
			  
		  }catch(Exception e){
			  throw new EMPException("获取数据失败！"+e.getMessage());			  
		  }		  
		return iColl;
	  }
	  /**
	   * 单笔授信将一个kColl中数据逐条插入另外一个kColl中，用于授信生成台帐
	   * @param kColl 提交的单笔授信数据
	   * @return kColl 生成的相应的台帐记录
	   * @throws EMPException 
	   * 
	   * */
	  public KeyedCollection insert2kColl(KeyedCollection kColl) throws EMPException{		  
		  KeyedCollection kc = new KeyedCollection("LmtIntbankAcc");
		  TableModelDAO dao=this.getTableModelDAO(this.getContext());
			Connection connection =  this.getConnection();
			Context context= this.getContext();
			String serno_value = null;
			try{
				serno_value = (String)kColl.getDataValue("serno");
				String cus_id = (String)kColl.getDataValue("cus_id");//客户编号
				String Lmt_cur_type = (String)kColl.getDataValue("cur_type");//授信币种
				String Lmt_amt = (String)kColl.getDataValue("lmt_amt");//授信总额
				String manager_id = (String)kColl.getDataValue("manager_id");//责任人
				String manager_br_id= (String)kColl.getDataValue("manager_br_id");//管理机构
				String term_type = (String)kColl.getDataValue("term_type");//期限类型
				String term = (String)kColl.getDataValue("term");//期限
				String agr_no = (String)kColl.getDataValue("agr_no");//协议编号
				String limit_type = (String)kColl.getDataValue("limit_type");//额度类型
				term = term.trim();				
				//确定授信起始日和到期日
				String start_date = (String)context.getDataValue(PUBConstant.OPENDAY);
				String end_date = LmtUtils.computeEndDate(start_date, term_type, term);//到期日				
				IndexedCollection iColl = dao.queryList("LmtIntbankAcc", "where cus_id='"+cus_id+"'", connection);
				
				if(iColl.size()>0){
					kc = (KeyedCollection)iColl.get(0);	
				    kc.setDataValue("cur_type", Lmt_cur_type);
				    kc.setDataValue("lmt_amt", Lmt_amt);
				    kc.setDataValue("start_date", start_date);
				    kc.setDataValue("end_date", end_date);
				    kc.setDataValue("manager_id", manager_id);
				    kc.setDataValue("manager_br_id", manager_br_id);
				    kc.setDataValue("lmt_status", "10");
				    kc.setDataValue("limit_type", limit_type);
					dao.update(kc, connection);					
				}else{										
					kc.addDataField("agr_no", agr_no);
					kc.addDataField("serno", serno_value);
					kc.addDataField("batch_cus_no", "");
					kc.addDataField("cus_id", cus_id);
					kc.addDataField("cur_type", Lmt_cur_type);
					kc.addDataField("lmt_amt", Lmt_amt);
					kc.addDataField("start_date", start_date);
					kc.addDataField("end_date", end_date);
					kc.addDataField("manager_id", manager_id);
					kc.addDataField("manager_br_id", manager_br_id);
					kc.addDataField("lmt_status", "10");
					kc.addDataField("limit_type", limit_type);
					dao.insert(kc, connection);
				}				
			}catch(Exception e){
				throw new EMPException("生成台帐失败！"+e.getMessage());
			}finally{
				if (connection != null)
					this.releaseConnection(context, connection);
			}		  
		  return kc;		  
	  }
	  /**
	   * 批量授信将一个kColl中数据逐条插入另外一个kColl中，
	   * 用于批量客户授信生成协议
	   * @param kColl 提交记录的数据
	   * @return kColl 协议数据
	   * @throws EMPException 
	   * 
	   * */
	  public KeyedCollection insert2kColl4Agr(KeyedCollection kColl) throws EMPException{		  
		  KeyedCollection kc = new KeyedCollection("LmtBatchLmtAgr");
			Connection connection =  this.getConnection();
			Context context = this.getContext();
			try{
				String serno = (String)kColl.getDataValue("serno");//授信协议编号
				String Batch_cus_no = (String)kColl.getDataValue("batch_cus_no");//批量客户编号				
				String Lmt_amt = (String)kColl.getDataValue("lmt_totl_amt");//授信总额
				String manager_id = (String)kColl.getDataValue("manager_id");//责任人
				String manager_br_id= (String)kColl.getDataValue("manager_br_id");//管理机构
				String term_type = (String)kColl.getDataValue("term_type");//期限类型
				String term = (String)kColl.getDataValue("term");//期限
				String memo = (String)kColl.getDataValue("memo");//备注
				String app_type = (String)kColl.getDataValue("app_type");//申请类型
				String app_cls = (String)kColl.getDataValue("app_cls");//申请类别
				String limit_type=(String)kColl.getDataValue("limit_type");
				String single_quota = (String)kColl.getDataValue("single_quota");//单户限额
				term = term.trim();
				
				//确定授信起始日和到期日
				String start_date = (String)context.getDataValue(PUBConstant.OPENDAY);
				String end_date = LmtUtils.computeEndDate(start_date, term_type, term);//到期日
				
				kc.put("serno", serno);
				kc.put("batch_cus_no", Batch_cus_no);
				kc.put("lmt_totl_amt", Lmt_amt);
				kc.put("start_date", start_date);
				kc.put("end_date", end_date);
				kc.put("manager_id", manager_id);
				kc.put("manager_br_id", manager_br_id);
				kc.put("limit_type", limit_type);
				kc.put("app_type", app_type);
				kc.put("memo", memo);
				kc.put("app_cls", app_cls);
				kc.put("single_quota", single_quota);
				
				
			}catch(Exception e){
				throw new EMPException("授信协议生成失败！"+e.getMessage());
			}finally{
				if (connection != null)
					this.releaseConnection(context, connection);
			}		  
		  return kc;		  
	  }
	  /**
	   * 批量授信将一个kColl中数据逐条插入另外一个kColl中，
	   * 用于批量客户授信提交流程生成台帐
	   * @param kColl 提交后的数据
	   * @return kColl 产生的台帐数据
	 * @throws EMPException 
	   * 
	   * */
	  public KeyedCollection insert2kColl4Acc (KeyedCollection kColl) throws EMPException{	
		    Connection connection=this.getConnection();
		    Context  context =this.getContext();
		    KeyedCollection kc = new KeyedCollection("LmtIntbankAcc");
		    TableModelDAO dao = this.getTableModelDAO(context);			
			try{
				String serno = (String)kColl.getDataValue("serno");
				String Batch_cus_no = (String)kColl.getDataValue("batch_cus_no");//批量客户编号
				//String Cus_id = (String)kColl.getDataValue("cus_id");//客户编号
				String Lmt_cur_type = (String)kColl.getDataValue("cur_type");//授信币种
//				String Lmt_amt = (String)kColl.getDataValue("lmt_totl_amt");//授信总额
				String manager_id = (String)kColl.getDataValue("manager_id");//责任人
				String manager_br_id= (String)kColl.getDataValue("manager_br_id");//管理机构
				String term_type = (String)kColl.getDataValue("term_type");//期限类型
				String term = (String)kColl.getDataValue("term");//期限
//				String agr_no = (String)kColl.getDataValue("agr_no");//协议编号
				String limit_type = (String)kColl.getDataValue("limit_type");//额度类型
				term = term.trim();				
				//确定授信起始日和到期日
				String start_date = (String)context.getDataValue(PUBConstant.OPENDAY);
				String end_date = LmtUtils.computeEndDate(start_date, term_type, term);//到期日
				IndexedCollection iColl_corre = dao.queryList("LmtBatchCorre", "where batch_cus_no='"+Batch_cus_no+"'", connection);
				KeyedCollection kColl_corre = new KeyedCollection("LmtBatchCorre");
				for(int i=0;i<iColl_corre.size();i++){
					kColl_corre = (KeyedCollection)iColl_corre.get(i);
					String cus_id = (String)kColl_corre.getDataValue("cus_id");
					IndexedCollection iColl_lmt = dao.queryList("LmtSigLmt", "where cus_id='"+cus_id+"' and app_cls='02' and batch_serno='"+serno+"'", connection);
					KeyedCollection kColl_lmt =(KeyedCollection)iColl_lmt.get(0);
					String sernoSig=kColl_lmt.getDataValue("serno").toString();
					String lmt_amt_lmt = kColl_lmt.getDataValue("lmt_amt").toString();//单笔授信的授信金额
					//取出这笔授信的授信分项,将数据直接插入授信使用明细中
					IndexedCollection SubiColl = dao.queryList("LmtSubApp", "where serno='"+sernoSig+"'", connection);					
					for(int m=0;m<SubiColl.size();m++){
						KeyedCollection SubkColl=(KeyedCollection)SubiColl.get(m);
						String variet_no = SubkColl.getDataValue("variet_no").toString();
						IndexedCollection iColl4detail = dao.queryList("LmtIntbankDetail", "where cus_id='"+cus_id+"' and variet_no ='"+variet_no+"'", connection);
						if(iColl4detail.size()>0){
							SubkColl.remove("serno");
							SubkColl.put("cus_id", cus_id);
							SubkColl.setName("LmtIntbankDetail");
							dao.update(SubkColl, connection);
							SubkColl.remove("cus_id");
						}else{
							SubkColl.remove("serno");
							SubkColl.put("cus_id", cus_id);
							SubkColl.setName("LmtIntbankDetail");
							dao.insert(SubkColl, connection);
							SubkColl.remove("cus_id");
						}
					}
					//获取该客户在台帐中的数据。
					IndexedCollection iColl = dao.queryList("LmtIntbankAcc", "where cus_id='"+cus_id+"'", connection);
					if(iColl.size()>0){
						KeyedCollection kColl_acc = (KeyedCollection)iColl.get(0);	
						kColl_acc.put("serno", sernoSig);
						kColl_acc.put("cur_type", Lmt_cur_type);
						kColl_acc.put("batch_cus_no", Batch_cus_no);
						kColl_acc.put("lmt_amt", lmt_amt_lmt);
						kColl_acc.put("start_date", start_date);
						kColl_acc.put("end_date", end_date);
						kColl_acc.put("manager_id", manager_id);
						kColl_acc.put("manager_br_id", manager_br_id);
						kColl_acc.put("lmt_status", "10");
						kColl_acc.put("limit_type", limit_type);
						dao.update(kColl_acc, connection);
					}else{
						String agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);//生成新的协议编号
						kc.put("serno", sernoSig);
						kc.put("agr_no", agr_no);
						kc.put("batch_cus_no", Batch_cus_no);
						kc.put("cus_id", cus_id);
						kc.put("cur_type", Lmt_cur_type);
						kc.put("lmt_amt", lmt_amt_lmt);
						kc.put("start_date", start_date);
						kc.put("end_date", end_date);
						kc.put("manager_id", manager_id);
						kc.put("manager_br_id", manager_br_id);
						kc.put("lmt_status", "10");
						kc.put("limit_type", limit_type);
						dao.insert(kc, connection);
					}
				}
			}catch(Exception e){
				throw new EMPException("批量客户生成台帐失败！"+e.getMessage());
			}finally{
				if (connection != null)
					this.releaseConnection(context, connection);
			}		  
		  return kc;		  
	  }
	  /**
	   * 计算授信后，台帐中可用余额
	   * @param kColl 进行操作的台帐数据
	   * @return odd_amt 可用余额
	   * @throws EMPException 
	   * */
	  public double getOddAmt(KeyedCollection kColl) throws EMPException{
		  Connection connection = this.getConnection();
		  Context context =this.getContext();
		   double odd_amt =0;
		  try{
			  String froze_lmt = (String)kColl.getDataValue("froze_amt");
			  String total_lmt = (String)kColl.getDataValue("lmt_amt");
			  double  froze =0;
			  double  total = Double.parseDouble(total_lmt);
			  if(froze_lmt!=null){
				  froze = Double.parseDouble(froze_lmt);//得到冻结额度
				  odd_amt = new BigDecimal(total - froze).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			  }else{
				  odd_amt = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
			  }
			  			 			  
		  }catch(Exception e){
				throw new EMPException("获取可用余额失败！"+e.getMessage());
			}finally{
				if (connection != null)
					this.releaseConnection(context, connection);
			}
		  return odd_amt;
	  }
	  /**
	   * 在删除批量客户授信后，删除批量包中单笔授信
	   * @param serno 删除数据的流水号
	   * @return void 
	   * @throws EMPException 
	   * */
	  
	  public void  DeleteLmtSub(String serno) throws EMPException{
		  IndexedCollection iColl = new IndexedCollection();
		  Connection connection  = this.getConnection();
		  Context context = this.getContext();
		  try{			  
			  TableModelDAO  dao = this.getTableModelDAO(context);
			  KeyedCollection kColl_batch =dao.queryAllDetail("LmtBatchLmt", serno, connection);
			  String batch_cus_no = (String)kColl_batch.getDataValue("batch_cus_no");
			  iColl = dao.queryList("LmtBatchCorre", "where batch_cus_no='"+batch_cus_no+"'", connection);
			  KeyedCollection kColl = new KeyedCollection();
			  for(int i=0;i<iColl.size();i++){
				  kColl=(KeyedCollection)iColl.get(i);
				  String cus_id = (String)kColl.getDataValue("cus_id");
				  IndexedCollection iColl_sig = dao.queryList("LmtSigLmt", "where cus_id='"+cus_id+"'"+"and app_cls='"+"02"+"'", connection);
				  String serno_sig = null;
				  LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				  for(int j=0;j<iColl_sig.size();j++){
					  KeyedCollection kColl_sig =(KeyedCollection)iColl_sig.get(j);
					  serno_sig = (String)kColl_sig.getDataValue("serno");
					     //删除授信分项中数据
					  Map<String,String> refFields1 = new HashMap<String,String>();
					  refFields1.put("serno", serno_sig);
					  lmtComponent.deleteByField("LmtSubApp", refFields1);
				  }				  
				  //删除批量包中客户的授信
				 Map<String,String> refFields = new HashMap<String,String>();
			     refFields.put("cus_id", cus_id);
			     refFields.put("app_cls", "02");			 
				 lmtComponent.deleteByField("LmtSigLmt", refFields);			 				  				  
			  }			  
		  }catch(Exception e){
			  throw new EMPException("删除相关单笔授信失败！"+e.getMessage());		  
		  }finally{
			  if (connection != null)
					this.releaseConnection(getContext(), connection);		 
	  }
  }
	  /**
	   * 批量客户单笔授信更新时，删除其授信分项信息
	   * @param iColl 更新的单笔授信数据 
	   * @throws EMPException 
	   * 
	   * */
	  public void updateSub(IndexedCollection iColl) throws EMPException{
		  KeyedCollection kColl = null;
		  Connection connection = this.getConnection();
		  Context context = this.getContext();
		    try{
		    	for(int i=0;i<iColl.size();i++){
		    		kColl = (KeyedCollection)iColl.get(i);
		    	}
		    	String serno = (String)kColl.getDataValue("serno");
		    	Map<String,String> refFields = new HashMap<String,String>();
		        refFields.put("serno", serno);		             
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
	            lmtComponent.deleteByField("LmtSubApp", refFields);		    	
		    }catch(Exception e){
		    	throw new EMPException("删除授信分项失败！"+e.getMessage());
		    }finally{
				  if (connection != null)
						this.releaseConnection(getContext(), connection);		 
		  }
	  }
	  
	  /**
	   * 更改批量包的状态为已授信
	   * @param  batch_cus_no 
	   * @throws EMPException 
	   * 
	   * */
	  public void updateStatus(String batch_cus_no) throws EMPException{
		  KeyedCollection kColl =null;
		  Connection connection = this.getConnection();
		  Context context = this.getContext();
		  TableModelDAO dao = this.getTableModelDAO(context);
		  try{
			  String condition = "where batch_cus_no='"+batch_cus_no+"'";
			  kColl=dao.queryFirst("LmtIntbankBatchList",null, condition, connection);
			  String status = (String)kColl.getDataValue("status");
			  if(status.equals("01")){
				  kColl.setDataValue("status", "02");
			  }else{
				  kColl.setDataValue("status", "01");
			  }	  
			  dao.update(kColl, connection);
		  }catch(Exception e){
			  throw new EMPException("更改批量包状态失败！"+e.getMessage());
		    }finally{
				  if (connection != null)
						this.releaseConnection(getContext(), connection);		 
		  }
	  }


	private void releaseConnection(Context context, Connection connection) {
		// TODO Auto-generated method stub
		
	}
	protected TableModelDAO getTableModelDAO(Context context) throws EMPException{
		return this.getTableModelDAO(CMISConstance.ATTR_TABLEMODELDAO, context);
	}
	protected TableModelDAO getTableModelDAO(String modelId, Context context) throws EMPException{
		TableModelDAO dao = (TableModelDAO)context.getService(modelId);
		return dao;
	}

}
