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

    
    
    public void SetNext(AutoSchedule next){
        this.next = next;
    }
    
    public AutoSchedule getNext(){
        return this.next;
    }

    public Action getAction(){
        return this.act;
    }

    public void removeAction(){
        act = next.getAction();
        next = next.getNext();
    }

    public void addAction(Action act){
        if (act == null) this.act = act;
        else if (next == null) next = new AutoSchedule(act);
        else
            next.addAction(act);
    }

    public int Length(){
        if (next == null) return 1;
        return 1 + next.Length();
    }

    public String currentActionName(){
        return act.getActionName();
    }
}

