export const getCurrentUser = () => {
    let user = null;
    try {
      user = localStorage.getItem('user') != null ? JSON.parse(localStorage.getItem('user')) : null;
    } catch (error) {
      console.log(">>>> src/js/storage.js : getCurrentUser -> error", error)
      user = null;
    }
    return user;
  }
  
  export const setCurrentUser = (user) => {
    try {
      if (user) {
        localStorage.setItem('user', JSON.stringify(user))
      } else {
        localStorage.removeItem('user');
      }
    } catch (error) {
      console.log(">>>> src/js/storage.js : setCurrentUser -> error", error)
    }
  }