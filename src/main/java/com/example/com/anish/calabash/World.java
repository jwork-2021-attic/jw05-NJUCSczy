package com.example.com.anish.calabash;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.Color;

import com.example.maze.*;
import com.example.com.anish.calabash.BFS;
import com.example.com.anish.calabash.Creature.Function;

public class World {

    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    
    private Tile<Thing>[][] tiles;
    public ArrayList<Creature> Calabashes=new ArrayList<>();
    public ArrayList<Creature> Monsters=new ArrayList<>();
    private BFS bfs = new BFS();


    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }
        MazeGenerator mazeGenerator = new MazeGenerator(HEIGHT, WIDTH);
        mazeGenerator.generateMaze();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(mazeGenerator.getMazes()[i][j] != 0 ? new Floor(this) : new Wall(this));
            }
        }
        
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

    public boolean isInArea(int x, int y) {
        return x >= 0 && x < tiles[0].length && y >= 0 && y < tiles.length;
    }

    public ArrayList<Node> find_way(int x1, int y1,int x2,int y2) {
        bfs.load(tiles);
        bfs.calc(x1, y1,x2,y2);
        return bfs.getPlan();
    }

    public void attack(Creature attacker, Creature target, int attack) {
        target.get_hurt(attack);
    }

    public ArrayList<Creature> get_calabashes() {
        return Calabashes;
    }

    public ArrayList<Creature> get_monsters() {
        return Monsters;
    }

    public Tile[][] get_tile() {
        return tiles;
    }

    public void add_creature(Creature _Creature,Node node) {
        if (_Creature instanceof Monster) {
            Monsters.add(_Creature);
            tiles[node.x][node.y].setThing(_Creature);
        } else if (_Creature instanceof Calabash) {
            Calabashes.add(_Creature);
            tiles[node.x][node.y].setThing(_Creature);
        }
        else{
            return;
        }
    }

    
}
