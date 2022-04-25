class projectile{
    Double x;
    int y;
    int damage;
    String color;
    public int count;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // count amount of times that move
    public projectile(Double x, int y, int damage){
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.color = "green";
        this.count = 0;
    }

    public void incrementX(){
        x+=0.01;
        count++;
    }
    public void setX(Double x){
        this.x = x;
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