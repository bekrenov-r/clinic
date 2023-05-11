
# Clinic application

Web application for clinic service. So far, you can register/login, sign up for a new
appointment, edit your profile and manage existing appointments.

### How to run this application
1. You will need MySQL8 environment installed.
2. Open <b>application.properties</b> file. Replace <b>your_username</b>
and <b>your_password</b> placeholders with your MySQL credentials.
3. Open MySQL Workbench. Run <b>create_database.sql</b> script from sql_scripts folder.
It'll create a few sample users.
4. Run ClinicApplication.java. Make sure that port 8080 is available.
5. In your browser, type localhost:8080 and press Enter. 
6. You will need to log in. Use username of one of users from 'users' database table, and password 'pass'.
7. If everything's ok, you can now use application.

### Used technologies:
- SpringBoot 3
- Spring MVC, Security, REST
- Spring Data JPA
- Maven
- HTML/CSS/JavaScript
- Bootstrap 5
- Thymeleaf
