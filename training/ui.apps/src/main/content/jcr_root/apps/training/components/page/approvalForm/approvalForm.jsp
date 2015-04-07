<%--

  Approval Form Page Component component.

  

--%><%
%><%@include file="/libs/foundation/global.jsp"%><%
%><%@page session="false" %><%
%>
<html>
<head>
    <title> Event Approval Form </title>
</head>
<body>
<%
	String eventPath = request.getParameter("eventPath");
if(eventPath!=null && !eventPath.equalsIgnoreCase("")) {

    Page eventPage = pageManager.getContainingPage(eventPath);


%>


    <h3>This is the Approval Form Page which would be viewed by the external approvers in the publish environment.</h3>


    <div>The new event content can be viewed <a target="_new" href="<%= eventPage.getPath()%>.html">here.</a></div>

    <div style="margin-top:45px;">Please approve or disapprove the content by submitting the below form.</div>
    <form action="/bin/eventApproveServlet" method="POST">
        <input type="hidden" name="eventPath" value="<%= eventPage.getPath() %>" />
        <input type="radio" name="action"  value="approved">Approve
		<br>
		<input type="radio" name="action" value="disapproved">Disapprove
        <br>
        <br>
        <input type="submit" name="submit" value="submit" />
    </form>

    <% } else { %>


    No Event Path provided in the url.. No form would be showed.

    <% } %>
</body>
</html>