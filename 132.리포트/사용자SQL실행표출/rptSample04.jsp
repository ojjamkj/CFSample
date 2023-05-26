<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/changeflow/header.jsp" %>

<script type="text/javascript">

	//화면 로딩시 TO-DO 작업 처리
	$(document).ready(function() {
		// /*커스텀 jsp 그리드*/
		var reportInfo = {
			REPORT_ID : "RptSample04",
			REPORT_NAME : "Custom URL 리포트",
			REPORT_TYPE_CD : "DATA_GRID",
			CUSTOM_URL : "/changeflow/report/rptSample04.jsp",
			CONDITION: [
				{ REPORT_COND_ID: "SQL",  	COND_TYPE: "TextArea",   COND_ID: "sql", COND_URL: "textarea.js", WIDTH: "25%", COND_TITLE:"SQL",REPORT_COND_OPTIONS : { placeholder: "SQL"}
				}
			]
		};
		reportObj = gridConfObj(reportInfo);
		reportObj.init();
	});
	
	
	function getReportData1(){
		let sql = $("#SQL").dxTextArea("instance").option('value');
		var gReportParam={ QUERY_KEY : 'CFReport_RptSample04' , CALL_TYPE:"QUERY", SQL: sql};
		
		$.when(new CFActionRun().exe({"runAction": "com.gtone.cfadmin.actions.report.ReportQueryAction", "actionParam": gReportParam}))
		.done(function(jsonData){
			$("#GTDataGrid_Area").dxDataGrid('instance').option('dataSource', jsonData.GRID_DATA);
		})
		.fail(function(){
		});	
		
	}
	
</script>

          
<div class="panel panel-primary cf-panel-right">
	<div class="panel-heading">
		<div class="panel-heading-title" id="divReportTitleArea">
				
		</div>
		<div class="panel-heading-button">
			<button type="button" class="btn btn-xs btn-warning" onClick="javascript:getReportData1();"><span class="glyphicon glyphicon-search"></span> <fmt:message key="common.button.search"/></button>
		</div>
	</div>
	<div class="panel-body" id="divCondArea">
		
	</div>
	
	<div class="panel-footer" >
		<div id="GTDataGrid_Area"></div>
	</div>
</div>
