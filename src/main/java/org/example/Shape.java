package org.example;

import java.awt.*;

import static org.example.Board.BLOCK_SIZE;
import static org.example.Board.BOARD_HEIGHT;

public class Shape {

    private int x = 4, y = 0;
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForShapeMovement = normal;
    private long beginTime;
    private int deltaX = 0;
    private boolean collision = false;
    private int[][] coords;
    private Board board;
    private Color color;

    public Shape(int [][] coords, Board board, Color color){
        this.coords = coords;
        this.board = board;
        this.color = color;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void reset(){
        this.x = 4;
        this.y = 0;
        collision = false;
    }
    public void update(){
        if (collision ) {
            //fill the color for board
            for(int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[0].length; col++){
                    if (coords[row][col] != 0){
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            //set current shape
            board.setCurrentShape();

            return;
        }

        boolean moveX = true;

        //check moving horizontal, se lo mettiamo sotto prende il ritardo del delay e segue la velocità del blocco
        if(!(x + deltaX + coords[0].length > 10)&& !(x + deltaX < 0)) {
            for (int row = 0; row < coords.length; row++){
                for (int col = 0; col < coords[row].length; col++){
                    if (coords[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX) {
                x += deltaX;
            }
        }
        deltaX = 0;

        if (System.currentTimeMillis() - beginTime > delayTimeForShapeMovement) {
            //movimento verticale
            if(!(y + 1 + coords.length > BOARD_HEIGHT)){
                for (int row = 0; row < coords.length; row++){
                    for(int col = 0; col < coords[row].length; col++){
                        if(coords[row][col] != 0){
                            if (board.getBoard()[y + 1 + row][x + deltaX + col] != null) {
                                collision = true;
                            }
                        }
                    }
                }
                if (!collision){
                    y++;
                }
            } else {
                collision = true;
            }

            beginTime = System.currentTimeMillis();
        }

    }

    private void checkLine(){
        int bottomLine = board.getBoard().length -1;
        for(int topLine = board.getBoard().length -1; topLine > 0; topLine--){
            int count = 0;
            for(int col = 0; col < board.getBoard()[0].length; col++){
                if (board.getBoard()[topLine][col] != null){
                    count++;
                }
                board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
            }
            if (count < board.getBoard()[0].length){
                bottomLine--;
            }
        }
    }

    //rotazione shape
    public void rotateShape(){
        int[][] rotateShape = transposeMatrix(coords);
        reverseRows(rotateShape);
        //check for right side and bottom
        if ((x + rotateShape[0].length > Board.BOARD_WIDTH) || (y + rotateShape.length > 20)){
            return;
        }
        //check for collision with other shaper before rotated
        for (int row = 0; row < rotateShape.length; row++){
            for (int col = 0; col < rotateShape[row].length; col++){
                if (rotateShape[row][col] != 0){
                    if (board.getBoard()[y + row][x + col] != null){
                        return;
                    }
                }
            }
        }
        coords = rotateShape;
    }

    private int[][] transposeMatrix(int [][] matrix){
        int[][] temp =  new int[matrix[0].length][matrix.length];
        for (int row = 0; row < matrix.length; row++){
            for(int col = 0; col < matrix[0].length; col++){
                temp[col][row] = matrix[row][col];
            }
        }
        return temp;
    }

    private void reverseRows(int[][] matrix){
        int middle = matrix.length  / 2;
        for (int row = 0; row < middle; row++){
            int[] temp = matrix[row];
            matrix[row] = matrix[matrix.length - row - 1];
            matrix[matrix.length - row - 1] = temp;
        }
    }

    //Draw the shape (creo la forma)
    public void render(Graphics g){
        for(int row = 0; row< coords.length; row++){
            for (int col = 0; col < coords[0].length;col++){
                if(coords[row][col] != 0){
                    g.setColor(color);
                    g.fillRect(col * BLOCK_SIZE + x * BLOCK_SIZE, row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE,  BLOCK_SIZE);

                }
            }
        }
    }
    public int[][] getCoords(){
        return coords;
    }
    public void speedUp(){
        delayTimeForShapeMovement = fast;
    }
    public void speedDown(){
        delayTimeForShapeMovement = normal;
    }
    public void moveRight(){
        deltaX = 1;
    }
    public void moveLeft(){
        deltaX = -1;
    }

    public int getY(){
        return y;
    }

    public int getX(){
        return x;
    }


}
