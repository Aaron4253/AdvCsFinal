class dyingZombie{
    private double x;
    private int y;
    private int runTime;
    private int currentFrame;
    public dyingZombie(double x, int y, int runTime){
        this.x = x;
        this.y = y;
        this.runTime = runTime;
        currentFrame = 0;
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

    public int getCurrentFrame(){
        return currentFrame;
    }

    public int getRunTime(){
        return runTime;
    }
    
    public void incrementFrame(){
        currentFrame++;
    }

    public dyingZombie copy(){
        return new dyingZombie(x, y, runTime);
    }
}