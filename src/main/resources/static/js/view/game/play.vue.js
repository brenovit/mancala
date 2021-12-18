const PlayGame = {
  components:{
    BoardGame
  },
  template: `
  <div>
    <div v-if="gameWaiting">
      <p class="text-justify">
        Waiting for a opponent! Invite a friend to play with you informing the game name <strong>{{ game?.name }}</strong> 
        or the game id <strong>{{ game?.id }}</strong>
      </p>
    </div>
    <div v-else-if="gameReady">
      <template v-if="isOwner">
        <div clas="row">
          <div clas="col">
            <p class="text-justify">
              The game is Ready! Player <strong>{{ opponentName }}</strong> joined the game. Let's sow!
            </p>
          </div>
          <div clas="col">
            <button class="btn btn-outline-info my-2 my-sm-0 float-right" type="button" @click="startGame">Start</button>
          </div>
        </div>
      </template>
      <template v-else>
        <div clas="row">
          <div clas="col">
            <p class="text-justify">
              The game is Ready you joined <strong>{{ opponentName }}</strong> game. Waiting until it starts!
            </p>
          </div>
        </div>
      </template>
    </div>
    <div v-else-if="gameStarted">
      <div clas="row">
        <div clas="col">
          <h4 class="text-center"><strong>{{ currentUser.name }}</strong> vs <strong>{{ opponentName }}</strong> at <strong>{{ game.name }}</strong>!</h4>
        </div>
      </div>
      <div clas="row">
        <div clas="col">
          <board-game :game="game"></board-game>
          <br />
          <h5 class="text-center" v-if="isMyTurn">Your turn</h5>
        </div>
      </div>
      <br />
      <div clas="row">
        <div clas="col">
          <button class="btn btn-outline-danger my-2 my-sm-0 float-right" type="button" @click="abandonGame"><i class="bi-box-arrow-right"></i> Abandon</button>
        </div>
      </div>
    </div>
    
  </div>
  `,
  data() {
    return {
      game: {
        status: ''
      },
      stompClient: null,
      socket: null
    }
  },
  computed: {
    currentUser(){
      return this.$store.state.currentUser;
    },
    gameWaiting(){
      return this.game?.status === "WAITING";
    },
    gameReady(){
      return this.game?.status === "READY";
    },
    gameStarted(){
      return this.game?.status === "STARTED";
    },
    isMyTurn(){
      return this.game?.turn.id === this.currentUser.id;
    },
    opponentName(){
      if(this.game.player1.id !== this.currentUser.id){
        return this.game.player1.name;
      }
      return this.game.player2.name;
    },
    isOwner(){
      return this.currentUser.id === this.game.player1.id;
    }
  },
  mounted(){
    this.loadGame();
    this.checkSocket();
  },
  unmounted(){
    this.disconnectSocket();
  },
  watch: {
    game: function (newGame, oldGmae) {
      if(newGame.status === 'FINISHED'){
        this.finishGame()
      }
    }
  },
  methods: {
    loadGame(){
      gameApi.findById(this.$route.params.id)
      .then(r => {
          this.game = r.data;
          this.connectStomp();
      })
      .catch(e => {
        if(e.response){
          alert(e.response.data.code + " : "+e.response.data.message);
          console.error(e.response.data);
          this.$router.push('/');
        }else{
          console.error(e);
        }
      });
    },
    connectStomp(){
      this.socket = new SockJS(BASE_URL + "/gameplay");
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.debug = () => {};
      this.stompClient.connect({}, 
        this.stompSubscribe,
        error => {
          console.log(error);
        }
      );

      this.socket.onclose = () =>{
        if(this.stompClient){
          this.stompClient.disconnect(() => {
            console.log("status frame: DISCONNECTED");
          });
        }
      }
    },
    stompSubscribe(frame){
      console.log("status frame: " + frame);
      let _this = this;
      this.stompClient.subscribe("/topic/game-progress/" + this.game.id, function (response) {
        _this.game = JSON.parse(response.body);            
      })
    },
    startGame(){
      gameApi.start({gameId: this.game.id})
        .then(r => {
            this.game = r.data;
        })
        .catch(e => {
            alert(e.response.data.code + " : "+e.response.data.message);
            console.error(e.response.data);
        });
    },
    abandonGame(){
      if(confirm('If you leave you will lose the game. Are you sure?')){
        const request = {
          gameId: this.game.id,
          player: this.currentUser
        }
        gameApi.abandon(request)
        .then(r => {
            this.game = r.data;
            this.finishGame();
        })
        .catch(e => {
            alert(e.response.data.code + " : "+e.response.data.message);
            console.error(e.response.data);
        });
      }
    },
    finishGame(){
      if(this.game.message){
        alert(this.game.message);
      }
      this.disconnectSocket();
      this.$router.push('/');
    },
    disconnectSocket(){
      if (this.socket) {
        this.socket.close();
      }
    },
    checkSocket(){
      if(this.socket){
        this.socket.close();
      }
    }
  }
}