import java.util.Objects;

public class Node {

    String value;
    int occurrences;
    Node left;
    Node right;
    Node parent;

    public Node(String value, int occurrences) {
        this.value = value;
        this.occurrences = occurrences;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }


    public void deleteItself() {
        if (this.parent == null) return;
        if (this.parent.hasLeft() && this.parent.left.equals(this)) {
            this.parent.left = null;
        } else {
            this.parent.right = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return occurrences == node.occurrences && value.equals(node.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, occurrences);
    }


    public String toString() {
        return this.value + " -> " + this.occurrences + "\n";
    }


    public void cleanNode() {
        this.parent = null;
        this.left = null;
        this.right = null;
    }

}