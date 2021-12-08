class Pot {
    constructor(_id) {
        this.id = 'h_' + _id;
        this.getNumber = () => { return parseInt(this.id.charAt(1)); };
        this.$ = () => { return $('#' + this.id); };
    }
}