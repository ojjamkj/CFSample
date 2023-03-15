<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/changeflow/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<%@ include file="/common/_header.jsp" %>


<script type="text/javascript">

	var biglookActionRun = new CFActionRun();
	
	function callBiglook(checkedResIds)
	{
		var param = new Object();
		param.INST_ID="[INST_ID]";
		param.BIGLOOK_PRJ_CD="[BIGLOOK_PRJ_CD]";
		
		viewProgressMessageDiv("검사중...");


		biglookActionRun.run({
			actionParam : param,
			runAction : 'test.biglook.BigLookAction',
			/*completedEvent : function onSuccess(actionParam, resultData){hideProgressMessageDiv();},*/
			failEvent : function onFail(actionParam, resultData){hideProgressMessageDiv();}
		});
	}
	
</script>
</head>

<body class="mainBody" >
<jsp:include page="/changeflow/progressMessage.jsp" />
<div class="panel panel-primary">
	<div class="panel-heading">
		<div class="panel-heading-title">

		</div>
		<div class="panel-heading-button">
			<button type="button" class="btn btn-xs btn-default" onclick="javascript:callBiglook()">검사실행</button>
			
		</div>
	</div>
	<div class="panel-body">
		
	</div>
	<!-- <div class="panel-footer" style="text-align: right;"></div>	 -->
</div>


</body>
</html>