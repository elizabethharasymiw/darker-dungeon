/**
 * @file GameState.java
 * @author Elizabeth Harasymiw
 */

package com.mypackage;

import com.mypackage.Map;
import java.util.Arrays;

/**
 * @brief GameState class that holds game state information about the program
 */
public class GameState{
    public enum MenuOptions { ZERO, DONOTHING, FORWARD, TURNLEFT, TURNRIGHT, OPENDOOR, END}
    boolean[] unlockedOptions;
    private Map myMap;
    int doNothingCount;
    int progress_bar_length = 25;
    int startingPlayerExitDistance;
    int currentPlayerExitDistance;

    /**
     * @brief GameState constructor that requires a starting map
     * @param myMap Starting map
     */
    public GameState(){
        this.unlockedOptions = new boolean[GameState.MenuOptions.END.ordinal()];
        Arrays.fill(unlockedOptions, true);
        this.myMap = new Map();
        this.doNothingCount = 0;
        this.startingPlayerExitDistance = myMap.getShortestDistanceExit();
        this.currentPlayerExitDistance = myMap.getShortestDistanceExit();
    }

    /**
     * @brief Function to print the starting Game message
     */
    public void printStartMessage(){
        clearScreen();
        System.out.println();
        printASCIIkey();

        System.out.println();
        System.out.println(" You wake up to an unfamilar dark room...");
        System.out.println(" You find a dimly lit glowing key in your hand.");
        System.out.println(" You hear a mysterious voice say \"find the door\".");

        updateMenu();
        printMenu();
    }

    /**
     * @brief Function to print out the current available player options
     */
    public void printMenu(){

        System.out.println();
        System.out.println(" " + "What would you like to do?");

        for(int i = 0; i < GameState.MenuOptions.END.ordinal(); i++){
            MenuOptions index = MenuOptions.values()[i];
            if(unlockedOptions[i]){
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

        if(unlockedOptions[playerAction.ordinal()] == false){
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

        if(lastPlayerAction != MenuOptions.OPENDOOR){
            updateMenu();
            printMenu();
        }
    }

    /**
     * @brief Function to update what options the user has avaiable to them
     */
    public void updateMenu(){
        unlockedOptions[MenuOptions.OPENDOOR.ordinal()] = myMap.checkExit();
        unlockedOptions[MenuOptions.FORWARD.ordinal()] = myMap.checkPlayerFORWARD();
        unlockedOptions[MenuOptions.DONOTHING.ordinal()] = doNothingCheck();
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