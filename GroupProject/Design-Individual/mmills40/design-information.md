## Design Information ##
Maritza Mills, @mmills40

## Requirements ##
1. **When starting the application, a user may choose to either create a new player or log in.  For simplicity in designing the application, you should consider the application to run locally; that is, only players on the same device will be able to share information (e.g., puzzles, tournaments, scores). In addition, you do not need to consider authentication or security requirements; that is, a (unique) username will be sufficient for logging in.**
*Class User and Player are represented in the UML diagram Username is recognized as an attribute of a player.*
2. **After logging in, the application shall allow players to (1) create a puzzle phrase, (2) solve a random puzzle, (3) create a tournament, (4) join or continue a tournament, and (5) view the puzzle statistics.**
*Puzzle, Tournament and Statistics are represented as classes in the UML diagram. A puzzle phrase is recognized as an attribute of the Puzzle class. The relationship between Players using Puzzles is demonstrated. Operations are represented for the user*
3. **The application shall maintain an underlying database to save persistent information across runs (e.g., players, puzzles, statistics, tournament information).**
*I did not consider this requirement because the database will only persist data already present in the design.*
4. **When creating a new player, a user will:
  a. Enter the player’s first name.
  b. Enter the player’s last name.
  c. Enter the player’s desired username.
  d. Enter the player’s email.
  e. Submit the information.
  f. Either receive a confirmation that the information is saved and return to the menu
  or
  receive an error if the username is already taken on that device and be offered the option to pick a different username.
A player cannot be edited or deleted after a successful save.**
*these attributes are represented as a part of the Player class.*
5. **To create a puzzle, the player will:
  a. Enter a phrase.
  b. Enter the maximum number of allowed wrong guesses a user can make before losing the game, between 0 and 10.
  c. Save and view the returned unique identifier for the puzzle. The puzzle may not be further edited after this point.**
*Phrases, unique identifier and maximum number of allowed wrong guesses are both represented as attributes of the Puzzle class in the UML diagram.*
6. **When a player starts solving a puzzle, whether selected randomly or belonging to a tournament, the game will:
  a. Display the puzzle phrase, where (1) all non-alphabetic characters (e.g., numbers or punctuation) are shown, and (2) regular letters are replaced by blanks. The game should also display a list of all letters not yet chosen, the total prize, with an initial value of $0, and the remaining number of allowed wrong guesses, initialized to the maximum number of allowed wrong guesses chosen by the puzzle creator (see above).
  b. Allow the player to choose, at every turn, whether to guess a consonant, buy a vowel, or solve the puzzle.
    i. Guessing a consonant will show the player a randomly chosen prize value that is a multiple of $100 and is between $100 and $1000. If the guess is correct (i.e., the consonant is in the puzzle), all the occurrences of the consonant in the puzzle will be revealed, and the total prize will be increased by the prize value times the number of such occurrences.  
    ii. Buying a vowel will cost $300 of the player’s total prize and will result in revealing all instances of that vowel in the puzzle.
    iii. If a vowel or a consonant are guessed incorrectly (i.e., the guessed letter is not present in the puzzle), the remaining number of allowed wrong guesses is decremented. If the number goes below zero, the player gets a prize of $0 for that puzzle, and the game ends.
    iv. If a player selects to solve the puzzle and is successful, he/she will score $1000 for each letter not yet revealed, and his/her total prize will be recorded and associated to that puzzle and player. Conversely, if a player tries to solve the puzzle and is unsuccessful, he/she gets a prize of $0 for that puzzle, and the game ends.**
*The Prize class is represented in the UML diagram with attribute Value. The relationship between players and tournaments is demonstrated. Tournaments add players. Prize demonstrates operations that increment and decrement prizes. The association between Puzzles and prizes is represented.*
7. **If a player interrupts a puzzle (e.g., by explicitly choosing to exit the game while solving a puzzle), the game must give the player the option to continue. If the player confirms that he/she wants to exit, he/she gets a prize of $0 for that puzzle, and the game ends.**
8. **When a player selects to solve a random puzzle, the game will not chose puzzles he/she has created or already successfully/unsuccessfully played.**
9. **When a player selects a tournament for which he/she has already played some of the puzzles, the game will consider these puzzles already completed and preserve the prize the player won (including $0 for puzzles the player quit or did not successfully solve).**
10. **To create a tournament, a player will:
  a. Select 1 to 5 puzzles from a list of puzzles that they have either created or already played.
  b. Enter a name for the tournament.
  c. Either receive a confirmation that the tournament has been created and return to the menu
  or 
  receive an error if the tournament name is already taken and be offered the option to pick a different name.
	At this point, the tournament will be available for others to join.**
*The relationship between multiple puzzles in a single tournament is represented as an aggregation. The relationship of puzzles being in more than one tournament is clarified. The attribute of name for tournament is represented in tournagement class.*
11. **To play a tournament, a player can select whether to join a new tournament or continue a tournament he/she has already joined.
  a. If the player opts for joining a new tournament, the game will show the player a list of tournaments that are currently available for him/her to join (i.e., all puzzles not created and not yet played by the player all tournaments that contain (1) no puzzles created by the player and (2) at least one puzzle not yet played by the player). When the player chooses a tournament in the list, the game will display the first puzzle in the tournament.
  b. If the player opts for continuing a tournament they have already joined, the game will show the player a list of tournaments they are currently playing that still have puzzles not completed by the player. When the player chooses a tournament in the list, the game will display the first unsolved puzzle in that tournament.
  d. After a player completes the last puzzle in a tournament, the tournament ends (for that player), and the game stores the overall tournament prize of the player, which is the sum of the player’s total prizes in all the puzzles in the tournament.**
12. **When a player opts to view the puzzle statistics, the game will show four pieces of information:
  a. The list of puzzles completed by that player with, for each puzzle, the prize the player won (including $0 for puzzles he/she quit or did not successfully solve).
  b. The list of tournaments completed by that player with, for each tournament, the prize the player won.
  c. The complete list of puzzles with, for each puzzle, (1) the number of players who played it and (2) the top prize won by a player for that puzzle, together with the username of that player.
  d. The complete list of tournaments with, for each tournament, (1) the number of players who completed the tournament and (2) the top prize won by a player for that tournament, together with the username of that player.**
 *The attribute of number of players and the top prize won by players is represented in the tournament class. The relationship between a player and statistics about that player are represented as an association. Statistics store information about top prize as an attribute, as well as a list of tournaments and puzzles.*
13. **The User Interface (UI) shall be intuitive and responsive.**
*I did not consider this requirement because the instructinos explicitly say that GUI classes do not need to be considered.
14. **The performance of the game should be such that players does not experience any considerable lag between their actions and the response of the game.**
*This would be better suited for a behavioral diagram, not a class diagram. I did not consider this requirement because it does not directly affect any objects in the class diagram nor affect any representation in the operations that need to be represented. This is a performance metric to optimize after the system is built.*


