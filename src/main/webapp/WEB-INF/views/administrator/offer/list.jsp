<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.offer.form.label.instantiationMoment" path="instantiationMoment"/>
	<acme:list-column code="administrator.offer.form.label.heading" path="heading"/>
	<acme:list-column code="administrator.offer.form.label.summary" path="summary"/>
	<acme:list-column code="administrator.offer.form.label.availabilityStart" path="availabilityStart"/>
	<acme:list-column code="administrator.offer.form.label.availabilityEnd" path="availabilityEnd"/>
	<acme:list-column code="administrator.offer.form.label.price" path="price"/>
	<acme:list-column code="administrator.offer.form.label.link" path="link"/>
</acme:list>

<acme:button code="administrator.offer.form.button.create" action="/administrator/offer/create"/>