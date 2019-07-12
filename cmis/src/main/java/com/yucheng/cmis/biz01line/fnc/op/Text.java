package com.yucheng.cmis.biz01line.fnc.op;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
public class Text extends CMISOperation{
	private final String modelId = "IqpMeFncBs";

	@Override
	public String doExecute(Context context) throws EMPException {
		KeyedCollection kColl = null;
		 String tempFileName = null;
	     Connection connection = null;
	     String flagInfo = CMISMessage.DEFEAT;
	     KeyedCollection kCol = null;//选择要导入的财务信息基本信息
	     try {
	            connection = this.getConnection(context);
	            
	            
	          
	            //读取xls文件路径
	            String tempPath =(String)context.getDataValue("DocBasicinfo__file_path");
	            String tempName =(String)context.getDataValue("DocBasicinfo__file_name");
	            ResourceBundle res = ResourceBundle.getBundle("cmis");
	            String tempFileRootPath = res.getString("tempFileRootPath");
	            
	            tempFileName = tempFileRootPath + tempPath + tempName;
	            
	     }catch (EMPException ee) {
	            throw ee;
	        } 
	    
	     /**
	      * 开始处理数据导入
	      */
	     kColl = (KeyedCollection)context.getDataElement(modelId);
	     TableModelDAO dao = this.getTableModelDAO(context);
	     IndexedCollection iColl = new IndexedCollection();
         try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(tempFileName));
			
			  for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) { 
				  if (null != workbook.getSheetAt(numSheets)||!workbook.getSheetAt(numSheets).equals("")) { 
					HSSFSheet Sheet = workbook.getSheetAt(numSheets);
				       ///现金和银行账款
				      int a = 0;
					   for(int i =7 ;i<=Sheet.getLastRowNum();i++){
						   if(Sheet.getCellComment(1, i).toString().equals(null)||Sheet.getCellComment(1, i).toString().equals(""))
						   {
							   a=i;
							   break;
						   }
						   if(!Sheet.getCellComment(1, i).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, i).toString());
							   kColl.put("remark", Sheet.getCellComment(2, i).toString());
							   kColl.put("amt", Sheet.getCellComment(3, i).toString());
							   kColl.put("fnc_flag", "160");
							   kColl.put("fnc_tpe", 150);
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   a =i;
						   break;
					   }
					   int b =0;
					    b = a+3;
					    int d=0;
					   //应收账款
					   for(int c=b;c<=Sheet.getLastRowNum();b++){
						   if(Sheet.getCellComment(1, c).toString().equals(null)||Sheet.getCellComment(1, c).toString().equals(""))
						   {
							   d=c;
							   break;
						   }
						   if(!Sheet.getCellComment(1, c).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, c).toString());
							   kColl.put("begin_date", Sheet.getCellComment(2, c).toString());
							   kColl.put("end_date", Sheet.getCellComment(3, c).toString());
							   kColl.put("amt", Sheet.getCellComment(4,c).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "161");
							
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   d =c;
						   break;
					   }
					   //预付账款
					   int f = 0;
					   for(int e =d+3;e<=Sheet.getLastRowNum();e++){
						   if(Sheet.getCellComment(1, e).toString().equals(null)||Sheet.getCellComment(1, e).toString().equals(""))
						   {
							   e=f;
							   break;
						   }
						   if(!Sheet.getCellComment(1, e).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, e).toString());
							   kColl.put("remark", Sheet.getCellComment(2, e).toString());
							   kColl.put("amt", Sheet.getCellComment(3, e).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "162");
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   d =f;
						   break;
					   }
					   //存货
					   int h = 0;
					   for(int g=f+3;g<=Sheet.getLastRowNum();g++){
						   if(Sheet.getCellComment(1, g).toString().equals(null)||Sheet.getCellComment(1, g).toString().equals(""))
						   {
							   g=h;
							   break;
						   }
						   if(!Sheet.getCellComment(1, g).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, g).toString());
							   kColl.put("num", Sheet.getCellComment(2, g).toString());
							   kColl.put("amt", Sheet.getCellComment(3, g).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "163");
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   h =g;
						   break; 
					   }
					   //固定资产
					   int l=0;
					   for(int m=h+3;m<=Sheet.getLastRowNum();m++){
						   //数据库字段没有定义
						   if(Sheet.getCellComment(1, m).toString().equals(null)||Sheet.getCellComment(1,m).toString().equals(""))
						   {
							   l=m;
							   break;
						   }
						   if(!Sheet.getCellComment(1, m).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, m).toString());
							   kColl.put("num", Sheet.getCellComment(2, m).toString());
							   kColl.put("amt", Sheet.getCellComment(3, m).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "164");
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   l =m;
						   break;  
						   
					   }
					   //其他资产
					   int n =0;
					   for(int p=l+3;p<=Sheet.getLastRowNum();p++){
						   if(Sheet.getCellComment(1,p).toString().equals(null)||Sheet.getCellComment(1,p).toString().equals(""))
						   {
							   n=p;
							   break;
						   }
						   if(!Sheet.getCellComment(1,n).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1,p).toString());
							   kColl.put("begin_date", Sheet.getCellComment(2,p).toString());
							   kColl.put("end_date", Sheet.getCellComment(3,p).toString());
							   kColl.put("amt", Sheet.getCellComment(4,p).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "165");
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   n =p;
						   break;
					   }
					   //表外资产
					   int q = 0;
					   for(int r=n+3;r<=Sheet.getLastRowNum();r++){
						   if(Sheet.getCellComment(1,r).toString().equals(null)||Sheet.getCellComment(1,r).toString().equals(""))
						   {
							   q=r;
							   break;
						   }
						   if(!Sheet.getCellComment(1,r).toString().equals("总计：")){
							   kColl.put("pro_num", Sheet.getCellComment(1, r).toString());
							   kColl.put("remark", Sheet.getCellComment(2, r).toString());
							   kColl.put("amt", Sheet.getCellComment(3, r).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "166");
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
						   }
						   q=r;
						   break;
					   }
					  //财产情况
					   int s=0;
					   for(int t = q+4;t<=Sheet.getLastRowNum();t++){
						   if(Sheet.getCellComment(1,t).toString().equals(null)||Sheet.getCellComment(1,t).toString().equals(""))
						   {
							   s=t;
							   break;
						   }
						   if(!Sheet.getCellComment(1,t).toString().equals("车辆")){
							   kColl.put("code_num", Sheet.getCellComment(1,t).toString());
							   kColl.put("owner_name", Sheet.getCellComment(2,t).toString());
							   kColl.put("hou_num", Sheet.getCellComment(3,t).toString());
							   kColl.put("hou_addr", Sheet.getCellComment(4,t).toString());
							   kColl.put("hou_area", Sheet.getCellComment(5,t).toString());
							   kColl.put("fnc_tpe", 150);
							   kColl.put("fnc_flag", "167");
							   System.out.println(iColl.add(kColl));
						   }
						   s=t;
						   break;					   
					   }
						int u = 0;
						for(int v =s+2;v<=Sheet.getLastRowNum();v++){
							 if(Sheet.getCellComment(1,v).toString().equals(null)||Sheet.getCellComment(1,v).toString().equals(""))
							   {
								   u=v;
								   break;
							   } 
							 if(!Sheet.getCellComment(1,v).toString().equals("银行账户")){
								 kColl.put("code_num", Sheet.getCellComment(1,v).toString());
								 kColl.put("owner_name", Sheet.getCellComment(2,v).toString());
								 kColl.put("CAR_FLAG_NO", Sheet.getCellComment(3,v).toString());
								 kColl.put("buy_money", Sheet.getCellComment(4,v).toString());
								 kColl.put("cus_id_car", Sheet.getCellComment(5,v).toString());
								 kColl.put("fnc_tpe", 150);
								 kColl.put("fnc_flag", "168");
								 iColl.add(kColl);
								 System.out.println(iColl.add(kColl));
							 }
							 u=v;
							 break;					   
					   }
						for(int x= u+2;x<=Sheet.getLastRowNum()-2;x++){
							if(Sheet.getCellComment(1,x).toString().equals(null)||Sheet.getCellComment(1,x).toString().equals(""))
							   {
								   break;
							   } 
							if(!Sheet.getCellComment(1,x).toString().equals("银行账户")){
							 kColl.put("code_num", Sheet.getCellComment(1,x).toString());
							 kColl.put("accout_name", Sheet.getCellComment(2,x).toString());
							 kColl.put("bank_name", Sheet.getCellComment(3,x).toString());
							 kColl.put("accout_no", Sheet.getCellComment(4,x).toString());
							 kColl.put("fnc_tpe", 150);
							 kColl.put("fnc_flag", "169");
							 iColl.add(kColl);
							 System.out.println(iColl.add(kColl));
							}
							 break;
						}
						//取负责列数据
						int aa=0;
						for(int i=7;i<=Sheet.getLastRowNum();i++){
							if(Sheet.getCellComment(4, i).toString().equals(null)||Sheet.getCellComment(4, i).toString().equals("")){
								aa=i;
								break;
							}
							if(!Sheet.getCellComment(4,i).toString().equals("总计：")){
								kColl.put("pro_num", Sheet.getCellComment(4, i).toString());
								kColl.put("begin_date", Sheet.getCellComment(5, i).toString());
								kColl.put("end_date", Sheet.getCellComment(6, i).toString());
								kColl.put("amt", Sheet.getCellComment(7, i).toString());
								kColl.put("fnc_tpe", 151);
								kColl.put("fnc_flag", "170");
								iColl.add(kColl);
								 System.out.println(iColl.add(kColl));
							}
							aa=i;
							break;
						}
						//预收账款
						int bb=0;
						for(int c=aa+3;c<=Sheet.getLastRowNum();c++){
							 if(Sheet.getCellComment(4,c).toString().equals(null)||Sheet.getCellComment(4,c).toString().equals(""))
							   {
								   bb=c;
								   break;
							   }
							   if(!Sheet.getCellComment(4,c).toString().equals("总计：")){
								   kColl.put("pro_num", Sheet.getCellComment(4, c).toString());
								   kColl.put("remark", Sheet.getCellComment(5, c).toString());
								   kColl.put("amt", Sheet.getCellComment(6, c).toString());
								   kColl.put("fnc_tpe", 151);
								   kColl.put("fnc_flag", "171");
								   iColl.add(kColl);
								   System.out.println(iColl.add(kColl));
							   }
							   bb=c;
							   break;
						}
						//银行借款
						int cc=0;
					  for(int e=bb+3;e<=Sheet.getLastRowNum();e++){
						  if(Sheet.getCellComment(4,e).toString().equals(null)||Sheet.getCellComment(4,e).toString().equals(""))
						   {
							   e=cc;
							   break;
						   }
						   if(!Sheet.getCellComment(4,e).toString().equals("总计：")){
								 kColl.put("pro_num", Sheet.getCellComment(4,e).toString());
								 kColl.put("bank_name", Sheet.getCellComment(5,e).toString());
								 kColl.put("begin_date", Sheet.getCellComment(6,e).toString());
								 kColl.put("end_date", Sheet.getCellComment(7,e).toString());
								 kColl.put("amt", Sheet.getCellComment(8,e).toString());
								 kColl.put("fnc_tpe", 151);
								 kColl.put("fnc_flag", "172");
								 iColl.add(kColl);
								 System.out.println(iColl.add(kColl));
						   }
						   e=cc;
						   break;
					  }
					  //应付其他账款
					  int dd=0;
					  for(int g=cc+3;g<=Sheet.getLastRowNum();g++){
						  if(Sheet.getCellComment(4,g).toString().equals(null)||Sheet.getCellComment(4,g).toString().equals(""))
						   {
							  g=dd;
							   break;
						   }
						   if(!Sheet.getCellComment(4,g).toString().equals("总计：")){
								 kColl.put("pro_num", Sheet.getCellComment(4,g).toString());
								 kColl.put("begin_date", Sheet.getCellComment(5,g).toString());
								 kColl.put("end_date", Sheet.getCellComment(6,g).toString());
								 kColl.put("amt", Sheet.getCellComment(7,g).toString());
								 kColl.put("fnc_tpe", 151);
								 kColl.put("fnc_flag", "173");
								 iColl.add(kColl);
								 System.out.println(iColl.add(kColl));
						   }
						   g=dd;
						   break;
					  }
					  int amt = dd+5;
							   kColl.put("apply_money", Sheet.getCellComment(6, amt).toString());
							   kColl.put("amount_money", Sheet.getCellComment(8, amt).toString());
							   int amt1 = dd+14;
							   kColl.put("inser_user", Sheet.getCellComment(7, amt1).toString());
							   kColl.put("inser_date", Sheet.getCellComment(9, amt1).toString());
							   kColl.put("fnc_tpe", 151);
							   iColl.add(kColl);
							   System.out.println(iColl.add(kColl));
			   }		  
			  }
		    for(int i=0;i<=iColl.size();i++){
		    	KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
		    	kColl1.setName("IqpMeFncBs");
		    	dao.insert(kColl1, connection);
		    }
            

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
          
		return null;
	}
public void main(String[]args){
	
	
	
}
}
