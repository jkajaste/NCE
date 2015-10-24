# NCE

Notes from developer

Main design principles
 Use MVP, Activity as the View
 Presenter and Model are implemented against interfaces
 View and Presenter implement also callback interface for Presenter and Model, respectively
 Use of callbacks makes method calls asynchronous by design

Testing
 App seems to be stable; It survives my manual testing

Known issues
 There are no nice-to-haves that a production version would require,
 like user preferences.
 
 Maybe Model, View and Presenter should be in separate packages?

 Android JUnit testing would be good to master, but I ran out of time
 to learn it in the scope of this exercise.

Next features
 Drive mode; automatic search result updates
 Query spam filter; Max size for query buffer
 Inform user in cases of "not connected", "no location" or "bad response"
