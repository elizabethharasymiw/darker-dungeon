/**
 * @file DarkerDungeon.java
 * @brief Java Monofile Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;


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
        System.out.println(" You wake up to a unfamilar dark room.");
        System.out.println(" You see the dim outline of a door.");

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

    /**
     * @brief Menu constructor that requires a starting map
     * @param myMap Starting map
     */
    public Menu(Map myMap){
        this.unlockedOptions = new ArrayList<>(Collections.nCopies(Menu.MenuOptions.END.ordinal(), true));
        this.myMap = myMap;
        this.doNothingCount = 0;
    }

    /**
     * @brief Function to print out the current available player options
     */
    public void printMenu(){

        System.out.println();
        System.out.println(" " + "What would you like todo?");

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
                return false;
        }

        return true;
    }

    /**
     * @brief Function to print the current display based on the game state
     * @param lastPlayerAction The last action the player did
     */
    public void printScreen(MenuOptions lastPlayerAction){

        if(myMap.checkExit()){
            clearScreen();
            printASCIIDoor();
        }

        System.out.println();

        switch(lastPlayerAction){
            case FORWARD:
                System.out.println(" You decide to walk for a bit.");
                break;
            case TURNLEFT:
                System.out.println(" You decide to turn left.");
                break;
            case TURNRIGHT:
                System.out.println(" You decide to turn right.");
                break;
            case DONOTHING:
                switch(doNothingCount){
                case 1:
                    System.out.println(" So this is how you want to spend your time? Doing nothing?");
                    break;
                case 2:
                    System.out.println(" So you know there a door? You can get out of here.");
                    break;
                case 3:
                    System.out.println(" Alright, that's enough of that.");
                    break;
                }
                break;
            case OPENDOOR:
                clearScreen();
                System.out.println();
                printASCIISun();
                System.out.println("\n You have Escaped!\n");
                break;
            default:
                System.out.println(" You can't do that.");
                break;
        }

        if(myMap.checkExit() && lastPlayerAction == MenuOptions.FORWARD){
            System.out.println(" You are now standing next to the door.");
        }

        if(myMap.checkExit() && lastPlayerAction != MenuOptions.FORWARD && lastPlayerAction != MenuOptions.OPENDOOR){
            System.out.println(" Did you try opening the door?");
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
     * @brief Function to print an ASCII door to the screen
     */
    public void printASCIIDoor(){
        System.out.println("        ___");
        System.out.println("       |   |");
        System.out.println("       |  o|");
        System.out.println("       |___|");
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
     * @brief Function to clear the entire screen, screen being the console
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
    private int width;
    private int height;
    private Directions playerFront;
    private int currentLocationX;
    private int currentLocationY;
    private int exitLocationX;
    private int exitLocationY;
    private ArrayList<ArrayList<Character>> mapGrid;
    private char exitDoor = 'E';
    private char wall = '#';
    // @TODO: Add more environment descriptions text
    // private bool nextToWall;
    // private bool inCorner;

    /**
     * @brief Map constructor that requires the maps dimensions
     */
    public Map(){
        this.width = 5;
        this.height = 3;
        this.playerFront = Directions.SOUTH;
        this.currentLocationX = 1;
        this.currentLocationY = 1;

        // starting map, # = wall, E = exit door
        // @NOTE The map must have all edges be walls (#) to not have issues
        //       checking the player's future forward moves
        // ######
        // #  ###
        // #    D
        // ######
        this.mapGrid = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList('#', '#', '#', '#', '#', '#')),
            new ArrayList<>(Arrays.asList('#', ' ', ' ', '#', '#', '#')),
            new ArrayList<>(Arrays.asList('#', ' ', ' ', ' ', 'E', '#')),
            new ArrayList<>(Arrays.asList('#', '#', '#', '#', '#', '#'))
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
        if(this.currentLocationX == this.exitLocationX &&
           this.currentLocationY == this.exitLocationY){
            return true;
        }
        return false;
    }

    /**
     * @brief Function to check if the player can move forward
     * @return boolean based on if the player can move forward or not
     */
    public Boolean checkPlayerFORWARD(){
        switch (playerFront){
            case NORTH:
                if((currentLocationY > 0) && mapGrid.get(currentLocationY - 1).get(currentLocationX) != wall)
                    return true;
                break;
            case WEST:
                if((currentLocationX > 0) && mapGrid.get(currentLocationY).get(currentLocationX - 1) != wall)
                    return true;
                break;
            case SOUTH:
                if((currentLocationY < height) && mapGrid.get(currentLocationY + 1).get(currentLocationX) != wall)
                    return true;
                break;
            case EAST:
                if((currentLocationX < width) && mapGrid.get(currentLocationY).get(currentLocationX + 1) != wall)
                    return true;
                break;
        }

        return  false;
    }
}
