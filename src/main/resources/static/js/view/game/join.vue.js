const JoinGame = {
    template: `
    <div>
    <table>
  
  <thead>
    <th>#</th>
    <th>player name</th>
    <th>game name</th>
    <th>game status</th>
    <th>spectors</th>
    <th>action</th>
  </thead>
  
  <tbody>
    <tr v-for="game in games" :key="game.id">
      <td data-label="player name">{{game.id}}</td>  
      <td data-label="player name">{{game.player.name}}</td>
      <td data-label="game name">{{game.name}}</td>
      <td data-label="game status">{{game.status}}</td>
      <td data-label="spectors">{{game.spectors}}</td>
      <td data-label="action"><button @click="joinGame(game.id)">Join</button>|<button @click="watchGame(game.id)">Watch</button></td>
    </tr>
  </tbody>
  
</table>
    </div>    
    `,
    data() {
            return {
                games: [
                    {
                        id: 'gid1',
                        name: 'test game',
                        status: 'waiting',
                        player: {
                            id: 'pid1',
                            name: 'p 1',
                        },
                        spectors: 3
                    },
                    {
                        id: 'gid2',
                        name: 'test game 2',
                        status: 'waiting',
                        player: {
                            id: 'pid2',
                            name: 'p 2',
                        },
                        spectors: 0
                    },
                    {
                        id: 'gid3',
                        name: 'test game 3',
                        status: 'in progress',
                        player: {
                            id: 'pid3',
                            name: 'p 3',
                        },
                        spectors: 9
                    },
                    {
                        id: 'gid4',
                        name: 'test game4',
                        status: 'waiting',
                        player: {
                            id: 'pid1',
                            name: 'p 1',
                        },
                        spectors: 3
                    },
                    {
                        id: 'gid5',
                        name: 'test game 5',
                        status: 'waiting',
                        player: {
                            id: 'pid2',
                            name: 'p 2',
                        },
                        spectors: 0
                    },
                    {
                        id: 'gid6',
                        name: 'test game 6',
                        status: 'in progress',
                        player: {
                            id: 'pid3',
                            name: 'p 3',
                        },
                        spectors: 9
                    }
                ]
            }
        },
    methods:{
        joinGame(gameId){
            alert('Joining game '+gameId);
            this.$router.push('/game/play');
        },
        watchGame(gameId){
            alert('Watching game '+gameId);
            this.$router.push('/game/play');
        }
    }
}