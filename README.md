[![Build workflow](https://github.com/IVF13/tic-tac-toe/actions/workflows/build.yml/badge.svg)](https://github.com/IVF13/tic-tac-toe/actions/workflows/build.yml)
<a href="https://codeclimate.com/github/IVF13/tic-tac-toe/maintainability"><img src="https://api.codeclimate.com/v1/badges/776dba3228321697125e/maintainability" /></a>

# Tic-Tac-Toe Game
## Rest-API functional:
From this README you will learn how works this REST-API being integrated in Tic-Tac-Toe application and what sequence of commands(endpoints) is necessary for the correct completion of the game.

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

