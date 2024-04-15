/**
 * @file DarkerDungeon.java
 * @brief Java Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

package com.mypackage;

import com.mypackage.Map;
import com.mypackage.GameState;
import java.util.Scanner;

/**
 * @brief Main class of the program, contains high level program structure
 */
public class DarkerDungeon{
    public static void main(String[] args){
        Map myMap = new Map();
        GameState myMenu = new GameState(myMap);
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
            GameState.MenuOptions playerAction = myMenu.parseUserInput(userRawInput);
            myMenu.clearScreen();
            trapped = myMenu.doPlayerAction(playerAction);
            myMenu.printScreen(playerAction);
        }

        scanner.close();
    }
}