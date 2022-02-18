package frc.robot;

public class Action {
    private String actionName;
    private double target;

    public Action(String name, double target){
        this.actionName = name;
        this.target = target;
    }

    public boolean finished(double value, double rangeOfError){
        return value < this.target + rangeOfError &&
         value > this.target - rangeOfError;
    }

    public String getActionName(){
        return this.actionName;
    }

    public double getTarget(){
        return this.target;
    }

    @Override
    public String toString(){
        return actionName + " - " + target;
    }
}
