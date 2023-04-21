<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="assistant.tutorial.session.list.label.title" path="title"/>
	<acme:list-column code="assistant.tutorial.session.list.label.type" path="sessionType"/>
	<acme:list-column code="assistant.tutorial.session.list.label.startDate" path="startDate"/>
	<acme:list-column code="assistant.tutorial.session.list.label.finishDate" path="finishDate"/>
</acme:list>


<acme:button test="${showCreate}" code="assistant.tutorial.session.list.button.create" action="/assistant/tutorial-session/create?masterId=${masterId}"/>