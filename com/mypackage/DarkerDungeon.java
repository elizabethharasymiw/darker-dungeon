/**
 * @file DarkerDungeon.java
 * @brief Java Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

package com.mypackage;

import com.mypackage.GameState;
import java.util.Scanner;

/**
 * @brief Main class of the program, contains high level program structure
 */
public class DarkerDungeon{
    public static void main(String[] args){
        GameState myGame = new GameState();
        Scanner scanner = new Scanner(System.in);
        Boolean trapped = true;

        myGame.clearScreen();
        System.out.println();
        myGame.printASCIIkey();

        System.out.println();
        System.out.println(" You wake up to an unfamilar dark room...");
        System.out.println(" You find a dimly lit glowing key in your hand.");
        System.out.println(" You hear a mysterious voice say \"find the door\".");

        while(trapped){
            myGame.updateMenu();
            myGame.printMenu();
            String userRawInput = scanner.nextLine();
            GameState.MenuOptions playerAction = myGame.parseUserInput(userRawInput);
            myGame.clearScreen();
            trapped = myGame.doPlayerAction(playerAction);
            myGame.printScreen(playerAction);
        }

        scanner.close();
    }
}