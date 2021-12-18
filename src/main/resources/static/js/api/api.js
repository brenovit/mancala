const httpClient = axios.create({
    baseURL: BASE_URL + '/games',
    timeout: 20000,
    headers: {'Content-Type': 'application/json'}
});

const gameApi = {
    findAll(){
        return httpClient.get('')
    },
    findById(gameId){
        return httpClient.get(`/${gameId}`)
    },
    create(request){
        return httpClient.post('',{
            name: request.gameName,
            seeds: request.seeds,
            player: {
                id: request.player.id,
                name: request.player.name,
            }
        })
    },
    join(request){
        return httpClient.post(`join/${request.gameId}`,{
            id: request.player.id,
            name: request.player.name,
        })
    },
    start(request){
        return httpClient.post(`start/${request.gameId}`)
    },
    sow(request){
        return httpClient.post(`sow/${request.gameId}`,{
            playerId: request.playerId,
            holePosition: request.holePosition
        })
    },
    spectate(request){
        return httpClient.post(`spectate/${request.gameId}`,{
                id: request.player.id,
                name: request.player.name
        })
    },
    abandon(request){
        return httpClient.post(`abandon/${request.gameId}`,{
                id: request.player.id,
                name: request.player.name
        })
    }
}