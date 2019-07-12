package com.yucheng.cmis.biz01line.fnc.master.component;


/**
 *@Classname	FncRptCheckComponent.java
 *@Version 		1.0	
 *@author 		Daniel Z 
 *@Copyright 	yuchengtech
 *@Since 
 *@Description
 *@Lastmodified 
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncRptCheckAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.util.StringUtil;

public class FncRptCheckComponent extends CMISComponent{
	
	private StringUtil strut = new StringUtil();
	private FncStatBase fsb;
	private FncConfStyles fcs;
	private Map valueMap;
	
	public FncRptCheckComponent(){
		super();
	}
	
	public FncRptCheckComponent(FncConfStyles fcs,FncStatBase fsb){
		super();
		this.fcs = fcs;
		this.fsb = fsb;
	}
	
	public void setFsb(FncStatBase fsb){
		this.fsb = fsb;
	}
	
	public FncStatBase getFsb(){
		return this.fsb;
	}
	
	public void setFcs(FncConfStyles fcs){
		this.fcs = fcs;
	}
	
	public FncConfStyles getFcs(){
		return this.fcs;
	}
	
	public Map getValueMap(){
		return this.valueMap;
	}
	
	public void setValueMap() throws Exception{
		this.valueMap = this.getItemValues();
	}
	
	
	/**
	 * 将计算或检查公式转换成算术式(用于公式由多表列表项组成的情况)
	 * 公式原始格式如：({[].[].[]}+{[].[].[]})*{[].[].[]}
	 * @param formula 未转变格式的公式
	 * @param oleftFlag 外层左侧标志 {
	 * @param orightFlag 外层右侧标志}
	 * @param ileftFlag 内层左侧标志 [
	 * @param irightFlag 内层右侧标志]
	 * @exception Exception
	 * @return formula 转变后的公式
	 * */
	
	public String transFormula(String formula,char oleftFlag,char orightFlag,char ileftFlag,char irightFlag) throws Exception{
		
		String param = "";
		String param1 = "";
		String param2 = "";
		//String formula = fcdf.getFncConfCalFormula(); 
		String d1;
		String rformula = "";
	    
		/*
		 * 当外层左侧标志符在公式中不存在时，置返回值为"null"，并抛出异常。
		 * 当外层左侧标志符在公式中存在时执行循环
		 * 循环中内容：
		 * 		当取出的外层右侧标志索引小于外层左侧标志索引时，置返回值为"null"，并抛出异常。
		 * 		当取出的外层右侧标志索引大于外层左侧标志索引且左侧索引值大于等于零时
		 * 		根据	外层左右侧标志将公式字符串分为三个子字符串
		 * 		实例化FncRptCheckAgent,调用getItemValue方法获取包含itemId子字符串对应的数值
		 * 		用该数值替换包含itemId的字符串和其他两个子字符串组成新的公式,并将新公式赋值给返回值
		 * 
		 * */
	    if(formula.indexOf(oleftFlag) == -1){
	    	rformula = "null";
	    	String message = "公式中"+oleftFlag+"不存在";
			throw new Exception(message);
	    }
	    else{
	    	while(formula.indexOf(oleftFlag) != -1){
		    	
		    	if(formula.indexOf(oleftFlag)>formula.indexOf(orightFlag)){
		    		//rformula = "null";
		    		String message = "公式错误，不能转换，请检查。错误发生在第"
		    					   +formula.indexOf(orightFlag)+"位，没有与之匹配的"+oleftFlag;
		    		throw new Exception(message);
		    		
		    	}
		    	else if(formula.indexOf(oleftFlag)>=0 && formula.indexOf(orightFlag)>=0){
			    
			        int pos1=formula.indexOf(oleftFlag);
			        int pos2=formula.indexOf(orightFlag);
			        
			        param1=formula.substring(0,pos1);
			        
			        param2=formula.substring(pos2+1);
			        
			        param = strut.getParamString(formula, oleftFlag, orightFlag);
			        
			        try{
			        	FncRptCheckAgent frCheckAgent = (FncRptCheckAgent)
													this.getAgentInstance("FncRptCheckAgent");
			        	
			        	d1 = frCheckAgent.getItemValue(this.fsb,param,ileftFlag,irightFlag);
			        	if(d1.equals("null"))
			        		throw new Exception(param+"的值不存在");
			        	 //加上当对d1为空时的判断
			        	
			        	formula = new String(new StringBuffer().append(param1).append(d1).append(param2));
			        	rformula = formula;
			        }
			        catch(Exception e){
			        	e.printStackTrace();
			        }

			    }
			    
			}
	    }
		
	    
	   	return rformula;
	}
	
	/**
	 * 将FncConfStyles中数据itemid及对应的data值封装到Ｍａｐ中
	 * @param
	 * @return map
	 * */
	public Map getItemValues() throws Exception{
		Map map = new HashMap();
		
		try{
			List fcdfList = this.fcs.getItems();
			
			Iterator it = fcdfList.iterator();
			
			while(it.hasNext()){
				FncConfDefFormat fcdf = (FncConfDefFormat)it.next();
				Double[] value = new Double[2];
				
				value[0] = fcdf.getData1();
				value[1] = fcdf.getData2();
				//System.out.println(fcdf.getItemId()+" "+value[0]+" "+value[1]);
							
				map.put(fcdf.getItemId(), value);			
			}
		}
		catch(Exception e){
			throw new Exception("kakakakakaaka"+e.getMessage());
		}
		
		
		return map;	
	}
	
	/**
	 * 判断传入的值和依据公式计算的值是否相等，适用于（资产负债表、损益表）
	 * @param FncConfDefFormat fcdf 报表配置格式对象
	 * @param String data1	和itemId对应的期初数
	 * @param String data2  和itemId对应的期末数
	 * @param String[] results 根据公式计算出的结果
	 * @return String tmp 返回错误信息 
	 * @exception Exception
	 * */
	public String valueCompare(FncConfDefFormat fcdf,String data1,String data2,String[] results)throws Exception{
		
		String tmp = "fail";
		
		if(data1.charAt(data1.length()-1) == '0')
			data1 = data1+'0';
		if(data2.charAt(data2.length()-1) == '0')
			data2 = data2+'0';
				
			if((!results[0].equals(data1)) && 
					results[1].equals(data2)){
				tmp = fcdf.getItemId()+" "+fcdf.getItemName()+"期初数值有误，应为"+results[0]+"请核实";
				//throw new Exception(tmp);
			}
			else if(results[0].equals(data1) && 
					(!results[1].equals(data2))){
				tmp = fcdf.getItemId()+" "+fcdf.getItemName()+"期末数值有误，应为"+results[1]+"请核实";
				//throw new Exception(tmp);
			}
			else if(results[0].equals(data1) && results[1].equals(data2)){
				tmp = "success";
			}
			else{
				tmp = fcdf.getItemId()+" "+fcdf.getItemName()+" 期末期初数值均有误，应为"+results[0]+"、"+results[1]+"请核实";
				
				//throw new Exception(tmp);
			}
			return tmp;	
	}
	
	/**
	 * 将计算或检查公式转换成算术式(用于公式由表内列表项组成的情况，如：资产负债表、损益表)
	 * 公式原始格式如：({[]}+{[]})*{[]}
	 * @param formula 未转变格式的公式
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag
	 * @param irightFlag
	 * @exception Exception
	 * @return formulaArray 转变后的公式数组
	 * */
	public String[] transFormulas(String formula,char oleftFlag,char orightFlag,char ileftFlag,char irightFlag) throws Exception{
		
		String param = "";
		String param1 = "";
		String param2 = "";		
		Double[] d = new Double[2];
		//String formula = fcdf.getFncConfCalFormula(); 
		double d1;
		double d2;
		String[] formulaArray = new String[2];
		formulaArray[0] = formula;
		formulaArray[1] = formula;
	    
		/*
		 * 当外层左侧标志符在公式中不存在时，置返回值为"null"，并抛出异常。
		 * 当外层左侧标志符在公式中存在时执行循环
		 * 循环中内容：
		 * 		当取出的外层右侧标志索引小于外层左侧标志索引时，置返回值为"null"，并抛出异常。
		 * 		当取出的外层右侧标志索引大于外层左侧标志索引且左侧索引值大于等于零时
		 * 		根据	外层左右侧标志将公式字符串分为三个子字符串
		 * 		从封装有itemId及其对应值得Map中获取含有itemId子字符串对应的值
		 * 		用该数值替换包含itemId的字符串和其他两个子字符串组成新的公式,并将新公式赋值给返回值
		 * 
		 * */
		if(formula.indexOf(oleftFlag) == -1){
			formulaArray = null;
			String message = "公式中"+oleftFlag+"不存在";
			throw new Exception(message);
		}
		else{
			while(formulaArray[0].indexOf(oleftFlag)!=-1 || formulaArray[1].indexOf(oleftFlag)!=-1){
		    	/*
		    	while(formula.indexOf(orightFlag)<formula.indexOf(oleftFlag)){
		    		formula=formula.substring(formula.indexOf(orightFlag)+1);	    		
		    	}
		    	*/		          
		    	if(formulaArray[0].indexOf(oleftFlag)>formulaArray[0].indexOf(orightFlag) || 
		    			formulaArray[1].indexOf(oleftFlag)>formulaArray[1].indexOf(orightFlag)){
		    		formulaArray = null;
		    		String message = "公式错误，不能转换，请检查。错误发生在第"
						   +formula.indexOf(orightFlag)+"位，没有与之匹配的"+oleftFlag;
		    		throw new Exception(message);
		    		
		    	}
		    	else if(formulaArray[0].indexOf(orightFlag)>=formulaArray[0].indexOf(oleftFlag)
		    			&& formulaArray[1].indexOf(orightFlag)>=formulaArray[1].indexOf(oleftFlag)){
		    		for(int i=0;i<2;i++){
		    			int pos1=formulaArray[i].indexOf(oleftFlag);
				        int pos2=formulaArray[i].indexOf(orightFlag);
				        
				        param1=formulaArray[i].substring(0,pos1);
				        
				        param2=formulaArray[i].substring(pos2+1);
				        
				        param = strut.getParamString(formulaArray[i], oleftFlag, orightFlag);
				        
				        param = strut.getParamString(param,ileftFlag , irightFlag);
				        
				        //System.out.println("itemId is:"+param);
				        
				        d = (Double[])this.valueMap.get(param);
				        
				        d1 = d[i];
				        
				        /*
				        if(i==0)
				        	System.out.println("期初： param is "+param+" and value is "+d1);
				        else
				        	System.out.println("期末： param is "+param+" and value is "+d1);
				        */
				        
				        formulaArray[i] = new String(new StringBuffer().append(param1).append(d1).append(param2));
				        //formulaArray[1] = new String(new StringBuffer().append(param1).append(d2).append(param2));
		    		}

			    }
			}
		}
			    
	   	
		return formulaArray;	
		
	}
		
	/**
	 * 计算公式结果
	 * @param formula
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag 内层左侧标志
	 * @param orightFlag 内层右侧标志
	 * @return result 算术式计算值
	 * 
	 * */
	public String getFormulaResult(String formula) throws Exception{
		
		//formula = this.transFormula(formula, oleftFlag, orightFlag, ileftFlag, irightFlag);
		
		String result = "";
		if(formula.equals("null")){
			throw new Exception("公式为空，无法转换，请检查");
		}
		else
			result = strut.getFormulaValue(formula);		
		
		return result;
	}
	
	/**
	 * 计算公式结果
	 * @param String[] formulaArray 
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志	
	 * @return results 算术式计算值数组
	 * 
	 * */
	public String[] getFoumulaResults(String[] formulaArray) throws Exception{
		String[] results = new String[2];
		
		if(formulaArray.equals(null)){
			throw new Exception("公式错误，无法转换，请检查");
		}
		else{
			results[0] = this.strut.getFormulaValue(formulaArray[0]);
			results[1] = this.strut.getFormulaValue(formulaArray[1]);
		}		
		
		return results;
	}
	
	/**
	 * 检查前台传入的报表数据包(主要用于现金流量表、)
	 * @param formula 转换后的公式
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag 内层左侧标志
	 * @param orightFlag 内层右侧标志
	 * @return  message	
	 * */
	
	public String checkStatCFS(char oleftFlag,char orightFlag,char ileftFlag,char irightFlag) throws Exception{
		
		String message = "";
		FncConfDefFormat fcdf;
		String formula = "";
		String result = "";
		String tmp = "";
		List list = new ArrayList();
		list = this.fcs.getItems();
		Iterator it = list.iterator();
		
		/*
		 * 枚举list中FncConfDefFormat对象
		 * 获取对象中的公式，并把公式转换成算术式，再获取算术式的值
		 * 将算术式的值和 FncConfDefFormat对象中对应的值比较
		 * 如果相等返回值置为success
		 * 如果不相等则置返回值为错误信息，并抛出异常
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			formula = fcdf.getFncConfChkFrm();
			if(formula!=null && formula.length()!=0){
				
				try{
					formula = this.transFormula(formula, oleftFlag, orightFlag, ileftFlag, irightFlag);
					result = this.getFormulaResult(formula);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				String data = ((Double)fcdf.getData1()).toString();
				if(data.charAt(data.length()-1) == '0')
					data = data+'0';
				if(!result.equals(data)){
					message = fcdf.getItemId()+fcdf.getItemName()+"数据有误，请核实";					
					throw new Exception(message);
				}
				
			}			
			
		}
		
		if(message.length() == 0)
			message = "success";
		
		return message;		
	}
	
	/**
	 * 检查前台传入的报表数据包(主要用于现金流量表、)
	 * 指定外层和内层标志
	 * 
	 *  oleftFlag = '{' 外层左侧标志
	 *  orightFlag = '}'外层右侧标志
	 *  ileftFlag = '['内层左侧标志
	 *  orightFlag = ']'内层右侧标志
	 * @return  message	
	 * */
	
	public String checkStatCFS() throws Exception{
		
		String message = "";
		FncConfDefFormat fcdf;
		String formula = "";
		String result = "";
		String tmp = "";
		List list = new ArrayList();
		list = this.fcs.getItems();
		Iterator it = list.iterator();
		
		/*
		 * 枚举list中FncConfDefFormat对象
		 * 获取对象中的公式，并把公式转换成算术式，再获取算术式的值
		 * 将算术式的值和 FncConfDefFormat对象中对应的值比较
		 * 如果相等返回值置为success
		 * 如果不相等则置返回值为错误信息，并抛出异常
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			formula = fcdf.getFncConfChkFrm();
			if(formula!=null && formula.length()!=0){
				
				try{
					formula = this.transFormula(formula, '{', '}', '[', ']');
					result = this.getFormulaResult(formula);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				
				String data = ((Double)fcdf.getData1()).toString();
				if(data.charAt(data.length()-1) == '0')
					data = data+'0';
				if(!result.equals(data)){
					message = fcdf.getItemId()+fcdf.getItemName()+"数据有误，请核实";					
					throw new Exception(message);
				}
			}			
			
		}
		
		if(message.length() == 0)
			message = "success";
		
		return message;		
	}
	
	/**
	 * 检查前台传入的报表数据包(主要用于资产负债表、损益表)
	 * 
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag  内层左侧标志
	 * @param irightFlag 内层右侧标志
	 * @return  message	
	 * */
	public String check(char oleftFlag,char orightFlag,char ileftFlag,char irightFlag) throws Exception{
		String message = "";
		String[] formulaArray;
		String[] results = new String[2];
		String data1;
		String data2;
		
		//获取fcs对象中包含的所有itemId及对应的值，封装到map中，并赋值给this.valueMap
		this.setValueMap();
		
		FncConfDefFormat fcdf;		
		List fcdfList = this.fcs.getItems();
		Iterator it = fcdfList.iterator();
		/*
		 * 枚举list中FncConfDefFormat对象
		 * 获取对象中检查公式，并获取公式计算所得数值
		 * 将公式计算所得数值与对象中对应的数值进行比较，
		 * 	  若数值不匹配择封装对应的错误信息，将错误信息作为异常抛出
		 * 	  若数值匹配则置返回值为"success"
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			String formula = fcdf.getFncConfChkFrm();
			
			if(formula!=null && formula.length()!=0){
				
					formulaArray = this.transFormulas(formula, oleftFlag, orightFlag,ileftFlag,irightFlag);
					results = this.getFoumulaResults(formulaArray);
					data1 = ((Double)fcdf.getData1()).toString();
					data2 = ((Double)fcdf.getData2()).toString();
					
					message = this.valueCompare(fcdf, data1, data2, results);						
			}			
			
		}
		
		if(message.length() == 0)
			message = "success";
		
		return message;
	}
	
	/**
	 * 检查前台传入的报表数据包(主要用于资产负债表、损益表)
	 * 指定外层标志oleftFlag = '{',orightFlag = '}'
	 * @param 
	 * @return  message	
	 * */
	public String check() throws Exception{
		StringBuffer message = new StringBuffer();
		String[] formulaArray;
		String[] results = new String[2];
		String data1;
		String data2;
		String tmp = "";
		
		//获取fcs对象中包含的所有itemId及对应的值，封装到map中，并赋值给this.valueMap
		this.setValueMap();
		
		FncConfDefFormat fcdf;		
		List fcdfList = this.fcs.getItems();
		Iterator it = fcdfList.iterator();
		/*
		 * 枚举list中FncConfDefFormat对象
		 * 获取对象中检查公式，并获取公式计算所得数值
		 * 调用本类中方法valueCompare将公式计算所得数值与对象中对应的数值进行比较，
		 * 	  若数值不匹配择封装对应的错误信息，将错误信息作为Excption异常抛出
		 * 	  若该FncConfStyles中所有itemId的数值均匹配则置返回值为"success"
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			
			String formula = fcdf.getFncConfChkFrm();
			
			if(formula!=null && formula.length()!=0){
								
					//System.out.println("formula is :"+formula);
					try{
						formulaArray = this.transFormulas(formula, '{', '}','[',']');
					}
					catch(Exception e){
						throw new Exception(fcdf.getItemId()+" "+fcdf.getItemName()+" "+e.getMessage());
					}
						
						results = this.getFoumulaResults(formulaArray);
						data1 = ((Double)fcdf.getData1()).toString();
						data2 = ((Double)fcdf.getData2()).toString();
						
						tmp = this.valueCompare(fcdf, data1, data2, results);						
						
						if(!tmp.equals("success")){
							message.append(tmp+"\n");
						}															
												
				}		
						
			}			
				
		if(message.length() == 0 || message == null){
			message.append("success");
		}
		else{
			throw new Exception(message.toString());
		}
			
		
		return message.toString();
		
	}
	
	/**
	 * 自动计算现金流量表，股东权益变动表
	 * @param FncConfStyles fcStyles
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag 内层左侧标志
	 * @param orightFlag 内层右侧标志
	 * @return void 	
	 * 
	 * */
	
	public void autoCalcRpt(FncConfStyles fcStyles,char oleftFlag,char orightFlag,char ileftFlag,char irightFlag)
		throws AgentException{
		//获取ｆｃｓ对象
		String formula;
		String result;
		FncConfDefFormat fcdf;
		String itemId;
		List fcdsList = fcStyles.getItems();
		Iterator it = fcdsList.iterator();
		String fncConfTyp = fcStyles.getFncConfTyp();
		
		/*
		 * 枚举list中FncConfDefFormat对象，获取列表项对应的计算公式
		 * 转换计算公式，计算出公式的值
		 * 实例化FncRptCheckAgent对象
		 * 调用其中方法保存记录
		 * 
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			formula = fcdf.getFncConfCalFrm();
			
			if(formula!=null && formula.length()!=0){
				itemId = fcdf.getItemId();
				
				try{
					formula = this.transFormula(formula, oleftFlag, orightFlag, ileftFlag, irightFlag);
					result = this.getFormulaResult(formula);
					
					FncRptCheckAgent frcAgent = (FncRptCheckAgent)this.getAgentInstance("FncRptCheckAgent");					
					frcAgent.saveRecord(this.fsb, fncConfTyp, itemId, result);
				}
				catch(Exception e){
					e.printStackTrace();
				}			
				
			}
			
		}
		//此处需增加更改fnc_stat_base中statflag的方法
	}
	
	/**
	 * 自动计算资产负债表、损益表
	 * 
	 * @param oleftFlag 外层左侧标志
	 * @param orightFlag 外层右侧标志
	 * @param ileftFlag  内层左侧标志
	 * @param irightFlag 内层右侧标志
	 * @return  FncConfStyles	
	 * */
	
	public FncConfStyles autoCalcRpt(char oleftFlag,char orightFlag,char ileftFlag,char irightFlag) throws AgentException{
		FncConfStyles pfcs = new FncConfStyles();
		String[] formulaArray;
		String[] results;
		String tmp;
		String itemId;
		String fncConfTyp = this.fcs.getFncConfTyp();
		FncConfDefFormat fcdf;
		List<FncConfDefFormat> fcdfList = new ArrayList<FncConfDefFormat>();
		Iterator it = this.fcs.getItems().iterator();
		
		/*
		 * 枚举list中FncConfDefFormat对象，
		 * 有计算公式的获取列表项计算公式
		 * 		转换计算公式，计算出公式的值
		 * 		将计算所得值set到fcdf对象中，在每一次循环临近结束时将处理后的fcdf对象添加至fcdfList中
		 * 无计算公式的直接将fcdf对象添加到fcdfList中
		 * 枚举结束后，将fcdfListset到pfcs对象中
		 * 返回pfcs对象
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			tmp = fcdf.getFncConfCalFrm();
			//itemId = fcdf.getItemId();
			if(tmp!=null && tmp.length()!=0){
				
				try{
					formulaArray = this.transFormulas(tmp, oleftFlag, orightFlag,ileftFlag,irightFlag);
					results = this.getFoumulaResults(formulaArray);
					fcdf.setData1(Double.parseDouble(results[0]));
					fcdf.setData2(Double.parseDouble(results[1]));
					
					/*计算后直接更新至数据库
					FncRptCheckAgent frcAgent = (FncRptCheckAgent)this.getAgentInstance("FncRptCheckAgent");
					
					frcAgent.saveRecord(fsb, fncConfTyp, itemId, results);
					*/
				}
				catch(Exception e){
					e.printStackTrace();
				}
								
				
			}
			fcdfList.add(fcdf);
		}
		//*根据提交动作决定是否更改fnc_stat_base中statflag，暂存不用，保存则更新。
		pfcs.setItems(fcdfList);
		return pfcs;
	}
	
	/**
	 * 自动计算资产负债表、损益表
	 * 指定外层和内层标志符
	 * 外层标志符'{''}'
	 * 内层标志符'['']'
	 * @return  FncConfStyles
	 * @exception AgentException	
	 * */
	
	public FncConfStyles autoCalcRpt() throws Exception,AgentException{
		//FncConfStyles pfcs = new FncConfStyles();
		//pfcs.setFncName(this.fcs.getFncName());
		
		String[] formulaArray;
		String[] results;
		String formula;
		this.setValueMap();
				
		//String itemId;
		//String fncConfTyp = this.fcs.getFncConfTyp();
		
		FncConfDefFormat fcdf;
		List<FncConfDefFormat> fcdfList = new ArrayList<FncConfDefFormat>();
		Iterator it = this.fcs.getItems().iterator();
		
		/*
		 * 枚举list中FncConfDefFormat对象，
		 * 有计算公式的获取列表项计算公式
		 * 		转换计算公式，计算出公式的值
		 * 		将计算所得值set到fcdf对象中，在每一次循环临近结束时将处理后的fcdf对象添加至fcdfList中
		 * 无计算公式的直接将fcdf对象添加到fcdfList中
		 * 枚举结束后，将fcdfListset到pfcs对象中
		 * 返回pfcs对象
		 * */
		while(it.hasNext()){
			fcdf = (FncConfDefFormat)it.next();
			formula = fcdf.getFncConfCalFrm();
			//itemId = fcdf.getItemId();
			if(formula!=null && formula.length()!=0){
				
				try{
					
					formulaArray = this.transFormulas(formula, '{', '}','[',']');					
					results = this.getFoumulaResults(formulaArray);
					fcdf.setData1(Double.parseDouble(results[0]));
					fcdf.setData2(Double.parseDouble(results[1]));
					
					Double[] tmpd = new Double[2];
					tmpd[0] = Double.parseDouble(results[0]);
					tmpd[1] = Double.parseDouble(results[1]);
					
					this.valueMap.put(fcdf.getItemId(), tmpd);					
					
					/*计算后直接更新至数据库,如需直接插入数据库可放开
					FncRptCheckAgent frcAgent = (FncRptCheckAgent)this.getAgentInstance("FncRptCheckAgent");
					
					frcAgent.saveRecord(fsb, fncConfTyp, itemId, results);
					*/
				}
				catch(Exception e){
					throw new AgentException(fcdf.getItemId()+" "+fcdf.getItemName()+" "+e.getMessage());
				}
								
				
			}
			fcdfList.add(fcdf);
		}
		//*根据提交动作决定是否更改fnc_stat_base中statflag，暂存不用，保存则更新。
		this.fcs.setItems(fcdfList);
		return fcs;
	}
}
