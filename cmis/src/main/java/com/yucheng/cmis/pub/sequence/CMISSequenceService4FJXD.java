package com.yucheng.cmis.pub.sequence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.sequence.CMISSequenceService;

public class CMISSequenceService4FJXD extends CMISSequenceService {

	private String defaultOwner = "0000";

	protected String querySequenceFromDB(String aType, String owner, Connection connection) throws EMPException {
		String sqlStr = null;
		
		
		//PreparedStatement state = null;	
		ResultSet rs = null;
		String seqenceName="";
		String cur_sernum=null;
		String initcycle = null;
		String sequenceName="";
		DataSource dataSource = null;
		Statement state = null;
		try{
			
			if(null!=aType){
				sequenceName=sequenceName+aType;
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "] beacuse ATYPE is null");
				throw new SequenceNotFoundException("Can not found the sequence number for aType[" + aType+ "] and owner[" + owner + "] beacuse ATYPE is null");
			}
			//可以修改查询
			if(null!=owner){
				sequenceName=sequenceName+"_"+owner;
			}else{
				sequenceName=sequenceName+"_"+defaultOwner;
			}
			
	
			sqlStr = "select to_char("+sequenceName+".nextval) from dual";
			state = connection.createStatement();
			
			rs = state.executeQuery(sqlStr);
			if(null==rs){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "] beacuse Sequence rs is null");
				throw new SequenceNotFoundException("Can not found the sequence number for aType[" + aType+ "] and owner[" + owner + "] beacuse Sequence rs is null");
			}
			
			if(rs.next()){
				cur_sernum = rs.getString(1);
			}else{
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "] beacuse Sequence  next is erro ");
				throw new SequenceNotFoundException("Can not found the sequence number for aType[" + aType+ "] and owner[" + owner + "] beacuse Sequence recode next is erro");
			}
			rs.close();
			rs = null;
			state.close();
			state = null;
			
	
			
			return cur_sernum;
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			throw new EMPException(e);
		}finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public String getDefaultOwner() {
		return defaultOwner;
	}

	public void setDefaultOwner(String defaultOwner) {
		this.defaultOwner = defaultOwner;
	}

}
