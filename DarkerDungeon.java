import java.util.Scanner;

public class main{

    public static void main(String[] args){

        System.out.println("You wake up to pitch blackness");
        System.out.println("You have found a Door");

        enum menuOptions { ZERO, OPENDOOR, DONOTHING, END}

        Scanner scanner = new Scanner(System.in);

        Boolean trapped = true;

        while(trapped){

            System.out.println("What would you like todo?");
            System.out.println("1: Open Door");
            System.out.println("2: Do Nothing");

            // Get the user's answer
            String userInput = scanner.nextLine();

            // Get the user's number from their answer
            int userNumber;
            try {
                userNumber = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                userNumber = 0;
            }


            // Convert users number into a menu option
            menuOptions userOption = menuOptions.ZERO;
            if(userNumber > menuOptions.ZERO.ordinal() && userNumber < menuOptions.END.ordinal())
                userOption = menuOptions.values()[userNumber];

            // Respond to choosen user option
            switch(userOption){
                case OPENDOOR:
                    System.out.println("As you open the door you feel the warmth of the sun");
                    System.out.println("Congratulations! You Escape");
                    trapped = false;
                    break;
                case DONOTHING:
                    System.out.println("You do nothing for a while");
                    break;
                default:
                    System.out.println("You can't do that");
                    break;
            } // Switch End

        } // Trapped While Loop End

        scanner.close();

    } // Void Main End

} // Class Main End
