/**
 * <p>该文件用于存放easyui扩展js</p>
 * 扩展：
 * 		1.$.fn.form.methods：
 * 				增加iload方法，提供符合modeid.column,同时支撑radio,checkbox,combo类的数据加载；
 * 		2.$.fn.validatebox.defaults.rules：支持各类dataType格式检查及radio、checkbox必输项检查
 * 		3.$.fn.datagrid.defaults.editors：支持这表格中编辑combogrid、numberboxspinner、dateymbox、popbox、checkbox
 * 		4.$.parser:扩展plugins以满足对新增$fn.dateymbox的解析
 * 		5.$.fn.datagrid.defaults.onLoadError设置datagrid加载数据失败默认处理函数
 *      6.$.fn.propertygrid.methods：getKcollData方法以满足kcoll形式的数据传递；urlLoad通过url进行数据加载，兼容EMP原有的kcoll取数据。
 *      7.$.fn.panel.defaults:onBeforeDestroy， panel关闭时回收内存，主要用于布局组件使用iframe嵌入网页时的内存泄漏问题
 *      8.$.fn.datagrid.methods:扩展方法mergeCellsMore，用于执行多个合并操作。参数merges为mergeCells方法参数的集合
 *      	增加findText方法根据field、rowIndex（行标识）获取表格指定单元格的文本内容 add by wangbin 2014-5-14 17:31:15
 *      9.$.fn.combobox.defaults:onLoadSuccess初始化combobox添加空值选项“---请选择---”
 *      10.$.fn.combogrid.defaults.keyHandler:扩展query方法，通过queryParams设置[参数]=$q将输入框的值给特定的查询参数，以支撑更灵活便捷的查询eg：queryParams={text:$q}
 *      11.$.fn.dialog.defaults、$.fn.window.defaults:设置onMove防止panel/window/dialog组件超出浏览器边界
 *      12.$.fn.combotree.defaults、$.fn.tree.defaults、$.fn.treegrid.defaults:设置loadFliter以支持扁平数据，即后台无需关注其对树形结构的要求。
 *      13.重写$.fn.combo.methods的setText方法。 根据text值是否为空决定是否进行验证。用于解决页面在初始化后就显示输入错误的图标"!" add by zangys at 2014-6-25 16:20:55
 *      14.编辑器增加combogridext类型!" add by zangys at 2014-7-14 16:20:55
 *      15.重写validatebox组件的validate/isValid方法，校验必输时，去掉校验字符串起始和结尾的空格. add by zangys at 2014-7-15 16:23:20
 *      16.修改$.fn.datagrid.defaults.editors的combogrid、combogridext、combobox类型的setValue方法，在setValue值时不调用onchange方法,避免点击行的时候就触发onchange事件。
 *      	注意：为保证在手动赋值的时候调用onchange事件，需采用EMP.setEDataGridValue中的方式对编辑表格进行赋值(即：编辑器.target.setValue(value)的方式)。add by zangys at 2014-7-21 16:18:34
 *      17.$.fn.datagrid.defaults.editors增加类型：numberspinnerext；add by zangys at 2014-7-23 14:22:21
 *      18.修改$.fn.datagrid.defaults.editors的numberspinnerext、datebox、combobox、dateymbox的setvalue方法，在setValue值时不调用onchange方法,避免避免点击行的时候就触发onchange事件。add by zangys at 2014-7-23 17:28:34
 *      19.修改$.fn.datagrid.defaults.editors的numberspinnerext在init时增加dType属性。add by zangys at 2014-7-29 12:48:35
 *      20.$.fn.datagrid.defaults.editors增加linkage类型。add by zangys at 2014-8-11 16:04:21
 *      21.$.fn.datagrid.methods : autoMergeCells方法，根据列内容自动合并列内容相同的单元格;deleteRow4AutoMergeCells:自动合并单元格列表删除行。add by zangys at 2014-10-15 10:03:06
 *      22.$.fn.numberbox.methods增加getText方法，用于取格式化后的显示值。add by zangys at 2014-10-22 17:07:54
 *      23.扩展$.fn.datagrid.methods，增加changeEditor方法：支持动态更换当前编辑行某列的编辑器。add by zangys at 2015-04-03 13:21:23
 *      24.扩展$.fn.validatebox.defaults.rules，增加对年月数据类型的校验。add by zangys at 2015-04-23
 *      25.增加DataGridOnLoadSuccessExt(data)方法，作为datagrid的onLoadSuccess事件的扩展。add by zangys at 2015-06-08 17:49
 *      26.扩展window、dialog的onBeforeDestroy事件，进行内存回收(问题来源：pop框弹出窗口如果为树，多次点击树节点关闭弹出窗口后，则页面输入框无法获得焦点)。add by zangys at 2015-06-09 14:54
 *      27.扩展button的默认click方法,仿重复提交，将按钮禁用并倒计时,倒计时结束文本恢复，禁用取消
 *      28.tab重写的onSelect方法执行完成后执行onAfterSelect方法，解决用户调用onSelect方法时覆盖此扩展修改的问题 add by liwei at 2015-10-26 09:56
 *      29.numberspinner设置默认的onChange方法，用来扩展emp标签的onchange事件，其中emp标签支持beforeOnchange onchange afterOnchange三个属性  add by liwei at 2015-12-24 10:00
 *      30.combobox设置默认的onChange方法，用来扩展emp标签的onchange事件，其中emp标签支持beforeOnchange onchange afterOnchange三个属性 add by liwei at 2015-12-24 10:00
 *      31.datebox设置默认的onChange方法，用来扩展emp标签的onchange事件，其中emp标签支持beforeOnchange onchange afterOnchange三个属性 add by liwei at 2015-12-26 10:00
 *      32.datetimebox设置默认的onChange方法，用来扩展emp标签的onchange事件，其中emp标签支持beforeOnchange onchange afterOnchange三个属性 add by liwei at 2016-02-18 17:00
 *      33.修改表格、可编辑表格、树形表格的scrollbarSize默认值为23, 修复部分分辨率下的表格默认出现坚向滚动条，数据显示不完整 add by liwei at 2016-4-12 13:40:56
 *      34.修改tabs标签的扩展方法onAfterSelect执行时，上下文使用当前对象
 *      35.修改$.fn.datagrid.defaults.editors的validatebox(可编辑表格的text标签)添加对onchange,beforeOnchenge,afterOnchange事件（触发的都是onchange事件）支持 add by liucheng3 at 2016-4-25 13:40:56
 *      36.消费合并:修改PostCode的校验正则添加 (?!\\d)
 *      37.消费合并:修改AccountNo的校验允许"-"
 *      38.消费合并:添加Chinese只允许中文的校验
 *      39.消费合并:combobox点击时添加target.setValue('');
 *      40.消费合并:scrollbarSize改为25
 *      41.修改可编辑表格的validatebox，如果属性useOnchangeExt==true时才使用扩展的onchenge，否则使用原生方式以此来兼容旧代码
 *      42.数据校验组件$.fn.validatebox.defaults.rules 添加年份、电话号码【手机和固话统称电话号码】校验 add by wnagyf10 2016/6/21
 *      43.可编辑表格添加my97date的支持 add by liwe at 2016-07-18
 *      44.numberbox设置默认的onChange方法，用来扩展emp标签的onchange事件，其中emp标签支持beforeOnchange onchange afterOnchange三个属性  add by liucheng3 at 2016-11-18 10:00
 *      45.可编辑表格添加numberbox的支持 add by liucheng3 at 2016-12-02
 * bug修复：
 * 		1.iload方法使用ajax请求时未指定请求返回类型，firefox下默认返回对象为dom对象而非string类型。通过设置返回类型“json”避免该bug add by wangbin 2014-4-3 16:49:49
 * 		2.为checkbox组件在datagrid编辑器中添加name（field+'-'+rowIndex）避免多行多列使用该组件时出现name无法判断的bug add by wangbin 2014-5-20 10:22:09
 * 		3.属性表格最后一行值数据加载不上，当数据源为KCOL格式时数据解析错误。add by zangys at 2016-6-21 10:08:21
 * 		4.修改$.fn.validatebox.defaults.rules.Identity末尾为'X'校验失效问题及身份证号月、天为08\09时通过parseInt()转换等于0引起的校验失效的问题。 add by zangys at 2014-9-2 14:53:52
 * 		5.修改pop框在编辑列表中通过renderReadOnly(true)失效的问题。add by zangys at 2014-9-16 11:43:24
 * 		6.修改$.fn.datagrid.defaults.editors的numberspinnerext类型getValue方法，返回值去掉末尾的0。add by zangys at 2014-9-19 16:13:45
 * 		7.修改$.fn.datagrid.defaults.editors的numberspinnerext类型getValue方法返回字符串。add by zangys at 2014-10-14 16:02:26
 *      8.修改combobox的‘---请选择---’选项样式名为：combobox-item-psel,控制该项不作为选择项。add by zangys at 2014-11-4 11:18:25
 *      9.select框，‘--请选择--’项绑定click事件，清除select框的值。add by zangys at 2014-11-5 14:35:41
 *      10.修改tabs的tab页为iframe时，onLoad事件不触发的问题，同时修改tab为非iframe时，设置catche为false时重复加载的问题。add by zangys at 2015-05-04 11:41
 *      11.修改编辑表格编辑器类型为select且多选时，取不到多选值且翻译失效的问题。add by zangys at 2015-05-05 14:19
 *      12.编辑表格编辑器combogridext类型，setValue值时将关联的value值设置的编辑器中。add by zangys at 2015-05-29 10:38
 *      13.编辑表格编辑器checkbox类型,class属性增加checkbox，针对通过编辑器ed.target.getValue()取值为空的问题。add by zangys at 2015-06-08 10:05
 *      14.修改tab页快速切换时出现datagrid显示不全的问题，相应的重写了panel的close方法。add by zangys at 2015-06-17 15:00
 *      15.修改编辑表格动态更换编辑器后，格式化失效的问题。add by zangys at 2015-07-07 16:54
 *      16.由于14修改导致tab页面不显示panel无法关闭等问题，注释14的修改。add by liwei at 2015-12-21 14:12
 *      17.DataGridOnLoadSuccessExt方法修复在不使用分页时如果total返回的值为0导致表格resize时错位的问题(&& data.rows.length==0) add by liwei at 2016-08-26
 */
//----------------------parser
/**
 * <p>扩展$.parser的plugins以支持自定义dateymbox,popbox、edatagrid的解析</p>
 * <p>注：由于easyui是顺序解析，为了方便datagrid调用其他表单类组件，扩展组件放置在datagrid之前</p>
 */
$.extend($.parser,{
	plugins:["draggable","droppable","resizable","pagination","linkbutton","menu","menubutton","splitbutton","progressbar","tree","combobox","combotree","combogrid","numberbox","validatebox","searchbox","numberspinner","timespinner","calendar","datebox","datetimebox","slider","layout","panel","checkbox","dateymbox","popbox","richtext","linkage","datagrid","propertygrid","treegrid","tabs","accordion","window","dialog","edatagrid","steps"]
});
//----------------------validatebox
/**
 * <p>
 * 数据校验组件扩展
 * </p>
 */
$.extend($.fn.validatebox.defaults.rules, {
	Phone : {// 固定电话校验
		validator : function(value, param) {
			var reg = new RegExp(
					"^(0?[0-9]{2,3}-?){0,1}[0-9]{7,8}(-[0-9]{1,4}){0,1}$");
			var checkres = reg.test(value);

			return checkres;
		},
		message : '请输入正确的固定电话号码（例如 010-1234567）'
	},
	Mobile : {// 移动电话校验
		validator : function(value, param) {

			var reg = new RegExp("^[0-9]\\d{10}$");
			var checkres = reg.test(value.replace(/[ ]/g, ""));

			return checkres;
		},
		message : '请输入正确的移动电话号码（例如 13012345678）'
	},
	Date : {// 日期校验
		validator : function(value, param) {

			var checkres = false;
			var ss = (value.split('-'));
			var y = parseInt(ss[0], 10);
			var m = parseInt(ss[1], 10);
			var d = parseInt(ss[2], 10);

			if (isNaN(y) || isNaN(m) || isNaN(d)) {

				checkres = false;// 如果三者中有一个不为数据类型
		} else {
			if (y < 1900 || m > 12 || m < 1 || d > 31 || d < 1) {// 数值在合理范围内
	
				checkres = false;
			} else {
				if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10
						|| m == 12) {
					if (d <= 31) {
						checkres = true;
					} else {
						checkres = false;
					}
				} else if (m == 2) {
					if (y % 4 == 0) {// 润年
						if (d <= 29) {
							checkres = true;
						} else {
							checkres = false;
						}
					} else {
						if (d <= 28) {
							checkres = true;
						} else {
							checkres = false;
						}
					}
				} else {
					if (d <= 30) {
						checkres = true;
					} else {
						checkres = false;
					}
				}
			}
		}
		return checkres;
	},
	message : '请输入正确的日期（例如 2001-01-15）'
	},
	Postcode : {// 邮政编码
		validator : function(value, param) {
		    var reg = new RegExp("^[0-9]\\d{5}(?!\\d)$");
			var checkres = reg.test(value.replace(/[ ]/g, ""));
			
			return checkres;
		},
		message : '请输入正确的邮政编码 （例如 100012）'
	},
	CodeNo : {// 编码
		validator : function(value, param) {
			var reg = new RegExp("^(\\w*_*!*-*)+$");
			var checkres = reg.test(value);

			return checkres;
		},
		message : '请输入正确的编码（例如 abc123d）'
	},
	AccountNo : {// 帐号
		validator : function(value, param) {
			var reg = new RegExp("^[0-9-]*$");
			var checkres = reg.test(value);

			return checkres;
		},
		message : '请输入正确的帐号'
	},
	Number : {// 号码
		validator : function(value, param) {
			var reg = new RegExp("^[0-9]*$");
			var checkres = reg.test(value);

			return checkres;
		},
		message : '只能输入数字'
	},
    Chinese : {// 只允许输入中文
        validator : function(value, param) {
            var reg = new RegExp("^[\u4e00-\u9fa5]+$");
            var checkres = reg.test(value);

            return checkres;
        },
        message : '只能输入中文'
    },
	MaxLength : {// 最大长度 (注：这里中文算两个字节)
		validator : function(value, param) {

			var cArr = value.match(/[^\x00-\xff]/ig);
			var cLen = value.length + (cArr == null ? 0 : cArr.length);
			return (cLen <= param[0]);

		},
		message : '请输入不超过{0}位长度的字符（汉字按2位长度计算）'
	},
	MinLength : {// 最大小长度 (注：这里中文算两个字节)
		validator : function(value, param) {

			var cArr = value.match(/[^\x00-\xff]/ig);
			var cLen = value.length + (cArr == null ? 0 : cArr.length);
			return (cLen >= param[0]);

		},
		message : '请输入至少{0}位长度的字符（汉字按2位长度计算）'
	},
	Length : {// 长度范围 (注：这里中文算两个字节)
		validator : function(value, param) {

			var cArr = value.match(/[^\x00-\xff]/ig);
			var cLen = value.length + (cArr == null ? 0 : cArr.length);
			return (cLen >= param[0] && cLen <= param[1]);

		},
		message : '请输入不小于{0}位且不超过{1}位长度的字符（汉字按2位长度计算）'
	},
	Identity : {// 身份证校验
		validator : function(value, param) {
			if (value == null || value == "") {
				return true;// 如果未输入任何数据，则不作格式检查和转换。
		}
	
		var length = value.length;
		if (length != 15 && length != 18) {
			return false;
		}
	
		var year = null;
		var month = null;
		var day = null;
		/*if (length == 15) {// 15位的身份证号
			var reg = /^([0-9]{15})$/;
			var checkres = reg.test(value);
			if (!checkres) {
				return false;
			}
			year = "19" + value.substring(6, 8);
			month = value.substring(8, 10);
			day = value.substring(10, 12);
		} else*/ if (length == 18) {//18位身份证号
			var reg = /^([0-9]{17})([0-9|x|X])$/;
			var checkres = reg.test(value);
			if (!checkres) {
				return false;
			}
			year = value.substring(6, 10);
			month = value.substring(10, 12);
			day = value.substring(12, 14);
		}
		var dateObj = new Date(year, month - 1, day);
		if (isNaN(dateObj)) {
			return false;
		}
		//修改parserInt('08')=0,parserInt('09')=0引起的校验失败问题
		if ((parseInt(day,10) != parseInt(dateObj.getDate(),10))
				|| (parseInt(month,10) != parseInt((dateObj.getMonth() + 1),10))) {
			return false;
		}
		if (length == 18) {
			var wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
			var verifyCodeList = [ "1", "0", "x", "9", "8", "7", "6", "5", "4",
					"3", "2" ];
			var totalCode = 0;
			for ( var k = 0; k < 17; k++) {
				totalCode += value.substring(k, k + 1) * wi[k];
			}
			var code = verifyCodeList[totalCode % 11];
			
			//针对code为'x'，value最后一位为'X'的情况
			if(code == "x" && value.substring(17, 18)=="X"){
				return true;
			}
			if (code != value.substring(17, 18)) {
				return false;
			}
		}
		return true;
	},
	message : '请输入正确的身份证号码'
	},
	TextArea : {// 大文本框校验
		validator : function(value, param) {

			var checkres = !jQuery.checkSpecialChar(value);

			return checkres;
		},
		message : '文本框内不能有特殊字符（例如等号、半角双引号、尖括号等）'
	},
	radio : {
		validator : function(value, param) {
			var groupname = param[0];
			var ok = false;
			$('input[name="' + groupname + '"]').each(function() { // 查找表单中所有此名称的radio如果有被选中则
						if (this.checked) {
							ok = true;
							return false;
						}
					});
			return ok;
		},
		message : '请选择一项！'
	},
	checkbox : {
		validator : function(value, param) {
			var groupname = param[0], checkNum = 0;
			$('input[name="' + groupname + '"]').each(function() { // 查找表单中所有此名称的checkbox
						if (this.checked)
							checkNum++;
					});
			return checkNum > 0;
		},
		message : '请选择一项'
	},
	DateYM : {// 年月校验
		validator : function(value, param) {
		
			var checkres = false;
			var ss = (value.split('-'));
			var y = parseInt(ss[0], 10);
			var m = parseInt(ss[1], 10);

			if (isNaN(y) || isNaN(m)) {
				checkres = false;// 如果两者中有一个不为数据类型
			} else {
				if (y < 1900 || m > 12 || m < 1) {// 数值在合理范围内
					checkres = false;
				}else{
					checkres = true;
				}
			}
			
			return checkres;
		},
		message : '请输入正确的年月（例如 2001-01）'
	},
	PhoneOrMobile : {// 移动电话校验或者固定电话校验
		validator : function(value, param) {
			var reg = new RegExp("^[0-9]\\d{10}$");
			var checkres = reg.test(value.replace(/[ ]/g, ""));
			var reg2 = new RegExp("^(0?[0-9]{2,3}-?){0,1}[0-9]{7,8}(-[0-9]{1,4}){0,1}$");
			var checkres2 = reg2.test(value);
			return checkres||checkres2;
		},
		message : '请输入正确的号码格式 如:13012345678;010-1234567'
	},
	Year : {// 年份校验
		validator : function(value, param) {
			var reg = new RegExp("^[1-2][0-9][0-9][0-9]$");
			var checkres = reg.test(value.replace(/[ ]/g, ""));			
			return checkres;
		},
		message : '请输入正确的年份 如:2016'
	}

});
//----------------------combobox
/**
 * <p>combobox添加空值选项‘---请选择---’</p>
 * <p>修改‘---请选择---’选项样式名为：combobox-item-psel,控制该项不作为选择项</p>
 * 注：如果修改此方法，需相应修改linkage标签的initItemStyle方法。
 */
$.extend($.fn.combobox.defaults,{
	onLoadSuccess:function(data){
		var target=$(this);
		var panel=target.combo("panel");	
		var item=$("<div class=\"combobox-item-psel\"></div>").prependTo(panel);
		item.bind('click',function(){
			//清除select框的值
		    target.setValue('');
			target.combobox('clear');
			target.combobox('hidePanel');
		});
		item.attr("value","");
		item.html(CusLang.EUIExt.extCombobox.blankLabel);
		item.hover(
			function(){
				item.addClass("combobox-item-hover");
			},
			function(){
				item.removeClass("combobox-item-hover");
			}
		);
	}
});

/**
 * 设置默认的onchange事件用来支持emp属性扩展扩展
 * 分别调用beforeOnchange  empOnchange afterOnchange
 */
$.extend($.fn.combobox.defaults,{
    onChange:function(newValue, oldValue){
        var opts = $(this).combobox("options");
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(this, newValue, oldValue);
        }
        if(opts.empOnchange){
            opts.empOnchange.call(this, newValue, oldValue);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(this, newValue, oldValue);
        }
    }
});
//----------------------datagrid
/**
 * <p>设置datagrid加载数据失败默认处理函数</p>
 */
$.extend($.fn.datagrid.defaults,{
	onLoadError:function(){
		var opts=$(this).datagrid("options");
		$.messager.show({title: 'error',
            msg: "【"+opts.title+"】"+CusLang.EUIExt.extDatagrid.loadErrorMsgSuf});
	},
	scrollbarSize : 25
});

/**
 * <p>可编辑表格edatagrid本地化</p>
 */
$.extend($.fn.edatagrid.defaults,{
	destroyMsg:{
	    norecord:{
	        title:CusLang.EUIExt.extEdatagrid.norecordTitle,
	        msg:CusLang.EUIExt.extEdatagrid.norecordMsg
	    },
	    confirm:{
	        title:CusLang.EUIExt.extEdatagrid.confirmTitle,
	        msg:CusLang.EUIExt.extEdatagrid.confirmMsg
	    }
	},
    scrollbarSize : 25
});

/**
 * <p>树形表格treegrid本地化</p>
 */
$.extend($.fn.treegrid.defaults,{
    scrollbarSize : 25
});

/**
 * <p>扩展datagrid的editors方法支持combogrid、combogridext、numberspinner、numberspinnerext、dateymbox、popbox编辑</p>
 * <p>注意：
 * 		1.编辑器中setValue方法中没有调用onchange事件方法，避免在点击编辑表格行时就调用onchange方法。</p>
 * 		2.为保证在手动赋值的时候调用onchange事件，需采用EMP.setEDataGridValue中的方式对编辑表格进行赋值(即：编辑器.target.setValue(value)的方式)。
 * </p>
 */ 
$.extend($.fn.datagrid.defaults.editors,{
	validatebox:{
		init:function(container,options){//文本框
		    //如果使用onchange扩展则与form表单保持一致
		    if(options.useOnchangeExt){
	            function getFName(fn){
	                return (/^[\s\(]*function(?:\s+([\w$_][\w\d$_]*))?\(/).exec(fn.toString())[1] || '';
	            }
	            var newOpts = "";   
	            if(options.beforeOnchange){
	                newOpts+="beforeOnchange:"+getFName(options.beforeOnchange)+",";
	            };
	            if(options.onchange){
	                newOpts+="onchange:"+getFName(options.onchange)+",";
	            };
	            if(options.afterOnchange){
	                newOpts+="afterOnchange:"+getFName(options.afterOnchange)+",";
	            };
	            //删除最后一个逗号
	            if(newOpts != null){
	                var lastIndex = newOpts.lastIndexOf(',');
	                if (lastIndex > -1) {
	                    newOpts = newOpts.substring(0, lastIndex);
	                } 
	            }
	            var input=$("<input type=\"text\" onmouseover=\""+options.onmouseover+"\"  onchange=\"$.textOnchangeExt(this)\" onclick=\""+options.onclick+"\" onfocus=\""+options.onfocus+"\" onblur=\""+options.onblur+"\" class=\"datagrid-editable-input\" data-options=\""+newOpts+"\">").appendTo(container);
	            input.validatebox(options);
	            return input;
		    }else{
		        //如果不使用onchange扩展与form表单保持一致
	            var input=$("<input type=\"text\" onmouseover=\""+options.onmouseover+"\"  onchange=\""+options.onchange+"\" onclick=\""+options.onclick+"\" onfocus=\""+options.onfocus+"\" onblur=\""+options.onblur+"\" class=\"datagrid-editable-input\">").appendTo(container);
	            input.validatebox(options);
	            return input;
		    }
			
		},
		destroy:function(target){
			$(target).validatebox("destroy");
		},
		getValue:function(target){
			return $(target).val();
		},
		setValue:function(target,value){
			$(target).val(value);
		},
		resize:function(target,width){
			$(target)._outerWidth(width);
		}
	},
	combogrid : {//下拉数据表格列表框
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
			input.combogrid(options);
			return input;
		},
		destroy : function(target) {
			$(target).combogrid('destroy');
		},
		getValue : function(target) {
			return $(target).combogrid('getValue');
		},
		setValue : function(target, value) {//此方法中不调用onchange事件方法
			return target.each(function(){
				var _7f3=target[0];
				var _7f4=[value];
				var opts=$.data(_7f3,"combogrid").options;
				var grid=$.data(_7f3,"combogrid").grid;
				var rows=grid.datagrid("getRows");
				var ss=[];
				for(var i=0;i<_7f4.length;i++){
				var _7f6=grid.datagrid("getRowIndex",_7f4[i]);
				if(_7f6>=0){
				grid.datagrid("selectRow",_7f6);
				ss.push(rows[_7f6][opts.textField]);
				}else{
				ss.push(_7f4[i]);
				}
				}
				if($(_7f3).combo("getValues").join(",")==_7f4.join(",")){
				return;
				}
				comboSetValueNoOnchange($(_7f3),_7f4);
				$(_7f3).combo("setText",ss.join(opts.separator));
			});
		},
		resize : function(target, width) {
			$(target).combogrid('resize', width);
		}
	},
	combogridext : {//扩展下拉数据表格列表框
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input combogridext">').appendTo(container);
			input.combogridext(options);
			return input;
		},
		destroy : function(target) {
			$(target).combogridext('destroy');
		},
		getValue : function(target) {
			return $(target).combogridext('getValue');
		},
		setValue : function(target, value) {//此方法中不调用onchange事件方法
		   var opts = $(target).combogridext('options');
		   if(opts.parentID && opts.valueFieldID){
			   setTimeout(function(){
				   var ed = EMP.getEDataGridEditor($(opts.parentID)[0],opts.valueFieldID);
				   var edvalue = ed.target.getValue();
				   if(edvalue)
					   $.fn.datagrid.defaults.editors.combogrid.setValue(target,edvalue);
				   $(target).combogridext('setText',value);
			   
			   },0);
		   }else{
			   $.fn.datagrid.defaults.editors.combogrid.setValue(target,edvalue);
		   }		  
		  
		},
		resize : function(target, width) {
			$(target).combogridext('resize', width);
		}
	},
	numberspinner : {//数值输入框
		init : function(container, options) {
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
			input.numberspinner(options);
			return input;
		},
		destroy : function(target) {
			$(target).numberspinner('destroy');
		},
		getValue : function(target) {
			return $(target).numberspinner('getValue');
		},
		setValue : function(target, value) {//此方法中不调用onchange事件方法
			$.fn.datagrid.defaults.editors.numberspinnerext.setValue(target,value);
		},
		resize : function(target, width) {
			$(target).numberspinner('resize', width);
		}
	},
	numberspinnerext : {//数值输入框扩展
		/**
		 * 初始化时增加dType属性，根据该值在数值输入框得到/失去焦点时对显示值进行处理
		 */
		init : function(container, options) {
			var input = $('<input type="text" dType="'+options.dType+'" class="datagrid-editable-input">').appendTo(container);
			input.numberspinnerext(options);
			return input;
		},
		destroy : function(target) {
			$(target).numberspinner('destroy');
		},
		/**
		 * 去掉返回值末尾的零，解决点击此编辑器后，列表视为该值改变的问题
		 * (此问题原因：从数据库取数值，会把小数后面末尾的0去掉造成)
		 */
		getValue : function(target) {
			var val = $(target).numberspinner('getValue');
			//isNaN(val) val为空时返回false!!!
			if(!isNaN(val) && $.trim(val) != ""){
				//返回string类型，（避免""==0返回true）
				return ""+parseFloat(val);
			}
			return val;
		},
		setValue : function(target, value) {//此方法中不调用onchange事件方法
			return target.each(function(){
				var _3e9=this;
				var _3ea=value;
				var _3eb=$.data(_3e9,"numberbox");
				var opts=_3eb.options;
				var _3ec=$.data(_3e9,"numberbox").field.val();
				_3ea=opts.parser.call(_3e9,_3ea);
				opts.value=_3ea;
				_3eb.field.val(_3ea);
				$(_3e9).val(opts.formatter.call(_3e9,_3ea));
			});
		},
		resize : function(target, width) {
			$(target).numberspinner('resize', width);
		}
	},
	dateymbox : {//年月输入框
		init : function(container, options) {
		var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
		input.dateymbox(options);
		return input;
		},
		destroy : function(target) {
			$(target).dateymbox('destroy');
		},
		getValue : function(target) {
			return $(target).combo('getValue');
		},
		setValue : function(target, value) {//此方法中不调用onchange事件方法
			comboSetValueNoOnchange($(target),[value]);
			$(target).combo('setText',value);
		},
		resize : function(target, width) {
			$(target).combo('resize', width);
		}
	},

    my97: {
        init: function (container, options) {
            var box = $("<input type=\"text\"></input>").appendTo(container).my97(options);
            box.my97("textbox").addClass("datagrid-editable-input");
            return box;
        },
        destroy: function (target) {
            $(target).my97("destroy");
        },
        getValue: function (target) {
            var t = $(target), opts = t.my97("options");
            return t.my97(opts.multiple ? "getValues" : "getValue");
        },
        setValue: function (target, value) {
            comboSetValueNoOnchange($(target),[value]);
            $(target).combo('setText',value);
        },
        resize: function (target, width) {
            $(target).my97("resize", width);
        },
        setFocus: function (target) {
            $(target).my97("textbox").focus();
        }
    },
	popbox : {
		init : function(container, options) {
			var input = $('<input type="text" readonly class="datagrid-editable-input popbox">').appendTo(container);
			input.popbox(options);
			return input;
		},
		destroy : function(target) {
			$(target).popbox('destroy');
		},
		getValue : function(target) {
			return $(target).val();
		},
		setValue : function(target, value) {
			$(target).val(value);
		},
		resize : function(target, width) {
			$(target)._outerWidth(width-20);
		}
	},
	checkbox : {//checkbox、radio输入组件 增加样式mis-radio以便支持表格编辑时调用renderReadonly等方法
		init : function(container, options) {
			var input = $('<input type="hidden" class="datagrid-editable-input mis-radio checkbox">').appendTo(container);
			//获取行索引
			var rowIndex=container.context.parentElement.rowIndex;
			input.attr('id',options.field+'-'+rowIndex);//修复radio、checkbox无id时命名为undefind造成多个并存是的混乱
			input.checkbox(options);
			return input;
		},
		getValue : function(target) {
			return $(target).checkbox('getValue');
		},
		setValue : function(target, value) {
			$(target).checkbox('setValue',value);
		},
		resize : function(target, width) {
			$(target)._outerWidth(width);
		}
	},
	linkage : {
		init : function(container, options) {
			var input=$("<input type=\"text\" class=\"datagrid-editable-input\" >").appendTo(container);
			input.linkage(options||{});
			return input;
		},
		getValue : function(target) {
			return $(target).linkage('getValue');
		},
		setValue : function(target, value) {
		    /*$.fn.datagrid.defaults.editors.combobox.setValue(target,value);*/
		    $(target).linkage('setValue',value);
		},
		resize : function(target, width) {
			$(target).linkage("resize",width);
		}
	},
	numtx : {//不带微调按钮的数值输入框
		init : function(_59c, _59d) {
			var _59e = $("<input type=\"text\" dType=\""+_59d.dType+"\" class=\"datagrid-editable-input\">").appendTo(_59c);
			_59e.numberbox(_59d);
			return _59e;
		},
		destroy : function(_59f) {
			$(_59f).numberbox("destroy");
		},
		getValue : function(_5a0) {
			$(_5a0).blur();
			return $(_5a0).numberbox("getValue");
		},
		setValue : function(_5a1, _5a2) {//注释原方法，避免在点击可编辑表格时就触发onchange事件
			//$(_5a1).numberbox("setValue", _5a2);
			return _5a1.each(function(){
				var _3e9=this;
				var _3ea=_5a2;
				var _3eb=$.data(_3e9,"numberbox");
				var opts=_3eb.options;
				var _3ec=$.data(_3e9,"numberbox").field.val();
				_3ea=opts.parser.call(_3e9,_3ea);
				opts.value=_3ea;
				_3eb.field.val(_3ea);
				$(_3e9).val(opts.formatter.call(_3e9,_3ea));
			});
		},
		resize : function(_5a3, _5a4) {
			$(_5a3)._outerWidth(_5a4);
		}
	}
});

/**
 * 修改datagrid的editors的datebox类型的setValue方法中不触发onchange事件
 */
$.extend($.fn.datagrid.defaults.editors.datebox,{
	setValue : function(target,value){//此方法中不调用onchange事件方法
		return target.each(function(){
			var _80d=this;
			var _803=value;
			var _80f=$.data(_80d,"datebox");
			var opts=_80f.options;
			comboSetValueNoOnchange($(_80d),[value]);
			$(_80d).combo("setText",value);
			_80f.calendar.calendar("moveTo",opts.parser(value));
		});
	}
});

/**
 * 修改datagrid的editors的combobox类型的setValue方法中不触发onchange事件
 */
$.extend($.fn.datagrid.defaults.editors.combobox,{
	setValue : function(target,value){//此方法中不调用onchange事件方法
		return target.each(function(){
			var _7aa=this;
			var opts=$.data(_7aa,"combobox").options;
			var data=$.data(_7aa,"combobox").data;
			var _7ab;
			if(opts.multiple){
				var separator = opts.separator;
				_7ab = value.split(separator);
			}else{
				_7ab=[value];
			}
			var _7ad=$(_7aa).combo("panel");
			_7ad.find("div.combobox-item-selected").removeClass("combobox-item-selected");
			var vv=[],ss=[];
			for(var i=0;i<_7ab.length;i++){
			var v=_7ab[i];
			var s=v;
			for(var j=0;j<data.length;j++){
			if(data[j][opts.valueField]==v){
			s=data[j][opts.textField];
			break;
			}
			}
			vv.push(v);
			ss.push(s);
			_7ad.find("div.combobox-item[value=\""+v+"\"]").addClass("combobox-item-selected");
			}
			comboSetValueNoOnchange($(_7aa),vv);
			$(_7aa).combo("setText",ss.join(opts.separator));
		});
	},
	getValue:function(jq){
		return jq.getValue();
	}
});

/**
 * 修改datagrid的editors的combotree类型的setValue方法中不触发onchange事件
 */
$.extend($.fn.datagrid.defaults.editors.combotree,{
	setValue : function(target,value){//此方法中不调用onchange事件方法
	return target.each(function(){
			var _7d9=this;
			var _7da=[value];
			var opts=$.data(_7d9,"combotree").options;
			var tree=$.data(_7d9,"combotree").tree;
			tree.find("span.tree-checkbox").addClass("tree-checkbox0").removeClass("tree-checkbox1 tree-checkbox2");
			var vv=[],ss=[];
			for(var i=0;i<_7da.length;i++){
			var v=_7da[i];
			var s=v;
			var node=tree.tree("find",v);
			if(node){
			s=node.text;
			tree.tree("check",node.target);
			tree.tree("select",node.target);
			}
			vv.push(v);
			ss.push(s);
			}
			comboSetValueNoOnchange($(_7d9),vv);
			$(_7d9).combo("setText",ss.join(opts.separator));
		});
	}
});
/**
 * 适用于combo组件的设值，此方法不调用onchange事件方法
 * @param target jq对象
 * @param value 类型：数组
 * @return
 */
function comboSetValueNoOnchange(target,value){
	return target.each(function(){
		var _77c=this;
		var _77d=value;
		var opts=$.data(_77c,"combo").options;
		var _778=_77c;
		var _779=[];
		var _77a=$.data(_778,"combo").combo;
		_77a.find("input.combo-value").each(function(){
			_779.push($(this).val());
		});
		var _77e=_779;
		var _77f=$.data(_77c,"combo").combo;
		_77f.find("input.combo-value").remove();
		var name=$(_77c).attr("comboName");
		for(var i=0;i<_77d.length;i++){
			var _780=$("<input type=\"hidden\" class=\"combo-value\">").appendTo(_77f);
			if(name){
				_780.attr("name",name);
			}
			_780.val(_77d[i]);
		}
		var tmp=[];
		for(var i=0;i<_77e.length;i++){
			tmp[i]=_77e[i];
		}
		var aa=[];
		for(var i=0;i<_77d.length;i++){
			for(var j=0;j<tmp.length;j++){
				if(_77d[i]==tmp[j]){
					aa.push(_77d[i]);
					tmp.splice(j,1);
					break;
				}
			}
		}
	});
}

/**
 * <p>
 * 扩展方法mergeCellsMore，用于执行多个合并操作。参数merges为mergeCells方法参数的集合
 * eg:[{index: 2,
 * 		rowspan: 2,
 * 		field:'id',
 * 		colspan:2
 * },{
 * 		index: 5,
 * 		field:'name',
 * 		rowspan: 2}];
 * </p>
 */
$.extend($.fn.datagrid.methods,{
	mergeCellsMore:function(jq,merges){
		jq.each(function(){
			//根据数组进行合并操作
			for(var i=0; i<merges.length; i++){
				$(this).datagrid('mergeCells',{
					index: merges[i].index,
					field: merges[i].field,
					rowspan: merges[i].rowspan,
					colspan: merges[i].colspan
				});
			}
		});
	},findText:function(jq,field,rowIndex){//field为查找的列id、rowIndex为查找的行标识缺省时为当前选中的行
		var target=jq[0]
		if(!rowIndex){
			var row=$(target).datagrid('getSelected');
			rowIndex=$(target).datagrid('getRowIndex',row);
		}
		var opts=$(target).datagrid('options');
		var ros=opts.finder.getTr(target,rowIndex,'body',2);
		return $(ros).find('td[field="'+field+'"]>div').html();
	},
	/**
	 * 动态更换当前编辑行某列的编辑器类型
	 *@param 说明： param: {isSetValue:true/false,field:'',editor:{type:'',options:{}}}
	 * isSetValue: 更换编辑器后是否将编辑表格中原有的值设置到新编辑器中(默认不设置)
	 */
	changeEditor:function(jq,param){
		var $dg = $(jq);
		var row = $dg.datagrid('getSelected');
		var rowIndex = $dg.datagrid('getRowIndex', row);
		if(rowIndex == -1)
			return;
		var field = param.field;
		var editortype = param.editor.type;
		var editorOpts = param.editor.options;
//		//更新column的options属性
//		var columnOptions = $dg.datagrid('getColumnOption',field);
//		for(var o in columnOptions){
//
//			if(editorOpts[o] != undefined && columnOptions[o] !=  editorOpts[o]){
//				
//				columnOptions[o] = editorOpts[o];
//			}
//		}
		
		var isSetValue = false;
		if(param.isSetValue){
			isSetValue = true;
		}
		
		var oldEd =  $dg.datagrid('getEditor', {index:rowIndex,field:field});
		var fieldValue = oldEd.target.getValue();
		
		var e = $dg.datagrid('getColumnOption', field);
		var opts=$dg.datagrid('options');
		var tr=opts.finder.getTr($dg[0],rowIndex,'body',2);
		var td = $(tr).find('td[field="'+field+'"]');
		var cell = $(td).find("div.datagrid-cell");
		var oldHtml = cell.html();
		cell.find('td').html('');
		var ed = opts.editors[editortype];
		var target = ed.init(cell.find("td"),editorOpts);
		var newEditor = {actions:ed,target:target,field:field,type:editortype,oldHtml:oldHtml};
		$.data(cell[0],"datagrid.editor",newEditor);
		e.editor = newEditor;
		if(isSetValue && fieldValue){
			newEditor.target.setValue(fieldValue);
		}
		//调整大小
		var dc=$.data($dg[0],"datagrid").dc;
		var vtd = dc.view.find("div.datagrid-editable").closest('td[field='+field+']');
		var col=$dg.datagrid("getColumnOption",field);
		vtd.find("div.datagrid-editable")._outerWidth(col.width);
		newEditor.actions.resize(target,vtd.find("div.datagrid-editable").width());
	}

});
//------------------------------form
/**
 * <p>扩展load方法满足model.column格式json填充数据;同时支撑radio,checkbox,combo类数据加载。</p>
 * 
 */
$.extend($.fn.form.methods, {
	iload : function(jq, param) {
		return jq.each(function() {
			load(this, param);
		});

		function load(target, param) {
			if (!$.data(target, "form")) {
				$.data(target, "form", {
					options : $.extend( {}, $.fn.form.defaults)
				});
			}
			var options = $.data(target, "form").options;
			if (typeof param == "string") {
				var params = {};
				if (options.onBeforeLoad.call(target, params) == false) {
					return;
				}
				//异步获取数据
				$.ajax( {
					type:"POST",
					url : param,
					data : params,
					success : function(rsp) {
						loadData(rsp);
						options.onLoadSuccess.call(target, rsp);
					},
					error : function(rsp) {
						options.onLoadError.apply(target, arguments);
					},
					dataType:"json"
				});
			} else {
				loadData(param);
			}
			//给表单控件赋值，以支持"对象名.属性名"和"对象名-属性名"格式命名的表单字段
			function loadData(jsonData) {
				jQuery.each(jsonData, function(param, value) {
					var _dSpace = param;
					if(typeof value=="string"){
						jQuery("#" + _dSpace ).setValue(value);
					}else{
						jQuery.each(value, function(_param, _value) {
							jQuery("#" + _dSpace + '\\.' + _param).setValue(_value);
							jQuery("#" + _dSpace + '-' + _param).setValue(_value);
							
							//表单调用iload加载数据时记录每个字段初始值_value，用于关闭页面时比较是否修改内容
							var _cssClass = jQuery("#" + _dSpace + '-' + _param).attr('class');
							if (_cssClass == undefined) {
								_cssClass = "";
							}
							//数值标签特殊处理
							if (_cssClass.indexOf('number') >= 0) {
								jQuery("#" + _dSpace + '-' + _param).attr('_value',jQuery("#" + _dSpace + '-' + _param).getValue());
							}else{
								jQuery("#" + _dSpace + '-' + _param).attr('_value',_value);
							}
						});
					}
					
				});
			}
			;
		}
	}
});
//-----------------------tabs
/**
 * <p>扩展onSelect事件，由于使用了iframe刷新iframe需要特殊处理</p>
 * 修改说明：在onSelect事件中设置tab页为绝对定位
 */
$.extend($.fn.tabs.defaults,{
	onSelect:function(title,index){
		var tab=$(this).tabs('getSelected');
		var tbId = tab.attr("id");
		var opts=tab.panel('options');
		
		/**
		 * 设置tab页的css属性positon为absolute(绝对定位)，然后通过设置tab页的css属性z-index来控制显示第几个tab页。
		 * 解决问题：快速切换tab页时，出现datagrid不显示的问题。
		 * 问题原因：html元素设置display为none后，html元素的css属性width、height为0，创建datagrid元素时出现问题。
		 * 注:此种方式依赖于重写的panel的close方法
		 * add by zangys at 2015-06-17 14:36
		 * 
		 * 由于导致一些现实问题，注释掉此修改 
		 * add by liwei1 at 2015-12-21 14:11
		 */
		//-----------begin------------
		/*tab.parent()[0].style.position = "absolute";
		$(tab).closest('div.tabs-panels').find('div.panel').each(function(i,obj){
			$(obj).css('z-index',0)
		});
		$(tab).closest('div.panel').css('z-index',index+1);*/
		//-----------end--------------
		
		//urlModel为iframe时刷新操作
		if (opts.urlModel=='iframe') {
			//获取tab的iframe对象
			var frame = tab.find('iframe');
			if(frame.length > 0){
				if(!tab.panel('options').cache){
					frame[0].src = 'about:blank';
					frame[0].contentWindow.document.write('');
					$(frame[0]).empty();
					frame[0].contentWindow.close();
					frame.remove();
				}else{
					/**
					 * 在执行完之后执行onAfterSelect方法
					 * 可以在用此方法执行一些tab切换之后的操作处理
					 * add by liwei at 2015-10-26 09:56
					 */
					var tabOpts = $(this).tabs('options');
					if(tabOpts && tabOpts.onAfterSelect){
						tabOpts.onAfterSelect.call(this,title,index);
					}
					return false;
				}
			}
			if(opts.url){
				//tab.panel({content:'<iframe scrolling="auto" frameborder="0"  src="'+opts.url+'" style="width:100%;height:99%;"></iframe>'});
				//针对页面弹出提示窗口时出现滚动条的情况 2015-06-02
				tab.panel({content:'<iframe scrolling="auto" frameborder="0"  src="'+opts.url+'" style="width:100%;"></iframe>'});
				tab.find("iframe").attr('height',tab.find("iframe").closest('div').height()-4);
			}
			var tabsOpts = $(this).tabs('options');
			var myframe = tab.find('iframe');
			if(tabsOpts.onLoad && myframe.length > 0){
				myframe[0].onload = function(){
					tabsOpts.onLoad.call(this,tab);
				}
			}
			
		}else{//非iframe时刷新
			var href=tab.panel('options').href;
			if(href){
//				if(!tab.panel('options').cache){
//					tab.panel('refresh');//设置cache=false后，选择此tab时会自动刷新，不需要手动刷新，此处会造成重复加载
//				}	
			}else{
				tab.panel({href:opts.url});
			}
			
			//解决Tab页签urlModel=none浏览器坚向滚动条失效【环境：IE7】 
			$('.panel-body').css('position','relative');
		}
		/**
		 * 在执行完之后执行onAfterSelect方法
		 * 可以在用此方法执行一些tab切换之后的操作处理
		 * add by liwei at 2015-10-26 09:56
		 */
		var tabOpts = $(this).tabs('options');
		if(tabOpts && tabOpts.onAfterSelect){
			var onAfterSelect = null;
			if(typeof tabOpts.onAfterSelect == "string"){
				onAfterSelect = eval(tabOpts.onAfterSelect);
			}else{
				onAfterSelect = tabOpts.onAfterSelect;
			}
			onAfterSelect.call(this,title,index);
		}
	}
});

/**
 * 重写panel的close方法
 * 实现功能：绝对定位的tab页在切换时不隐藏tab页(与tabs的onSelect事件对应)
 * 
 * 由于导致一些现实问题，注释掉此修改 add by liwei1 at 2015-12-21 14:11
 */
/*$.extend($.fn.panel.methods,{
	close:function(jq,_1e8){
		return jq.each(function(){
			_1b4(this,_1e8);
		});
		
		function _1b4(_1c4,_1c5){
			var opts=$.data(_1c4,"panel").options;
			var _1c6=$.data(_1c4,"panel").panel;
			if(_1c5!=true){
			if(opts.onBeforeClose.call(_1c4)==false){
			return;
			}
			}
			_1c6._fit(false);
			//暂时注释，此代码影响panel的close方法正常关闭
			//此if条件与tabs的onSelect事件相对应
			//if($(_1c4).parent().parent().attr('class')!="tabs-panels" && $(_1c4).parent().css('position')=="absolute")
			_1c6.hide();
			opts.closed=true;
			opts.onClose.call(_1c4);
			};
		}
});*/

//-------------------------------combogrid
/**
 * <p>扩展combogrid 的query方法，通过queryParams设置[参数]=$q将输入框的值给特定的查询参数，以支撑更灵活便捷的查询</p>
 */
$.extend($.fn.combogrid.defaults.keyHandler,{
	query:function(q){
	var opts=$.data(this,"combogrid").options;
	var grid=$.data(this,"combogrid").grid;
	$.data(this,"combogrid").remainText=true;
	if(opts.multiple&&!q){
	_7f2(this,[],true);
	}else{
	_7f2(this,[q],true);
	}
	if(opts.mode=="remote"){
		
		grid.datagrid("clearSelections");
		queryParams=opts.queryParams;
		queryParams['q']=q;
		queryParams=parseParams(queryParams,q);
		grid.datagrid("load",queryParams);
	}else{
	if(!q){
	return;
	}
	var rows=grid.datagrid("getRows");
	for(var i=0;i<rows.length;i++){
	if(opts.filter.call(this,q,rows[i])){
	grid.datagrid("clearSelections");
	grid.datagrid("selectRow",i);
	return;
	}
	}
	}
	function _7f2(_7f3,_7f4,_7f5){
		var opts=$.data(_7f3,"combogrid").options;
		var grid=$.data(_7f3,"combogrid").grid;
		var rows=grid.datagrid("getRows");
		var ss=[];
		for(var i=0;i<_7f4.length;i++){
		var _7f6=grid.datagrid("getRowIndex",_7f4[i]);
		if(_7f6>=0){
		grid.datagrid("selectRow",_7f6);
		ss.push(rows[_7f6][opts.textField]);
		}else{
		ss.push(_7f4[i]);
		}
		}
		if($(_7f3).combo("getValues").join(",")==_7f4.join(",")){
		return;
		}
		$(_7f3).combo("setValues",_7f4);
		if(!_7f5){
		$(_7f3).combo("setText",ss.join(opts.separator));
		}
		};
		//处理参数中$q值替换
	function parseParams( a,q,traditional) {
			var prefix,
				s={},
				add = function( key, value ) {
					// If value is a function, invoke it and return its value
					value = jQuery.isFunction( value ) ? value() : ( value == null ? "" : (jQuery.trim(value)=="$q" ?value=q : jQuery.trim(value)) );
					if(value!="")
						s[ key  ]= value ;
				};

			// Set traditional to true for jQuery <= 1.3.2 behavior.
			if ( traditional === undefined ) {
				traditional = jQuery.ajaxSettings && jQuery.ajaxSettings.traditional;
			}
			// If an array was passed in, assume that it is an array of form elements.
			if ( jQuery.isArray( a ) || ( a.jquery && !jQuery.isPlainObject( a ) ) ) {
				// Serialize the form elements
				jQuery.each( a, function() {
					add( this.name, this.value ,q);
				});

			} else {
				// If traditional, encode the "old" way (the way 1.3.2 or older
				// did it), otherwise encode params recursively.
				for ( prefix in a ) {
					buildParams( prefix, a[ prefix ], traditional, add ,q);
				}
			}
			function buildParams( prefix, obj, traditional, add ,q) {
				var name;

				if ( jQuery.isArray( obj ) ) {
					// Serialize array item.
					jQuery.each( obj, function( i, v ) {
						if ( traditional || rbracket.test( prefix ) ) {
							// Treat each array item as a scalar.
							add( prefix, v ,q);

						} else {
							// If array item is non-scalar (array or object), encode its
							// numeric index to resolve deserialization ambiguity issues.
							// Note that rack (as of 1.0.0) can't currently deserialize
							// nested arrays properly, and attempting to do so may cause
							// a server error. Possible fixes are to modify rack's
							// deserialization algorithm or to provide an option or flag
							// to force array serialization to be shallow.
							buildParams( prefix + "[" + ( typeof v === "object" ? i : "" ) + "]", v, traditional, add );
						}
					});

				} else if ( !traditional && jQuery.type( obj ) === "object" ) {
					// Serialize object item.
					for ( name in obj ) {
						buildParams( prefix + "[" + name + "]", obj[ name ], traditional, add ,q);
					}

				} else {
					// Serialize scalar item.
					add( prefix, obj ,q);
				}
			}
			// Return the result
			return s;
		};
	}
});
//--------------------------propertygrid
/**
 * <p>扩展propertygrid新增getKcollData方法获取以满足kcoll形式的数据传递，参数ModelId：如果需要在field前添加表模型通过该参数指定。
 * 修改urlLoad方法加载数据，参数：url。调用方法$('#id').propertygrid('urlLoad',url);该方法可以兼容EMP原来kcoll取数据
 * </p>
 */
$.extend($.fn.propertygrid.methods,{
	getKcollData:function(jq,ModelId){
	    var data={};
	    ModelId=ModelId?ModelId+'.':'';
	    var rows=$(jq[0]).datagrid('getRows');
		for(var i=0;i<rows.length-1;i++){
			data[ModelId+rows[i].field]=rows[i].value;
		}
		return data;
	},
	urlLoad:function(jq,url){
		var opts=$.data(jq[0],"datagrid").options;
		$.post(url,opts.queryParams,function(data){
			var jsonData=$.parseJSON(data);
			var olddata=opts.data;
			var columns=opts.columns;
			//icoll数据
			if(jsonData.rows){
				var rows=jsonData.rows;
				for(var i=0;i<rows.length;i++){
					//增加列
					if(i>0){
						columns[0].push({field:'value'+i,title:'value'+i,width:150,resizable:false,formatter:$.transValueC});
					}
					for(var j=0;j<olddata.rows.length;j++){
						var val='';
							try{
								val=rows[i][olddata.rows[j].field];
							}catch(e){}
						if(i>0)
							eval("olddata.rows[j].value"+i+"='"+val+"'");				
						else
							olddata.rows[j].value=val;
					}
				}
			}else{//kcoll数据
				var tablename,tabledata;
				jQuery.each(jsonData, function(param, value) {
					tablename=param;
					tabledata=value;
				});
				eval('var '+ tablename+'=tabledata')
				for(var i=0;i<olddata.rows.length;i++){
					var val='';
						try{
							//val=eval(olddata.rows[i].field);
							val=eval(tablename)[olddata.rows[i].field];
						}catch(e){}
					olddata.rows[i].value=val;				
				}
			}
			
			$(jq).propertygrid({columns:columns,data:olddata});
		});
	}
});
//----------------------------------------tree/comobotree/treegrid
/**
 * <p>tree/combotree 的默认数据过滤，将扁平数据格式化为标准树结构</p>
 * <p>添加keyFields辅助主键，以支持联合主键构建树 add by wangbin 2014-3-27 17:01:30</p>
 */
defaultLoadFilter = {
		loadFilter : function(datas, parent) {
			var data=datas.rows;
			var opt = $(this).data().tree.options;
			if(data){
				var idField, textField, parentField,keyFields;
				if (opt.parentField) {
					idField = opt.idField || 'id';
					textField = opt.textField || 'text';
					parentField = opt.parentField || 'pid';
					//辅助主键：以逗号分隔。例如：id为主键，id1、id2为辅助主键，idField:'id',keyFields:'id1,id2'
					keyFields=opt.keyFields||'';
					function getPre(data){
						var temp='';
						//如果存在辅助主键
						if(keyFields){
							var fields=keyFields.split(',');
							for(var i=0;i<fields.length;i++){
								temp+=data[fields[i]]+'-';
							}
						}
						return temp;
					}
					var i, l, treeData = [], tmpMap = [];
					for (i = 0, l = data.length; i < l; i++) {
						tmpMap[getPre(data[i])+data[i][idField]] = data[i];
					}
					for (i = 0, l = data.length; i < l; i++) {
						var parent=tmpMap[getPre(data[i])+data[i][parentField]];//根据联合主键确定父节点
						if (parent && data[i][idField] != data[i][parentField]) {
							if (!parent['children'])
								parent['children'] = [];
							data[i]['text'] = data[i][textField];
							data[i]['id'] = getPre(data[i])+data[i][idField];//构建联合主键
							parent['children'].push(data[i]);
						} else {
							data[i]['text'] = data[i][textField];
							data[i]['id'] = getPre(data[i])+data[i][idField];
							treeData.push(data[i]);
						}
					}
					return treeData;
				}
			}
			return datas;
		},
		onBeforeLoad:function(node, param){//用于配合联合主键树形表格的查询，将主键、辅键传给后台
			if(node){
				param=param||{};
				var opt = $(this).data().tree.options;
				var keyFields=opt.keyFields;
				var id=node.id;
				if(keyFields){
					var fields=keyFields.split(',');//辅键
					var fieldsValues=id.split('-');
					for(var i=0;i<fields.length;i++){
						param[fields[i]]=fieldsValues[i];
					}
					param[opt.idField]=fieldsValues[fieldsValues.length-1];//主键
				}
			}
		}
	};
$.extend($.fn.combotree.defaults, defaultLoadFilter);
$.extend($.fn.tree.defaults, defaultLoadFilter);

/**
 *<p> treegrid数据过滤将扁平数据转换成标准树结构</p>
 *<p>添加keyFields辅助主键，以支持联合主键构建树 add by wangbin 2014-3-27 17:01:30</p>
 */
$.extend($.fn.treegrid.defaults, {
	loadFilter : function(datas, parentId) {
	var data=datas.rows;
	if(data){
		var opt = $(this).data().treegrid.options;
		var idField, textField, parentField,keyFields;
		if (opt.parentField) {
			idField = opt.idField || 'id';
			textField = opt.treeField || 'text';
			parentField = opt.parentField || 'pid';
			//辅助主键：以逗号分隔。例如：id为主键，id1、id2为辅助主键，idField:'id',keyFields:'id1,id2'
			keyFields=opt.keyFields||'';
			function getPre(data){
				var temp='';
				//如果存在辅助主键
				if(keyFields){
					var fields=keyFields.split(',');
					for(var i=0;i<fields.length;i++){
						temp+=data[fields[i]]+'-';
					}
				}
				return temp;
			}
			var i, l, treeData = [], tmpMap = [];
			for (i = 0, l = data.length; i < l; i++) {
				tmpMap[getPre(data[i])+data[i][idField]] = data[i];
			}
			for (i = 0, l = data.length; i < l; i++) {
				var parent=tmpMap[getPre(data[i])+data[i][parentField]];
				if (parent && data[i][idField] != data[i][parentField]) {
					if (!parent['children'])
						parent['children'] = [];
					data[i]['text'] = data[i][textField];
					data[i]['id'] = getPre(data[i])+data[i][idField];
					parent['children'].push(data[i]);
				} else {
					data[i]['text'] = data[i][textField];
					data[i]['id'] = getPre(data[i])+data[i][idField];
					treeData.push(data[i]);
				}
			}
			return treeData;
		}
	}
		return data;
	},
	onBeforeLoad:function(row, param){//用于配合联合主键树形表格的查询，将主键、辅键传给后台
		if(row){
			param=param||{};
			var opt = $(this).data().treegrid.options;
			var keyFields=opt.keyFields;
			if(keyFields){
				var fields=keyFields.split(',');//辅键
				for(var i=0;i<fields.length;i++){
					param[fields[i]]=row[fields[i]];
				}
				param[opt.idField]=row[opt.idField];//主键
			}
		}
	}
});
//----------------------------------------panel/window/dialog
/**
 * <p>panel关闭时回收内存，主要用于布局组件使用iframe嵌入网页时的内存泄漏问题</p>
 */
$.extend($.fn.panel.defaults, {
	onBeforeDestroy : function() {
		var frame=$(this).find('iframe');
		try {
			if (frame.length > 0) {
				for (var i = 0; i < frame.length; i++) {
					frame[i].src = 'about:blank';
					frame[i].contentWindow.document.write('');
					$(frame[i]).empty();
					frame[i].contentWindow.close();
				}
				frame.remove();
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
						alert(e);
					}
				}
			}
		} catch (e) {
			alert(e);
		}
	}
});

/**
 * <p>window关闭时回收内存</p>
 * 注意：此种扩展方法限制了onBeforeDestroy的自定义功能，如果在使用中动态指定onBeforeDestroy，则此处被覆盖。
 */
$.extend($.fn.window.defaults, {
	onBeforeDestroy : function() {
		var frame=$(this).find('iframe');
		try {
			if (frame.length > 0) {
				for (var i = 0; i < frame.length; i++) {
					frame[i].src = 'about:blank';
					frame[i].contentWindow.document.write('');
					$(frame[i]).empty();
					frame[i].contentWindow.close();
				}
				frame.remove();
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
						alert(e);
					}
				}
			}
		} catch (e) {
			alert(e);
		}
	}
});


/**
 * <p>window关闭时回收内存</p>
 * 注意：此种扩展方法限制了onBeforeDestroy的自定义功能，如果在使用中动态指定onBeforeDestroy，则此处被覆盖。
 */
$.extend($.fn.dialog.defaults, {
	onBeforeDestroy : function() {
		var frame=$(this).find('iframe');
		try {
			if (frame.length > 0) {
				for (var i = 0; i < frame.length; i++) {
					frame[i].src = 'about:blank';
					frame[i].contentWindow.document.write('');
					$(frame[i]).empty();
					frame[i].contentWindow.close();
				}
				frame.remove();
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
						alert(e);
					}
				}
			}
		} catch (e) {
			alert(e);
		}
	}
});



/**
 * <p>防止panel/window/dialog组件超出浏览器边界</p>
 */
panelonMove = {
	onMove : function(left, top) {
		var l = left;
		var t = top;
		if (l < 1) {
			l = 1;
		}
		if (t < 1) {
			t = 1;
		}
		var width = parseInt($(this).parent().css('width')) + 14;
		var height = parseInt($(this).parent().css('height')) + 14;
		var right = l + width;
		var buttom = t + height;
		var browserWidth = $(window).width();
		var browserHeight = $(window).height();
		if (right > browserWidth) {
			l = browserWidth - width;
		}
		if (buttom > browserHeight) {
			t = browserHeight - height;
		}
		$(this).parent().css({/* 修正面板位置 */
			left : l,
			top : t
		});
	}
};
$.extend($.fn.dialog.defaults, panelonMove);
$.extend($.fn.window.defaults, panelonMove);

/**
 * <p>重写combo组件的setText方法。 根据text值是否为空决定是否进行验证，用于解决页面在初始化后就显示输入错误的图标"!"</p>
 */
$.extend($.fn.combo.methods,{
	setText:function(jq,text){
	return jq.each(function(){
		var combo=$.data(this,"combo").combo;
		combo.find("input.combo-text").val(text);
		var opts=$.data(this,"combo").options;
		var combo_text=$.data(this,"combo").combo.find("input.combo-text");
		combo_text.validatebox(opts);
		if(text){
			combo_text.validatebox("validate");
		}
		$.data(this,"combo").previousValue=text;
	});
	}
});

/**
 * <p>重写validatebox组件的validate/isValid方法，校验必输时，去掉校验字符串起始和结尾的空格。</p>
 */
(function($){
	$.extend($.fn.validatebox.methods,{
		validate:function(jq){
		return jq.each(function(){
			validateExt(this);
		});
		},
		isValid:function(jq){
			return validateExt(jq[0]);
		}
	});
	
	function validateExt(jq){
		var _3b3 = jq;
		var _3b4=$.data(_3b3,"validatebox");
		var opts=_3b4.options;
		var tip=_3b4.tip;
		var box=$(_3b3);
		var _3b5=box.val();
		
		if(opts.required){
		//校验时去掉字符串起始和结尾的空格。
		if($.trim(_3b5)==""){
			box.addClass("validatebox-invalid");
			_3b6(opts.missingMessage);
			if(_3b4.validating){
				_3aa(_3b3);
			}
				return false;
			}
		}
		if(opts.validType){
			if(typeof opts.validType=="string"){
			if(!_3b7(opts.validType)){
				return false;
			}
			}else{
				for(var i=0;i<opts.validType.length;i++){
					if(!_3b7(opts.validType[i])){
						return false;
					}
				}
			}
		}
		box.removeClass("validatebox-invalid");
		_3a9(_3b3);
		return true;
		
		function _3b6(msg){
			_3b4.message=msg;
		};
		function _3b7(_3b8){
			var _3b9=/([a-zA-Z_]+)(.*)/.exec(_3b8);
			var rule=opts.rules[_3b9[1]];
			if(rule&&_3b5){
			var _3ba=eval(_3b9[2]);
			if(!rule["validator"](_3b5,_3ba)){
			box.addClass("validatebox-invalid");
			var _3bb=rule["message"];
			if(_3ba){
			for(var i=0;i<_3ba.length;i++){
			_3bb=_3bb.replace(new RegExp("\\{"+i+"\\}","g"),_3ba[i]);
			}
			}
			_3b6(opts.invalidMessage||_3bb);
			if(_3b4.validating){
			_3aa(_3b3);
			}
			return false;
			}
			}
			return true;
		};
		function _3aa(_3ab){
			var msg=$.data(_3ab,"validatebox").message;
			var tip=$.data(_3ab,"validatebox").tip;
			if(!tip){
			tip=$("<div class=\"validatebox-tip\">"+"<span class=\"validatebox-tip-content\">"+"</span>"+"<span class=\"validatebox-tip-pointer\">"+"</span>"+"</div>").appendTo("body");
			$.data(_3ab,"validatebox").tip=tip;
			}
			tip.find(".validatebox-tip-content").html(msg);
			_3ac(_3ab);
		};
		function _3ac(_3ad){
			var _3ae=$.data(_3ad,"validatebox");
			if(!_3ae){
			return;
			}
			var tip=_3ae.tip;
			if(tip){
			var box=$(_3ad);
			var _3af=tip.find(".validatebox-tip-pointer");
			var _3b0=tip.find(".validatebox-tip-content");
			tip.show();
			tip.css("top",box.offset().top-(_3b0._outerHeight()-box._outerHeight())/2);
			if(_3ae.options.tipPosition=="left"){
			tip.css("left",box.offset().left-tip._outerWidth());
			tip.addClass("validatebox-tip-left");
			}else{
			tip.css("left",box.offset().left+box._outerWidth());
			tip.removeClass("validatebox-tip-left");
			}
			_3af.css("top",(_3b0._outerHeight()-_3af._outerHeight())/2);
			}
		};
		function _3a9(_3b1){
			var tip=$.data(_3b1,"validatebox").tip;
			if(tip){
			tip.remove();
			$.data(_3b1,"validatebox").tip=null;
			}
		};
	}
})(jQuery)

/**
 * <p>扩展datagrid方法</p>
 * <p>注意：使用自动合并单元格删除行功能需要指定标志字段：idField</p>
 * <p>autoMergeCells:根据内容自动合并单元格 
 * @fields 可为空,为数组:['fieldName1','fieldName2','fieldName3'....]</p>
 * <p>deleteRow4AutoMergeCells:自动合并单元格删除行
 * @index 删除行索引</p>
 */
$.extend($.fn.datagrid.methods, {  
    autoMergeCells : function (jq, fields) {  
        return jq.each(function () {  
            var target = $(this);  
            if (!fields) {  
                fields = target.datagrid("getColumnFields");  
            }  
            var rows = target.datagrid("getRows");  
            var i = 0,  
            j = 0,  
            temp = {};  
            for (i; i < rows.length; i++) {  
                var row = rows[i];  
                j = 0;  
                for (j; j < fields.length; j++) {  
                    var field = fields[j];  
                    var tf = temp[field];  
                    if (!tf) {  
                        tf = temp[field] = {};  
                        tf[row[field]] = [i];  
                    } else {  
                        var tfv = tf[row[field]];  
                        if (tfv) {  
                            tfv.push(i);  
                        } else {  
                            tfv = tf[row[field]] = [i];  
                        }  
                    }  
                }  
            }
            var mergesObj = []; 
            var opts = target.datagrid("options"); 
            $.each(temp, function (field, colunm) {  
                $.each(colunm, function () {  
                    var group = this;  
                      
                    if (group.length > 1) {  
                        var before,  
                        after,  
                        megerIndex = group[0];  
                        for (var i = 0; i < group.length; i++) {  
                            before = group[i];  
                            after = group[i + 1];  
                            if (after && (after - before) == 1) {  
                                continue;  
                            }  
                            var rowspan = before - megerIndex + 1;
                            if (rowspan > 1) {
	                            var merge =  {  
	                                    index : megerIndex,  
	                                    field : field,  
	                                    rowspan : rowspan,
	                                    keys : []  
	                                } 
                                target.datagrid('mergeCells', merge);
	                            mergesObj.push(merge);
                            }  
                            if (after && (after - before) != 1) {  
                                megerIndex = after;  
                            }  
                        }  
                    }  
                });  
            });
            if(!opts.idField){
            	return;
            }
            //获取分组信息包含的各行数据key
			for(var i=0;i<mergesObj.length;i++){
				if(!mergesObj[i].rowspan)
					continue;
				for(var j=0;j<mergesObj[i].rowspan;j++){
					if((mergesObj[i].index+j)>rows.length){
						continue;
					}
					var row =  rows[mergesObj[i].index+j];
					mergesObj[i].keys.push(row[opts.idField]);
				}
			}
			
            opts.merges = mergesObj;
        });  
    },
    deleteRow4AutoMergeCells:function(jq,index){
    	if(index != undefined && index != "" && isNaN(index)){
    		return;
    	}
    	var $dg = $(jq);
		var opts = $dg.datagrid('options');
		var idFieldValue = $dg.datagrid('getRows')[index][opts.idField];
		var displayTDRowObj = [];
		for(var i=0;i<opts.merges.length;i++){
			var merge = opts.merges[i];
			if(merge.index > index){
				merge.index += -1;
			}
			var displayTDObj = {};
			if(merge.index == index && merge.rowspan>1){
				displayTDObj.rowIndex = index;
				displayTDObj.field = merge.field;
				displayTDRowObj.push(displayTDObj);
			}
			
			for(var j=0;j<merge.keys.length;j++){
				var key = merge.keys[j];
				if(key == idFieldValue && merge.rowspan > 1){
					merge.rowspan += -1;
					opts.merges[i].keys[j] = "";
					break;
				}
			}
		}
		
		$dg.datagrid('deleteRow',index);
		//如果合并单元格起始行被删除，则将下一行的隐藏信息展示出来
		for(var l=0;l<displayTDRowObj.length;l++){
			var tr = opts.finder.getTr($dg[0],displayTDRowObj[l].rowIndex,'body',2);
			if(tr.length>0){
				tr.find('td[field="'+displayTDRowObj[l].field+'"]').css({'display':''});
			}
		}
		
		var merges = opts.merges;
		for(var i=0; i<merges.length; i++){
			mergeCells($dg,{
				index: merges[i].index,
				field: merges[i].field,
				rowspan: merges[i].rowspan,
				colspan: merges[i].colspan
			});
		}
		
		/**
		 * 修改datagrid的mergeCells方法 : rowspan==1&&colspan==1 不return
		 */
		function mergeCells(jq,_5e9){
			return jq.each(function(){
				_57c(this,_5e9);
				});
			function _57c(_57d,_57e){
				var opts=$.data(_57d,"datagrid").options;
				_57e.rowspan=_57e.rowspan||1;
				_57e.colspan=_57e.colspan||1;
				/*
				if(_57e.rowspan==1&&_57e.colspan==1){
				return;
				}
				*/
				var tr=opts.finder.getTr(_57d,(_57e.index!=undefined?_57e.index:_57e.id));
				if(!tr.length){
				return;
				}
				var row=opts.finder.getRow(_57d,tr);
				var _57f=row[_57e.field];
				var td=tr.find("td[field=\""+_57e.field+"\"]");
				td.attr("rowspan",_57e.rowspan).attr("colspan",_57e.colspan);
				td.addClass("datagrid-td-merged");
				for(var i=1;i<_57e.colspan;i++){
				td=td.next();
				td.hide();
				row[td.attr("field")]=_57f;
				}
				for(var i=1;i<_57e.rowspan;i++){
				tr=tr.next();
				if(!tr.length){
				break;
				}
				var row=opts.finder.getRow(_57d,tr);
				var td=tr.find("td[field=\""+_57e.field+"\"]").hide();
				row[td.attr("field")]=_57f;
				for(var j=1;j<_57e.colspan;j++){
				td=td.next();
				td.hide();
				row[td.attr("field")]=_57f;
				}
				}
				_4d0(_57d);
				};
			function _4d0(_4d5){
				var dc=$.data(_4d5,"datagrid").dc;
				dc.body1.add(dc.body2).find("td.datagrid-td-merged").each(function(){
				var td=$(this);
				var _4d6=td.attr("colspan")||1;
				var _4d7=_4a3(_4d5,td.attr("field")).width;
				for(var i=1;i<_4d6;i++){
				td=td.next();
				_4d7+=_4a3(_4d5,td.attr("field")).width+1;
				}
				$(this).children("div.datagrid-cell")._outerWidth(_4d7);
				});
				};
			function _4a3(_4db,_4dc){
				function find(_4dd){
				if(_4dd){
				for(var i=0;i<_4dd.length;i++){
				var cc=_4dd[i];
				for(var j=0;j<cc.length;j++){
				var c=cc[j];
				if(c.field==_4dc){
				return c;
				}
				}
				}
				}
				return null;
				};
				var opts=$.data(_4db,"datagrid").options;
				var col=find(opts.columns);
				if(!col){
				col=find(opts.frozenColumns);
				}
				return col;
				};
		}
    }
}); 

/**
 * <p>number标签增加getText方法，取显示值</p> 
 */
$.extend($.fn.numberbox.methods,{
	getText:function(jq){
		return $(jq).val();
	}
});

/**
 * 设置numberspinner默认的onChange方法
 * 方法中按顺序执行beforeOnchange empOnchange afterOnchange
 * 其中emp标签中的onchange属性对应的是empOnchange
 */
$.extend($.fn.numberspinner.defaults,{
    onChange:function(newValue, oldValue){
        var opts = $(this).numberspinner("options");
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(this, newValue, oldValue);
        }
        if(opts.empOnchange){
            opts.empOnchange.call(this, newValue, oldValue);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(this, newValue, oldValue);
        }
        
    }
});

/**
 * 设置numberbox默认的onChange方法(emp:numtx标签，无微调按钮)
 * 方法中按顺序执行beforeOnchange empOnchange afterOnchange
 * 其中emp标签中的onchange属性对应的是empOnchange
 */
$.extend($.fn.numberbox.defaults,{
    onChange:function(newValue, oldValue){
        var opts = $(this).numberbox("options");
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(this, newValue, oldValue);
        }
        if(opts.empOnchange){
            opts.empOnchange.call(this, newValue, oldValue);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(this, newValue, oldValue);
        }
        
    }
});

//-----------------datebox/datetime-------------//
/**
 * 设置默认的onchange事件用来支持emp属性扩展扩展
 * 分别调用beforeOnchange  empOnchange afterOnchange
 */
$.extend($.fn.datebox.defaults,{
    onChange:function(newValue, oldValue){
        var opts = $(this).datebox("options");
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(this, newValue, oldValue);
        }
        if(opts.empOnchange){
            opts.empOnchange.call(this, newValue, oldValue);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(this, newValue, oldValue);
        }
    }
});
/**
 * 设置默认的onchange事件用来支持emp属性扩展扩展
 * 分别调用beforeOnchange  empOnchange afterOnchange
 */
$.extend($.fn.datetimebox.defaults,{
    onChange:function(newValue, oldValue){
        var opts = $(this).datetimebox("options");
        if(opts.beforeOnchange){
            opts.beforeOnchange.call(this, newValue, oldValue);
        }
        if(opts.empOnchange){
            opts.empOnchange.call(this, newValue, oldValue);
        }
        if(opts.afterOnchange){
            opts.afterOnchange.call(this, newValue, oldValue);
        }
    }
});

/**
 * datagrid onLoadSuccess事件扩展部分
 * 此方法调用代码在Table.java中指定
 * 目前扩展：
 * 1、页面竖向滚动条盖住部分表格的问题；
 * 2、返回0条数据时，列标题显示不全的问题
 * ......
 */
function DataGridOnLoadSuccessExt(data){
	//解决页面竖向滚动条盖住部分表格的问题
	var jq=this;
	setTimeout(function(){
		$(jq).datagrid('resize');
	},0);
	
	//解决返回0条数据时，列标题显示不全的问题
	if(data.total == 0 && data.rows.length==0){
		var dc = $(this).data('datagrid').dc;
		var header2Row = dc.header2.find('tr.datagrid-header-row');
		dc.body2.find('table').append(header2Row.clone().css({"visibility":"hidden"}));
	}
}
/**
 * button的默认click扩展方法
 * 将按钮禁用并倒计时,倒计时结束文本恢复，禁用取消
 * 若lapseTime不为数字，将不进行按钮禁用倒计时操作，只有当设置了lapseTime为数字并大于0，则开始按钮禁用并倒计时
 */
function ButtonDefaultClick(but, lapseTime) {
	//判断延时时间是否为数字
	if(!isNaN(lapseTime)) {
		//为数字
		var opts = $(but).linkbutton('options');
		//获取原有文本
		var text = opts.text;
		CountDown();
		var countdown = setInterval(CountDown, 1000);
		function CountDown() {
			//按钮禁用并提示
			$(but).linkbutton('disable');
			$(but).linkbutton({text:CusLang.EUIExt.extButtonDefaultClick.textMsgPre+'('+lapseTime+')'});
			if (lapseTime <= 0) {
				//改回原有文本，按钮启用
				$(but).linkbutton({text: text});
				$(but).linkbutton('enable');
				clearInterval(countdown);
				return;
			}
			lapseTime--;
		}
	}else {
		//不为数字
		return;
	}
}

/**
 * 设置表格默认行高  调用方法位于Table.java类中  
 * 1920分辨率下该方法会被覆盖
 * @returns {String}
 */
function setDefaultGridHeight() {
	return 'height: 33px';
}

/**
 * 表格默认表头高度  
 * 1920分辨率下该方法会被覆盖
 */
function setDatagridHeader(i,th){
    $(this).css('height','33px');
}
/**
 * 调用设置表头高度方法
 */
$(function () {
	$(".datagrid-header-row td").each(setDatagridHeader);
});





