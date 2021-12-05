const NavBar = {
  template: `
  <header>    
    <div class="nav-bar">
      <ul>
        <li :class="{ active: isActive('home') }"><router-link to="/">Home</router-link></li>        
        <li :class="{ active: isActive('create') }"><router-link to="/create">Create Game</router-link></li>
        <li :class="{ active: isActive('join') }"><router-link to="/join">Join Game</router-link></li>
        <li :class="{ active: isActive('about') }"><router-link to="/about">About it</router-link></li>
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
computed: {
  currentRouteName() {
      return this.$route.name;
  }
},
methods: {
  login(){
    alert('logged as: '+this.userName);
  },
  isActive(name){
    return this.currentRouteName === name;
  }
}
};