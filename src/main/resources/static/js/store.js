import { getCurrentUser, setCurrentUser } from "./utils/storage.js"
import authApi from './api/auth-api.js'

const store = Vuex.createStore({
    state: {
      currentUser: getCurrentUser()
    },
    getters:{
      currentUser: state => state.currentUser
    },
    mutations: {
      setUser(state, payload){
        state.currentUser = payload
      },
      setLogout(state) {
        state.currentUser = null
      }
    },
    actions: {
      login({ commit }, payload){
        authApi.signin(payload)
        .then(
          response => {
            const user = {
              id: response.data.id,
              name: response.data.name
            }
            setCurrentUser(user)
            commit('setUser', user)
          },
          err => {
            console.log(err.message)
            setCurrentUser(null)
          }
        )
      },
      logout({ commit }){
        setCurrentUser(null)
        commit('setUser')
      }
    }
})

export default store;