class projectile{
    Double x;
    int y;
    int damage;
    String color;

    public projectile(Double x, int y, int damage){
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.color = "green";
    }

    public void incrementX(){
        x+=0.005;
    }

    public Double getX(){
        return x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDamage() {
        return this.damage;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }    
}