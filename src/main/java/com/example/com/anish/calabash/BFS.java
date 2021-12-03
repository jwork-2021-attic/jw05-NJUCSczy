package com.example.com.anish.calabash;

import com.example.com.anish.calabash.*;

import com.example.maze.Node;
import com.example.com.anish.calabash.Target;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class BFS {
    int[][] bfsCondition;
    Node target;
    ArrayList<Node> plan = new ArrayList<Node>();
    ArrayList<Node> buffer = new ArrayList<>();

    public void load(Tile<Thing> tiles[][]) {
        bfsCondition = new int[tiles.length][];
        for (int i = 0; i < bfsCondition.length; i++) {
            bfsCondition[i] = new int[tiles[0].length];
            for (int j = 0; j < tiles[0].length; j++) {
                if (!(tiles[i][j].getThing() instanceof Floor) && !(tiles[i][j].getThing() instanceof Bullet))
                    bfsCondition[i][j] = -1;
                else
                    bfsCondition[i][j] = 0;
            }
        }
    }

    public ArrayList<Node> getPlan() {
        return plan;
    }

    public void calc(int x1, int y1, int x2, int y2) {
        plan.clear();
        target = new Node(x2, y2);
        bfsCondition[x2][y2] = 0;
        buffer.add(new Node(x1, y1));
        bfs_action();
    }

    public boolean bfs_action() {
        if (buffer.size() == 0)
            return false;
        Node startNode = buffer.get(0);
        boolean ok = false;
        Node[][] pre = new Node[bfsCondition.length][];
        for (int i = 0; i < bfsCondition.length; i++)
            pre[i] = new Node[bfsCondition[0].length];
        while (ok == false && buffer.size() > 0) {
            Node node = buffer.get(0);
            if (node.x == target.x && node.y == target.y) {
                ok = true;
            }
            int[] rand = { 1, 2, 3, 4 };
            Random r = new Random();
            for (int t = 0; t < 10; t++) {
                int x1 = r.nextInt(4);
                int x2 = r.nextInt(4);
                int temp = rand[x1];
                rand[x1] = rand[x2];
                rand[x2] = temp;
            }
            for (int j = 0; j < 4; j++) {
                switch (rand[j]) {
                    case 1:
                        if (isValid(node.x - 1, node.y)) {
                            bfsCondition[node.x - 1][node.y] = 1;
                            pre[node.x - 1][node.y] = node;
                            buffer.add(new Node(node.x - 1, node.y));
                        }
                        break;
                    case 2:
                        if (isValid(node.x + 1, node.y)) {
                            bfsCondition[node.x + 1][node.y] = 1;
                            pre[node.x + 1][node.y] = node;
                            buffer.add(new Node(node.x + 1, node.y));
                        }
                        break;
                    case 3:
                        if (isValid(node.x, node.y - 1)) {
                            bfsCondition[node.x][node.y - 1] = 1;
                            pre[node.x][node.y - 1] = node;
                            buffer.add(new Node(node.x, node.y - 1));
                        }
                        break;
                    case 4:
                        if (isValid(node.x, node.y + 1)) {
                            bfsCondition[node.x][node.y + 1] = 1;
                            pre[node.x][node.y + 1] = node;
                            buffer.add(new Node(node.x, node.y + 1));
                        }
                        break;
                }

            }
            bfsCondition[node.x][node.y] = 2;
            buffer.remove(0);

        }
        if (ok) {
            Node node = target;
            while (node.x != startNode.x || node.y != startNode.y) {
                plan.add(node);
                node = pre[node.x][node.y];
            }
            buffer.clear();
            return true;
        }
        buffer.clear();
        return false;

    }

    public boolean isValid(int x, int y) {
        if (x < 0 || x >= bfsCondition.length || y < 0 || y >= bfsCondition[0].length) {
            return false;
        }
        return bfsCondition[x][y] == 0 || bfsCondition[x][y] == -2;
    }

}