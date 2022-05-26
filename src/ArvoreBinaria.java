import java.util.*;

public class ArvoreBinaria {

    private Node root;

    public ArvoreBinaria() {
    }

    public Node getRoot() {
        return root;
    }

    /**
     * This method will add a key to the bst. If it already exists, it will increment the number of occurences
     */
    public void addNode(String value) {
        if (root == null) {
            root = new Node(value, 1);
        } else {
            Node finger = root;
            boolean put = false;
            while (finger.hasLeft() || finger.hasRight()) {

                if (finger.getValue().equals(value)) {
                    finger.setOccurrences(finger.getOccurrences() + 1);
                    put = true;
                    return;
                }

                if (!finger.hasLeft()) {
                    if (value.compareTo(finger.getValue()) > 0) {
                        finger = finger.right;
                    } else {
                        Node toAdd = new Node(value, 1);
                        toAdd.parent = finger;
                        finger.left = toAdd;
                        put = true;
                    }
                } else if (!finger.hasRight()) {
                    if (value.compareTo(finger.getValue()) < 0) {
                        finger = finger.left;
                    } else {
                        Node toAdd = new Node(value, 1);
                        toAdd.parent = finger;
                        finger.right = toAdd;
                        put = true;
                    }
                } else {
                    if (value.compareTo(finger.getValue()) > 0) {
                        finger = finger.right;
                    } else {
                        finger = finger.left;
                    }
                }
            }
            if(finger.value.equals(value) ) {
                finger.setOccurrences(finger.getOccurrences() + 1);
                return;
            }
            if (!put) {
                if (value.compareTo(finger.getValue()) > 0) {
                    Node toAdd = new Node(value, 1);
                    toAdd.parent = finger;
                    finger.right = toAdd;
                } else {
                    Node toAdd = new Node(value, 1);
                    toAdd.parent = finger;
                    finger.left = toAdd;
                }
            }
        }
    }

    /**
     * This method will remove a certain key from the tree if it exists
     * @param value the key to remove
     * @param node the auxiliary node to help the traversal
     * @return wheather the key was removed or not
     */
    public boolean removeNode(String value, Node node) {
        if (node == null) return false;
        if (value.compareTo(node.getValue()) < 0) {
            return removeNode(value, node.left);
        } else if (value.compareTo(node.getValue()) > 0) {
            return removeNode(value, node.right);
        } else {
            if (node.hasLeft() && node.hasRight()) {
                Node minimum = min(node.right);
                node.setValue(minimum.getValue());
                node.setOccurrences(minimum.getOccurrences());
                return removeNode(minimum.getValue(), node.right);
            } else {
                if (!node.hasRight() && !node.hasLeft()) {
                    if (node == root) root = null;
                    node.deleteItself();
                } else {
                    Node deleteNode = node;
                    node = (node.hasLeft()) ? (node.left) : (node.right);
                    if (deleteNode.equals(root)) root = node;
                    node.parent = deleteNode.parent;
                    if (node.parent != null) {
                        if (node.parent.hasLeft() && node.parent.left.equals(deleteNode)) {
                            node.parent.left = node;
                        } else node.parent.right = node;
                    }
                    deleteNode.left = null;
                    deleteNode.right = null;
                }
            }
        }
        return true;
    }


    /**
     * This method will return the Node with the minimum value in a certain subtree
     * @param node the root of the subtree
     * @return the node with the minimum value of the subtree
     */
    public Node min(Node node) {
        if (!node.hasLeft()) {
            return node;
        }
        return min(node.left);
    }

    /**
     * This method will all the nodes that match with the pattern provided. It will do so through a dfs traversal of the
     * tree
     * @param value the pattern to match for
     * @return the list of nodes that have a key with value that matches such pattern
     */
    public List<Node> getNodes(String value) {
        Stack<Node> stack = new Stack<>();
        List<Node> toReturn = new ArrayList<>();
        if (root == null) return toReturn;
        stack.push(root);
        while (!stack.empty()) {
            Node inspect = stack.pop();
            if (inspect.getValue().contains(value)) {
                toReturn.add(inspect);
            }
            if (inspect.hasRight()) stack.push(inspect.right);
            if (inspect.hasLeft()) stack.push(inspect.left);
        }
        return toReturn;
    }


    /**
     * This method will balance the bst with the Day Stout-Warren algorithm
     */
    public void balance() {
        if (root == null) return;
        LinkedList<Node> backboneList = inOrderTraversal(root);
        int n = backboneList.size();
        int m = (int) Math.pow(2, (int) (Math.log(n) / Math.log(2))) - 1;
        Node backboneRoot = getBackBone(backboneList);
        root = backboneRoot;
        manageRotations(backboneRoot, n - m);
        while (m > 0) {
            m = m / 2;
            manageRotations(root, m);
        }
        getParents(root);
    }

    /**
     * This method will reestablish all the correct references between children and parents
     * @param node the auxiliary node to help the traversal
     */
    public void getParents(Node node) {
        if (node.hasLeft()) {
            node.left.parent = node;
            getParents(node.left);
        }
        if (node.hasRight()) {
            node.right.parent = node;
            getParents(node.right);
        }
    }

    /**
     * This metodo will perform a certain number of left rotation in the odd-positioned nodes in the right branch of
     * the tree. Lookup how Day Stout-Warren works to better understand
     * @param node the root of the tree
     * @param times the number of left rotations
     */
    public void manageRotations(Node node, int times) {
        List<Node> listOfNodes = new ArrayList<>();
        Node finger = node;
        int i = 1;
        while (listOfNodes.size() < times) {
            if (i % 2 != 0) {
                listOfNodes.add(finger);
            }
            finger = finger.right;
            i++;
        }
        for (Node n : listOfNodes) {
            leftRotate(n);
        }
    }

    /**
     * This method will perform a left rotation on a certain node
     * @param node to perform the left rotation on
     */
    public void leftRotate(Node node) {
        Node right = node.right;
        if (right != null) {
            String tempValue = node.value;
            int tempOccurences = node.occurrences;
            node.value = right.value;
            node.occurrences = right.occurrences;
            right.value = tempValue;
            right.occurrences = tempOccurences;
            node.right = right.right;
            right.right = right.left;
            right.left = node.left;
            node.left = right;
        }
    }

    /**
     * This method will perform an in-order traversal of the tree and return it in a linked-list
     * @param node the root of the subtree
     * @return the linked-list
     */
    public LinkedList<Node> inOrderTraversal(Node node) {
        if (node == null) return new LinkedList<>();
        LinkedList<Node> traversalLeft = inOrderTraversal(node.left);
        LinkedList<Node> traversalRight = inOrderTraversal(node.right);
        traversalLeft.add(node);
        traversalLeft.addAll(traversalRight);
        return traversalLeft;
    }


    /**
     * This method will transform an in-order traversal of the tree (in a linked list) into a right branch tree
     * called the backbone
     * @param linkedList containing the in-order traversal
     * @return the Node representing the root of the tree
     */
    public Node getBackBone(LinkedList<Node> linkedList) {
        Node finger = linkedList.get(0);
        Node root = finger;
        root.cleanNode();
        for (int i = 1; i < linkedList.size(); i++) {
            Node toAdd = linkedList.get(i);
            toAdd.cleanNode();
            finger.right = toAdd;
            finger = toAdd;
        }
        return root;
    }
}