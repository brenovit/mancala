import { setCurrentUser, getCurrentUser } from './storage.js'

export default (to, from, next) => {  
  if (to.matched.some(record => record.meta.loginRequired)) {    
    const user = getCurrentUser();
    if (user) {
      next();
    } else {
      setCurrentUser(null);
      alert('To access the game you must enter a username in the top right corner!')
      next('/')
    }
  } else {
    next()
  }
}
