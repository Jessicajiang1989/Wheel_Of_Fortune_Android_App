# Project Plan

**Author**: \<ahegde39\>

## 1 Introduction

- *Wheel of Fortune is an intelligent word game that offers users ability to make money by using their intelligence and guessing abilities. The players can solve random puzzles and play tournaments. They can also create puzzles and tournaments and challenge other players as well. At the end of the game they can view the various statistical information related to puzzles, tournaments and money won.*

## 2 Process Description

- *Activity name (verb or verb phrase)*
- *Activity description (concise paragraph)*
- *Entrance criteria (inputs needed for the activity)*
- *Exit criteria (outputs produced by the activity and how you know it has been completed satisfactorily)*

----------------------------------------------------------------------------------------------------------------------------------------------------

*Activity 1*

- *Activity name : StartActivity*
- *Activity description : This activity is the start of the app. It enables/prompts the user to either login or create a new player in order to start the game. This is the preliminary step before starting the game. Here the user will either click on login after entering the username or clickon create a new player in order to go to the next screen.*
- *Entrance criteria : For login, we need username.*
- *Exit criteria : Once user is logged in, he/she will get the confirmation that user is logged in successfully. The user will click create a new player if the user is not created.*

*Activity 2*

- *Activity name : PlayerInfoActivity*
- *Activity description : Here new player will provide the required details in order to register and start playing the games*
- *Entrance criteria : This screen will emerge when create a new player is clicked in Activity 1. The inputs required in order to create a new player is firstname,lastname,email and username.*
- *Exit criteria : New player will submit the required infromation and the player will be created.*

*Activity 3*

- *Activity name : MenuActivity*
- *Activity description : Here Player will have different options to choose, where player can select any one of the options and continue with their game.*
- *Entrance criteria : Once player is logged in, then player will enter this activity which has different options like create puzzle, playe puzzle, create Tournament, join or continue Tournament and show Statistics. Also player has another option called Exit where the player can exit out of the game.*
- *Exit criteria : When player clicks on any of the above mentioned options will redirect to the respective activities.*

*Activity 4*

- *Activity name : CreatePuzzleActivity*
- *Activity description : In this activity the player will create the puzzle with certain criteria.*
- *Entrance criteria : When the player will click create puzzle in Activity 3 then player will enter this activity, where he/she can create the puzzle. Inputs required for this activtiy is Phrase and maximum number of allowed wrong guesses where the player will create the phase and set the allowed wrong guesses to some digit limit.*
- *Exit criteria : Once player create the puzzle then the puzzle will saved and created successfully.*

*Activity 5*

- *Activity name : SolveAPuzzleActivity*
- *Activity description : In this activity player will play this puzzle by solving the word phrase by guessing the vowels and consonants. The player can only guess the word with some limit, which has been set by the puzzle creator.*
- *Entrance criteria : When the player will click solve puzzle option in Activity 3 then player will enter this activity, where he/she can solve the puzzle. Inputs required for this activity are, display puzzle phrase where the player has to guess it by entering either vowels or consonants, and exit button, if the player is willing to exit out of the game.*
- *Exit criteria : Player will successfully complete the puzzle or player opt to exit the puzzle (interuption) or the game is ended where the player is unsuccessful in guessing the phrase.*

*Activity 6*

- *Activity name : CreateATournamentActivity*
- *Activity description : In this activity player will create a list tournament where the player can select any of the puzzles in that tournament and start to play.*
- *Entrance criteria : When the player will click create a tournament option in Activity 3 then player will enter this activity, where he/she can create a tournament with list of puzzles. Inputs needed for this activity are name of the tournament and number of puzzles in the list.*
- *Exit criteria : Recieve confirmation that the tournament has been created successfully or if the player clicks exit button then the player will be returned to menu options.*

*Activity 7*

- *Activity name : JoinOrContinueATournamentActivity*
- *Activity description : In this activity player will either join the tournament or continue the tournament from the menu activity.*
- *Entrance criteria :When the player will click join or continue tournament option in Activity 3 then player will enter this activity, where he/she can join a tournament if the player is new to the tournament. Inputs needed for this activity are displaying list of tournaments where player can choose any tournament in order to play. If the player is willing to continue the tournament then player will get to see list of tournament the player was playing and the puzzle which the player has left unsolved.Inputs required for this activity are list of available tournaments and list of joined tournmaments.*
- *Exit criteria : Player selects any one tournament and will go to solve a puzzle.*

*Activity 8*

- *Activity name : ShowStatisticsActivity*
- *Activity description : In this activity player will view the statistcs like which players have played the puzzles/tournaments and who won the highest prize in the puzzle as well as in tournament by displaying the player's username.*
- *Entrance criteria : When the player will click show statistics option in Activity 3 then player will enter this activity, where he/she can view the statistics. Inputs required for this activity are list of puzzles played, list of tournaments played, how many players have played the puzzles, how many players have played the tournaments, player who won highest prize in puzzle by displaying the username, player who won the highest prize in tournaments by displaying the username.*
- *Exit criteria : Once player checks the statistics, then player will press exit button to return to the menu.*

## 3 Team

- *Team members' names*
    - *Abhilasha Hegde, Yuhao Lan, Maritza Mills*
- *Roles, with a short description of each role*

    - *Project Manager : A project manager is a person who has the overall responsibility for the successful initiation, planning, design, execution, monitoring, controlling and closure of a project. The project manager must have a combination of skills including an ability to ask penetrating questions, detect unstated assumptions and resolve conflicts, as well as more general management skills.*
    
    - *Design Engineer : Design engineers study, research and develop ideas for new products and the systems used to make them. They also modify existing products or processes to increase efficiency or improve performance. Design engineers are not only concerned with making products that look good and are easy and safe to use: they are also concerned with ensuring that the product can be made cost-effectively and efficiently.*
    
    - *Android Developer: Android Developers translate designs and wireframes into high quality code. They also design, build, and maintain high performance, reusable, and reliable Java code. They ensure the best possible performance, quality, and responsiveness of the application. They also help maintain code quality, organization, and automatization.*
    
    - *Quality Assurance Engineer : QA engineer responsibilities include designing and implementing tests, debugging and defining corrective actions. They also review requirements, specifications and technical design documents to provide timely and meaningful feedback. They also create detailed, comprehensive and well-structured test plans and test cases.*

- *Table showing which team member(s) has which role(s)*

  |       Roles        |       Team Member    |
  | ---------------    | --------------------  |
  |         Project Manager           |     Yuhao Lan                  |
  | Design Engineer        |       Abhilasha Hegde               |
  |  Android Developer  |  Yuhao Lan , Abhilasha Hegde , Maritza Mills |
  | QA Engineer   |   Maritza Mills |
  
  
  
