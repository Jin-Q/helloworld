<%@page language="java" contentType="text/html; charset=GBK"%> 
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
//检验组信用历史(农)的指标
			item=document.getElementsByName('G2015203967.ST025$ST02501');
		if(!judgeRadioChecked(item)){
			alert("指标[银行贷款资产状态]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203967.ST025$ST02502');
		if(!judgeRadioChecked(item)){
			alert("指标[商业信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203967.ST025$ST02503');
		if(!judgeRadioChecked(item)){
			alert("指标[个人信用]未选择，请选择后提交。");
			return false;
		}
	//检验组规模实力(农)的指标
		//检验组偿债能力(农)的指标
							item=document.getElementsByName('G2015203969.ST025$ST025010');
		if(!judgeRadioChecked(item)){
			alert("指标[或有负债比率]未选择，请选择后提交。");
			return false;
		}
	//检验组现金流量(农)的指标
			item=document.getElementsByName('G2015203970.ST025$ST02511');
		if(!judgeRadioChecked(item)){
			alert("指标[现金净流量]未选择，请选择后提交。");
			return false;
		}
		//检验组盈利能力(农)的指标
			//检验组营运能力(农)的指标
			//检验组发展能力(农)的指标
			item=document.getElementsByName('G2015203973.ST025$ST02519');
		if(!judgeRadioChecked(item)){
			alert("指标[销售收入增长率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203973.ST025$ST02520');
		if(!judgeRadioChecked(item)){
			alert("指标[净利润增长率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203973.ST025$ST02521');
		if(!judgeRadioChecked(item)){
			alert("指标[净资产增长率]未选择，请选择后提交。");
			return false;
		}
	//检验组综合评价(农)的指标
			item=document.getElementsByName('G2015203974.ST025$ST02522');
		if(!judgeRadioChecked(item)){
			alert("指标[领导者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02523');
		if(!judgeRadioChecked(item)){
			alert("指标[管理水平]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02524');
		if(!judgeRadioChecked(item)){
			alert("指标[合作情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02525');
		if(!judgeRadioChecked(item)){
			alert("指标[我行行业信贷政策导向]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02526');
		if(!judgeRadioChecked(item)){
			alert("指标[经营的厂房、土地、店铺情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02527');
		if(!judgeRadioChecked(item)){
			alert("指标[经营期限]未选择，请选择后提交。");
			return false;
		}
	//检验组修正调整项(农)的指标
			item=document.getElementsByName('G2015203975.ST025$ST02528');
		if(!judgeRadioChecked(item)){
			alert("指标[所有者权益]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02529');
		if(!judgeRadioChecked(item)){
			alert("指标[利润]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02530');
		if(!judgeRadioChecked(item)){
			alert("指标[销售收入或利润总额连续二年下跌幅度>=10%]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02531');
		if(!judgeRadioChecked(item)){
			alert("指标[财务报表未经会计师事务所审计]未选择，请选择后提交。");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2015203967" groupName="信用历史(农)" seqno="1">
				<ind:IndItemRadio indexNo="ST025$ST02501" indexName="银行贷款资产状态" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无关注、次级、可疑、损失贷款"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有关注贷款"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有次级、可疑、损失之一"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02502" indexName="商业信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="报告期内存在逾期1个月(含)以内的信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="存在逾期1个月以上的信用记录;或败诉商业纠纷的;或有拖欠员工工资记录的"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="保全资产类借新还旧贷款"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02503" indexName="个人信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良违约记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有违约记录6次(含)以下,但现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有违约记录7-11次(含),现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="报告期内有违约记录超过12次(含)以上的或有违约记录且有违约余额在3个月以内"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="报告期尚有违约余额且逾期3个月以上"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203968" groupName="规模实力(农)" seqno="2">
    			<ind:IndItemText indexNo="ST025$ST02504" indexName="销售收入(万元)" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02505" indexName="纳税总额(万元)" readonly="false" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203969" groupName="偿债能力(农)" seqno="3">
    			<ind:IndItemText indexNo="ST025$ST02506" indexName="资产负债率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02507" indexName="流动比率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02508" indexName="速动比率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02509" indexName="利息保障倍数" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST025$ST025010" indexName="或有负债比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="没有或有负债"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于10%(含)"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%-20%(含)"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%-30%(含)"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="30%-40%(含)"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="高于40%"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203970" groupName="现金流量(农)" seqno="4">
				<ind:IndItemRadio indexNo="ST025$ST02511" indexName="现金净流量" readonly="true">
							<ind:IndItemRadioOption indValue="0" indDesc="经营性现金净流量>0,现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营性现金净流量>0,现金净流量≤0"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营性现金净流量≤0,现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="经营性现金净流量≤0,现金净流量≤0"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="无"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST025$ST02512" indexName="现金流动比率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203971" groupName="盈利能力(农)" seqno="5">
    			<ind:IndItemText indexNo="ST025$ST02513" indexName="总资产报酬率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02514" indexName="销售利润率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02515" indexName="净资产收益率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203972" groupName="营运能力(农)" seqno="6">
    			<ind:IndItemText indexNo="ST025$ST02516" indexName="存货周转率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02517" indexName="应收账款周转率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02518" indexName="总资产周转率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203973" groupName="发展能力(农)" seqno="7">
				<ind:IndItemRadio indexNo="ST025$ST02519" indexName="销售收入增长率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于20%(含)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15%(含)-20%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%(含)-15%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5%(含)-10%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="低于5%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02520" indexName="净利润增长率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于15%(含)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="10%(含)-15%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="5%(含)-10%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="3%(含)-5%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="低于3%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02521" indexName="净资产增长率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于20%(含)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15%(含)-20%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%(含)-15%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5%(含)-10%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="低于5%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203974" groupName="综合评价(农)" seqno="8">
				<ind:IndItemRadio indexNo="ST025$ST02522" indexName="领导者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="领导者有丰富的管理经验;近3年总资产或销售收入逐年扩大;业绩显著,有良好的社会声誉"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02523" indexName="管理水平" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="客户产权明晰、组织结构完善、财务制度健全、财务报表可信"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02524" indexName="合作情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="与我行合作三年以上,有介绍新客户"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="与我行合作3年以上或有介绍新客户之一"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02525" indexName="我行行业信贷政策导向" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="支持"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="限制、禁止"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02526" indexName="经营的厂房、土地、店铺情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="土地、厂房产权清晰完整，手续完备"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="产权部分完整"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="没有办理相关产权自有土地厂房"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="租用"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02527" indexName="经营期限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc=" 生产经营时间超过5年(含)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="超过3年(含)未达5年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="低于3年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203975" groupName="修正调整项(农)" seqno="9">
				<ind:IndItemRadio indexNo="ST025$ST02528" indexName="所有者权益" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="大于等于6亿元"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3(含)-6亿元"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1-3亿元"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="小于等于1亿元"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02529" indexName="利润" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="利润总额 农业>=3亿元"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02530" indexName="销售收入或利润总额连续二年下跌幅度>=10%" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02531" indexName="财务报表未经会计师事务所审计" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
