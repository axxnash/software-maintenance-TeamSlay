<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<div class="container sections-group">
    <div class="row">
        <div class="col-md-12 column ui-sortable">
            <div class="page-header">
                <h1>My Bookings</h1>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <c:choose>
                <c:when test="${not empty bookingsList and bookingsList.size() > 0}">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Booking ID</th>
                                <th>Booking Date</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                                <th>Guests</th>
                                <th>Nights</th>
                                <th>Total Cost</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="booking" items="${bookingsList}">
                                <tr>
                                    <td>${booking.booking_id}</td>
                                    <td>${booking.bookingDate}</td>
                                    <td>${booking.startDate}</td>
                                    <td>${booking.endDate}</td>
                                    <td>${booking.numGuests}</td>
                                    <td>${booking.numNights}</td>
                                    <td>$<fmt:formatNumber value="${booking.totalCost}" pattern="#,##0.00"/></td>
                                    <td>
                                        <a href="${contextPath}/rooms/${booking.bookingURL}" class="btn btn-sm btn-primary">View Room</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        <h4>No bookings found</h4>
                        <p>You haven't made any bookings yet. Browse our <a href="${contextPath}/rooms">available rooms</a> to make a reservation.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
    <div class="row" style="margin-top: 20px;">
        <div class="col-md-12">
            <a href="${contextPath}/rooms" class="btn btn-lg btn-primary">Book More Rooms</a>
        </div>
    </div>
</div>
