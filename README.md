[![Build workflow](https://github.com/IVF13/tic-tac-toe/actions/workflows/build.yml/badge.svg)](https://github.com/IVF13/tic-tac-toe/actions/workflows/build.yml)
<a href="https://codeclimate.com/github/IVF13/tic-tac-toe/maintainability"><img src="https://api.codeclimate.com/v1/badges/776dba3228321697125e/maintainability" /></a>

# Tic-Tac-Toe Game With Rest-API Functional:
This application is the implementation of the game tic-tac-toe built using the Spring Framework. The game can be played in the console or using the REST API. The application has the function of recording game logs and subsequent playback.
Also, it has many functions for working with game data and storing it in the JPA database. From this README you will learn how works this REST-API being integrated in Tic-Tac-Toe application and what sequence of commands(endpoints) 
is necessary for the correct completion of the game.

## Gameplay:

### At first, use:
###### /gameplay/start - starts game (POST Type)
### After that you need to set player names using the JSON format:
###### /gameplay/player1/set/name -
###### /gameplay/player2/set/name - sets player name (POST Type)
```json
{
  "name": "Dean"
}
```
### Now you can make steps in order using the JSON format:
###### /gameplay/player1/set/step -
###### /gameplay/player2/set/step - makes new step (PUT Type)
```json
{
    "cell": 5
}
```
### After completion the game you can restart it, using:
###### /gameplay/restart - restarts game (POST Type)
## Game data operations:
### If you want to see the result of the game, use:
###### /gameplay/result - shows last game result (GET Type)
### For session, use:
###### /gameplay/results - shows all the results of the current session (GET Type)
### If you want to see information about player, use:
###### /gameplay/players/{id} - shows information about player with specified id (GET Type)
### For all players, use:
###### /gameplay/players/info - shows all the information about both players (GET Type)
### If you want to see information about certain step, use:
###### /gameplay/steps/{stepNum} - shows information about specified step (GET Type)
### For all list of steps, use:
###### /gameplay/steps/info - shows all the information about the steps taken (GET Type)
### To delete player, use:
###### /gameplay/players/{id} - deletes the player with current id (DELETE Type)
### To delete step, use:
###### /gameplay/steps/{stepNum} - deletes the step with current stepNum (DELETE Type)
### To delete results, use:
###### /gameplay/results/delete - deletes all the results from current session (DELETE Type)
## Log writing and game simulation:
### To get info about writing the game log, use:
###### /gameplay/write/log/info - shows information about available log formats (GET TYPE)
### To write the game log, use:
###### /gameplay/write/log/{menuItemNum} - writes a log in the selected format (POST TYPE)
### To get info about simulating game by the written log, use:
###### /gameplay/simulate/info - shows information about logs available for reading (GET TYPE)
### To get game simulation from the written log, use:
###### /gameplay/simulate/{menuItemNum} - shows game simulation by the selected log (GET TYPE)
## Database interaction options:
### To save your last game to database, use:
###### /gameplay/save/last/game/to/db - saves the last played game(if it was finished) to the database (PUT TYPE)
### To find all saved games in the database, use:
###### /gameplay/findAll/games/in/db - shows all the information about all saved games (GET TYPE)
### To find the saved game by its id in the database, use:
###### /gameplay/find/game/byId/in/db - shows all the information about the specified game (GET TYPE)
```json
1
```
### The game also can be found by player data:
###### /gameplay/find/game/byPlayer/in/db - shows list of saved games that contains specified player data (GET TYPE)
```json
{
  "playerId": 1,
  "name": "Ivan",
  "symbol": "X"
}
```
### Or by game result:
###### /gameplay/find/game/byGameResult/in/db - shows list of saved games that contains specified game result (GET TYPE)
```json
{
    "result": "{playerId=1, name='Ivan', symbol='X'}",
    //or
    "result": "Draw!"
}
```
### Also, you can delete game data from the database by its id using:
###### /gameplay/delete/game/byId/from/db - deletes game with specified id from the database (DELETE TYPE)
```json
1
```


