const JoinGame = {
    template: `
    <h1>Join Game</h1>
    <hr />
    <table>
        <thead>
            <th>#</th>
            <th>player name</th>
            <th>game id</th>
            <th>game name</th>
            <th>game status</th>
            <th>spectors</th>
            <th>action</th>
        </thead>

        <tbody>
            <tr v-for="(game,index) in games" :key="game.id">
                <td data-label="#">{{index}}</td>
                <td data-label="player name">{{game.player1.name}}</td>
                <td data-label="game id">{{game.id}}</td>
                <td data-label="game name">{{game.name}}</td>
                <td data-label="game status">{{game.status}}</td>
                <td data-label="spectors">{{game.watchers?.length}}</td>
                <td data-label="action">
                    <template v-if="isGameNotFinished(game)">
                        <button @click="joinGame(game.id)" class="btn btn-success btn-sm"><i class="bi-joystick"></i> Play</button>&nbsp;
                        <button @click="watchGame(game.id)" class="btn btn-info btn-sm"><i class="bi-eye"></i> Watch</button>
                    </template>
                </td>
            </tr>
        </tbody>

    </table>
    `,
    data() {
        return {
            games: []
        }
    },
    mounted() {
        this.findGames();
    },
    methods:{
        findGames(){
            gameApi.findAll()
            .then(r => {
                this.games = r.data;
            })
            .catch(e => {
                alert(e);
                console.error(e);
            });
        },
        isGameNotFinished(game){
            return game.status !== "FINISHED";
        },
        joinGame(gameId){
            const request = {
                gameId,
                player: this.$store.state.currentUser
            }
            gameApi.join(request)
            .then(r => {
                this.$router.push('/game/play/'+gameId);
            })
            .catch(e => {
                alert(e.response?.data?.code + " : "+e.response?.data?.message);
                console.error(e?.response?.data);
            });
        },
        watchGame(gameId){
            const request = {
                gameId,
                player: this.$store.state.currentUser
            }
            gameApi.spectate(request)
            .then(r => {
                this.$router.push('/game/play/'+gameId);
            })
            .catch(e => {
                alert(e.response.data.code + " : "+e.response.data.message);
                console.error(e.response.data);
            });
        }
    }
}