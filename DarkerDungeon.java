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
     * @brief Disabled Menu default constructor
     */
    private Menu(){}

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
     * @TODO
     * @brief Function to update what options the user has avaiable to them
     */
    public void updateMenu(){

    }
}

/**
 * @brief Map Class that holds state information about player and environment
 */
class Map{
    private enum Directions { NORTH, EAST, SOUTH, WEST }
    private enum PlayerMovements {FORWARD, LEFT, RIGHT, BACKWARD}
    private int width;
    private int height;
    private Directions playerFront;
    private SimpleEntry<Integer, Integer> currentLocation;
    private SimpleEntry<Integer, Integer> exitLocation;
    // @TODO: Add more environment descriptions text
    // private bool nextToWall;
    // private bool inCorner;

    /**
     * @brief Disabled Map default constructor
     */
    private Map(){}

    /**
     * @brief Map constructor that requires the maps dimensions
     * @param width The width the map will be made with
     * @param height The height the map will be made with
     */
    public Map(int width, int height){
        this.width = width;
        this.height = height;
        this.playerFront = Directions.SOUTH;
        this.currentLocation = new SimpleEntry<>(0, 0);
        this.exitLocation = new SimpleEntry<>(width - 1, height - 1);
    }

    /**
     * @TODO
     * @brief Function to update player information based on a requested move action
     * @apiNote Warning this function does not do bounds checking for you
     */
    public void move(PlayerMovements myMove){

    }

    /**
     * @brief Function to check if the player is at the exit door
     * @return boolean based on if the player is or is not at the exit door
     */
    public Boolean checkExit(){
        if(this.currentLocation == this.exitLocation){
            return true;
        }
        return false;
    }
}
