/**
 * @file Map.java
 * @author Elizabeth Harasymiw
 */

package com.mypackage;

/**
 * @brief Map Class that holds state information about player and environment
 * @apiNote The map is draw from the upper left to lower right, so upper left
 *          is coordinates (0, 0), and lower right is coordinates
 *          (width, height).
 */
public class Map{
    private enum Directions { NORTH, EAST, SOUTH, WEST }
    private enum PlayerLocationStates { ROOM, HALLWAYINTERSECTION, HALLWAY, DEADEND, CORNER }
    private int width;
    private int height;
    private Directions playerFront;
    private int currentLocationX;
    private int currentLocationY;
    private char[][] mapGrid;
    private char exitDoor = 'E';
    private char wall = '#';
    private PlayerLocationStates currentPlayerLocationState;
    private PlayerLocationStates priorPlayerLocationState;

    /**
     * @brief Map constructor that requires the maps dimensions
     */
    public Map(){
        this.width = 5;
        this.height = 3;
        this.playerFront = Directions.SOUTH;
        this.currentLocationX = 1;
        this.currentLocationY = 1;
        this.currentPlayerLocationState = PlayerLocationStates.CORNER;
        this.priorPlayerLocationState = PlayerLocationStates.CORNER;

        // starting map, # = wall, E = exit door
        // @NOTE The map must have all edges be walls (#) to not have issues
        //       checking the player's future forward moves
        this.mapGrid =  new char[][] {
            { '#', '#', '#', '#', '#', '#', '#' },
            { '#', ' ', ' ', '#', ' ', '#', '#' },
            { '#', ' ', ' ', ' ', ' ', 'E', '#' },
            { '#', '#', '#', '#', '#', '#', '#' }
        };
    }

    /**
     * @brief Function to update player location to be 1 square forward
     * @apiNote Warning this function does not do bounds checking for you
     */
    public void movePlayer(){
        switch(playerFront){
            case NORTH:
                currentLocationY--;
                break;
            case EAST:
                currentLocationX++;
                break;
            case SOUTH:
                 currentLocationY++;
                break;
            case WEST:
                currentLocationX--;
                break;
        }
    }

    /**
     * @brief Function to update the players direction for a 90 degree
     *        left turn
     */
    public void turnPlayerLeft(){
        switch(playerFront){
            case NORTH:
                playerFront = Directions.WEST;
                break;
            case EAST:
                playerFront = Directions.NORTH;
                break;
            case SOUTH:
                 playerFront = Directions.EAST;
                break;
            case WEST:
                playerFront = Directions.SOUTH;
                break;
        }
    }

    /**
     * @brief Function to update the players direction for a 90 degree
     *        right turn
     */
    public void turnPlayerRight(){
        switch(playerFront){
            case NORTH:
                playerFront = Directions.EAST;
                break;
            case EAST:
                playerFront = Directions.SOUTH;
                break;
            case SOUTH:
                 playerFront = Directions.WEST;
                break;
            case WEST:
                playerFront = Directions.NORTH;
                break;
        }
    }

    /**
     * @brief Function to check if the player is at the exit door
     * @return boolean based on if the player is or is not at the exit door
     */
    public Boolean checkExit(){
        if(mapGrid[currentLocationY][currentLocationX] == exitDoor){
            return true;
        }
        return false;
    }

    /**
     * @brief Function to check if the player can move forward
     * @apiNote If the map does not have walls surrounding all edges
     *          this could throw an out of bounds error when the player
     *          is at the edge of the map and facing the edge.
     * @return boolean based on if the player can move forward or not
     */
    public Boolean checkPlayerFORWARD(){
        switch (playerFront){
            case NORTH:
                if(mapGrid[currentLocationY - 1][currentLocationX] != wall)
                    return true;
                break;
            case WEST:
                if(mapGrid[currentLocationY][currentLocationX - 1] != wall)
                    return true;
                break;
            case SOUTH:
                if(mapGrid[currentLocationY + 1][currentLocationX] != wall)
                    return true;
                break;
            case EAST:
                if(mapGrid[currentLocationY][currentLocationX + 1] != wall)
                    return true;
                break;
        }

        return  false;
    }

    /**
     * @brief Function to update the current players location
     *        state, and most recent last location state, using
     *        information from the map around the player.
     */
    public void updatePlayerLocationStates(){
        priorPlayerLocationState = currentPlayerLocationState;

        int directWallCount = 0;
        Boolean northWall = false;
        Boolean westWall = false;
        Boolean southWall = false;
        Boolean eastWall = false;

        if(mapGrid[currentLocationY - 1][currentLocationX] == wall){
            northWall = true;
            directWallCount++;
        }

        if(mapGrid[currentLocationY][currentLocationX - 1] == wall){
            westWall = true;
            directWallCount++;
        }

        if(mapGrid[currentLocationY + 1][currentLocationX] == wall){
            southWall = true;
            directWallCount++;
        }

        if(mapGrid[currentLocationY][currentLocationX + 1] == wall){
            eastWall = true;
            directWallCount++;
        }

        Boolean adjacentWalls = false;
        Boolean oppositeWalls = false;

        if(directWallCount == 2){
            if( (westWall && eastWall) || (northWall && southWall)){
                oppositeWalls = true;
            }else{
                adjacentWalls = true;
            }
        }

        int cornerWallCount = 0;

        // Check North West Corner is Wall
        if(mapGrid[currentLocationY - 1][currentLocationX - 1] == wall){
            cornerWallCount++;
        }

        // Check North East Corner is Wall
        if(mapGrid[currentLocationY - 1][currentLocationX + 1] == wall){
            cornerWallCount++;
        }

        // Check South West Corner is Wall
        if(mapGrid[currentLocationY + 1][currentLocationX - 1] == wall){
            cornerWallCount++;
        }

        // Check South East Corner is Wall
        if(mapGrid[currentLocationY + 1][currentLocationX + 1] == wall){
            cornerWallCount++;
        }

        if(directWallCount == 3){
            currentPlayerLocationState = PlayerLocationStates.DEADEND;
        }
        else if(adjacentWalls){
            currentPlayerLocationState = PlayerLocationStates.CORNER;
        }
        else if(oppositeWalls){
            currentPlayerLocationState = PlayerLocationStates.HALLWAY;
        }
        else if( (cornerWallCount == 4) && (directWallCount <= 1) ){
            currentPlayerLocationState = PlayerLocationStates.HALLWAYINTERSECTION;
        }
        else{
            currentPlayerLocationState = PlayerLocationStates.ROOM;
        }
    }

    /**
     * @brief Function to return information about the players
     *        current location state in string form.
     * @return String that describes the players current location
     */
    public String getCurrentPlayerLocationState(){
        switch(currentPlayerLocationState){
            case ROOM:
                return "room";
            case HALLWAYINTERSECTION:
                return "hallway intersection";
            case HALLWAY:
                return "hallway";
            case DEADEND:
                return "dead end";
            case CORNER:
                return "corner";
        }

        return "ERROR_UNKNOWN_CURRENT_PLAYER_LOCATION_STATE";
    }

    /**
     * @brief Function to return information about the players
     *        prior location state in string form.
     * @return String that describes the players prior location
     */
    public String getPriorPlayerLocationState(){
        switch(priorPlayerLocationState){
            case ROOM:
                return "room";
            case HALLWAYINTERSECTION:
                return "hallway intersection";
            case HALLWAY:
                return "hallway";
            case DEADEND:
                return "dead end";
            case CORNER:
                return "corner";
        }

        return "ERROR_UNKNOWN_PRIOR_PLAYER_LOCATION_STATE";
    }

    /**
     * @brief Function to get the shortest path to exit, based
     *        on counting required forward moves needed.
     * @return int that is the shortest number of forward moves
     *         needed to reach the exit.
     */
    public int getShortestDistanceExit(){
        int[][] progress = {
            { 9, 9, 9, 9, 9, 9, 9 },
            { 9, 5, 4, 9, 2, 9, 9 },
            { 9, 4, 3, 2, 1, 0, 9 },
            { 9, 9, 9, 9, 9, 9, 9 }
        };

        return progress[currentLocationY][currentLocationX];

    }
}
