<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<h2>
	<acme:message code="lecturer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.total-number-theory-lectures"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfTheoryLectures}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.total-number-hands-on-lectures"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfHandsOnLectures}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.average-learning-time-lectures"/>
		</th>
		<td>
			<acme:print value="${averageLearningTimeOfLectures}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.deviation-learning-time-lectures"/>
		</th>
		<td>
			<acme:print value="${deviationLearningTimeOfLectures}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.minimum-learning-time-lectures"/>
		</th>
		<td>
			<acme:print value="${minimumLearningTimeOfLectures}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="lecturer.dashboard.form.label.maximum-learning-time-lectures"/>
		</th>
		<td>
			<acme:print value="${maximumLearningTimeOfLectures}"/>
		</td>
	</tr>
</table>