import { getCurrentUser, setCurrentUser } from "./utils/storage.js"
import { createStore } from 'vuex'

const store = createStore({
    state: {
      currentUser: getCurrentUser()
    },
    getters:{
      currentUser: state => state.currentUser
    },
    mutations: {
      setUser(state, payload){
        state.currentUser = payload
      }
    },
    actions: {
      login({ commit }, payload){
        authApi.signin(payload.name)
        .then(
          response => {
            const user = {
              id: response.data.id,
              name: response.data.name
            }
            setCurrentUser(user)
            commit(setUser, user)
          },
          err => {
            console.log(err.message)
            setCurrentUser(null)
          }
        )
      }      
    }
})

export default store;