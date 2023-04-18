<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.course.list.label.code" path="code"/>
	<acme:list-column code="lecturer.course.list.label.title" path="title"/>
	<acme:list-column code="lecturer.course.list.label.anAbstract" path="anAbstract"/>	
</acme:list>
<acme:message code="lecturer.course.info.text"/>

<br><br>
<acme:button code="lecturer.course.list.button.create" action="/lecturer/course/create"/>