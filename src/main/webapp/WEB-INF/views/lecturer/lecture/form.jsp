<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="lecturer.lecture.form.label.title" path="title"/>
	<acme:input-textbox code="lecturer.lecture.form.label.anAbstract" path="anAbstract"/>
	<acme:input-double code="lecturer.lecture.form.label.learningTime" path="learningTime"/>
	<acme:input-textbox code="lecturer.lecture.form.label.body" path="body"/>
	<acme:input-url code="lecturer.lecture.form.label.furtherInformation" path="furtherInformation"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:input-textbox code="lecturer.lecture.form.label.type" path="type"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:input-select code="lecturer.lecture.form.label.type" path="type" choices="${types}"/>
			<acme:submit code="lecturer.lecture.form.button.update" action="/lecturer/lecture/update"/>
			<acme:submit code="lecturer.lecture.form.button.delete" action="/lecturer/lecture/delete"/>
			<acme:submit code="lecturer.lecture.form.button.publish" action="/lecturer/lecture/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="lecturer.lecture.form.label.type" path="type" choices="${types}"/>
			<acme:submit code="lecturer.lecture.form.button.create" action="/lecturer/lecture/create"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>