const App = {    
    template: `
    <div>
        <nav-bar></nav-bar>
        <router-view></router-view>
    </div>
    `,
    componets: {
        'nav-bar': NavBar
    }
}