<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.upgrade.RuleSetInstance" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%
//调用shuffleAPI范例代码
String i_rulesetid="zjpfk";//调用的规则集ID
String i_ruleid="grxfdk";//调用的规则ID

Map inputMap=new LinkedHashMap();//输入参数是一个Map,Map中的key/value都是String类型
inputMap.put("担保方式", "保证");//inputMap的key可以是参数的中文名称（如：担保方式）或者英文别名（如：dbfs）
//inputMap.put("dbfs", "保证");
//inputMap.put("担保方式", "保证(0.4),质押(0.6)");//调用时传入的值有可能是按比例的，如“抵押(0.4),质押(0.6)”表示抵押占40％质押占60％，按比例时指标项必须是string类型的（不能是数字类型）

inputMap.put("房产状况","按揭");
inputMap.put("个人年收入","1341232.34");//规则中定义的“个人年收入”是float类型，但调用的时候只要按String类型传值即可，引擎内部计算时会自动匹配类型
inputMap.put("年龄","4");

RuleSetInstance rsi=new RuleSetInstance(i_rulesetid,i_ruleid);//通过API方式调用规则引擎接口
Map outputMap=rsi.fireTargetRules(inputMap);//返回结果也是一个Map，Map中的key/value都是String类型
%>

调用规则后返回结果如下：<br><br>
<%=outputMap%>


<%
//代码中访问规则扩展信息如下：
String ruleSetId="zjpfk";//规则集ID
String ruleId="testFormula";//规则ID
RuleSet ruleSet=(RuleSet)RuleBase.getInstance().ruleSets.get(ruleSetId.toUpperCase());
Rule rule=ruleSet.getRule(ruleId);
String alertTarget=rule.alertTarget;//预警对象，用分号分割多个值，如“R.role2;G.duty2;U.user1;U.user2”
//U代表用户，R代表角色，G代表岗位，X代表应用扩展类（预留）
String alertType = rule.alertType;//预警方式；0.不预警;1.消息预警;2.邮件预警;3.短信预警;4.所有方式预警
String riskvalue = rule.riskvalue;//风险等级
System.out.println("alertTarget==="+alertTarget);
System.out.println("alertType==="+alertType);
System.out.println("riskvalue==="+riskvalue);
%>