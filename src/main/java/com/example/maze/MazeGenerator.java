package com.example.maze;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import java.util.Arrays;
import java.lang.Math;
import java.lang.Number;

public class MazeGenerator {
    
    private Stack<Node> stack = new Stack<>();
    private Random rand = new Random();
    private int[][] maze;
    private int dimensionX;
    private int dimensionY;
    private Node target;

    public MazeGenerator(int dimx,int dimy) {
        maze = new int[dimx][dimy];
        dimensionX = dimx;
        dimensionY=dimy;
    }

    public void generateMaze() {
        for(int i=0;i<dimensionX;i++){
            for(int j=0;j<dimensionY;j++){
                maze[i][j]=1;
            }
        }
        maze[dimensionX/4][dimensionY/4]=maze[dimensionX/4][3*dimensionY/4]=maze[3*dimensionX/4][dimensionY/4]=maze[3*dimensionX/4][3*dimensionY/4]=0;
        if(dimensionX<=2||dimensionY<=2)
            return;
        int maxNum=(int)Math.sqrt((dimensionX+dimensionY)/2);
        for(int i=0;i<maxNum;i++){
            int x=0,y=0;
            while(x<1||x>dimensionX-2||y<1||y>dimensionY-2||maze[x][y]==0){
                Random r=new Random();
                x=r.nextInt(dimensionX-1)+1;
                y=r.nextInt(dimensionY-1)+1;
            }
            maze[x][y]=0;
        }
        int num=0;
        int x=1,y=1;
        while(num<dimensionX*dimensionY/3){
            if(maze[x][y]==0){
                num+=set_neighbor(x, y, (double)(dimensionX*dimensionY-num)/(double)(dimensionX*dimensionY));
            }
            Random r=new Random();
                x=r.nextInt(dimensionX-1)+1;
                y=r.nextInt(dimensionY-1)+1;
        }
    }

    public int[][] getMazes(){
        return maze;
    }

    private int set_neighbor(int x,int y,double value){
        int res=0;
        Random r=new Random();
        if(pointOnGrid(x, y-2)&&maze[x][y-1]!=0&&r.nextDouble()<=value){
            maze[x][y-1]=0;
            res++;
        }
        if(pointOnGrid(x, y+2)&&maze[x][y+1]!=0&&r.nextDouble()<=value){
            maze[x][y+1]=0;
            res++;
        }
        if(pointOnGrid(x-2, y)&&maze[x-1][y]!=0&&r.nextDouble()<=value){
            maze[x-1][y]=0;
            res++;
        }
        if(pointOnGrid(x+2, y)&&maze[x+1][y]!=0&&r.nextDouble()<=value){
            maze[x+1][y]=0;
            res++;
        }
        return res;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimensionX && y < dimensionY;
    }

    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }
    
    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }
}