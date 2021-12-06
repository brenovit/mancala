const NavBar = {
  template: `
  <header>    
    <div class="nav-bar">
      <ul>
        <li :class="{ active: isActiveRoute('home') }"><router-link to="/">Home</router-link></li>        
        <li :class="{ active: isActiveRoute('create') }"><router-link to="/game/create">Create Game</router-link></li>
        <li :class="{ active: isActiveRoute('join') }"><router-link to="/game/join">Join Game</router-link></li>
        <li :class="{ active: isActiveRoute('about') }"><router-link to="/about">About it</router-link></li>
        <li class="login"><input v-model="userName" placeholder="Add your name here" /><button @click="login">Save</button></li>
      </ul>
    </div>
    <h2 class="welcome" v-if="currentUser"> Welcome user [{{ currentUser?.name }}] id [{{ currentUser?.id }}] </h2>
  </header>
`,
setup(){
  const userName = Vue.ref("") 
  const store = Vuex.useStore();
  const currentUser = Vue.computed(() => store.state.currentUser);
  const currentRouteName = Vue.computed(() => { return VueRouter.useRoute().name })

  function login(){
    console.log('login: '+userName.value);
    store.dispatch('login', userName.value);
    alert('logged as: '+userName.value);
  }
  function isActiveRoute(name){
    return this.currentRouteName === name;
  }

  return {
    userName,
    currentUser,
    login,
    currentRouteName,
    isActiveRoute
  }
}
};