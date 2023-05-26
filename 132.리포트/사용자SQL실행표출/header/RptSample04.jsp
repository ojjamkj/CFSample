<%@page contentType="application/json; charset=utf-8" %>
<%@ page import="com.gtone.cf.util.JSONUtil,com.gtone.cf.util.CodeServiceUtil,com.gtone.cfclient.util.PropertyServiceUtil"%>
<%@ include file="/changeflow/header.jsp" %>
{
	export : {
		enabled:true,
		allowExportSelectedData:true,
		excelFilterEnabled:true,
	},
}