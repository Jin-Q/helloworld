/**
 * 修改痕迹保留
 * 
 * Author: logicm
 * Date: 2011-04-24
 */
	function HashMap(){   
		/** Map 大小 **/  
		var size = 0;   
		/** 对象 **/  
		var entry = new Object();   
		/** 存 **/  
		this.put = function (key , value){   
			if(!this.containsKey(key)){   
				size ++ ;   
			}   
			entry[key] = value;   
		}   
		        
		/** 取 **/  
		this.get = function (key){   
			return this.containsKey(key) ? entry[key] : null;   
		}   

		 /** 删除 **/
		this.remove = function ( key ) {
			if( this.containsKey(key) && ( delete entry[key] ) ) {
				size --;
			}
		}
	        
		/** 是否包含 Key **/  
		this.containsKey = function ( key ) {   
			return (key in entry);   
		}   
		        
		/** 是否包含 Value **/  
		this.containsValue = function ( value ){   
			for(var prop in entry) {   
				if(entry[prop] == value){   
					return true;   
				}   
			}   
			return false;   
		}   
		        
		/** 所有 Value **/  
		this.values = function (){   
			var values = new Array();   
			for(var prop in entry){   
				values.push(entry[prop]);   
			}   
			return values;   
		}   
		        
		/** 所有 Key **/  
		this.keys = function (){   
			var keys = new Array();   
			for(var prop in entry) {   
				keys.push(prop);   
			}   
			return keys;   
		}   
		        
		/** Map Size **/  
		this.size = function () {   
			return size;   
		}   
		        
		/* 清空 */  
		this.clear = function () {   
			size = 0;   
			entry = new Object();   
		}
	}
	var mapOrignVal = new HashMap();
	var mapModifyVal = new HashMap();
    	    
	/** 初始化留痕事件
	 * 在window.onload时调用 
	 * @return
	 */
	function initTraceEvent(){
		for(var i = 0; i < page.objectsDefine.dataFields.length; i++){
			var _objId = page.objectsDefine.dataFields[i];
			var _val = new Array();
			_val[0] = eval($('#'+_objId)).getValue(); //实际值
			_val[1] = eval($('#'+_objId)).getText();//显示值$('#'+_objId).getText()
			mapOrignVal.put(_objId, _val);
		}
    	        
		for(var i = 0; i < page.objectsDefine.dataFields.length; i++){
			var _objId = page.objectsDefine.dataFields[i];
			var _var = mapOrignVal.get(_objId);
			var oldValue = _var[0];
			$('#' + _objId).bindOnChange(doChangeEvent4Trace);   
		}
	}

	/** 
	* 处理页面中域的ONCHANGE事件，实现留痕操作————记录修改后的值至缓存之中 
	* @param _e
	* @return
	*/
	function doChangeEvent4Trace(newValue, oldValue){
		var _objId = $(this).attr('id');
		//部分标签需要重新取值newValue,原传入参数为undefined本段代码未判断直接重新取值了
		var newValue = eval($('#'+_objId)).getValue();
		if(newValue != oldValue){
			var _newvar = new Array();
			_newvar[0] = newValue;//修改后实际值
			_newvar[1] = eval($('#'+_objId)).getText();//修改后显示值
			mapModifyVal.put(_objId, _newvar);
		} else if(mapModifyVal.containsKey(_objId)){
			if(newValue == oldValue){///删除又改回原值的
				mapModifyVal.remove(_objId);
			}
		}
	}
	
	/**
	 * 页面加载时将标签的初始值记录
	 * @param _pk:
	 * @param _sid:
	 * @param _page:
	 * @return
	 */
	function loadTraceRecord(_pk, _sid, _page){
		if(_pk == '' || _pk == null){
			return ;
		}
		traceForm.innerHTML = ''; //清空FORM
		var _inputPage = document.createElement("input");
		//域名
		_inputPage.type="hidden";
		_inputPage.name = "pageUrl";
		_inputPage.id = _inputPage.name;
		_inputPage.value =  _page;
		traceForm.appendChild(_inputPage);     
		
		var _inputPk = document.createElement("input");
		//记录主键值
        _inputPk.type="hidden";
        _inputPk.name = "pkVal";
        _inputPk.id = _inputPk.name;
        _inputPk.value = _pk;
         
        traceForm.appendChild(_inputPk);
        var url = contextPath + "/loadTrace4field.do?EMP_SID="+_sid;
        $.ajax({ 
        	type: "POST", 
        	url: url, 
        	data :  {'pkVal':_pk,'pageUrl':_page},
        	async:false, 
        	dataType:"html",
        	success: function(data) { 
        		try {
        			var jsonstr = eval("("+data+")");
        		} catch(e) {
        			EMP.alertException(data);
        			//$.messager.alert('错误',data,'error');
        			return;
        		}
        		// 在页面上有修改记录的字段后面添加小u
        		for(var n = 0; n < jsonstr.traceList.length; n++){
        			var _label = jsonstr.traceList[n].m_datetime + ":被用户[" + jsonstr.traceList[n].usr_id + "]";
        			_label += "\n\r从[" + jsonstr.traceList[n].m_old_disp_v + "]修改为[" + jsonstr.traceList[n].m_new_disp_v + "]";
        			$('#field_'+jsonstr.traceList[n].m_field_id).after("<a  id='U_"+jsonstr.traceList[n].m_field_id+"' href='javascript:doShowTraceByPk(\"" + jsonstr.traceList[n].m_field_id + "\");' title='" + _label + "'><font color='RED'>&nbsp;&nbsp;U</font></a>")
        			if($('#field_'+jsonstr.traceList[n].m_field_id).is(':hidden')){
        				$("#U_"+jsonstr.traceList[n].m_field_id).hide();
        			}
        		}
        	}
        });  
	}

	/**
	 * 保存痕迹记录 
	 * 在页面保存、确认、提交时调用
	 * @param _pk
	 * @param _sid
	 * @param _page
	 * @param _recall
	 * @return
	 */
	function saveTraceRecord(_pk,_sid, _page, _recall){
		if(_pk == '' || _pk == null){
			return ;
		}
		traceForm.innerHTML = ''; //清空FORM
		var _inputPage = document.createElement("input");
		//域名
		_inputPage.type="hidden";
		_inputPage.name = "M_PAGE_URL";
		_inputPage.id = _inputPage.name;
		_inputPage.value =  _page;
		traceForm.appendChild(_inputPage);  
 		   
		var mv = mapModifyVal.keys();
		for(var i=0; i<mv.length; i++){
			var _objId = mv[i];
			var _inputPk = document.createElement("input");
			//记录主键值
			_inputPk.type="hidden";
			_inputPk.name = "MODI["+i+"].M_PK_V";
			_inputPk.id = _inputPk.name;
			_inputPk.value = _pk;
			traceForm.appendChild(_inputPk);   
			var _inputNm = document.createElement("input");
			//域名
			_inputNm.type="hidden";
			_inputNm.name = "MODI["+i+"].M_FIELD_ID";
			_inputNm.id = _inputNm.name;
			_inputNm.value = _objId;
			traceForm.appendChild(_inputNm);             
 	             
			var _inputNm = document.createElement("input");
			//域名
			_inputNm.type="hidden";
			_inputNm.name = "MODI["+i+"].M_FIELD_NM";
			_inputNm.id = _inputNm.name;
			if($('#'+'label_'+_objId).html()){
			var value = $('#'+'label_'+_objId).html();
				_inputNm.value = value.substring(0,value.length-1);
			}else{
				var spanGroup = $('#'+_objId).closest('span[id=field_'+_objId+']').parent();
				var field_group = spanGroup.attr('id');
				var groupID = field_group.substring(6,field_group.length);
				var label = $('#label_'+groupID).html();
				var value = label;
				_inputNm.value = value.substring(0,value.length-1);
			}
			traceForm.appendChild(_inputNm);             
			var _input = document.createElement("input");
			//新值
			_input.type="hidden";
			_input.name = "MODI["+i+"].M_NEW_V";
			_input.id = _input.name;
			_input.value =  eval($('#'+_objId)).getValue();
			traceForm.appendChild(_input);

			var _inputD = document.createElement("input");
			//新值(显示)
			_inputD.type="hidden";
			_inputD.name ="MODI["+i+"].M_NEW_DISP_V";
			_inputD.id = _inputD.name;
			_inputD.value = eval($('#'+_objId)).getText();
			traceForm.appendChild(_inputD);
 	 			
			var _input_old = document.createElement("input");
			//旧值
			_input_old.type="hidden";
			_input_old.name = "MODI["+i+"].M_OLD_V";
			_input_old.id = _input_old.name;
			_input_old.value =  mapOrignVal.get(_objId)[0];
			traceForm.appendChild(_input_old);

			var _inputD_old = document.createElement("input");
			//旧值(显示)
			_inputD_old.type="hidden";
			_inputD_old.name = "MODI["+i+"].M_OLD_DISP_V";
			_inputD_old.id = _inputD_old.name;
			_inputD_old.value = mapOrignVal.get(_objId)[1];
			traceForm.appendChild(_inputD_old);
		}
		var jData = $('#traceForm').toJsonData();	
		var url = contextPath + "/saveTraceLog.do?EMP_SID="+_sid;	
		$.ajax({ 
			type: "POST", 
			url: url, 
			data : jData,
			async:false, 
			dataType:"html",
			success: function(data) { 
				try {
					var jsonstr = eval("("+data+")");
					return;
				} catch(e) {
					EMP.alertException(data);
					// $.messager.alert('错误',data,'error');
					return;
				}
			}
		}); 
	}