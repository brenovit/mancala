const NavBar = {
  template: `
  <header>
    <div class="nav-bar">
      <ul>
        <li><router-link to="/">Home</router-link></li>        
        <li><router-link to="/create">Create Game</router-link></li>
        <li><router-link to="/join">Join Game</router-link></li>        
        <li class="username"><input v-model="userName" placeholder="Add your name here" /><button @click="login">Save</button></li>
      </ul>
    </div>
    <h2 class="welcome"> Welcome player name | #id123 </h2>
  </header>
`,
data(){
  return {
    userName: ''
  }
},
methods: {
  login(){
    alert('logged as: '+this.userName);
  }
}
};