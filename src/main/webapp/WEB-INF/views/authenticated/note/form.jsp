<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="Instantiation Moment" path="instantiationMoment" readonly="true"/>	
	<acme:input-textbox code="Author" path="author" readonly="true"/>
	<acme:input-textbox code="Title" path="title"/>
	<acme:input-textarea code="Message" path="message"/>
	<acme:input-textbox code="Email" path="email"/>
	<acme:input-url code="Link" path="link"/>
	
	<jstl:if test="${ _command == 'create'}">
		<acme:input-checkbox code="Confirm" path="confirmation"/>
		<acme:submit code="Post the note" action="/authenticated/note/create"/>
	</jstl:if>
</acme:form>