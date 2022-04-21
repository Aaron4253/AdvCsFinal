class zombie{
    Double x;
    int y;
    int health;
    int damage;
    int delay;
    int currentFrame;

    public zombie(Double x, int y, int health, int damage){
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.currentFrame = 0;
    }

    public Double getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getHealth(){
        return health;
    }

    public int getDamage(){
        return damage;
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public void incrementX(){
        x-=0.001;
    }

    public void deductHp(int hp){
        this.health -= hp;
    }

    public zombie copy(){
        return new zombie(x, y, health, damage);
    }
}