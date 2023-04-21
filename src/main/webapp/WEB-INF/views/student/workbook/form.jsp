<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="student.workbook.form.label.title" path="title"/>	
	<acme:input-textarea code="student.workbook.form.label.activityAbstract" path="abstractElement"/>
	<acme:input-select code="student.workbook.form.label.indicator" path="type" choices="${types}"/>
	<acme:input-moment code="student.workbook.form.label.periodStart" path="periodStart"/>
	<acme:input-moment code="student.workbook.form.label.periodEnd" path="periodEnd"/>
	<acme:input-url code="student.workbook.form.label.link" path="link"/>
	<jstl:choose>
	<jstl:when test="${_command == 'create'}">
		<acme:input-select code="student.workbook.form.label.enrolment-code" path="enrolment" choices="${enrolments}"/>
	</jstl:when>
	<jstl:otherwise>
		<acme:input-textbox code="student.workbook.form.label.enrolment-code" path="enrolment" readonly="true"/>
	</jstl:otherwise>
	</jstl:choose>
	
	<jstl:if test="${ finalised == true}">
		<jstl:choose>
			<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
				<acme:submit code="student.workbook.form.button.update" action="/student/workbook/update"/>
				<acme:submit code="student.workbook.form.button.delete" action="/student/workbook/delete"/>
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="student.workbook.form.button.create" action="/student/workbook/create"/>
			</jstl:when>		
		</jstl:choose>
	</jstl:if>
</acme:form>