var routes = [
    { path:'/', component: Home },
    { path:'/settings', component: Settings }
 ];

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})

const app = Vue.createApp({
    components:{
        'app': App
    }
});

app.use(router);

app.mount('#app');