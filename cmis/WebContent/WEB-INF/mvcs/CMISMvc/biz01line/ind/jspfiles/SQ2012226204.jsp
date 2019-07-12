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
//检验组信用历史的指标
			item=document.getElementsByName('G2012203818.ST001$ST00101');
		if(!judgeRadioChecked(item)){
			alert("指标[银行贷款资产状态（五级分类）]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203818.ST001$ST00102');
		if(!judgeRadioChecked(item)){
			alert("指标[商业信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203818.ST001$ST00103');
		if(!judgeRadioChecked(item)){
			alert("指标[个人信用（法人、实际负责人、主要股东）]未选择，请选择后提交。");
			return false;
		}
	//检验组规模实力的指标
			item=document.getElementsByName('G2012203859.ST009$ST00901');
		if(!judgeRadioChecked(item)){
			alert("指标[资产规模]未选择，请选择后提交。");
			return false;
		}
					item=document.getElementsByName('G2012203859.ST009$ST00902');
		if(!judgeRadioChecked(item)){
			alert("指标[利润总额]未选择，请选择后提交。");
			return false;
		}
	//检验组偿债能力的指标
				item=document.getElementsByName('G2012203860.ST009$ST00908');
		if(!judgeRadioChecked(item)){
			alert("指标[或有负债比率]未选择，请选择后提交。");
			return false;
		}
			//检验组现金流量的指标
			item=document.getElementsByName('G2012203861.ST009$ST00909');
		if(!judgeRadioChecked(item)){
			alert("指标[现金净流量]未选择，请选择后提交。");
			return false;
		}
		//检验组盈利能力的指标
			//检验组公司地位情况的指标
			item=document.getElementsByName('G2012203863.ST009$ST00924');
		if(!judgeRadioChecked(item)){
			alert("指标[公司级别]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203863.ST009$ST00925');
		if(!judgeRadioChecked(item)){
			alert("指标[公司获得的当地政府支付力度]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203863.ST009$ST00926');
		if(!judgeRadioChecked(item)){
			alert("指标[与政府的关系]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203863.ST009$ST00927');
		if(!judgeRadioChecked(item)){
			alert("指标[融资能力水平]未选择，请选择后提交。");
			return false;
		}
	//检验组地方经济环境的指标
			item=document.getElementsByName('G2012203864.ST009$ST00928');
		if(!judgeRadioChecked(item)){
			alert("指标[区域地位]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203864.ST009$ST00929');
		if(!judgeRadioChecked(item)){
			alert("指标[当地GDP发展情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203864.ST009$ST00930');
		if(!judgeRadioChecked(item)){
			alert("指标[当地财政发展情况]未选择，请选择后提交。");
			return false;
		}
	//检验组管理水平的指标
			item=document.getElementsByName('G2012203865.ST009$ST00914');
		if(!judgeRadioChecked(item)){
			alert("指标[企业组织机构及生产管理现场情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203865.ST009$ST00916');
		if(!judgeRadioChecked(item)){
			alert("指标[公司员工情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203865.ST009$ST00915');
		if(!judgeRadioChecked(item)){
			alert("指标[公司内部制度建设、制度实施情况]未选择，请选择后提交。");
			return false;
		}
	//检验组综合评价的指标
			item=document.getElementsByName('G2012203866.ST009$ST00917');
		if(!judgeRadioChecked(item)){
			alert("指标[领导者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203866.ST009$ST00904');
		if(!judgeRadioChecked(item)){
			alert("指标[宏观经济政策]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203866.ST009$ST00918');
		if(!judgeRadioChecked(item)){
			alert("指标[合作情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203866.ST009$ST00920');
		if(!judgeRadioChecked(item)){
			alert("指标[资金回笼率]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203866.ST009$ST00919');
		if(!judgeRadioChecked(item)){
			alert("指标[生产经营期限]未选择，请选择后提交。");
			return false;
		}
	//检验组修正/调整项的指标
			item=document.getElementsByName('G2012203867.ST009$ST00921');
		if(!judgeRadioChecked(item)){
			alert("指标[是否多营业执照经营]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203867.ST009$ST00922');
		if(!judgeRadioChecked(item)){
			alert("指标[财务报表是否经过会计事务所审计]未选择，请选择后提交。");
			return false;
		}
			return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203818" groupName="信用历史" seqno="1">
				<ind:IndItemRadio indexNo="ST001$ST00101" indexName="银行贷款资产状态（五级分类）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无关注、次级、可疑、损失贷款"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有关注贷款"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有次级、可疑、损失之一"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST001$ST00102" indexName="商业信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="报告期内存在逾期1个月（含）以内的信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="存在逾期1个月以上的信用记录；或败诉商业纠纷的；或有拖欠员工工资记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="保全资产类借新还旧贷款"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST001$ST00103" indexName="个人信用（法人、实际负责人、主要股东）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良违约纪录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有违约记录6次（含）以下，但现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有违约记录7-11次（含），现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="报告期内有违约记录超过12次（含）以上的或有违约记录且有违约余额在3个月以内"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="报告期尚有违约余额且逾期3个月以上"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203859" groupName="规模实力" seqno="2">
				<ind:IndItemRadio indexNo="ST009$ST00901" indexName="资产规模" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="30000万（含）以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="20000万（含）-30000万"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="15000万元（含）-20000万"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="10000万（含）—15000万元"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="5000万（含）—10000万元"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="2000万（含）-5000万元"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="2000万元"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST009$ST00903" indexName="实有净资产(万元)" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST009$ST00902" indexName="利润总额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc=" 2000万（含）以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="1500万元（含）-2000万"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1000万（含）-1500万元"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="500万（含）-1000万元"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="300万（含）-500万元"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="100万（含）-300万元"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="100万元以下"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203860" groupName="偿债能力" seqno="3">
    			<ind:IndItemText indexNo="ST009$ST00905" indexName="资产负债率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST009$ST00908" indexName="或有负债比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="没有或有负债"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于10%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%-20%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%-30%（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="30%-40%（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="40%-50%（含）"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="50%-60%（含）"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="高于60%"/>
	    						<ind:IndItemRadioOption indValue="8" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST009$ST00906" indexName="流动比率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST009$ST00907" indexName="利息保障倍数" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203861" groupName="现金流量" seqno="4">
				<ind:IndItemRadio indexNo="ST009$ST00909" indexName="现金净流量" readonly="true">
							<ind:IndItemRadioOption indValue="0" indDesc="经营性现金净流量>0，现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营性现金净流量>0，现金净流量≤0"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营性现金净流量≤0，现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="经营性现金净流量≤0，现金净流量≤0"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST009$ST00910" indexName="现金流动比率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203862" groupName="盈利能力" seqno="5">
    			<ind:IndItemText indexNo="ST009$ST00911" indexName="总资产报酬率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST009$ST00912" indexName="销售利润率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST009$ST00913" indexName="净资产收益率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203863" groupName="公司地位情况" seqno="6">
				<ind:IndItemRadio indexNo="ST009$ST00924" indexName="公司级别" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="省级"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="地市级"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="县区级"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="村镇级"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00925" indexName="公司获得的当地政府支付力度" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="地方政府或部门有注入土地、股权等资产，超过公司资产总额的40%（含）以上，并已办理过户手续"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="注入土地、股权等资产，超过30%（含）以上"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="注入土地、股权等资产，超过20%（含）以上"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="注入土地、股权等资产，超过10%（含）以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="无任何资产注入"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00926" indexName="与政府的关系" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="直属当地政府（如市政府）、主要领导（如市长、副市长）直接管辖"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="直属当地财政局等强势部门直接管辖"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其它"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00927" indexName="融资能力水平" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="融资灵活性强，有两家或两家以上的金融机构融资业务合作"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="融资灵活性一般，有1家金融机构融资业务合作"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="融资灵活性差或首次融资"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203864" groupName="地方经济环境" seqno="7">
				<ind:IndItemRadio indexNo="ST009$ST00928" indexName="区域地位" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="区域地位高"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="区域地位中"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="区域地位低"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00929" indexName="当地GDP发展情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="当地经济GDP发展速度明显高于区域发展速度"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="当地经济经济GDP发展速度与区域发展速度相差不超过1%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="当地经济经济GDP发展速度低于区域发展速度"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00930" indexName="当地财政发展情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="当地财政收入发展速度明显高于区域财政收入发展水平"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="当地经济经济GDP发展速度与区域发展速度相差不超过1%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="当地经济经济GDP发展速度低于区域发展速度"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203865" groupName="管理水平" seqno="8">
				<ind:IndItemRadio indexNo="ST009$ST00914" indexName="企业组织机构及生产管理现场情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有2项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00916" indexName="公司员工情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有2项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00915" indexName="公司内部制度建设、制度实施情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有1项表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203866" groupName="综合评价" seqno="9">
				<ind:IndItemRadio indexNo="ST009$ST00917" indexName="领导者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="领导者有丰富的管理经验，兼任或曾担任过政府重要领导岗位"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00904" indexName="宏观经济政策" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="积极扩张的财政政策或货币政策"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="稳健的财政政策或货币政策"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="紧缩的财政政策或货币政策"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="指标值不达标不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00918" indexName="合作情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="与我行合作三年以上，有介绍新客户"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="与我行合作3年以上或有介绍新客户之一"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00920" indexName="资金回笼率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="资金回笼率高于200%(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="100%（含）-200%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="80%（含）-100%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="50%-80%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="低于50%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00919" indexName="生产经营期限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="生产经营时间超过5年(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3年(含)-5年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="低于3年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203867" groupName="修正/调整项" seqno="10">
				<ind:IndItemRadio indexNo="ST009$ST00921" indexName="是否多营业执照经营" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="不是"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST009$ST00922" indexName="财务报表是否经过会计事务所审计" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经会计事务所审计持无保留意见"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经会计事务所审计却持保留意见"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="未经审计"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST009$ST00923" indexName="其它可减分因素" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
