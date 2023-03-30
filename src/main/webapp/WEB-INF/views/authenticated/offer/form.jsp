<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-moment code="Instantiation Moment" path="instantiationMoment"/>	
	<acme:input-textarea code="Heading" path="heading"/>
	<acme:input-textarea code="Summary" path="summary"/>
	<acme:input-moment code="Availability Start" path="availabilityStart"/>
	<acme:input-moment code="Availability End" path="availabilityEnd"/>
	<acme:input-money code="Price" path="price"/>
	<acme:input-url code="Link" path="link"/>
</acme:form>