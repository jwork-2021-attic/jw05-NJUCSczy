package com.example.com.anish.calabash;

import java.awt.Color;
import java.util.ArrayList;

import com.example.com.anish.screen.WorldScreen;
import com.example.maze.Node;

public class Monster extends Creature implements Comparable<Monster> {

    private int rank;

    public Monster(Color color, int rank, World world,int health,int attack) {
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
    public int compareTo(Monster o) {
        return Integer.valueOf(this.rank).compareTo(Integer.valueOf(o.rank));
    }

    @Override
    public Function func(){
        if(!is_alive()){
            return new Function(new Node(-1, -1), null, 0);
        }
        Tile[][] tiles=world.get_tile();
        if(getX()>0&&tiles[getX()-1][getY()].getThing() instanceof Calabash){
            return new Function(new Node(-1, -1), (Creature)tiles[getX()-1][getY()].getThing(), attack);
        }
        else if(getX()<tiles.length-1&&tiles[getX()+1][getY()].getThing() instanceof Calabash){
            return new Function(new Node(-1, -1), (Creature)tiles[getX()+1][getY()].getThing(), attack);
        }
        else if(getY()>0&&tiles[getX()][getY()-1].getThing() instanceof Calabash){
            return new Function(new Node(-1, -1), (Creature)tiles[getX()][getY()-1].getThing(), attack);
        }
        else if(getY()<tiles[0].length-1&&tiles[getX()][getY()+1].getThing() instanceof Calabash){
            return new Function(new Node(-1, -1), (Creature)tiles[getX()][getY()+1].getThing(), attack);
        }
        ArrayList<Creature> calabashes=world.get_calabashes();
        int length=-1;
        ArrayList<Node> targetWay=new ArrayList<>();
        for(int i=0;i<calabashes.size();i++){
            if(!calabashes.get(i).is_alive())
                continue;
            ArrayList<Node> way=world.find_way(getX(),getY(),calabashes.get(i).getX(), calabashes.get(i).getY());
            if(length==-1||way.size()<length){
                length=way.size();
                targetWay=way;
            }
        }
        if(length>0){
            return new Function(targetWay.get(targetWay.size()-1), null, 0);
        }
        return new Function(new Node(-1, -1), null, 0);
    }

}
