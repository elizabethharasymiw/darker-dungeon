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

        myGame.printStartScreen();

        while(trapped){
            String userRawInput = scanner.nextLine();
            GameState.MenuOptions playerAction = myGame.parseUserInput(userRawInput);
            myGame.clearScreen();
            trapped = myGame.doPlayerAction(playerAction);
            myGame.printScreen(playerAction);
        }

        scanner.close();
    }
}