const NavBar = {
  template: `  
  <header>    
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
            <router-link class="nav-link" v-bind:class="{ disabled: isNotLogged() }"  to="/game/create">Create Game</router-link>
          </li>
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('join') }">
            <router-link class="nav-link" v-bind:class="{ disabled: isNotLogged() }"  to="/game/join">Join Game</router-link>
          </li>
          <li class="nav-item" v-bind:class="{ active: isActiveRoute('about') }">
            <router-link class="nav-link" to="/about">About it</router-link>
          </li>
        </ul>
        <form class="form-inline mt-2 mt-md-0">
          <input class="form-control mr-sm-2" type="text" v-model="userName" placeholder="Add your name here" aria-label="Login">
          <button class="btn btn-outline-success my-2 my-sm-0" type="button" @click="login">Login</button>
        </form>
      </div>
    </nav>
    <br />
    <h2 class="welcome" v-if="currentUser"> Welcome user [{{ currentUser?.name }}] id [{{ currentUser?.id }}] </h2>
  </header>
  `,
  data() {
    return {

    }
  },
  setup(){
    const store = Vuex.useStore();
    const currentUser = Vue.computed(() => store.state.currentUser);
    const currentRouteName = Vue.computed(() => { return VueRouter.useRoute().name })
    let userName = Vue.ref("");
    
    function login(){
      store.dispatch('login', userName.value);
      alert('logged as: '+userName.value);
    }

    function isActiveRoute(name){
      return currentRouteName === name;
    }

    function isNotLogged(){
      return currentUser === null || currentUser === undefined;
    }

    return {
      userName,
      currentUser,
      login,
      currentRouteName,
      isActiveRoute,
      isNotLogged
    }
  }
};