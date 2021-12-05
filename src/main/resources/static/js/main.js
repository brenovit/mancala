var routes = [
    { path:'/', component: Home },
    { path:'/join', component: JoinGame },
    { path:'/create', component: CreateGame }
 ];

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})

const app = Vue.createApp({
    components:{
        'app': App,
    }
});

app.component('nav-bar', NavBar);

app.use(router);

app.mount('#app');