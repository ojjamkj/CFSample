<%--
 * version $Id: header.jsp,v 1.1 2019/06/19 00:05:15 ojjamkj Exp $
--%>
<link rel="stylesheet" type="text/css" href="/site/css/changeflow/style.css" /><!--site style sheet-->
<!--base editor-->
<link rel="stylesheet" type="text/css" href="/js/summernote-0.8.18/summernote.min.css" />
<script type="text/javascript" language="javascript" src="/js/summernote-0.8.18/summernote.js?v=20210121"></script>
<script type="text/javascript" language="javascript" src="/js/summernote-0.8.18/plugin/cleaner/summernote-cleaner.js?v=20210121"></script>
<%if(langType.indexOf("en")==-1) {%>
<script type="text/javascript" language="javascript" src="/js/summernote-0.8.18/lang/summernote-<%=langType%>.min.js"></script>
<%} %>
<script type="text/javascript" src="/js/se2/js/service/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="/site/js/localization/cf_site.<%=currentLocale%>.js?v=20170322"></script><!--site message -->