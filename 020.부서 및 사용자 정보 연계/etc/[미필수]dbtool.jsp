<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="java.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="jspeed.base.util.*"%>
<%@page import="jspeed.base.jdbc.QueryHelper"%>
<%@page import="com.gtone.cf.util.DBHelper"%>
<%@page import="jspeed.base.jdbc.CacheResultSet"%>
<%@page import="com.gtone.cf.util.ExceptionHandler"%>
<%@ include file="/changeflow/header.jsp" %>
<%@ include file="authCheck.jsp" %>


<html>
<head>	

<link type="text/css" rel="stylesheet" href="/css/changeflow/style.css"></link></head>

<body>

<%
String sql = StringHelper.evl(request.getParameter("QueryContent"), "");
String ds = StringHelper.evl(request.getParameter("DSSource"), "");
String tsType = StringHelper.evl(request.getParameter("TSType"), "");
String spliter = StringHelper.evl(request.getParameter("spliter"), ";");
String ignoreErr = StringHelper.evl(request.getParameter("ignoreErr"), "N");

QueryHelper qHelper = null;
CacheResultSet crs = null;
int columnSize=0;

try{
	
	if(!StringHelper.isNull(sql) && !StringHelper.isNull(ds) )
	{
		qHelper = new QueryHelper(ds);
		if("select".equals(tsType)){
			crs = new CacheResultSet(qHelper.executeQuery(sql));
			columnSize = crs.getColumnCount();	
		}else if("notselect".equals(tsType)){
			qHelper.begin();
			String[] sqls = StringHelper.split(sql,spliter.charAt(0),true);
			
			for(int i=0; i<sqls.length; i++)
			{
				if(!"".equals(sqls[i].trim())){
					try{
						int count1 = qHelper.executeUpdate(sqls[i], new Object[]{});
						out.println(sqls[i] +" : " + count1 +"건 작업완료 <br><br>");
					}catch(Exception e){
						if("Y".equals(ignoreErr)){
							out.println(sqls[i]+ "<br>");
							out.println("<p color='red'>"+com.gtone.cf.util.ExceptionHandler.getErrorMessage4Front(e)+"</p>");					
							qHelper.commit();
						}else{
							ExceptionHandler.createCFException(e);
						}
					}
				}
			}
			qHelper.commit();
		}
	}
}catch(Exception e){
	e.printStackTrace();
	out.println("<p color='red'>"+com.gtone.cf.util.ExceptionHandler.getErrorMessage4Front(e)+"</p>");
	ExceptionHandler.handleException(e);
	qHelper.rollback();
}finally{
	//if(crs !=null) try{crs.close();}catch(Exception ignore){}
	if(qHelper !=null) try{qHelper.close();}catch(Exception ignore){ExceptionHandler.ignoreException(ignore, null);}
}
%>

<form name="fm" method="post">
* 데이터 소스 : 
<select name="DSSource">
	<option value="jSpeedDataSource" <% if("jSpeedDataSource".equals(ds)){%>selected<%} %>>jSpeedDataSource</option>
	<option value="ITPDataSource" <% if("ITPDataSource".equals(ds)){%>selected<%} %>>ITPDataSource</option>
	<option value="CMDataSource" <% if("CMDataSource".equals(ds)){%>selected<%} %>>CMDataSource</option>
	<option value="HRDataSource" <% if("HRDataSource".equals(ds)){%>selected<%} %>>HRDataSource</option>
</select>

&nbsp;&nbsp;&nbsp;&nbsp;* 쿼리 구분자 : 
<input type="text" name="spliter" value=";" style="width:20px;"/>

&nbsp;&nbsp;&nbsp;&nbsp;* 오류발생시 계속 진행 : 
<input type="checkbox" name="ignoreErr" value="Y"/>


&nbsp;&nbsp;&nbsp;&nbsp;* 트랜잭션 구분 : 
<select name="TSType">
	<option value="select" <% if("select".equals(ds)){%>selected<%} %>>select</option>
	<option value="notselect" <% if("notselect".equals(ds)){%>selected<%} %>>insert/update/delete/ddl</option>
</select>
<br>
<textarea rows="10" cols="100" name="QueryContent"><%=sql %></textarea>
<input type="button" name="실행" value="실행" onClick="fm.submit();"/>
</form>


<% if(crs !=null && "select".equals(tsType)){ %>

* 조회결과 (총건수 : <%=crs.getRowCount() %> ) <br>	
<table width="100%" border="1" cellspacing="0" cellpadding="0">
<tr>
<% 
	for(int i=0; i<columnSize;i++)
	{
		out.println("<td bgcolor=grey>" + crs.getColumnName(i+1) +"</td>");
	}
%>
</tr>



<% 
	while(crs.next()){
		out.println("<tr>");
		for(int i=0; i<columnSize;i++)
		{
			out.println("<td>" + crs.getObject(i+1) +"</td>");
		}
		out.println("</tr>");
	}


	if(crs!=null)crs.close();
%>

</table>

<%}%>


</body>
</html>