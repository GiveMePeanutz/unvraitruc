<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<label for="username">Username </label>
<input type="text" id="username" name="username" value="<c:out value="${user.username}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['username']}</span>
<br />

<label for="password">Password</label>
<input type="text" id="password" name="password" value="<c:out value="${user.password}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['password']}</span>
<br />
<br />
<label for="name">Last Name </label>
<input type="text" id="lastName" name="lastName" value="<c:out value="${user.lastName}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['lastName']}</span>
<br />

<label for="firstName">First Name </label>
<input type="text" id="firstName" name="firstName" value="<c:out value="${user.firstName}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['firstName']}</span>
<br />
<br />

<label for="sex">Sex </label>
<input type="radio" id="sex" name="sex" value="<c:out value="0"/>" />Male 
<input type="radio" id="sex" name="sex" value="<c:out value="1"/>" />Female
<span class="error">${form.errors['sex']}</span>
<br />
<br />
<label for="address">Home Address</label>
<input type="text" id="address" name="address" value="<c:out value="${user.address}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['address']}</span>
<br />

<label for="phone">Phone Number </label>
<input type="text" id="phone" name="phone" value="<c:out value="${user.phone}"/>" size="30" maxlength="30" />
<span class="error">${form.errors['phone']}</span>
<br />

<label for="email">Email Address</label>
<input type="email" id="email" name="email" value="<c:out value="${user.email}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['email']}</span>
<br />

<label for="birthDate">Date of Birth (DD/MM/YYY)</label>
<input type="datetime" id="birthDate" name="birthDate" value="<c:out value="${user.birthDate}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['birthDate']}</span>
<br />

<label for="promotion">Promotion</label>
<input type="text" id="promotion" name="promotion" value="<c:out value="${user.promotion}"/>" size="30" maxlength="60" />
<span class="error">${form.errors['promotion']}</span>
<br />
<br />
<label for="photoURL">Photo</label>
<input type="file" id="photoURL" name="photoURL" />
<span class="error">${form.errors['photoURL']}</span>
<br />

