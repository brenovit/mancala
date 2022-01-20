
![Logo](image_logo.png?raw=true "Playing Game")
# Kalah/Mancala
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mancala-kalah&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mancala-kalah)

## Description
Kalah, also called Kalaha or Mancala is a game in the mancala family invented in the United States by William Julius Champion, Jr. in 1940.

### The Game
Each of the two players has his six pits in front of him. To the right of the six pits, each player has a larger pit(pot). At the start of the game, there are six stones(can be more) in each of the six round pits.

### Rules
#### Game Play
The player who begins with the first move picks up all the stones in any of his own six pits, and sows the stones on to the right, one in each of the following pits, including his own big pit. 

No stones are put in the opponents' big pit. If the player's last stone lands in his own big pit, he gets another turn. This can be repeated several times before it's the other player's turn.

#### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone lands in an own empty pit, the player captures his own stone and all stones in the opposite pit (the other player’s pit) and puts them in his own big pit.

#### The Game Ends
The game is over as soon as one of the sides runs out of stones. The player who still has stones in his pits keeps them and puts them in his big pit. The winner of the game is the player who has the most stones in his big pit.

## Galery
![Playing Game](image_play_game.png?raw=true "Playing Game")
Link: https://mancala-breno.herokuapp.com/#/

## Project
This is a SpringBoot project with embedded VueJS, using WebSocket translating Mancala/Kalah game rules.

### Structure
    src/main
    ├─ java/com/bol/brenovit/mancala
    ├─ core                  # Main business rules (domain)
    │  └─ common             # Module used by all domains, usually domain-agnostic 
    ├─ entrypoint            # REST layer
    ├─ gateway               # Connection with external resources (database, messaging, apis)
    ├─ infraestrucure        # Application configurations, usually business agnostic
    └─ resources             # Application resources (properties)
       └─ static             # Folder following thymeleaf strucute
          ├─ css             # All styles
          ├─ images          # All images
          └─ js              # All JS realated files
             ├─ api          # Scrip for REST connection using axios
             ├─ component    # Vue Components or classes used by the project
             ├─ utils        # Generic or global scripts
             └─ view         # All pages

### Libs
- Backend  
  - Java 11
    - Spring Boot : 2.6.1
      - Thymeleaf
      - Web
      - Websocket
- Frontend (embedded)
  - VueJS : 3.2.23
    - Vuex : 4.0.0
    - Vue-Router : 4.0.5
  - JQuery : 3.5.1
  - SocketJs : 1.4.0
  - Stomp : 2.3.3
  - Bootstrap : 4.6.1

### External resources
To build this project, it was used different resources from the internet:

- Board design [Mancala](https://codepen.io/ChiliTomatoNoodle/pen/LOaPmy) Board from [@ChiliTomatoNoodle](https://codepen.io/ChiliTomatoNoodle)
- Logo [stone games](https://thenounproject.com/icon/stone-games-3312354/) from [Azam Ishaq](https://thenounproject.com/shmai.com/)
## Usage
To execute the aplication on local environment run:
> ./mvnw clean spring-boot:run

It will compile the project and start `MancalaApplication.java`

Access http://localhost:8080/

### Deploy
Since the frontend is not decoupled from backend, you need to change the `BASE_URL` const value, located at `../js/utils/const.js`, with the host you will use, like:
``` js
const BASE_URL = 'https://mynewhost.com/mancala';
```

## Roadmap
- [ ] Convert Frontend to use npm structure
- [ ] Split frontend from backend
- [ ] Add NoSQL database
- [ ] Update Join Game page data using socket
- [ ] Add tests at API level
- [ ] Create Authentication/Authorization at backend side
- [ ] Make the board flexible to amount of pits
- [ ] Add docker
- [ ] Create AI for single player
- [ ] Add game mode that shows the amount of seeds per pit
- [ ] Fix bug that doens't show the last seed distribution at the end of the game
- [ ] Add Log lib
- [ ] Make the game flexible to support more than 2 players
