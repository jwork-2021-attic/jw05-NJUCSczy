package com.example.com.anish.calabash;

import java.awt.Color;
import java.util.ArrayList;

import com.example.maze.Node;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.example.com.anish.screen.WorldScreen;

public class Calabash extends Creature implements Comparable<Calabash> {

    private int rank;
    private String direction;
    private int moveColdTime=100;
    private int attackColdTime=600;
    private long lastMoveTime=-1;
    private long lastAttackTime=-1;

    public Calabash(Color color, int rank, World world,int health,int attack) {
        super(color, (char) 2, world,health,attack);
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return String.valueOf(this.rank);
    }

    @Override
    public int compareTo(Calabash o) {
        return Integer.valueOf(this.rank).compareTo(Integer.valueOf(o.rank));
    }

    public void set_attack_direction(String dir){
        direction=dir;
    }

    public String get_direction(){
        return direction;
    }

    public Function try_attack(){
        if(!is_alive()||(lastAttackTime!=-1&&System.currentTimeMillis()-lastAttackTime<attackColdTime)||direction==""){
            return new Function(new Node(-1, -1), null, 0);
        }
        lastAttackTime=System.currentTimeMillis();
        Tile[][] tiles=world.get_tile();
        if(getX()>0 && direction=="left"){
            if(tiles[getX()-1][getY()].getThing() instanceof Monster)
                return new Function(new Node(-1, -1), (Creature)tiles[getX()-1][getY()].getThing(), attack);
            else
                return new Function(new Node(getX()-1, getY()), null, attack);
        }
        else if(getX()<tiles.length-1 && direction=="right"){
            if(tiles[getX()+1][getY()].getThing() instanceof Monster)
                return new Function(new Node(-1, -1), (Creature)tiles[getX()+1][getY()].getThing(), attack);
            else
                return new Function(new Node(getX()+1, getY()), null, attack);
        }
        else if(getY()>0 && direction=="up"){
            if(tiles[getX()][getY()-1].getThing() instanceof Monster)
                return new Function(new Node(-1, -1), (Creature)tiles[getX()][getY()-1].getThing(), attack);
            else
                return new Function(new Node(getX(), getY()-1), null, attack);
        }
        else if(getY()<tiles[0].length-1 && direction=="down"){
            if(tiles[getX()][getY()+1].getThing() instanceof Monster)
                return new Function(new Node(-1, -1), (Creature)tiles[getX()][getY()+1].getThing(), attack);
            else
                return new Function(new Node(getX(), getY()+1), null, attack);
        }
        
        return new Function(new Node(-1, -1), null, 0);
    }

    @Override
    public void tryMove(int x, int y) {
        if(!is_alive()||( lastMoveTime!=-1 && System.currentTimeMillis()-lastMoveTime<moveColdTime))
            return;
        if(x<0||x>=world.get_tile().length||y<0||y>=world.get_tile()[0].length){
            return;
        }
        lastMoveTime=System.currentTimeMillis();
        if ((world.get(x, y) instanceof Floor)) {
            int _x = this.getX();
            int _y = this.getY();
            world.put(world.get(x, y), _x, _y);
            world.put(this, x, y);
        }
    }


}
