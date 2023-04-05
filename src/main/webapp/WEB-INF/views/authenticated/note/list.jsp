<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="Instantiation Moment" path="instantiationMoment"/>
	<acme:list-column code="Title" path="title"/>
	<acme:list-column code="Author" path="author"/>
	<acme:list-column code="Message" path="message"/>
	<acme:list-column code="Email" path="email"/>
	<acme:list-column code="Link" path="link"/>
</acme:list>

<acme:button code="Post a note" action="/authenticated/note/create"/>