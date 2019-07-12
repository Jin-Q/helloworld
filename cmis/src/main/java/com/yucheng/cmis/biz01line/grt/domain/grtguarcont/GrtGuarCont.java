package com.yucheng.cmis.biz01line.grt.domain.grtguarcont;               
                                                                         
import com.yucheng.cmis.pub.CMISDomain;                                  
                                                                         
public class GrtGuarCont implements CMISDomain {                         
                                                                         
	private String guarContNo;      //担保合同编号                         
	private String guarContCnNo;    //中文合同编号                         
	private String guarContType;    //担保合同类型                         
	private String guarWay;         //担保方式                             
	private String cusId; 			//客户码		                               
	private String guarTermType;    //担保期限类型                         
	private String curType; 		//币种		                                 
	private String guarAmt; 		//担保金额		                             
	private String guarModel; 		//担保模式	                             
	private String guarTerm; 		//担保期限	                               
	private String guarStartDate;   //担保起始日                           
	private String guarEndDate; 	//担保终止日                             
	private String signDate; 		//签订日期	                               
	private String guarContState;   //担保合同状态                         
	private String memo; 			//备注			                                 
	private String inputId; 		//责任人		                               
	private String managerId; 		//登记人	                               
	private String inputBrId; 		//责任机构                               
	private String managerBrId; 	//登记机构                               
	private String regDate; 		//登记日期		                             
	private String lmtGrtFlag; 		//是否授信项下                           
	private String lmtSerno; 		//授信协议编号	                           
	private String limitCode;       //授信台账编号                         
	                                                                       
	public/*protected*/ Object clone() throws CloneNotSupportedException { 
                                                                         
		// call父类的clone方法                                               
                                                                         
		Object result = super.clone();                                       
                                                                         
                                                                         
                                                                         
		//TODO: 定制clone数据                                                
                                                                         
		return result;                                                       
                                                                         
		}                                                                    
	public String getGuarContNo() {                                        
		return guarContNo;                                                   
	}                                                                      
	public void setGuarContNo(String guarContNo) {                         
		this.guarContNo = guarContNo;                                        
	}                                                                      
	public String getGuarContCnNo() {                                      
		return guarContCnNo;                                                 
	}                                                                      
	public void setGuarContCnNo(String guarContCnNo) {                     
		this.guarContCnNo = guarContCnNo;                                    
	}                                                                      
	public String getGuarContType() {                                      
		return guarContType;                                                 
	}                                                                      
	public void setGuarContType(String guarContType) {                     
		this.guarContType = guarContType;                                    
	}                                                                      
	public String getGuarWay() {                                           
		return guarWay;                                                      
	}                                                                      
	public void setGuarWay(String guarWay) {                               
		this.guarWay = guarWay;                                              
	}                                                                      
	public String getCusId() {                                             
		return cusId;                                                        
	}                                                                      
	public void setCusId(String cusId) {                                   
		this.cusId = cusId;                                                  
	}                                                                      
	public String getGuarTermType() {                                      
		return guarTermType;                                                 
	}                                                                      
	public void setGuarTermType(String guarTermType) {                     
		this.guarTermType = guarTermType;                                    
	}                                                                      
	public String getCurType() {                                           
		return curType;                                                      
	}                                                                      
	public void setCurType(String curType) {                               
		this.curType = curType;                                              
	}                                                                      
	public String getGuarAmt() {                                           
		return guarAmt;                                                      
	}                                                                      
	public void setGuarAmt(String guarAmt) {                               
		this.guarAmt = guarAmt;                                              
	}                                                                      
	public String getGuarModel() {                                         
		return guarModel;                                                    
	}                                                                      
	public void setGuarModel(String guarModel) {                           
		this.guarModel = guarModel;                                          
	}                                                                      
	public String getGuarTerm() {                                          
		return guarTerm;                                                     
	}                                                                      
	public void setGuarTerm(String guarTerm) {                             
		this.guarTerm = guarTerm;                                            
	}                                                                      
	public String getGuarStartDate() {                                     
		return guarStartDate;                                                
	}                                                                      
	public void setGuarStartDate(String guarStartDate) {                   
		this.guarStartDate = guarStartDate;                                  
	}                                                                      
	public String getGuarEndDate() {                                       
		return guarEndDate;                                                  
	}                                                                      
	public void setGuarEndDate(String guarEndDate) {                       
		this.guarEndDate = guarEndDate;                                      
	}                                                                      
	public String getSignDate() {                                          
		return signDate;                                                     
	}                                                                      
	public void setSignDate(String signDate) {                             
		this.signDate = signDate;                                            
	}                                                                      
	public String getGuarContState() {                                     
		return guarContState;                                                
	}                                                                      
	public void setGuarContState(String guarContState) {                   
		this.guarContState = guarContState;                                  
	}                                                                      
	public String getMemo() {                                              
		return memo;                                                         
	}                                                                      
	public void setMemo(String memo) {                                     
		this.memo = memo;                                                    
	}                                                                      
	public String getInputId() {                                           
		return inputId;                                                      
	}                                                                      
	public void setInputId(String inputId) {                               
		this.inputId = inputId;                                              
	}                                                                      
	public String getManagerId() {                                         
		return managerId;                                                    
	}                                                                      
	public void setManagerId(String managerId) {                           
		this.managerId = managerId;                                          
	}                                                                      
	public String getInputBrId() {                                         
		return inputBrId;                                                    
	}                                                                      
	public void setInputBrId(String inputBrId) {                           
		this.inputBrId = inputBrId;                                          
	}                                                                      
	public String getManagerBrId() {                                       
		return managerBrId;                                                  
	}                                                                      
	public void setManagerBrId(String managerBrId) {                       
		this.managerBrId = managerBrId;                                      
	}                                                                      
	public String getRegDate() {                                           
		return regDate;                                                      
	}                                                                      
	public void setRegDate(String regDate) {                               
		this.regDate = regDate;                                              
	}                                                                      
	public String getLmtGrtFlag() {                                        
		return lmtGrtFlag;                                                   
	}                                                                      
	public void setLmtGrtFlag(String lmtGrtFlag) {                         
		this.lmtGrtFlag = lmtGrtFlag;                                        
	}                                                                      
	public String getLmtSerno() {                                          
		return lmtSerno;                                                     
	}                                                                      
	public void setLmtSerno(String lmtSerno) {                             
		this.lmtSerno = lmtSerno;                                            
	}                                                                      
	public String getLimitCode() {                                         
		return limitCode;                                                    
	}                                                                      
	public void setLimitCode(String limitCode) {                           
		this.limitCode = limitCode;                                          
	} 	                                                                   
	                                                                       
                                                                         
	                                                                       
}                                                                        