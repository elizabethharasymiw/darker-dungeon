/**
 * @file DarkerDungeon.java
 * @brief Java Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.LinkedList;
//import java.util.Queue;

/**
 * @brief Main class of the program, contains high level program structure
 */
public class main{
    public static void main(String[] args){
        Map myMap = new Map();
        Menu myMenu = new Menu(myMap);
        Scanner scanner = new Scanner(System.in);
        Boolean trapped = true;

        myMenu.clearScreen();
        System.out.println();
        myMenu.printASCIIkey();

        System.out.println();
        System.out.println(" You wake up to an unfamilar dark room...");
        System.out.println(" You find a dimly lit glowing key in your hand.");
        System.out.println(" You hear a mysterious voice say \"find the door\".");

        while(trapped){
            myMenu.updateMenu();
            myMenu.printMenu();
            String userRawInput = scanner.nextLine();
            Menu.MenuOptions playerAction = myMenu.parseUserInput(userRawInput);
            myMenu.clearScreen();
            trapped = myMenu.doPlayerAction(playerAction);
            myMenu.printScreen(playerAction);
        }

        scanner.close();
    }
}

/**
 * @brief Menu class that holds game state information about the program
 */
class Menu{
    public enum MenuOptions { ZERO, DONOTHING, FORWARD, TURNLEFT, TURNRIGHT, OPENDOOR, END}
    ArrayList<Boolean> unlockedOptions;
    private Map myMap;
    int doNothingCount;
    int progress_bar_length = 25;
    int startingPlayerExitDistance;
    int currentPlayerExitDistance;

    /**
     * @brief Menu constructor that requires a starting map
     * @param myMap Starting map
     */
    public Menu(Map myMap){
        this.unlockedOptions = new ArrayList<>(Collections.nCopies(Menu.MenuOptions.END.ordinal(), true));
        this.myMap = myMap;
        this.doNothingCount = 0;
        this.startingPlayerExitDistance = myMap.getShortestDistanceExit();
        this.currentPlayerExitDistance = myMap.getShortestDistanceExit();
    }

    /**
     * @brief Function to print out the current available player options
     */
    public void printMenu(){

        System.out.println();
        System.out.println(" " + "What would you like to do?");

        for(int i = 0; i < Menu.MenuOptions.END.ordinal(); i++){
            MenuOptions index = MenuOptions.values()[i];
            if(unlockedOptions.get(i)){
                switch(index){
                    case DONOTHING:
                        System.out.println(" " + MenuOptions.DONOTHING.ordinal() + ": Do Nothing");
                        break;
                    case FORWARD:
                        System.out.println(" " + MenuOptions.FORWARD.ordinal() + ": Walk Forward");
                        break;
                    case TURNLEFT:
                        System.out.println(" " + MenuOptions.TURNLEFT.ordinal() + ": Turn Left");
                        break;
                    case TURNRIGHT:
                        System.out.println(" " + MenuOptions.TURNRIGHT.ordinal() + ": Turn Right");
                        break;
                    case OPENDOOR:
                        System.out.println(" " + MenuOptions.OPENDOOR.ordinal() + ": Open Door");
                        break;
                    default:
                        break;
                }
            }
        }

        System.out.println();
    }

    /**
     * @brief Function to parse player input and determine what option they intended
     * @param userRawInput A raw input string from the player
     * @return A valid menu option, defaults to ZERO if the input was invalid
     */
    public MenuOptions parseUserInput(String userRawInput){

        // Parse out a number from user input
        int userNumber;
        try {
            userNumber = Integer.parseInt(userRawInput);
        } catch (NumberFormatException e) {
            userNumber = MenuOptions.ZERO.ordinal();
        }

        // Convert users number into a menu option
        MenuOptions playerAction = MenuOptions.ZERO;
        if(userNumber > MenuOptions.ZERO.ordinal() && userNumber < MenuOptions.END.ordinal()){
            playerAction = MenuOptions.values()[userNumber];
        }

        if(unlockedOptions.get(playerAction.ordinal()) == false){
            playerAction = MenuOptions.ZERO;
        }

        return playerAction;
    }

    /**
     * @brief Function to process a player action
     * @param playerAction The action the player is trying to do
     * @return Boolean state of whether or not the player is still trapped
     */
    public Boolean doPlayerAction(MenuOptions playerAction){

        Boolean trapped = true;

        switch(playerAction){
            case FORWARD:
                myMap.movePlayer();
                break;
            case TURNLEFT:
                myMap.turnPlayerLeft();
                break;
            case TURNRIGHT:
                myMap.turnPlayerRight();
                break;
            case DONOTHING:
                doNothingCount++;
                break;
            case OPENDOOR:
                trapped = false;
                break;
        }

        myMap.updatePlayerLocationStates();
        currentPlayerExitDistance = myMap.getShortestDistanceExit();

        return trapped;
    }

    /**
     * @brief Function to print the current display based on the game state
     * @param lastPlayerAction The last action the player did
     */
    public void printScreen(MenuOptions lastPlayerAction){

        System.out.println();
        printASCIIkey();

        if(myMap.checkExit()){
            printASCIIDoor();
        }
        else if(myMap.checkPlayerFORWARD() == false){
            printASCIIWall();
        }

        System.out.println();

        switch(lastPlayerAction){
            case FORWARD:
                if(myMap.getCurrentPlayerLocationState() == myMap.getPriorPlayerLocationState()){
                    System.out.print(" You continue walking in a ");
                    if(myMap.getCurrentPlayerLocationState() == "corner"){
                        System.out.print("room");
                    }
                    else{
                        System.out.print(myMap.getCurrentPlayerLocationState());
                    }
                }
                else if(myMap.getPriorPlayerLocationState() == "corner"){
                    System.out.print(" You continue walking in a room");
                }
                else{
                    System.out.print(" You enter a ");
                    System.out.print(myMap.getCurrentPlayerLocationState());
                }
                break;
            case TURNLEFT:
                System.out.print(" You turn left in the ");
                System.out.print(myMap.getCurrentPlayerLocationState());
                break;
            case TURNRIGHT:
                System.out.print(" You turn right in the ");
                System.out.print(myMap.getCurrentPlayerLocationState());
                break;
            case DONOTHING:
                switch(doNothingCount){
                case 1:
                    System.out.print(" So this is how you want to spend your time?");
                    break;
                case 2:
                    System.out.print(" You know there is a door?");
                    break;
                case 3:
                    System.out.print(" Alright, that's enough of that.");
                    break;
                }
                break;
            case OPENDOOR:
                clearScreen();
                System.out.println();
                printASCIISun();
                System.out.print("\n You have Escaped!\n");
                break;
            default:
                System.out.print(" You can't do that.");
                break;
        }

        // check if the player just got here
        if(myMap.checkExit() && lastPlayerAction == MenuOptions.FORWARD){
            System.out.println(" and find a door");
        }
        else{
            System.out.println();
        }

        // check if the player did not move locations
        if(myMap.checkExit() && lastPlayerAction != MenuOptions.FORWARD && lastPlayerAction != MenuOptions.OPENDOOR){
            System.out.println(" A door is nearby");
        }

    }

    /**
     * @brief Function to update what options the user has avaiable to them
     */
    public void updateMenu(){
        unlockedOptions.set(MenuOptions.OPENDOOR.ordinal(), myMap.checkExit());
        unlockedOptions.set(MenuOptions.FORWARD.ordinal(), myMap.checkPlayerFORWARD());
        unlockedOptions.set(MenuOptions.DONOTHING.ordinal(), doNothingCheck());
    }

    /**
     * @brief Function to print an ASCII key and progress bar to the screen
     */
    public void printASCIIkey(){
        int barLength = progress_bar_length / startingPlayerExitDistance;
        int remaining = 0 + (barLength * currentPlayerExitDistance);
        int progress = progress_bar_length - (barLength * currentPlayerExitDistance);

        System.out.print( " ◯─┬┐ " );

        for(int i = 0; i < progress; i++){
            System.out.print("█");
        }

        for(int i = 0; i < remaining; i++){
            System.out.print("_");
        }

        System.out.println();
    };

    /**
     * @brief Function to print an ASCII door to the screen
     */
    public void printASCIIDoor(){
        System.out.println("              ___");
        System.out.println("             |   |");
        System.out.println("             |  o|");
        System.out.println("             |___|");
    }

    /**
     * @brief Function to print an ASCII sun to the screen
     */
    public void printASCIISun(){
        System.out.println(   "      \\ _ /");
        System.out.println(   "    _ /   \\ _");
        System.out.println(   "      \\ _ /");
        System.out.println(   "      /   \\");
    }

    /**
     * @brief Function to print an ASCII wall to the screen
     */
    public void printASCIIWall(){
         System.out.println("  _____________________________ ");
         System.out.println(" |___|___|___|___|___|___|___|_|");
         System.out.println(" |_|___|___|___|___|___|___|___|");
         System.out.println(" |___|___|___|___|___|___|___|_|");
         System.out.println(" |_|___|___|___|___|___|___|___|");
    }

    /**
     * @brief Function to clear the entire screen, screen being the console
     *
     *        \033 is the start of an ansi code
     *        [H Move cursor to the beginning
     *        [2J Clear everything from this point
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * @brief Function to check when to disable the "Do Nothing" player action
     * @return boolean for whether or not the "Do Nothing" option should be displayed
     */
    public Boolean doNothingCheck(){
        if(doNothingCount < 3){
            return true;
        }
        return false;
    }
}

/**
 * @brief Map Class that holds state information about player and environment
 * @apiNote The map is draw from the upper left to lower right, so upper left
 *          is coordinates (0, 0), and lower right is coordinates
 *          (width, height).
 */
class Map{
    private enum Directions { NORTH, EAST, SOUTH, WEST }
    private enum PlayerLocationStates { ROOM, HALLWAYINTERSECTION, HALLWAY, DEADEND, CORNER }
    private int width;
    private int height;
    private Directions playerFront;
    private int currentLocationX;
    private int currentLocationY;
    private ArrayList<ArrayList<Character>> mapGrid;
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
        this.mapGrid = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList('#', '#', '#', '#', '#', '#', '#')),
            new ArrayList<>(Arrays.asList('#', ' ', ' ', '#', ' ', '#', '#')),
            new ArrayList<>(Arrays.asList('#', ' ', ' ', ' ', ' ', 'E', '#')),
            new ArrayList<>(Arrays.asList('#', '#', '#', '#', '#', '#', '#'))
        ));
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
        if(mapGrid.get(currentLocationY).get(currentLocationX) == exitDoor){
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
                if(mapGrid.get(currentLocationY - 1).get(currentLocationX) != wall)
                    return true;
                break;
            case WEST:
                if(mapGrid.get(currentLocationY).get(currentLocationX - 1) != wall)
                    return true;
                break;
            case SOUTH:
                if(mapGrid.get(currentLocationY + 1).get(currentLocationX) != wall)
                    return true;
                break;
            case EAST:
                if(mapGrid.get(currentLocationY).get(currentLocationX + 1) != wall)
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

        if(mapGrid.get(currentLocationY - 1).get(currentLocationX) == wall){
            northWall = true;
            directWallCount++;
        }

        if(mapGrid.get(currentLocationY).get(currentLocationX - 1) == wall){
            westWall = true;
            directWallCount++;
        }

        if(mapGrid.get(currentLocationY + 1).get(currentLocationX) == wall){
            southWall = true;
            directWallCount++;
        }

        if(mapGrid.get(currentLocationY).get(currentLocationX + 1) == wall){
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
        if(mapGrid.get(currentLocationY - 1).get(currentLocationX - 1) == wall){
            cornerWallCount++;
        }

        // Check North East Corner is Wall
        if(mapGrid.get(currentLocationY - 1).get(currentLocationX + 1) == wall){
            cornerWallCount++;
        }

        // Check South West Corner is Wall
        if(mapGrid.get(currentLocationY + 1).get(currentLocationX - 1) == wall){
            cornerWallCount++;
        }

        // Check South East Corner is Wall
        if(mapGrid.get(currentLocationY + 1).get(currentLocationX + 1) == wall){
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
        ArrayList<ArrayList<Integer>> progress = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(9, 9, 9, 9, 9, 9, 9)),
            new ArrayList<>(Arrays.asList(9, 5, 4, 9, 2, 9, 9)),
            new ArrayList<>(Arrays.asList(9, 4, 3, 2, 1, 0, 9)),
            new ArrayList<>(Arrays.asList(9, 9, 9, 9, 9, 9, 9))
        ));

        return progress.get(currentLocationY).get(currentLocationX);
        
                /*
        int moveCount = 0;
        int rows = height + 1;
        int columns = width + 2;
        ArrayList<ArrayList<Boolean>> visited = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Boolean> row = new ArrayList<>(Collections.nCopies(columns, false));
            visited.add(row);
        }
        Queue<ArrayList<Integer>> queue = new LinkedList<>();

        visited.get(currentLocationY).set(currentLocationX, true);
        queue.add(new ArrayList<>(Arrays.asList(currentLocationX, currentLocationY, 0)));

        for(int i = 1; queue.isEmpty() == false; i++){

            ArrayList<Integer> node = queue.remove();

            if(mapGrid.get(node.get(1)).get(node.get(0)) == exitDoor){
                return node.get(2);
            }

            // Check if North of current node is visited
            if((visited.get(node.get(1) - 1).get(node.get(0)) == false)
               && (mapGrid.get(node.get(1) - 1).get(node.get(0)) != wall)){
                queue.add(new ArrayList<>(Arrays.asList(node.get(0), node.get(1) - 1, i)));
                visited.get(node.get(1) - 1).set(node.get(0), true);
            }

            // Check if West of current node is visited
            if((visited.get(node.get(1)).get(node.get(0) - 1) == false)
               && (mapGrid.get(node.get(1)).get(node.get(0) - 1) != wall)){
                queue.add(new ArrayList<>(Arrays.asList(node.get(0) - 1, node.get(1), i)));
                visited.get(node.get(1)).set(node.get(0) - 1, true);
            }

            // Check if East of current node is visited
            if((visited.get(node.get(1)).get(node.get(0) + 1) == false)
               && (mapGrid.get(node.get(1)).get(node.get(0) + 1) != wall)){
                queue.add(new ArrayList<>(Arrays.asList(node.get(0) + 1, node.get(1), i)));
                visited.get(node.get(1)).set(node.get(0) + 1, true);
            }

            // Check if South of current node is visited
            if((visited.get(node.get(1) + 1).get(node.get(0)) == false)
               && (mapGrid.get(node.get(1) + 1).get(node.get(0)) != wall)){
                queue.add(new ArrayList<>(Arrays.asList(node.get(0), node.get(1) + 1, i)));
                visited.get(node.get(1) + 1).set(node.get(0), true);
            }

            //moveCount++;
        }

        //return moveCount;
        return 99; // error not able to find exit
        */
    }
}
