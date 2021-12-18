const CreateGame = {
    template: `
    <h1>Create Game</h1>
    <hr />
    <div>
        <form @submit.prevent="createGame">
            <div class="form-group">
                <label for="gamName">Name</label>
                <input class="form-control" id="gamName" placeholder="Game name" v-model="name" required>
            </div>
            <div class="form-group">
                <label for="gameSeeds">Seeds</label>
                <select class="form-control" id="gameSeeds" v-model="seeds">
                    <option :value="4">4</option>
                    <option :value="6" selected="true">6</option>
                    <option :value="7">7</option>
                    <option>10</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary"><i class="bi-box-arrow-down"></i> Submit</button>
        </form>
    </div>

    `,
    data() {
        return {
            name: '',
            seeds: 6,
            holes: 6
        }
    },
    methods:{
        createGame(){
            const  request = {
                gameName: this.name,
                seeds: this.seeds,
                player: this.$store.state.currentUser
            }
            gameApi.create(request).then(r => {
                this.$router.push('/game/play/'+r.data.id);
            })
            .catch(e => {
                alert(e.response.data.code + " : "+e.response.data.message);
                console.error(e.response.data);
            });
        }
    }
}