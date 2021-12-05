const BoardGame = {
  template: `
<div class="board" id="board">
  <div class="section endsection">
    <div class="pot" id="mb"></div>
  </div>
  <div class="section midsection">
    <div class="midrow topmid">
      <div class="pot" id="pt1"></div>
      <div class="pot" id="pt2"></div>
      <div class="pot" id="pt3"></div>
      <div class="pot" id="pt4"></div>
      <div class="pot" id="pt5"></div>
      <div class="pot" id="pt6"></div>
    </div>
    <div class="midrow botmid">
      <div class="pot" id="pb6"></div>
      <div class="pot" id="pb5"></div>
      <div class="pot" id="pb4"></div>
      <div class="pot" id="pb3"></div>
      <div class="pot" id="pb2"></div>
      <div class="pot" id="pb1"></div>
    </div>
  </div>
  <div class="section endsection">
    <div class="pot" id="mt"></div>
  </div>
</div>
`
};