function Color(r,g,b,a) {
  this.r = r;
  this.g = g;
  this.b = b;
  this.a = a;
  this.toString = () => {
    return "rgba("+Math.floor(this.r)+","+Math.floor(this.g)+","+Math.floor(this.b)+","+this.a+")"
  }
  this.lerpTo = (dest,alpha) => {
    const acomp = 1 - alpha;
    return new Color(
      r * acomp + dest.r * alpha,
      g * acomp + dest.g * alpha,
      b * acomp + dest.b * alpha,
      a * acomp + dest.a * alpha
    );
  }
}