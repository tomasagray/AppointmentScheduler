# Appointment Scheduler

A multi-user calendar and appointment tracking software solution. Supports 
several languages and configuration options.


### Testing instructions

* Locale testing can be performed by un-commenting either of the lines at the 
  beginning of the start() method in appointmentscheduler.AppointmentScheduler.java .

* The application was developed at the outset to support multiple users. Therefore,
  the Appointments by Consultant report was modified to display a bar chart 
  representing the number of appointments each consultant has each month.

* The Add Appointment dialog can be closed by clicking outside of it.

* The login form can be submitted by hitting the 'ENTER' key.

* Because the appointment TYPE field was not implemented, it was necessary
  to do some hacky things to implement this requirement.

* The ZoneId for the application timezone is set based on the user's 
  office location, not on system time. Therefore, to test timezone 
  functionality, it is necessary to change the office location under
  Settings. It defaults to PHOENIX.