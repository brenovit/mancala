import AuthGuard from "./utils/auth.guard.js";

var routes = [
    { path:'/', component: Home, name: 'home' },
    { path:'/game', name: 'game', meta: { loginRequired: true }, component: IndexGame, redirect: { name: 'create'},
        children: [
            { path:'create', component: CreateGame, name: 'create' },
            { path:'join', component: JoinGame, name: 'join' },
            { path:'play/:id', component: PlayGame, name: 'play' }
        ]
    },
    { path:'/about', component: About, name: 'about' }
 ];


const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
})

router.beforeEach(AuthGuard);

export default router;