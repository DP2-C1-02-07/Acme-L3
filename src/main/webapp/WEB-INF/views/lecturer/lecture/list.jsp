<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.lecture.list.label.title" path="title"/>	
	<acme:list-column code="lecturer.lecture.list.label.anAbstract" path="anAbstract"/>
	<acme:list-column code="lecturer.lecture.list.label.learningTime" path="learningTime"/>
	<acme:list-payload path="payload"/>
</acme:list>
<acme:message code="lecturer.lecture.info.text"/>

<br><br>
<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="lecturer.lecture.list.button.create" action="/lecturer/lecture/create"/>
</jstl:if>