const About = {
    template: `
    <h1> About id</h1>
    <div>
    <p>Board design <a href="https://codepen.io/ChiliTomatoNoodle">Mancala Board</a> from <a href="https://codepen.io/ChiliTomatoNoodle">@ChiliTomatoNoodle</a></p>
    <p>Logo <a href="https://thenounproject.com/icon/stone-games-3312354/">stone games</a> from <a href="https://thenounproject.com/shmai.com/">Azam Ishaq</a></p>
    <p>Game logic <a href="https://github.com/brenovit/mancala">brenovit</a></p>
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