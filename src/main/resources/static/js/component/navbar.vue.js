const NavBar = {
  template: `
  <header>
    <div class="nav-bar">
      <ul>
        <li><router-link to="/">Home</router-link></li>
        <li><router-link to="/settings">Join Game</router-link></li>
        <li><router-link to="/">Create Game</router-link> </li>
        <li><router-link to="/">Your Active Games</router-link></li>
        <li class="float-right"><a href="#">user-name</a></li>
      </ul>
    </div>
    <h1 class="game-name"> Game Name | id Z created by </h1>
  </header>
`
};