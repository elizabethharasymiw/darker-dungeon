/**
 * @file DarkerDungeon.java
 * @brief Java Monofile Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;


/**
 * @brief Main class of the program, contains high level program structure
 */
public class main{
    public static void main(String[] args){
        int mapWidth = 3;
        int mapHeight = 3;
        Map myMap = new Map(mapWidth, mapHeight);
        Menu myMenu = new Menu(myMap);
        Scanner scanner = new Scanner(System.in);
        Boolean trapped = true;

        System.out.println("\nYou wake up to pitch blackness\n");

        while(trapped){
            myMenu.updateMenu();
            myMenu.printMenu();
            String userRawInput = scanner.nextLine();
            Menu.MenuOptions userOption = myMenu.parseUserInput(userRawInput);
            // TODO: clear screen
            trapped = myMenu.printAndDoResponse(userOption);
        }

        scanner.close();
    }
}

/**
 * @brief Menu class that holds game state information about the program
 */
class Menu{
    public enum MenuOptions { ZERO, DONOTHING, FORWARD, LEFT, RIGHT, BACKWARD, OPENDOOR, END}
    ArrayList<Boolean> unlockedOptions;
    private Map myMap;

    /**
     * @brief Menu constructor that requires a starting map
     * @param myMap Starting map
     */
    public Menu(Map myMap){
        this.unlockedOptions = new ArrayList<>(Collections.nCopies(Menu.MenuOptions.END.ordinal(), true));
        this.myMap = myMap;
    }

    /**
     * @brief Function to print out the current available player options
     */
    public void printMenu(){

        System.out.println("What would you like todo?");

        for(int i = 0; i < Menu.MenuOptions.END.ordinal(); i++){
            MenuOptions index = MenuOptions.values()[i];
            if(unlockedOptions.get(i)){
                switch(index){
                    case MenuOptions.DONOTHING:
                        System.out.println(MenuOptions.DONOTHING.ordinal() + ": Do Nothing");
                        break;
                    case MenuOptions.FORWARD:
                        System.out.println(MenuOptions.FORWARD.ordinal() + ": Walk Forward");
                        break;
                    case MenuOptions.LEFT:
                        System.out.println(MenuOptions.LEFT.ordinal() + ": Walk Left");
                        break;
                    case MenuOptions.RIGHT:
                        System.out.println(MenuOptions.RIGHT.ordinal() + ": Walk Right");
                        break;
                    case MenuOptions.BACKWARD:
                        System.out.println(MenuOptions.BACKWARD.ordinal() + ": Backtrack");
                        break;
                    case MenuOptions.OPENDOOR:
                        System.out.println(MenuOptions.OPENDOOR.ordinal() + ": Open Door");
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
        MenuOptions userOption = MenuOptions.ZERO;
        if(userNumber > MenuOptions.ZERO.ordinal() && userNumber < MenuOptions.END.ordinal()){
            userOption = MenuOptions.values()[userNumber];
        }

        return userOption;
    }

    /**
     * @TODO
     * @brief Function to process and report back what happened to a player action
     * @param userOption The action the player is trying to do
     * @return Boolean state of whether or not the player is still trapped
     */
    public Boolean printAndDoResponse(MenuOptions userOption){
        switch(userOption){
            case MenuOptions.FORWARD:
                System.out.println("\nYou decide to walk for a bit\n");
                break;
            case MenuOptions.LEFT:
                System.out.println("\nYou decide to turn left and walk for a bit\n");
                break;
            case MenuOptions.RIGHT:
                System.out.println("\nYou decide to turn right and walk for a bit\n");
                break;
            case MenuOptions.BACKWARD:
                System.out.println("\nYou decide to turn around and walk for a bit\n");
                break;
            case MenuOptions.DONOTHING:
                System.out.println("\nYou decide to do nothing for a while\n");
                break;
            case MenuOptions.OPENDOOR:
                System.out.println("\nAs you open the door you feel the warmth of the sun");
                System.out.println("\nCongratulations!");
                System.out.println("\nYou Escaped\n");
                return false; // break;
            default:
                System.out.println("\nYou can't do that\n");
                break;
        }

        return true;
    }

    /**
     * @brief Function to update what options the user has avaiable to them
     */
    public void updateMenu(){
        unlockedOptions.set(MenuOptions.OPENDOOR.ordinal(), myMap.checkExit());
        unlockedOptions.set(MenuOptions.FORWARD.ordinal(), myMap.checkPlayerFORWARD());
        unlockedOptions.set(MenuOptions.LEFT.ordinal(), myMap.checkPlayerLEFT());
        unlockedOptions.set(MenuOptions.BACKWARD.ordinal(), myMap.checkPlayerBACKWARD());
        unlockedOptions.set(MenuOptions.RIGHT.ordinal(), myMap.checkPlayerRIGHT());
    }
}

/**
 * @brief Map Class that holds state information about player and environment
 */
class Map{
    private enum Directions { NORTH, EAST, SOUTH, WEST }
    private enum PlayerMovements {FORWARD, LEFT, BACKWARD, RIGHT}
    private int width;
    private int height;
    private Directions playerFront;
    private int currentLocationX;
    private int currentLocationY;
    private int exitLocationX;
    private int exitLocationY;
    // @TODO: Add more environment descriptions text
    // private bool nextToWall;
    // private bool inCorner;

    /**
     * @brief Map constructor that requires the maps dimensions
     * @param width The width the map will be made with
     * @param height The height the map will be made with
     */
    public Map(int width, int height){
        this.width = width;
        this.height = height;
        this.playerFront = Directions.SOUTH;
        this.currentLocationX = 0;
        this.currentLocationY = 0;
        this.exitLocationX = width - 1;
        this.exitLocationY = height - 1;
    }

    /**
     * @brief Function to update player information based on a requested move action
     * @apiNote Warning this function does not do bounds checking for you
     */
    public void movePlayer(PlayerMovements myMove){
        switch(playerFront){
            case Directions.NORTH:
                switch(myMove){
                    case PlayerMovements.FORWARD:
                        currentLocationY--;
                        break;
                    case PlayerMovements.LEFT:
                        currentLocationX--;
                        playerFront = Directions.WEST;
                        break;
                    case PlayerMovements.BACKWARD:
                        currentLocationY++;
                        playerFront = Directions.SOUTH;
                        break;
                    case PlayerMovements.RIGHT:
                        currentLocationX++;
                        playerFront = Directions.EAST;
                        break;
                }
                break;
            case Directions.EAST:
                switch(myMove){
                    case PlayerMovements.FORWARD:
                        currentLocationX++;
                        break;
                    case PlayerMovements.LEFT:
                        currentLocationY--;
                        playerFront = Directions.NORTH;
                        break;
                    case PlayerMovements.BACKWARD:
                        currentLocationX--;
                        playerFront = Directions.WEST;
                        break;
                    case PlayerMovements.RIGHT:
                        currentLocationY++;
                        playerFront = Directions.SOUTH;
                        break;
                }
                break;
            case Directions.SOUTH:
                switch(myMove){
                    case PlayerMovements.FORWARD:
                        currentLocationY++;
                        break;
                    case PlayerMovements.LEFT:
                        currentLocationX++;
                        playerFront = Directions.EAST;
                        break;
                    case PlayerMovements.BACKWARD:
                        currentLocationY--;
                        playerFront = Directions.NORTH;
                        break;
                    case PlayerMovements.RIGHT:
                        currentLocationX--;
                        playerFront = Directions.WEST;
                        break;
                }
                break;
            case Directions.WEST:
                switch(myMove){
                    case PlayerMovements.FORWARD:
                        currentLocationX--;
                        break;
                    case PlayerMovements.LEFT:
                        currentLocationY++;
                        playerFront = Directions.SOUTH;
                        break;
                    case PlayerMovements.BACKWARD:
                        currentLocationX++;
                        playerFront = Directions.EAST;
                        break;
                    case PlayerMovements.RIGHT:
                        currentLocationY--;
                        playerFront = Directions.NORTH;
                        break;
                }
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
            case Directions.NORTH:
                if(currentLocationY > 0)
                    return true;
                break;
            case Directions.WEST:
                if(currentLocationX > 0)
                    return true;
                break;
            case Directions.SOUTH:
                if(currentLocationY < height)
                    return true;
                break;
            case Directions.EAST:
                if(currentLocationX < width)
                    return true;
                break;
        }

        return  false;
    }

    /**
     * @TODO
     * @brief Function to check if the player can move left
     * @return boolean based on if the player can move left or not
     */
    public Boolean checkPlayerLEFT(){
        return false;
    }

    /**
     * @TODO
     * @brief Function to check if the player can move backward
     * @return boolean based on if the player can move backward or not
     */
    public Boolean checkPlayerBACKWARD(){
        return false;
    }

    /**
     * @TODO
     * @brief Function to check if the player can move backward
     * @return boolean based on if the player can move backward or not
     */
    public Boolean checkPlayerRIGHT(){
        return false;
    }
}
