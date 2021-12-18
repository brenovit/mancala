const App = {    
    template: `
    <header>
        <nav-bar></nav-bar>
    </header>    
    <main role="main" class="flex-shrink-0">
        <div class="container">
            <router-view></router-view>
        </div>
    </main>
    <footer class="footer fixed-bottom mt-auto py-3">
        <div class="container">
            <footer-message></footer-message>
        </div>
    </footer>    
    `
}