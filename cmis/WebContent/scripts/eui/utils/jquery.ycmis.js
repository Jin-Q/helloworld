/**
 * jquery plugin for Yucheng mis(depend on jeasyui1.3.2 + jquery1.8.0)
 * 该js通过扩展jquery.easyui.min.js、jquery-1.8.0.min.js提供符合easyui的基础js方法具体包括：
 * 新增：
 * 		1.getValue:提供真实值的获取;
 * 		2.setValue:提供赋值操作，解决radio,checkbox赋值问题，同时对赋值进行格式化操作。
 * 		3.renderHidden:提供隐藏操作
 * 		4.renderReadonly，renderDisabled：提供设置只读和不可用
 * 		5.createwin：提供基于iframe的dialog新增操作
 * 		6.toJsonData:将选择器项下的数据转成json对象 
 * 		7.toForm:将选择器项下的数据添加到指定的form
 * 		8.searchParams：构造搜索框查询参数的json对象
 *      9.transValueC：用于propertygrid的数据格式化 
 *      10.添加addWithIframe方法用于tabs以iframe形式添加页签jQuery对象调用
 *      11.transCheckbox4C:用于checkbox、radio的表格数据数据格式化
 *      12.EMP.toJsonDataAry:用于将rows转成EMP支持的icoll类型的json对象。参数：icollid(icoll名称)、rows(要转换的rows)、json(需要放入的json对象，可为空)
 *      13.toJsonDataAry4Edit:将选择器对应的DataGrid中数据组装成后台可识别的JSON数据（自动拆分insert,update,delete）
 *      14.$(window).resize:容器自适应窗口的变化。
 *      15.EMP.buildAnchors:建立锚点。通过配置参数可以快速建立一组锚点，用于内容过多的页面快速定位。
 *      16.$fn.getSearchBoxValue:快速查询取值
 *      17.EMP.toJsonDataAry4Nodes:将tree的节点对象转成EMP支持的icoll类型的json对象。
 *      18.checkAll用于检查表单数据，参数isRequired为false时不检查必输项，默认检查必输项。
 *      19.dataGridEndEdit 方法，统一关闭datagrid编辑器的方法，使用方式：$("#datagridId").dataGridEndEdit();	
 *      20.EMP.getEDataGridValue 编辑表格edatagrid取值：取当前选中行的指定字段的值 add by yuhq at 2014-6-4 22:47:49
 *      21.EMP.setEDataGridValue 编辑表格edatagrid赋值：给当前选中行的指定字段的赋值 add by yuhq at 2014-6-4 22:47:49
 *      22.EMP.SetInputDefValue 设置_value用于存放加载时的数据  add by zhangrj at 2014-07-11 15:33:33
 *      23.EMP.validateFormChanged 离开页面时,调用该方法检测表单元素是否被修改,可根据结果给出提示.防止用户错失修改的机会 add by zhangrj at 2014-07-11 15:33:33
 *      24.EMP.getEDataGridEditor 获取编辑表格当前行指定字段的编辑器,根据编辑器可以做赋值、必输项等控件 add by yuhq at 2014-7-14
 *      25.EMP.createCalendar 创建自定义日历表格。add by zangys at 2014-9-25 17:52:58
 *      26.jQuery.fn.getText用于取显示值。add by zangys at 2014-10-22 17:09:05
 *      27 jQuery.fn.bindOnChange 提供统一的方式为标签附加onChange事件，该方法不会覆盖掉标签已配置的onchange事件。 add by zangys at 2014-10-31 15:03:56
 *      28.EMP.renderSpinnerHide 用于隐藏/显示微调按钮。add by zangys at 2015-05-04 16:04
 *      29.toJsonDataAry4EditWithAllTabData方法，在可编辑表格中，假分页的条件下，将选择器对应的DataGrid中全部数据组装成后台可识别的JSON数据（只有all）add by wangyang13 at 2015-12-15 15:00
 *      30.添加 jQuery.textOnchangeExt方法 用与对text标签onchange事件的扩展 add by liwei at 2015-12-24 11:00:00
 *      31.添加EMP.promptMessage方法，用户可以输入文本的并且带“确定”和“取消”按钮的消息提示窗体 add by wangyang at 2016-01-06 14:20
 *      
 *修改说明：
 *		1.规范jquery.ycmis.js的函数命名：使用EMP指定公用函数，影响函数（createwin、closewin、queryJson、searchParams）
 *		  调用请修改为EMP.createwin（推荐使用top.EMP.createwin在顶层页面打开窗口）、EMP.closewin、EMP.queryJson、EMP.searchParams
 *		2.将dateymbox、popbox、checkbox以扩展组件js文件形式独立出来，放置easyuiplugins文件夹下，EUIInclude.jsp引入该js文件。
 *		3.将easyui扩展js转移到ext.easyui.js add by wangbin 2014-3-25 16:29:09
 *		4.修改renderRequired、renderHidden、renderReadonly、renderDisabled以支持操作选择器下的所有匹配元素。 add by wangbin 2014-4-2 16:37:01
 *		5.修改renderHidden方法满足布局的自动缩进 add by wangbin 2014-4-4 16:06:32
 *		6.修改EMP.alertMessage 增加参数fn 在窗口关闭的时候触发该回调函数
 *		7.修复table编辑中使用renderRequired方法js错误。 add by wangbin 2014年5月13日17:03:50
 *		8.为checkAll增加一个参数用于指定是否进行必输项检查 add by wangbin 2014-5-13 17:39:15
 *      9.修改getValue方法将combo类的数据获取转换成字符串 add by wangbin 2014-5-20 11:22:46
 *      10.扩展checkAll参数类型，不传参数默认全部检查，传false表示全部不检查；或者传递用逗号分隔的id字符串，表示指定的id标签不做必输项检查 add by Pansq 2014-5-20 14:38:20
 *      11.重构getValue和setValue方法，使其根据easy ui的缺省class来区分组件（之前是cssClass），解决了编辑表格中取值为undefined的BUG add by yuhq at 2014-5-26 9:55:40
 *      12.将下拉选择的"--请选择--"文本删除，add by yuhq at 2014-6-12 11:50:26
 *      13.修改createwin方法，将iframe的src属性在创建完成后赋值，针对IE10重复加载问题。 add by zangys at 2014-6-17 10:10:00
 *      14.增加renderDisabled对popbox的处理。add by zangys at 2014-7-16 13:28:23
 *      15.修改combobox,combogrid,combotree在required=true时，通过renderRequired设置required=false后,在叹号没有消失时，checkAll校验必输时返回false的bug add by zangys at 2014-7-30 9:50:23
 *      16.修改通过renderRequired对checkbox/radio组件进行设置不生效的问题。add by zangys at 2014-7-30 14:51:23
 *      17.修改trans*4C类方法中precision写死的问题。add by zangys at 2014-7-31 17:56:50
 *      18.删除transText4C方法，将numberbox的默认formatter更改为transNumber4C,之前通过transText4C，由于其余标签的默认formatter均为transText4C，
 *         如果不在此方法中做一一相应处理，会覆盖掉其余标签原有的formatter方法。add by zangys at 2014-8-4 15:09:45
 *      19.修改numberbox在表格中如果不设置 prefix，suffix显示undefined的问题。add by zangys at 2014-8-4 16:20:12
 *      20.删除下拉列表框value=''的隐藏input项。add by zangys at 2014-9-11 13:21:21
 *      21.修改combogrid在出现下拉选项时，不选择记录，直接鼠标点击其他地方，则输入框中之前输入的数据被清空的问题。add by zangys at 2014-9-15 10:22:48
 *      22.修改combogrid在多选时，出现下拉选项，不选择记录，直接鼠标点击其他地方，则输入框中之前输入的数据只留一个的情况。add by zangys at 2014-9-16 12:54:29
 *      23.修改combogrid在失去焦点时,如果是多选情况，则text与value不同则默认合法，单选情况下下拉表格框的输入框中的内容value与text值必须在当前下拉表格的选项中。add by zangys at 2014-9-18 10:46:56
 *      24.扩展jQuery.fn.setValue方法，增加combogridext类型。add by zangys at 2014-9-18 10:48:58
 *      25.修改jQuery.fn.checkAll方法，校验disabled=true的标签;修改jQuery.fn.setValue方法，普通文本框在设置值后重新进行校验。add by zangys at 2014-10-17 11:27:09
 *      26.修改jQuery.fn.checkAll方法,当参数为false时不做必输项检查。add by zangys at 2014-10-22 10:18:42
 *      27.修改jQuery.fn.toJsonData方法，将id中[替换为\\[，]替换为\\]。add by zangys at 2014-10-28 17:02:53
 *      28.修改jQuery.fn.setValue方法，增加popbox分支。add by zangys at 2014-10-31 15:04:39
 *      29.修正EMP.createwin以最大化窗口的方式打开时，如果父页面出现滚动条时则引起打开窗口错位的问题。add by zangys at 2014-11-5 10:15:50
 *      30.修改jQuery.fn.setValue方法中combobox分支，避免增加value=''的选项。add by zangys at 2014-11-5 15:56:30
 *      31.修改下拉框失去焦点事件，target.combo('getValue')修改成getText方式，以解决在手动输入时，输入框失去焦点未获取到值的情况下赋值成功add by zangys at 2014-11-14 11:13:30
 *      32.修改bindOnchange方法 checkbox分支附加onchange失败的问题。add by zangys at 2015-03-09 15:20:23
 *      33.$.fn.setValue增加对年月框的处理。add by zangys at 2015-04-23
 *      34.修改滚动条覆盖住部分panel的问题。add by zangys at 2015-05-19
 *      35.修改numberbox数据类型百分比、千分比、利率类型显示精度问题。add by zangys at 2015-05-25 12:41
 *      36.修改EMP.createwin方式打开的窗口，弹出提示框时背景出现滚动条的问题。add by zangys at 2015-06-02 10:13
 *      37.修改修改EMP.createwin打开窗口，在IE8下窗口下移的问题及shadow与window错位的问题。add by zangys at 2015-06-09 12:39
 *      38.修改jQuery.transNumber4C方法小数超过4位就被四舍五入的问题，改为采用用户设置的精度为准
 *      39.修改IE11下打开窗口返回后，无法获取输入框的焦点
 *      40.修改setValue为扩展展示用的id选择器为属性选择器，兼容sst模块也静态页可能存在多个id相同的字段的问题。add by liwei at 2015-11-23 17:00
 *      41.修改createwin方法，默认可以修改大小2015-12-2
 *      42.修改addWithIframe方法，判断tabs是否已经打开该标签页，如打开了则切换选择它，如没有则打开标签页
 *      43.toForm 添加win参数，win为要组装成form的windows对象 add by liwei at 2015-12-21 20:42
 *      44.jQuery.maskNumber 修复大于-1的负数显示时不显示负号的问题 add by liwei 2016-01-19
 *      45.jQuery.textOnchangeEx方法的执行，有原来的通过eval改为call方法，call执行对象使用当前input的this对象  add by liwei 2016-01-26
 *		46.修改EMP.alertException，解决其没有对截取的msg code进行解析编码所对应的提示信息的问题 add by wangyang 2016-01-26
 */
//规范命名
var EMP = EMP||{};
(function(jQuery) {		
	/**
	 * <p>
	 * 获取并返回指定输入域的实际值
	 * </p>
	 */
	jQuery.fn.getValue = function() {

		try{
			var _id = jQuery(this).attr('id');
			
//			// 替换.为\\.以满足jquery特殊字符规范
//			_id = _id.replace('.', '\\.');
			//取当前组件的class样式
			var _cssClass = jQuery(this).attr('class');
//			if (_id == undefined) {
//				// alert('标签没有id属性，无法执行getValue方法!');
//				return '';
//			}
			if(_cssClass == undefined) { //添加undefined判断，修复普通文本框取值出错的bug liuhw@20140528
				_cssClass = "";
			}
			//扩展数据表格下拉框
			if(_cssClass.indexOf('combogridext')!=-1){
				var _v = jQuery(this).combogridext('getValue');
				return _v;
			}
			//数值类型取值
			if(_cssClass.indexOf('number')!=-1){
				var _v = jQuery(this).numberbox('getValue');
				return _v;
			}
			//combo类型取值
			else if(_cssClass.indexOf('combo')!=-1){
				//多选的情况下，是数组
				var values = jQuery(this).combo('getValues');
				var separator=$(this).combo('options').separator;
				if(values){
					for(var i=0;i<values.length;i++){
						if(i==0)
							_v=values[i];
						else{
							_v+=separator+values[i];
						}
					}
				}
				return _v;
			}
			//checkbox类型取值
			else if(_cssClass.indexOf('checkbox')!=-1){
				var _v=jQuery(this).checkbox('getValue');
				return _v;
			}
			//富文本框类型取值
			else if(_cssClass.indexOf('richtext') >= 0){
				return jQuery(this).richtext('getValue');
			}
			//文本取值
			else
				return jQuery(this).val();
		}catch(e){}
	};
	
	/**
	 * <p>
	 * 获取并返回指定输入域的显示值
	 * </p>
	 */
	jQuery.fn.getText = function() {

		try{
			var _id = jQuery(this).attr('id');
			
			//取当前组件的class样式
			var _cssClass = jQuery(this).attr('class');
			
			if(_cssClass == undefined) { //添加undefined判断，修复普通文本框取值出错的bug liuhw@20140528
				_cssClass = "";
			}
			//扩展数据表格下拉框
			if(_cssClass.indexOf('combogridext')!=-1){
				var _v = jQuery(this).combogridext('getText');
				return _v;
			}
			//数值类型取值
			if(_cssClass.indexOf('number')!=-1){
				var _v = jQuery(this).numberbox('getText');
				return _v;
			}
			//combo类型取值
			else if(_cssClass.indexOf('combo')!=-1){
				//多选的情况下，是数组
				var _v = jQuery(this).combo('getText');
				return _v;
			}
			//checkbox类型取值
			else if(_cssClass.indexOf('checkbox')!=-1){
				var _v=jQuery(this).checkbox('getTextValue');
				return _v;
			}
			//富文本框类型取值
			else if(_cssClass.indexOf('richtext') >= 0){
				return jQuery(this).richtext('getValue');
			}
			//文本取值
			else
				return jQuery(this).val();
		}catch(e){}
	};

	/**
	 * <p>
	 * 设置指定输入域的实际值，并返回该域的显示
	 * </p>
	 * <p>
	 * 	 <ul>1、该方法非常重要，重写的JQuery的setValue方法，需要考虑的场景有：表单、编加表格、查看模式(view=true)</ul>
	 * 	 <ul>2、从之前的cssClass改为class来判断控件类型,是因为编辑表格中没有cssClass样式</ul>
	 * 	 <ul>3、查看模式下的显示值通过display_value来处理，动态加载的数据通过该方法翻译，如果指定缺省值通过标签类来翻译</ul>
	 * </p>
	 * @param value
	 */
	jQuery.fn.setValue = function(value) {
		try{
			var _id = jQuery(this).attr('id');
			//取当前组件class样式
			var _cssClass = jQuery(this).attr('class');
			if (_cssClass == undefined) {
				_cssClass = "";
			}
			var _v,display_value;

			//数值组件
			if (_cssClass.indexOf('number') >= 0) {
				jQuery(this).numberbox('setValue', value);
				display_value=$(this).val();
			}
			//日期组件
			else if(_cssClass.indexOf('datebox-f combo-f')>=0 || _cssClass.indexOf('datetimebox')>=0){
				jQuery(this).datebox('setValue', value);
				display_value=$(this).combo('getText');
			}
			//联动下拉框(因为_cssClass中包含combobox,所以此分支要放在下拉列表分支前面)
			else if(_cssClass.indexOf('easyui-linkage') >= 0){
				jQuery(this).linkage('setValue', value);
				display_value=$(this).combo('getText');
			}
			//下拉列表
			else if(_cssClass.indexOf('combobox') >= 0){
				var separator = jQuery(this).combobox('options').separator;
				var _ary = value.split(separator);
				jQuery(this).combobox('setValues', _ary);
				if(value == ""){
					//避免下拉选项增加value=''的选项
					jQuery(this).combobox('clear');
				}
				display_value=$(this).combo('getText');
			}
			//扩展数据表格下拉框(因为_cssClass中包含combogrid,所以此分支要放在下拉数据表分支前面)
			else if(_cssClass.indexOf('combogridext')!=-1){
				var separator = jQuery(this).combogridext('options').separator;
				var _ary = value.split(separator);
				jQuery(this).combogridext('setValues', _ary);
				display_value=$(this).combogrid('getText');//修复使用combo获取text失效的bug
			}
			//下拉数据表
			else if (_cssClass.indexOf('combogrid') > 0) {
				var separator = jQuery(this).combogrid('options').separator;
				var _ary = value.split(separator);
				jQuery(this).combogrid('setValues', _ary);
				display_value=$(this).combogrid('getText');//修复使用combo获取text失效的bug
			} 
			//下拉树
			else if (_cssClass.indexOf('combotree') > 0) {
				var separator = jQuery(this).combotree('options').separator;
				var _ary = value.split(separator);
				jQuery(this).combotree('setValues', _ary);
				display_value=$(this).combo('getText');
			} 
			//radio、checkbox
			else if (_cssClass.indexOf('checkbox') >= 0) {
				jQuery(this).checkbox('setValue', value);
			}
			//富文本框
			else if(_cssClass.indexOf('richtext') >= 0){
				return jQuery(this).richtext('setValue',value);
			}
			//pop框
			else if(_cssClass.indexOf('popbox') >= 0){
				jQuery(this).popbox('setValue', value);
				display_value=jQuery(this).val();
				jQuery(this).validatebox("validate");
			}
			//年月框
			else if(_cssClass.indexOf('dateymbox') >= 0){
				jQuery(this).dateymbox('setValue', value);
				display_value = jQuery(this).combo('getText');
			}
			//缺省：普通文本框
			else {
				jQuery(this).val(value);
				display_value=$(this).val();
				jQuery(this).validatebox("validate");
			}
			
			_v = value;
			
			$(this).checkAll();// check current field
			if(_id){
				/**
				 * updated by liwei at 2015-11-23 
				 * 修改赋值的选择器为属性选择器，兼容sst模块也静态页可能存在多个id相同的字段
				 */
				$('[id="'+'view_'+_id.replace('.', '\\.')+'"]').text(display_value);
				//$('#view_'+_id.replace('.', '\\.')).text(display_value);//查看时输出显示值
			}
			return _v;
			
			//年月暂未处理
//			else if (_dType == 'YYMM') {// 年月
//				jQuery(this).combo('setValue', value).combo("setText",value);
//				display_value=$(this).combo('getText');
//			} 
		}catch(e){}
		
	};

	/**
	 * <p>
	 * 设置必输
	 * </p>
	 * @param true必输、false非必输
	 */
	jQuery.fn.renderRequired = function(isRequired) {
		$.each($(this), function(key, field) {
			var _dType = $(field).attr('dType');
			var _id = $(field).attr('id');
			//是否组合标签
			if(_dType!=null&&_dType=='FieldLayout'){
				$.each($('#field_'+_id.replace('.', '\\.')).find('input[id],select[id],textarea[id]'), function(k, f) {
					doRenderRequired($(f),isRequired)
				});
				if (isRequired) {
					jQuery("#flag_" + _id).show();
				} else {
					jQuery("#flag_" + _id).hide();
				}
			}else{
				doRenderRequired($(field),isRequired)
			}
		});
		
		function doRenderRequired(jq,isRequired){
			var opts;
			var _id = jq.attr('id');
			//根据类型取得options对象
			if (jq.hasClass('combo-f')) {
				opts=jq.data().combo.options;
				//修改combobox,combogrid,combotree在required=true时，通过renderRequired设置required=false后,叹号没有消失时，checkAll校验必输时返回false的bug
				$.data($.data(jq[0],'combo').combo.find('input.combo-text')[0],'validatebox').options.required=isRequired;
			} else if (jq.hasClass('mis-radio')||jq.hasClass('mis-checkbox')) {
				opts=jq.data().checkbox.options;
				//由于radio、checkbox组件使用了隐藏validatebox-text，不需要对其进行检查，需要检查的是其选择项。
				var last=jq.data().checkbox.field.find('input').last();
				$(last).validatebox({validType: opts.dataType+"['"+_id+"']"});
				if(isRequired){
					$(last).removeClass('validatebox-text').addClass('validatebox-text');
				}else{
					$(last).removeClass('validatebox-text').removeClass('validatebox-invalid');
				}
			}else if(jq.hasClass('richtext')){
				opts=jq.richtext('options');
			} else {
				opts=jq.data().validatebox.options;
			}
			
			// 替换.为\\.以满足jquery特殊字符规范
			if(_id){
				_id = _id.replace('.', '\\.');
			}
			if (isRequired) {
				opts.required=true;
				jQuery("#flag_" + _id).show();
			} else {
				opts.required=false;
				jQuery("#flag_" + _id).hide();
			}
			//重新检查
			jq.checkAll();
		}
	};
	
	/**
	 * <p>
	 * 设置隐藏
	 * </p>
	 * @param true隐藏、false显示
	 */
	jQuery.fn.renderHidden = function(ishide) {
		$.each($(this), function(key, field) {
			var _id = $(field).attr('id');
			// 替换.为\\.以满足jquery特殊字符规范
			if(_id){
				_id = _id.replace('.', '\\.');
			}
			if (ishide == true) {
				jQuery("#field_" + _id).hide();
			} else {
				jQuery("#field_" + _id).show();
			}
			//在table布局中自动缩进
			var parent=jQuery("#field_" + _id).parent();
			if(parent.is("td")){
				if (ishide == true) {
					parent.hide();
				} else {
					parent.show();
				}
			}
			
		});
		
	};

	/**
	 * <p>
	 * 设置不可用。
	 * </p>
	 * @param true可用、false不可用
	 * 
	 */
	jQuery.fn.renderDisabled = function(isDisable) {
		$.each($(this), function(key, field) {
			var _dType = jQuery(field).attr('dType');
			var _id = jQuery(field).attr('id');
			//是否组合标签
			if(_dType!=null&&_dType=='FieldLayout'){
				$.each($('#field_'+_id.replace('.', '\\.')).find('input[id],select[id],textarea[id]'), function(k, f) {
					doRenderDisabled($(f),isDisable)
				});
			}else{
				doRenderDisabled($(field),isDisable)
			}
		});
		
		function doRenderDisabled(jq,isDisable){
			var _id = jq.attr('id');
			if (jq.hasClass('combo-f')) {//combo类组件
				if (isDisable == true) {
					jq.combo('disable');
				} else {
					jq.combo('enable');
				}
			} else if (jq.hasClass('mis-radio')||jq.hasClass('mis-checkbox')) {//radio、checkbox需要对选择项input全部设置是否可用
				jQuery("input[name=" + _id + "]").attr("disabled", isDisable);
			}else if(jq.hasClass('popbox')){
				if (isDisable == true) {
					jq.popbox('disable');
					jq.attr("disabled", true);
				} else {
					jq.popbox('enable');
				}
			}else if(jq.hasClass('spinner-f')){//微调组件
				if (isDisable == true) {
					jq.spinner('disable');
				} else {
					jq.spinner('enable');
				}
			} else {
				jq.attr("disabled", isDisable);
			}
		}
		
	};

	/**
	 * <p>
	 * 设置只读
	 * </p>
	 * @param true只读、false非只读
	 * 
	 */
	jQuery.fn.renderReadonly = function(isReadonly) {
		$.each($(this), function(key, field) {
			var _dType = jQuery(field).attr('dType');
			var _id = jQuery(field).attr('id');
			//是否组合标签
			if(_dType!=null&&_dType=='FieldLayout'){
				$.each($('#field_'+_id.replace('.', '\\.')).find('input[id],select[id],textarea[id]'), function(k, f) {
					doRenderReadonly($(f),isReadonly)
				});
			}else{
				doRenderReadonly($(field),isReadonly)
			}
		});
		
		function doRenderReadonly(jq,isReadonly){
			var _id = jq.attr('id');
			//combo和spinner均使用disabled进行只读设置，不影响取值。
			if (jq.hasClass('combo-f')) {
				if (isReadonly == true) {
					jq.combo('disable');
				} else {
					jq.combo('enable');
				}
			}else if(jq.hasClass('spinner-f')){
				if (isReadonly == true) {
					jq.spinner('disable');
				} else {
					jq.spinner('enable');
				}
				
			}//radio,checkbox只能使用disabled。  
			else if (jq.hasClass('mis-radio')||jq.hasClass('mis-checkbox')) {
				jQuery("input[name=" + _id + "]").attr("disabled", isReadonly);
			}else if(jq.hasClass('popbox')){
				if (isReadonly == true) {
					jq.popbox('disable');
				} else {
					jq.popbox('enable');
				}
			}else if(jq.hasClass('richtext')){
				if (isReadonly == true) {
					jq.richtext('disable');
				} else {
					jq.richtext('enable');
				}
			} else {
				jq.attr("readonly", isReadonly);
			}
		}
		
	};
	
	/**
	 * <p>
	 * 检查选择器对应的所有域(单个)的正确性，若其中有一个域检查不通过，则返回FALSE，全通过返回TRUE 用法
	 * @param isRequired 是否检查必输项默认检查，当参数为false时不再检查必输项 add by wangbin 2014-5-13 17:22:12
	 * <ul>添加参数类型判断：不传参数则默认全部检查，传递false则表示全部不检查,传递逗号分隔的id组成的字符串，则表示此类id不做必输项校验，add by Pansq 2014-05-20</ul>
	 * <ul>isRequired为false时不做必输项检查 add by zangys at 2014-10-22 10:09:20</ul>
	 * </p>
	 * 
	 */
	jQuery.fn.checkAll = function(isRequired) {
		var findList = "";
		if(isRequired == undefined){
			isRequired = true;
		}else {
			if(typeof isRequired == "boolean"){
				if(isRequired){
					isRequired = true;
				}else {
					//全部不检查必输,只校验数据类型
					//return true;
				}
			}else if(typeof isRequired == "string"){
				//组装筛选参数
				var fieldList = isRequired.split(',');
				for(var i=0;i<fieldList.length;i++){
					findList += "#"+ fieldList[i];
					if(i<fieldList.length-1){
						findList +=",";
					}
				}
			}
		}
		if($.fn.validatebox){
			$(this).find(".combobox-f,.combogrid-f,.combotree-f").each(function(){
				var opts=$(this).combo("options");
				var value=$(this).combo("getValue");
				if(opts.required&&(value==null||value.length<=0)){
					$(this).combo("setText","");
				}
			});
			// 1.排除easyui-checkbox：由于checkbox组件使用了隐藏validatebox-text但不需要对其进行检查，需要检查的是其选择项。
			var fields = [];
			if(isRequired){
				fields = $(this).find(".validatebox-text:not(.easyui-checkbox):not(div)").not(findList);
			}
			else{
				//easyui-checkbox选项只做必输项检查
				fields = $(this).find(".validatebox-text:not(.easyui-checkbox):not(div)[type!=radio][type!=checkbox]").not(findList);
			}
			var res=false;//遍历结果
			var first=null;//存放第一个
			var temp=false;//过程判断变量，当遇见第一个未输项时变为true
			//遍历
			$.each(fields,function(i,field){
				var val=$(field).getValue();
				//如果不不检查必输项，则当内容为空时跳过后续的检查
				if(isRequired||(val!=null&&val.length>0)){
					if($(field).hasClass('richtext')){
						res=$(field).richtext("validate");
					}else{
						res=$(field).validatebox('isValid');
					}
					if(!res&&!temp){
						first=$(field);
						temp=true;
					}
				}
			});
			//重置焦点到第一个未输入必输项
			if(temp){
				if(first.hasClass('richtext')){
					first.richtext('focus');
				}else{
					first.focus();
				}
				res=false;
			}else{
				res=true;
			}
			return res;
		}
		return true;
	};
	
	/**
	 * <p>
	 * 将选择器对应的所有域(单个)组装成JSON数据 用法 $(jq选择表达式).toJsonData();
	 * </p>
	 * @param jsonData：传入的json对象，该参数可为空。为空时会自动创建json对象
	 */
	jQuery.fn.toJsonData = function(jsonData) {
		//如果未定义，则进行定义
		jsonData=jsonData||{};
		$.each($(this), function(key, field) {
			//div、form时遍历出所有数据域
			if($(this).is("div")||$(this).is("form")){
				$.each($(field).find('input[id],select[id],textarea[id],.easyui-datagrid'), function(k, f) {
					putData($(f));
				});
			}else{
				putData($(field));
			}
			
			function putData(field){
				if(field.is('table')){//当数据域为table时组装table当前所有data数据
					var _id=field.attr('id');
					var rows=field.datagrid('getData').rows;
					EMP.toJsonDataAry(_id,rows,jsonData);
				}else{
					var id=$(field).attr('id');
					// 替换.为\\.以满足jquery特殊字符规范 ，替换[为\\[,替换]为\\]
					var value=$('#'+id.replace('.', '\\.').replace('[','\\[').replace(']','\\]')).getValue();
					//zhangwx20140507 添加trim方法，去除内容前和内容后的空格
					value = $.trim(value);
					id = id.replace('__','.');//将__换成. 用以在后台组成KCOL数据
					id = id.replace('-','.');//将-换成. 用以在后台组成KCOL数据
					jsonData[id]=''+value;//避免combo类数据以对象形式加入json
				}
			};
			
		});
		return jsonData;
	};

	/**
	 * <p>
	 * 将datagrid数据组装成JSON数据(EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...)
	 * </p>
	 * @param icolid:需要设置的模型id
	 * @param rows:datagrid行数据
	 * @param json:传入的json对象用于存放数据可为空，为空时自动创建 
	 */
	EMP.toJsonDataAry = function(icolid,rows,json){
		json=json||{};
		//EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...
		$.each(rows, function(key,value){
			$.each(value, function(_key,_value){
				var _newkey = icolid+'[' + key + '].'+_key;
					//zhangwx20140507 添加trim方法，去除内容前和内容后的空格
					json[_newkey] = $.trim(_value);
				 //json[_newkey] = _value ;
			});	
		});
		return json;
	}
	/**
	 * <p>
	 * 	将nodes数据组装成JSON数据(EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...)
	 * </p>
	 * <p>
	 * 	因为树的数据是由id和text组成，为了方便后台根据“对象名.属性名”的方法来取值，转换id和text为期望的字段名(属性名)
	 * </p>
	 * @param icolid:需要设置的模型id
	 * @param o:配置对象{idField:'id',textField:'text'}用于指定转换时id/text翻译，一般对应于tree标签中的idField/textField
	 * @param nodes:tree的节点数据
	 * @param json:传入的json对象用于存放数据可为空，为空时自动创建 
	 */
	EMP.toJsonDataAry4Nodes=function(icolid,o,nodes,json){
		json=json||{};
		//EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...
		$.each(nodes, function(key,node){
			var _newkey = icolid+'[' + key + '].'+o.idField;
			var _newText= icolid+'[' + key + '].'+o.textField;
			json[_newkey] = node.id ;
			json[_newText] = node.text ;
		});
		return json;
	}
	
	/**
	 * 将选择器对应的DataGrid中数据组装成JSON数据-为可编辑列表统一组装数据
	 * (EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...)
	 * 自动转化成四组数据：新增inserted、已修改deleted、已删除updated、当前所有all
	 */
	jQuery.fn.toJsonDataAry4Edit = function(json){
		json=json||{};
		var _data = null;
		var _id = jQuery(this).attr('id');//以当前DataGrid的ID为对象名
		var _datastr = '';
		_data = jQuery(this).datagrid('getChanges','updated');
		if(_data != undefined || _data != null){
			$.each(_data, function(key,value){
				$.each(value, function(_key,_value){
					var _newkey = 'updated.'+_id+'[' + key + '].'+_key;
					json[_newkey] = _value;
					// _datastr += ",'" + _newkey + "':'" + _value + "'";
				});	
			});
		}
		_data = jQuery(this).datagrid('getChanges','inserted');
		if(_data != undefined || _data != null){
			$.each(_data, function(key,value){
				$.each(value, function(_key,_value){
					var _newkey = 'inserted.'+_id+'[' + key + '].'+_key;
					json[_newkey] = _value;
					// _datastr += ",'" + _newkey + "':'" + _value + "'";
				});	
			});
		}
		_data = jQuery(this).datagrid('getChanges','deleted');
		if(_data != undefined || _data != null){
			$.each(_data, function(key,value){
				$.each(value, function(_key,_value){
					var _newkey = 'deleted.'+_id+'[' + key + '].'+_key;
					json[_newkey] = _value;
					// _datastr += ",'" + _newkey + "':'" + _value + "'";
				});	
			});
		}
		_data = jQuery(this).datagrid('getRows');
		if(_data != undefined || _data != null){
			$.each(_data, function(key,value){
				$.each(value, function(_key,_value){
					var _newkey = 'all.'+_id+'[' + key + '].'+_key;
					if(_key != 'isNewRecord'){
						json[_newkey] = _value;
					}
					// _datastr += ",'" + _newkey + "':'" + _value + "'";
				});	
			});
		}
		//_datastr = "({" + _datastr.substring(1) + "})";
		//return eval(_datastr);
		return json;
	};
	/**
	 * 将选择器对应的DataGrid中数据组装成JSON数据-为可编辑列表统一组装数据
	 * (EMP能识别的数组结构: icol[0].key=value icol[1].key=value ...)
	 * 自动转化成四组数据：新增inserted、已修改deleted、已删除updated、当前所有all
	 * 在列表假分页时，得到列表中的全部数据（非当前页）
	 */
	jQuery.fn.toJsonDataAry4EditWithAllTabData = function(json){
		json=json||{};
		var _data = null;
		var _id = jQuery(this).attr('id');//以当前DataGrid的ID为对象名
		var _datastr = '';
		_data = jQuery(this).datagrid('getData');
		if(_data != undefined || _data != null){
			// rows记录的是当前页数据，originalRows是记录的全部数据
			_data = _data.originalRows;
			$.each(_data, function(key,value){
				$.each(value, function(_key,_value){
					var _newkey = 'all.'+_id+'[' + key + '].'+_key;
					if(_key != 'isNewRecord'){
						json[_newkey] = _value;
					}
					// _datastr += ",'" + _newkey + "':'" + _value + "'";
				});	
			});
		}
		return json;
	}; 
	/**
	 * <p>
	 * 将选择器对应的所有域(单个)组装成页面FORM，以备提交后台，不建议使用。建议使用toJsonData方法获取数据，然后调用post/get/ajax
	 * </p>
	 * @param formId
	 * @param win
	 * 
	 * 
	 * 1.  添加win参数，win为要组装成form的windows对象
	 *     例如: $('#updateDetailForm').toForm('form1',window.parent);,是将updateDetailForm中的数据封装到当前页面的父页面的form1中
	 *     add by liwei at 2015-12-21 20:42
	 */
	jQuery.fn.toForm = function(formId,win) {
        var jq;
        if(win){
            jq=win.$('#' + formId);
        }else{
            jq=$('#' + formId);
        }
        /** 将选择器所指定的所有符合条件的域加入指定FORM中 */
        $.each($(this), function(key, field) {
            if($(this).is("div")||$(this).is("form")){
                $.each($(field).find('input[id],select[id],textarea[id]'), function(k, f) {
                    var id=$(f).attr('id');
                    // 替换.为\\.以满足jquery特殊字符规范
                    var value=$('#'+id.replace('.', '\\.')).getValue();
                    id = id.replace('__','.');//将__换成. 用以在后台组成KCOL数据
                    id = id.replace('-','.');//将-换成. 用以在后台组成KCOL数据
                    jq.find("input[name=\""+id+"\"]").remove();
                    $("<input type=\"hidden\" id=\""+id+"\" name=\""+id+"\">").val(value).appendTo(jq);//添加id,否则新添加的字段不能使用s/getValue liuhw@20140528 
                });
            }else{
                var id=$(field).attr('id');
                // 替换.为\\.以满足jquery特殊字符规范
                var value=$('#'+id.replace('.', '\\.')).getValue();
                id = id.replace('__','.');//将__换成. 用以在后台组成KCOL数据
                id = id.replace('-','.');//将-换成. 用以在后台组成KCOL数据
                jq.find("input[name=\""+id+"\"]").remove();
                $("<input type=\"hidden\" id=\""+id+"\" name=\""+id+"\">").val(value).appendTo(jq);//添加id,否则新添加的字段不能使用s/getValue liuhw@20140528
            }
            
        });
    }

	/** 
	 * <p>除法运算 返回d1 / d2，运算精度scale</p>
	 * @param d1被除数
	 * @param d2除数
	 * @param scale精度
	 */
	jQuery.div = function(d1, d2, scale) {
		var _v1 = parseFloat(d1);
		var _v2 = parseFloat(d2);
		_v1 = _v1 / _v2;
		_v1 *= Math.pow(10, scale);
		_v1 = Math.round(_v1);
		_v1 /= Math.pow(10, scale);
		return _v1;
	};

	/**
	 *<p> 乘法运算 返回d1 * d2，运算精度scale</p>
	 *@param d1被乘数
	 *@param d2乘数
	 *@param scale精度 
	 */
	jQuery.mul = function(d1, d2, scale) {
		var _v1 = parseFloat(d1);
		var _v2 = parseFloat(d2);
		_v1 = _v1 * _v2;
		_v1 *= Math.pow(10, scale);
		_v1 = Math.round(_v1);
		_v1 /= Math.pow(10, scale);
		return _v1;
	};
	
	/**
	 * <p>表格数值类数据格式化</p>
	 * <p>
	 * 修改历史：
	 *  1. 修复大于-1的负数显示时不显示负号的问题 add by liwei 2016-01-19
	 * </p>
	 * @param _num 值
	 * @param precision 精确度
	 * @param decimalSeparator 整数与小数的分割符
	 * @param groupSeparator 整数组分隔符例如：999,999.00
	 * @param prefix 前缀
	 * @param suffix 后缀
	 */
	jQuery.maskNumber = function(_num,precision,decimalSeparator,groupSeparator,prefix,suffix) {
		//根据参数precision重置精确度
		_num=parseFloat(_num).toFixed(precision);
		_num = new String(_num);
		//拆分整数、小数部分
		var a = _num.split('.', 2);
		var d = a[1];
		var i = parseInt(a[0]);
		if (isNaN(i)) {
			return _num;
		}
		var minus = '';
		if (_num < 0) {
			minus = '-';
		}
		i = Math.abs(i);
		var n = new String(i);
		//是否分组
		if(groupSeparator){
			var a = [];
			while (n.length > 3) {
				var nn = n.substr(n.length - 3);
				a.unshift(nn);
				n = n.substr(0, n.length - 3);
			}
			if (n.length > 0) {
				a.unshift(n);
			}		
			n = a.join(groupSeparator);
		}
		
		if (d) {
			_num = n + decimalSeparator + d;
		} else {
			_num = n;
		}
		_num =minus + _num;
		//添加前缀
		if(prefix){
			_num =prefix+ _num;
		}
		//添加后缀
		if(suffix){
			_num =_num+suffix;
		}
		return _num;
	};

	/**
	 * <p>属性表格porperty value值的格式化：根据每行数据携带的formatter进行格式化处理</p>
	 * <p>eg:设置property组件value的formatter如下columns:[[
    		{field:'name',title:'MyName',width:150,sortable:true},
   		    {field:'value',title:'MyValue',width:150,resizable:true,formatter:$.transValueC}
        ]]</p>
	 * @param value 当前值
	 * @param row 列表行数据
	 */
	jQuery.transValueC= function(val,row){
		//获取当前列的formatter并调用该方法
		if(row.formatter){
			return row.formatter(val,row);
		}
		return val;
	}
	
	/** 
	 * <p>列表中列转金额数字（用于列表column的formatter方法）</p>
	 * @param value 当前值
	 * @param row 列表行数据
	 * 
	 */
	jQuery.transCurrency4C = function(val, row) {
		if (val != null && !isNaN(val) && val != '') {
			var precision=2;
			//table中使用formatter
			if(row){
				if(this.precision)
					precision=this.precision;
				val = $.maskNumber(val,precision,this.decimalSeparator,this.groupSeparator,this.prefix,this.suffix);
			}else{
				var state=$.data(this, 'numberbox');
				var opts=state.options;
				if(opts.precision)
					precision=opts.precision;
				val = $.maskNumber(val,precision,opts.decimalSeparator,opts.groupSeparator,opts.prefix,opts.suffix);
			}
		}
		return val;
	};
	
	/**
	 * <p> 列表中列转double数字（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transDouble4C = function(val, row) {
		if (val != null && !isNaN(val) && val != '') {
			var precision=2;
			//table中使用formatter
			if(row){
				if(this.precision)
					precision=this.precision;
				val = $.maskNumber(val,precision,this.decimalSeparator,null,this.prefix,this.suffix);
			}else{
				var state=$.data(this, 'numberbox');
				var opts=state.options;
				if(opts.precision)
					precision=opts.precision;
				val = $.maskNumber(val,precision,opts.decimalSeparator,null,opts.prefix,opts.suffix);
			}
		}
		return val;
	};
	
	/** 
	 * <p>列表中列转百分比（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transPercent4C = function(val, row) {
		if (val != null && !isNaN(val) && val != '') {
			var _v = parseFloat(val);
			var precision=4;
			//table中使用formatter
			if(row){
				if(this.precision)
					precision=this.precision;
				
				if(precision > 2){
					precision -= 2;
				}else if(precision <= 2)
					precision = 0;
				val = $.mul(_v, 100, precision);
				val = $.maskNumber(val,precision,this.decimalSeparator,null,this.prefix,'%');
			}else{
				var state=$.data(this, 'numberbox');
				var opts=state.options;
				if(opts.precision)
					precision=opts.precision;
				
				if(precision > 2){
					precision -= 2;
				}else if(precision <= 2)
					precision = 0;
				val = $.mul(_v, 100, precision);
				val = $.maskNumber(val,precision,opts.decimalSeparator,null,opts.prefix,'%');
			}
			
		}
		return val;
	};
	/**
	 * numberbox标签默认formatter，以保持numberbox的formatter在form与datagrid中一致
	 * 针对在不指定dataType类型时，数字标签在form中能格式化显示，而在datagrid中不能格式化显示的情况。
	 * @param val 当前值
	 * @param row 列表行数据(在datagrid中，在form中row值为undefined)
	 */
	jQuery.transNumber4C = function(val, row){
		//datagrid中
		if(row){
			if(this.tagType=='numberbox'){
				var _3fb=val;
				if(!_3fb){
					return _3fb;
				}
				_3fb=_3fb+"";
				var opts=this;
				var s1=_3fb,s2="";
				var dpos=_3fb.indexOf(".");
				
				if(opts.prefix==undefined)
					opts.prefix="";
				if(opts.suffix==undefined)
					opts.suffix="";
				
				if(dpos>=0){
					s1=_3fb.substring(0,dpos);
					s2=_3fb.substring(dpos+1,_3fb.length);
				}
				if(opts.groupSeparator){
					var p=/(\d+)(\d{3})/;
					while(p.test(s1)){
						s1=s1.replace(p,"$1"+opts.groupSeparator+"$2");
					}
				}
				if(s2){
					return opts.prefix+s1+opts.decimalSeparator+s2+opts.suffix;
				}else{
					return opts.prefix+s1+opts.suffix;
				}
			}
		}else{
			//form表单中
			var state=$.data(this, 'numberbox');
			if(state)
				return $.fn.numberbox.defaults.formatter.call(this,val);
		}
		return val;
	};
	/**
	 * <p> 列表中列转年利率（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transRate4C = function(val, row) {
		if (val != null && !isNaN(val) && val != '') {
			var _v = parseFloat(val);
			val = $.mul(_v, 100, 8) ;
			var precision=8;
			//table中使用formatter
			if(row){
				if(this.precision)
					precision = this.precision;
				
				if(precision > 2){
					precision -= 2;
				}else if(precision <= 2)
					precision = 0;
				/******处理无效尾数0的展示效果 add  by xumt start*******/
				if(precision>2){
					var valStr=val+"";
					var sp=valStr.split(".");
					if(sp.length>1 && sp[1].length>2){
						if(precision>sp[1].length){
							precision=sp[1].length;
						}
					}else{
						precision=2;
					}
				}
				/******处理无效尾数0的展示效果 add  by xumt end*******/
				val = $.maskNumber(val,precision,this.decimalSeparator,null,this.prefix,'%');
			}else{
				var state=$.data(this, 'numberbox');
				var opts=state.options;
				if(opts.precision)
					precision=opts.precision;
				
				if(precision > 2){
					precision -= 2;
				}else if(precision <= 2)
					precision = 0;
				/******处理无效尾数0的展示效果 add  by xumt start*******/
				if(precision>2){
					var valStr=val+"";
					var sp=valStr.split(".");
					if(sp.length>1 && sp[1].length>2){
						if(precision>sp[1].length){
							precision=sp[1].length;
						}
					}else{
						precision=2;
					}
				}
				/******处理无效尾数0的展示效果 add  by xumt end*******/
				val = $.maskNumber(val,precision,opts.decimalSeparator,null,opts.prefix,'%');
			}
		}
		return val;
	};
	/**
	 * <p> 列表中列转千分比（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transPermille4C = function(val, row) {
	
		if (val != null && !isNaN(val) && val != '') {
			var _v = parseFloat(val);
			val = $.mul(_v, 1000, 9);//千分比数据,防止提前丢失精度
			var precision=2;
			//table中使用formatter
			if(row){
				if(this.precision)
					precision=this.precision;
		
				if(precision > 3){
					precision -= 3;
				}else if(precision <= 3)
					precision = 0;
	
				val = $.maskNumber(val,precision,this.decimalSeparator,null,this.prefix,'‰');
			}else{
				var state=$.data(this, 'numberbox');
				var opts=state.options;
				if(opts.precision)
					precision=opts.precision;
				if(precision > 3){
					precision -= 3;
				}else if(precision <= 3)
					precision = 0;
				val = $.maskNumber(val,precision,opts.decimalSeparator,null,opts.prefix,'‰');
			}
		}
		return val;
	};


	/**
	 * <p> 列表中列转数字典项（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transDict4C = function(val, row) {
		var dict = page.objectsDefine.dataDics[this.dictname];// dictname来自datagrid中column的data-options
		if (dict) {
			var displayValue='';
			//支持字典项多选翻译
			if(val){
				var _ary = val.split(this.separator);
				for ( var k = 0; k < _ary.length; k++) {
					var temp=EMP.queryJson(dict,"@enname=='"+_ary[k]+"'");
					if(temp)
						displayValue += temp.cnname;
					else
						continue;
					if(k!=_ary.length-1)
						displayValue += this.separator;
						
				}
				return displayValue;
			}
		}
		return val;
	}

	/** 
	 * <p>列表中列转checkbox（用于列表column的formatter方法）</p>
	 * @param val 当前值
	 * @param row 列表行数据
	 */
	jQuery.transCheckbox4C = function(val, row) {
		var data ;// 取得选择项数据
		if(this.data){
			data=this.data;
		}else{
			$.post(this.url,null,function(data){
				data=data; 
				},"json");
		}
		if (data) {
			var displayValue='';
			//支持字典项多选翻译
			if(val){
				var _ary = val.split(this.separator);
				for ( var k = 0; k < _ary.length; k++) {
					var temp=EMP.queryJson(data,"@"+this.valueField+"=='"+_ary[k]+"'");
					if(temp)
						displayValue += temp[this.textField];
					else
						continue;
					if(k!=_ary.length-1)
						displayValue += this.separator;
						
				}
				return displayValue;
			}
		}
		return val;
	}
	
	/**
	 *<p> 检查值中是否包含特殊字符（" < > { } = ( )）, 用于防止页面注入 返回true表示包含特殊字符</p>
	 *@param _v输入的字符串
	 */
	jQuery.checkSpecialChar = function(_v) {
		var reg = new RegExp('["<>{}=()\^]');//
		return reg.test(_v);
	}
	
	/**
	 * 对text标签onchange事件的扩展方法
	 * 扩展之后占用html的onchange事件，分别执行emp标签设置的
	 * beforeOnchange onchange afterOnchange方法
	 */
    jQuery.textOnchangeExt = function(target){
        var opts = $.parser.parseOptions(target);
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(target);
        }
        if(opts.onchange){
            opts.onchange.call(target);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(target);
        }
    } 
	/**
	 * <p>使用Iframe添加标签页</p>
	 * <p>看标签页是否存在，如存在切换到该页签，没有才新增</p>
	 * @param options为tabs选项卡属性配置对象使用url加载远程内容
	 */
	$.fn.addWithIframe=function(options){
		var opts=$.extend({
			title:CusLang.EMP.jqFnExt.addWithIframe_defaultTitle,
			closable:true
		},options);
		if ($(this).tabs('exists', options.title)) {
			$(this).tabs('select', options.title);
		} else {
			var content = '<iframe scrolling="auto" frameborder="0"  src="'+options.url+'" style="width:100%;height:99%;"></iframe>'; 
			opts.content=content;
			$(this).tabs('add',opts)
		}
	}

	/**
	 * <p>
	 * <ul>提供基于iframe的dialog实现，保证引入页面的可用性</ul>
	 * </p>
	 * <ul>将iframe的高度改为99%，防止重复出现滚动条问题</ul>
	 * <ul>如果要顶层打开窗口，请使用top.EMP.createwin</ul>
	 * @param options为配置参数对象{id:id,title:'标题',url:'url地址',width:800,height:300,modal:true,draggable:true,resizable:true,maximized:false,maximizable：true,toolbar}
	 * 参考dialog属性和方法
	 * id:唯一标识
	 * title:弹出窗口的标题
	 * url:弹出窗口的内容
	 * width:窗口宽度，默认600
	 * height:窗口高度，默认300
	 * modal:是否模态窗口,默认true
	 * draggable:是否可拖动，默认true
	 * resizable:是否可改变大小，默认true
	 * maximized:是否以最大化窗口的方式打开，默认true
	 * maximizable:是否有最大化窗口的按钮
	 * toolbar:工具栏
	 */
	EMP.createwin=function(options) {
		var opts=$.extend({
				width : 600,
				height : 300,
				modal:true,
				resizable:true,
				maximized:true,
				maximizable:true,
				onClose : function() {
					$(this).dialog('destroy');
				}
		},options);
		if (options.url) {
			opts.content = '<iframe id="" src="" allowTransparency="true" scrolling="auto" width="100%" height="98%" frameBorder="0" name=""></iframe>';
		}
		win=$('<div/>');  
		var dia = win.dialog(opts);
		var diaWin = dia.dialog('window');
		if(opts.maximized){
			//修正以最大化窗口的方式打开时，如果父页面出现滚动条时则引起打开窗口错位的问题
			diaWin.css('top',$(window).scrollTop());
		}
		dia.find("iframe").attr("src",options.url);
		
		//修正shadow与window错位的问题
		var diawin = $.data(dia[0],"window");
		if(diawin.shadow){
			diawin.shadow.css("top",diawin.window.position().top);
		}
		
		
		//针对页面弹出提示窗口时出现滚动条的情况 2015-06-02
		dia.find("iframe").attr('height',dia.find('div.panel').height()-4);
		
		//根据浏览器大小的改变调整dialog页面自身大小 add by wangyf10 2016/6/24
		/**厦门国际银行改造，注释这段代码解决IE8浏览器不兼容问题 start**/
		/*window.onresize=function(){
	        dia.dialog('resize');
	    }*/
		/**厦门国际银行改造，注释这段代码解决IE8浏览器不兼容问题 end**/
		return dia;
	};
	//关闭打开的窗口
	EMP.closewin=function(){
		//IE 下在移除父层的 DIV 的时候，也就是 IFrame 外层的 DIV 的时候,IE 并没有将内部的 IFrame 从 DOM 中移除
		//为了解决IE11下打开窗口返回后，无法获取输入框的焦点
		var frame = $('iframe', win);
		if ($.browser.msie) {
            CollectGarbage();
        }
        if (frame.length > 0) {
            frame[0].contentWindow.document.write('');
            frame[0].contentWindow.close();
            frame.src = "";
            frame.remove();
        }
		win.dialog('close');
	}

	
	/** 
	 *<p> JS 实现 JSON查询:通过@匹配子元素例如exp='@enname==1'即查询json对象下子元素enname==1的对象。</p>
	 *@param queryArr json对象
	 *@param exp 表达式
	 **/
	EMP.queryJson=function(queryArr,exp){
		//修正_tempFun在IE8下报语法错识的问题 add by yuhq at 2014-9-2 11:18:19
		var _tempFun= 'function(queryArr,exp) { 		    '+
			'	for (var i = 0; i < queryArr.length; i++) {    '+
			'		var e = queryArr[i];  									'+
			'		if ($exp) {													'+
			'			return queryArr[i]; 									'+
			'		}  																'+
			'	}  																	'+
			' return null; 														'+
			'}  ';
		
		var funStr=_tempFun.replace('$exp',exp).replace('@','e.');
		var fun=eval('0,'+funStr);
		return fun(queryArr,exp);
	}
	
//	/** 
//	 *<p> JS 实现 JSON查询:通过@匹配子元素例如exp='@enname==1'即查询json对象下子元素enname==1的对象。</p>
//	 *@param queryArr json对象
//	 *@param exp 表达式
//	 **/
//	EMP.queryJson=function(queryArr,exp){
//		var funStr=_tempFun.replace('$exp',exp).replace('@','e.');
//		var fun=eval('0,'+funStr);
//		return fun(queryArr,exp);
//	}
//	/**
//	 * <p>模板函数，配合queryJson使用</p>
//	 */	
//	var _tempFun=function(queryArr,exp) {
//		for (var i = 0; i < queryArr.length; i++) {
//			var e = queryArr[i];
//			if ($exp) {
//				return queryArr[i];
//			}
//		}
//		return null;
//	}.toString();	

	/**
	 * <p>构造搜索框查询参数json对象</p>
	 * @param id 搜索框对应menuid
	 * @param name 当前查询名称
	 * @param value 输入值
	 * @return json对象
	 */
	EMP.searchParams=function(id,name,value){
		var params={};
		if(name=='all'){
			$.each($('#'+id+'>div'), function(key, val){
				var options=jQuery(val).attr('data-options');
				if(options){
					var name=options.split(':')[1].replace(/\'/g,"");
					params[name]=value;
				}
			});
		}else{
			params[name]=value;
		}
		return params;
	}
	
	/**
	 * 是否需要提示消息(需要先在JSP中用<emp:msgloader>标签加载所需消息)
	 * @param  code 消息码
	 * @return boolean 是否需要检查该消息
	 */
	EMP.needMessage=function(code){
		var msg =code;
		var level = "INFO";
		var isneed = "true";
		
		if(SMsgConfig && SMsgConfig.length > 0){

			for(var i=0; i<SMsgConfig.length; i++){
			   if(SMsgConfig[i].code == code){
				   
				   isneed = SMsgConfig[i].isneed;
				   break;
			   }
			}
		}
  	 
		return (isneed == 'true');
	};
	
	/**
	 * 提示消息(需要先在JSP中用<emp:msgloader>标签加载所需消息)
	 * @param  code 消息码
	 * @param  params 消息参数JS数组格式(可选)
	 * @param fn 在窗口关闭的时候触发该回调函数
	 */
	EMP.alertMessage=function(code,params,fn){
		var msg =code;
		var level = "INFO";
		var flag = false;
		
		if(SMsgConfig && SMsgConfig.length > 0){

			for(var i=0; i<SMsgConfig.length; i++){
			   if(SMsgConfig[i].code == code){
				   flag = true;
				   msg = SMsgConfig[i].desc;
				   level = SMsgConfig[i].level;
				   if(level == "INFO"){
					   level = "info";
				   }else if(level == "WARN"){
					   level = "warning";
				   }else if(level == "ERROR"){
					   level = "error";
				   }
				   break;
			   }
			}
  	      ///替换参数
  	        if(params && params.length > 0){
	 	      for(var i=0; i<params.length; i++){
	 		    msg = msg.replace('{'+i+'}',params[i]);
	  	      }
  	       }
	    }
		if(flag){
			top.$.messager.alert(null,code+" : "+msg,level,fn);
		}else{
			top.$.messager.alert(null,msg,'info',fn);
		}
	    

	};
	
	/**
	 * 确认消息(需要先在JSP中用<emp:msgloader>标签加载所需消息)
	 * @param  code 消息码
	 * @param  recall 确认/取消之后的回调方法（回调方法参数：flag 表示是否确认）
	 * @param  params 消息参数JS数组格式(可选)
	 */
	EMP.confirmMessage=function(code,recall,params){
		var msg =code;
		var level = "INFO";
		var flag = false;
		
		if(SMsgConfig && SMsgConfig.length > 0){

			for(var i=0; i<SMsgConfig.length; i++){
			   if(SMsgConfig[i].code == code){
				   flag = true;
				   msg = SMsgConfig[i].desc;
				   level = SMsgConfig[i].level;
				   if(level == "INFO"){
					   level = "提示";
				   }else if(level == "WARN"){
					   level = "警告";
				   }
				   break;
			   }
			}
  	      ///替换参数
  	        if(params && params.length > 0){
	 	      for(var i=0; i<params.length; i++){
	 		    msg = msg.replace('{'+i+'}',params[i]);
	  	      }
  	       }
	    }
		if(flag){
			top.$.messager.confirm(null,code+" : "+msg,recall);
		}else{
			top.$.messager.confirm(null,msg,recall);
		}
		
	};
	
	/**
	 * 提示后台异常消息（异步）
	 * @param  expMsg 异常消息
	 */
	EMP.alertException=function(expMsg){
		var errorMsg = expMsg.toString();
		var idx1 = errorMsg.indexOf("[MSG_CDE ");
		var idx2 = errorMsg.indexOf(']', idx1+1);
	   
		var code = "";
		if(idx1 >= 0){
			code = errorMsg.substr(idx1+9, idx2 - idx1 - 9);
			errorMsg = errorMsg.substr(idx2+1);
		}
	   
		if(code != null && code != ''){
			EMP.alertMessage(code);
			//top.$.messager.alert(null, code+" : "+expMsg, 'error');
		} else {
			top.$.messager.alert(null, expMsg, 'error');
		}
	};
	
	/**
	 * 用户可以输入文本的并且带“确定”和“取消”按钮的消息窗体(需要先在JSP中用<emp:msgloader>标签加载所需消息)
	 * @param  code 消息码
	 * @param  recall 确认/取消之后的回调方法（回调方法参数：flag 表示是否确认）
	 * @param  params 消息参数JS数组格式(可选)
	 */
	EMP.promptMessage=function(code, recall, params){
		var msg = code;
		var level = "INFO";
		var flag = false;
		if(SMsgConfig && SMsgConfig.length > 0){
			for(var i=0; i<SMsgConfig.length; i++){
			   if(SMsgConfig[i].code == code){
				   flag = true;
				   msg = SMsgConfig[i].desc;
				   level = SMsgConfig[i].level;
				   if(level == "INFO"){
					   level = "提示";
				   }else if(level == "WARN"){
					   level = "警告";
				   }
				   break;
			   }
			}
			//替换参数
  	        if(params && params.length > 0){
	 	      for(var i=0; i<params.length; i++){
	 		    msg = msg.replace('{'+i+'}', params[i]);
	  	      }
  	       }
	    }
		if(flag){
			top.$.messager.prompt('信息', code+" : "+msg, recall);
		}else{
			top.$.messager.prompt('信息', msg, recall);
		}
	};
	
	/**
	 * 设置锚点，对于内容较多的页面可以设定锚点，通过锚点可以快速回到相应位置。
	 * @param o 配置对象有两个属性id锚点对象的id，text显示文本。eg:[{id:'test',text:'回到这里'}]
	 * 
	 */
	EMP.buildAnchors=function(o){
		//框体
		var div=$("<div/>").appendTo(document.body);
		div.attr("id","sideCatalog");
		//左侧工具栏
		var leftBar=$('<div/>').appendTo(div);
		leftBar.attr('id','sideCatalog-sidebar');
		var barTop=$('<div/>').appendTo(leftBar);
		barTop.addClass('sideCatalog-sidebar-top');
		var barBottom=$('<div/>').appendTo(leftBar);
		barBottom.addClass('sideCatalog-sidebar-bottom');
		//锚点列表
		var anchorsDiv=$('<div/>').appendTo(div);
		anchorsDiv.attr('id','sideCatalog-catalog');
		var anchorsDl=$('<DL/>').appendTo(anchorsDiv);		
		for(var i=0;i<o.length;i++){
			var dd=$("<dd/>").appendTo(anchorsDl);
			dd.addClass('sideCatalog-item1');
			if(i==0)
				dd.addClass('highlight');
			var a=$("<a/>").appendTo(dd);
			a.attr("href","#"+o[i].id);
			a.html(o[i].text);
			var span=$('<span/>').appendTo(dd);
			span.addClass('sideCatalog-dot');
		}
		//监听滚动事件
		$(window).scroll(function(){
			var top=$(window).scrollTop();
			var index=0;
			var min=0;
			for(var i=0;i<o.length;i++){
				var a=document.getElementById(o[i].id);
				if(a){
					var aTop=a.offsetTop;
					var tep=Math.abs(aTop-top)
					if(i==0){
						min=tep;
					}else{
						if(min>tep){
							min=tep;
							index=i;
						}
					}
				}
				
			}
			$('#sideCatalog-catalog dd').removeClass('highlight');
			$('dd').has('a[href="#'+o[index].id+'"]').addClass('highlight');
		})
	}
	
	/**
	 * 获取编辑表格当前行指定字段的编辑器,根据编辑器可以做赋值、必输项等控件
	 * 
	 * @edatagrid 编辑表格JQ对象
	 * @fieldName 指定字段的编辑器
	 */
	EMP.getEDataGridEditor = function(edatagrid, fieldName){
		var row=$(edatagrid).edatagrid('getSelected');//当前选择行
		var editrow=$(edatagrid).edatagrid('getRowIndex',row);//当前选择行的索引号
		var ed = $(edatagrid).datagrid('getEditor', {index:editrow,field:fieldName});//取得编辑行的编辑器
		return ed;
	}
	
	/**
	 * 编辑表格edatagrid取值：取当前选中行的指定字段的值
	 * @edatagrid 编辑表格JQ对象
	 * @fieldName 待取值的字段
	 */
	EMP.getEDataGridValue = function(edatagrid, fieldName){
		var ed = EMP.getEDataGridEditor(edatagrid, fieldName);
		return ed.target.getValue();//修改编辑器的相关内容
	}
	
	/**
	 * 编辑表格edatagrid赋值：给当前选中行的指定字段的赋值
	 * @edatagrid 编辑表格JQ对象
	 * @fieldName 待赋值字段
	 * @value 待赋的值
	 */
	EMP.setEDataGridValue = function(edatagrid, fieldName, value){
		return EMP.getEDataGridEditor(edatagrid, fieldName).target.setValue(value);//修改编辑器的相关内容
	}
	
	/**
	 * <p>页面加载时处理</p>
	 */
	$(document).ready(function(){
		/**
		 * 选择框默认提示信息“---请选择---”
		 */
		$('.combobox-f,.combotree-f,.combogrid-f').each(function(){
			var target=$(this);
			//采用text方式取值，避免手动输入时，输入框失去焦点后未获取到值的情况下赋值成功
			var val=target.combo('getText');
			if(val==null||val.length<=0){
				//$(this).combo('setText','---请选择---')
			}
			target.combo('textbox').focusout(function(){
				var val=target.combo('getValue');
				if(val==null||val.length<=0){
					//target.combo('setText','---请选择---')
				}else{
					/** zwx输入提示，如果select框中输入的值，在字典里不存在，在光标离开输入框时，讲select的value设置为"",text设置为'---请选择---'  start **/
					
					var cName = target.context.className;
					if(cName.indexOf('combobox')>=0){
						//如果是普通下拉框combobox
						var _data = target.combobox('getData');
						var flag = false;
						$.each(_data,function(index,obj){
							$.each(obj,function(key,value){
								if($.trim(val) == $.trim(value)){
									flag = true;
									}
								})
							});
						if(!flag){
							target.combobox('setValue','');
							//target.combobox('setText','---请选择---')
							target.combobox('setText','');
							
							//删除value=''的隐藏input，针对下拉列表框多选时，选择下拉选项后，清除输入框内容，然后输入内容过滤，再重新选择选项时内容最开始出现","的情况
							var combo=$.data(target[0],"combo").combo;
							combo.find("input.combo-value[value='']").remove();
						}
						
					}else if(cName.indexOf('combogrid') >= 0){
						var flag = false;
						//如果是combogrid
						val = target.combogrid('getValues');//采用getValues方法取值，以包含多选的情况
						var opts = target.combogrid('options');	
						var text = target.combogrid('getText');
						//如果value值与text值不同，则默认为合法的值，包括:单选，多选(选项在不同页)的情况。
						if(val.join(opts.separator) != text){
							flag = true;
						}
						
						var gridObj = target.combogrid('grid');	// 获取数据表格对象
						var gridData = gridObj.datagrid('getData').rows;
						var textField =  target.combogrid('options').textField
						var idField = target.combogrid('options').idField;
						var valueArr = [];
						var textArr = [];
						
						$.each(val,function(index,value){
							$.each(gridData,function(index,obj){
								var tempSelectCde="";
								var tempSelectText="";
								
								$.each(obj,function(key,value){
									if($.trim(key) == textField){
										tempSelectText = value;
									
									}else if($.trim(key) == idField){
										tempSelectCde = value;
									}
								});
								//比较value值或者text值
								if($.trim(value) == $.trim(tempSelectCde)||$.trim(value) == $.trim(tempSelectText)){
									valueArr.push(tempSelectCde);
									textArr.push(tempSelectText);
								}
							});
						});
						
						//只选择一项时，如果手动修改了输入框中的内容,则输入框中的内容必须在当前下拉列表页中
						if(valueArr.length > 0&&val.length==1){
							target.combogrid('setValues',valueArr);
							target.combogrid('setText',textArr.join(opts.separator));
							flag = true;
						}
						
						if(!flag){
							target.combogrid('setValue','');
							//target.combobox('setText','---请选择---')
							target.combogrid('setText','');
							//如果value与text分开存储则清除存储value的输入框
							if(opts.valueFieldID){
								$("#"+opts.valueFieldID).setValue("");
								//编辑表格中
								if(opts.parentID){
									EMP.setEDataGridValue(opts.parentID, opts.valueFieldID, "");
								}
							}
						}
					}else{
						//combotree
					}
					
					
					/** zwx输入提示，如果select框中输入的值，在字典里不存在，在光标离开输入框时，讲select的value设置为"",text设置为'---请选择---'  end **/
				}
			}).focusin(function(){
				var val=target.combo('getValue');
				if(val==null||val.length<=0){
					target.combo('setText','')
				}
			});
		});
	});
	/**
	 * <p>容器自适应</p>
	 * <p>panel（面板）、datagrid（表格）、edatagrid（可编辑表格，会导致ie8及低版本崩溃，取消！）、treegrid（树形表格）自适应窗口变化。同时解决"Tab页签，如有坚向滚动条，则表格不能完整显示"的bug</p>
	 * add by wangbin 2014-3-31 16:41:27
	 */
	$(window).resize(function () {
         $('.easyui-datagrid').datagrid('resize');
         $('.easyui-edatagrid').edatagrid('resize');
         $('.easyui-treegrid').treegrid('resize');
         $('.easyui-panel').panel('resize');         
     });
	
	/**
	 * <p>针对滚动条覆盖住panel/table一部分内容的问题</p>
	 * 特别说明：如果此处不做修改，需要修改jquery.easyui.min.js line:2104
	 * 将：1a7.add(_1a8)._outerWidth(_1a6.width());修改为：_1a7.add(_1a8)._outerWidth("auto");
	 * 针对easyUI版本：1.3.2
	 * add by zangys at 2015-05-19 11:07
	 */
	$(function(){
		$('div.panel-header').width('auto');
		$('div.panel-body').width('auto');
	})
	
	/**
	 * getSearchBoxValue 方法，通过data-options设置[参数]=name，根据name的值判断是全局查询还是单个查询，把数据包装起来传递到后台
	 * 参数value  快速查询输入框中的值
	 * 参数name   快速查询data-options中name的值
	 * 返回params 用于返回调用datagrid的load方法时使用
	 */
	jQuery.fn.getSearchBoxValue = function(_value,_name) {
		var _id = jQuery(this).attr('id');
		if (_id == undefined) {
			alert(CusLang.EMP.jqFnExt.getSearchBoxValue_noIdmsg);
			return '';
		}
		if (_value == undefined) {
			_value = "";
		}
		if (_name == undefined) {
			// alert('标签没有name属性，无法执行getSearchBoxValue方法!');
			return '';
		}
		var params={};
		if(_name=='all'){
			$.each($("#"+_id+">div"), function(key, val){
				var options=jQuery(val).attr('data-options');
				if(options){
					var _name=options.split(':')[1].replace(/\'/g,"");
					params[_name]=_value;
				}
			});
		}else{
			params[_name]=_value;
		}
		/**传递前台标识，后台判断处理，执行快速查询**/
		params["searchType"] = "quickquery";
		return params;
	};
	
	
	//将表单中所有字段设置为只读
	jQuery.fn.renderDisabledAll = function(){
		var _id = jQuery(this).attr('id');
		$('#'+_id+' :input').each(function(){
			var id = $(this).attr("id");
			if(id != "undefined" && id != undefined ){
				/**将Disabled改为Readonly ，解决IE浏览器 -大文本框使用该方法时，不显示滚动条的BUG，--xulq 2016 12 27*/
				$('#'+id).renderReadonly(true);
			}
	    });
	}
	
	/**
	 * dataGridEndEdit 方法，关闭datagrid编辑器的方法
	 */
	jQuery.fn.dataGridEndEdit = function(){
		var rows = $(this).datagrid('getRows');  
	       for ( var i = 0; i < rows.length; i++) {  
	       $(this).datagrid('endEdit', i);  
	 	}  
	}
	
	/**
	 * 设置_value用于存放加载时的数据，
	 */
	EMP.SetInputDefValue = function(formId){
		$('#'+formId+' :input').each(function(){
			var id = $(this).attr("id");
			if(id != "undefined" && id != undefined ){
				jQuery(this).attr('_value',jQuery(this).getValue());
			}
	    });
	}
	
	/**
	 * 离开页面时,调用该方法检测表单元素是否被修改,可根据结果给出提示.防止用户错失修改的机会，提高用户体验
	 * 
	 */
	EMP.validateFormChanged = function(formId) { 
		var is_changed = false; 
		jQuery('#'+formId+' :input').each(function() {
			var id = $(this).attr("id");
			if(id != "undefined" && id != undefined ){
				var _v = jQuery(this).attr('_value'); 
				if(_v == undefined || _v == 'undefined') _v = jQuery(this).getValue(); 
				if(typeof(_v) == 'undefined') _v = ''; 
				if(_v != jQuery(this).getValue()){
					is_changed = true; 
					return is_changed;
				} 
			}
		}); 
		jQuery('.easyui-edatagrid').each(function() {
			$(this).dataGridEndEdit();
			var rows = $(this).datagrid('getChanges');
			if(rows.length > 0){
				is_changed = true; 
				return is_changed;
			}
		});
		return is_changed; 
	} 
	
	/**
	 * 创建自定义日历表格
	 * @param divID 存放日历表格DIV对应的ID
	 * @param initDate 初始化日期。格式：YYYY-MM-DD，可为null。
	 * @param clickDayFun 点击日历日触发的事件，带有参数：当前点击的日期
	 * @return 
	 */
	EMP.createCalendar = function(divID,initDate,clickDayFun){
        if($("#"+divID).length==0){
            return;
        }
        $("#"+divID).addClass("calendar");
        var calendarDiv = $("<div class='calendar'></div>");
        var calendarTitleDiv = $("<div class='calendarTitle'></div>");
        var prevMonthA = $("<a href='###' class='prevMonth'></a>");
        var nextMonthA = $("<a href='###' class='nextMonth'></a>");
        var dateSpan = $("<span></span>");
        calendarTitleDiv.append(prevMonthA);
        calendarTitleDiv.append(dateSpan);
        calendarTitleDiv.append(nextMonthA);
        
        var dateTitleDiv = $("<div class='dateTitle'></div>");
        var titleArr = ['S','M','T','W','T','F','S'];
        for(var i = 0;i < titleArr.length; i++){
            var titleA = $("<a href='###'>"+titleArr[i]+"</a>");
            dateTitleDiv.append(titleA);
        }
        
        var calendarCoreDiv = $("<div class='calendarCore'></div>");
        calendarCoreDiv.append("<table cellpadding=0 cellspacing=0></table>");
        
        calendarDiv.append(calendarTitleDiv);
        calendarDiv.append(dateTitleDiv);
        calendarDiv.append(calendarCoreDiv);

        $("#"+divID).append(calendarTitleDiv);
        $("#"+divID).append(dateTitleDiv);
        $("#"+divID).append(calendarCoreDiv);
        
        var calendar ={
            main:calendarDiv,
            calendarCore:calendarCoreDiv,
            calendarTitle:calendarTitleDiv,//日历的头
            prevMonth:prevMonthA,//上个月的按钮
            dateSpan:dateSpan,
            nextMonth:nextMonthA,//下个月的按钮
            prevYear:null,//上个年的按钮
            nextYear:null,//下个年的按钮
            drawCal:function(month, year,today){//创建日历；
                this.calendarCore.find("table").empty();//日历初始
                this.calendarCore.find("td").removeClass("dataTime");
                var _prevMonthDate=null;
                var firstDay=null;
                var _day=null;
                var days=null;
                firstDay = new Date(year,month,1);//每月一号
                _day=firstDay.getDay();//每月一号是星期几；
                days=this.getDays(month,year);//天数
                var monthName=this.getMonthName(month);//获取月份1,2,3,4,5,6,7,8,9,10，11,12
                this.calendarTitle.find("span").text(year+CusLang.EMP.createCalendar.patternYear+this.getMonthName(month));//
                for (var i=0; i<Math.ceil((days+_day)/7); i++) {
                    this.calendarCore.find("table").append("<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>");
                }
                for (var i=0; i<days; i++) {
                    this.calendarCore.find("td").eq(_day+i).text(i+1).addClass("dataTime");
                }
                
                $(calendarCoreDiv).find(".dataTime").eq(today-1).addClass("now");//今天的日期；3月30日；
                
                if(month>11){
                    var _year=year+1;
                    lastMonthDays=this.getDays(0,_year);
                }else{
                    lastMonthDays=this.getDays(0,year);
                }
                if(month>0){
                    var _year=year-1;
                    lastMonthDays=this.getDays(0,_year);
                }else{
                    lastMonthDays=this.getDays(11,year);
                }

                for (var i=0; i<_day; i++) {
                    this.calendarCore.find("td").eq(_day-1-i).text(lastMonthDays-i);
                }

                for (var i=0; i<Math.ceil((days+_day)/7)*7-(_day+days); i++) {
                    this.calendarCore.find("td").eq(_day+days+i).text(i+1);
                }
            },
            tab:function(){
                var _this=this;
                var now=new Date();
                /*
                this.prevYear.bind("click",function(event){
                    event.preventDefault();
                    year--;
                    _this.drawCal(month, year);
                })
                this.nextYear.bind("click",function(event){
                    event.preventDefault();
                    year++;
                    _this.drawCal(month, year);
                })
                */
                this.prevMonth.bind("click",function(event){
                    event.preventDefault();
                    month--;
                    if(month<0){
                        year--;
                        month=11;
                    }
                    if(month>11){
                        year++;
                        month=0;
                    }
                    var maxDay = _this.getDays(month,year);
                    if(today>maxDay){
                        today = maxDay;
                    }
                    _this.drawCal(month, year,today);
                    _this.callBack(year+"-"+(month+1)+"-"+today);
                })

                this.nextMonth.bind("click",function(event){
                    event.preventDefault();
                    month++;
                    if(month<0){
                        year--;
                        month=0;
                    }
                    if(month>11){
                        year++;
                        month=0;
                    }
                    var maxDay = _this.getDays(month,year);
                    if(today>maxDay){
                        today = maxDay;
                    }
                    _this.drawCal(month, year,today);
                    _this.callBack(year+"-"+(month+1)+"-"+today);
                })
                
            },
            leapYear:function(year){
                if (year%4 == 0){
                        return true 
                    }else{
                        return false 
                    }
            },
            getDays:function(month, year){
                var ar = new Array(12);
                ar[0] = 31 // January
                ar[1] = (this.leapYear(year)) ? 29 : 28 // February
                ar[2] = 31 // March
                ar[3] = 30 // April
                ar[4] = 31 // May
                ar[5] = 30 // June
                ar[6] = 31 // July
                ar[7] = 31 // August
                ar[8] = 30 // September
                ar[9] = 31 // October
                ar[10] = 30 // November
                ar[11] = 31 // December
                return ar[month]
            },
            getMonthName:function(month) {// 为月份名称设定数组
                var ar = CusLang.EMP.createCalendar.monthNames;
                return ar[month];
            },
            callBack:function(curDate){
                if(clickDayFun){
                    try{
                        clickDayFun(curDate);
                    }catch(e){}
                }
            }
        };
        
        var date = initDate;
        var b = null;
        if(initDate){
            b=initDate.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
            if(b==null){
            }
        }
        if(initDate&&b){
            year=initDate.split("-")[0];
            month=parseInt(initDate.split("-")[1],10)-1;
            today=parseInt(initDate.split("-")[2],10);
            calendar.drawCal(month,year,today);
        }else{
            date= new Date();
            year=date.getFullYear();
            month=date.getMonth();
            today=date.getDate();
            calendar.drawCal(month,year,today);
            initDate = year+"-"+(month+1)+"-"+today;
        }
        calendar.tab();//日历年月切换
        //初始化日期，默认点击了日历
        calendar.callBack(initDate);
        
        calendar.calendarCore.delegate("td.dataTime","click",function(){
            $(this).addClass("now");
            $(this).parent().siblings().find(".now").removeClass("now");
            $(this).siblings().removeClass("now");
            var curDate;
            var curYearMonth = calendar.dateSpan.text();
            curDate = curYearMonth;
            curDate += $(this).text();
            curDate = curDate.replace(CusLang.EMP.createCalendar.patternYear,'-').replace(CusLang.EMP.createCalendar.patternMonth,'-');
            calendar.callBack(curDate);
        });
    }
	
	/**
	 *  提供统一的方式为标签附加onchange事件，该方法不会覆盖掉标签已配置的onchange事件。
	 * @param fun 新绑定的onchange事件,参数：newValue，oldVlue (如果组件为：validatebox,checkbox则参数为undefined)
	 * 说明：暂不支持richtext，TimeSpinner
	 * 
	 */
	jQuery.fn.bindOnChange = function(fun){
		var $target = jQuery(this);
		var pluginName = "";
		var className = jQuery(this).attr('class');
		for(var i = 0;i<jQuery.parser.plugins.length;i++){
			if(jQuery(this).hasClass('easyui-'+jQuery.parser.plugins[i])){
				pluginName = jQuery.parser.plugins[i];
				break;
			} 
		}
		if(pluginName == ""){
			return;
		}
		var oldonChange;
		var _cssClass = jQuery(this).attr('class');
		var _id = jQuery(this).attr('id');
		//文本框
		if(pluginName == "validatebox"){
			
			$target.bind('change',function(){
				fun.call(this);
			})

		}
		//单选/多选框
		else if(pluginName == "checkbox"){
			var optsC = jQuery(this).checkbox('options');
			var targetC = $(this);
			var span=$.data(targetC[0],"checkbox").field;
			span.find('input[type='+optsC.dataType+']').bind('change',function(){
				fun.call(targetC[0]);
			});
		}else{
			var getOptionsStr = "$target."+pluginName+"('options')";
			var opts = eval(getOptionsStr);
			if(opts.onChange){
				oldonChange = opts.onChange;
			}
			function newonChange(newValue, oldValue){
				try {
					oldonChange.call(this,newValue,oldValue);
				}catch(e){}
				try {
					fun.call(this,newValue,oldValue);
				}catch(e){}					
			}
			
			var type = '';
			var _cssClass = jQuery(this).attr('class');
			if(_cssClass == undefined) {
				_cssClass = "";
			}
			
			//数值类型
			if(_cssClass.indexOf('number')!=-1){
				type = "numberbox";
			}
			//combo类型
			else if(_cssClass.indexOf('combo')!=-1){
				type = "combo";
			}
			//富文本框类型
			else if(_cssClass.indexOf('richtext') >= 0){
				type = "richtext";
				
				return;
			}
			//popbox类型
			else if(_cssClass.indexOf('popbox') >= 0){
				type = "popbox";
			}
			//文本类型
			else{
				type = "validatebox";
				$target.bind('change',function(){
					fun.call(this);
				})
				return;
			}
			
			var bOpts = $.data($target[0],type).options;
			bOpts.onChange = newonChange;
		}
	}
	
	/**
	 * 隐藏/显示 微调按钮
	 * @param jq number jq对象
	 * @param flag true:显示 false：隐藏 
	 */
	EMP.renderSpinnerHide = function(jq,flag){
		var spinner = $.data(jq[0],"spinner").spinner;
		var arrow = spinner.find('.spinner-arrow');
		if(flag){
			arrow.hide();
			jq.width(spinner.width())
		}else{
			arrow.show();
			jq.width(spinner.width()-arrow.outerWidth()-4);
		}
	}
})(jQuery);
