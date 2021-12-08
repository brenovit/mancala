var pot_center = new Point(30,30);
var proximity_threshold = 14 * 14;
var colors = [
  new Color(255,0,0,0.7), //red
  new Color(0,255,0,0.7), //green
  new Color(0,0,255,0.7), //blue
  new Color(255,255,0,0.7), //yellow
  //new Color(0,255,255,0.7), //light blue
  new Color(255,0,255,0.7), //pink
  new Color(255,255,255,0.7), //white
  new Color(0,0,0,0.7) //black
];
var isTopPlayer = true;
var pots = 6;
var size = 6;
function Point(x,y)
{
  this.x = x;
  this.y = y;
  this.plus = (p) =>
  {
    return new Point(
      this.x + p.x,
      this.y + p.y
    );
  }
  this.minus = (p) =>
  {    
    return new Point(
      this.x - p.x,
      this.y - p.y
    );
  }
  this.normSq = () =>
  {
    return x * x + y * y;
  }
}

function Color(r,g,b,a)
{
  this.r = r;
  this.g = g;
  this.b = b;
  this.a = a;
  this.toString = () =>
  {
    return "rgba("+Math.floor(this.r)+","+Math.floor(this.g)+","+Math.floor(this.b)+","+this.a+")"
  }
  this.lerpTo = (dest,alpha) =>
  {
    const acomp = 1 - alpha;
    return new Color(
      r * acomp + dest.r * alpha,
      g * acomp + dest.g * alpha,
      b * acomp + dest.b * alpha,
      a * acomp + dest.a * alpha
    );
  }
}

function Pot(id_in){
    //console.log('id_in:'+id_in)
    if (!(
      (id_in.charAt(0) === 'p' || id_in.charAt(0) === 'm') &&
      (id_in.charAt(1) === 't' || id_in.charAt(1) === 'b') &&
      (id_in.charAt(2) >= 1 || id_in.charAt(2) <= pots))) {
      throw "invalid id for pot construction";
    }
    this.id = id_in;
    this.isTop = () => { return this.id.charAt(1) === 't'; };
    this.isBottom = () => { return !isTop(); };
    this.isMan = () => { return this.id.charAt(0) === 'm'; };
    this.getSide = () => { return this.id.charAt(1); };
    this.getOtherSide = () => {
      if (this.getSide() === 't') {
        return 'b';
      }
      return 't';
    };
    this.getNumber = () => { return parseInt(this.id.charAt(2)); };
    this.getOpposite = () => {
      if (this.isMan()) {
        throw "cannot get opposite of mancala pot";
      }
      return new Pot("p" + this.getOtherSide() + (7 - this.getNumber()));
    };
    this.getNextSown = (isTopPlayer)  =>
    {
      if (this.isMan()) {
        if (this.isTop()) {
          return new Pot("pb1");
        }

        else {
          return new Pot("pt1");
        }
      }

      else {
        if (this.getNumber() === pots) {
          if (isTopPlayer) {
            if (this.isTop()) {
              return new Pot('mt');
            }

            else {
              return new Pot('pt1');
            }
          }

          else {
            if (this.isBottom()) {
              return new Pot('mb');
            }

            else {
              return new Pot('pb1');
            }
          }
        }

        else {
          return new Pot('p' + this.getSide() + (this.getNumber() + 1));
        }
      }
    }
    this.$ = () =>
    {
      return $('#' + this.id);
    }
  }

const BoardGameNew = {
  template: `
  <div clas="row"> 
  <div clas="col-12">  
<div class="board" id="board">
  <div class="section endsection">
    <div class="pot" id="pit_0"></div>
  </div>
  <div class="section midsection">
    <div class="midrow topmid">
      <div class="pot" id="pot_t_1" @click=addPotHandlers></div>
      <div class="pot" id="pot_t_2"></div>
      <div class="pot" id="pot_t_3"></div>
      <div class="pot" id="pot_t_4"></div>
      <div class="pot" id="pot_t_5"></div>
      <div class="pot" id="pot_t_6"></div>
    </div>
    <div class="midrow botmid">
      <div class="pot" id="pot_b_13"></div>
      <div class="pot" id="pot_b_12"></div>
      <div class="pot" id="pot_b_11"></div>
      <div class="pot" id="pot_b_10"></div>
      <div class="pot" id="pot_b_9"></div>
      <div class="pot" id="pot_b_8"></div>
    </div>
  </div>
  <div class="section endsection">
    <div class="pot" id="pit_7"></div>
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
              color: 'red',
              hole: 0
            },
            {
              color: 'blue',
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
              color: 'red',
              hole: 1
            },
            {
              color: 'blue',
              hole: 1
            },
            {
              color: 'green',
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
              color: 'red',
              hole: 1
            },
            {
              color: 'blue',
              hole: 1
            },
            {
              color: 'green',
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
              color: 'red',
              hole: 1
            },
            {
              color: 'blue',
              hole: 1
            },
            {
              color: 'green',
              hole: 1
            }
          ],
          owner: '123' 
        }
      ]
      
    }
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
  populate_board(){
    console.log(this.game);
    const red = this.colors[0];
    //pt1.., color
  },
  place_new_bead(id,c)
  {
    const bead = $("<div>",{"class":"bead"});
    this.setbg_rgba(bead,c);
    const dest_pot = new Pot(id);
    this.position_bead(bead,dest_pot);
    dest_pot.$().append(bead);
  },
  setbg_rgba(e,c)
  {
    const hi = c.lerpTo(new Color(255,255,255,0),0.8);
    hi.a = 0.85;
    const lo = c.lerpTo(new Color(0,0,0,0),0.8);
    lo.a = 0.85;
    const grad =  "radial-gradient(farthest-corner at 9px 9px," +
      hi + " 0%, " + hi + " 8%, " + c + " 30%, " +
      lo + " 90%)";
    e.css("background-image",grad );
  },
  read_pos (bead)
  {
    return new Point(
      parseInt($(bead).css("left").slice(0,-2)),
      parseInt($(bead).css("top").slice(0,-2))
    );
  },
  generate_pot_offset( radius )
  {
    const theta = Math.PI * (2 * Math.random() - 1);
    const r = radius * Math.random();
    return new Point( 
      Math.floor( r * Math.cos(theta) ),
      Math.floor( r * Math.sin(theta) )
    );
  },
  pos_proximity_test (test_pos,dest_pot,dist)
  { 
    var too_close = false;
    dest_pot.$().children().each(function(idx,bead)
    {
      const pos_bead = this.read_pos(bead);
      if( pos_bead.minus(test_pos).normSq() < dist )
      {
        too_close = true;
        return false;
      }
    });
    return !too_close;
  },
  set_bead_pos(bead,pos)
  {
    $(bead).css( {
      "top":pos.y + "px","left":pos.x + "px"
    } );  
  },
  position_bead(bead,dest_pot)
  {
    let dsq = proximity_threshold;
    let done = false;
    while( !done )
    {
      dsq--;
      const cand_pos = pot_center.plus( 
        this.generate_pot_offset( 25 )
      );
      if(this.pos_proximity_test(cand_pos,dest_pot,dsq))
      {
        this.set_bead_pos(bead,cand_pos);
        done = true;
      }    
    }
  },
  move_bead(bead,dest_pot)
  {
    this.position_bead(bead,dest_pot);  
    $(bead).appendTo(dest_pot.$());
  },  
  populate_row(row)
  {
    let n = 0;
    for(let c = 0; c < size; c++)
    {
      for(let i = 1; i <= pots; i++,n++)
      {
        this.place_new_bead(row + i,colors[c]);
      }
    }
  },
  string_out(src_pot,last_pot)
  { 
    const children = src_pot.$().children();
    //console.log('children: '+children.length );
    if(children.length === 0)
    {
      src_pot.$().css("background-color","rgba(255, 255, 255, 0.08)");
      this.addPotHandlers();
      return;
    }
    if(last_pot === undefined)
    {
      last_pot = src_pot;
    }
    const el = children.get(0);
    last_pot = last_pot.getNextSown(true);
    // steal
    if(children.length == 1 &&
      isTopPlayer === last_pot.isTop() &&
      last_pot.$().children().length === 0 &&
      !last_pot.isMan())
    {
      last_pot.getOpposite().$().children().each(function(idx,el_steal)
      {
        this.move_bead(el_steal,new Pot('mt'));
      });    
      this.move_bead(el,new Pot('mt'));
    }
    else
    {
      this.move_bead(el,last_pot);
    }
    setTimeout(string_out,0,src_pot,last_pot)
  },
  addPotHandlers()
  {
    console.log('addPotHandler')
      this.string_out(new Pot($(this).attr("id")));
      isTopPlayer = !isTopPlayer;
  }
}
};