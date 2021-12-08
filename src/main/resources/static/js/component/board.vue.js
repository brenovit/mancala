var hole_center = new Point(40,40);
var proximity_threshold = 20 * 20;
var radius = 40;

const BoardGameNew = {
  template: `
  <div clas="row"> 
    <div clas="col-12">  
      <div class="board" id="board">
        <div class="section endsection">
          <div class="pot" id="h_0"></div>
        </div>
        <div class="section midsection">
          <div class="midrow topmid">
            <div class="pot" id="h_1" @click="sow"></div>
            <div class="pot" id="h_2" @click="sow"></div>
            <div class="pot" id="h_3" @click="sow"></div>
            <div class="pot" id="h_4" @click="sow"></div>
            <div class="pot" id="h_5" @click="sow"></div>
            <div class="pot" id="h_6" @click="sow"></div>
          </div>
          <div class="midrow botmid">
            <div class="pot" id="h_13" @click="sow"></div>
            <div class="pot" id="h_12" @click="sow"></div>
            <div class="pot" id="h_11" @click="sow"></div>
            <div class="pot" id="h_10" @click="sow"></div>
            <div class="pot" id="h_9" @click="sow"></div>
            <div class="pot" id="h_8" @click="sow"></div>
        </div>
        </div>
        <div class="section endsection">
          <div class="pot" id="h_7"></div>
        </div>
      </div>
    </div>
  </div>
  <div clas="row"> 
    <div clas="col-12">
      <button @click="abandonGame">Abandon!</button>
    </div>
  </div>

`,
data(){
  return {
    seeds: 6,
    pots: 6,
    colors: [
      new Color(255,0,0,0.7), //red
      new Color(0,255,0,0.7), //green
      new Color(0,0,255,0.7), //blue
      new Color(255,255,0,0.7), //yellow
      //new Color(0,255,255,0.7), //light blue
      new Color(255,0,255,0.7), //pink
      new Color(255,255,255,0.7), //white
      new Color(0,0,0,0.7) //black
    ],
    game: {
      id: '123',
      name: 'game 1',
      player1: {
        id: '123',
        name: 'breno'
      },
      player2:{
        id: '456',
        name: 'breno 2'
      },
      status: 'playing',
      board: [
        {
          id: '0',
          type: 'pit',
          seeds: [
            {
              color: 1,
              hole: 0
            },
            {
              color: 2,
              hole: 0
            },
          ],
          owner: '123' 
        },
        {
          id: '1',
          type: 'pot', //pit
          seeds: [
            {
              color: 1,
              hole: 1
            },
            {
              color: 2,
              hole: 1
            },
            {
              color: 0,
              hole: 1
            }
          ],
          owner: '123' 
        },
        {
          id: '2',
          type: 'pot', //pit
          seeds: [
            {
              color: 1,
              hole: 1
            },
            {
              color: 2,
              hole: 1
            },
            {
              color: 0,
              hole: 1
            }
          ],
          owner: '123' 
        },
        {
          id: '3',
          type: 'pot', //pit
          seeds: [
            {
              color: 1,
              hole: 1
            },
            {
              color: 2,
              hole: 1
            },
            {
              color: 0,
              hole: 1
            }
          ],
          owner: '123' 
        },
        {
          id: '7',
          type: 'pit',
          seeds: [
            {
              color: 1,
              hole: 1
            },
            {
              color: 2,
              hole: 1
            },
            {
              color: 0,
              hole: 1
            }
          ],
          owner: '123' 
        }
      ]
      
    }
  }
},
computed: {
  getTotalSeeds(event){
    return event.currentTarget.children.length;
  }
},
mounted(){  
  this.populate_board();
},
methods:{
  abandonGame(){
    if(confirm('If you leave you will lose the game. Are you sure?')){
      this.$router.push('/');
    }
  },
  sow(event){
    console.log('from pot: '+event.currentTarget.id);
    console.log('seeds: '+event.currentTarget.children.length);
  },
  populate_board(){
    const holes = this.game.board;
    holes.forEach(h => {
      h.seeds.forEach(s => {
        this.place_new_seed(h.id, this.colors[s.color])
      })
    })
  },
  place_new_seed(id,c) {
    const bead = $("<div>",{"class":"bead"});
    this.setbg_rgba(bead,c);
    const dest_pot = new Pot(id);
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