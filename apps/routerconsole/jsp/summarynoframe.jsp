<%@page import="net.i2p.router.web.SummaryHelper" %>
<%
/*
 * Note:
 * This is included almost 30 times, so keep whitespace etc. to a minimum.
 */
%>
<jsp:useBean class="net.i2p.router.web.SummaryHelper" id="helper" scope="request" />
<jsp:setProperty name="helper" property="contextId" value="<%=(String)session.getAttribute("i2p.contextId")%>" />
<jsp:setProperty name="helper" property="action" value="<%=request.getParameter("action")%>" />
<jsp:setProperty name="helper" property="updateNonce" value="<%=request.getParameter("updateNonce")%>" />
<jsp:setProperty name="helper" property="consoleNonce" value="<%=request.getParameter("consoleNonce")%>" />
<jsp:setProperty name="helper" property="requestURI" value="<%=request.getRequestURI()%>" />
<jsp:setProperty name="helper" property="writer" value="<%=out%>" />
<%
/*
 * The following is required for the reseed button to work, although we probably
 * only need the reseedNonce property.
 */
%>
<jsp:useBean class="net.i2p.router.web.ReseedHandler" id="reseed" scope="request" />
<jsp:setProperty name="reseed" property="*" />
<%
/*
 * The following is required for the update buttons to work, although we probably
 * only need the updateNonce property.
 */
%>
<jsp:useBean class="net.i2p.router.web.UpdateHandler" id="update" scope="request" />
<jsp:setProperty name="update" property="*" />
<jsp:setProperty name="update" property="contextId" value="<%=(String)session.getAttribute("i2p.contextId")%>" />
<%
    // moved to java for ease of translation and to avoid 30 copies
    helper.renderSummaryBar();
%>
