package com.example.pacman;

import java.util.ArrayList;

public class SegmentManager {
    ArrayList<Segment> list;
    int n;
    private class Cell {
        int i, j;
        Cell identifier;
        boolean right, top;
        public Cell() {}
        public Cell(int ini, int inj) {
            i=ini;
            j=inj;
            identifier=this;
            right=true;
            top=true;
        }
    }
    private void recomputeIdentifier(Cell cell) {
        if (cell.identifier==cell) return;
        recomputeIdentifier(cell.identifier);
        cell.identifier=cell.identifier.identifier;
    }

    private class Wall {
        Cell cell;
        int side; //0 means right, 1 means top.
        public Wall(Cell incell, int inside) {
            cell=incell;
            side=inside;
        }

    }

    Cell [][] table;
    private void generateCells() {
        table=new Cell[n][n];
        for (int i=0; i<n;i++) {
            for (int j=0; j<n;j++) {
                table[i][j]=new Cell(i,j);
            }
        }
        ArrayList<Wall> listwalls=new ArrayList<>();
        for (int i=0; i<n;i++) {
            for (int j = 0; j < n; j++) {
                if (i<n-1) listwalls.add(new Wall(table[i][j], 0));
                if (j<n-1) listwalls.add(new Wall(table[i][j], 1));
            }
        }
        for (int iwall=1; iwall<listwalls.size(); iwall++){
            int iotherwall=(int)(Math.random()*(iwall+1));
            Wall wall=listwalls.get(iwall);
            Wall otherwall=listwalls.get(iotherwall);
            listwalls.set(iwall, otherwall);
            listwalls.set(iotherwall, wall);
        }
        for (int iwall=0; iwall<listwalls.size(); iwall++){
            Wall wall= listwalls.get(iwall);
            Cell cell=wall.cell;
            int side=wall.side;
            int i=cell.i;
            int j=cell.j;
            Cell neightborcell;
            if (side==0) neightborcell=table[i+1][j];
            else neightborcell=table[i][j+1];
            recomputeIdentifier(cell);
            recomputeIdentifier(neightborcell);
            Cell idcell=cell.identifier;
            Cell idneightborcell=neightborcell.identifier;
            if (idcell==idneightborcell) continue;
            idcell.identifier=idneightborcell;
            if (side==0) cell.right=false;
            else cell.top=false;
        }
    }

    private void generateSegments() {
        list=new ArrayList<>();
        list.add(new Segment(0,0,n,0));
        list.add(new Segment(0,0,0,n));
        for (int i=0; i<n;i++)  {
            for (int j=0; j<n;j++) {
                Cell cell=table[i][j];
                if (cell.right) {
                    list.add(new Segment(i+1,j,i+1,j+1));
                }
                if (cell.top) {
                    list.add(new Segment(i,j+1,i+1,j+1));
                }
            }
        }
    }
    public SegmentManager() {
        list=new ArrayList<>();
        list.add(new Segment(new point(0.5,0), new point(0.5,0.5)));
        list.add(new Segment(new point(-0.5,0), new point(0,0.5)));
    }
    public SegmentManager(int inn) {
        n=inn;
        generateCells();
        generateSegments();
    }
}

