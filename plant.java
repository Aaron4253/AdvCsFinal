class plant{
    int x;
    int y;
    int health;
    int damage;
    int delay;
	int currentFrame;
	private final int originalHp;

    public plant(int x, int y, int health, int damage, int delay){
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.delay = delay;
		currentFrame = delay - 1;
		this.originalHp = health;
    }

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

    public void deductHp(int x){
        this.health -= x;
    }

	public Object getCurrentFrame() {
		return this.currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void incrementFrame(){
		this.currentFrame++;
	}

	public int getOriginalHp(){
		return originalHp;
	}

	public plant copy(){
		return new plant(this.x, this.y, this.health, this.damage, this.delay);
	}

	public projectile shoot(){
		if(currentFrame > delay){
			currentFrame = 0;
			return new projectile( (Double)(x + 0.0), y, damage);
		}else{
			return null;
		}
	}
}