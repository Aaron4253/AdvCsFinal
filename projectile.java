class projectile{
    Double x;
    int y;
    int damage;
    public projectile(Double x, int y, int damage){
        this.x = x;
        this.y = y;
        this.damage = damage;
    }

    public void incrementX(){
        x+=0.05;
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

    public void setDamage(int damage) {
        this.damage = damage;
    }    
}