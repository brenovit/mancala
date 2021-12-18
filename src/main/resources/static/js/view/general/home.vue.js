const Home = {
    template: `
    <h1>Home</h1>
    <hr />
    <div class="row">
       <div class="col-md-12">
          <p class="text-justify">Kalah, also called Kalaha or Mancala is a game in the mancala family invented in the 
          United States by William Julius Champion, Jr. in 1940. This game is sometimes also 
          called "Kalahari", possibly by false etymology from the Kalahari desert in Namibia. 
          <span><a href="https://en.wikipedia.org/wiki/Kalah" target="_blank">Wikipedia</a></span></p>
       </div>
    </div>
    <game-rules></game-rules>
    `,
    data() {
            return {
            }
        },
    components: {
        'game-rules':RulesGame
    }
}