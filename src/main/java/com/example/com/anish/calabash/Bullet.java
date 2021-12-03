package com.example.com.anish.calabash;

import com.example.asciiPanel.AsciiPanel;
import java.awt.Color;

public class Bullet extends Thing {

    Class<?> ownerClass;
    int attack;
    int speed=10;
    Boolean exist=true;
    String direction;

    public Bullet(World world,int _attack,String _direction,Creature _owner) {
        super(Color.gray, (char)8, world);
        attack=_attack;
        direction=_direction;
        ownerClass=_owner.getClass();
    }

    public void func(){
        if(!exist){
            return;
        }
        int _X=this.getX(),_Y=this.getY();
        int X=_X,Y=_Y;
        switch(direction){
            case "left":
                X-=1;
                break;
            case "right":
                X+=1;
                break;
            case "up":
                Y-=1;
                break;
            case "down":
                Y+=1;
                break;
            default:
            break;
        }
        if(X>=world.get_tile().length || X<0 || Y>=world.get_tile()[0].length || Y<0){
            exist=false;
            return;
        }
        if(world.get(X, Y) instanceof Monster){
            Creature target=(Creature)world.get(X, Y);
            target.get_hurt(attack);
            exist=false;
            return;
        }
        else if(world.get(X,Y) instanceof Wall){
            exist=false;
            return;
        }
        else if(world.get(X,Y) instanceof Floor){
            world.put(world.get(X,Y), _X, _Y);
            world.put(this, X, Y);
        }
    }

    public int get_attack(){
        return attack;
    }

    public void destroy(){
        exist=false;
        world.put(new Floor(world), getX(), getY());
    }

    public Class<?> getOwnerClass(){
        return ownerClass;
    }

    public boolean is_alive(){
        return exist;
    }

    public int get_speed(){
        return speed;
    }

}
