var hole_center = new Point(40,40);
var proximity_threshold = 20 * 20;
var radius = 40;

const BoardGame = {
  template: `
  <div clas="row">
    <div clas="col-12">
      <div class="board" id="board">
        <div class="section endsection">
          <div class="pot" id="h_6"></div>
        </div>
        <div class="section midsection">
          <div class="midrow topmid">
            <div class="pot" id="h_5" @click="sow"></div>
            <div class="pot" id="h_4" @click="sow"></div>
            <div class="pot" id="h_3" @click="sow"></div>
            <div class="pot" id="h_2" @click="sow"></div>
            <div class="pot" id="h_1" @click="sow"></div>
            <div class="pot" id="h_0" @click="sow"></div>
          </div>
          <div class="midrow botmid">
            <div class="pot" id="h_7" @click="sow"></div>
            <div class="pot" id="h_8" @click="sow"></div>
            <div class="pot" id="h_9" @click="sow"></div>
            <div class="pot" id="h_10" @click="sow"></div>
            <div class="pot" id="h_11" @click="sow"></div>
            <div class="pot" id="h_12" @click="sow"></div>
        </div>
        </div>
        <div class="section endsection">
          <div class="pot" id="h_13"></div>
        </div>
      </div>
    </div>
  </div>
  `,
  props: {
    game: {
      type: Object
    }
  },
  data(){
    return {
      colors: [
        new Color(119, 217, 112, 0.7)        
      ],
    }
  },
  computed: {
    gameInstance(){
      return this.game;
    },
    currentUser(){
      return this.$store.state.currentUser;
    },
  },
  mounted(){  
    this.build_board();
    this.distribute_seeds();
  },
  watch: {
    game: function (newGame, oldGmae) {
      this.distribute_seeds()
    }
  },
  methods:{
    sow(event){
      const ownerId = event.currentTarget.getAttribute('data-owner');
      if(ownerId !== this.currentUser.id){
        alert("This pit doens't belong to you!");
        return;
      }
      const totalSeeds = event.currentTarget.children.length
      if(totalSeeds <= 0){
        return;
      }
      const request = {
        gameId: this.gameInstance.id,
        playerId: this.currentUser.id,
        holePosition: event.currentTarget.getAttribute('data-pos')
      }
      gameApi.sow(request)
        .catch(e => {
            alert(e.response.data.code + " : "+e.response.data.message);
            console.error(e.response.data);
        });
    },
    build_board(){
      const holes = this.game.board.holes;
      holes.forEach(h => {
        const dest_pot = new Pot(h.index);
        dest_pot.$().attr('data-pos', h.position);
        dest_pot.$().attr('data-owner', h.owner.id);
        if(h.owner.id !== this.currentUser.id){
          dest_pot.$().addClass("enemy");
        }
      });
    },
    distribute_seeds(){
      const holes = this.game.board.holes;
      holes.forEach(h => {
        const dest_pot = new Pot(h.index);
        const currentSeeds = dest_pot.$().children().length;
        if(currentSeeds !== h.seeds){
          dest_pot.$().empty();
          for (var i = 0; i < h.seeds; i++) {
            this.place_new_seed(dest_pot, this.colors[0]);
          }
        }
      })
    },
    place_new_seed(dest_pot,color) {
      const bead = $("<div>",{"class":"bead"});
      this.setbg_rgba(bead,color);
      this.position_bead(bead,dest_pot);      
      dest_pot.$().append(bead);
    },
    //add gradient color effect
    setbg_rgba(e,c) {
      const hi = c.lerpTo(new Color(255,255,255,0),0.8);
      hi.a = 0.85;
      const lo = c.lerpTo(new Color(0,0,0,0),0.8);
      lo.a = 0.85;
      const grad =  "radial-gradient(farthest-corner at 9px 9px," +
        hi + " 0%, " + hi + " 8%, " + c + " 30%, " +
        lo + " 90%)";
      e.css("background-image",grad );
    },
    position_bead(bead,dest_pot) {
      let dsq = proximity_threshold;
      let done = false;
      while(!done) {
        dsq--;
        const seed_pos = hole_center.plus(this.generate_pot_offset(radius));
        if(this.pos_proximity_test(seed_pos,dest_pot,dsq)) {
          this.set_bead_pos(bead,seed_pos);
          done = true;
        }    
      }
    },
    generate_pot_offset( radius ) {
      const theta = Math.PI * (2 * Math.random() - 1);
      const r = radius * Math.random();
      return new Point( 
        Math.floor( r * Math.cos(theta) ),
        Math.floor( r * Math.sin(theta) )
      );
    },
    pos_proximity_test (test_pos,dest_pot,dist) { 
      var too_close = false;
      let _this = this;
      dest_pot.$().children().each(function(idx,bead)
      {
        const pos_bead = _this.read_pos(bead);
        if( pos_bead.minus(test_pos).normSq() < dist ) {
          too_close = true;
          return false;
        }
      });
      return !too_close;
    },
    read_pos (bead) {
      return new Point(
        parseInt($(bead).css("left").slice(0,-2)),
        parseInt($(bead).css("top").slice(0,-2))
      );
    },
    set_bead_pos(bead,pos) {
      $(bead).css( {
        "top":pos.y + "px","left":pos.x + "px"
      } );  
    }
  }
};