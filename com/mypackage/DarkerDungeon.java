/**
 * @file DarkerDungeon.java
 * @brief Java Game Coding Challenge
 * @author Elizabeth Harasymiw
 */

package com.mypackage;

import com.mypackage.Map;
import com.mypackage.Menu;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @brief Main class of the program, contains high level program structure
 */
public class DarkerDungeon{
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