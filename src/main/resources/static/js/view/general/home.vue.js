const Home = {
    template: `
    <div>
    <game-rules></game-rules>
    </div>    
    `,
    data() {
            return {
            }
        },
    components: {
        'game-rules':RulesGame
    }
}