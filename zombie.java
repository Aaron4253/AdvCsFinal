class zombie{
    int x;
    int y;
    int health;
    int damage;
    int delay;
    int currentFrame;

    public zombie(int x, int y, int health, int damage){
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.currentFrame = 0;
    }

    public zombie copy(){
        return new zombie(x, y, health, damage);
    }
}