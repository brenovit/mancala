var Settings =   Vue.defineAsyncComponent( () => loadModule('component/Settings.vue', options) );
var Home = loadModule('component/Home.vue', options);
var App = loadModule('component/App.vue', options);

var routes = [
    { path:'/', component: Home },
    { path:'/settings', component: Settings }
 ];

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})

const app = Vue.createApp({
    component: App
});
app.use(router);

app.mount('#app');

/*
Vue.config.productionTip = false*/