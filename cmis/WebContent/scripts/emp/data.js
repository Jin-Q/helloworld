/**
 * data.js
 * 该脚本文件包含了数据对象、编辑域、数据分组及数据列表处理的实现类，
 * 其中编辑域为基类，各种扩展的编辑域类保存在fields.js脚本中。
 *
 * @author LiJia
 * @since 2007-12
 * @lastmodified 2008-9-5
 */
 
  
/*
 * 以下三个类 EMP.data.Field、EMP.data.KColl、EMP.data.IColl
 * 为数据对象封装类，用于页面开发人员在JS中获取页面数据元素。
 * 它们保存了到实际编辑域或数据列表的引用，数据结构和对象命名方式则和后台保持一致。
 *
 * 在信贷一期中，是直接将编辑域和数据列表对象生成到window下供调用，
 * 由于编辑域和数据列表中的属性和方法众多，如果按照二期的嵌套数据形式进行对象命名，
 * 则很难保证不出现重名的情况（例如，user.setValue这个命名可理解为user域的setValue方法，
 * 也可理解为user分组下的setValue域），因此特地将页面开发人员直接引用的对象独立出来，
 * 仅包含尽可能少的常用方法，并通过对实际编辑域的引用来进行其它方法的调用。
 */
 
/**
 * EMP.data.Field
 * 单个数据（数据域）的封装类。
 * 它保存了对页面某个编辑域（EMP.field.Base或其子类）实例的引用。
 * 仅为存取值以及向表单复制值的常用操作提供了方法，其余方法通过引用来进行调用。
 * 所有方法采用下划线开头，原因见EMP.data.KColl的说明。
 */
if (!EMP.data.Field) {

	/**
	 * 构造方法
	 * @param obj 所引用的编辑域对象
	 */
	EMP.data.Field = function(obj) {
		this._obj = obj;
		obj.data = this;	//将自身设为编辑域的data属性
	};
	
	/**
	 * 设置取值的快捷方法
	 * @param value 待设置的取值
	 */
	EMP.data.Field.prototype._setValue = function (value) {
		this._obj.setValue(value);
	};
	
	/**
	 * 获得取值的快捷方法
	 * @return 数据域的取值
	 */
	EMP.data.Field.prototype._getValue = function () {
		return this._obj.getValue();
	};

	/**
	 * 将该数据域的取值复制到指定表单
	 * @param form 指定表单DOM对象
	 */
	EMP.data.Field.prototype._toForm = function (form, prefix) {
		this._obj.toForm(form, prefix);
	};
	
	/**
	 * 对该数据域的值进行校验
	 */
	EMP.data.Field.prototype._checkAll = function () {
		return this._obj.doCheckAll();
	};
	
	/**
	 * 判断对象是本类实例的标志
	 * 由于JS是弱类型语言，因此无法像Java那样通过instanceof来判断
	 */
	EMP.data.Field.prototype._isField = true;
	
};

/**
 * EMP.data.KColl
 * 一组数据的封装类。
 * 它没有保存引用，而是以属性的形式包含了多个数据域对象，
 * 并提供了将这些数据域的值一起复制到表单的方法。
 *
 * 例如要访问名为user的KColl下的名为name的数据域，
 * 则直接可以用user.name去找到对应的数据域对象。
 * （注意，user.name不是编辑域，要使用编辑域的方法，需要通过_obj引用）
 * 所有方法采用下划线开头，是因为尽量避免与KColl下的数据域发生命名冲突。
 * Field和IColl虽然没有冲突可能，但也需要与KColl保持一致供内部调用。
 */
if (!EMP.data.KColl) {

	/**
	 * 构造方法，空实现
	 */
	EMP.data.KColl = function() {};

	/**
	 * 将KColl下的所有数据对象的取值复制到指定表单
	 * @param form 指定表单DOM对象
	 */
	EMP.data.KColl.prototype._toForm = function (form, prefix) {
		if (form == null) return;
		for(var i in this) {
			if (this[i]._isField || this[i]._isKColl || this[i]._isIColl) {
				if (this[i]._toForm) {
					this[i]._toForm(form, prefix);
				}			
			}		
		}
	};
		
	/**
	 * 对KColl下的所有数据对象进行校验
	 */
	EMP.data.KColl.prototype._checkAll = function () {
		var res = true;
		var flag = true;
		for(var i in this) {
			if (this[i]._isField || this[i]._isKColl || this[i]._isIColl) {
				if(!this[i]._checkAll()) {
				    if(flag) {
                        //如果没有tab页就跳过不管
                        try {
                            var id = this[i]._obj.id;
                            var divObj = document.getElementById(id).parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
                            var divObjId = divObj.id;
                            var tabsObjId = divObj.parentNode.id;
                            var lengthAfter = tabsObjId.length - "relatedtabs_".length;
                            var tabsId = tabsObjId.substring("relatedtabs_".length).substring(0, lengthAfter - "_main".length);
                            var tabId = divObjId.substring(0, divObjId.length - "_tab_div".length);
                            eval(tabsId + ".tabs." + tabId + "_tab._clickLink();");
                        } catch(e) {
                        }
                        try {
                            //当前页面有错的字段进行focus操作
                            document.getElementById(id).parentNode.focus();
                            //最后成功执行时，标记置为false
                            flag = false;
                        } catch(e) {
                        }
                    }
					res = false;
				}
			}		
		}
		return res;
	};
	
	
	/**
	 * 对KColl中同名的数据进行更新
	 */
	EMP.data.KColl.prototype._putKColl = function (kColl) {
		for(var i in this){
			if (this[i]._isField) {
				var obj = eval("kColl."+i);
				if(obj){
					this[i]._setValue(obj._getValue());
				}
			}
		}
	};
	
	/**
	 * 判断对象是本类实例的标志
	 */
	EMP.data.KColl.prototype._isKColl = true;
};

/**
 * EMP.data.IColl
 * 多条数据记录的封装类。
 * 它保存了对页面某个数据列表的引用，
 * 并继承了JS的Array类，其中保存了多个KColl实例，
 * 可以通过iColl[0]的方式取得。
 * 若要取得其中的数据域，可以用iColl[0].field的方式。
 */
if (!EMP.data.IColl) {

	/**
	 * 构造方法
	 * @param obj 所引用的编辑域对象
	 */
	EMP.data.IColl = function(obj) {
		this._obj = obj;
		if (EMP.util.Tools.Browser.ie) this.size = 0;
		obj.data = this;	//将自身设为数据列表的data属性
	};

	/**
	 * 继承自Array类，非ie浏览器下有效
	 */
	EMP.data.IColl.prototype = new Array();
	
	/**
	 * IE无法继承基本类型Array，因此重写push和splice方法，并改用_getSize方法取得数组长度
	 */
	if (EMP.util.Tools.Browser.ie) {
		EMP.data.IColl.prototype.push = function (obj) {
			this[this.size] = obj;
			this.size++;
		};
		
		EMP.data.IColl.prototype.splice = function (idx) {	//和正规的splice不同，未实现第二个参数
			if (idx>=0 && idx<this.size) {
				for (var i=idx; i<this.size-2;i++) {
					this[i] = this[i+1];
				}
				delete this[this.size-1];
				this.size--;
			}
		};
	};

	EMP.data.IColl.prototype._getSize = function () {
		if (EMP.util.Tools.Browser.ie) {
			return this.size;
		} else {
			return this.length;
		};
	};
	
	/**
	 * 将IColl下的多条数据记录的取值复制到指定表单
	 */
	EMP.data.IColl.prototype._toForm = function (form, prefix) {

		var tableName = this._obj.dataName;
		var div = EMP.util.Tools.getElementById(form,"toform_icoll_"+tableName,"div");
		if (div!=null) {
			div.innerHTML = "";				
		} else {
			div = document.createElement("div");
			form.appendChild(div);
			div.id = "toform_icoll_"+tableName;
		}
		for (var i=0;i<this._getSize();i++) {
			var p = prefix;
			if (p!=null)
				p = p + tableName+"["+i+"].";
			else
				p = tableName+"["+i+"].";
			this[i]._toForm(form,p);
		}
	};
		
	/**
	 * 对IColl下的多条数据记录的值进行校验
	 */
	EMP.data.IColl.prototype._checkAll = function () {
		var res = true;
		for (var i=0;i<this._getSize();i++) {
			if(!this[i]._isKColl)
				continue;
			if(!this[i]._checkAll())
				res = false;
		}
		return res;
	};
		
	/**
	 * 对IColl数据集中增加一条kColl数据
	 */
	EMP.data.IColl.prototype._addKColl = function (kColl) {
		this._obj._addRow();
		this._obj.recordCount = this._getSize();
		
		for (var i=0;i<this._obj.columnNames.length;i++) {
		
			var columnName = this._obj.columnNames[i];
			try{
				var field = eval("kColl."+columnName);
			}catch(e){
				continue;
			}
			if(field == null || !field._isField)
				continue;
			var value = field._getValue();
			try {
				if (value!=null) {
					var obj = eval("this[(this._getSize()-1)]."+columnName);
					if (obj) {
						obj._setValue(value);
					}
				}
			} catch(e) {alert(e.message)}
		}
		this._obj.setMessage(null);	
	};
	
	/**
	 * 判断对象是本类实例的标志
	 */
	EMP.data.IColl.prototype._isIColl = true;
};


/**
 * EMP.field.Base
 * 编辑域的基类，所有编辑域均由此类继承而来。
 * Base类实现了普通的文本显示（没有编辑功能），常用于数据列表中。
 *
 * 它解析并保存注册标签的各属性，并向注册标签内的HTML对象添加各种样式和事件，
 * 实现各种特殊的编辑需要。
 */
if (!EMP.field.Base) {

	/**
	 * 构造方法，空实现
	 */
	EMP.field.Base = function() {};
	
	/*
	 * 内部方法，在page对象对整个页面进行解析时自动调用，
	 * 解析并保存注册标签的各属性，并调用_initialize方法。
	 * 此方法不需要重载。
	 * @param tag 注册标签对象<SPAN>
	 */
	EMP.field.Base.prototype._parseParams = function(tag) {	
		
		this.tag = tag;		//注册标签对象(SPAN)
		this.data = null;	//数据域对象(EMP.data.Field)
		this.dataName = null;//注册对象的名称
		
		this.id = this.tag.getAttribute("id");	//标识符
		this.title = this.tag.getAttribute("title");		//中文名
		this.value = this.tag.getAttribute("value");	//取值
		this.initValue = this.value;	//初始取值
		this.checkMessage = null;//校验的结果(包括基本的检验和业务层次的检验)
		
//		this.container = null;	//包含该编辑域的容器，用于扩展
		this.table = null;
		
		//设置各种属性默认值
		this.config = {
			required : false,		//是否必填
			hidden : false,			//是否隐藏
			readonly : false,		//是否只读
			disabled : false,		//是否无效
			datatype : null		//数据类型
		};
		//解析各个指定的属性
		this._parseAttribute("cssErrorClass");
		this._parseAttribute("cssRequiredClass");
		this._parseAttribute("required");
		this._parseAttribute("hidden");
		this._parseAttribute("colSpan");
		this._parseAttribute("disabled");
		this._parseAttribute("readonly");
		this._parseAttribute("onlyControlElement");
		this._parseAttribute("dictname");
		this._parseAttribute("statistic");
		this._parseAttribute("fieldErrorValue");
		this._parseAttribute("dataType");
		this._parseAttribute("validateJS");
		this._parseAttribute("convertorJS");
		this._parseAttribute("rangeErrorMsg");
		this._parseAttribute("formatErrorMsg");
		this._parseAttribute("rendered");
		
		//具体各个标签的初始化过程
		this._initialize();		
		
		this.setValue(this.getValue());
		
		//如果存在着服务器端校验失败的信息，则显示该信息
		if(this.config.fielderrorvalue){
			this.reportError(this.config.fielderrorvalue);
		}
		
		//如果设置了dataType，则缺省给SPAN标签设置emp_datatype_+dataType的样式
		if (this.config.datatype != null) {
			EMPTools.addClass(this.tag,"emp_datatype_"+this.config.datatype);
		}
		
		//处理标签的相关显示状态
		this._renderStatus();
	};
	
	
	/**
	 * 解析指定的属性
	 * @param attrName 指定的属性名称
	 */
	EMP.field.Base.prototype._parseAttribute = function(attrName) {
		var attrValue = this.tag.getAttribute(attrName);
		if(attrValue != null && attrValue != ""){
			if(attrValue.toLowerCase && attrValue.toLowerCase() == "true"){
				this.config[attrName.toLowerCase()] = true;
			}else if(attrValue.toLowerCase && attrValue.toLowerCase() == "false"){
				this.config[attrName.toLowerCase()] = false;
			}else{
				this.config[attrName.toLowerCase()] = attrValue;
			}
		}
	};
	
	/**
	 * 获得注册标签内的HTML元素，并向其添加各种样式和事件。
	 * 重载该方法实现自定义编辑域的HTML元素初始化处理。
	 */
	EMP.field.Base.prototype._initialize = function() {
		this.element = this.tag.getElementsByTagName("SPAN")[0];
	};
	
	/**
	 * 以下几个方法处理组件显示、读写状态。
	 * 重载它们实现自定义编辑域的状态处理功能。
	 */
	EMP.field.Base.prototype._renderStatus = function() {
		this._renderHidden(this.config.hidden);
		this._renderReadonly(this.config.readonly);
		this._renderDisabled(this.config.disabled);
		this._renderRequired(this.config.required);
	};
	
	EMP.field.Base.prototype._renderHidden = function(hidden) {
		
		this.config.hidden = hidden;
		
		if (hidden) {
			EMPTools.addClass(this.tag, 'emp_field_hidden');
		} else {
			EMPTools.removeClass(this.tag, 'emp_field_hidden');
		}
		if (this.container && this.container._renderHidden)
			this.container._renderHidden(hidden);
	};
	
	EMP.field.Base.prototype._renderReadonly = function(readonly) {
		
		this.config.readonly = readonly;
		
		if (readonly) {
			EMPTools.addClass(this.tag, 'emp_field_readonly');
		} else {
			EMPTools.removeClass(this.tag, 'emp_field_readonly');
		}
		if (this.container && this.container._renderReadonly)
			this.container._renderReadonly(readonly);
	};
		
	EMP.field.Base.prototype._renderDisabled = function(disabled) {
		
		this.config.disabled = disabled;
		
		if (disabled) {
			EMPTools.addClass(this.tag, 'emp_field_disabled');
		} else {
			EMPTools.removeClass(this.tag, 'emp_field_disabled');
		}
		if (this.container && this.container._renderDisabled)
			this.container._renderDisabled(disabled);
	};
	
	EMP.field.Base.prototype._renderRequired = function(required) {
		
		this.config.required = required;
		
		if(required){
			if(!this.requiredSpan){
				this.requiredSpan = document.createElement("SPAN");
				var text = document.createTextNode("*");
				this.requiredSpan.appendChild(text);
				
				//如果已存在着错误信息SPAN，则将必输显示的SPAN放在前面
				if(this.errorSpan){
					this.tag.insertBefore(this.requiredSpan, this.errorSpan);
				}else{
					this.tag.appendChild(this.requiredSpan);
				}
				
				if(this.config.cssrequiredclass){
					EMPTools.addClass(this.requiredSpan, this.config.cssrequiredclass);
				}
				
			}else{
				this.requiredSpan.style.display = "";
			}
		}else{
			if(this.requiredSpan){
				this.requiredSpan.style.display = "none";
			}
		}
	};
	
	/**
	 * 设置编辑域真实值。
	 * 重载该方法实现自定义编辑域的真实值设置及更新页面显示功能。
	 * @param value 待设置的取值
	 */
	EMP.field.Base.prototype.setValue = function(value) {
		this.value = value;
		this.tag.setAttribute("value",this.value);	//设值
		var displayValue = value;
		
		if (displayValue == null) 
			displayValue = "";
		else
			if (this.config.dictname != null) {	//若有字典修饰，则从page.dataDics中取得字典数据进行显示转换
				var dict = page.dataDics[this.config.dictname];
				if (dict) {
					displayValue = dict[value];
					if(!displayValue){//若字典数据中没有此值、则直接返回真实值
						value = EMP.util.Tools.trim(value);
						displayValue = dict[value];
						if(!displayValue)
							displayValue = value;
					}
				}
			} else if (this.config.datatype) {	//若有数据类型，则进行真实值->显示值的转换
				displayValue = EMP.type.Base.display(this.value, this.config.convertorjs);
			}
		
		//更新页面显示
		EMP.util.Tools.setInnerText(this.element,displayValue);
	};

	/**
	 * 获得编辑域真实值。
	 * 重载该方法实现自定义编辑域的真实值获取功能。
	 * @return 编辑域真实值
	 */
	EMP.field.Base.prototype.getValue = function() {
		return this.value;
	};
	
	/**
	 * 将该数据域的取值复制到指定表单
	 * @param form 指定表单DOM对象
	 */
	EMP.field.Base.prototype.toForm = function(form, prefix) {
		if (form == null) 
			return;
		var name = this.dataName;
		if (prefix!=null) 
			name = prefix + name;
		
		var value = this.getValue();
		
		var input = form[name];
		
		//若form中没有同名的input，则新建一个隐藏的input放在form中
		if(input == null){
			input = document.createElement("input");
			input.type="hidden";
			input.name = name;
			form.appendChild(input);	
			form[name] = input;//强制在form对象中将新建的隐藏域放入
		}
		if(value != null){
			input.value = value;
		}else{
			input.value = "";
			//form.removeChild(input);
			//form[name] = null;
		}
		
		EMP.util.Tools.log('EMP.data.Field',0
				,'数据域['+name+']值['+value+']被复制到表单['+form.id+'].');
	};
	
	
	/**
	 * 检查输入数据的合法性(包括了长度校验、数据类型校验以及业务层次的校验)
	 @return true/false 输入数据是否通过校验
	 */
	EMP.field.Base.prototype.doCheckAll = function () {
		
		var retMsg = this.doCheckLength();
		if(!retMsg || retMsg.result == true){
			retMsg = this.doCheckDataType();
		}
		if(retMsg && retMsg.result == false){
			var errorMsg = retMsg.message;
			this.reportError(errorMsg);
			return false;
		}
		this.clearError();
		
		//判断是否已经完成过业务层次的校验(若有完成且校验的结果是不需要校验，则直接返回)
		if(this.checkMessage && this.checkMessage.control == 2){
			return true;
		}
		//进行业务层次的校验
		var ret = this.doCheckBussiness();
		if(ret == false && this.checkMessage.control == 1){//业务校验不通过，但可以忽略的情况
			return true;
		}
		if(ret == false && this.checkMessage.control == 0){//业务校验不通过，但不可以忽略的情况
			return false;
		}
		return true;
		
	};
	
	/**
	 * 检查输入数据的长度，基类为空实现。
	 * 重载该方法实现自定义编辑域的数据类型检查功能。
	 * @return EMPTools.message
	 */
	EMP.field.Base.prototype.doCheckLength = function() {
		return EMPTools.message(true);	
	};
	
	/**
	 * 检查数据类型，基类为空实现。
	 * 重载该方法实现自定义编辑域的数据类型检查功能。
	 * @return EMPTools.message
	 */
	EMP.field.Base.prototype.doCheckDataType = function() {
		return EMPTools.message(true);
	};
		
	/**
	 * 进行业务层次的校验
	 * @return true/false
	 */
	EMP.field.Base.prototype.doCheckBussiness = function() {
		//在页面中是否定义了业务层次的校验(可提供了接口，具体实现由业务人员在页面上进行定义)
		if(window.pageBussinessCheck){
			retMsg = window.pageBussinessCheck(this);
			this.checkMessage = retMsg;
			if(retMsg.result == false){
				var errorMsg = retMsg.message;
				if(retMsg.level == 0){
					this.reportError(errorMsg);
				}else {
					this.checkNext = false;
					alert(errorMsg);
				}
				return false;
			}else{
				this.clearError();
			}
		}
		return true;
	};
	
	/**
	 * 为该域设置错误信息，默认方法是在HTML元素后面添加一个用于显示错误信息的SPAN。
	 * 该方法一般不需要被重载（因为各编辑域的错误域显示方式应该是统一的），
	 * 若要实现别的错误信息显示方式，可以重新为Base类定义该方法，覆盖掉此方法
	 * @param msg 要显示的错误信息
	 */
	EMP.field.Base.prototype.reportError = function(msg) {
		this.checkNext = true;
		if (msg!=null) {
			if (!this.errorSpan) {
				this.errorSpan = document.createElement("SPAN");
				this.tag.appendChild(this.errorSpan);
				if(this.config.csserrorclass){
					EMPTools.addClass(this.errorSpan, this.config.csserrorclass);
				}
			}
			EMP.util.Tools.setInnerText(this.errorSpan,msg);
		}
	};	
	
	/**
	 * 发生错误后是否继续检查后面的域，这个值通常与reportError方法的具体实现有关。
	 * 默认的实现方法是在域后面添加错误信息，因为可以同时显示多个域的错误信息，所以返回true。
	 * 若改变为alert弹出框进行提示的方式，则最好重新定义此值为false，
	 * 避免多个域出错时不断弹出对话框。
	 */
	EMP.field.Base.checkNext = true;

	/**
	 * 清除该域的错误信息。
	 * 该方法一般不需要被重载（因为各编辑域的错误域显示方式应该是统一的），
	 * 若要实现别的错误信息显示方式，可以重新为Base类定义该方法，覆盖掉此方法
	 */
	EMP.field.Base.prototype.clearError = function() {
		if (this.errorSpan) {
			this.errorSpan.innerHTML = "";
		}
	};
	
	/**
	 * 将焦点置于该编辑域，基类为空实现
	 */
	EMP.field.Base.prototype.focus = function() {
	};
	
	/**
	 * 将该编辑域的取值恢复初始值
	 */
	EMP.field.Base.prototype.reset = function() {
		this.setValue(this.initValue);
	};
	
};


/**
 * EMP.widget.DataGroup
 * 数据分组的实现类，它维护了一组编辑域（还可能包含数据列表），
 * 可对它们进行统一的数据检查和复制到表单等操作。
 *
 * 默认将页面所有编辑域和数据列表归入名为_default的数据分组。
 * 若要定义自己的数据分组，可以用一个DIV将待分组的编辑域、数据列表包含起来（要符合HTML结构）
 * 并将其的class属性设为emp_group_div，这样在解析页面时就会生成一个名为该DIV的id的数据分组。 
 * 也可以通过JS脚本手工生成DataGroup实例，并向其中添加编辑域。
 * 引用一个数据分组，需要使用page.dataGroups._default的形式。
 *
 * 数据分组和KColl比较相似，但不能将数据分组作为KColl的引用，
 * 原因是KColl反映后台数据结构，而数据分组里面的编辑域和数据结构无关，完全是线性的，
 * 只能进行批量操作，并且不能通过名称去访问某个特定编辑域。
 */
if (!EMP.widget.DataGroup) {

	/**
	 * 构造方法
	 * @param tag 定义数据分组的DIV（注册标签）
	 */
	EMP.widget.DataGroup = function(tag) {
		if (tag != null) {
			this.tag = tag;
			this.id = tag.id;
		}
		this.fields = new Array();	//用于存放其中编辑域和数据列表的数组
	};

	/**
	 * 为数据分组添加一个编辑域
	 * @param obj 待添加的编辑域
	 */
	EMP.widget.DataGroup.prototype.push = function(obj) {
		this.fields.push(obj);
	};
	
	/**
	 * 批量检查输入数据的长度，一般供内部调用
	 * @return 若通过返回true，否则返回false
	 */
	EMP.widget.DataGroup.prototype._checkLength = function() {
		var res = true;
		for (var i in this.fields) {
			var field = this.fields[i];
			if(!field || typeof(field) != 'object'){
				continue;
			}
			if (!field.data || !field.data._isField) 
				continue;
			var retMsg = field.doCheckLength();
			if(retMsg && retMsg.result == false){
				res = false;//当前存在着校验不通过的情况
				var errorMsg = retMsg.message;
				field.reportError(errorMsg);
				if (!field.checkNext) 
					return false;
			}
		}
		return res;
	};
	
	/**
	 * 批量检查数据类型，一般供内部调用
	 * @return 若通过返回true，否则返回false
	 */
	EMP.widget.DataGroup.prototype._checkDataType = function() {
		var res = true;
		for (var i in this.fields) {
			var field = this.fields[i];
			if(!field || typeof(field) != 'object'){
				continue;
			}
			if (!field.data || !field.data._isField) 
				continue;
			var retMsg = field.doCheckDataType();
			if(retMsg && retMsg.result == false){
				res = false;//当前存在着校验不通过的情况
				var errorMsg = retMsg.message;
				field.reportError(errorMsg);
				if (!field.checkNext) 
					return false;
			}
		}
		return res;
	};
	
/*	EMP.widget.DataGroup.prototype._checkDataLength = function(){
	    for (var i in this.fields){
	        if(!this.fields[i].doCheckDataLength())
	           return false;
	    }
	    return true;
	};*/
	
	/**
	 * 批量检查分组中所有编辑域的必填及数据类型。
	 * 在复制到表单并提交前请先执行此方法。
	 * 目前没有对数据列表进行处理，待实现。
	 * @return 若通过返回true，否则返回false
	 */
	EMP.widget.DataGroup.prototype.checkAll = function() {
		
		var res = true;
		for (var i in this.fields) {
			var field = this.fields[i];
			if(!field || typeof(field) != 'object'){
				continue;
			}
			if (!field.data || !field.data._isField) 
				continue;
			
			var checkResult = field.doCheckAll();
			if(!checkResult){
				res = false;
				if(!field.checkNext)
					return false;
			}
		}
		return res;
	};
	
	/**
	 * 将分组中的所有编辑域（和数据列表）的值复制到指定表单。
	 * @param form 指定表单对象
	 * @param containIColl 是否复制数据列表
	 */
	EMP.widget.DataGroup.prototype.toForm = function(form, containIColl) {
	
		if (form == null)
			return;
		for (var i in this.fields) {
			var field = this.fields[i];
			if(!field || typeof(field) != 'object'){
				continue;
			}
			if(!field.data)
				continue;
			if (!containIColl && field.data._isIColl)
				continue;
			field.data._toForm(form);
		}	
	};

	/**
	 * 将分组中的所有编辑域的值重置。
	 */	
	EMP.widget.DataGroup.prototype.reset = function() {
		
		for (var i in this.fields) {
			var field = this.fields[i];
			if(!field || typeof(field) != 'object'){
				continue;
			}
			if (!field.data || !field.data._isField) 
				continue;
			field.reset();			
		}	
	}
};


/**
 * EMP.widget.DataTable
 * 数据列表的实现类，它以一个表格的形式展现了多条记录数据，
 * 并可以选择其中的若干行进行进一步操作。
 * 此外还可以通过异步请求从后台获取JSON格式的定义串对表格进行动态刷新（多用于分页查询或条件查询）
 */

if (!EMP.widget.DataTable) {

	/**
	 * 构造方法，空实现
	 */
	EMP.widget.DataTable = function () {};
	

	/*
	 * 内部方法，在page对象对整个页面进行解析时自动调用，
	 * 解析并保存注册标签的各属性，并调用_initialize方法。
	 * @param tag 注册标签对象<TABLE>
	 */
	EMP.widget.DataTable.prototype._parseParams = function (tag) {
	
		this.tag = tag;		//注册标签对象(TABLE)
		this.data = null;	//对应的IColl
		this.dataName = null;//注册对象的名称
		
		this.tableType = tag.getAttribute("tableType");//表格类型 g为分组型表格
		
		this.nodata = tag.getAttribute("nodata");	//此标志代表后台不存在所要的数据集合
		this.selectType = tag.getAttribute("selectType");	//选择类型，0代表不支持选择，1代表单选，2代表多选，其它选择方式待实现
		this.statisticType = tag.getAttribute("statisticType");		//统计类型，相关代码暂时屏蔽，待整理
		this.needTableTitle = tag.getAttribute("needTableTitle")=="true"?true:false;	//是否显示表头
		this.pageMode = tag.getAttribute("pageMode")=="true"?true:false;	//是否分页查询
		this.url = tag.getAttribute("url");	//分页查询的默认URL
		if(this.selectType == 2){//如果可以多选，则缺省当前是多选状态
			this.multiSelect = true;
		}
		
		this.columnNames = [];	//各字段的名称
		this.recordCount = 0;	//记录数
		this.selectedRows = [];	//各行选中标志(true or false)
		
		this._initialize();
	};	
	
	/**
	 * 获得注册标签中的各HTML元素，对其添加各种样式和事件
	 * 
	 * 由后台生成的TABLE中共有5个TBODY，功能分别如下：
	 * [0]tBodySample	数据域模板，在需要动态添加行的时候，从此模板复制
	 * [1]tBodyMain		列表数据，即表格的主要部分
	 * [2]tBodyTotal	统计栏，用于显示各字段的合计或平均值
	 * [3]tBodyMsg		提示信息，用于显示查询的提示信息等
	 * [4]tBodyPq		分页查询功能栏，用于显示分页查询的各功能元素
	 * 一开始各部分都是隐藏的，会根据需要显示其中的某些部分。
	 */	 
	EMP.widget.DataTable.prototype._initialize = function() {
			
		this.tHead = this.tag.tHead;				//表头
		this.tBodySample = this.tag.tBodies[0];		//数据域模板
		this.tBodyMain = this.tag.tBodies[1];		//列表数据
		this.tBodyTotal = this.tag.tBodies[2];		//统计栏
		this.tBodyMsg = this.tag.tBodies[3];		//提示信息
		this.tBodyPq = this.tag.tBodies[4];			//分页查询功能栏
		this.msgSpan = this.tBodyMsg.getElementsByTagName("SPAN")[0];	//提示信息的SPAN
		
		this.recordCount = this.tBodyMain.rows.length;
		
		this._addCss();	//添加隔行样式
		if (this.needTableTitle) 
			this.tHead.style.display="";	//需要显示表头
		
		if (this.pageMode) {	//分页模式，添加分页处理对象，显示分页查询功能栏
			this.pageQuery = new EMP.widget.PageQuery(this);
			this.tBodyPq.style.display="";
		}
		
		if (this.nodata) {	//若无数据集合，则显示相应信息（通常为第一次进入查询列表页面的情况）
			this._noData();
		}
		
		if (this.recordCount == 0) {	//若数据集合无记录，则显示相应信息
			this._noRecords();
		} else {
			this.tBodyMain.style.display="";	//否则显示列表数据
		}
		
		//此处待添加统计的实现
		
		if (this.selectType!=0) {	//若支持选择，则添加相应事件
			this._addSelectEvents();
		}
		
		if(this.tableType){
			
		}
		
	};
	
	/**
	 * 内部方法，当无数据集合时的处理
	 */
	EMP.widget.DataTable.prototype._noData = function() {
		this.tBodyMain.style.display="none";
		this.tBodyTotal.style.display="none";
		this.setMessage("请输入查询条件");
	};
	
	/**
	 * 内部方法，当数据集合无记录时的处理
	 */
	EMP.widget.DataTable.prototype._noRecords = function() {
		this.tBodyMain.style.display="none";
		this.tBodyTotal.style.display="none";
		this.setMessage("无符合条件的记录！");
	};
	
	/**
	 * 设置表格的提示讯息
	 * @param msg 提示讯息
	 */
	EMP.widget.DataTable.prototype.setMessage = function(msg) {

		if (msg==null) {
			this.tBodyMsg.style.display="none";
		} else {
			EMP.util.Tools.setInnerText(this.msgSpan,msg);
			this.tBodyMsg.style.display="";
		}
	};
	
	/**
	 * 内部方法，从THEAD上取得各字段的名称
	 */
	EMP.widget.DataTable.prototype._getColumnNames = function() {
		
		var tHead = this.tag.tHead.rows[0];
		for (var i=0;i<tHead.cells.length;i++) {
			var columnName = tHead.cells[i].getAttribute("columnName");
			this.columnNames[i] = columnName;
		}
	};
	
	/**
	 * 内部方法，查找TABLE中的所有编辑域注册标签，
	 * 进行实例化，并将数据添加到对应的iColl中
	 */
	EMP.widget.DataTable.prototype._regist = function() {
	
		this._getColumnNames();
		 
		for (var i=0; i<this.recordCount; i++)	{
		
			EMP.util.Tools.log('EMP.widget.DataTable',0
					,'开始处理数据列表['+this.dataName+']第['+i+']行...');
					
			var row = this.tBodyMain.rows[i];
			//this.iColl[i] = this._registRow(row);
			this.data.push(this._registRow(row));
		}
	 	
	    for(var r=0; r<this.recordCount; r++){
	      	for(var c=0; c<this.columnNames.length; c++){
	      	  try{
	          	var _column = this.columnNames[c];
	          	var _dataType = this.data[r][_column]._obj.config.datatype;
	      		if(_dataType == 'Currency' || _dataType == 'Rate' || _dataType == 'Rate4Month' || 
	      				_dataType == 'Percent' || _dataType == 'Percent2' || _dataType == 'Permille' ||
	      					_dataType == 'Int' || _dataType == 'Long' || _dataType == 'Float' || 
	      						_dataType == 'Double' || _dataType == 'Decimal'){
	      			this.data[r][_column]._obj.element.parentElement.parentElement.style.textAlign='right';
	      		}
	      	   }catch(e){
	      	   }
	      	}
	    }
	};
	
	/**
	 * 内部方法，查找指定TR内的所有编辑域，封装为kColl
	 * @param row 指定TR
	 * @return 封装后的kColl
	 */
	EMP.widget.DataTable.prototype._registRow = function(row) {
		var _tbodymain_str = row.parentNode.id.split('_');
		var _this_tablename = _tbodymain_str[2];
		//数据类型数组
		var _types = new Array();
		var kColl = new EMP.data.KColl();	
		for (var j=0; j<row.cells.length; j++){		
			var tag = row.cells[j].getElementsByTagName("span")[0];		//one cell one field
			var dataTypeName = tag.getAttribute("dataType");
			if(dataTypeName == 'Currency'){
				dataTypeName = 'number';
			}else{
				dataTypeName = 'string';
			}
			_types[j] = dataTypeName;
			if (tag!=null && tag.getAttribute("type") != null && tag.getAttribute("rendered")=="false") {
				var obj = EMP.util.Page.instField(tag, true);
				if (obj != null) {
					obj.table = this;
					var dataName = obj.dataName;
					EMP.util.Tools.log('EMP.widget.DataTable',0,'在第['+j+']列找到数据域['+dataName+']的注册标签，进行实例化...');
					page.addData(kColl,"dataField",dataName,obj);
				} else {
				}
			} else {
			}
		}
		/** 添加每个DataTable和对应数据类型列表到Map中(clear:20130927) */
		if(!EMPDataTableMap.containsKey(_this_tablename)){
			EMPDataTableMap.put(_this_tablename,_types);
		}
		return kColl;
	};
	
	/**
	 * 为列表数据的TBODY添加隔行不同颜色的CSS样式
	 * @param start 从第几行开始添加
	 */
	EMP.widget.DataTable.prototype._addCss = function(start) {
		if (start==null) start = 0;
		for (var i=start;i<this.tBodyMain.rows.length;i++) {
			EMP.util.Tools.removeClass(this.tBodyMain.rows[i],"row1");
			EMP.util.Tools.removeClass(this.tBodyMain.rows[i],"row2");
			var dtype=this.tBodyMain.rows[i].dtype;
			if(dtype != '1'){
			   EMP.util.Tools.addClass(this.tBodyMain.rows[i],"row"+(i%2+1));
			}
		}
	};

	/**
	 * 为列表数据的TBODY添加点击（选择）事件
	 */
	EMP.widget.DataTable.prototype._addSelectEvents = function() {
		EMP.util.Tools.addEvent (this.tBodyMain, "click", this.click, this);
		EMP.util.Tools.addEvent (this.tBodyMain, "dblclick", this.dblclick, this);
	};
	
	/**
	 * 双击一条记录事件,展示页面双击就查看本条记录，选择返回页面就选择返回
	 * @param e 事件对象
	 */
	EMP.widget.DataTable.prototype.dblclick = function(e) {
		var funName = this.tag.getAttribute("funName");
		try{
			eval(funName)();
		}catch(err){
			try{
				doSelect();
			}catch(err){}	
		}	
	};	
	
	/**
	 * 点击一条记录事件
	 * @param e 事件对象
	 */
	EMP.widget.DataTable.prototype.click = function(e) {

		var rowIndex = -1;
		if (EMP.util.Tools.Browser.ie) {
			if(e.srcElement.tagName == 'TD'){
				rowIndex = e.srcElement.parentNode.sectionRowIndex;
			} else if(e.srcElement.tagName == 'SPAN'){
				if(e.srcElement.parentNode.parentNode.tagName == 'TD'){
					rowIndex = e.srcElement.parentNode.parentNode.parentNode.sectionRowIndex;
				} else if(e.srcElement.parentNode.parentNode.tagName == 'TR'){
					rowIndex = e.srcElement.parentNode.parentNode.sectionRowIndex;
				} else
					return;
			} else
				return;
		} else {
			if (e.target.tagName == 'TD') {
				rowIndex = e.target.parentNode.sectionRowIndex;
			} else if(e.target.tagName == 'SPAN'){
				if(e.target.parentNode.parentNode.tagName == 'TD'){
					rowIndex = e.target.parentNode.parentNode.parentNode.sectionRowIndex;
				} else if(e.target.parentNode.parentNode.tagName == 'TR'){
					rowIndex = e.target.parentNode.parentNode.sectionRowIndex;
				} else
					return;
			}
			else 
				return;
		}
		
		EMP.util.Tools.log('EMP.widget.DataTable',0
				,'进行了点击第['+rowIndex+']行的操作.');
		
		//若按下了ctrl键，则为多选操作
	/*	var clear = false;
		try{
			if (!e) e=event;	//for IE;
		}catch(err){};
		if (e && !e.ctrlKey)
			clear = true;*/
		
		//如果存在单、复选，则选中一行中的任何地方与选中单、复选框是一致的
		this.select(rowIndex, false);
	};
	
	/**
	 * 选择一条记录
	 * @param i 记录编号
	 * @param clear 是否清除当前选择
	 */
	EMP.widget.DataTable.prototype.select = function(i,clear){	
		
		//如果不允许多选或未打开多选模式，则强制清除当前选择
		if (!(this.selectType == 2 && this.multiSelect))
			clear = true;
			
		if (clear)
			this.clearAll();

		//选择状态改变，清除全选框
	//	if (this.selectAllEl)
	//		this.selectAllEl.checked = false;
			
		//设置选择标志，改变样式
		var row = this.tBodyMain.rows[i];
		
		if (this.selectedRows[i]) {
			this.selectedRows[i] = false;
			EMP.util.Tools.removeClass(row,"selected");
		} else {
			this.selectedRows[i] = true;
			EMP.util.Tools.addClass(row,"selected");
		}		
		
		if (this.onSelect)	//若定义了onSelect属性（自定义事件），则执行
			this.onSelect(i);

	};
	
	/**
	 * 清除所有选择
	 */
	EMP.widget.DataTable.prototype.clearAll = function() {
		for (var i=0;i<this.recordCount;i++) {
			if (this.selectedRows[i]) {
				this.selectedRows[i] = false;
				EMP.util.Tools.removeClass(this.tBodyMain.rows[i],"selected");
			}
		}
	};
	
	/**
	 * 选中所有记录
	 */
	EMP.widget.DataTable.prototype.selectAll = function() {
		for (var i=0;i<this.recordCount;i++) {
			if (!this.selectedRows[i]) {
				this.selectedRows[i] = true;
				EMP.util.Tools.addClass(this.tBodyMain.rows[i],"selected");
			}
		}
	};
	
	/**
	 * 点击全选事件
	 *//*
	EMP.widget.DataTable.prototype.selectAllClick = function(){
		if (this.selectAllEl.checked)
			this.selectAll();
		else
			this.clearAll();
	};*/

	/**
	 * API方法，以数组形式获得当前选中记录的行号
	 */
	EMP.widget.DataTable.prototype.getSelectedIdx = function() {
		var idx = new Array();
		for (var i=0;i<this.recordCount;i++){
			if (this.selectedRows[i]) {
				idx.push(i);
			}
		}
		return idx;
	};
	
	/**
	 * API方法，以数组形式获得当前选中记录的取值集合(只包含取值，而不是数据对象)
	 *	
	 */
	EMP.widget.DataTable.prototype.getDataValue = function() {
		var datas = new Array();
		for (var i=0; i<this.recordCount; i++){
			if (this.selectedRows[i]) {
				var recordData = {};
				for (var j in this.data[i]) {
					var dataName = this.data[i][j].dataName;
					recordData[dataName]=this.records[i][j]._getValue();
				}
				
				datas.push(recordData);
			}
		}
		return datas;
	};
	
	
	/**
	 * 以数组形式获得当前选中记录的kColl集合
	 */
	EMP.widget.DataTable.prototype.getSelectedData = function() {
		var datas = new Array();
		for (var i=0;i<this.recordCount;i++){
			if (this.selectedRows[i]) {
				datas.push(this.data[i]);
			}
		}
		return datas;
	};

	/**
	 * 获取当前选中记录的某些字段的GET请求串
	 * @param keys 要获取的多个字段名，数组形式
	 * @return GET请求串，若没有选中数据，则返回null
	 */
	EMP.widget.DataTable.prototype.getParamStr = function(keys,prefix){
		
		var idx = this.getSelectedIdx();
		if (idx.length==0) return null;
		var paramIdx = 0;
				
		var sb = "";
		for (var i=0;i<idx.length;i++) {
			for (var k=0;k<keys.length;k++) {
				sb += "&";
				if(this.selectType == 2 && this.dataName != null && this.dataName != ""){
					sb += this.dataName+"["+paramIdx+"].";
					paramIdx ++;
				}
				var value = this.data[idx[i]][keys[k]]._getValue();
				if(prefix)
					sb += prefix+"."+keys[k]+"="+value;
				else
					sb += keys[k]+"="+value;
			}
		}

		return sb.substring(1);
	};
	
	/**
	 * 获取当前选中记录的某个字段的值 add by huangxg 2009.05.08
	 * @param key 字段名 
	 */
	EMP.widget.DataTable.prototype.getParamValue = function(key){
		var idx = this.getSelectedIdx();
		if (idx.length==0) return null;
		return this.data[idx][key]._getValue();
	}
	
	/**
	 * 设置某一字段（列）的隐藏状态。
	 * 设置完后需要再调用renderHidden方法刷新显示。
	 * @param key 字段名
	 * @param hidden 隐藏状态 true or false
	 *//*
	EMP.widget.DataTable.prototype.setHidden = function(key, hidden){
	
		for (var i=0;i<this.data._getSize();i++) {
			var field = this.data[i][key];
			if (field) {field._obj.config.hidden = hidden;}
		}
	};
	
	/**
	 * 根据各字段的隐藏状态刷新显示
	 *//*
	EMP.widget.DataTable.prototype.renderHidden = function () {
		if (this.norecords) {
			var tHead = this.tag.tHead.rows[0];
			var realCells = tHead.cells.length;
			for (var i=0;i<tHead.cells.length;i++) {
				if(tHead.cells[i].getAttribute("hidden")=="true") {
					tHead.cells[i].style.display="none";					
					realCells--;
				} else {
					tHead.cells[i].style.display="";				
				}
			}
			if (this.tBodyMsg.rows.length==1 && realCells>0) {
				this.tBodyMsg.rows[0].cells[0].colSpan=realCells;
			}
		} else {
			var columnIdx;	//校验用，看列号是否一致
			var tHead = this.tag.tHead.rows[0];
			for (var i=0;i<tHead.cells.length;i++) {
				var id = tHead.cells[i].id.substring(3);	//去除th_
				if (id == "null") continue;
				var isHidden = true;
				for (var j=0;j<this.records.length;j++) {
					isHidden = isHidden && this.records[j].fields[id].config.hidden;	//所有行都hidden，才可隐藏该列			
				}
				if (isHidden) {
					var display = "none";
				} else {
					var display = "";	
				}
					
				tHead.cells[i].style.display=display;	
				if (this.tBodyTotal.rows.length>0)	
					this.tBodyTotal.rows[0].cells[i].style.display=display;		
				for (var j=0;j<this.records.length;j++) {
					this.records[j].fields[id].container.style.display=display;			
				}
			}
		}
	};
	
	/**
	 * 将整个表单数据复制到指定form
	 *
	 * @param form 目标form
	 */
	EMP.widget.DataTable.prototype.toForm = function(form) {
		this.data._toForm(form);
	};
	
	/**
	 * 使用tBodySample中的模板动态添加一行
	 */
	EMP.widget.DataTable.prototype._addRow = function() {
		
		var clonedRow = this.tBodySample.rows[0].cloneNode(true);
		this.tBodyMain.appendChild(clonedRow);	
		this.tBodyMain.style.display = "";
		this.data.push(this._registRow(clonedRow));

		this._addCss(clonedRow.sectionRowIndex);
	//	EMP.util.Tools.addClass(clonedRow,"row"+(clonedRow.rowIndex%2+1));
	//	clonedRow.id="EMP.widget.DataTable_"+this.id+"_tr_"+clonedRow.rowIndex;
	//	this.renderHidden();
	//	return null;	
		
	};
		
	/**
	 * 删除指定的一行
	 * @param idx 要删除的行号
	 */
	EMP.widget.DataTable.prototype._deleteRow = function(idx) {
	
		this.tBodyMain.deleteRow(idx);
		this.data.splice(idx,1);
		if (this.data._getSize()==0) this._noRecords();
		this._addCss(idx);
	//	if (this.records.length==0) {
	//		this.norecords=true;			
	//		this.tBodyMsg.style.display="";
	//	}
	//	this.addCss();
	//	this.clearAll();
	};
			
	/**
	 * 对EMP.widget.DataTable中的数据进行统计(TODO 尚未实现、内部代码只供参考)
	 */
	EMP.widget.DataTable.prototype.statistic = function() {
		/*
		if (this.tBodyTotal.rows.length!=1) return;
		var row = this.tBodyTotal.rows[0];
		for (var i=0;i<row.cells.length;i++) {
			var cell = row.cells[i];
			var spans = cell.getElementsByTagName("SPAN");
			if (spans && spans.length==1) {
				var dataName = spans[0].getAttribute("dataName");
				var dataType = spans[0].getAttribute("dataType");
				
				var statType = spans[0].getAttribute("statType");
				
				var result = this.getStatValue(dataName, statType);
				try{
					result = EMP.type.Base.checkAndDisplay(result, this.config.datatype, this.config.formatErrorMsg, this.config.rangeErrorMsg);
				}catch(e){
				}
				spans[0].innerHTML = result;
				EMP.util.Tools.addClass(cell,"emp_datatype_"+dataType);
			}		
		}
		*/		
	};
	
	/**
	 * 获得某一列的统计数据
	 *
	 * @param dataName 字段名
	 * @param statType 统计方式
	 *//*
	EMP.widget.DataTable.prototype.getStatValue = function(dataName, statType) {
	
		var total = 0;
		for (var j=0;j<this.records.length;j++){
			var value = this.records[j].fields[dataName].getValue();
			total += parseFloat(value);
		}
		//TODO 根据statType使用不同统计方式
		if (statType=="average" && this.records.length>0) {
			total = total/this.records.length;
		}
		return ""+total;	
	};*/
		
	/**
	 * API方法，向url发起异步请求，取得JSON定义串
	 * @param url 所请求的URL
	 */
	EMP.widget.DataTable.prototype.ajaxQuery = function (url, form) {

		if (url != null){
			this.url = url;
		}
		
		if (this.url != null) {
			this.tablePageQuery(this.url, form);
		}
	};
	
	/** 由上面方法改造的，解决极少数情况下无分页报js错的问题 by GC 20130320 begin */
	EMP.widget.DataTable.prototype.ajaxQuery_noPage = function (url, form) {

		if (url != null){
			this.url = url;
		}
		
		if (this.url != null) {
			this.tablePageQuery_noPage(this.url, form);
		}
	};

	EMP.widget.DataTable.prototype.tablePageQuery_noPage = function (url, form) {
			if(form != null){
				this.querydata = YAHOO.util.Connect.setForm(form);
			}
			
			this.setMessage("正在查询，请稍候...");
			var handleSuccess = function(responseText, callback){	
				callback.table.parseAjaxQuery_noPage(responseText, callback);
			};
			var callback = {
				success : handleSuccess,
				isJSON : true,
				handleError : true,
				formdata : this.querydata,
				table : this
			};
			
			if(this.querydata){
				EMP.util.Tools.ajaxRequest('POST', url, callback);
			} else {
				EMP.util.Tools.ajaxRequest('GET', url, callback);
			}
	};
	EMP.widget.DataTable.prototype.parseAjaxQuery_noPage = function (text, callback) {
		this.setMessage("查询完成，正在解析...");
		
		//显示异常信息
		var errorMessage = callback.errorMessage;
		if(errorMessage != null && errorMessage != ""){
			this.setMessage(errorMessage);
			return;
		}
		if(callback.isJSON == false){
			this.setMessage("查询失败！无法识别服务器返回内容！");
			return;
		}
		var json = text;
		var iColl = json[this.dataName];
		if (iColl == null) {
			if(this.pageMode) {
				this.pageQuery.setInfo(1, this.pageQuery.info.maxLine, 0);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
				this.setMessage("查询失败！服务器未返回所需数据！");
			}
			return;
		}
		this.tBodyMain.style.display="";
		var size = iColl.length;
		if(size == 0){
			this._noRecords();
			if(this.pageMode) {
				this.pageQuery.setInfo(1, this.pageQuery.info.maxLine, 0);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
			}
			return;
		}
		if (size>this.recordCount) {
			for (var i=0;i<size-this.recordCount;i++) {
				this._addRow();
			}
		}
		if (size<this.recordCount) {
			for (var i=0;i<this.recordCount-size;i++) {
				this._deleteRow(this.recordCount-i-1);
			}			
		}
		for (var i=0;i<size;i++) {			
			for (var j=0;j<this.columnNames.length;j++) {
				var columnNames = this.columnNames[j];
				var value = eval("iColl[i]."+columnNames);
				/** 排序增加特性(clear:20130927) */
				if(j==0&&columnNames=="displayid"){
					if(!this.pageMode){
						value = i+1;
					}else{
						value = (json.pageInfo.currentPage-1)*json.pageInfo.maxLine+i+1;
					}					
				}
				try {
					if (value!=null) {
						var obj = eval("this.data[i]."+columnNames);
						if (obj) obj._setValue(value);
					} else {
						var obj = eval("this.data[i]."+columnNames);
								if (obj) obj._setValue("");
					}
				} catch(e) {
					alert(e.message)
				}
			}
		}
		this.recordCount = size;
		//分页或者异步查询后重新初始化排序对象
		var _tables = this.tag.id.split('_');
		var _types = EMPDataTableMap.get(_tables[2]);
		if(_types != null && _types != 'undefined' && _types != '') {
			var _sorObj = EMPDataTableSortMap.get(this.tag.id);
			if(_sorObj)
				_sorObj._init(_sorObj,eval(_tables[2]));
		}
		if (this.pageMode) {				
			if (json.pageInfo) {
				this.pageQuery.setInfo(json.pageInfo.currentPage, json.pageInfo.maxLine, json.pageInfo.recordSize);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
			} else {
				this.setMessage("未找到分页信息！");
				return;
			}
		}
		this.setMessage(null);
	};
	/** 由上面方法改造的，解决极少数情况下无分页报js错的问题 end */
	
	/** 
	 * 展开/收缩记录分组
	 */
	EMP.widget.DataTable.prototype.groupExpand = function (_groupId){
		var _trList = document.getElementsByTagName("tr");
	  
		for(var n=0; n<_trList.length; n++){
			
			if(_trList[n].id.indexOf('tr_'+_groupId) == 0){
				
                if(_trList[n].style.display == 'none'){
				   _trList[n].style.display = 'block';
				   document.all("img_" + _groupId).src = "images/minimize.gif";
                }else{
                   _trList[n].style.display = 'none';
                   document.all("img_" + _groupId).src = "images/maximize.gif";
                }
			} 
		}
	}
	
	/**
	 * 为了满足分页与过滤查询两种情况，将Ajax请求部分抽取出来作为单独的方法，然后由前面两种情况下的不同方法进行调用
	 */
	EMP.widget.DataTable.prototype.tablePageQuery = function (url, form) {
			if(form != null){
				this.querydata = YAHOO.util.Connect.setForm(form);
			}
			
			this.setMessage("正在查询，请稍候...");
			var handleSuccess = function(responseText, callback){	
				callback.table.parseAjaxQuery(responseText, callback);
			};
			var callback = {
				success : handleSuccess,
				isJSON : true,
				handleError : true,
				formdata : this.querydata,
				table : this
			};
			
			if(this.querydata){
				EMP.util.Tools.ajaxRequest('POST', url, callback);
			} else {
				EMP.util.Tools.ajaxRequest('GET', url, callback);
			}
	};
	/**
	 * 异步请求的回调函数，解析JSON串，
	 * 从中取得dataName所对应的iColl定义，用于更新表格数据
	 * @param text JSON格式数据定义串
	 */
	EMP.widget.DataTable.prototype.parseAjaxQuery = function (text, callback) {
		this.setMessage("查询完成，正在解析...");
		
		//显示异常信息
		var errorMessage = callback.errorMessage;
		if(errorMessage != null && errorMessage != ""){
			this.setMessage(errorMessage);
			return;
		}
		if(callback.isJSON == false){
			this.setMessage("查询失败！无法识别服务器返回内容！");
			return;
		}
		var json = text;
		var iColl = json[this.dataName];
		if (iColl == null) {
			if(this.pageMode) {
				this.pageQuery.setInfo(1, this.pageQuery.info.maxLine, 0);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
				this.setMessage("查询失败！服务器未返回所需数据！");
			}
			return;
		}
		this.tBodyMain.style.display="";
		var size = iColl.length;
		if(size == 0){
			this._noRecords();
			if(this.pageMode) {
				this.pageQuery.setInfo(1, this.pageQuery.info.maxLine, 0);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
			}
			return;
		}
		if (size>this.recordCount) {
			for (var i=0;i<size-this.recordCount;i++) {
				this._addRow();
			}
		}
		if (size<this.recordCount) {
			for (var i=0;i<this.recordCount-size;i++) {
				this._deleteRow(this.recordCount-i-1);
			}			
		}
		for (var i=0;i<size;i++) {			
			for (var j=0;j<this.columnNames.length;j++) {
				var columnNames = this.columnNames[j];
				var value = eval("iColl[i]."+columnNames);
				/** 排序增加特性(clear:20130927) */
				if(j==0&&columnNames=="displayid"){
					value = (json.pageInfo.currentPage-1)*json.pageInfo.maxLine+i+1;
				}
				try {
					if (value!=null) {
						var obj = eval("this.data[i]."+columnNames);
						if (obj) obj._setValue(value);
					} else {
						var obj = eval("this.data[i]."+columnNames);
								if (obj) obj._setValue("");
					}
				} catch(e) {
					alert(e.message)
				}
			}
		}
		this.recordCount = size;
		//分页或者异步查询后重新初始化排序对象
		var _tables = this.tag.id.split('_');
		var _types = EMPDataTableMap.get(_tables[2]);
		if(_types != null && _types != 'undefined' && _types != '') {
			var _sorObj = EMPDataTableSortMap.get(this.tag.id);
			if(_sorObj)
				_sorObj._init(_sorObj,eval(_tables[2]));
		}
		if (this.pageMode) {				
			if (json.pageInfo) {
				this.pageQuery.setInfo(json.pageInfo.currentPage, json.pageInfo.maxLine, json.pageInfo.recordSize);
				this.pageQuery.setWaiting(false);
				this.pageQuery.refreshInfo();
			} else {
				this.setMessage("未找到分页信息！");
				return;
			}
		}
		this.setMessage(null);
	};
	/** ---------- 排序添加(clear)，20130927 ----------------- */
	EMP.widget.DataTable.prototype.Gird = (function () {
		//排序数据类型，目前支持数字、日期、字符串类型数据的排序
		var fSortFunctions = {
			"number":		sortByNumber,
			"data":			sortByDate,
			"string":		sortByString
		};
		var ie = /msie/ig.test(window.navigator.userAgent);
		var iX, iY;
		//初始化排序对象
		function initialize(oThis, oTb) {
			var oHead = oThis.element.tHead;		//表头标签对象 <thead></thead>
			var oBody = oThis.element.tBodies[1];	//取得当前DataTable的数据体，整个DataTable存在5个<tbody>，其中第二个为数据体
			var oHeadRow = oHead.rows[0];			//表头中的行<tr>对象
			var oRows = oBody.rows;					//数据体的行数
			var iColumn = oHeadRow.cells.length		//数据的列数
			for (var i=0; i<iColumn; i++) {	
				var oCell = oHeadRow.cells[i];					//数据列集合
				var oIcon = document.createElement("img");		//创建一个排序图标
				oIcon.src = "images/asc.gif";					//初始情况使用升序图标
				oIcon.style.visibility = "hidden";				//初始隐藏排序图标
				var _tagName = oCell.lastChild.tagName;
				if(_tagName != 'IMG')
					oCell.appendChild(oIcon);					//为本列添加图标子元素
				oCell.unselectable = "on";						//设置为非选中状态
				oCell.onclick = function() {					//表头单击触发函数
					for(var j=0; j<iColumn; j++) {
						var oTempCell = oHeadRow.cells[j];
						if(oTempCell.id == this.id) {			//单击的是哪一行就调用该行的排序方法
							oThis.sort(j); 
						}
					}
				}
				oCell.onmouseover = function () {
				};
			}
			oThis.data = new Array();							//创建data对象用于存储列表数据
			for (var i=0; i<oRows.length; i++) {
				var tmp = new Array();
				for (var j=0; j<iColumn; j++) {
					tmp[j] = getText(oRows[i].cells[j]);
					oRows[i].cells[j].unselectable = "on";
				}
				oThis.data[i] = { text : tmp, row : oRows[i] };//填充数据对象
			}
		};
		//初始化结束
		function getText(el) {
			var s = el.childNodes[0].value;
			return s;
		};
		//获得数据类型，默认使用string
		function getType(oThis, x) {
			return oThis.types[x] || "string";
		};
		//根据数据类型获取排序函数
		function getSortFunction(sType) {
			sType = sType.toLowerCase();
			if (typeof fSortFunctions[sType] != "undefined") {
				return fSortFunctions[sType];
			} else {
				return fSortFunctions["string"];
			}
		};
		//字符串排序函数
		function sortByString (a, b) {
			var f = gird.bDir ? 1 : -1;
			var i = gird.iSortIndex;
			if (a.text[i] > b.text[i]) return f;
			if (a.text[i] < b.text[i]) return -f;
			return 0;
		};
		//数字排序函数
		function sortByNumber (a, b) {
			var f = gird.bDir ? 1 : -1;
			var i = gird.iSortIndex;
			if (parseFloat(a.text[i]) - parseFloat(b.text[i]) > 0) return f;
			if (parseFloat(a.text[i]) - parseFloat(b.text[i]) < 0) return -f;
			return 0;		
		};
		//日期排序函数
		function sortByDate (a, b) {
			function toDate(s) {
				return Date.parse(s.replace(/-/g, '/'));
			}
			var i = gird.iSortIndex;
			var da = toDate(a.text[i]), db = toDate(b.text[i]);
			var f = gird.bDir ? 1 : -1;
			return (da - db) * f;		
		};
		//程序初始化构造函数入口
		function gird(oTable, sTypes) {
			this.element = oTable;
			this.types = sTypes || [];
			this.dir = false;
			this.currentColumn = null;
			this.onselect = null;
			initialize(this, oTable);
		};
		gird.bDir = false;
		gird.iSortIndex=null;
		//提供addType函数用于添加其他数据类型的排序扩展方法
		gird.addType = function (sType, fSortFunction) {
			fSortFunctions[sType] = fSortFunction;
		};	
		gird.prototype._init = function(oThis,oTable) {
			initialize(oThis, oTable);
		}
		//单击调用的排序方法
		gird.prototype.sort = function (x) {
			var oHeadCells = this.element.tHead.rows[0].cells;							//表头集合
			var oBody = this.element.tBodies[1];										//数据体
			var oRows = oBody.rows;														//数据体行集合
			var oIcon = oHeadCells[x].lastChild;										//取得排序事先添加的排序图标对象
			if (this.currentColumn != null && this.currentColumn != x ) {				
				oHeadCells[this.currentColumn].lastChild.style.visibility = "hidden";	//如果单击的不是本列则隐藏本列的排序图标
			}
			oIcon.style.visibility = "visible";											//显示单击列的排序图标
			if (this.currentColumn != x) {												
				this.dir = true;														//升序降序标志，true表示升序,false表示降序
			} else {
				this.dir = !this.dir;
				this.data.reverse();													//如果单击是当前排序列则重新排序
			}
			gird.bDir = this.dir;
			oIcon.src = gird.bDir ? "images/asc.gif" : "images/desc.gif";
			gird.iSortIndex = x;
			this.data.sort(getSortFunction(getType(this, x)));
			for (var i=0; i<oRows.length; i++) {
				oBody.insertBefore(this.data[i].row, oBody.childNodes[i]);
			}
			//循环数据map对象用于重新初始化一些东西
			for(var j=0; j<EMPDataTableMap.size(); j++) {
				var _keys = EMPDataTableMap.keySet();
				for(var k=0; k<_keys.length;k++) {
					var _tbodymain_str ='emp_table_'+_keys[k]+'_table';
					if(_tbodymain_str == this.element.id) {
						//担保信息Tab页面，点击排序时，不执行下面代码，会导致列表中只读属性丢失
						if(_keys[k] != "GrtLoanRGurListZge" && _keys[k] != "GrtLoanRGurListYb" ){
							eval(_keys[k])._obj.data = new EMP.data.IColl(eval(_keys[k])._obj);	//重新实例化data对象
							eval(_keys[k])._obj._regist();									//重新装载EMPTable
							eval(_keys[k])._obj._addCss();										//重新添加样式
							eval(_keys[k])._obj.selectAll();									//设置重新选择，由于之前排序后页面之前记载索引已经
							eval(_keys[k])._obj.clearAll();										//之前选择行丢失，所以没法重新设置非选中css样式，
																								//这里使用先全选后取消全选的方法
						}
					}
				}
			}
			this.currentColumn = x;
		};
		return gird;
	})();
};

/**
 * EMP.widget.PageQuery
 * 数据列表分页查询功能栏，由数据列表调用生成，不单独使用
 */
if (!EMP.widget.PageQuery) {

	/**
	 * 构造方法
	 * @param table 数据列表对象
	 */
	EMP.widget.PageQuery = function(table) {
		
		this.table = table;
		var tag = table.tBodyPq;
		
		var buttons = tag.getElementsByTagName("button");
		for (var i = 0; i<buttons.length; i++) {
			if (buttons[i].id == "emp_pq_previous") this.previous = buttons[i];
			if (buttons[i].id == "emp_pq_next") this.next = buttons[i];			
			if (buttons[i].id == "emp_pq_first") this.first = buttons[i];
			if (buttons[i].id == "emp_pq_last") this.last = buttons[i];	
			if (buttons[i].id == "emp_pq_jumpButton") this.jumpButton = buttons[i];			
		}
		
		var inputs = tag.getElementsByTagName("input");
		for (var i = 0; i<inputs.length; i++) {
			if (inputs[i].id == "emp_pq_jumpInput") this.jumpInput = inputs[i];
			if (inputs[i].id == "emp_pq_maxLine") this.maxLine = inputs[i];
		}
		
		var spans = tag.getElementsByTagName("span");
		for (var i = 0; i<spans.length; i++) {
			if (spans[i].id == "emp_pq_currentPage") this.currentPage = spans[i];
			if (spans[i].id == "emp_pq_totalPage") this.totalPage = spans[i];
			if (spans[i].id == "emp_pq_recordSize") this.recordSize = spans[i];
		}
		
		this.info = { currentPage : 1 , maxLine : 15 , totalPage : 1 , recordSize: 0 };
		
		var recordSize = null;
		if(this.recordSize){
			recordSize = this.recordSize.innerHTML;
		}
		this.setInfo(this.currentPage.innerHTML, this.maxLine.value, recordSize);
		
	/*	var location = window.location.toString();
		var currentPage = parseInt(this.getAnchor(location, "currentPage"));
		var recordSize = parseInt(this.getAnchor(location, "recordSize"));
		var maxLine = parseInt(this.getAnchor(location, "maxLine"));
		if (!isNaN(currentPage) && !isNaN(recordSize) && !isNaN(maxLine)) {
			this.setInfo(currentPage, recordSize, maxLine);
			this.toPage(currentPage);		
		} else {*/
		
		this.refreshInfo();

		EMP.util.Tools.addEvent(this.previous,"click",this.toPrevious,this);
		EMP.util.Tools.addEvent(this.next,"click",this.toNext,this);
		EMP.util.Tools.addEvent(this.first,"click",this.toFirst,this);
		EMP.util.Tools.addEvent(this.last,"click",this.toLast,this);
		EMP.util.Tools.addEvent(this.jumpButton,"click",this.directTo,this);
	};
	
	/**
	 * 设置分页信息
	 */
	EMP.widget.PageQuery.prototype.setInfo = function(currentPage,maxLine,recordSize) {
		
		currentPage = parseInt(currentPage);
		if(!isNaN(currentPage))
			this.info.currentPage = currentPage;
		
		maxLine = parseInt(maxLine);
		if(!isNaN(maxLine))
			this.info.maxLine = maxLine;
		
		recordSize = parseInt(recordSize);
		if(!isNaN(recordSize))
			this.info.recordSize = recordSize;
		
		this.info.totalPage = Math.ceil(this.info.recordSize/this.info.maxLine);	
		if(	this.info.totalPage < 1)
			this.info.totalPage = 1;
//		if (isNaN(this.info.totalPage))
	};
	
	/**
	 * 刷新分页信息的显示
	 */
	EMP.widget.PageQuery.prototype.refreshInfo = function () {
	
/*		var location = window.location.toString();			
		location = this.setAnchor(location, "currentPage", this.info.currentPage);
		location = this.setAnchor(location, "recordSize", this.info.recordSize);
		location = this.setAnchor(location, "maxLine", this.info.maxLine);
		window.location = location;*/
	
		this.currentPage.innerHTML = this.info.currentPage;
		this.totalPage.innerHTML = this.info.totalPage;
		this.recordSize.innerHTML = this.info.recordSize;
	
		this.maxLine.value = this.info.maxLine;
		this.jumpInput.value = this.info.currentPage;
		
		if (this.info.currentPage <= 1){
			this.previous.disabled = true;
			this.first.disabled = true;
		} else {
			this.previous.disabled = false;
			this.first.disabled = false;
		}
		
		if (this.info.currentPage >= this.info.totalPage){
			this.next.disabled = true;
			this.last.disabled = true;
		} else {
			this.next.disabled = false;
			this.last.disabled = false;
		}		
		
		this.jumpButton.disabled = false;	

	};
	
	/**
	 * 转向某一页
	 * @param targetPage 目标页号
	 */
	EMP.widget.PageQuery.prototype.toPage = function (targetPage) {
		
		if (targetPage!=1 && (!targetPage || targetPage<1 || targetPage>this.info.totalPage )){
			alert("无效的跳转！");
			return;
		}
		
		var url = this.table.url;
		
		if (url) {
			var maxLine = this.maxLine.value;
			//---------分页跳转变得更--------
			var pageHelp = targetPage;
			if((pageHelp-1) < 0){
				pageHelp=1;
			}
			if(((pageHelp-1)*maxLine)>=this.info.recordSize){
				alert("无效的跳转！");
				return;
			}
			if (maxLine!=null && maxLine!="" && maxLine!="0" && maxLine.match(/^[0-9]+$/)) {
				var newTotalPage = Math.ceil(this.info.recordSize/maxLine) + 1;
				if (newTotalPage<targetPage) targetPage = newTotalPage;
				url = EMP.util.Tools.setParam(url,"pageInfo.maxLine",maxLine);
			} else {
				maxLine = this.info.maxLine;
			}
			if(maxLine > EMP.util.Tools.PageQueryMaxLine){
				alert("每页行数必须少于"+EMP.util.Tools.PageQueryMaxLine+"行!");
				return;
			}
			
			url = EMP.util.Tools.setParam(url,"pageInfo.targetPage",targetPage);
			url = EMP.util.Tools.setParam(url,"pageInfo.recordSize",this.info.recordSize);
			 
			if(this.table.tableType == 'g'){
			    window.location = url;
			}else{
				this.setWaiting(true);		
				this.table.tablePageQuery(url, null);
				this.table.clearAll();
			}
		}else{
			alert("未设置分页的URL，请检查列表的配置!");
		}
	};
	
	/**
	 * 转向前一页
	 */
	EMP.widget.PageQuery.prototype.toPrevious = function () {
		this.toPage(this.info.currentPage-1);
	};
	
	/**
	 * 转向后一页
	 */	
	EMP.widget.PageQuery.prototype.toNext = function () {
		this.toPage(this.info.currentPage+1);	
	};
	
	/**
	 * 转向第一页
	 */	
	EMP.widget.PageQuery.prototype.toFirst = function () {
		this.toPage(1);
	};
	
	/**
	 * 转向最后一页
	 */	
	EMP.widget.PageQuery.prototype.toLast = function () {
		this.toPage(this.info.totalPage);	
	};
	
	/**
	 * 转向跳转框中指定的页面
	 */	
	EMP.widget.PageQuery.prototype.directTo = function () {
		var value = this.jumpInput.value;
		if (value!=null && value!="" && value.match(/^[0-9]+$/))
			this.toPage(value);
		else
			alert("请输入有效的数字!");
	};

	/**
	 * 设置各操作元素的查询等待状态
	 * @param waiting 是否查询等待状态
	 */
	EMP.widget.PageQuery.prototype.setWaiting = function (waiting) {
		this.previous.disabled = waiting;
		this.next.disabled = waiting;
		this.first.disabled = waiting;
		this.last.disabled = waiting;
		this.jumpButton.disabled = waiting;
	};
}


if (!EMP.widget.DataFieldContainer) {

	/**
	 * 构造方法
	 * @param obj 编辑域对象
	 */
	EMP.widget.DataFieldContainer = function(obj, isTable) {
		this._obj = obj;
		this.isTable = isTable;
		this.parentTD = null;
		this.labelTD = null;
	};
	
	EMP.widget.DataFieldContainer.prototype._renderHidden = function(hidden){
		
		if(this._obj.config.onlycontrolelement == true)
			return;
		
		var parentTD = this.parentTD;
		if(!parentTD){
			parentTD = this._obj.tag.parentNode;
			if(parentTD.tagName != 'TD')
				return;
			this.parentTD = parentTD;
		}
		
		var preTD = this.labelTD;
		if(!preTD && !this.isTable){//针对非列表的情况进行处理
			preTD = parentTD.previousSibling;
			while(preTD){
				if(preTD.tagName != 'TD'){
					preTD = preTD.previousSibling;
					continue;
				}
				this.labelTD = preTD;
				break;
			}	
		}
		var hiddenStyle = "";
		if(hidden)
			hiddenStyle = "none";
		
		parentTD.style.display = hiddenStyle;
		if(preTD)
			preTD.style.display = hiddenStyle;//设置label属性
		
		
		if(!this.isTable && EMPTools.reGridLayout){
			//对单元格进行colSpan重新设置
			var parentTR = parentTD.parentNode;
			if(parentTR.tagName == 'TR'){
				//如果当前TD后面还有TD(表示并非本行的最后一个数据)，则不做重新布局
				var reGridLayout = true;
				if(this._obj.tag.getAttribute("rendered") == 'false'){
					var nextTD = parentTD.nextSibling;
					while(nextTD){
						if(nextTD.tagName != 'TD'){
							nextTD = nextTD.nextSibling;
							continue;
						}
						reGridLayout = false;
						break;
					}
				}
				
				if(reGridLayout){//需要重新布局的情况
					var tagList = parentTR.getElementsByTagName("span");
					var countSpan = 0;
					var lastTag = null;
					for(var i=0;i<tagList.length;i++){
						var tag = tagList[i];
						if (tag!=null && tag.empObj != null) {
							var obj = tag.empObj;
							if(obj.config.hidden)
								continue;
							var colSpan = obj.config.colspan;
							if(colSpan == null)
								colSpan = "1";
							countSpan += parseInt(colSpan);
							tag.parentNode.colSpan = parseInt(colSpan)*2-1; //将之前调整后的colSpan还原
							lastTag = tag;
						}
					}
					
					if(countSpan == 0){//全部隐藏的情况
						parentTR.style.display = "none";
					}else{
						parentTD = lastTag.parentNode;//最后一个非隐藏的编辑域的TD
						parentTR.style.display = "";
						var parentTable = parentTR.parentNode;
						while(parentTable.tagName != 'TABLE'){
							parentTable = parentTable.parentNode;
						}
						var maxColumn = parentTable.getAttribute("maxColumn");
						if(maxColumn){
							var countSpan = parseInt(maxColumn)-countSpan;
							var colSpan = parentTD.colSpan;
							if(!colSpan)
								colSpan = "1";
							
							if(countSpan > 0){
								var colSpan = parseInt(colSpan) + countSpan*2;
								parentTD.colSpan = colSpan;
							}
						}
					}
				}
			}
		}
		
	};

	
	EMP.widget.DataFieldContainer.prototype._renderReadonly = function(readonly) {
		
	};
		
	EMP.widget.DataFieldContainer.prototype._renderDisabled = function(disabled) {
		
	};
	
};

/**向form中添加隐藏域对象
 * @form form对象
 * @name 对象名称
 * @value 对象值
 * */
function doAddHiddenChildToForm(form, name, value){
	try{
		var input = document.createElement("input");
		input.type="hidden";
		input.name = name;
		input.value = value;
		form.appendChild(input);	
		form[name] = input;//强制在form对象中将新建的隐藏域放入
	}catch(e){}
};