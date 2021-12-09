import router from './routes.js'
import store from './store.js';

const app = Vue.createApp({
    components:{
        'app': App,
    }
});

app.component('nav-bar', NavBar);
app.component('welcome-message', WelcomeMessage);
app.component('footer-message', FooterMessage);

app.use(router);
app.use(store);

app.mount('#app');