import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Queue;

public class Game {

    // Create all nodes first
    public static TreeNode root = new TreeNode("You wake up in a labyrinth-like maze with no exit in sight. Enter 1 to turn left, 2 to turn right.");
    public static TreeNode toSkeleton = new TreeNode("You turn left and enter a room. You see a skeleton in the corner. It has a shining red ring on its finger and a dagger in its other hand. Enter 1 to take the ring, 2 to continue forward and ignore it.");
    public static TreeNode tryTakeRing = new TreeNode("You walk up to the skeleton and attempt to take the ring. The skeleton suddenly awakens and raises its dagger, cutting you across your right shoulder. You are now injured. Enter 1 to attack the skeleton, 2 to continue past it.");
    public static TreeNode attackSkeleton = new TreeNode("You attack the skeleton. It crumbles under your blow, turning to dust. Only the red ring remains on the ground.");
    public static TreeNode takeRing = new TreeNode("You grab the ring from the ground. You will now have the ability to cast a fireball during a fight. Enter 1 to continue");
    public static TreeNode moveToStatueRoom = new TreeNode("You enter a room containing three different statues. The first is a dolphin, the second an eagle, and the third a snake. Each statue has a button in front of it, and a large lever sits to the right of the statues.");
    public static TreeNode leverIncorrect = new TreeNode("Incorrect! A trap activates and you are killed by its arrow.");
    public static TreeNode leverCorrect = new TreeNode("You solved the puzzle! The middle statue turns and reveals a bright red healing potion. You are no longer injured! Enter 1 to continue");
    public static TreeNode backToStart = new TreeNode("The next path leads you back to the start of the labyrinth. Enter 1 to go right, enter 2 to go left.");
    public static TreeNode toChest = new TreeNode("You enter a room that is completely empty except for a chest in the center of the room. Enter 1 to open the chest, 2 to kick the chest");
    public static TreeNode openChest = new TreeNode("The chest was a mimic! It leaps and attacks you. You are now injured. Enter 1 to continue");
    public static TreeNode hitChest = new TreeNode("The chest falls back to reveal it was a mimic, but the kick broke it completely, leaving an orange potion within the scattered pieces of wood. Gain one potion of strength. Enter 1 to turn around and continue down the hallway.");
    public static TreeNode EnterBossRoom = new TreeNode("You see a giant minotaur standing on an iron throne, with an enormous axe standing next to him.");


    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Create player
        System.out.print("Enter a name for your player: ");
        Player p = new Player(s.nextLine());
        System.out.print("\n");

        // Run game
        buildTree();
        TreeNode curr = root;

        while(curr != null) {
            if(curr.equals(moveToStatueRoom)) {
                System.out.println(moveToStatueRoom.getText());
                if(playPuzzle(s) == 0) {
                    curr = leverIncorrect;
                    break;
                }
                else {
                    curr = leverCorrect;
                }
            }

            curr = turn(p, curr, s);
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
    }

    /**
     * Play statue puzzle
     * @param s Scanner for input
     * @return Integer based on result; 0 for loss, 1 for win
     */
    public static int playPuzzle(Scanner s) {
        int left = 0;
        int mid = 1;
        int right = 2;
        Queue<Integer> qLeft = new LinkedBlockingQueue<Integer>();
        Queue<Integer> qMid = new LinkedBlockingQueue<Integer>();
        Queue<Integer> qRight = new LinkedBlockingQueue<Integer>();
        int choice = 0;

        // Set queues
        qLeft.offer(1);
        qLeft.offer(2);

        qMid.offer(2);
        qMid.offer(0);

        qRight.offer(0);
        qRight.offer(1);

        while(choice != 4) {
            System.out.println("Press 1 for button in front of left statue, 2 for middle, 3 for right. To pull the lever, press 4");
            System.out.printf("Current Layout: %s %s %s%n", statueType(left), statueType(mid), statueType(right));
            choice = s.nextInt();

            if(choice == 1) {
                qLeft.offer(left);
                left = qLeft.poll();
            }
            else if(choice == 2) {
                qMid.offer(mid);
                System.out.println(qMid.peek());
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
     * @param s Scanner for user input
     * @return Result node
     */
    public static TreeNode turn(Player p, TreeNode curr, Scanner s) {
        if(curr.getChildren().size() == 0) {
            return null;
        }

        System.out.println(curr.getText());

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

        int choice = s.nextInt();

        while(choice < 1 || choice > curr.getChildren().size()) {
            System.out.println("Please enter a valid choice.");
        }

        return curr.getChildren().get(choice-1);
    }
}