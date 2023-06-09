# Electronic Medical Record Application

## An application with clinical uses

What will this application do:
- Be able to retrieve and view patient medical records
- Be able to store patient data
- Be able to send prescriptions to pharmacies across UBC
- Be able to send patients to specialists

<p>
The electronic medical records app will allow 
doctors and medical staff to easily access and 
update patient information, send prescriptions to 
pharmacies, and book appointments with specialists
in a secure and efficient manner. This project
is of interest because many people in my life who are 
work in healthcare have complained about the inefficiency of
current medical record systems, and I believe that
this app could provide a valuable solution to 
these issues.
</p>

<h3>
USER STORIES
</h3>

What a potential user will gain from this app:
- As a user, I would like to add and remove patients from a list
- As a user, I would like to send prescriptions to pharmacies
- As a user, I would like to store data of previous patients
- As a user, I would like to change existing patients
- As a user, I would like to add doctors notes and diagnoses to patient files
- As a user, I would like to view and search through a list of all my patients by their name
- As a user, I would like to be able to save my patients and their data to a file
- As a user, I want to have the option to load my previous patients when I login 
- As a user, I want to have the option to save patient data to file

<h3>
Instructions for Grader
</h3>

<h5>
Please follow these step-by-step instructions to interact with the GUI:
</h5>

- Launch Application and you will be presented with the LoginScreenUI
- To register and add a doctor click the signup button and enter the information requested (name, password)
- Click the "Sign Up" button, and you will be redirected to the main menu UI

<h5>
Adding Patients to Doctors Record
</h5>

- Click the "Add Patient" button on the "Main Menu UI"
- Fill in the required data for the Patient (name, age, weight, height)
- Click the save button and a prompt will confirmation prompt will appear
- Click "Yes" to confirm, and the patient will be added to this doctors
- Repeat these steps to add several patients
- 
  <h5>
  Adding Medical Records to Patients
  </h5>

- Click the name of a patient you wish to add a medical record to on the left scroll panel
- Click the new Medical record Button in the top right corner
- A prompt will appear to enter information to create a new medical record (symptoms, prescriptions, doctors note)
- Click the add button and the medical record will be added to this patient
- Repeat these steps to add several medical records

<h5>
Visual Component
</h5>

- The Visual Component is a logo that can be found on the "LoginScreenUI", "SignupUI", "LoginUI", and "MainMenuUI"

<h5>
Saving the state of the application
</h5>

- To Save the state of the application, click the logout button in the bottom left corner. You will be prompted to 
save you progress, click yes and the current state of the application will be saved

<h5>
Loading the State of the Application from File
</h5>

- To load the state of the application, login with your credentials
- A prompt will appear to load previous patients
- Click "Yes" to load the previously saved state of the application

<h5>
Phase 4: Task 2
<h6>
Note: user must first log out then exits application for to prints out event log
</h6>
</h5>

- Wed Apr 12 18:19:33 PDT 2023
- ZaidKhan added to MediRecords
- 
- Wed Apr 12 18:19:42 PDT 2023
- ZaidKhan added to Ryan to their Patient list
- 
- Wed Apr 12 18:19:54 PDT 2023
- ZaidKhan added to Steven to their Patient list
- 
- Wed Apr 12 18:20:04 PDT 2023
- New Medical record added to Steven
- 
- Wed Apr 12 18:20:13 PDT 2023 
- New Medical record added to Ryan 
- 
- Wed Apr 12 18:20:46 PDT 2023 
- ZaidKhan added to Richard to their Patient list 
- 
- Wed Apr 12 18:20:56 PDT 2023 
- New Medical record added to Richard 
- 
- Wed Apr 12 18:21:09 PDT 2023 
- ZaidKhan removed Richard from their Patient list 
- 
- Wed Apr 12 18:21:17 PDT 2023 
- Ryan changed weight to 100 
- 
- Wed Apr 12 18:21:24 PDT 2023 
- ZaidKhan changed their password

<h5>
Phase 4: Task 3
</h5>

<p>
If I had more time to work on this project, I would 
definitely consider refactoring the `MainMenuUI` class. Currently,
this class has too many methods, and could be simplified into other classes.
We can refactor the UI-related method to separate classes to have a better 
separation between the UI and methods that make the program run.
I would also implement exception handling, the application would become more
robust and user-friendly. This would involve adding try-catch blocks where necessary, 
particularly when working with file I/O, JSON parsing, or other operations that may
result in errors. Furthermore, adding input validation checks would help ensure that the application
processes only valid data and responds appropriately to incorrect or malformed inputs.
These changes would improve the overall reliability and stability of my application.
</p>

