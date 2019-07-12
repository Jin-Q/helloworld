if (!EMP.field.Base) {
	alert('脚本[fields.js]需要[data.js]的支持！');
}

if (!EMP.field.Text) {

	EMP.field.Text = function() {
	};

	EMP.field.Text.prototype = new EMP.field.Base();

	EMP.field.Text.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");

		this.element = this.tag.getElementsByTagName("INPUT")[0];

		if (this.config.maxlength)
			this.element.setAttribute("maxLength", this.config.maxlength);

		// 定义缺省的onblur事件，以校验输入的数据是否合法
		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);

	};

	EMP.field.Text.prototype._renderHidden = function(hidden) {
		EMP.field.Base.prototype._renderHidden.call(this, hidden);
		this.element.hidden = hidden;
	};

	EMP.field.Text.prototype._renderReadonly = function(readonly) {

		EMP.field.Base.prototype._renderReadonly.call(this, readonly);
		this.element.readOnly = readonly;
	};

	EMP.field.Text.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		this.element.disabled = disabled;
	};

	/**
	 * 默认onblur方法
	 */
	EMP.field.Text.prototype._defaultEventHandler = function() {

		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (this.element.value == null || this.element.value == "") {
			this.clearError();
			return true;
		}
		if (this.config.datatype != null) {
			// 对于月利率，页面输入为月利率，数据库存、取值为年利率，传入值不一样却调用同样的check和display方法，所以页面输入值加上"‰"以示区别
			// edit by kuangjie 这样改侵入了平台标签的机制，但没有更好的办法 edit wqgang
			if (this.config.datatype == 'Rate4Month'
					|| this.config.datatype == 'Percent'
					|| this.config.datatype == 'Percent2'
					|| this.config.datatype == 'Rate'
					|| this.config.datatype == 'Permille') {

				var val = this.element.value;
				var checkStr = this.config.validatejs;
				var arrPram = checkStr.split(',');
				if (arrPram[2].indexOf('false') == -1) {// 百分之
					if (val.indexOf('%') == -1) {// 没填百分号
						this.element.value = val + '%';
					}
				} else if (arrPram[2].indexOf('true') == -1) {// 千分之
					if (val.indexOf('‰') == -1) {// 没填千分号
						this.element.value = val + '‰';
					}
				} else {

				}
			}
			// end edit
			var retMsg = EMP.type.Base.checkAndDisplay(this.element.value,
					this.config.validatejs, this.config.convertorjs,
					this.config.formaterrormsg, this.config.rangeerrormsg);
			if (retMsg.result == false) {
				var errorMsg = retMsg.message;
				if (errorMsg == null || errorMsg == "")
					errorMsg = this.config.formaterrormsg;

				this.reportError(errorMsg);

				this.focus();

				return false;
			}

			this.element.value = retMsg.message;
		}
		this.clearError();// 在校验通过后，清除原来的错误信息

		// 进行业务层次的校验
		var ret = this.doCheckBussiness();
		if (ret == false)
			this.focus();
		return ret;
	};

	/**
	 * 设置数据域取值 设置的值可以是输入值、显示值、真实值中任何一种格式
	 * 这里根据李嘉的定义的逻辑，setValue输入值应该为真实值。getValue输出值应该是真实值。
	 * 只有这样才能从一个对象中把真实值get出来存入另一个。显示还能正确modify by xubin
	 * 
	 * @param value
	 */
	EMP.field.Text.prototype.setValue = function(value) {
		this.value = value;

		if (this.config.datatype != null) {
			this.element.value = EMP.type.Base.display(this.value,
					this.config.convertorjs);
		} else {
			this.element.value = this.value;
		}
	};

	/**
	 * 获得数据域取值
	 */
	EMP.field.Text.prototype.getValue = function() {
		var value = this.element.value;
		if (this.config.datatype != null) {// 目前所有由percent 扩展的dataTypeDef
			// 的getValue方法无法正确取到真实值，所以不建议采用该方法,如果要实现getValue取得真实值需要改造该方法，
			// 根据类型修改check方法的value参数即可，参考EMP.field.Text.prototype._defaultEventHandler,
			// 因为福建已经通过测试，减少风险，所以在此暂不修改 wqgang comment
			var retMsg = EMP.type.Base.check(value, this.config.validatejs,
					this.config.formaterrormsg, this.config.rangeerrormsg);
			if (retMsg.result == false) {
				value = null;
			} else {
				value = retMsg.message;
			}
		}
		return value;
	};

	EMP.field.Text.prototype.getDisplayValue = function() {
		return this.element.value;
	}

	EMP.field.Text.prototype.getId = function() {

		return this.element.name;
	}

	EMP.field.Text.prototype.doCheckLength = function() {

		var len = EMPTools.getByteLength(this.getValue());

		// 判断最长输入字符、最短输入字符
		if (this.config.maxlength && len > this.config.maxlength) {
			return EMPTools.message(false, this.title + "输入超过"
					+ this.config.maxlength + "个字节！");
		} else if (len != 0 && this.config.minlength
				&& len < this.config.minlength) {
			return EMPTools.message(false, this.title + "输入少于"
					+ this.config.minlength + "个字节！");
		}

		// 判断必输项
		if (!this.config.required) {
			return EMPTools.message(true);
		} else if (len == 0) {
			return EMPTools.message(false, "请输入" + this.title + "！");
		}

		return EMPTools.message(true);
	};
	/**
	 * 指定域添加一个按钮 @ id: 按钮ID title：标签 function：点击事件执行函数
	 */
	EMP.field.Text.prototype.addOneButton = function(id, title, fun) {
		var tempTag = this.tag;
		if (this.config.readonly) {
			// this.config.readonly=true;
			this._renderReadonly(true);
			if (tempTag.firstChild.readonly == undefined)
				tempTag.firstChild.disabled = false;
			else
				tempTag.firstChild.readonly = true;
		}
		;
		// tempTag.firstChild.style.
		// var pxSize=160-title.length*15;
		// tempTag.firstChild.style.width=pxSize+"px";
		var btn = document.createElement('BUTTON');
		btn.innerText = title;
		btn.id = id;
		btn.className = "emp_field_pop_button";
		btn.onclick = fun;
		tempTag.appendChild(btn);
		return btn;
	}
	/**
	 * 删除按钮，完善添加按钮方法  add by zhaozq
	 * @ id: 按钮ID
	 */
	EMP.field.Text.prototype.removeOneButton = function(id) {
		var tempTag = this.tag;
		if (document.getElementById(id)==null) {
			return EMPTools.message(false);
		}else{
			tempTag.removeChild(document.getElementById(id));
			return EMPTools.message(true);
		}
	}
	/**按钮样式改变，为业务提供
	 * 指定域添加一个按钮 @ id: 按钮ID title：标签 function：点击事件执行函数
	 */
	EMP.field.Text.prototype.addTheButton = function(id, title, fun) {
		var tempTag = this.tag;
		if (this.config.readonly) {
			// this.config.readonly=true;
			this._renderReadonly(true);
			if (tempTag.firstChild.readonly == undefined)
				tempTag.firstChild.disabled = false;
			else
				tempTag.firstChild.readonly = true;
		}
		;
		// tempTag.firstChild.style.
		// var pxSize=160-title.length*15;
		// tempTag.firstChild.style.width=pxSize+"px";
		var btn = document.createElement('BUTTON');
		btn.innerText = title;
		btn.id = id;
		btn.className = "button90"; 
		btn.onclick = fun;
		tempTag.appendChild(btn);
		return btn;
	}
	/**
	 * 检查数据类型
	 */
	EMP.field.Text.prototype.doCheckDataType = function() {
		var value = this.element.value;
		if (this.config.datatype != null) {
			var retMsg = EMP.type.Base.check(value, this.config.validatejs,
					this.config.formaterrormsg, this.config.rangeerrormsg);
			return retMsg;
		}
		return EMPTools.message(true);
	};

	EMP.field.Text.prototype.focus = function() {
		this.element.focus();
		this.element.select();
	};
};

if (!EMP.field.DynPassword) {

	EMP.field.DynPassword = function() {
	};

	EMP.field.DynPassword.prototype = new EMP.field.Text();

	EMP.field.DynPassword.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");
		this._parseAttribute("keyBoardTitle");
		this._parseAttribute("keyBoardHeight");
		this._parseAttribute("keyBoardWidth");

		this.element = this.tag.getElementsByTagName("INPUT")[0];

		if (this.config.maxlength)
			this.element.setAttribute("maxLength", this.config.maxlength);

		// 定义缺省的onblur事件，以校验输入的数据是否合法
		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);

		// 定义onfocus事件，用于弹出动态键盘
		EMPTools.addEvent(this.element, "click", this._clickEventHandler, this);
	};

	EMP.field.DynPassword.prototype._clickEventHandler = function() {
		if (this.keyBoard == null)
			this.keyBoard = new EMP.widget.SoftKeyboard(this);
		this.keyBoard.showKeyboard();
	};
};

if (!EMP.field.AppendPin) {

	EMP.field.AppendPin = function() {
	};

	EMP.field.AppendPin.prototype = new EMP.field.Text();

	EMP.field.AppendPin.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");
		this._parseAttribute("codeLength");

		this.element = this.tag.getElementsByTagName("INPUT")[0];
		this.image = this.tag.getElementsByTagName("IMG")[0];

		this.element.setAttribute("maxLength", this.config.codelength);

		// 定义缺省的onblur事件，以校验输入的数据是否合法
		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);

	};

	EMP.field.AppendPin.prototype.doCheckLength = function() {

		var len = EMPTools.getByteLength(this.getValue());

		// 判断输入字符长度
		if (len != 0 && len != this.config.codelength) {
			return EMPTools.message(false, this.title + "输入必须是"
					+ this.config.codelength + "个字节！");
		}

		// 判断必输项
		if (!this.config.required) {
			return EMPTools.message(true);
		} else if (len == 0) {
			return EMPTools.message(false, "请输入" + this.title + "！");
		}

		return EMPTools.message(true);
	};
};

if (!EMP.field.Link) {

	EMP.field.Link = function() {
	};

	EMP.field.Link.prototype = new EMP.field.Base();

	EMP.field.Link.prototype._initialize = function() {

		this._parseAttribute("operation");
		this._parseAttribute("target");
		this._parseAttribute("opName");

		this.element = this.tag.getElementsByTagName("A")[0];

		this.config.href = this.element.href;

		if (this.config.operation != null) {
			EMP.util.Tools.addEvent(this.element, "click", this.click, this);
		}
	};

	EMP.field.Link.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		this.element.disabled = this.config.disabled;
		if (disabled)
			this.element.href = "#";
		else
			this.element.href = this.config.href;
	};

	EMP.field.Link.prototype.click = function() {
		if (!this.config.disabled) {
			if (this.table) {
				var idx = this.tag.parentNode.parentNode.sectionRowIndex;
				this.table.select(idx, true);
			}
			window['do' + this.config.operation.substring(0, 1).toUpperCase()
					+ this.config.operation.substring(1)]();
		}
	};

	EMP.field.Link.prototype.setValue = function(value) {
		if (this.config.opname == null) {
			EMP.field.Base.prototype.setValue.call(this, value);
		}
	};

	/**
	 * 进行业务层次的校验(Link没有业务层次的校验)
	 */
	EMP.field.Link.prototype.doCheckBussiness = function() {

	};
};

if (!EMP.field.MultiLink) {

	EMP.field.MultiLink = function() {
	};

	EMP.field.MultiLink.prototype = new EMP.field.Base();

	EMP.field.MultiLink.prototype._initialize = function() {

		this.elements = this.tag.getElementsByTagName("A");
		this.config.hrefs = new Array();
		for ( var i = 0; i < this.elements.length; i++) {
			var href = this.elements[i].href;
			this.config.hrefs[i] = href;
			var sharpidx = href.lastIndexOf('#');
			if (sharpidx != -1 && sharpidx != href.length - 1) {
				var operation = href.substring(sharpidx + 1);
				EMPTools.addEvent(this.elements[i], "click", this.click, this);
			}
		}
		this.config.linkcount = this.elements.length;
	};

	/**
	 * 进行业务层次的校验(MultiLink没有业务层次的校验)
	 */
	EMP.field.MultiLink.prototype.doCheckBussiness = function() {

	};

	EMP.field.MultiLink.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		for ( var i = 0; i < this.config.linkcount; i++) {
			this.elements[i].disabled = disabled;
			if (disabled)
				this.elements[i].href = '#';
			else
				this.elements[i].href = this.config.hrefs[i];
		}
	};

	EMP.field.MultiLink.prototype.setDisabled = function(disabled, idx) {
		if (idx == null) {
			this._renderDisabled(disabled);
		} else {
			if (idx >= 0 && idx < this.config.linkcount) {
				this.elements[idx].disabled = disabled;
				if (disabled)
					this.elements[idx].href = '#';
				else
					this.elements[idx].href = this.config.hrefs[i];
			} else {
				alert(this.title + "没有第" + (idx + 1) + "个链接!");
			}
		}
	};

	EMP.field.MultiLink.prototype.getDisabled = function(idx) {
		if (idx == null) {
			return this.config.disabled;
		} else {
			if (idx >= 0 && idx < this.config.linkcount) {
				return this.elements[idx].disabled;
			} else {
				alert(this.title + "没有第" + (idx + 1) + "个链接!");
				return false;
			}
		}
	};

	EMP.field.MultiLink.prototype.click = function(e) {

		if (EMP.util.Tools.Browser.ie) {
			var href = e.srcElement.href;
		} else {
			var href = e.target.href;
		}
		var sharpidx = href.lastIndexOf('#');
		var operation = href.substring(sharpidx + 1);
		if (operation != '' && !this.config.disabled) {
			if (this.table) {
				var idx = this.tag.parentNode.parentNode.sectionRowIndex;
				this.table.select(idx, true);
			}
			window['do' + operation.substring(0, 1).toUpperCase()
					+ operation.substring(1)]();
		}

	};

	EMP.field.MultiLink.prototype.setValue = function(value) {

	};
};

if (!EMP.field.TextArea) {

	EMP.field.TextArea = function() {
	};

	EMP.field.TextArea.prototype = new EMP.field.Text();

	EMP.field.TextArea.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");
		this._parseAttribute("cols");
		this._parseAttribute("rows");

		this.element = this.tag.getElementsByTagName("TEXTAREA")[0];

		if (this.config.maxlength) {// 通过JS的方式来限制TextArea的字数
			// EMPTools.addEvent(this.element, "keypress",
			// this._defaultOnKeyPressEvent, this);
		}

		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);
	};

	EMP.field.TextArea.prototype._defaultOnKeyPressEvent = function() {
		var len = EMPTools.getByteLength(this.getValue());
		return (len < this.config.maxlength);
	};

};

if (!EMP.field.Select) {

	EMP.field.Select = function() {
	};

	EMP.field.Select.prototype = new EMP.field.Base();

	EMP.field.Select.prototype._initialize = function() {

		this._parseAttribute("defMsg");

		this.element = this.tag.getElementsByTagName("SELECT")[0];
		this.fakeinput = this.tag.getElementsByTagName("INPUT")[0];

		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);
		EMPTools.addEvent(this.element, "change",
				this._changeValueEventHandler, this);

	};

	EMP.field.Select.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		this.element.disabled = disabled;
	};

	EMP.field.Select.prototype.setValue = function(value) {

		value = EMPTools.trim(value);
		this.value = value;

		this.element.value = value;
		if (this.element.selectedIndex == -1) {
			this.element.value = ""; // 置为"请选择"的情况
			if (!this.config.flat)
				this.fakeinput.value = "";
		} else {
			if (!this.config.flat)
				this.fakeinput.value = this.element.options[this.element.selectedIndex].text;
		}

		if (this.relatedSelectDefine && this.relatedSelectDefine.next) {
			var nextSelect = this.relatedSelectDefine.next;
			// 如果置在"请选择"的选项上，则不需要向后台发起请求，直接将下一联动下拉框的选项清空
			if (this.element.selectedIndex == -1
					|| this.element.selectedIndex == 0) {
				var selectSrc = [];
				this.relatedSelectDefine.group.updateSelectInnerHTML(
						nextSelect, selectSrc);
			} else {
				this.relatedSelectDefine.group.doInitSelectContent(nextSelect);
			}
		}
	};

	EMP.field.Select.prototype.getDisplayValue = function() {

		return this.element.options(this.element.selectedIndex).text;
	};

	EMP.field.Select.prototype.getValue = function() {
		return this.element.value;
	};

	EMP.field.Select.prototype.doCheckLength = function() {
		if (!this.config.required)
			return EMPTools.message(true);
		if (this.element.value == null || this.element.value == "") {
			return EMPTools.message(false, "请选择一项作为" + this.title + "！");
		}
		return EMPTools.message(true);
	};

	/**
	 * 默认onblur方法
	 */
	EMP.field.Select.prototype._defaultEventHandler = function() {

		this.clearError();// 先清除原来的错误信息

		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (this.element.value == null || this.element.value == "") {
			return true;
		}

		// 只进行业务层次的校验
		var ret = this.doCheckBussiness();
		return ret;
	};

	/**
	 * 只适合于联动下拉框情况
	 */
	EMP.field.Select.prototype._changeValueEventHandler = function() {
		if (this.relatedSelectDefine && this.relatedSelectDefine.next) {
			this.setValue(this.getValue());
		}
	};

	/**
	 * 指定域添加一个按钮 @ id: 按钮ID title：标签 function：点击事件执行函数
	 */
	EMP.field.Select.prototype.addOneButton = function(id, title, fun) {
		var tempTag = this.tag;
		if (this.config.readonly) {
			// this.config.readonly=true;
			this._renderReadonly(true);
			if (tempTag.firstChild.readonly == undefined)
				tempTag.firstChild.disabled = false;
			else
				tempTag.firstChild.readonly = true;
		}
		;
		// tempTag.firstChild.style.
		// var pxSize=160-title.length*15;
		// tempTag.firstChild.style.width=pxSize+"px";
		var btn = document.createElement('BUTTON');
		btn.innerText = title;
		btn.id = id;
		btn.className = "emp_field_pop_button";
		btn.onclick = fun;
		tempTag.appendChild(btn);
		return btn;
	};
};

if (!EMP.field.Date) {

	EMP.field.Date = function() {
	};

	EMP.field.Date.prototype = new EMP.field.Text();

	EMP.field.Date.prototype._initialize = function() {

		this.element = this.tag.getElementsByTagName("INPUT")[0];

		EMPTools.addEvent(this.element, "blur", this._defaultEventHandler, this);
		
		if (!Calendar)
			alert("未包含Calendar.js，日期域不可用，请检查页面编辑内容！");
		else
			EMPTools.addEvent(this.element, "click", this._openCalendar, this);
	};

	EMP.field.Date.prototype._openCalendar = function() {
		// 只读或不可操作的情况下，不弹出日期选择框
		if (this.config.readonly || this.config.disabled)
			return;
		if (page.calendarObj == null) {
			page.calendarObj = new Calendar("page.calendarObj");
			var tempDiv = document.createElement("DIV");
			document.body.appendChild(tempDiv);
			tempDiv.innerHTML += page.calendarObj;
		}
		page.calendarObj.show(this.element);
	};

};

if (!EMP.field.Pop) {

	EMP.field.Pop = function() {
	};

	EMP.field.Pop.prototype = new EMP.field.Text();

	EMP.field.Pop.prototype._initialize = function() {

		this._parseAttribute("url");
		this._parseAttribute("returnMethod");
		this._parseAttribute("popParam");
		this._parseAttribute("dataMapping");

		this.element = this.tag.getElementsByTagName("INPUT")[0];
		this.button = this.tag.getElementsByTagName("BUTTON")[0];
		// 设置前方的文本框为只读 edit by xubin
		this.element.readOnly = true;
		EMPTools
				.addEvent(this.element, "blur", this._defaultEventHandler, this);

		var popFunc = function() {

			var returnMethod = this.dataName + "._obj.dataMappingHandler";
			var url = EMPTools.setParam(this.config.url, "popReturnMethod",
					returnMethod);

			EMPTools.openWindow(url, this.id, this.config.popparam);
		}
		EMPTools.addEvent(this.button, "click", popFunc, this);
	};

	EMP.field.Pop.prototype.dataMappingHandler = function(data) {

		this.clearError();// 先清除原来的错误信息

		if (this.config.datamapping) {
			var mappings = this.config.datamapping.split(";");
			for ( var i = 0; i < mappings.length; i++) {
				var mappingStr = mappings[i];
				var idx = mappingStr.indexOf("=");
				if (idx == -1)
					continue;
				var targetObj = mappingStr.substring(0, idx);
				var srcObj = mappingStr.substring(idx + 1);
				eval(targetObj + "._obj.setValue(data." + srcObj + ")");
			}
		}
		// 执行自定义的返回方法
		if (this.config.returnmethod)
			eval(this.config.returnmethod + "(data)");

		// 进行业务层次的校验
		this.doCheckBussiness();
	};

	/**
	 * 默认onblur方法
	 */
	EMP.field.Pop.prototype._defaultEventHandler = function() {

		this.clearError();// 先清除原来的错误信息

		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (this.element.value == null || this.element.value == "") {
			return true;
		}

		// 只进行业务层次的校验
		var ret = this.doCheckBussiness();
		return ret;
	};

	EMP.field.Pop.prototype._renderReadonly = function(readonly) {
		// 修改_renderReadonly方法，使readonly只作用于后方的按钮。edit by xubin
		if (readonly) {
			this.button.disabled = 'disabled';
			EMP.field.Base.prototype._renderReadonly.call(this, readonly);
		} else {
			this.button.disabled = '';
			EMP.field.Base.prototype._renderReadonly.call(this, readonly);
		}
	};
	EMP.field.Pop.prototype._renderInputReadonly = function(readonly) {
		EMP.field.Base.prototype._renderReadonly.call(this, readonly);
	};

};

if (!EMP.field.Radio) {

	EMP.field.Radio = function() {
	};

	EMP.field.Radio.prototype = new EMP.field.Base();

	EMP.field.Radio.prototype._initialize = function() {
		this.elements = this.tag.getElementsByTagName("INPUT");// this.element是一个INPUT框的集合
		for ( var i = 0; i < this.elements.length; i++) {
			EMPTools.addEvent(this.elements[i], "click", this.click, this);
		}
		this.isCheckColumn = false;// 缺省不作为列表的单、复选列
	};

	EMP.field.Radio.prototype.click = function() {

		this.clearError();// 先清除原来的错误信息

		// 只进行业务层次的校验
		var ret = this.doCheckBussiness();
		return ret;
	};

	EMP.field.Radio.prototype.setValue = function(value) {

		this.value = value;

		for ( var i = 0; i < this.elements.length; i++) {
			this.elements[i].checked = false;
			if (this.elements[i].value == value) {
				this.elements[i].checked = true;
			}
		}
	};

	EMP.field.Radio.prototype.getValue = function() {

		var value = "";
		for ( var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].checked == true) {
				value = this.elements[i].value;
				break;
			}
		}
		return value;
	};

	EMP.field.Radio.prototype.doCheckLength = function() {
		if (!this.config.required)
			return EMPTools.message(true);
		for ( var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].checked)
				return EMPTools.message(true);
		}
		return EMPTools.message(false, "请选择一项作为" + this.title + "！");
	};

	EMP.field.Radio.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		for ( var i = 0; i < this.elements.length; i++) {
			this.elements[i].disabled = disabled;
		}
	};
};

if (!EMP.field.CheckBox) {

	EMP.field.CheckBox = function() {
	};

	EMP.field.CheckBox.prototype = new EMP.field.Radio();

	EMP.field.CheckBox.prototype._initialize = function() {

		this._parseAttribute("valueCollection");

		this.elements = this.tag.getElementsByTagName("INPUT");// this.element是一个INPUT框的集合
		for ( var i = 0; i < this.elements.length; i++) {
			EMPTools.addEvent(this.elements[i], "click", this.click, this);
		}
		this.isCheckColumn = false;// 缺省不作为列表的单、复选列
	};

	EMP.field.CheckBox.prototype.setValue = function(valueList) {

		if (valueList == null) {
			valueList = new Array();
		}
		this.value = valueList;

		for ( var i = 0; i < this.elements.length; i++) {
			this.elements[i].checked = false;
			for ( var j = 0; j < valueList.length; j++) {
				if (this.elements[i].value == valueList[j]) {
					this.elements[i].checked = true;
					break;
				}
			}
		}
	};

	EMP.field.CheckBox.prototype.getValue = function() {

		var valueList = new Array();
		for ( var i = 0; i < this.elements.length; i++) {
			if (this.elements[i].checked == true) {
				valueList.push(this.elements[i].value);
			}
		}
		return valueList;
	};

	/**
	 * 将该数据域的取值复制到指定表单
	 * 
	 * @param form
	 *            指定表单DOM对象
	 */
	EMP.field.CheckBox.prototype.toForm = function(form, prefix) {
		if (form == null)
			return;
		var name = this.dataName;
		if (prefix != null)
			name = prefix + name;

		var valueList = this.getValue();

		if (this.config.valuecollection == null) {// 将多选框当作一个数据对象看待
			var value = "";
			for ( var i = 0; i < valueList.length; i++) {
				if (i == 0)
					value = valueList[i];
				else
					value = value + "," + valueList[i];
			}
			var input = form[name];

			// 若form中没有同名的input，则新建一个隐藏的input放在form中
			if (input == null) {
				input = form.document.createElement("input");
				input.type = "hidden";
				input.name = name;
				form.appendChild(input);
				form[name] = input;// 强制在form对象中将新建的隐藏域放入
			}
			if (value != null && value != "")
				input.value = value;
		} else {// 将多选框的数据当作列表数据进行提交
			// TODO 将集合中的数据域名称都统一成enname
			name = "enname";

			for ( var i = 0; i < valueList.length; i++) {
				var value = valueList[i];
				var inputName = this.config.valuecollection + "[" + i + "]."
						+ name;

				var input = form[inputName];

				// 若form中没有同名的input，则新建一个隐藏的input放在form中
				if (input == null) {
					input = document.createElement("input");
					input.type = "hidden";
					input.name = inputName;
					form.appendChild(input);
					form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
				}
				if (value != null) {
					input.value = value;
				} else {
					input.value = "";
				}
			}

			// 将form中可能存在着集合数据清空
			for ( var i = valueList.length; i < this.elements.length; i++) {
				var inputName = this.config.valuecollection + "[" + i + "]."
						+ name;
				var input = form[inputName];

				if (input) {
					input.value = "";
					// form.removeChild(input);
					// form[inputName] = null;
				}
			}
		}

		EMP.util.Tools.log('EMP.data.Field', 0, '数据域[' + name + ']值[' + value
				+ ']被复制到表单[' + form.id + '].');
	};
};

/**
 * 用于普通输入框的区间查询
 */
if (!EMP.field.TextSpace) {

	EMP.field.TextSpace = function() {
	};

	EMP.field.TextSpace.prototype = new EMP.field.Text();

	EMP.field.TextSpace.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");

		this.element_begin = this.tag.getElementsByTagName("INPUT")[0];
		this.element_end = this.tag.getElementsByTagName("INPUT")[1];

		if (this.config.maxlength) {
			this.element_begin.setAttribute("maxLength", this.config.maxlength);
			this.element_end.setAttribute("maxLength", this.config.maxlength);
		}

		EMP.util.Tools.addEvent(this.element_begin, "blur",
				this._defaultEventHandler, this);
		EMP.util.Tools.addEvent(this.element_end, "blur",
				this._defaultEventHandler, this);

	};

	/**
	 * 判断输入区间是否正确
	 */
	EMP.field.TextSpace.prototype.doCheckLength = function() {

	};

	/**
	 * 进行业务层次的校验(区间查询没有业务层次的校验)
	 */
	EMP.field.TextSpace.prototype.doCheckBussiness = function() {

	};

	/**
	 * 默认onblur方法 检查数据类型，并调用自定义onblur方法(若存在)
	 */
	EMP.field.TextSpace.prototype._defaultEventHandler = function(e) { // 事件方法参数

		var el = EMP.util.Tools.Browser.ie ? e.srcElement : e.target;// IE与firefox对事件处理不同

		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (el.value == null || el.value == "") {
			this.clearError();
			return true;
		}

		if (this.config.datatype != null) {
			var retMsg = EMP.type.Base.checkAndDisplay(el.value,
					this.config.validatejs, this.config.convertorjs,
					this.config.formaterrormsg, this.config.rangeerrormsg);
			if (retMsg.result == false) {
				var errorMsg = retMsg.message;
				if (errorMsg == null || errorMsg == "")
					errorMsg = this.config.formaterrormsg;

				this.reportError(errorMsg);
				this.focus();
				return false;
			}
			el.value = retMsg.message;
		}

		// 判断区间(必须是后面输入框的内容大于前面输入框的内容)
		var valueObj = this.getValue();
		if (valueObj.begin && valueObj.end) {
			var beginValue = parseFloat(valueObj.begin);
			var endValue = parseFloat(valueObj.end);
			if (!isNaN(beginValue) && !isNaN(endValue)) {
				if (beginValue > endValue) {
					this.reportError(this.title + "输入的区间错误!");
					return false;
				}
			} else if (valueObj.begin > valueObj.end) {
				this.reportError(this.title + "输入的区间错误!");
				return false;
			}
		}

		this.clearError();// 在校验通过后，清除原来的错误信息

		return true;
	};

	/**
	 * 检查数据类型
	 */
	EMP.field.TextSpace.prototype.doCheckDataType = function() {
		if (this.config.datatype != null) {
			// 校验前面的输入框
			var retMsg = EMP.type.Base.check(this.element_begin.value,
					this.config.validatejs, this.config.formaterrormsg,
					this.config.rangeerrormsg);
			if (retMsg.result == false) {
				this.focus(this.element_begin);// 焦点在前面的输入框
				return retMsg;
			}

			// 校验后面的输入框
			retMsg = EMP.type.Base.check(this.element_end.value,
					this.config.validatejs, this.config.formaterrormsg,
					this.config.rangeerrormsg);
			if (retMsg.result == false) {
				this.focus(this.element_end);// 焦点在后面的输入框
				return retMsg;
			}
		}
		return EMPTools.message(true);
	};

	/**
	 * 数据域取值
	 */
	EMP.field.TextSpace.prototype.setValue = function(valueObj) {
		this.value = valueObj;

		if (this.config.datatype != null) {
			// 设置前一个输入框的值
			this.element_begin.value = EMP.type.Base.display(this.value.begin,
					this.config.convertorjs);
			// 设置后一个输入框的值
			this.element_end.value = EMP.type.Base.display(this.value.end,
					this.config.convertorjs);
		} else {
			this.element_begin.value = valueObj.begin;
			this.element_end.value = valueObj.end;
		}
	};

	/**
	 * 获得数据域取值
	 */
	EMP.field.TextSpace.prototype.getValue = function() {
		var valueObj = {};
		if (this.config.datatype != null) {
			// 取得前一个输入框的值
			var retMsg = EMP.type.Base.check(this.element_begin.value,
					this.config.validatejs, this.config.formaterrormsg,
					this.config.rangeerrormsg);
			if (retMsg.result == true) {
				valueObj.begin = retMsg.message;
			}
			// 取得后一个输入框的值
			retMsg = EMP.type.Base.check(this.element_end.value,
					this.config.validatejs, this.config.formaterrormsg,
					this.config.rangeerrormsg);
			if (retMsg.result == true) {
				valueObj.end = retMsg.message;
			}
		} else {
			valueObj.begin = this.element_begin.value;
			valueObj.end = this.element_end.value;
		}
		return valueObj;
	};

	EMP.field.TextSpace.prototype._renderHidden = function() {
		EMP.field.Base.prototype._renderHidden.call(this);
	};

	EMP.field.TextSpace.prototype._renderReadonly = function() {
		EMP.field.Base.prototype._renderReadonly.call(this);
		this.element_begin.readOnly = this.config.readonly;
		this.element_end.readOnly = this.config.readonly;
	};

	EMP.field.TextSpace.prototype._renderDisabled = function() {
		EMP.field.Base.prototype._renderDisabled.call(this);
		this.element_begin.disabled = this.config.disabled;
		this.element_end.disabled = this.config.disabled;
	};

	EMP.field.TextSpace.prototype.focus = function(element) {
		if (!element)
			return;
		element.focus();
		element.select();
	};

	/**
	 * 将该数据域的取值复制到指定表单
	 * 
	 * @param form
	 *            指定表单DOM对象
	 */
	EMP.field.TextSpace.prototype.toForm = function(form, prefix) {
		if (form == null)
			return;
		var name = this.dataName;
		if (prefix != null)
			name = prefix + name;

		var valueObj = this.getValue();// 取值

		// 先toForm前一个输入框
		var value = valueObj.begin;
		var inputName = name + "_begin";
		var input = form[inputName];
		if (input == null) {
			input = form.document.createElement("input");
			input.type = "hidden";
			input.name = inputName;
			form.appendChild(input);
			form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
		}
		if (value != null && value != "")
			input.value = value;

		// 然后toForm后一个输入框
		value = valueObj.end;
		inputName = name + "_end";
		input = form[inputName];
		if (input == null) {
			input = form.document.createElement("input");
			input.type = "hidden";
			input.name = inputName;
			form.appendChild(input);
			form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
		}
		if (value != null && value != "")
			input.value = value;
	};
};

/**
 * 用于日期类型的区间查询
 */
if (!EMP.field.DateSpace) {

	EMP.field.DateSpace = function() {
	};

	EMP.field.DateSpace.prototype = new EMP.field.TextSpace();

	EMP.field.DateSpace.prototype._initialize = function() {
		this.element_begin = this.tag.getElementsByTagName("INPUT")[0];
		this.element_end = this.tag.getElementsByTagName("INPUT")[1];

		EMP.util.Tools.addEvent(this.element_begin, "blur",
				this._defaultEventHandler, this);
		EMP.util.Tools.addEvent(this.element_end, "blur",
				this._defaultEventHandler, this);

		if (!Calendar)
			alert("未包含Calendar.js，日期域不可用，转为普通文本框！");
		else {
			EMP.util.Tools.addEvent(this.element_begin, "click",
					this._openCalendar, this);
			EMP.util.Tools.addEvent(this.element_end, "click",
					this._openCalendar, this);
		}
	};

	EMP.field.DateSpace.prototype._openCalendar = function(e) {
		// 只读情况下，不弹出日期选择框
		if (this.config.readonly || this.config.disabled)
			return;

		var el = EMP.util.Tools.Browser.ie ? e.srcElement : e.target;// IE与firefox对事件处理不同

		if (page.calendarObj == null) {
			page.calendarObj = new Calendar("page.calendarObj");
			var tempDiv = document.createElement("DIV");
			document.body.appendChild(tempDiv);
			tempDiv.innerHTML += page.calendarObj;
		}
		page.calendarObj.show(el);
	};
};

/**
 * 查询框__文本
 */
if (!EMP.field.SearchText) {

	EMP.field.SearchText = function() {
	};

	EMP.field.SearchText.prototype = new EMP.field.Base();

	EMP.field.SearchText.prototype._initialize = function() {

		this._parseAttribute("maxlength");
		this._parseAttribute("minlength");

		// 文本元素
		this.element = this.tag.getElementsByTagName("INPUT")[1];

		// 查询框元素
		this.select_element = this.tag.getElementsByTagName("SELECT")[0];
		this.fakeinput = this.tag.getElementsByTagName("INPUT")[0];

		if (this.config.maxlength)
			this.element.setAttribute("maxLength", this.config.maxlength);

		// 定义缺省的onblur事件，以校验输入的数据是否合法
		EMPTools.addEvent(this.element, "blur", this._defaultEventHandler, this);

	};

	EMP.field.SearchText.prototype._renderHidden = function(hidden) {
		EMP.field.Base.prototype._renderHidden.call(this, hidden);
		this.element.hidden = hidden;
	};

	EMP.field.SearchText.prototype._renderReadonly = function(readonly) {

		EMP.field.Base.prototype._renderReadonly.call(this, readonly);
		this.element.readOnly = readonly;
	};

	EMP.field.SearchText.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		this.element.disabled = disabled;
	};

	/**
	 * 默认onblur方法
	 */
	EMP.field.SearchText.prototype._defaultEventHandler = function(e) {
		var el = EMP.util.Tools.Browser.ie ? e.srcElement : e.target;// IE与firefox对事件处理不同
		
		//取下拉框的name
		var name = el.name;
		//得到当前SPAN的name
		if(name.indexOf("_begin")>-1) name = name.substring(0,name.indexOf("_begin"));
		if(name.indexOf("_end")>-1) name = name.substring(0,name.indexOf("_end"));
		//得到当前tag
		var tag = document.getElementById("emp_field_" + name);
		
		//从tag(SPAN)标签里取到dataType以相关校验属性
		var dataType = tag.dataType;
		var validateJS = tag.validateJS;
		var convertorJS = tag.convertorJS;
		var formatErrorMsg = tag.formatErrorMsg; 
		var rangeErrorMsg = tag.rangeErrorMsg;
		var cssErrorClass = tag.cssErrorClass;
		
		//提示错误信息SPAN的NAME
		var errorSpanName = name+"errorSpan";
		
		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (el.value == null || el.value == "") {
			EMP.field.SearchText.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
			return true;
		}
		if (dataType != null) {
			// 对于月利率，页面输入为月利率，数据库存、取值为年利率，传入值不一样却调用同样的check和display方法，所以页面输入值加上"‰"以示区别
			// edit by kuangjie 这样改侵入了平台标签的机制，但没有更好的办法 edit wqgang
			if (dataType == 'Rate4Month'
					|| dataType == 'Percent'
					|| dataType == 'Percent2'
					|| dataType == 'Rate'
					|| dataType == 'Permille') {

				var val = el.value;
				var checkStr = validateJS;
				var arrPram = checkStr.split(',');
				if (arrPram[2].indexOf('false') == -1) {// 百分之
					if (val.indexOf('%') == -1) {// 没填百分号
						this.element.value = val + '%';
					}
				} else if (arrPram[2].indexOf('true') == -1) {// 千分之
					if (val.indexOf('‰') == -1) {// 没填千分号
						this.element.value = val + '‰';
					}
				} else {

				}
			}
			// end edit
			var retMsg = EMP.type.Base.checkAndDisplay(el.value,
					validateJS, convertorJS,formatErrorMsg, rangeErrorMsg);
			if (retMsg.result == false) {
				var errorMsg = retMsg.message;
				if (errorMsg == null || errorMsg == "")
					errorMsg = formatErrorMsg;
				
				//重写reportError
				if (errorMsg!=null) {
					EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,errorMsg,cssErrorClass);
				}else{
					//清除错误提示，如果有的话
					EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
				}
				
				el.focus();
				return false;
			}
			//校验通过，则将错误提示信息删除，如果存在的话
			else{
				EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
			}

			el.value = retMsg.message;
		}
//		this.clearError();// 在校验通过后，清除原来的错误信息 

		// 进行业务层次的校验
		var ret = this.doCheckBussiness();
		if (ret == false)
			this.focus();
		return ret;
	};
	
	/**
	 * 显示错错信息
	 * @param tag 父节点，在tag后添加错误信息
	 * @param name 子节点的名 还用于判断该节点存在性
	 * @param errorMsg 显示的错识信息
	 * @param cssErrorClass 子节点的样式
	 */
	EMP.field.SearchText.prototype.reportErrorSpan = function(tag,errorSpanName,errorMsg,cssErrorClass){
		var existErrorSpan = false;
		var errorSpan;
		
		//找到是否存在errorSpan
		var size = tag.getElementsByTagName("SPAN").length;
		
		for(var i=0; i<size; i++){
			var tmpObj = tag.getElementsByTagName("SPAN")[i];
			if(tmpObj.name == errorSpanName) {
				existErrorSpan = true;
				errorSpan = tmpObj;
			}
		}
		
		//如果existErrorSpan不存在 则添加
		if(!existErrorSpan && errorMsg !=null && errorMsg != ""){
			errorSpan = document.createElement("SPAN");
			errorSpan.name = errorSpanName;
			tag.appendChild(errorSpan);
			if(cssErrorClass){
				EMPTools.addClass(errorSpan, cssErrorClass);
			}
			EMP.util.Tools.setInnerText(errorSpan,errorMsg);
		}
		//如果存在，则添加信息
		if(existErrorSpan && errorMsg != ""){
			EMP.util.Tools.setInnerText(errorSpan,errorMsg);
		}
		//清除信息
		else if(existErrorSpan && errorMsg == ""){
			errorSpan.innerHTML = "";
		}
		
	}

	/**
	 * 设置数据域取值 设置的值可以是输入值、显示值、真实值中任何一种格式
	 * 这里根据李嘉的定义的逻辑，setValue输入值应该为真实值。getValue输出值应该是真实值。
	 * 只有这样才能从一个对象中把真实值get出来存入另一个。显示还能正确modify by xubin
	 * 
	 * @param value
	 */
	EMP.field.SearchText.prototype.setValue = function(value) {
		this.value = value;

		if (this.config.datatype != null) {
			this.element.value = EMP.type.Base.display(this.value,
					this.config.convertorjs);
		} else {
			this.element.value = this.value;
		}
	};

	/**
	 * 获得数据域取值 = 查询模式+“_”+真实值  区间查询以_begin开始 _end 结束
	 */
	EMP.field.SearchText.prototype.getValue = function() {
		var search_model_value = this.select_element.value;// 下拉选项的值
		
		//输入框的值不能从this.element中取，因为在区间查询时，已经将该element移除掉，
		var ele = this.tag.getElementsByTagName("INPUT")[1];
		var value = ele.value; // text输入框的值
		var name = ele.name;
		
		//非区间查询模式
		// 如果查询模式不为空，则value=查询模式_查询值==>让后台知道查询模式
		if (search_model_value != "undefined" && search_model_value != null
				&& search_model_value != "" && search_model_value != "70") { 
			value = search_model_value + "_" + value
			
			if (this.config.datatype != null) {
				var retMsg = EMP.type.Base.check(value, this.config.validatejs,
						this.config.formaterrormsg, this.config.rangeerrormsg);
				if (retMsg.result == false) {
					value = null;
				} else {
					value = retMsg.message;
				}
			}
			
			return value;
		}
		//区间查询模式
		else if(search_model_value=="70"){
			try{
				var valueObj={};
				//得到当前tag
				//区间查询的第一个文档输入框的name是以_begin结束的，这里要简单处理掉
				if(name.indexOf("_begin")){
					name = name.substring(0,name.indexOf("_begin")); 
				}
				var tag = document.getElementById("emp_field_" + name);
				element_begin = tag.getElementsByTagName("INPUT")[1];
				element_end = tag.getElementsByTagName("INPUT")[2];
				
				valueObj.begin = element_begin.value;
				valueObj.end = element_end.value;
				
				return valueObj; 
			}catch(e){
			}
		}
		
		return value;
		
	};
	
	/**
	 * 查询模式为：70-区间查询，改变查询个数
	 */
	EMP.field.SearchText.prototype.spaceSearch = function(ele) {
		//取下拉框的name
		var name = ele.name;
		//取查询模式
		var search_model_value = ele.value;
		//得到当前SPAN的name
		name = name.substring(0, name.indexOf("searchType"));
		//得到当前tag
		var tag = document.getElementById("emp_field_" + name);
		
		//区间查询模式：
		if(search_model_value == "70"){
			
			//先建两个文本框
			var input_begin = document.createElement("INPUT");
			var input_end = document.createElement("INPUT");
			input_begin.name = name + "_begin";
			input_end.name = name + "_end";

			//移除以前的输入框
			ele_old = tag.getElementsByTagName("INPUT")[1];
			tag.removeChild(ele_old);
			
			//将新建的查询框添加到tag标签后
			tag.appendChild(input_begin);
			tag.insertAdjacentHTML("BeforeEnd","<span id='spaceText'> 到 </span>");
			tag.appendChild(input_end);
			
			// 定义缺省的onblur事件，以校验输入的数据是否合法
			EMPTools.addEvent(input_begin, "blur", EMP.field.SearchText.prototype._defaultEventHandler, this);
			EMPTools.addEvent(input_end, "blur", EMP.field.SearchText.prototype._defaultEventHandler, this);
			
		}
		//非区间查询
		else{
			//判断查询框个数
			var size = tag.getElementsByTagName("INPUT").length;
			//如果个数大于2个(算上查询模式本身的INPUT)，则是区间查询模改为非区间查询模式，改变查询框
			if(size > 2){
				//移除tag下所有查询框
				for(var i = 1; i<size; i++){
					var tmpObj = tag.getElementsByTagName("INPUT")[1];
					tag.removeChild(tmpObj);
				}
				//移除距间文件
				var spaceTextSpan = tag.getElementsByTagName("SPAN")[0];
				tag.removeChild(spaceTextSpan);
				
				//新建 名为name的查询框
				var sel_input = document.createElement("INPUT");
				
				sel_input.name = name;
				tag.appendChild(sel_input);
				
				
				// 定义缺省的onblur事件，以校验输入的数据是否合法
				EMPTools.addEvent(sel_input, "blur", EMP.field.SearchText.prototype._defaultEventHandler, this);
			}
		}
	};
	
	/**
	 * 将该数据域的取值复制到指定表单
	 * 
	 * 对于查询模式为区间查询的将xx_begin xx_end复制到表单，非区间查询使用父类方法
	 * 
	 * @param form
	 *            指定表单DOM对象
	 */
	EMP.field.SearchText.prototype.toForm = function(form, prefix) {
		var value = this.select_element.value; // 查询模式
		if (form == null)
			return;
		var name = this.dataName;
		if (prefix != null)
			name = prefix + name;
		
		//区域查询模式
		if(value !=null && value == "70"){
			var valueObj = this.getValue();// 取值
			
			// 先toForm前一个输入框
			var value = valueObj.begin;
			var inputName = name + "_begin";
			var input = form[inputName];
			if (input == null) {
				input = form.document.createElement("input");
				input.type = "hidden";
				input.name = inputName;
				form.appendChild(input);
				form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
			}
			if (value != null && value != "")
				input.value = value;

			// 然后toForm后一个输入框
			value = valueObj.end;
			inputName = name + "_end";
			input = form[inputName];
			if (input == null) {
				input = form.document.createElement("input");
				input.type = "hidden";
				input.name = inputName;
				form.appendChild(input);
				form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
			}
			if (value != null && value != "")
				input.value = value;
			
		}
		//非区间查询
		else {
			//EMP.field.Base.prototype.toForm.call(this);
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
			}
			
			EMP.util.Tools.log('EMP.data.Field',0
					,'数据域['+name+']值['+value+']被复制到表单['+form.id+'].');
			
		}
	};

	EMP.field.SearchText.prototype.getDisplayValue = function() {
		return this.element.value;
	}

	EMP.field.SearchText.prototype.getId = function() {

		return this.element.name;
	}

	EMP.field.Text.prototype.doCheckLength = function() {

		var len = EMPTools.getByteLength(this.getValue());

		// 判断最长输入字符、最短输入字符
		if (this.config.maxlength && len > this.config.maxlength) {
			return EMPTools.message(false, this.title + "输入超过"
					+ this.config.maxlength + "个字节！");
		} else if (len != 0 && this.config.minlength
				&& len < this.config.minlength) {
			return EMPTools.message(false, this.title + "输入少于"
					+ this.config.minlength + "个字节！");
		}

		// 判断必输项
		if (!this.config.required) {
			return EMPTools.message(true);
		} else if (len == 0) {
			return EMPTools.message(false, "请输入" + this.title + "！");
		}

		return EMPTools.message(true);
	};
	/**
	 * 指定域添加一个按钮 @ id: 按钮ID title：标签 function：点击事件执行函数
	 */
	EMP.field.Text.prototype.addOneButton = function(id, title, fun) {
		var tempTag = this.tag;
		if (this.config.readonly) {
			// this.config.readonly=true;
			this._renderReadonly(true);
			if (tempTag.firstChild.readonly == undefined)
				tempTag.firstChild.disabled = false;
			else
				tempTag.firstChild.readonly = true;
		}
		;
		// tempTag.firstChild.style.
		// var pxSize=160-title.length*15;
		// tempTag.firstChild.style.width=pxSize+"px";
		var btn = document.createElement('BUTTON');
		btn.innerText = title;
		btn.id = id;
		btn.className = "emp_field_pop_button";
		btn.onclick = fun;
		tempTag.appendChild(btn);
		return btn;
	}
	/**
	 * 检查数据类型
	 */
	EMP.field.Text.prototype.doCheckDataType = function() {
		var value = this.element.value;
		if (this.config.datatype != null) {
			var retMsg = EMP.type.Base.check(value, this.config.validatejs,
					this.config.formaterrormsg, this.config.rangeerrormsg);
			return retMsg;
		}
		return EMPTools.message(true);
	};

	EMP.field.Text.prototype.focus = function() {
		this.element.focus();
		this.element.select();
	};
};

/**
 * 查询框_日期
 */
if (!EMP.field.SearchDate) {

	EMP.field.SearchDate = function() {
	};

	EMP.field.SearchDate.prototype = new EMP.field.Text();

	EMP.field.SearchDate.prototype._initialize = function() {

		// this.element = this.tag.getElementsByTagName("INPUT")[0];

		// 文本元素
		this.element = this.tag.getElementsByTagName("INPUT")[1];

		// 查询框元素
		this.select_element = this.tag.getElementsByTagName("SELECT")[0];
		this.fakeinput = this.tag.getElementsByTagName("INPUT")[0];

		EMPTools.addEvent(this.element, "blur", this._defaultEventHandler, this);

		if (!Calendar)
			alert("未包含Calendar.js，日期域不可用，请检查页面编辑内容！");
		else
			EMPTools.addEvent(this.element, "click", this._openCalendar, this);
	};

	EMP.field.SearchDate.prototype._openCalendar = function() {
		// 只读或不可操作的情况下，不弹出日期选择框
		if (this.config.readonly || this.config.disabled)
			return;
		if (page.calendarObj == null) {
			page.calendarObj = new Calendar("page.calendarObj");
			var tempDiv = document.createElement("DIV");
			document.body.appendChild(tempDiv);
			tempDiv.innerHTML += page.calendarObj;
		}
		page.calendarObj.show(this.element);
	};
	
	/**
	 * 默认onblur方法 检查数据类型
	 * 
	 * 因为有区间查询，移除和添加了新的input 所以要丢失了config属性，只能通过SPAN标签找到
	 * 对应的属必加以调用
	 * 
	 */
	EMP.field.SearchDate.prototype._defaultEventHandler = function(e) { // 事件方法参数
		
		var el = EMP.util.Tools.Browser.ie ? e.srcElement : e.target;// IE与firefox对事件处理不同
		
		//取下拉框的name
		var name = el.name;
		//得到当前SPAN的name
		if(name.indexOf("_begin")>-1) name = name.substring(0,name.indexOf("_begin"));
		if(name.indexOf("_end")>-1) name = name.substring(0,name.indexOf("_end"));
		//得到当前tag
		var tag = document.getElementById("emp_field_" + name);
		
		//从tag(SPAN)标签里取到dataType以相关校验属性
		var dataType = tag.dataType;
		var validateJS = tag.validateJS;
		var convertorJS = tag.convertorJS;
		var formatErrorMsg = tag.formatErrorMsg; 
		var rangeErrorMsg = tag.rangeErrorMsg;
		var cssErrorClass = tag.cssErrorClass;
		
		//提示错误信息SPAN的NAME
		var errorSpanName = name+"errorSpan";
		
		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (el.value == null || el.value == "") {
			EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
			return true;
		}

		if (dataType != null) {
			var retMsg = EMP.type.Base.checkAndDisplay(el.value,
					validateJS, convertorJS,formatErrorMsg, rangeErrorMsg);
			if (retMsg.result == false) {
				var errorMsg = retMsg.message;
				if (errorMsg == null || errorMsg == "")
					errorMsg = formatErrorMsg;
				
				//重写reportError
				if (errorMsg!=null) {
					EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,errorMsg,cssErrorClass);
				}else{
					//清除错误提示，如果有的话
					EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
				}
				
				el.focus();
				return false;
			}
			//如果校验成功，则将错误提示信息删除，如果存在的话
			else{
				EMP.field.SearchDate.prototype.reportErrorSpan(tag,errorSpanName,"",cssErrorClass);
			}
			
			el.value = retMsg.message;
		}
	};
	
	
	/**
	 * 显示错错信息
	 * @param tag 父节点，在tag后添加错误信息
	 * @param name 子节点的名 还用于判断该节点存在性
	 * @param errorMsg 显示的错识信息
	 * @param cssErrorClass 子节点的样式
	 */
	EMP.field.SearchDate.prototype.reportErrorSpan = function(tag,errorSpanName,errorMsg,cssErrorClass){
		var existErrorSpan = false;
		var errorSpan;
		
		//找到是否存在errorSpan
		var size = tag.getElementsByTagName("SPAN").length;
		
		for(var i=0; i<size; i++){
			var tmpObj = tag.getElementsByTagName("SPAN")[i];
			if(tmpObj.name == errorSpanName) {
				existErrorSpan = true;
				errorSpan = tmpObj;
			}
		}
		
		//如果existErrorSpan不存在 则添加
		if(!existErrorSpan && errorMsg !=null && errorMsg != ""){
			errorSpan = document.createElement("SPAN");
			errorSpan.name = errorSpanName;
			tag.appendChild(errorSpan);
			if(cssErrorClass){
				EMPTools.addClass(errorSpan, cssErrorClass);
			}
			EMP.util.Tools.setInnerText(errorSpan,errorMsg);
		}
		//如果存在，则添加信息
		if(existErrorSpan && errorMsg != ""){
			EMP.util.Tools.setInnerText(errorSpan,errorMsg);
		}
		//清除信息
		else if(existErrorSpan && errorMsg == ""){
			errorSpan.innerHTML = "";
		}
		
	}
	
	/**
	 * 查询模式为：70-区间查询，改变查询个数
	 */
	EMP.field.SearchDate.prototype.spaceSearch = function(ele) {
		
		//取下拉框的name
		var name = ele.name;
		//取查询模式
		var search_model_value = ele.value;
		//得到当前SPAN的name
		name = name.substring(0, name.indexOf("searchType"));
		//得到当前tag
		var tag = document.getElementById("emp_field_" + name);
		
		//区间查询模式：
		if(search_model_value == "70"){
			
			//先建两个文本框
			var input_begin = document.createElement("INPUT");
			var input_end = document.createElement("INPUT");
			input_begin.name = name + "_begin";
			input_end.name = name + "_end";

			//移除以前的输入框
			ele_old = tag.getElementsByTagName("INPUT")[1];
			tag.removeChild(ele_old);
			
			//将新建的查询框添加到tag标签后
			tag.appendChild(input_begin);
			tag.insertAdjacentHTML("BeforeEnd","<span id='spaceText'> 到 </span>");
			tag.appendChild(input_end);
			
			//给输入框添加事件:弹出日期控件
			EMP.util.Tools.addEvent(input_begin, "click",EMP.field.SearchDate.prototype._openCalendarSpace, this);
			EMP.util.Tools.addEvent(input_end, "click",EMP.field.SearchDate.prototype._openCalendarSpace, this);
			
			
			// 定义缺省的onblur事件，以校验输入的数据是否合法
			EMPTools.addEvent(input_begin, "blur", EMP.field.SearchDate.prototype._defaultEventHandler, this);
			EMPTools.addEvent(input_end, "blur", EMP.field.SearchDate.prototype._defaultEventHandler, this);
			
		}
		//非区间查询
		else{
			//判断查询框个数
			var size = tag.getElementsByTagName("INPUT").length;
			//如果个数大于2个(算上查询模式本身的INPUT)，则是区间查询模改为非区间查询模式，改变查询框
			if(size > 2){
				//移除tag下所有查询框
				for(var i = 1; i<size; i++){
					var tmpObj = tag.getElementsByTagName("INPUT")[1];
					tag.removeChild(tmpObj);
				}
				//移除距间文件
				var spaceTextSpan = tag.getElementsByTagName("SPAN")[0];
				tag.removeChild(spaceTextSpan);
				
				//新建 名为name的查询框
				var sel_input = document.createElement("INPUT");
				
				sel_input.name = name;
				tag.appendChild(sel_input);
				
				//给输入框添加事件:弹出日期控件
				EMP.util.Tools.addEvent(sel_input, "click",EMP.field.SearchDate.prototype._openCalendarSpace, this);
			}
		}
		
	};
	
	EMP.field.SearchDate.prototype._openCalendarSpace = function(e) {
		// 只读情况下，不弹出日期选择框
		//if (this.config.readonly || this.config.disabled)
		//	return; 

		var el = EMP.util.Tools.Browser.ie ? e.srcElement : e.target;// IE与firefox对事件处理不同

		if (page.calendarObj == null) {
			page.calendarObj = new Calendar("page.calendarObj");
			var tempDiv = document.createElement("DIV");
			document.body.appendChild(tempDiv);
			tempDiv.innerHTML += page.calendarObj;
		}
		page.calendarObj.show(el);
	};

	/**
	 * 获得数据域取值 = 查询模式+“_”+真实值
	 */
	EMP.field.SearchDate.prototype.getValue = function() {
		var search_model_value = this.select_element.value;// 下拉选项的值
		
		//输入框的值不能从this.element中取，因为在区间查询时，已经将该element移除掉，
		var ele = this.tag.getElementsByTagName("INPUT")[1];
		var value = ele.value; // text输入框的值
		var name = ele.name;
		
		//非区间查询模式
		// 如果查询模式不为空，则value=查询模式_查询值==>让后台知道查询模式
		if (search_model_value != "undefined" && search_model_value != null
				&& search_model_value != "" && search_model_value != "70") { 
			value = search_model_value + "_" + value
			
			if (this.config.datatype != null) {
				var retMsg = EMP.type.Base.check(value, this.config.validatejs,
						this.config.formaterrormsg, this.config.rangeerrormsg);
				if (retMsg.result == false) {
					value = null;
				} else {
					value = retMsg.message;
				}
			}
			
			return value;
		}
		//区间查询模式
		else if(search_model_value=="70"){
			try{
				var valueObj={};
				//得到当前tag
				//区间查询的第一个文档输入框的name是以_begin结束的，这里要简单处理掉
				if(name.indexOf("_begin")){
					name = name.substring(0,name.indexOf("_begin")); 
				}
				var tag = document.getElementById("emp_field_" + name);
				element_begin = tag.getElementsByTagName("INPUT")[1];
				element_end = tag.getElementsByTagName("INPUT")[2];
				
				valueObj.begin = element_begin.value;
				valueObj.end = element_end.value;
				
				return valueObj; 
			}catch(e){
			}
		}
		
		return value;
	};

	/**
	 * 将该数据域的取值复制到指定表单
	 * 
	 * @param form
	 *            指定表单DOM对象
	 */
	EMP.field.SearchDate.prototype.toForm = function(form, prefix) {
		var select_model_value = this.select_element.value; // 查询模式
		if (form == null)
			return; 
		var name = this.dataName;
		if (prefix != null)
			name = prefix + name;
		
		
		//区域查询模式
		if(select_model_value !=null && select_model_value == "70"){
			
			var valueObj = this.getValue();// 取值
			
			// 先toForm前一个输入框
			var value = valueObj.begin;
			var inputName = name + "_begin";
			var input = form[inputName];
			if (input == null) {
				input = form.document.createElement("input");
				input.type = "hidden";
				input.name = inputName;
				form.appendChild(input);
				form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
			}
			if (value != null && value != "")
				input.value = value;

			// 然后toForm后一个输入框
			value = valueObj.end;
			inputName = name + "_end";
			input = form[inputName];
			if (input == null) {
				input = form.document.createElement("input");
				input.type = "hidden";
				input.name = inputName;
				form.appendChild(input);
				form[inputName] = input;// 强制在form对象中将新建的隐藏域放入
			}
			if (value != null && value != "")
				input.value = value;
			
		}
		//非区间查询
		else {
			//EMP.field.Base.prototype.toForm.call(this);
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
			}
			
			EMP.util.Tools.log('EMP.data.Field',0
					,'数据域['+name+']值['+value+']被复制到表单['+form.id+'].');
			
		}
	};
};

/**
 * 查询框_下拉选项
 */
if (!EMP.field.SearchSelect) {

	EMP.field.SearchSelect = function() {
	};

	EMP.field.SearchSelect.prototype = new EMP.field.Base();

	EMP.field.SearchSelect.prototype._initialize = function() {

		this._parseAttribute("defMsg");

		// 查询框元素
		this.select_element = this.tag.getElementsByTagName("SELECT")[0];

		// 文本元素
		this.element = this.tag.getElementsByTagName("SELECT")[1];
		this.fakeinput = this.tag.getElementsByTagName("INPUT")[0];

		EMPTools.addEvent(this.element, "blur", this._defaultEventHandler, this);
		EMPTools.addEvent(this.element, "change",this._changeValueEventHandler, this);

	};

	EMP.field.SearchSelect.prototype._renderDisabled = function(disabled) {
		EMP.field.Base.prototype._renderDisabled.call(this, disabled);
		this.element.disabled = disabled;
	};

	EMP.field.SearchSelect.prototype.setValue = function(value) {

		value = EMPTools.trim(value);
		this.value = value;

		this.element.value = value;
		if (this.element.selectedIndex == -1) {
			this.element.value = ""; // 置为"请选择"的情况
			if (!this.config.flat)
				this.fakeinput.value = "";
		} else {
			if (!this.config.flat)
				this.fakeinput.value = this.element.options[this.element.selectedIndex].text;
		}

		if (this.relatedSelectDefine && this.relatedSelectDefine.next) {
			var nextSelect = this.relatedSelectDefine.next;
			// 如果置在"请选择"的选项上，则不需要向后台发起请求，直接将下一联动下拉框的选项清空
			if (this.element.selectedIndex == -1
					|| this.element.selectedIndex == 0) {
				var selectSrc = [];
				this.relatedSelectDefine.group.updateSelectInnerHTML(
						nextSelect, selectSrc);
			} else {
				this.relatedSelectDefine.group.doInitSelectContent(nextSelect);
			}
		}
	};

	EMP.field.SearchSelect.prototype.getDisplayValue = function() {

		return this.element.options(this.element.selectedIndex).text;
	};

	EMP.field.SearchSelect.prototype.getValue = function() {
		var search_model_value = this.select_element.value;// 下拉选项的值
		var value = this.element.value; // text输入框的值

		// 如果查询模式不为空，则value=查询模式_查询值==>让后台知道查询模式
		if (search_model_value != "undefined" && search_model_value != null
				&& search_model_value != "") {
			value = search_model_value + "_" + value
		}

		return value;
	};

	EMP.field.SearchSelect.prototype.doCheckLength = function() {
		if (!this.config.required)
			return EMPTools.message(true);
		if (this.element.value == null || this.element.value == "") {
			return EMPTools.message(false, "请选择一项作为" + this.title + "！");
		}
		return EMPTools.message(true);
	};

	/**
	 * 默认onblur方法
	 */
	EMP.field.SearchSelect.prototype._defaultEventHandler = function() {

		this.clearError();// 先清除原来的错误信息

		// 如果什么都没有输入，则不做任何校验，也不显示任何错误信息
		if (this.element.value == null || this.element.value == "") {
			return true;
		}

		// 只进行业务层次的校验
		var ret = this.doCheckBussiness();
		return ret;
	};

	/**
	 * 只适合于联动下拉框情况
	 */
	EMP.field.SearchSelect.prototype._changeValueEventHandler = function() {
		if (this.relatedSelectDefine && this.relatedSelectDefine.next) {
			this.setValue(this.getValue());
		}
	};

	/**
	 * 指定域添加一个按钮 @ id: 按钮ID title：标签 function：点击事件执行函数
	 */
	EMP.field.SearchSelect.prototype.addOneButton = function(id, title, fun) {
		var tempTag = this.tag;
		if (this.config.readonly) {
			// this.config.readonly=true;
			this._renderReadonly(true);
			if (tempTag.firstChild.readonly == undefined)
				tempTag.firstChild.disabled = false;
			else
				tempTag.firstChild.readonly = true;
		}
		;
		// tempTag.firstChild.style.
		// var pxSize=160-title.length*15;
		// tempTag.firstChild.style.width=pxSize+"px";
		var btn = document.createElement('BUTTON');
		btn.innerText = title;
		btn.id = id;
		btn.className = "emp_field_pop_button";
		btn.onclick = fun;
		tempTag.appendChild(btn);
		return btn;
	};
	
	/**
	 * 给指定的输入框添加 输入提示
	 * @ title:提示的内容  @ ggq
	*/
	EMP.field.Text.prototype.addPrompt = function(title) { 
		var tempTag=this.tag;  
        if(this.config.readonly){ 
           this.config.readonly=true; 
           this._renderReadonly(true); 
           if(tempTag.firstChild.readonly==undefined)
        	   tempTag.firstChild.disabled=false;
           else tempTag.firstChild.readonly=true;
        };   
	    var btn=document.createElement('span');
	    btn.style.color="blue";   
	    btn.innerText=title;
	    tempTag.appendChild(btn);
	    return btn;
	}
	
	/**
	 * 给指定的输入框添加 输入提示
	 * @ title:提示的内容  @ ggq
	*/
	EMP.field.Select.prototype.addPrompt = function(title) { 
		var tempTag=this.tag;  
        if(this.config.readonly){ 
           this.config.readonly=true; 
           this._renderReadonly(true); 
           if(tempTag.firstChild.readonly==undefined)
        	   tempTag.firstChild.disabled=false;
           else tempTag.firstChild.readonly=true;
        };   
	    var btn=document.createElement('span');
	    btn.style.color="blue";   
	    btn.innerText=title;
	    tempTag.appendChild(btn);
	    return btn;
	}
};
