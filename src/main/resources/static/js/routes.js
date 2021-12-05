import AuthGuard from "./utils/auth.guard.js";

var routes = [
    { path:'/', component: Home, name: 'home' },
    { path:'/game', name: 'game', meta: { loginRequired: true },
        children: [
            { path:'/join', component: JoinGame, name: 'join' },
            { path:'/create', component: CreateGame, name: 'create' },
            { path:'/board', component: BoardGame, name: 'board' }
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