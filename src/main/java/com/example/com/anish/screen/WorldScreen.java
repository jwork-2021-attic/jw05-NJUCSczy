package com.example.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.awt.Color;
import com.example.com.anish.calabash.Calabash;
import com.example.com.anish.calabash.Creature;
import com.example.com.anish.calabash.Floor;
import com.example.com.anish.calabash.BFS;
import com.example.com.anish.calabash.Bullet;
import com.example.com.anish.calabash.Monster;
import com.example.com.anish.calabash.Wall;
import com.example.com.anish.calabash.World;
import com.example.com.anish.calabash.Creature.Function;
import com.example.asciiPanel.AsciiPanel;
import com.example.maze.Node;

public class WorldScreen implements Screen {

    private World world;
    public ExecutorService exec;
    private Lock lock = new ReentrantLock();
    String[] sortSteps;
    Calabash bro;
    String unhandledMoveInput = "";
    String unhandledAttackInput = "";

    public WorldScreen() {
        world = new World();
        exec = Executors.newCachedThreadPool();
        init_creatures(1, 7);
        bro = (Calabash) world.Calabashes.get(0);

    }

    public void init_creatures(int numOfCalabashes, int numOfMonsters) {
        ArrayList<Creature> creatures = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numOfCalabashes + numOfMonsters; i++) {
            Random r = new Random();
            int x = -1, y = -1;
            boolean ok = false;
            while (!ok) {
                ok = true;
                x = 1 + r.nextInt(world.get_tile().length - 2);
                y = 1 + r.nextInt(world.get_tile()[0].length - 2);
                if (!(world.get_tile()[x][y].getThing() instanceof Floor))
                    ok = false;
                for (int j = 0; j < nodes.size(); j++) {
                    if (nodes.get(j).x == x && nodes.get(j).y == y) {
                        ok = false;
                        break;
                    }
                }
                if (world.find_way(0, 0, x, y).size() == 0)
                    ok = false;
            }
            nodes.add(new Node(x, y));
            if (i < numOfCalabashes) {
                creatures.add(new Calabash(new Color(i * 128 / numOfCalabashes, 255, i * 128 / numOfCalabashes), i,
                        world, 500, 50));
                        nodes.set(0, new Node(0,0));
            } else {
                creatures.add(new Monster(
                        new Color(255, (i - numOfCalabashes) * 128 / numOfMonsters,
                                (i - numOfCalabashes) * 128 / numOfMonsters),
                        i - numOfCalabashes, world, 90 + r.nextInt(20), 25 + r.nextInt(10)));
            }

        }
        for (int i = 0; i < creatures.size(); i++) {
            world.add_creature(creatures.get(i), nodes.get(i));
        }
        for (int i = 0; i < creatures.size(); i++) {
            exec.execute(new CreatureGenerator(creatures.get(i)));
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        Integer keyCode = key.getKeyCode();
        switch (keyCode) {
            case 87:
                unhandledMoveInput = "up";
                break;
            case 83:
                unhandledMoveInput = "down";
                break;
            case 65:
                unhandledMoveInput = "left";
                break;
            case 68:
                unhandledMoveInput = "right";
                break;
            case 38:
                unhandledAttackInput = "up";
                break;
            case 40:
                unhandledAttackInput = "down";
                break;
            case 37:
                unhandledAttackInput = "left";
                break;
            case 39:
                unhandledAttackInput = "right";
                break;
        }

        return this;
    }

    public class CreatureGenerator implements Runnable {
        private Creature creature;

        CreatureGenerator(Creature _creature) {
            creature = _creature;
        }

        public void run() {
            if (creature instanceof Monster) {
                while (creature.is_alive()) {
                    try {
                        lock.lock();
                        Function func = creature.func();
                        if (func.attackTarget != null) {
                            func.attackTarget.get_hurt(func.attack);
                        } else if (func.move.x != -1 && func.move.y != -1) {
                            creature.tryMove(func.move.x, func.move.y);
                        }
                    } finally {
                        lock.unlock();
                        try {
                            Random r = new Random();
                            TimeUnit.MILLISECONDS.sleep(290 + r.nextInt(10));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    lock.lock();
                    world.get_tile()[creature.getX()][creature.getY()].setThing(new Floor(world));
                } finally {
                    lock.unlock();
                }
            } else if (creature instanceof Calabash) {
                Calabash player = (Calabash) creature;
                while (creature.is_alive()) {
                    try {
                        lock.lock();
                        switch (unhandledMoveInput) {
                            case "left":
                                player.tryMove(player.getX() - 1, player.getY());
                                break;
                            case "right":
                                player.tryMove(player.getX() + 1, player.getY());
                                break;
                            case "up":
                                player.tryMove(player.getX(), player.getY() - 1);
                                break;
                            case "down":
                                player.tryMove(player.getX(), player.getY() + 1);
                                break;
                        }
                        player.set_attack_direction(unhandledAttackInput);
                        
                        if (unhandledAttackInput != "") {
                            Function func = player.try_attack();
                            if (func.attackTarget != null) {
                                func.attackTarget.get_hurt(func.attack);
                            } else if (func.move.x != -1 && func.move.y != -1) {
                                Bullet bullet = new Bullet(world, func.attack, player.get_direction(), player);
                                world.get_tile()[func.move.x][func.move.y].setThing(bullet);
                                exec.execute(new BulletGenerator(bullet));
                            }
                        }
                        unhandledMoveInput = "";
                        unhandledAttackInput = "";
                    } finally {
                        lock.unlock();
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    lock.lock();
                    world.get_tile()[creature.getX()][creature.getY()].setThing(new Floor(world));
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public class BulletGenerator implements Runnable {
        private Bullet bullet;

        BulletGenerator(Bullet _bullet) {
            bullet = _bullet;
        }

        public void run() {
            while (bullet.is_alive()) {
                try {
                    lock.lock();
                    bullet.func();
                } finally {
                    lock.unlock();
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000 / bullet.get_speed());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            try {
                lock.lock();
                world.get_tile()[bullet.getX()][bullet.getY()].setThing(new Floor(world));
            } finally {
                lock.unlock();
            }
        }
    }

}
