# NoteTaking

Keeping notes can be tedious and time-consuming and if not done in a properly organized way. However, our time is limited and note organization is usually the last thing on our list to do. The purpose of this project is to develop a note-taking application that not only stores notes but also makes it easier to categorize and search existing notes. 

Below are the steps in setting up a profile and also creating a note.
The Database Schema name is notetakingtest.
One of the requirements is to make the application secured. However, Jason Web Token (JWT) is used in achieving this.
Tables were created in other to get this started and the tables are: User (user_info), Application (Application_info), Role, Note, and Label table.
Steps to profile an application:
1.	You must have an applicationId and also a Role to have access below is sql example in profiling an application
  
  a)	INSERT INTO application_info (appId,app_description, app_name, created)VALUES ('rs2', 'NoteTaking', 'NoteTaking','2022-11-01');
  
  
  b)	INSERT INTO ROLE (ROLEID,CREATED,ROLE_NAME,APPID)VALUES(‘1’,’2022-11-01’,’NoteTaking’,’rs2’);
  
  
Steps to register a use and setting up a noter:

1.	Create a User: A user must provide some details in other to get register to the application and the information are email(optional), name(required), password(required), username(required), surname(required).
Upon a successful registration you will be logged into the application and then user can proceed in setting up a note.

2. Create a Note: The application should allow users to create new notes. A note must consist of a title, note content and an optional label. 
The title should be a text field, the content a text area while the label should be text field but with suggested auto-complete from the existing labels. When the user saves the new note if an existing label was used then link the note to the existing label in the database otherwise first create the new label in the label table then link to it. On save, the user will be the newly created note with the rest of the notes. Basic validation (eg. required fields, maximum length etc.) is expected on the input elements. 

3. Edit a Note: When the user clicks on the note's edit button, a form similar to the one used when created a new note is shown however this time around it is pre-filled with the existing note data. The same behavior as when creating a new note is expected.

4.Delete a Note: When the delete button is clicked, the user is presented with a prompt to confirm note deletion. Once confirmed, the note should be deleted from the database. Additionally, make sure that there are no 'unused' labels. 

5. Filter/Search: A search button/bar should be created that allows the user to do a text search that should match either title, note content or label. Any notes that match these criteria must be shown on screen while those that do not match must be hidden. 

6. Quick Link: Show a sidebar that will contain a list (toggle buttons) of all the labels. When a user clicks on a label, it will automatically filter out the notes assigned to that label. 

Every detail above was designed using Java, Spring boot, Angular Technology.

Here is the postman collection below:

https://www.getpostman.com/collections/15a58c92c2f049d24fe3

this postman is available for api testing. While the frontend was built in angular and here is the url to access the login page       http://localhost:4200/

