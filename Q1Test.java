import java.util.*;

public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     } 

      // write your codes here
     static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
     }

     public static String verify(String traversal1, String traversal2, String traversal3) {

        String[] inputs = {
            traversal1, traversal2, traversal3
        };

        int[][] sequences = new int[3][];

        // Convert each string into an integer array
        try {
            for (int i = 0; i < 3; i++) {
                String[] parts = inputs[i].split("-");
                sequences[i] = new int[parts.length];

                for (int j = 0; j < parts.length; j++) {
                    sequences[i][j] = Integer.parseInt(parts[j]);
                }
            }
        } catch (Exception e) {
            return "Invalid traversals";
        }

        // Check that all traversals contain exactly the same unique nodes
        Set<Integer> firstSet = new HashSet<>();

        for (int value : sequences[0]) {
            if (!firstSet.add(value)) {
                return "Invalid traversals";
            }
        }

        for (int i = 1; i < 3; i++) {
            Set<Integer> currentSet = new HashSet<>();

            for (int value : sequences[i]) {
                if (!currentSet.add(value)) {
                    return "Invalid traversals";
                }
            }

            if (!currentSet.equals(firstSet)) {
                return "Invalid traversals";
            }
        }

        // Try each traversal as inorder
        for (int in = 0; in < 3; in++) {

            int[] inorder = sequences[in];

            // A BST's inorder traversal must be increasing
            boolean isSorted = true;

            for (int i = 1; i < inorder.length; i++) {
                if (inorder[i - 1] >= inorder[i]) {
                    isSorted = false;
                    break;
                }
            }

            if (!isSorted) {
                continue;
            }

            // Try each remaining traversal as preorder
            for (int pre = 0; pre < 3; pre++) {

                if (pre == in) {
                    continue;
                }

                // The remaining traversal must be postorder
                int post = 3 - in - pre;

                int[] preorder = sequences[pre];
                int[] postorder = sequences[post];

                // Reconstruct the tree from inorder + preorder
                int[] preIndex = {0};

                Node root = buildTree(
                        inorder,
                        preorder,
                        0,
                        inorder.length - 1,
                        preIndex
                );

                if (preIndex[0] != preorder.length) {
                    continue;
                }

                // Generate the reconstructed tree's postorder
                List<Integer> generated = new ArrayList<>();

                getPostorder(root, generated);

                // Compare generated postorder with the input
                if (generated.size() != postorder.length) {
                    continue;
                }

                boolean matches = true;

                for (int i = 0; i < postorder.length; i++) {
                    if (generated.get(i) != postorder[i]) {
                        matches = false;
                        break;
                    }
                }

                // All three traversals are valid
                if (matches) {
                    String[] names = new String[3];

                    names[in] = "Inorder";
                    names[pre] = "Preorder";
                    names[post] = "Postorder";

                    return "Traversal 1 - " + names[0]
                            + ", Traversal 2 - " + names[1]
                            + ", Traversal 3 - " + names[2];
                }
            }
        }

        return "Invalid traversals";
    }

    // Reconstruct a binary tree using inorder and preorder
    private static Node buildTree(int[] inorder, int[] preorder, int left,  int right, int[] preIndex) {

        if (left > right) {
            return null;
        }

        if (preIndex[0] >= preorder.length) {
            return null;
        }

        int rootValue = preorder[preIndex[0]++];

        // Find the root in the inorder array
        int position = left;

        while (position <= right
                && inorder[position] != rootValue) {
            position++;
        }

        // Root does not belong to this subtree
        if (position > right) {
            preIndex[0]--;
            return null;
        }

        Node root = new Node(rootValue);

        // Recursively construct the left subtree
        root.left = buildTree(
                inorder,
                preorder,
                left,
                position - 1,
                preIndex
        );

        // Recursively construct the right subtree
        root.right = buildTree(
                inorder,
                preorder,
                position + 1,
                right,
                preIndex
        );

        return root;
    }

    // Generate postorder: Left -> Right -> Root
    private static void getPostorder(
            Node root,
            List<Integer> result) {

        if (root == null) {
            return;
        }

        getPostorder(root.left, result);
        getPostorder(root.right, result);

        result.add(root.value);
    }
}