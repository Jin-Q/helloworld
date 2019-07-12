<%@page language="java" contentType="text/html; charset=UTF-8"%> 
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/ind.tld" prefix="ind" %>
<link rel="stylesheet" type="text/css" href="<emp:file fileName='styles/ccrTable.css'/>"/>

<script type="text/javascript">
//验证单选是否选中.
function judgeRadioChecked(obj)
{
   if (obj){
    if (obj.length!=undefined)
    {
     for(var i=0;i < obj.length;i++)
     {
     if (obj[i].checked) return true;
     }
    }
    else{
      if (obj.checked) return true;
    }
   }
   return false;
}
function checkRequired(){
//检查每个组中的指标是否有值.如果没有则警告并返回.
var item;
//检验组自然情况的指标
				item=document.getElementsByName('G2012203827.ST016$ST01602');
		if(!judgeRadioChecked(item)){
			alert("指标[婚姻状况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203827.ST016$ST01603');
		if(!judgeRadioChecked(item)){
			alert("指标[人寿投保状况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203827.ST016$ST01606');
		if(!judgeRadioChecked(item)){
			alert("指标[户口性质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203827.ST016$ST01604');
		if(!judgeRadioChecked(item)){
			alert("指标[身体健康]未选择，请选择后提交。");
			return false;
		}
		//检验组工作情况的指标
			item=document.getElementsByName('G2012203828.ST016$ST01607');
		if(!judgeRadioChecked(item)){
			alert("指标[工作单位性质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203828.ST016$ST01608');
		if(!judgeRadioChecked(item)){
			alert("指标[岗位性质]未选择，请选择后提交。");
			return false;
		}
			//检验组家庭情况的指标
			item=document.getElementsByName('G2012203829.ST016$ST01611');
		if(!judgeRadioChecked(item)){
			alert("指标[家庭月收入]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203829.ST016$ST01612');
		if(!judgeRadioChecked(item)){
			alert("指标[与当地平均家庭月收入相比]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203829.ST016$ST01613');
		if(!judgeRadioChecked(item)){
			alert("指标[家庭财产状况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203829.ST016$ST01614');
		if(!judgeRadioChecked(item)){
			alert("指标[住房]未选择，请选择后提交。");
			return false;
		}
	//检验组与本行关系的指标
			item=document.getElementsByName('G2012203830.ST016$ST01615');
		if(!judgeRadioChecked(item)){
			alert("指标[是否本行员工]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203830.ST016$ST01616');
		if(!judgeRadioChecked(item)){
			alert("指标[本行帐户]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203830.ST016$ST01617');
		if(!judgeRadioChecked(item)){
			alert("指标[存款余额]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203830.ST016$ST01618');
		if(!judgeRadioChecked(item)){
			alert("指标[其他借款情况]未选择，请选择后提交。");
			return false;
		}
	//检验组加分项目的指标
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203827" groupName="自然情况" seqno="1">
    			<ind:IndItemText indexNo="ST016$ST01601" indexName="年龄" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST016$ST01602" indexName="婚姻状况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="已婚有子女"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="已婚无子女"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="未婚"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01603" indexName="人寿投保状况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="有"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01606" indexName="户口性质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="常驻户口"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="临时户口"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01604" indexName="身体健康" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST016$ST01605" indexName="文化程度" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203828" groupName="工作情况" seqno="2">
				<ind:IndItemRadio indexNo="ST016$ST01607" indexName="工作单位性质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="国家机关事业单位"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="金融、保险及证券行业"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="通讯、水电等行业"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="律师、医生、教师等行业"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="上市企业、大型企业"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="一般企业"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01608" indexName="岗位性质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="企业单位主管"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="企业部门主管"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="企业一般职员"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="年利润50万元以上且负债率70%以下的个体、私营业主"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="年利润30万元以上且负债率70%以下的个体、私营业主"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="年利润10万元以上且负债率70%以下的个体、私营业主"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST016$ST01609" indexName="本单位服务年限（年）" readonly="true" />
	

    			<ind:IndItemText indexNo="ST016$ST01610" indexName="个人月均收入（元）" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203829" groupName="家庭情况" seqno="3">
				<ind:IndItemRadio indexNo="ST016$ST01611" indexName="家庭月收入" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="30000元以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="20000---30000元（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="15000---20000元（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="10000---15000元（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="5000---10000元（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="5000元（含）以下"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01612" indexName="与当地平均家庭月收入相比" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="5倍以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3—5倍（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="2—3倍（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="1—2倍（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="1/2—1倍（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="1/2 （含）以下"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01613" indexName="家庭财产状况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="100万以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="70—100万（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="50—70万（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="30—50万（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="20—30万（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="10—20万（含）"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="5—10万（含）"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="5万（含）以下"/>
	    						<ind:IndItemRadioOption indValue="8" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01614" indexName="住房" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="50万元以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="40---50万元（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="30---40万元（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20---30万元（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="15---20万元（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="10---15万元（含）"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="5---10万元（含）"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="5万元（含）以下"/>
	    						<ind:IndItemRadioOption indValue="8" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203830" groupName="与本行关系" seqno="4">
				<ind:IndItemRadio indexNo="ST016$ST01615" indexName="是否本行员工" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01616" indexName="本行帐户" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="有储蓄账户且往来频繁"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有储蓄账户户且往来一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有储蓄账户但往来极少"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="无我行结算账户"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01617" indexName="存款余额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="贷款前三个月日均存款20万元（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="贷款前三个月日均存款10（含）—20万"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="贷款前三个月日均存款5（含）—10万"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="贷款前三个月日均存款1（含）—5万"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="贷款前三个月日均存款1万元以下"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="无存款"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST016$ST01618" indexName="其他借款情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="有借款、已还清"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="从未借款"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有借款，尚未还清"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="有拖欠记录"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203831" groupName="加分项目" seqno="5">
    			<ind:IndItemText indexNo="ST016$ST01619" indexName="加分项（-3~3之间有效）" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
