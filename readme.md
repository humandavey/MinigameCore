# MinigameCore [humandavey]

This project is for the Darta Network and is the backbone of the Minigames available to play.

## Developer Usage

Creating a new minigame is super simple. Just follow these <> steps.

### 1. Create the class

Create a class in the `game.games` package and extend the `Game` class.
This will give you access to all the methods and variables you need to make your game work as intended.

### 2. Override onStart() and Constructor

Next you need to implement the `onStart()` method and add a constructor that takes an arena and passes it to the super constructor. That is all you need to do for the constructor. As expected, the code in the onStart() method will run when the games starts.

### 3. Add events and code your game

Here is the fun part, now you need to code your game! The class is already a listener as the Game class implements `Listener`, so you don't need to worry about that. It also unregisters its self when the game ends to stop memory leaks.

### 4. Add the end condition

Make sure you call the `end()` method! There are multiple ways you can end the game, you can give the win to a team, a player, or no one!

### 5. Add games to the GameType enum

Next, you need to add your game to the `GameType` enum in the `game` package. This will register it and allow for it to be played on your server!

Now you are done!

## Credits

Coded by `humandavey`, tested with `kenny787pilot` and `AVLG`.