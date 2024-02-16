import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;

public class Game {

    // Create all nodes first
    public static TreeNode root = new TreeNode("You wake up in a labyrinth-like maze with no exit in sight. Enter 1 or 'A' to turn left, 2 or 'D' to turn right.");
    public static TreeNode toSkeleton = new TreeNode("You turn left and enter a room. You see a skeleton in the corner. It has a shining red ring on its finger and a dagger in its other hand. Enter 1 or 'A' to take the ring, 2 or 'W' to continue forward and ignore it.");
    public static TreeNode tryTakeRing = new TreeNode("You walk up to the skeleton and attempt to take the ring. The skeleton suddenly awakens and raises its dagger, cutting you across your right shoulder. You are now injured. Enter 1 or 'A' attack the skeleton, 2 or 'W' to continue past it.");
    public static TreeNode attackSkeleton = new TreeNode("You attack the skeleton. It crumbles under your blow, turning to dust. Only the red ring remains on the ground. Press 1 or 'A' to collect it and continue forward.");
    public static TreeNode takeRing = new TreeNode("You grab the ring from the ground. You will now have the ability to cast a fireball during a fight. Enter 1 or 'A' to continue");
    public static TreeNode moveToStatueRoom = new TreeNode("You enter a room containing three different statues. The first is a dolphin, the second an eagle, and the third a snake. Each statue has a button in front of it, and a large lever sits to the right of the statues. Press 1 or 'A' to continue.");
    public static TreeNode leverIncorrect = new TreeNode("Incorrect! A trap activates and you are killed by its arrow.");
    public static TreeNode leverCorrect = new TreeNode("You solved the puzzle! The middle statue turns and reveals a bright red healing potion. You are no longer injured! Enter 1 or 'A' to continue");
    public static TreeNode backToStart = new TreeNode("The next path leads you back to the start of the labyrinth. Enter 1 or'A' to go right, enter 2 or 'W' to go left.");
    public static TreeNode alreadyVisited = new TreeNode("You've already explored everything this way, press 1 or 'A' to return to the beginning of the maze.");
    public static TreeNode toChest = new TreeNode("You enter a room that is completely empty except for a chest in the center of the room. Enter 1 'A' to open the chest, 2 or 'W' to kick the chest");
    public static TreeNode openChest = new TreeNode("The chest was a mimic! It leaps and attacks you. You are now injured. Enter 1 or 'A' to kick it.");
    public static TreeNode hitChest = new TreeNode("The kick shatters the mimic, leaving an orange potion within the scattered pieces of wood. Gain one potion of strength. Enter 1 or 'A' to turn around and continue down the hallway.");
    public static TreeNode door = new TreeNode("You see a set of iron double doors decorated with skulls, and both with iron handles. Enter 1 or 'A' to open the doors and continue.");
    public static TreeNode EnterBossRoom = new TreeNode("You see a giant minotaur standing on an iron throne, with an enormous axe standing next to him.  Enter 1 or 'A' to continue by attacking without modifications. If you have a potion of strength, enter 2 or 'W' to use it and attack the minotaur. If you have a fireball attack and wish to use it, enter 3 or 'D'.");
    public static TreeNode ringOfFireball = new TreeNode("The minotaur has a weakness to fire, and your fireball catches him off guard and burns him to a crisp. Enter 1 or 'A' to proceed.");
    public static TreeNode attackEmpower = new TreeNode("Your sword is filled with orange light. Your strike cuts through the minotaur's axe and severs it in two. Enter 1 or 'A' to proceed.");
    public static TreeNode attackNotEmpower = new TreeNode("Your swing is too quick for the minotaur and you are able to injure it. However, it strikes back and you are injured. Enter 1 or 'A' to try to run, 2 or 'W' to attack again.");
    public static TreeNode tryToRun = new TreeNode("You try to run, but the minotaur attacks you again.");
    public static TreeNode attackAgain = new TreeNode("Although severely wounded, you are able to defeat the minotaur. Enter 1 or 'A' to proceed");
    public static TreeNode winFight = new TreeNode("Directly behind the throne is a chest filled with gold and a door that leads to the outside. Press 1 or 'A' to Finish the Game.");

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Create player
        System.out.print("Enter a name for your player: ");
        Player p = new Player(s.nextLine());
        System.out.print("\n");
        System.out.println("Enter \"inventory\" to open your inventory.");
        // Run game
        buildTree();
        TreeNode curr = root; 
    
        while(curr != null) {
            System.out.println(curr.getText());

            //read input

            String input = s.next();
            System.out.println("==========================================================");
            if (!isInt(input)) //is string
            {
                if (input.toLowerCase().equals("inventory"))
                {
                    System.out.println(p.getInv());
                }
                if (input.toLowerCase().equals("a")){
                    curr = turn(p, curr, s, 1);
                }
                if (input.toLowerCase().equals("w")){
                    curr = turn(p, curr, s, 2);
                }
                if (input.toLowerCase().equals("d")){
                    curr = turn(p, curr, s, 3);
                }
                else
                {
                    System.out.println("I don't understand that command.");
                }
            }
            else if(isInt(input)){
                if(curr.equals(moveToStatueRoom)) { //if currentRoom is this
                    //System.out.println(moveToStatueRoom.getText());
                    if(playPuzzle(s,p) == 0) {
                        curr = leverIncorrect;
                        break;
                    }
                    else {
                        curr = leverCorrect;
                        System.out.println(curr.getText());
                    }
                }
                if(curr == attackSkeleton){
                    p.setRing();
                    
                }
                if(curr == openChest){
                    p.setEmpowered();
                }
                if(curr == EnterBossRoom && p.getRing() == false){
                    EnterBossRoom.removeAdjacent(ringOfFireball);
                }
                if(curr == EnterBossRoom && p.getEmpowered() == false){
                    EnterBossRoom.removeAdjacent(attackEmpower);
                }
                if(curr == winFight){
                    System.out.print("Congratulations, "+p.getName()+", you win!\n");
                }
                curr = turn(p, curr, s, Integer.parseInt(input));
            }
            else{
                System.out.println("Invalid Input");
                continue;
            }
        }

        if(curr == leverIncorrect) {
            System.out.println(leverIncorrect.getText());
        }

        System.out.println("Game Over!");
        
        s.close();
    }

    /*
     * Build game decision tree
     * @return The root of the tree
     */
    public static void buildTree() {
        // Root level
        root.addAdjacent(toSkeleton);
        root.addAdjacent(toChest);

        // Level 1 - left path (Skeleton)
        toSkeleton.addAdjacent(tryTakeRing);
        toSkeleton.addAdjacent(moveToStatueRoom);

        // Level 1 - right path (Chest)
        toChest.addAdjacent(openChest);
        toChest.addAdjacent(hitChest);

        // Level 2 - Skeleton battle or statue
        tryTakeRing.addAdjacent(attackSkeleton);
        tryTakeRing.addAdjacent(moveToStatueRoom);

        // Level 2 - statue room
        moveToStatueRoom.addAdjacent(leverIncorrect);
        moveToStatueRoom.addAdjacent(leverCorrect);

        // Skeleton attack
        attackSkeleton.addAdjacent(takeRing);
        attackSkeleton.addAdjacent(moveToStatueRoom);

        // Ring
        takeRing.addAdjacent(moveToStatueRoom);
     
        // Lever Correct
        leverCorrect.addAdjacent(backToStart);

        // Back to Start
        backToStart.addAdjacent(toChest);
        backToStart.addAdjacent(alreadyVisited);

        //Already Visited 
        alreadyVisited.addAdjacent(backToStart);

        // Chest Room
        toChest.addAdjacent(openChest);
        toChest.addAdjacent(hitChest);
        openChest.addAdjacent(hitChest);

        // End Chest
        //openChest.addAdjacent(door);
        hitChest.addAdjacent(door);

        door.addAdjacent(EnterBossRoom);

        // Boss
        EnterBossRoom.addAdjacent(attackNotEmpower);  
        EnterBossRoom.addAdjacent(attackEmpower);
        EnterBossRoom.addAdjacent(ringOfFireball);
        
        // Fireball
        ringOfFireball.addAdjacent(winFight);
        
         
        // Attack empower
        attackEmpower.addAdjacent(winFight); 

         // Not empowered
        attackNotEmpower.addAdjacent(tryToRun);
        attackNotEmpower.addAdjacent(attackAgain);

        // Attack again
        attackAgain.addAdjacent(winFight);
    }

    /**
     * Play statue puzzle
     * @param s Scanner for input
     * @return Integer based on result; 0 for loss, 1 for win
     */
    public static int playPuzzle(Scanner s, Player p) {
        int left = 0;
        int mid = 1;
        int right = 2;
        Queue<Integer> qLeft = new LinkedBlockingQueue<Integer>();
        Queue<Integer> qMid = new LinkedBlockingQueue<Integer>();
        Queue<Integer> qRight = new LinkedBlockingQueue<Integer>();
        

        // Set queues
        qLeft.offer(1);
        qLeft.offer(2);

        qMid.offer(2);
        qMid.offer(0);

        qRight.offer(0);
        qRight.offer(1);

        int choice = 0;
        while(choice != 4) {
            choice = 0; //reset choice
            System.out.println("Press 1 or 'A' for button in front of left statue, 2 or 'W' for middle, 3 or 'D' for right. To pull the lever, press 4");
            System.out.printf("Current Layout: %s %s %s%n", statueType(left), statueType(mid), statueType(right));
            String inputChoice = s.next();

            //ability to check inv during the puzzle sequence
            if(isInt(inputChoice))
            {
                choice = Integer.parseInt(inputChoice);
            }
            else
            {
                if (inputChoice.toLowerCase().equals("inventory"))
                {
                    System.out.println(p.getInv());                    
                }
                if (inputChoice.toLowerCase().equals("a")){
                    choice = 1;
                }
                if (inputChoice.toLowerCase().equals("w"))
                {
                    choice = 2;
                }
                if (inputChoice.toLowerCase().equals("d"))
                {
                    choice = 3;
                }
                else
                {
                    System.out.println("I don't understand that command.");
                }
            }
            if(choice == 1) {
                qLeft.offer(left);
                left = qLeft.poll();
            }
            else if(choice == 2) {
                qMid.offer(mid);
                //System.out.println(qMid.peek());
                mid = qMid.poll();
            }
            else if(choice == 3) {
                qRight.offer(right);
                right = qRight.poll();
            }
        }

        if(left == mid && left == right && mid == right) {
            return 1;
        }

        return 0;
    }

    public static String statueType(int val) {
        if(val == 0) return "Dolphin";
        if(val == 1) return "Eagle";
        return "Snake";
    }

    /*
     * Player's turn; player will choose an option, and we will update the current node based on the decision
     * @param p The player
     * @param curr The node we are currently at
     * @param inputChoice int of user input
     * @return Result node
     */
    
    public static TreeNode turn(Player p, TreeNode curr, Scanner s, int inputChoice) {
        if(curr.getChildren().size() == 0) {
            return null;
        }

        //System.out.println(curr.getText());
       
        if(curr.getText().contains("injured")) {
            if(!curr.getText().contains("no longer")) {
                if(p.hurt()) {
                    System.out.println("You have been fatally wounded.");
                    return null;
                }
    
                p.injure();
            }
            else {
                p.heal();
            }
        }
       
        while(inputChoice < 1 || inputChoice > curr.getChildren().size()) {
            System.out.println("Please enter a valid choice.");
            inputChoice = s.nextInt();
        }

        return curr.getChildren().get(inputChoice-1);
    }
    public static boolean isInt(String s)
    {
        try
        {
            Integer.parseInt((s));
            return true;
        }
        catch (Exception e) 
        {
            return false;
        }
    }

}