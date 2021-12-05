const Home = {
    template: `
    <div>
    <game-rules></game-rules>
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
                    }
                ]
            }
        },
    components: {
        'game-rules':RulesGame
    }
}