<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="student.workbook.list.label.title" path="title" />
	<acme:list-column code="student.workbook.list.label.enrolment-code" path="enrolment" />
</acme:list>

<acme:button code="student.workbook.form.button.create" action="/student/workbook/create"/>