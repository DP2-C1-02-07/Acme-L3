<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.peep.form.label.title" path="title"/>
	
		<acme:list-column code="any.peep.form.label.instantiationMoment" path="instantiationMoment"/>
		<acme:list-column code="any.peep.form.label.nick" path="nick"/>
		
</acme:list>
				<acme:button code="administrator.offer.form.button.create" action="/any/peep/create"/>
