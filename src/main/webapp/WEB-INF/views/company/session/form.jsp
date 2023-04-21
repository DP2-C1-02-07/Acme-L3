<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="company.session.form.label.title" path="title"/>
	<acme:input-textarea code="company.session.form.label.abstractThing" path="abstractThing"/>
	<acme:input-moment code="company.session.form.label.startDate" path="startDate"/>
	<acme:input-moment code="company.session.form.label.finishDate" path="finishDate"/>
	<acme:input-textbox code="company.session.form.label.info" path="info"/>
	
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:input-select code="company.session.form.label.practicum" path="practicum" choices="${practica}"/>
			<acme:submit code="company.session.form.button.update" action="/company/session/update"/>
			<acme:submit code="company.session.form.button.delete" action="/company/session/delete"/>
			<acme:submit code="company.session.form.button.publish" action="/company/session/publish"/>
			
		</jstl:when>
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == false}">
			<acme:input-textbox code="company.session.form.label.practicum" path="practicum.code"/>			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="company.session.form.label.practicum" path="practicum" choices="${practica}"/>
			<acme:submit code="company.session.form.button.create" action="/company/session/create"/>
		</jstl:when>
		<jstl:when test="${_command == 'create-addendum'}">
			<acme:input-select code="company.session.form.label.practicum" path="practicum" choices="${practica}"/>
			<acme:input-checkbox code="company.session.form.label.confirmation" path="confirmation"/>
			<acme:submit code="company.session.form.button.create" action="/company/session/create-addendum"/>
		</jstl:when>		
	</jstl:choose>
	<jstl:if test="${addendum == true}">
		<acme:message code="company.session.form.message.addendum"/>
	</jstl:if>
</acme:form>

