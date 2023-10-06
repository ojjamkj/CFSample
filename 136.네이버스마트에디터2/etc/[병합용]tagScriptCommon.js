/* version $Id: tagScriptCommon.js,v 1.37 2019/06/14 01:14:45 ojjamkj Exp $*/

function getElement(ID)
{
	if (document.all)  return document.all(ID);
	if(document.getElementById) return document.getElementById(ID);
}
function chkAfterSubmit() 
{
	var dateObjs = jQuery(".input_date")
	var temp;
	for( var i=0; i <dateObjs.length; i++)
	{
		if(dateObjs[i].value  != null && dateObjs[i].value.length == 8)
		{
			temp = dateObjs[i].value.substring(0, 4) + "-" +dateObjs[i].value.substring(4, 6) + "-" + dateObjs[i].value.substring(6,8)
			dateObjs[i].value = temp
		}
	}
}
var wfSelObj = new Object();
function changeWorkDep(selId, obj)
{	
	wfSelObj.DEP_ID = jQuery(obj).parent().find("#"+ selId)[0];
	openOrgaTreeCall( 'setWorkDep' , selId );
}

function setWorkDep ( depinfo , selId  ) {
	wfSelObj.DEP_ID.value = depinfo.depid ;
	jQuery(jQuery(wfSelObj.DEP_ID).parents("TD")[0]).find("#" +  selId+"_NM").html(depinfo.depname) ;
	
}
 
function openBizTreePopUp( selId, obj ) 
{
 	wfSelObj.BIZ_ID = jQuery(obj).parent().find("#"+ selId)[0];
 	var callBack = function(e){
		var returnBizTreeObj =e.data;
    	setBizId( returnBizTreeObj.BIZ_ID, returnBizTreeObj.BIZ_LABEL, selId ) ; 
 	}
 	window.addEventListener("message", callBack, false);

 	var returnBizTreeObj = openModal( "/bizloc.do?cmd=tree_popup&isUserRequest=Y&isModal=M", window , 350, 410, "dialogWidth:350px; dialogHeight:410px ; scroll:1; help:1; status:0", true ) ;
 	if(document.getElementById('dialog-close')){ //chrome
 		document.getElementById('dialog-close').addEventListener('click', function(e) {
	       	e.preventDefault();
	        window.removeEventListener("message",callBack);
	    });	 	
 	}else{//window
		window.removeEventListener("message",callBack);
 	}

}


 
function openMultiBizTreePopUp( selId ) {
	var callBack = function(e){
		var returnBizTreeObj =e.data;
    	setMultiBiz( returnBizTreeObj.BIZ_ID, returnBizTreeObj.BIZ_LABEL , selId ) ; 
 	}
 	window.addEventListener("message", callBack, false);


	var returnBizTreeObj = openModal( "/bizloc.do?cmd=multi_tree_popup&isUserRequest=Y&isModal=Y",  getCurrentMultiBiz(selId) , 550, 410, "dialogWidth:350px; dialogHeight:410px ; scroll:1; help:1; status:0", true ) ;
	if(document.getElementById('dialog-close')){ //chrome
 		document.getElementById('dialog-close').addEventListener('click', function(e) {
	       	e.preventDefault();
	        window.removeEventListener("message",callBack);
	    });	 	
 	}else{//window
		window.removeEventListener("message",callBack);
 	}
}
function getCurrentMultiBiz(selId)
{
	var obj = new Object();
	obj.BIZ_ID_NM=getElement( selId + "_NM").innerText;
	var ids = getElement( selId);
	var idStr="";
	
	try{
		idStr = ids.value;
	}catch(e){}
	
	for( var i=0; ids!= null && i < ids.length ; i++)
	{
		if(i==0){ idStr = ""; }
		if(i>0) idStr+=",";
		idStr += ids[i].value;
	}
	obj.BIZ_ID=idStr;
	return obj;
}
function chkDate()
{
	var dateObjs = jQuery(".input_date")
	for( var i=0; i < dateObjs.length; i++)
	{
		var inputVal = dateObjs[i].value
		if(inputVal=="") continue;
		inputVal = removeDash(inputVal);
		if(isValidDate(inputVal) == false)
		{
			alert("invalid date");
			dateObjs[i].focus();
			return false;
		}
	}
	for( var i=0; i < dateObjs.length; i++)
	{
		var dateName = dateObjs[i].name; 
		if(dateName.indexOf("_FROM")>0)
		{
			var toDateName=dateName.substring(0, dateName.length-5) + "_TO"
			var toDateObj = jQuery(dateObjs[i]).parent().find("#" + toDateName)
			if(toDateObj != null && toDateObj.length>0)
			{
			 
		 		if( ! isPastDay(toDateObj[0], dateObjs[i].value , finishThanStartMsg) )
		 		{
		 			dateObjs[i].focus();
			 		 return false;
		 		}
			}
		}
	}
	
	return true;
}
function setMultiBiz( idObj, nmObj,  selId ){
	var id ="";
	var text ="";
	var hiddenHtml = ""
	for(var i=0;i<idObj.length;i++){
		id = id + idObj[i]+","
		text = text + nmObj[i]+","
		hiddenHtml += "<input type=hidden name=" + selId + " value=" + idObj[i] + ">\n";
	}
	 
	if(idObj.length > 0){
		id = id.substring(0,id.length-1);
		text = text.substring(0,text.length-1);
		getElement(selId+"_HIDDEN_AREA").innerHTML=hiddenHtml;
		getElement(selId+"_NM").innerText =text ;
	 
		try
		{
			changeReceiveUserAndDepByBizIds(id);
		} 
		catch(e) { } 
	 
	}
	
}

function openBizTreePopWithAuth( selId, obj ) 
{
	wfSelObj.BIZ_ID = jQuery(obj).parent().find("#"+ selId)[0];

	var callBack = function(e){
		var returnBizTreeObj =e.data;
    	setBizId( returnBizTreeObj.BIZ_ID, returnBizTreeObj.BIZ_LABEL, selId ) ; 
 	}
 	window.addEventListener("message", callBack, false);

 	var returnBizTreeObj = openModal( "/bizloc.do?cmd=tree_popup&isUserRequest=Y&isModal=M&AUTH_TP_CD=3102", window , 350, 410, "dialogWidth:350px; dialogHeight:410px ; scroll:1; help:1; status:0", true ) ;
 	if(document.getElementById('dialog-close')){ //chrome
 		document.getElementById('dialog-close').addEventListener('click', function(e) {
	       	e.preventDefault();
	        window.removeEventListener("message",callBack);
	    });	 	
 	}else{//window
		window.removeEventListener("message",callBack);
 	}
}

function chkBeforeSubmit(input)
{
	var returnParam =  jQuery("#RETURN_PARAM");
	if(returnParam.length>0)
	{
		returnParam[0].value="flag=" + input;
	}
	jQuery(".all_select_box").find("option").attr("selected","true")
	if(!chkDate()) return false;
	if(!chkMultiFile()) return false;
 
	if(!chkMaxLength()) return false;
 
	if(input=='approve')
	{
		var requireObj = jQuery('*[required=true]')
	 	var requireObjSize = requireObj.length;
		for( var i=0; i < requireObjSize; i++)
		{
			var val =requireObj[i].value;
			var inputName = requireObj[i].name;
			if(  requireObj[i].type=="checkbox" )
			{
				val = returnCheckedValue(requireObj[i].name);
			}else if(requireObj[i].type=="radio"){
				val = $(":radio[name='"+requireObj[i].name+"']:checked").val();
			}
			else if(requireObj[i].tagName=="SELECT")
			{
				val = jQuery(requireObj[i]).val()
			}
			else if(requireObj[i].tagName=="TEXTAREA" && requireObj[i].className=="summernote")
			{
				val=getEdiorValue(requireObj[i]);
				if ($(requireObj[i]).summernote('isEmpty')) {
				  val="";
				}
			}
			else if(requireObj[i].tagName=="TEXTAREA" && requireObj[i].className=="gt_se2")
			{
				if (val =="<p><br></p>" || val =="<br>") {
				  val="";
				}
			}
			else if(requireObj[i].type=="hidden")
			{	
				if(inputName.indexOf("ATTACH_")==0) {
					var allrowDelete = $(requireObj[i]).closest('table').attr('allrowdelete');
					if(allrowDelete=='true'){
						val="-";
					}else if(val=="" || val==null){
						var tableId = $(requireObj[i]).closest('table').attr("id").replace("_table","");
						alert( $(requireObj[i]).parents('tr').find("*[id='title_"+tableId+"']").text() + notNullMsg);
						try{requireObj[i].focus();}catch(e){}
						return false;
					}
				}else{
					val = getEdiorValue(requireObj[i]);
				}
			}
			else if(requireObj[i].type=="file")
			{
				 if(val=="" || val==null)
				 {
					 var oldName = requireObj[i].name.substring(0, requireObj[i].name.length-4) + "_OLD";
					 var oldObj = $(requireObj[i].parentNode.parentNode).find("*[name="+oldName+"]");
					 if(oldObj !=null)
					 	val = $(requireObj[i].parentNode.parentNode).find("*[name="+oldName+"]").val();
				 }
			}	
			else if(requireObj[i].tagName=="SPAN")
			{
				var tempObj = jQuery(requireObj[i]).find("input")
			 
				if(tempObj.length>0)
				{
					val = tempObj[0].value
					inputName = tempObj[0].name
				}
				else
				{
					val = "";
					inputName = requireObj[i].id
					var tIdx = inputName.indexOf("_HIDDEN_AREA");
					if(tIdx>0)
					{
						inputName = inputName.substring(0,tIdx);
						 
					}
				}
			}
			if(val=="" || val==null)
			{				
				//필수값이 첨부파일V2의 요소인 경우, 첨부파일이 첨부되지 않았고, 첨부파일이 필수가 아니면, 필수값 체크를 스킵한다.
				var multiFileTable = $(requireObj[i]).closest('table .multifile_table');
				if(multiFileTable.length > 0){ ////필수값이 첨부파일의 요소인가?
					var attachId = $(requireObj[i]).closest('tr').find('input[name=ATTACH_ID]'); //첨부파일 V2여부 판단, 필수값이 속한 row에서 ATTACH_ID를 찾는다. 존재하면 V2
					if( attachId.length > 0 && attachId.val()==""){ //첨부파일이 V2이면서 첨부가 되지 않은 경우 필수값 체크를 스킵한다.
						//if(!multiFileTable.parent().prev().hasClass("wf_title_required")){ //첨부파일이 필수가 아닌가? title의 class로 판단. Nullable:Yes
						if($(requireObj[i]).closest('table').attr('allrowdelete')=='true'){ //첨부파일이 필수가 아닌가? table의 allrowdelete로 판단. REQUIRED:false
							continue; //스킵
						}
					}
				}
				
				alert( getAlertTitle(inputName )  + notNullMsg);
				try
				{
					requireObj[i].focus();
				}
				catch(e)
				{

				}
				return false;
			}
		}
	}
	else
	{
		var chgTitleObj = jQuery('#CHANGE_TITLE')
		if(chgTitleObj.length==1)
		{
			if(chgTitleObj[0].type=="text" && chgTitleObj[0].value=="" )
			{
				alert( getAlertTitle( chgTitleObj[0].name)  + notNullMsg);
				return false
			}
		}
	}
	var numObj = jQuery('.input_number')
	for( var i=0; i < numObj.length; i++)
	{
		if(numObj[i].value!="" && !isNumber(numObj[i].value))
		{
			alert( jQuery("#title_" + numObj[i].name).text() + notNullMsg);
			numObj[i].focus();
			return false;
		}
	}
	var dateObjs = jQuery(".input_date")
	
	for( var i=0; i < dateObjs.length; i++)
	{
		var temp = dateObjs[i].value.replace(/-/gi,"")
		var dateName = dateObjs[i].name; 
	
		var hhObj = getElement(dateName + "_HH")
		if(temp != "" && hhObj != null)
		{
			temp +=  (hhObj.value==""?"00":hhObj.value);
			var mmObj = getElement(dateName + "_mm")
			if(mmObj != null)
			{
				temp += (mmObj.value==""?"00":mmObj.value);
				var ssObj = getElement(dateName + "_ss")
				if(ssObj != null)
				{
					temp += (ssObj.value==""?"00":ssObj.value);
				}
			}
			
		}
		 
		dateObjs[i].value = temp;
		
	}
	clearAttach()
	clearMultiInput()
	//return false
	return true
}
/*한글 및 특수문자( db utf-8 한글 3byte, new line 2byte, the other 1byte*/
function chkMaxLength()
{
	
	var obj = jQuery("textarea[maxLength]")
 
	for( var i=0; i < obj.length ; i++)
	{
		var userByte = checkValueByte(obj[i].value);
		if(obj[i].maxLength< userByte)
		{
			alert( getAlertTitle( obj[i].name)  + maxLengthMsg + "("+ userByte +"/" + obj[i].maxLength + ")");
			return false;
		}
	}
	obj = jQuery(":hidden").filter("[maxLength]")
	 
	for( var i=0; i < obj.length ; i++)
	{
		var userByte = checkValueByte(getEdiorValue(obj[i]));
		if(obj[i].maxLength< userByte)
		{
			alert( getAlertTitle( obj[i].name)  + maxLengthMsg + "("+ userByte +"/" + obj[i].maxLength + ")");
			
			return false;
		}
	}
	
	obj = jQuery(":text").filter("[maxLength]")
	 
	for( var i=0; i < obj.length ; i++)
	{
		var userByte = checkValueByte(obj[i].value);
		if(obj[i].maxLength< userByte)
		{
			alert( getAlertTitle( obj[i].name)  + maxLengthMsg + "("+ userByte +"/" + obj[i].maxLength + ")");
			return false;
		}
	}
	return true;
}

function checkValueByte(val){
	var codeByte = 0;
	for(var idx=0; idx<val.length; idx++)
	{
		var oneChar = escape(val.charAt(idx));
		if(oneChar.length==1) codeByte++;
		else if(oneChar.indexOf("%u") != -1) codeByte+=3;
		else if(oneChar=="%0A") codeByte+=2;
		else if(oneChar.indexOf("%") != -1) codeByte++;
	}

	return codeByte;
}


 
function populateDateField1( calendar, fieldId, calendarId )
{
	
	//var date = calendar.getDate().toString();
	 
	var date = calendar.getFormattedDate().toString();
	 
	jQuery(wfSelObj.CAL).val(date.substring(0, date.indexOf(" 00:00:00")))
	  jQuery("#" + calendarId ).css("visibility",  "hidden")
 
	
	
} 
function setBizId( bizid, bizLabel , selId ) {
	 
	wfSelObj.BIZ_ID.value = bizid ;
	 
	jQuery(jQuery(wfSelObj.BIZ_ID).parents("TD")[0]).find("#" +selId+"_NM").text(bizLabel) ;
	try
	{
		changeReceiveUserAndDep(bizid);
	} 
	catch(e) { }
	
	
}
function getAlertTitle( objName ) 
{
	var alertTitle= jQuery("#title_" + objName).text()
	if(alertTitle == "")
	{		 
		alertTitle = jQuery("#" + objName).parent().prev().text()
	}
	return alertTitle
}
function getEdiorValue( hiddenObj )
{
	try
	{
		$('#'+hiddenObj.name+'__Frame')[0].contentWindow.FCK.UpdateLinkedField();
		var val =hiddenObj.value.trim();
		if(val == "<br />" || val=="&nbsp;")
		{
			val="";	
		}
		return val;
	}
	catch(e)
	{
		return hiddenObj.value;
	}
	
}
function setEdiorValue( name, newVal )
{
	try
	{
		eval(name + "__Frame.FCK.UpdateLinkedField()")
		jQuery("#" + name).val(newVal);
		 
	}
	catch(e)
	{
	 
	}
	
} 
var removeObj = new Object();
function getValue(obj)
{
	var type = obj.attr("type")
	if(type=="radio" || type=="checkbox")
	{
		if(obj.is(":checked"))
		{
			return obj.val();
		}
		else
		{
			return "";
		}
	}
	else
	{
		return obj.val();
	}
}
function chkMultiFile()
{
	var tables = jQuery(".multifile_table");
	var result=true;
	for( var i=0; i < tables.length; i++)
	{

		jQuery(tables[i].rows).each( function (k)
				{
					var row1 = jQuery(this)[0];
					var fileObjNull=true; 
					var fileObj = null;
					var hasVal=false;
					var idx=0;
					 jQuery(this).find("input").each(function (i)
						{
						 var $obj = jQuery(this);

						 if($obj.attr("type")!="button" && getValue( $obj ) != "" && $obj.attr("name").indexOf("_SEQ")<0)
						 {
							 
							 if($obj.attr("type")=="file"  )
						 	 {
								 fileObjNull=false;
								
						 	 }
							 if($obj.attr("name").indexOf("_OLD")>0  || $obj.attr("name").indexOf("ATTACH_ID")>-1 )
						 	 {
								 fileObjNull=false;
						 	 }
						 	 hasVal=true;
						 }
							idx++;
						}
					)
					 
					if(idx>0 && hasVal && fileObjNull)
					{
						
						alert(   getAlertTitle( fileObj.attr("name") ) +  notNullMsg);
						fileObj[0].focus();
						result = false;
						return  ;
					}
				}//input
		)//row

	}//table
	return result;
}
function clearMultiInput()
{
       var tables = jQuery(".multiinput_table, .multitable_table");

       for( var i=0; i < tables.length; i++)
       {
             jQuery(tables[i].rows).each( function (k)
            		 {
                                 var row1 = jQuery(this)[0];
                                 hasVal=false;

                                 var idx=0;

                                 jQuery(this).find("input,select").each(function (i)
                                 {
                                              if(jQuery(this).attr("type")!="button" && getValue( jQuery(this) ) != "")
                                              {
                                                     hasVal=true;
                                              }
                                              idx++;
                                        }
                                 )
                                 if(idx>0 && !hasVal )
                                 {
                                        tables[i].deleteRow(row1.rowIndex );
                                 }
                           }
             )
       }

}

function resetAllAttach()
{
	var tables = jQuery(".multifile_table");
	
	for( var i=0; i < tables.length; i++)
	{
		var idx = tables[i].rows.length
		for( var j=1; j <idx ; j++)
		{
			if(j==1)
			{
				resetAttachFile1(tables[i].rows[1])
			}
			else
			{
				tables[i].deleteRow(j);
			}
		}
		 
	}
}
function clearAttach()
{
	
	var tables = jQuery(".multifile_table");
	for( var i=0; i < tables.length; i++)
	{
	 
		if(tables[i].allRowDelete )
		{
			if(jQuery(tables[i]).find("INPUT[type='file']").val()=="")
			{
				
			 
				if(	jQuery(tables[i]).find("*[required=true]").length ==0)
				{
				 
				tables[i].deleteRow(1);
				} 
			}
			 
		}
		else  if(tables[i].rows.length>0 )
		{
			if( $(tables[i]).find("input#ATTACH_ID").length == 0 //첨부파일이 V2가 아니면
				&& tables[i].rows[0].cells.length==3)
			{
				var fileObj = jQuery(tables[i]).find("INPUT[type='file']")
			
				for( var k=0; k < fileObj.length ;k++)
				{
					var fileObj1 = fileObj[k]
					var oldName = fileObj1.name.substring(0, fileObj1.name.length-4) + "_OLD";
					var row = jQuery( fileObj1).parents("TR")[0];
					if(fileObj1.value=="")
					{
					    if(jQuery(row).find("INPUT[name='" + oldName + "']").val()){}else{tables[i].deleteRow(row.rowIndex);}
						if(jQuery(row).find("INPUT[name='" + oldName + "']").val()=="")
							tables[i].deleteRow(row.rowIndex);
				  
					}
				}
			}
			else
			{
				
				jQuery(tables[i].rows).each( function (k)
				{
					var row1 = jQuery(this)[0];
					//if(row.rowIndex ==0) continue;
					hasVal=false;
					var idx=0;
					 jQuery(this).find("input").each(function (i)
					{
							if(jQuery(this).attr("type")!="button" && getValue(jQuery(this) ) != "")
							{
								hasVal=true;
							}
							idx++;
						}
					)
					 
					if(idx>0 && !hasVal )
					{
						 
						tables[i].deleteRow(row1.rowIndex );
					}
					 
				})
			}
		}
	}
}

function onDeleteAttachFile( e ) {
	if(	jQuery(e.srcElement).parent().parent().find("*[required=true]").length !=0 && jQuery(e.srcElement).parent().parent().parent().children().length == 1) {
		var fileObj = jQuery(e.srcElement).parent().parent().find("INPUT[type='file']");
		var inputName= fileObj.attr("name");
		alert( getAlertTitle(inputName)  + notNullMsg);
		return;
	}
	
	onDeleteAttachIdCallback(e);
}

function onDeleteAttachIdCallback(e){
	var targEl;
	if ( !e ) e = window.event;
	if (e.target) {
		targEl = e.target;
	}
	else if (e.srcElement) {
		targEl = e.srcElement;
	}
	
	var pEl = null; 
	try { pEl = targEl.parentNode.parentNode ; } catch ( e ) { }
	
	if ( pEl != null ) {
		var tbodyObj = pEl.parentNode ; 
		var len = tbodyObj.rows.length; 
		tbodyObj.removeChild( pEl ) ; 
	}
}
function onDeleteAttachId( e , tableId) {
	
	var fileObj = $(e.srcElement).parent().parent().find("input[name$='ATTACH_ID']");
	var allRowDelete = $("#"+tableId).attr("allrowdelete");
	
	if(allRowDelete=="false" && $("#"+tableId).children('tbody').children('tr').length==1){
		var inputName= fileObj.attr("name");

		var tableId = fileObj.closest('table').attr("id").replace("_table","");
		alert( fileObj.parents('tr').find("*[id='title_"+tableId+"']").text() + cannotDeleteMsg);
		return;
	}

	var e1= e;
	if(fileObj.val() != ""){
		var param= {"ATTACH_ID": fileObj.val()};
		new CFActionRun().run({
			actionParam : param,
			runAction:"com.gtone.cfclient.actions.attach.AttachFileDeleteAction",
			completedEvent:function(actionParam, jsonResultData){
				onDeleteAttachIdCallback(e1);
			}
		});
	}else{
		onDeleteAttachIdCallback(e);
	}
}

function resetAttachFile1(tr)
{
	var len = tr.cells.length;
	if($(tr).find("input[type='FILE']").length > 0 )
	{
		for(var i = 0 ; i < len ; i ++ ) {
		
			var cell=tr.cells[i];
			if(i==len-2)
			{
				var h =cell.innerHTML;
				var pos = h.lastIndexOf(">")
				h = h.substring(0, pos)  + " onchange=setFileName(this)>"
				cell.innerHTML = h ;
			}
			$(cell).css("bgColor","white");
		}
	}
	
	multiValInput(tr);
}

function onAddAttachFile( tabName, prefix ) {
	 
	var tabObj = document.getElementById(tabName);
 
	tabObj.allRowDelete=false;
	 
	jQuery("#" + tabName).append("<tr>" + removeObj[tabName] + "</tr>")	;
	var tr = tabObj.rows[tabObj.rows.length-1];
	resetAttachFile1(tr);
 
}

function onAddAttachFileCallback( tabName, attachGroupCode, fieldName, attachObjs ) {
	 
	var tabObj = document.getElementById(tabName);
 
	tabObj.allRowDelete=false;
	 
	var tr = tabObj.rows[tabObj.rows.length-1];
	var create=true;

	if( $(tr).find("*[id='ATTACH_ID']").val() == ""){
		create=false;
	}

	$.each(attachObjs, function(index, data){
		if(create){
			$("#" + tabName).append("<tr>" + removeObj[tabName] + "</tr>")	;
			tr = tabObj.rows[tabObj.rows.length-1];	
		}else{
			create=true;		
		}

		multiValInput(tr);
		$(tr).find("*[name='ATTACH_ID']").val(data.ATTACH_ID);
		$(tr).find("*[name='ATTACH_GROUP']").val(data.ATTACH_GROUP);
		$(tr).find("*[id='ATTACH_NM_AREA']").html('<i class="fa fa-floppy-o" aria-hidden="true"></i><a class="file" href="javascript:onDownloadAttachId(\''+data.ATTACH_ID+'\');"> '+data.ATTACH_NM);
	});
}
function onDeleteMultiInput( e ) {
	
	var targEl;
	if ( !e ) e = window.event;
	if (e.target) {
		targEl = e.target;
	}
	else if (e.srcElement) {
		targEl = e.srcElement;
	}
	
	var pEl = null; 
	try { 
		pEl = targEl.parentNode.parentNode ;
		
	} catch ( e ) { }
	
	if ( pEl != null ) {
		if(pEl.rowIndex == 0) return;
		var tbodyObj = pEl.parentNode ; 
		var len = tbodyObj.rows.length; 
		var tabObj = tbodyObj.parentNode;
		
		tbodyObj.removeChild( pEl ) ; 
	 
		reOrderSeq( tabObj.id)
		 
		
	}
}

function reOrderSeq(tblName)
{
	var seqArea = jQuery("#" + tblName + " *[sequence=true]");
	var idx=1;
 
	for( var i=0; i < seqArea.length ; i++)
	{
		if(seqArea[i].tagName=="INPUT")
		{
			seqArea[i].value=idx;
		}
		else if(seqArea[i].tagName=="SPAN")
		{
			seqArea[i].innerText=idx;
			idx++;
		}
	}
}
var addCnt =0;
function multiValInput(tr)
{
	
	$(tr).find("[nocopy=true]").remove();
	
	
	var inputs = $(tr).find("INPUT");
	addCnt ++;
	for( var i=0; i< inputs.length; i++)
	{
		 
		if(inputs[i].type=="radio" || inputs[i].type=="checkbox")
		{
			var orgId = inputs[i].id;
			inputs[i].id =inputs[i].id+"_" + addCnt ;
			inputs[i].name =inputs[i].name+"_" + addCnt ;
			 
			if(inputs[i].type=="radio")
			{
				if(inputs[i].checked){
					inputs[i].checked=false;
					var orgValue = $("#"+orgId).parent().find(":hidden").val();
					$("#"+orgId+"[value='"+orgValue+"']").prop("checked", true);
				}
				inputs[i].onclick=setRadioVal;
			}
			else
			{
				inputs[i].onclick=setCheckVal;
			}
		}
		else if(inputs[i].type=="select")
		{
			$(inputs[i]).prop("selectedIndex", 0)	
		}
		else if(inputs[i].type=="text" || inputs[i].type=="hidden")
		{
			if(inputs[i].name.indexOf("ATTACH_") > -1){
				inputs[i].id =inputs[i].id;
			}else{
				inputs[i].id =inputs[i].id+"_" + addCnt ;
			} 
			
			inputs[i].value="";
			if($(inputs[i]).hasClass("hasDatepicker")){
				$(inputs[i]).parent().find("img").remove();
				$(inputs[i]).parent().find("script").remove();
				$(inputs[i]).removeClass("hasDatepicker");
				$(inputs[i]).datepicker();
			}
		}
	}
	
	jQuery(tr).find("TEXTAREA").each(function(){
		$(this).val("");
	});
	
	jQuery(tr).find("SELECT").each(function(){
		$(this).prop("selectedIndex", 0);
	});
	 
}
function getMaxRowLen(tabObj)
{
	if(tabObj.rows.length ==0) return 1;
	var tdClassName = tabObj.rows[0].cells[0].className;
	if(tdClassName != null && tdClassName.indexOf("title")==0)
	{
		return 2;
	}
	return 1;
}
function onAddMultiInput( tabName, prefix ) {
	 
	var tabObj = document.getElementById(tabName);
	
   

	tabObj.allRowDelete=false
	 
	jQuery("#" + tabName).append("<tr>" + removeObj[tabName] + "</tr>")	;
	var tr = tabObj.rows[tabObj.rows.length-1]
	var len = tr.cells.length;
	 
	multiValInput(tr);
	 
	if(prefix != 'true') reOrderSeq(tabName)

}
  
function setFileName(input) 
{
	var ip = $(input);
	var val = ip.val();
	
	if(val != null)
	{
		 
		if(ip.parent().parent().find("a").length>0)
		{
			ip.parent().parent().find("a").removeAttr("href").text(getFileNameOnly(val))
		}
		else
		{	
			ip.parent().prev("td").append("<i class=\"fa fa-floppy-o\" aria-hidden=\"true\"></i> <a >" + getFileNameOnly(val) + "</a>")
		}	 
		
		
	}

}
 
function setRadioVal(obj)
{
	obj=event.srcElement;
	
	var val = jQuery(obj).val();
	 
	jQuery(obj).parent().find(":hidden").val(val);
}
function setCheckVal(obj)
{
	if(!obj  ) obj=event.srcElement 
	var chkbox = jQuery(obj).parent().find(":checkbox");
	var val = "";
	var idx=0;
	for( var i=0; i < chkbox.length; i++)
	{
		if(chkbox[i].checked)
		{
			if(idx>0) val+= ",";
			idx++;
			val += chkbox[i].value
		}
	}
	 
	jQuery(obj).parent().find(":hidden").val(val);
}
function addOption4Tag(obj, id)
{
	var tableObj = jQuery(obj).parents("TABLE")[0]
	var selFrom =jQuery(tableObj).find("#" + id+"_FROM")[0]
	var selTo =  jQuery(tableObj).find("#" + id )[0]
	addSelSelected(selFrom, selTo);
	removeSelSelected(selFrom);
	
}
function addSelSelected( selFrom , selTo)
{
	for( var i=0; i < selFrom.options.length ; i++)
	{
		if(selFrom.options[i].selected)
		{
			addOption(selTo,  selFrom.options[i].value, selFrom.options[i].text)
		}
	}
}
function removeSelSelected( selFrom )
{
	for( var i=0; i < selFrom.options.length ; )
	{
		if(selFrom.options[i].selected)
		{
			selFrom.remove(i)
		}
		else
		{
			i++
		}
	}
}
function delOption4Tag(obj, id)
{
	var tableObj = jQuery(obj).parents("TABLE")[0]
	var selFrom =jQuery(tableObj).find("#" + id)[0]
	var selTo =  jQuery(tableObj).find("#" + id+"_FROM" )[0]
	addSelSelected(selFrom, selTo);
	removeSelSelected(selFrom);
}
function moveOnlyOne(row) //날짜 component등은 grid내에서 하나만 존재해야 함.
{
	var onlyone  =jQuery( row ).find("[onlyone=true]");
	if(onlyone.length>0)
	{
		jQuery(jQuery("#onlyoneArea")[0]).append(onlyone)
	}
	 
}
jQuery(document).ready(function() {
	var multis = jQuery(".multifile_table, .multiinput_table, .multitable_table");
	
	for( var i=0; i < multis.length ; i++)
	{
		var idx = getMaxRowLen(multis[i])-1;
		
		if(idx<multis[i].rows.length && removeObj[multis[i].id] == null)
		{ 
			moveOnlyOne(multis[i].rows.item(idx));
			removeObj[multis[i].id] = multis[i].rows.item(idx).innerHTML;	
		}
	}
 
	jQuery(".input_date").change(function (e){
		var $input = jQuery(this);
		var val = $input.val();
		$input.val(getChangeDateFormat(val))
	})
	
	jQuery(".multifile_table input:file").change(function (e){
		setFileName(jQuery(this)[0]);
	})
	jQuery(".multifile_table  input:radio,.multiinput_table input:radio, .multitable_table  input:radio").click(function (e){
		setRadioVal(this)
	})	
	jQuery(".multifile_table input:checkbox, .multiinput_table input:checkbox, .input_checkbox1, .multitable_table  input:checkbox").click(function (e){
		setCheckVal(this)
	})	
}) 

try{
	$.datepicker.setDefaults( {	showOn: "button",
		buttonImage: "/images/changeflow/icon/icon_calendar.gif",
		buttonImageOnly: true,
		showOtherMonths: true,
		selectOtherMonths: true,
		changeMonth: true,
		changeYear: true} );	
}catch(e){}


//new biz popup
var _selId;
var _selObj;

function openBizPopUp( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var popup = openCFBizTreePopup(setBizInfo);
}

function openBizPopUpWithAuth( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var popup = openCFBizTreePopupWithAuth(setBizInfo);
}

function setBizInfo( bizObj ) {
	_selObj.value = bizObj.BIZ_ID ;
	jQuery(jQuery(_selObj).parents("TD")[0]).find("#" +_selId+"_NM").text(bizObj.BIZ_NM) ;
	closeCFBizTreePopup();
}

function openMultiActiveBizPopUp( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var bizIdStr = _selObj.value;

	var defaultBizIds = [];
	if(typeof(bizIdStr) =='string' && bizIdStr.length>0) {
		var t = _selObj.value.split(",");
		for(var i=0; i<t.length; i++) {
			defaultBizIds[i] = Number(t[i]);
		}
	}else{
		defaultBizIds.push(Number(bizIdStr));
	}

	var popup = openCFBizTreePopup(setBizInfos, true, null, null, null, null, null, {"DEL_YN":"N"}, defaultBizIds);
}
function openMultiBizPopUp( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var bizIdStr = _selObj.value;

	var defaultBizIds = [];
	if(typeof(bizIdStr) =='string' && bizIdStr.length>0) {
		var t = _selObj.value.split(",");
		for(var i=0; i<t.length; i++) {
			defaultBizIds[i] = Number(t[i]);
		}
	}else{
		defaultBizIds.push(Number(bizIdStr));
	}

	var popup = openCFBizTreePopup(setBizInfos, true, null, null, null, null, null, null, defaultBizIds);
}
function openMulitActiveBizPopUpWithAuth( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	
	var bizIdStr = _selObj.value;

	var defaultBizIds = [];
	if(typeof(bizIdStr) =='string' && bizIdStr.length>0) {
		var t = _selObj.value.split(",");
		for(var i=0; i<t.length; i++) 
			defaultBizIds[i] = Number(t[i]);
	}else{
		defaultBizIds.push(Number(bizIdStr));
	}

	var popup = openCFBizTreePopupWithAuth(setBizInfos, true, null, null, null, null, null, {"DEL_YN":"N"}, defaultBizIds);
}
function openMulitBizPopUpWithAuth( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	
	var bizIdStr = _selObj.value;

	var defaultBizIds = [];
	if(typeof(bizIdStr) =='string' && bizIdStr.length>0) {
		var t = _selObj.value.split(",");
		for(var i=0; i<t.length; i++) 
			defaultBizIds[i] = Number(t[i]);
	}else{
		defaultBizIds.push(Number(bizIdStr));
	}

	var popup = openCFBizTreePopupWithAuth(setBizInfos, true, null, null, null, null, null, null, defaultBizIds);
}

function setBizInfos( bizObjs ) {
	var bizIds = "";
	var bizNms = "";
	for(var i=0; bizObjs!=null && i<bizObjs.length; i++){
		bizIds+=bizObjs[i].BIZ_ID+",";
		bizNms+=bizObjs[i].BIZ_NM+",";
	}
	if(bizIds!=""){
		bizIds = bizIds.substr(0, bizIds.length-1);
	}
	if(bizNms!=""){
		bizNms = bizNms.substr(0, bizNms.length-1);
	}
	_selObj.value = bizIds ;
	jQuery(jQuery(_selObj).parents("TD")[0]).find("#" +_selId+"_NM").text(bizNms);
	closeCFBizTreePopup();
}

function resetSelPopup( selId, obj ){
	var selObj = jQuery(obj).parent().parent().find("#"+ selId)[0];
	selObj.value="";
	jQuery(jQuery(selObj).parents("TD")[0]).find("#" +selId+"_NM").text("") ;
}

function openDepPopUp( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var popup = openCFDepTreePopup(setDepInfo, false);
}

function setDepInfo(depObj) {
	_selObj.value = depObj.DEP_ID;
	jQuery(jQuery(_selObj).parents("TD")[0]).find("#" +_selId+"_NM").text(depObj.DEP_TITLE) ;
	try
	{
		changeReceiveUserAndDep(depObj.DEP_ID);
	} 
	catch(e) { }
	closeCFDepTreePopup();
}

function openMultiDepPopUp( selId, obj ) {
	_selId = selId;
	_selObj = jQuery(obj).parent().find("#"+ selId)[0];
	var popup = openCFDepTreePopup(setDepInfos, true);
}

function setDepInfos(depObjs) {
	var depIds = "";
	var depNms = "";
	for(var i=0; depObjs!=null && i<depObjs.length; i++){
		depIds+=depObjs[i].DEP_ID+",";
		depNms+=depObjs[i].DEP_TITLE+",";
	}
	if(depIds!=""){
		depIds = depIds.substr(0, depIds.length-1);
	}
	if(depNms!=""){
		depNms = depNms.substr(0, depNms.length-1);
	}
	_selObj.value = depIds ;
	jQuery(jQuery(_selObj).parents("TD")[0]).find("#" +_selId+"_NM").text(depNms);
	
	closeCFDepTreePopup();
}