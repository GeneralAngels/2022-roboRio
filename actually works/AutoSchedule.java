package frc.robot;

public class AutoSchedule {
    private Action act;
    private AutoSchedule next;

    // i actually fucking hate java, who thought that 
    // not having default values to parameters would be a good idea
    // "yeah guys trust me everyone fucking loves overloading!"
    // - some stupid ass java dev
    public AutoSchedule(Action act, AutoSchedule next){
        this.act = act;
        this.next = next;
    }

    public AutoSchedule(String actionName, double target){
        this.act = new Action(actionName, target);
        next = null;
    }
    
    public AutoSchedule(Action act){
        this.act = act;
        next = null;
    }

    public AutoSchedule(){
        this.act = null;
        next = null;
    }
    
    public void setNext(AutoSchedule next){
        this.next = next;
    }
    
    public AutoSchedule getNext(){
        return this.next;
    }

    public Action getAction(){
        return this.act;
    }

    public void removeAction(){
        if (next == null) act = null;
        else {
            act = next.getAction();
            next = next.getNext();    
        }
    }

    public void addAction(Action act){
        if (this.act == null) this.act = act; // new Action(act.getActionName(), act.getTarget());
        else if (next == null) next = new AutoSchedule(act);
        else
            next.addAction(act);
    }

    public void addAction(AutoSchedule schedule){
        if (this.act == null) {
            this.act = schedule.getAction();
            this.next = schedule.getNext();
        }
        else if (next == null) this.next = schedule;
        else
            next.addAction(schedule);
    }

    public int length(){
        if (next == null) return 1;
        return 1 + next.length();
    }

    public String currentActionName(){
        return act.getActionName();
    }

    @Override
    public String toString(){
        if (act == null) return "empty schedule";
        if (next != null) return " -> " + act.toString() + next.toString();
        return " -> " + act.toString();
    }

    public boolean currentActionFinished(double value, double rangeOfError){
        return act.finished(value, rangeOfError);
    }

    public void reset(){
        act = null;
        next = null;
    }
}

