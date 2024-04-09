# TODO list for version v4.x.x
- [X] Add progress bar to show how close the player is to the exit.
- [X] Modify the map to be more complex, add a hallway and a extra room.
- [X] Add more description of the environment, like telling the player when they are in a corner, hallway, etc.

# Unplanned feature ideas
* Add other objects or interactions for the player to find if they ignore the main escape quest.
* Remove the need for the player to press "enter" for each option and do the options in response to a keypress instead.
* Add a mini-quest to find a second key to open the exit door.
* Change controls to either arrow keys or WASD.
* Refactor code base to have all classes in separate files
* Rename Menu class to something about game state, then pull out Menu specific functionality into its own class again.
* Refactor getShortestDistanceExit function to BFS solution instead of hard coding it.
