<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="Instantiation Moment" path="instantiationMoment"/>
	<acme:list-column code="Heading" path="heading"/>
	<acme:list-column code="Summary" path="summary"/>
	<acme:list-column code="Availability Start" path="availabilityStart"/>
	<acme:list-column code="Availability End" path="availabilityEnd"/>
	<acme:list-column code="Price" path="price"/>
	<acme:list-column code="Link" path="link"/>
</acme:list>