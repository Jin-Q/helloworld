/**
 * page init component
 * @return
 */
var globalComb=null;

function initpage(url_,hideObject){
	try{
		
		var ds = new Ext.data.Store({
		    proxy: new Ext.data.HttpProxy({
		    	url: url_
		    }),

		    reader: new Ext.data.JsonReader({
		        root: 'topics',
		        totalProperty: 'totalCount',
		        id: 'userid'
		    }, [
		        {name: 'userid', mapping: 'actorno'},
	            {name: 'username', mapping: 'actorname'},
	            {name: 'orgname', mapping: 'orgid_displayname'},
		    ])
		    
		});
		
		//暂时不做任何提示
		/*
		ds.on('load', function(store,records,options){
			if(store.getTotalCount()==0){
				alert('您输入的关键字查询结果为0!');
			}
		});
		*/
		
		Ext.QuickTips.init();
		
		var comb = new Ext.form.ComboBox
	    ({    
	    	tpl: '<tpl for="."><div ext:qtip="{username} {userid}/{orgname}" class="x-combo-list-item">{username} {userid}/{orgname}</div></tpl>',
	        id:"ComboBox_other",
	        fieldLabel:'username',
	        editable:true,//默认为true，false为禁止手写和联想功能
	        store:ds,
	        emptyText:'请下拉选择或输入姓名查询',
	        mode: 'remote',//指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载则为remote.默认值为：remote
	        triggerAction: 'all',
	        valueField:'userid', 
	        displayField:'username',
	        pageSize:10,
	        selectOnFocus:true,
	        renderTo:'selUsers',
	        width:250,
	        typeAhead:false,
	        frame:true,
	        resizable:true,
	        minChars : 0, //查询框的默认数目
	        queryDelay:500,//查询框输入完成半秒后,自动提交查询
	        //最终返回的value值
	        map:new JHashMap(),
	        onSelect: function(record){     
	    		//this.setValue(record.data.orgname+'/'+record.data.username+'_'+record.data.notesid);
	    		
	    	    this.setValue(record.data.username+' '+record.data.userid+'/'+record.data.orgname);
	    		//因为是单选,所以将之前所选数据清空
	    		this.map.clear();
	    		this.map.put(record.data.userid,record.data.userid);
	    		
	    		if(hideObject){
		    		hideObject.value = record.data.userid;
	    		}
	    		this.collapse();
	    	}
			
	    });

		globalComb = comb;
	}catch(e){
		
	}
}