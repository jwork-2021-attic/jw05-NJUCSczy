package com.example.com.anish.calabash;

import java.awt.Color;

import com.example.maze.Node;

public class Creature extends Thing {
    private int health;
    protected int attack;

    Creature(Color color, char glyph, World world, int _health, int _attack) {
        super(color, glyph, world);
        health = _health;
        attack = _attack;
    }

    public Function func() {
        return new Function(new Node(-1, -1), null, 0);
    }

    public void get_hurt(int hurt) {
        health -= hurt;
    }

    public boolean is_alive() {
        return health > 0;
    }

    public void tryMove(int x, int y) {
        if ((world.get(x, y) instanceof Floor)) {
            int _x = this.getX();
            int _y = this.getY();
            world.put(world.get(x, y), _x, _y);
            world.put(this, x, y);
        }
        else if(world.get(x, y) instanceof Bullet){
            Bullet bullet=(Bullet)world.get(x, y);
            
            if(this.getClass()==bullet.getOwnerClass()){
                get_hurt(bullet.get_attack());
                bullet.destroy();
                int _x = this.getX();
                int _y = this.getY();
                world.put(world.get(x, y), _x, _y);
                world.put(this, x, y);
            }
        }
    }

    public class Function {
        public Node move;
        public Creature attackTarget;
        public int attack;

        Function(Node _move, Creature target, int _attack) {
            move = _move;
            attackTarget = target;
            attack = _attack;
        }
    }
}
