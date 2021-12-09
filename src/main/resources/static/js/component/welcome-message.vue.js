const WelcomeMessage = {
  template: `  
  <template v-if="currentUser">
    <div class="welcome py-1 fixed-top">
      <h4> Welcome user <span>{{ currentUser?.name }}</span>! Your id is: <span>{{ currentUser?.id }}</span></h4>
    </div>
  </template>
  `,
  computed: {
    currentUser(){
      return this.$store.state.currentUser;
    }
  }
};