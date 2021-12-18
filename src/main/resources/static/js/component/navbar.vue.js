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
        <form class="form-inline mt-2 mt-md-0" @submit.prevent="login">
          <template v-if="isNotLogged">
            <input class="form-control mr-sm-1" type="text" v-model="userName" placeholder="Add your name here" aria-label="Login" v-on:keyup.enter="login" v-bind:disabled="!isNotLogged"/>
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit" v-bind:disabled="!isNotLogged">Login</button>
          </template>
          <template v-else>
            <input class="form-control mr-sm-2" type="text" :value="currentUser?.id" aria-label="userid" readonly/>
            <input class="form-control mr-sm-2" type="text" :value="currentUser?.name" aria-label="username" readonly/>
            <button class="btn btn-outline-danger my-2 my-sm-0" type="button" @click="logout" v-bind:disabled="isNotLogged">Logout</button>
          </template>
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
    },
    logout(){
      this.userName = '';
      this.$store.dispatch('logout');
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