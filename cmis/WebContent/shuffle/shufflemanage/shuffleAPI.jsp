<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.upgrade.RuleSetInstance" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%
//����shuffleAPI��������
String i_rulesetid="zjpfk";//���õĹ���ID
String i_ruleid="grxfdk";//���õĹ���ID

Map inputMap=new LinkedHashMap();//���������һ��Map,Map�е�key/value����String����
inputMap.put("������ʽ", "��֤");//inputMap��key�����ǲ������������ƣ��磺������ʽ������Ӣ�ı������磺dbfs��
//inputMap.put("dbfs", "��֤");
//inputMap.put("������ʽ", "��֤(0.4),��Ѻ(0.6)");//����ʱ�����ֵ�п����ǰ������ģ��硰��Ѻ(0.4),��Ѻ(0.6)����ʾ��Ѻռ40����Ѻռ60����������ʱָ���������string���͵ģ��������������ͣ�

inputMap.put("����״��","����");
inputMap.put("����������","1341232.34");//�����ж���ġ����������롱��float���ͣ������õ�ʱ��ֻҪ��String���ʹ�ֵ���ɣ������ڲ�����ʱ���Զ�ƥ������
inputMap.put("����","4");

RuleSetInstance rsi=new RuleSetInstance(i_rulesetid,i_ruleid);//ͨ��API��ʽ���ù�������ӿ�
Map outputMap=rsi.fireTargetRules(inputMap);//���ؽ��Ҳ��һ��Map��Map�е�key/value����String����
%>

���ù���󷵻ؽ�����£�<br><br>
<%=outputMap%>


<%
//�����з��ʹ�����չ��Ϣ���£�
String ruleSetId="zjpfk";//����ID
String ruleId="testFormula";//����ID
RuleSet ruleSet=(RuleSet)RuleBase.getInstance().ruleSets.get(ruleSetId.toUpperCase());
Rule rule=ruleSet.getRule(ruleId);
String alertTarget=rule.alertTarget;//Ԥ�������÷ֺŷָ���ֵ���硰R.role2;G.duty2;U.user1;U.user2��
//U�����û���R�����ɫ��G�����λ��X����Ӧ����չ�ࣨԤ����
String alertType = rule.alertType;//Ԥ����ʽ��0.��Ԥ��;1.��ϢԤ��;2.�ʼ�Ԥ��;3.����Ԥ��;4.���з�ʽԤ��
String riskvalue = rule.riskvalue;//���յȼ�
System.out.println("alertTarget==="+alertTarget);
System.out.println("alertType==="+alertType);
System.out.println("riskvalue==="+riskvalue);
%>