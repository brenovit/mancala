const NavBar = {
  template: `
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
      <router-link class="navbar-brand" to="/">
        <img src="images/logo2.png" height="50"/>
      </router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" 
      aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('home') }">
            <router-link class="nav-link" to="/">Home</router-link>
          </li>
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('create') }">
            <router-link class="nav-link" v-bind:disabled="isNotLogged"  to="/game/create">Create Game</router-link>
          </li>
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('join') }">
            <router-link class="nav-link" v-bind:disabled="isNotLogged"  to="/game/join">Join Game</router-link>
          </li>
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('about') }">
            <router-link class="nav-link" to="/about">About it</router-link>
          </li>
        </ul>
        <form class="form-inline mt-2 mt-md-0">
          <input class="form-control mr-sm-2" type="text" v-model="userName" placeholder="Add your name here" aria-label="Login">
          <button class="btn btn-outline-success my-2 my-sm-0" type="button" @click="login" v-bind:disabled="!isNotLogged">Login</button>
        </form>
      </div>
    </nav>  
  `,
  data() {
    return {
      userName: ''
    }
  },
  mounted() {
    this.loadUserName();
  },
  computed: {
    currentUser(){
      return this.$store.state.currentUser;
    },
    currentRouteName(){
      return VueRouter.useRoute().name;
    },
    isNotLogged(){
      return this.currentUser === null || this.currentUser === undefined;
    },
  },
  methods: {
    login(){
      this.$store.dispatch('login', this.userName);
      alert('logged as: '+this.userName);
    },
    isActiveRoute(name){
      return this.currentRouteName === name;
    },
    loadUserName(){
      if(!this.isNotLogged){
        this.userName = this.currentUser.name;
      }
    }
  }
};