const About = {
    template: `
    <h1> About it</h1>
    <div>
    <p>Board design <a target="_blank" href="https://codepen.io/ChiliTomatoNoodle/pen/LOaPmy">Mancala Board</a> from <a target="_blank" href="https://codepen.io/ChiliTomatoNoodle">@ChiliTomatoNoodle</a></p>
    <p>Logo <a target="_blank" href="https://thenounproject.com/icon/stone-games-3312354/">stone games</a> from <a target="_blank" href="https://thenounproject.com/shmai.com/">Azam Ishaq</a></p>
    <p>Game logic <a target="_blank" href="https://github.com/brenovit/mancala">brenovit</a></p>
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