# Hykr

  We plan to develop an app that will be an ideal companion for people with an interest in hiking and/or
who enjoy walking trails. Currently, we intend to create an application that records the number of steps the
user takes and the routes they travel. This data will be stored and used for different features in our app. The
user will have the option to input personal data as part of their user profile, this will allow the app to offer more
personalised analysis.

  Firstly, we will passively record the number of steps taken and store that data in a local database. From
this information we can calculate the distance the user has walked and the approximate number of calories they
have burned. By default, these numbers will be calculated using the attributes of an average person (average
height, average weight). This means that the results obtained may not be fully accurate unless the app user
inputs their personal height and weight values. To enhance this feature we will implement a streak system,
wherein a user will be rewarded with a streak if they meet their own set step goal by the end of the day.

  Secondly, we plan to have a trails tab that allows the user to plan a trip. The app will log the user's
starting location, their elevation and other data that will be saved at the end of the trip. This information will
then be added to a list of the user's trails. This list will be searchable, the user can view the statistics of all past
trails, including the number of steps taken and the distance walked. The user will also have the ability to view
the trail route on a map. To compliment this feature, there will be a curated list of user-submitted trips that any
user of the app can browse.

  Finally, we think a selection of analytics features will complement our app and we plan to dedicate an
entire page to them that will be filtered by dates, e.g. daily, weekly, monthly. This will add functionality that
some users may be attracted to such as the visualization of their progress. Whether this be used for bragging to
their friends or as a fitness objective is up to the user, people like to objectively view their progress.

  We envision the typical users of our app to be tourists, nature enthusiasts, hikers and people who are
new to hiking and/or interested in walking trails. Accordingly, we expect the main function to be the pedometer,
hence it will form the home page of the app and be immediately visible on start up. The trails and analytics
features will be on separate tabs so as not to clutter a single screen.

##### Expected Use Cases:

Use case 1: passive, everyday user, interested in being active and monitoring steps.
• Set up step goal at the beginning of the day, week, etc.
• The app will record the user’s steps and update the database accordingly.
• The users monitor their activity either on the steps tab or in the analytics tab for more detailed
information, with different graphs available.
• For every consecutive day a step goal is reached the user is awarded an additional streak point, for
motivation.

Use case 2: hiker/tourist, trails feature user, walking trails or in the wilderness.
- The user will log into the app by creating a user profile with an associated username and password.
- The user will add some personal information to their existing profile such as current weight and height
(This should allow the app to approximate the number of calories burned in relation to steps walked).
- The user can browse the app for recommendations of hiking trails or walking routes in their area.
- These recommendations will be accompanied by information related to the trail such as its length, it’s
popularity amongst other app users, expected weather conditions and predicted sunset time.
- While on the trail, users can monitor their progress via the map screen of the app.
- As opposed to selecting a recommended trail the user may be inclined to submit a trail/route of their
own.
- To do this the user will inform the app of their starting point. They will then walk their trail and at the
end inform the app of their end point.
Failure cases:
- We will make it impossible to record steps and/or distance while user is in a car or on a bicycle etc.
- We will make it so the user cannot inflate their step count by shaking the phone in their hand.

##### Planned API/Frameworks:
- Databases – Store all statistics and logs either locally or in the cloud (Firebase)
- Authentication/Security - User accounts/profiles to keep their data secure
- Accelerometer – Recording steps for pedometer
- Location – Record user location for trails feature and weather services
- Weather – To indicate predicted weather conditions and sunset time.
