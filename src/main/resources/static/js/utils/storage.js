export const getCurrentUser = () => {
    let user = null;
    try {
      user = sessionStorage.getItem('user') != null ? JSON.parse(sessionStorage.getItem('user')) : null;
    } catch (error) {
      console.log(">>>> src/js/storage.js : getCurrentUser -> error", error)
      user = null;
    }
    return user;
  }
  
  export const setCurrentUser = (user) => {
    try {
      if (user) {
        sessionStorage.setItem('user', JSON.stringify(user))
      } else {
        sessionStorage.removeItem('user');
      }
    } catch (error) {
      console.log(">>>> src/js/storage.js : setCurrentUser -> error", error)
    }
  }