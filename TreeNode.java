import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private List<TreeNode> adjacent;
    private String text;
    private TreeNode parent;
  
    public TreeNode(String text) {
      this.text = text;
      this.adjacent = new ArrayList<>();
    }
  
    public void addAdjacent(TreeNode node) {
      adjacent.add(node);
      node.parent = this; 
    }
  
    public void removeAdjacent(TreeNode node) {
      adjacent.remove(node);
    }
  
    public String getText() {
      return text;
    }
  
    public List<TreeNode> getChildren() {
      return adjacent; 
    }
  
    public void setParent(TreeNode parent) {
      this.parent = parent;
    }
  
    public TreeNode getParent() {
      return parent;
    }
  
  }