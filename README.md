# My Personal Project: Show-Tracker

## TV-Show Tracker Application

The **Show-Tracker** is a practical application that allows users to keep track of the favorite 
shows that they've watched, are watching, and plan to watch. The application lets users give 
**ratings** to the shows that are added, and can **log** the number of episodes they've seen. 
Other features include the ability to **sort** the list of all their shows, **increment** the episodes 
watched counter easily, and optionally add a note on each show. 

This app is for those who find themselves forgetting the names of all the shows they've watched, 
as well as the episode number of the last episode they watched. Having it all in one place makes
it easy to remember, and convenient when chatting with friends about their favorite shows without
having a **single one** slip their mind. Knowing exactly which episode everyone's on can also prevent
unwanted spoilers that nobody wants!

I find that this project will put an end to my frustrations of losing track of what I've watched 
and how much I liked it after I finished watching it. It will solve the problem of second-guessing
if I've already seen a particular episode, and will give me a simple overview of the shows I am 
currently watching, separated from the ones I already finished. I also enjoy looking for new shows,
and getting recommendations from friends for what I should watch next. Being able to add it to a 
*plan-to-watch* list is an effective way to not forget and ensure I eventually get to watch it. 

## User Stories

- As a user, I want to be able to add a show to my list
- As a user, I want to be able to remove a show from my list
- As a user, I want to be able to give a rating to a show in my list
- As a user, I want to be able to add the number of episodes I've watched for a particular show
- As a user, I want to be able to set a show I've finished to "completed"
- As a user, I want to be able to see the list of all the shows I've added
- As a user, I want to be able to see the overall statistics of all the shows I've added
- As a user, I want to be able to save my ShowList to file (if I so choose)
- As a user, I want to be able to be able to load my ShowList from file (if I so choose)


## Instructions for Grader

- You can add a Show to the ShowList by clicking Add Show from the main menu page. 
Fill in the attributes using the text input and dropdowns. When finished, click Add. 
- You can remove a show from your Show list by clicking Remove Show from the main menu. 
Enter the name of the show you want to remove in the text box, and click remove. 
- You can locate my visual component on the main page when the program starts.
- You can save the state of my application by clicking the Save List button from the main menu
- You can reload the state of my application by clicking the Load List button from the main menu

## Phase 4: Task 2
Example of event log:
- One Piece was added to the list!
- Show list and Show stats updated!
- Tokyo Revengers was added to the list!
- Show list and Show stats updated!
- Viewed Shows
- Tokyo Revengers was removed from the list!
- Show list and Show stats updated!
- Attack on Titan was added to the list!
- Show list and Show stats updated!
- Viewed Stats

## Phase 4: Task 3

One possible refactoring that could be done on ShowTracker is to use interfaces to abstract most
of the methods in the ShowList class to a possible ShowManager class that handles the methods used 
for adding, removing, and manipulating the fields of the shows to reduce coupling. Furthermore, the
Show class could be refactored to extend a class that represents a wider range of entertainment
services like movies, documentaries, or even podcasts. This could allow the fundamental attributes
to be implemented for each specific type of media and allow the Show class to be substituted for others. 

Additionally, I would add a DocumentListener to track the changes in the input fields when users are
adding shows to the list. This would allow the event log to track the individual fields when a show
is being added, which provides more insight as to what the user is doing. To improve the efficiency of
this functionality, we could introduce a new EventManager class that would aim to reduce the coupling 
between the GUI and EventLog classes and would increase cohesion and the SRP by separating the tasks. 
